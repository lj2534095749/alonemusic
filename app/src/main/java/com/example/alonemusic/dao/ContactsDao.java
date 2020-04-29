package com.example.alonemusic.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.example.alonemusic.bean.Contacts;
import com.example.util.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactsDao {

    private DBHelper dbHelper;

    public ContactsDao(Context context){
        this.dbHelper = new DBHelper(context);
    }

    public List<Contacts> queryAllContacts(ContentResolver contentResolver){
        List<Contacts> contactsList = new ArrayList<>();
        //使用ContentResolver查找联系人数据
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()){
            Contacts contacts = new Contacts();

            //获取联系人ID
            String contractId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的名字
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //使用ContentResolver查找联系人的电话号码
            Cursor phonesCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contractId, null, null);
            //遍历查询结果，获取该联系人的多个电话号码
            List<String> phoneList = new ArrayList<>();
            while (phonesCursor.moveToNext()) {
                //获取查询结果中电话号码列中的数据
                String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneList.add(phoneNumber);
            }
            phonesCursor.close();
            //使用ContentResolver查找联系人的E-mail地址
            Cursor emailsCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contractId, null, null);
            //遍历查询结果，获取该联系人的多个E-mail地址
            List<String> emailList = new ArrayList<>();
            while (emailsCursor.moveToNext()) {
                //获取查询结果中的E-mail地址列中的数据
                String emailAddress = emailsCursor.getString(emailsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                emailList.add(emailAddress);
            }
            emailsCursor.close();

            contacts.setId(contractId);
            contacts.setName(name);
            contacts.setPhoneList(phoneList);
            contacts.setEmailList(emailList);
            contactsList.add(contacts);
        }
        cursor.close();
        return contactsList;
    }
}