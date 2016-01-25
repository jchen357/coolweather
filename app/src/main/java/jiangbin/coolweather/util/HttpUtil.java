package jiangbin.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener) {
        /*在线程中进行http请求*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 *
                 */
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    /**
                     * URL的openConnection()方法将返回一个URLConnection对象
                     * HttpURLConnection是URLConnection的子类
                     */
                    connection = (HttpURLConnection) url.openConnection();
                    /*设置发送请求的方法*/
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    /*用HttpURLConnection的getInputStream()的方法得到InputStream对象*/
                    InputStream in = connection.getInputStream();
                    /*把字节流包装成字符流，并做缓冲处理*/
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    /**
                     * StringBuilder
                     * 一个可变的字符序列，该类被设计作用StringBuffer的一个简易替换，
                     * 用在字符串缓冲区被单个线程所使用的时候
                     */
                    StringBuilder response = new StringBuilder();
                    String line;
                    /*readLine()，返回一行字符串，在遇到\t(\n)停止*/
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        /*回调onFinish()方法*/
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        /*回调onError()方法*/
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}


