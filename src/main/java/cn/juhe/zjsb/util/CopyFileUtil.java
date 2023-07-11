package cn.juhe.zjsb.util;

import java.io.*;

public class CopyFileUtil {
    public static void copyFileUsingStream(File dest) throws IOException {
        try (InputStream is = CopyFileUtil.class.getResourceAsStream("/mobile.xlsx"); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            if (is != null) {
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }
        }
    }
}
