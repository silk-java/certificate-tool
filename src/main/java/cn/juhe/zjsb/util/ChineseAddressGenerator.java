package cn.juhe.zjsb.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChineseAddressGenerator {
    private static final List<String> provinces = Arrays.asList(
            "北京市", "天津市", "河北省", "山西省", "内蒙古自治区", "辽宁省", "吉林省", "黑龙江省",
            "上海市", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省",
            "湖南省", "广东省", "广西壮族自治区", "海南省", "重庆市", "四川省", "贵州省", "云南省",
            "西藏自治区", "陕西省", "甘肃省", "青海省", "宁夏回族自治区", "新疆维吾尔自治区", "台湾省",
            "香港特别行政区", "澳门特别行政区"
    );

    private static final List<String> cities = Arrays.asList(
            "市辖区", "县", "市"
    );

    private static final List<String> districts = Arrays.asList(
            "东区", "南区", "西区", "北区", "中区", "湾仔区", "油尖旺区", "深水埗区", "九龙城区",
            "黄大仙区", "观塘区", "荃湾区", "屯门区", "元朗区", "北区", "大埔区", "沙田区",
            "西贡区", "离岛区"
    );

    private static final List<String> streets = Arrays.asList(
            "兴隆街", "建国路", "人民路", "中山路", "解放路", "和平街", "长江路", "胜利街", "文化路",
            "新华路", "朝阳路", "东风街", "西大街", "南京路", "广场街", "东门路", "西门路", "宝山街",
            "山西路", "西安街", "解放东路", "文化西路", "环城路", "江东路", "湖滨路", "和平西路",
            "新市街", "北京路", "济南路", "长沙路", "中山东路", "西门东路", "东大街", "西大街", "青年路",
            "金山街", "湖北路", "胜利南路", "朝阳北路", "新华东路", "文化南路", "环城西路", "江南街",
            "湖滨东路", "和平北路", "新市南街", "北京西路", "济南东路", "长沙南路", "中山南路", "东门南路",
            "西门南路", "青年南路", "金山南街", "湖北南路", "朝阳南路", "新华南路", "文化南路", "环城南路",
            "江南南街", "湖滨南路", "和平南路", "新市南街", "北京南路", "济南南路", "长沙南路", "中山南路",
            "东门南路", "西门南路", "青年南路", "金山南街", "湖北南路", "朝阳南路", "新华南路", "文化南路",
            "环城南路", "江南南街", "湖滨南路", "和平南路"
    );

    public static void main(String[] args) {
        ArrayList<String> addresses = generateRandomAddresses(200);

        // 打印生成的地址列表
        for (String address : addresses) {
            System.out.println(address);
        }

        // 随机取出一个地址
        String randomAddress = getRandomAddress(addresses);
        System.out.println("Random Address: " + randomAddress);
    }

    public static ArrayList<String> generateRandomAddresses(int count) {
        ArrayList<String> addresses = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            String province = provinces.get(random.nextInt(provinces.size()));
            String city = cities.get(random.nextInt(cities.size()));
            String district = districts.get(random.nextInt(districts.size()));
            String street = streets.get(random.nextInt(streets.size()));
            String fullAddress = province + city + district + street;
            addresses.add(fullAddress);
        }

        return addresses;
    }

    public static String getRandomAddress(ArrayList<String> addresses) {
        Random random = new Random();
        int randomIndex = random.nextInt(addresses.size());
        return addresses.get(randomIndex);
    }
}
