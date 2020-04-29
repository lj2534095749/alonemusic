package com.example.alonemusic.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.UserDao;
import com.example.util.TipDialog;

public class RegisterActivity extends AppCompatActivity {

    private GlobalApplication app;
    EditText username;
    EditText password;
    EditText confirmPassword;
    Button register;
    Button back;
    Toolbar toolbar;
    TextView toolbarTitle;
    private UserDao userDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDao = new UserDao(this);

        toolbar = findViewById(R.id.toolbar_project);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Register");

        app = (GlobalApplication)getApplication();
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(username.getText().toString()) || "".equals(password.getText().toString())){
                    TipDialog.showNormalDialog(RegisterActivity.this, "请输入完成之后再注册");
                    return;
                }
                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                    app.setUsername(username.getText().toString());
                    app.setPassword(password.getText().toString());
                    app.setFirstLoginFlag(true);
                    User user = userDao.findUserByUsername(username.getText().toString());
                    //用户不存在
                    if(user == null){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("username", username.getText().toString());
                        contentValues.put("password", password.getText().toString());
                        contentValues.put("state", 1);
                        userDao.insertUser(contentValues);
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        TipDialog.showNormalDialog(RegisterActivity.this, "用户已存在");
                    }
                }
                else {
                    TipDialog.showNormalDialog(RegisterActivity.this, "两次密码输入不一致");
                }
            }
        });
        back = findViewById(R.id.button_register_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
