/**
 * x.java[v 1.0.0]
 * class:com.img.util,x
 * 周航 create at 2016-5-24 下午6:47:29
 */
package com.img.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class PictureUtils {

    /**
     * 压缩图片。
     * 
     * @param path
     *            图片路径
     * @param size
     *            图片最大尺寸
     * @return 压缩后的图片
     * @throws IOException
     */
    public static Bitmap compressImage(String path, int size)
            throws IOException {
        Bitmap bitmap = null;
        // 取得图片
        InputStream is = new FileInputStream(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
        options.inJustDecodeBounds = true;
        // 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
        BitmapFactory.decodeStream(is, null, options);
        // 关闭流
        is.close();

        // // 生成压缩的图片
        int i = 0;
        while (true) {
            // 这一步是根据要设置的大小，使宽和高都能满足
            if ((options.outWidth >> i <= size)
                    && (options.outHeight >> i <= size)) {
                // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
                is = new FileInputStream(path);
                // 这个参数表示 新生成的图片为原始图片的几分之一。
                options.inSampleSize = (int) Math.pow(2.0D, i);
                // 这里之前设置为了true，所以要改为false，否则就创建不出图片
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Config.ARGB_8888;
                // 同时设置才会有效
                options.inPurgeable = true;
                // 当系统内存不够时候图片自动被回收
                options.inInputShareable = true;
                // 创建Bitmap
                bitmap = BitmapFactory.decodeStream(is, null, options);
                break;
            }
            i += 1;
        }

        return bitmap;
    }

    /**
     * 缩放图片到固定文件大小
     * 
     * @param bm
     *            需要缩放的图片
     * @param maxSize
     *            目标文件大小，单位：KB
     * @return
     */
    public static Bitmap imageZoom(Bitmap bm, double maxSize) {
        // 图片允许最大空间 单位：KB
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            // 获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // 保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小
            bm = zoomImage(bm, bm.getWidth() / Math.sqrt(i), bm.getHeight()
                    / Math.sqrt(i));
        }
        return bm;
    }

    /***
     * 图片的缩放方法
     * 
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
            double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 
     * @Title: rotateImage
     * @param path
     * @return void
     */
    public static int getImageOrientation(String path) {

        try {
            ExifInterface exifInterface = new ExifInterface(path);

            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            return orientation;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ExifInterface.ORIENTATION_NORMAL;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix m = new Matrix();
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            m.setRotate(90);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            m.setRotate(180);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            m.setRotate(270);
        } else {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        try {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
        } catch (OutOfMemoryError ooe) {

            m.postScale(1, 1);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);

        }
        return bitmap;
    }
}
