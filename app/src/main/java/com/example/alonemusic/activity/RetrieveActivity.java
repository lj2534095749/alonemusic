package com.example.alonemusic.activity;

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
import com.example.util.TipDialog;

public class RetrieveActivity extends AppCompatActivity {

    private GlobalApplication app;
    EditText username;
    EditText password;
    EditText confirmPassword;
    Button retrieve;
    Button back;
    Toolbar toolbar;
    TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        toolbar = findViewById(R.id.toolbar_project);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Retrieve");

        app = (GlobalApplication)getApplication();
        username = findViewById(R.id.retrieve_username);
        password = findViewById(R.id.retrieve_password);
        confirmPassword = findViewById(R.id.retrieve_confirmPassword);
        retrieve = findViewById(R.id.button_retrieve);
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(username.getText().toString()) || "".equals(password.getText().toString())){
                    TipDialog.showNormalDialog(RetrieveActivity.this, "请输入完成之后再修改");
                    return;
                }
//                if(!username.getText().toString().equals(GlobalVariable.USERNAME))
                if(!username.getText().toString().equals(app.getUsername())){
                    TipDialog.showNormalDialog(RetrieveActivity.this, "无此用户");
                    return;
                }
                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                    app.setPassword(password.getText().toString());
                    Intent intent = new Intent(RetrieveActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    TipDialog.showNormalDialog(RetrieveActivity.this, "两次密码输入不一致");
                }
            }
        });
        back = findViewById(R.id.button_retrieve_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetrieveActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
