package cn.juhe.zjsb.task;

import cn.juhe.zjsb.HelloController;
import cn.juhe.zjsb.util.RandomBirthdayGenerator;
import cn.juhe.zjsb.entity.APIResult;
import cn.juhe.zjsb.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.scene.image.Image;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MyJob implements Job {
    public static String APPKEY;
    private static final Logger log = LoggerFactory.getLogger(MyJob.class);
    public static Queue<String> fileQueue = new LinkedList<>();
    public static ArrayList<String> names;
    public static ArrayList<String> address;
    public static ArrayList<String> idcards;
    public static ArrayList<String> birthdays;

    static {
        names = ChineseNameGenerator.generateRandomNames(100);
        address = ChineseAddressGenerator.generateRandomAddresses(200);
        idcards = ChineseIDCardGenerator.generateRandomIDCards(200);
        birthdays = RandomBirthdayGenerator.generateRandomBirthdays(200, 1932, 2023);
    }

    @Override
    public void execute(JobExecutionContext context) {

        DateTimeFormatter df = DateTimeFormatter.ofPattern("【yyyy-MM-dd HH:mm:ss】");
        String format = LocalDateTime.now().format(df);
        String s = fileQueue.poll();
        if (s==null){
            try {
                TaskManger.shutdown();
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        EventBusUtil.getDefault().post(format + "-->正在识别文件：" + s + "\n");
        APIResult data = new APIResult();
        data.setFilename(s);
        if (HelloController.RUN_MODEL.equals(HelloController.RUN_MODEL_REAL)) {
            packageDataReal(s, data);
        } else {
            assert s != null;
            packageDataTest(s, data);
        }
        EventBusUtil.getDefault().post(data);
        EventBusUtil.getDefault().post(format + s + "识别完成<--" + "\n");


    }

    private static void packageDataTest(String s, APIResult data) {
        data.setName(ChineseNameGenerator.getRandomName(names));
        data.setAddress(ChineseAddressGenerator.getRandomAddress(address));
        data.setIdno(ChineseIDCardGenerator.getRandomIDCard(idcards));
        data.setBirthday(RandomBirthdayGenerator.getRandomBirthday(birthdays));
        assert s != null;
        if (s.endsWith("png") || s.endsWith("jpg") || s.endsWith("jpeg")) {
            data.setHeadImg(new Image(new File(s).toURI().toString()));
        }
        data.setSex(System.currentTimeMillis() % 2 == 0 ? "男" : "女");
        data.setMinzu(ChineseEthnicGroupGenerator.getRandomEthnicGroup(new ArrayList<>(ChineseEthnicGroupGenerator.ethnicGroups)));
    }

    private static void packageDataReal(String s, APIResult data) {
        try {
            String resp = CertificateUtil.post("2", new File(s), MyJob.APPKEY);
            data.setResponse(resp);
            System.out.println("接口返回："+resp);
            JSONObject jsonObject = JSON.parseObject(resp);
            // 提取每个键的值
            int errorCode = jsonObject.getIntValue("error_code");
            String reason = jsonObject.getString("reason");
            JSONObject result = jsonObject.getJSONObject("result");
            String address = result.getString("住址");
            String reserved = result.getString("保留");
            String idCard = result.getString("公民身份号码");
            String birthdate = result.getString("出生");
            String avatar = result.getString("头像");
            String name = result.getString("姓名");
            String gender = result.getString("性别");
            String ethnicGroup = result.getString("民族");
            String orderId = result.getString("orderid");
            String userId = result.getString("userid");
            data.setError_code(errorCode);
            data.setReason(reason);
            data.setAddress(address);
            data.setIdno(idCard);
            data.setBirthday(birthdate);
            byte[] imageBytes = Base64.getDecoder().decode(avatar);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            Image image = new Image(inputStream);
            data.setHeadImg(image);
            data.setName(name);
            data.setSex(gender);
            data.setMinzu(ethnicGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
