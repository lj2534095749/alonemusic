package com.example.alonemusic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.alonemusic.R;
import com.example.alonemusic.bean.Notification;
import com.example.util.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {

    private DBHelper dbHelper;

    public NotificationDao(Context context){
        this.dbHelper = new DBHelper(context);
    }

    public List<Notification> findAllNotification(){
        Cursor cursor = dbHelper.queryAll("tb_notification");
        List<Notification> notificationList = new ArrayList<>();
        while (cursor.moveToNext()){
            Notification notification = new Notification();
            notification.setId(cursor.getInt(0));
            notification.setName(cursor.getString(1));
            notification.setHeadPortrait(cursor.getInt(2));
            notificationList.add(notification);
        }
        return notificationList;
    }

    public void insertNotification(ContentValues values){
        dbHelper.insert("tb_notification", values);
    }

    public void initNotification(){
        dbHelper.deleteAll("tb_notification");
        ContentValues values1 = new ContentValues();
        values1.put("name", "红樱花");
        values1.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values1);
        ContentValues values2 = new ContentValues();
        values2.put("name", "橙樱花");
        values2.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values2);
        ContentValues values3 = new ContentValues();
        values3.put("name", "黄樱花");
        values3.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values3);
        ContentValues values4 = new ContentValues();
        values4.put("name", "绿樱花");
        values4.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values4);
        ContentValues values5 = new ContentValues();
        values5.put("name", "青樱花");
        values5.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values5);
        ContentValues values6 = new ContentValues();
        values6.put("name", "蓝樱花");
        values6.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values6);
        ContentValues values7 = new ContentValues();
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
    }

    /**
     * @param resourceID  图片资源id
     * @return   将图片转化成byte
     */
    private byte[] picTobyte(Context context, int resourceID)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = context.getResources().openRawResource(resourceID);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        //压缩图片，100代表不压缩（0～100）
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();
    }
}