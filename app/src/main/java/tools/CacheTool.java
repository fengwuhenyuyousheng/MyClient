package tools;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 这是缓存操作类
 * Created by Administrator on 2016/6/13.
 */
public class CacheTool {

    /**
     * 这是将缓存数据以url经过MD5转换后作为文件名保存在本地文件中
     *
     * @param context
     * @param url
     * @param json
     */
    public static void setCacheToFile(Context context, String url, String json) {
        if(url==null ||json==null) {
            return;
        }else {
            String fileName = MD5Tool.getUrlMD5(url);
            Log.d("CacheTool","存储的文件名:"+fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fileOutputStream.write(json.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 读出文件名为url经过MD5转换的文件里的缓存数据
     *
     * @param context
     * @param url
     */
    public static String getCacheFromFile(Context context, String url) {
        if(url==null){
            return null;
        }else {
            String fileName = MD5Tool.getUrlMD5(url);
            Log.d("CacheTool","读取的文件名:"+fileName);
            FileInputStream fileInputStream = null;
            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = null;
            try {
                fileInputStream = context.openFileInput(fileName);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return stringBuffer.toString();
        }
    }
}
