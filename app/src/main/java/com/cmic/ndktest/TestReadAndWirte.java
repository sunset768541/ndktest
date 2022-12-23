package com.cmic.ndktest;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class TestReadAndWirte {
    public static void writeToSd(String sss, Activity activity){
        try {
            Random random = new Random();
            int pp = random.nextInt();
            while (pp<0){
                pp = random.nextInt();
            }
            String path="/data/data/com.cmic.ndktest/files/"+pp;
            File file = new File(path);
            if (!file.exists()) {
                // 创建文件夹
                file.mkdirs();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(path+"/mm.txt");
//            FileOutputStream fileOutputStream = activity.getApplicationContext().openFileOutput(path, activity.getApplicationContext().MODE_PRIVATE);
            //包装为字符输出流方法
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            outputStreamWriter.write(sss);
            outputStreamWriter.flush();
            fileOutputStream.flush();
            outputStreamWriter.close();
            fileOutputStream.close();

            //直接写入字节流方法
         /*fileOutputStream.write("这里是文件的内容！！！！".getBytes());
          * fileOutputStream.flush();
         fileOutputStream.close();*/
            Log.e("TRAW","保存成功"+path);
        } catch (Exception e) {
            Log.e("TRAW","保存失败");
            e.printStackTrace();
        }
    }
    public static String readString(Activity activity){

        try {
            FileInputStream fileInputStream = activity.getApplicationContext().openFileInput("myttt.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            char[] input = new char[fileInputStream.available()];
            inputStreamReader.read(input);
            inputStreamReader.close();
            fileInputStream.close();
            Log.e("TRAW","读取成功");
            return new String(input);
        } catch (Exception e) {
            Log.e("TRAW","读取失败");
            e.printStackTrace();
            return  "error";
        }
    }
}
