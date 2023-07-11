package cn.juhe.zjsb.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class CertificateUtil {
    public static void main(String[] args) throws Exception {
        File file = new File("D://918785084181803756.jpg");
        String key = "这里换成你自己的key";
        System.out.println(post("2", file, key));
    }

    public static String post(String type, File file, String key) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        // HttpClient请求的相关设置，可以不用配置，用默认的参数，这里设置连接和超时时长(毫秒)
        RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
        try {
            HttpPost httppost = new HttpPost("http://v.juhe.cn/certificates/query.php");
            // FileBody封装File类型的参数
            FileBody bin = new FileBody(file);
            // StringBody封装String类型的参数
            StringBody keyBody = new StringBody(key, ContentType.TEXT_PLAIN);
            StringBody typeBody = new StringBody(type, ContentType.TEXT_PLAIN);
            // addPart将参数传入，并指定参数名称
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("pic", bin).addPart("key", keyBody)
                    .addPart("cardType", typeBody).build();
            httppost.setEntity(reqEntity);
            httppost.setConfig(config);
            // 执行网络请求并返回结果
            response = httpClient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = ConvertStreamToString(resEntity.getContent(), "UTF-8");
            }
            EntityUtils.consume(resEntity);
        } finally {
            response.close();
            httpClient.close();
        }
        // 得到的是JSON类型的数据需要第三方解析JSON的jar包来解析
        return result;
    }

    // 此方法是把传进的字节流转化为相应的字符串并返回，此方法一般在网络请求中用到
    public static String ConvertStreamToString(InputStream is, String charset)
            throws Exception {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(is, charset)) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\r\n");
                }
            }
        }
        return sb.toString();
    }

}

