package cn.juhe.zjsb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChineseIDCardGenerator {
    public static void main(String[] args) {
        ArrayList<String> idCards = generateRandomIDCards(200);

        // 打印生成的身份证号列表
        for (String idCard : idCards) {
            System.out.println(idCard);
        }

        // 随机取出一个身份证号
        String randomIDCard = getRandomIDCard(idCards);
        System.out.println("Random ID Card: " + randomIDCard);
    }

    public static ArrayList<String> generateRandomIDCards(int count) {
        ArrayList<String> idCards = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            String idCard = generateRandomIDCard(random);
            idCards.add(idCard);
        }

        return idCards;
    }

    public static String generateRandomIDCard(Random random) {
        // 生成随机的年份，1900年至今
        int year = random.nextInt(122) + 1900;
        // 生成随机的月份，1月至12月
        int month = random.nextInt(12) + 1;
        // 生成随机的日期，1日至28日
        int day = random.nextInt(28) + 1;
        // 生成随机的顺序号，3位数
        int sequence = random.nextInt(1000);
        // 生成随机的校验码，1位数
        int checksum = random.nextInt(10);

        // 格式化生成的身份证号码
        String idCard = String.format("%04d%02d%02d******%03d%d", year, month, day, sequence, checksum);

        return idCard;
    }

    public static String getRandomIDCard(List<String> idCards) {
        Random random = new Random();
        int randomIndex = random.nextInt(idCards.size());
        return idCards.get(randomIndex);
    }
}
