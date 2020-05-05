package com.example.alonemusic.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.bean.LoveMusic;
import com.example.alonemusic.bean.LoveNotification;
import com.example.alonemusic.bean.Notification;
import com.example.util.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationDao {

    private DBHelper dbHelper;
    private GlobalApplication app;
    private int userId;

    public NotificationDao(Context context){
        this.dbHelper = new DBHelper(context);
        this.app = (GlobalApplication) ((Activity)context).getApplication();
        this.userId = app.getUserId();
    }

    //TODO ...
    public List<Notification> findAllNotification(){
        Cursor cursor = dbHelper.queryAll(DBHelper.TB_NOTIFICATION);
        List<Notification> notificationList = new ArrayList<>();
        while (cursor.moveToNext()){
            Notification notification = new Notification();
            notification.setId(cursor.getInt(0));
            notification.setTitle(cursor.getString(1));
            notification.setHeadPortrait(cursor.getString(2));
            notification.setContent(cursor.getString(3));
//            if(cursor.getString(4) != "" && cursor.getString(4).contains("|")){
//                notification.setImages(Arrays.asList(cursor.getString(4).split("|")));
//            }
            notificationList.add(notification);
        }
        return notificationList;
    }

    //TODO ...
    public Notification findNotificationById(Integer id){
        Cursor cursor = dbHelper.queryById(DBHelper.TB_NOTIFICATION, id);
        Notification notification = new Notification();
        if (cursor.moveToFirst()){
            notification.setId(cursor.getInt(0));
            notification.setTitle(cursor.getString(1));
            notification.setHeadPortrait(cursor.getString(2));
            notification.setContent(cursor.getString(3));
//            notification.setImages(Arrays.asList(cursor.getString(4).split("|")));
        }
        return notification;
    }

    public List<Notification> queryNotificationListByUserIdInLoveNotification(){
        // 查出该用户所有收藏
        Cursor cursor = dbHelper.queryByUserId(DBHelper.TB_NOTIFICATION_LOVE, userId);
        List<Notification> notificationList = new ArrayList<>();
        while (cursor.moveToNext()){
            // 根据收藏ID查询收藏信息
            if(cursor.getInt(cursor.getColumnIndex("state")) == 1){
                Notification notification = findNotificationById(cursor.getInt(cursor.getColumnIndex("notification_id")));
                notificationList.add(notification);
            }
        }
        return notificationList;
    }

    public List<LoveNotification> queryLoveNotificationListByUserId(){
        Cursor cursor = dbHelper.queryByUserId(DBHelper.TB_NOTIFICATION_LOVE, userId);
        List<LoveNotification> loveNotificationList = new ArrayList<>();
        while (cursor.moveToNext()){
            LoveNotification loveNotification = new LoveNotification();
            loveNotification.setId(cursor.getInt(0));
            loveNotification.setUserId(cursor.getInt(1));
            Notification notification = findNotificationById(cursor.getInt(2));
            loveNotification.setNotification(notification);
            loveNotification.setState(cursor.getInt(3));
            if(loveNotification.getState() == 1){
                loveNotificationList.add(loveNotification);
            }
        }
        return loveNotificationList;
    }

    private int queryLoveNotificationWhereClause(Integer id){
        Cursor c = dbHelper.querySelection(DBHelper.TB_NOTIFICATION_LOVE,
                "user_id = ? AND notification_id = ?", new String[]{String.valueOf(userId), String.valueOf(id)});
        Log.d("NotificationDao", "queryLoveNotificationWhereClause: " + c.getCount());
        return c.getCount();
    }

    public void insertNotification(ContentValues values){
        dbHelper.insert(DBHelper.TB_NOTIFICATION, values);
    }

    public void insertLoveNotification(ContentValues values, Integer id){
        if(queryLoveNotificationWhereClause(id) == 0){
            dbHelper.insert(DBHelper.TB_NOTIFICATION_LOVE, values);
        }else {
            updateLoveNotificationById(values, id);
        }

    }

    public void updateLoveNotificationById(ContentValues values, Integer id){
        dbHelper.updateWhereClause(DBHelper.TB_NOTIFICATION_LOVE, values,
                "user_id = ? AND notification_id = ?", new String[]{String.valueOf(userId), String.valueOf(id)});
    }

    public boolean hasLoveNotificationInLoveNotificationList(Integer id){
        List<LoveNotification> loveNotificationList = queryLoveNotificationListByUserId();
        for(int i = 0; i < loveNotificationList.size(); i++){
            if(loveNotificationList.get(i).getState() == 1){
                if(loveNotificationList.get(i).getNotification().getId() == id){
                    return true;
                }
            }
        }
        return false;
    }

    //TODO DELETE
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
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
        values7.put("name", "紫樱花");
        values7.put("headPortrait", R.drawable.ic_icons8_lol_24);
        dbHelper.insert("tb_notification", values7);
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