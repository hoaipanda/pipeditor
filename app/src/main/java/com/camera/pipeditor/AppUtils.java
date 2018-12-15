package com.camera.pipeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.camera.pipeditor.data.Menu;
import com.camera.pipeditor.data.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class AppUtils {
    public static ArrayList<Photo> getAllImageGallery(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<Photo> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED
        };
        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(new Photo(absolutePathOfImage, false));
        }
        return listOfAllImages;
    }

    public static ArrayList<Menu> getListMenu() {
        ArrayList<Menu> list = new ArrayList<>();
        list.add(new Menu(R.drawable.crop, R.drawable.crop_sl));
        list.add(new Menu(R.drawable.filter, R.drawable.filter_sl));
        list.add(new Menu(R.drawable.adjust, R.drawable.adjust_sl));
        list.add(new Menu(R.drawable.sticker, R.drawable.sticker_sl));
        return list;
    }

    public static ArrayList<String> getListSticker(Context context) {
        String[] files = new String[0];
        try {
            AssetManager assetManager = context.getAssets();
            files = assetManager.list("sticker");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>(Arrays.asList(files));
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }


    public static void createDirectoryAndSaveFile(Context context, Bitmap imageToSave) {
        long millis = System.currentTimeMillis();
        File direct = new File(Environment.getExternalStorageDirectory() + "/PipCamera/");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/PipCamera/");
            wallpaperDirectory.mkdirs();
        }
        File file = new File(new File("/sdcard/PipCamera/"), "IMG" + millis + ".jpg");
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            refreshGallery(context, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refreshGallery(Context context, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        context.sendBroadcast(mediaScanIntent);
    }


}
