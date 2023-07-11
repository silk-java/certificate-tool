package cn.juhe.zjsb;

import cn.juhe.zjsb.entity.APIResult;
import cn.juhe.zjsb.event.DownProgressEvent;
import cn.juhe.zjsb.task.TaskManger;
import cn.juhe.zjsb.util.EventBusUtil;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownLoadTask extends ScheduledService<Integer> {
    private static final Logger log = LoggerFactory.getLogger(DownLoadTask.class);
    String[] head = {"文件名","状态码","原因", "姓名", "性别","民族","出生","地址", "身份证号","接口返回" };
//接口返回：{"reason":"操作成功","result":{"保留":"","姓名":"陈厚德","性别":"男","民族":"汉","出生":"1938-09-07","住址":"江西省瑞金市壬田镇圳头村陈屋小组34号","公民身份号码":"362102193809072013","头像":""},"error_code":0}
    public String dirName;
    // 创建工作簿
    Workbook workbook = new XSSFWorkbook();
    public String filename;

    // 创建工作表
    Sheet sheet = workbook.createSheet("Sheet1");

    public DownLoadTask(int finished, String dirName, String fileName) {
        this.dirName = dirName;
        this.finished = finished;
        this.filename = fileName;
        System.out.println("fileName:" + fileName);
    }

    public int finished = 0;


    @Override
    protected Task<Integer> createTask() {
        return new Task<>() {
            @Override
            protected Integer call() {
                System.out.println("call");
                return finished;
            }

            @Override
            protected void updateValue(Integer value) {
                super.updateValue(value);
                System.out.println("当前完成：" + value);
                //这个list中的数据都是已经完成的，所以都需要写出
                ObservableList<APIResult> data1 = HelloController.list;
//                ArrayList<Map<String, Object>> list = new ArrayList<>();
                List list = new ArrayList<>();
                for (APIResult data : data1) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", data.getName());
                    map.put("filename", data.getFilename());
                    map.put("address", data.getAddress());
//                    map.put("birthday",data.getBirthday());
                    map.put("sex", data.getSex());
                    map.put("minzu", data.getMinzu());
                    map.put("idno", data.getIdno());
                }
                writeData(sheet, 0, 0, head);
                // 写入数据
                int written = 0;
                for (int i = 0; i < HelloController.list.size(); i++) {
                    written++;
                    APIResult data = HelloController.list.get(i);
                    //{"文件名","状态码","原因", "姓名", "性别","民族","出生","地址", "身份证号","接口返回" };
                    writeData(sheet, i+1, 0, new String[]{
                            data.getFilename(),
                            data.getError_code()+"",
                            data.getReason(),
                            data.getName(),
                            data.getSex(),
                            data.getMinzu(),
                            data.getBirthday(),
                            data.getAddress(),
                            data.getIdno(),
                            data.getResponse(),
                           });
                    if ((i + 1) % 10 == 0) {
                        // 每10行保存一次
                        saveWorkbook(workbook, filename, written);
                    }
                }
                // 最后保存一次，以确保所有数据都写入文件
                saveWorkbook(workbook, filename, written);
                // 关闭工作簿
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Excel文件写入完成。");
                System.out.println("list:" + list);
                this.cancel();
                //导出之后，再把任务给启动了
                try {
                    TaskManger.startJobs();
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }

            }
        };
    }

    private static void writeData(Sheet sheet, int rowIndex, int cellIndex, String[] data) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        for (int i = 0; i < data.length; i++) {
            Cell cell = row.createCell(cellIndex + i);
            cell.setCellValue(data[i]);
        }
    }

    private static void saveWorkbook(Workbook workbook, String filePath, int written) {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            EventBusUtil.getDefault().post(new DownProgressEvent(written / (double) HelloController.list.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
