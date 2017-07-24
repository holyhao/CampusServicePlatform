package com.img.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.os.Environment;

public class FileUtils {

    private static String getRootFilePath() {
        StringBuffer stbPath = new StringBuffer();
        stbPath.append(Environment.getExternalStorageDirectory().getPath());
        stbPath.append(File.separator);
        stbPath.append("example");
        stbPath.append(File.separator);
        return stbPath.toString();
    }

    public static void saveImage(final Bitmap bitmap) throws IOException,
            FileNotFoundException {

        // SD卡可用
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 创建文件目录
            File f = new File(getRootFilePath());
            f.mkdirs();
            // 获取成功
            if (bitmap != null) {
                // 获取当前系统时间
                Calendar cl = Calendar.getInstance();
                cl.setTime(Calendar.getInstance().getTime());
                long milliseconds = cl.getTimeInMillis();
                // 当前系统时间作为文件名
                String fileName = String.valueOf(milliseconds) + ".jpg";
                // 保存图片
                // 创建文件
                File file = new File(getRootFilePath(), fileName);

                file.createNewFile();

                if (!file.isFile()) {
                    throw new FileNotFoundException();
                }

                FileOutputStream fOut = null;

                fOut = new FileOutputStream(file);

                // 将图片转为输出流
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

                // 关闭输出流
                fOut.flush();
                fOut.close();

            }
        }
    }

    public static String getTakePhotoPath() {
        return getCacheFile().getPath() + File.separator + "takephoto.jpg";
    }

    /**
     * 得到缓存文件夹
     */
    public static File getCacheFile() {
        StringBuffer sb = new StringBuffer();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append(File.separator);
        sb.append("Android");
        sb.append(File.separator);
        sb.append("data");
        sb.append(File.separator);
        sb.append("cache");
        sb.append(File.separator);
        File file = new File(sb.toString());
        return file;
    }
}
