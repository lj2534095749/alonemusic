package com.example.alonemusic.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.NotificationDao;
import com.example.alonemusic.dao.UserDao;
import com.example.util.TipDialog;

public class LoginActivity  extends AppCompatActivity {

    private GlobalApplication app;
    EditText username;
    EditText password;
    Button login;
    TextView register;
    TextView retrieve;
    Toolbar toolbar;
    TextView toolbarTitle;
    CheckBox checkBox;
    private UserDao userDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = new UserDao(this);

        toolbar = findViewById(R.id.toolbar_project);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbarTitle = findViewById(R.id.toolbar_title);

        checkBox = findViewById(R.id.login_remember);

        app = (GlobalApplication)getApplication();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        if(app.getFirstLoginFlag()){
            username.setText(app.getUsername());
            password.setText(app.getPassword());
        }
        //如果不是刚注册成功
        if(!app.getFirstLoginFlag()) {
            //查询上一次登录的用户
            User lastUser = userDao.findLastUser();
            if(lastUser != null){
                username.setText(lastUser.getUsername());
                password.setText(lastUser.getPassword());
                checkBox.setChecked(true);
                //如果用户没有执行登出操作，则自动登录
                if(lastUser.getState() == 1){
                    app.setLogoutFlag(true);
                    app.setUserId(lastUser.getId());
                    initNotificationList();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }

        login = findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
                if("".equals(username.getText().toString()) || "".equals(password.getText().toString())){
                    TipDialog.showNormalDialog(LoginActivity.this, "请输入完成之后再登录");
                    return;
                }
                User user = userDao.findUserByUsername(username.getText().toString());
                if(user == null){
                    TipDialog.showNormalDialog(LoginActivity.this, "没有该用户");
                    return;
                }
                if(user.getPassword().equals(password.getText().toString())){
                    initNotificationList();
                    if(checkBox.isChecked()){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("user_id", user.getId());
                        contentValues.put("username", username.getText().toString());
                        contentValues.put("password", password.getText().toString());
                        contentValues.put("state", 1);
                        userDao.insertLastUser(contentValues);
                    }
                    app.setUserId(user.getId());
                    app.setFirstLoginFlag(false);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    TipDialog.showNormalDialog(LoginActivity.this, "用户名或密码错误");
                }
            }
        });
        register = findViewById(R.id.text_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        retrieve = findViewById(R.id.text_retrieve);
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RetrieveActivity.class);
                startActivity(intent);
            }
        });
    }

    // TODO DELETE
    private void initNotificationList(){
//        NotificationDao notificationDao = new NotificationDao(this);
//        notificationDao.initNotification();
    }
}
