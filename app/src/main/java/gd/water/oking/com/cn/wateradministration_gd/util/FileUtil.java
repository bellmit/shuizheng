package gd.water.oking.com.cn.wateradministration_gd.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import gd.water.oking.com.cn.wateradministration_gd.bean.Point;

/**
 * Created by zhao on 2016/10/12.
 */

public class FileUtil {

    public static String PraseUritoPath(Context context, Uri uri) {
        if (uri.getAuthority() != null && "com.android.externalstorage.documents".equals(uri.getAuthority())) {

            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];
            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        } else if (uri.getAuthority() != null && "com.android.providers.downloads.documents".equals(uri.getAuthority())) {
            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            return getDataColumn(context, contentUri, null, null);
        } else if (uri.getAuthority() != null && "com.android.providers.media.documents".equals(uri.getAuthority())) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{split[1]};
            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore  
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File  
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null){

                cursor.close();
            }
        }
        return null;
    }

    //datetimePoint 时间、errorExtent 误差范围ms
    public static Point getLastLocationFromFile(long datetimePoint, long errorExtent) {
        Point location = null;
        Point lastPoint = null;
        Point nextPoint = null;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/location");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(datetimePoint);
        File file = new File(mediaStorageDir, sdf.format(date) + ".txt");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String data = "";
                String str;
                while ((str = br.readLine()) != null) {
                    data += str + "\n";
                }

                if (data != null) {
                    String[] lines = data.split("\\n");
                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i];
                        String[] items = line.split(",");

                        if (items.length != 3) {
                            return null;
                        }

                        String Latitude = items[0];
                        String Longitude = items[1];
                        String datetime = items[2];

                        location = new Point();
                        location.setLatitude(Double.valueOf(Latitude));
                        location.setLongitude(Double.valueOf(Longitude));
                        location.setDatetime(Long.valueOf(datetime));

                        if (location.getDatetime() <= datetimePoint &&
                                location.getDatetime() >= datetimePoint - errorExtent) {
                            if (lastPoint == null) {
                                lastPoint = location;
                            } else if (location.getDatetime() >= lastPoint.getDatetime()) {
                                lastPoint = location;
                            }
                        }


                        if (location.getDatetime() >= datetimePoint &&
                                location.getDatetime() <= datetimePoint + errorExtent) {

                            if (nextPoint == null) {
                                nextPoint = location;
                            } else if (location.getDatetime() <= nextPoint.getDatetime()) {
                                nextPoint = location;
                            }
                        }

                    }

                    location = null;
                    if (lastPoint == null) {
                        location = nextPoint;
                    } else if (nextPoint == null) {
                        location = lastPoint;
                    } else {
                        //都不为空，则去前一刻定位
                        location = lastPoint;
                    }
                }

                br.close();
                isr.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return location;
    }

    public static ArrayList<Point> getPathFromFile(long begin, long end) {
        ArrayList<Point> locationPath = new ArrayList<>();
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/location");

        String[] fileName = mediaStorageDir.list();
        if (fileName == null) {
            return locationPath;
        }
        Arrays.sort(fileName);

        File locationFile = new File(mediaStorageDir, fileName[fileName.length - 1]);
        try {
            FileInputStream fis = new FileInputStream(locationFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String data = "";
            String str;
            while ((str = br.readLine()) != null) {
                data += str + "\n";
            }

            if (data != null) {
                String[] lines = data.split("\\n");

                for (int i = 0; i < lines.length; i++) {
                    Point location;

                    String Line = lines[i];
                    String[] items = Line.split(",");

                    if (items.length != 3) {
                        continue;
                    }

                    String Latitude = items[0];
                    String Longitude = items[1];
                    String datetime = items[2];

                    location = new Point();
                    location.setLatitude(Double.valueOf(Latitude));
                    location.setLongitude(Double.valueOf(Longitude));
                    location.setDatetime(Long.valueOf(datetime));

                    if (location.getDatetime() >= begin && location.getDatetime() <= end) {
                        locationPath.add(location);
                    }
                }
            }

            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationPath;
    }

    public static File compressImage(String path) {

        ////////////////////////////////////////////////////尺寸压缩
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 1200f;//这里设置高度为1200f
        float ww = 1920f;//这里设置宽度为1920f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(path, newOpts);

        //////////////////////////////////////////////////////////////质量压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int p = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, p, baos);
        while (baos.toByteArray().length > 200 * 1024) {
            baos.reset();
            p -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, p, baos);
        }

        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            bitmap.recycle();
            bitmap = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


    /**
     * 字节缓冲流读写复制文件
     * @param src 源文件
     * @param out 目标文件
     */
    public static void BufferInputStreamBufferOutputStream(String src, String out) {
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(out));
            bufferedInputStream = new BufferedInputStream(new FileInputStream(src));
            byte[] bytes = new byte[1024];
            int num = 0;
            while ((num = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, num);
                bufferedOutputStream.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedOutputStream.close();
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }


    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }


    public static File getCacheDir(Context context) {
        String APP_DIR_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
        File dir = new File(APP_DIR_NAME + context.getPackageName() + "/cache/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}
