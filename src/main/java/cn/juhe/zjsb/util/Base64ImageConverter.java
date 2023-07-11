package cn.juhe.zjsb.util;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64ImageConverter {
    public static Image byteArrayToImage(byte[] imageBytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        Image image = new Image(inputStream);
        return image;
    }

    public static void saveBase64Image(String base64String, String filePath) {
        try {
            // 解码base64字符串
            byte[] imageBytes = Base64.getDecoder().decode(base64String);

            // 创建文件输出流
            FileOutputStream imageOutputStream = new FileOutputStream(filePath);

            // 将图像字节数组写入文件
            imageOutputStream.write(imageBytes);

            // 关闭文件输出流
            imageOutputStream.close();
            
            System.out.println("Image saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String base64String =imageToBase64("/Users/java0904/zhengjianshibie/x.jpg"); // 替换为你的base64字符串
        String filePath = "output.png"; // 替换为你的文件路径
        saveBase64Image(base64String, filePath);
    }
    public static String imageToBase64(String imagePath) {
        try {
            // 读取图片文件为字节数组
            FileInputStream imageInputStream = new FileInputStream(imagePath);
            byte[] imageBytes = new byte[imageInputStream.available()];
            imageInputStream.read(imageBytes);
            imageInputStream.close();

            // 将字节数组转换为base64字符串
            String base64String = Base64.getEncoder().encodeToString(imageBytes);

            return base64String;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
