package com.example.alonemusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.alonemusic.R;
import com.example.alonemusic.bean.Contacts;

public class ContactsItemActivity extends AppCompatActivity {

    private Contacts contacts;
    private TextView itemName;
    private LinearLayout itemPhoneList;
    private LinearLayout itemEmailList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contants_item_detail);

        toolbar = findViewById(R.id.toolbar_contacts);
        toolbar.setTitle("通讯录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        Intent intent = getIntent();
        contacts = (Contacts) intent.getSerializableExtra("contactsList");

        itemName = findViewById(R.id.itemName);
        itemPhoneList = findViewById(R.id.itemPhones);
        itemEmailList = findViewById(R.id.itemEmails);

        itemName.setText(contacts.getName());
        for(int p = 0; p < contacts.getPhoneList().size(); p++){
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0 , 0, 0);
            textView.setLayoutParams(layoutParams);
            textView.setText(contacts.getPhoneList().get(p));
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorDeepBlue));
            textView.setTextSize(20);
            itemPhoneList.addView(textView);
        }
        for(int e = 0; e < contacts.getEmailList().size(); e++){
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0 , 0, 0);
            textView.setLayoutParams(layoutParams);
            textView.setText(contacts.getEmailList().get(e));
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorDeepBlue));
            textView.setTextSize(20);
            itemEmailList.addView(textView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
