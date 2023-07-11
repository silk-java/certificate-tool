package cn.juhe.zjsb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBirthdayGenerator {
    public static void main(String[] args) {
        ArrayList<String> birthdays = generateRandomBirthdays(200, 1932, 2023);

        // 打印生成的生日列表
        for (String birthday : birthdays) {
            System.out.println(birthday);
        }

        // 随机取出一个生日
        String randomBirthday = getRandomBirthday(birthdays);
        System.out.println("Random Birthday: " + randomBirthday);
    }

    public static ArrayList<String> generateRandomBirthdays(int count, int startYear, int endYear) {
        ArrayList<String> birthdays = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int year = random.nextInt(endYear - startYear + 1) + startYear;
            int month = random.nextInt(12) + 1;
            int day = random.nextInt(28) + 1;

            String birthday = String.format("%04d-%02d-%02d", year, month, day);
            birthdays.add(birthday);
        }

        return birthdays;
    }

    public static String getRandomBirthday(List<String> birthdays) {
        Random random = new Random();
        int randomIndex = random.nextInt(birthdays.size());
        return birthdays.get(randomIndex);
    }
}
