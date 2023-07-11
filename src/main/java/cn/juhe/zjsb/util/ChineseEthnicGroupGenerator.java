package cn.juhe.zjsb.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChineseEthnicGroupGenerator {
    public  static final List<String> ethnicGroups = Arrays.asList(
        "汉族", "壮族", "满族", "回族", "苗族", "维吾尔族", "土家族", "彝族", "蒙古族", "藏族",
        "布依族", "侗族", "瑶族", "朝鲜族", "白族", "哈尼族", "哈萨克族", "黎族", "傣族", "畲族",
        "傈僳族", "仡佬族", "东乡族", "高山族", "拉祜族", "水族", "佤族", "纳西族", "羌族", "土族",
        "仫佬族", "锡伯族", "柯尔克孜族", "达斡尔族", "景颇族", "毛南族", "撒拉族", "布朗族", "塔吉克族",
        "阿昌族", "普米族", "鄂温克族", "怒族", "京族", "基诺族", "德昂族", "保安族", "俄罗斯族",
        "裕固族", "乌兹别克族", "门巴族", "鄂伦春族", "独龙族", "塔塔尔族", "赫哲族", "珞巴族"
    );

    public static void main(String[] args) {
        ArrayList<String> ethnicGroupList = new ArrayList<>(ethnicGroups);

        // 打印所有民族
        System.out.println("所有民族：");
        for (String ethnicGroup : ethnicGroupList) {
            System.out.println(ethnicGroup);
        }

        // 随机取出一个民族
        String randomEthnicGroup = getRandomEthnicGroup(ethnicGroupList);
        System.out.println("随机民族: " + randomEthnicGroup);
    }

    public static String getRandomEthnicGroup(ArrayList<String> ethnicGroupList) {
        Random random = new Random();
        int randomIndex = random.nextInt(ethnicGroupList.size());
        return ethnicGroupList.get(randomIndex);
    }
}
