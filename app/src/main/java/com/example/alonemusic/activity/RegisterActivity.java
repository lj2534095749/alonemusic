package com.example.alonemusic.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.dao.UserDao;
import com.example.util.TipDialog;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method.POST;

public class RegisterActivity extends AppCompatActivity {

    private GlobalApplication app;
    EditText username;
    EditText password;
    EditText confirmPassword;
    Button register;
    Toolbar toolbar;
    TextView toolbarTitle;
    private UserDao userDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_register);

        userDao = new UserDao(this);

        toolbar = findViewById(R.id.toolbar_project);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        toolbarTitle.setText("Register");

        app = (GlobalApplication)getApplication();
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.button_register);
        register.setOnClickListener(v -> registerJson());
    }

    private Handler handler = new Handler(msg -> {
        switch (msg.what){
            case 200:
                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                    app.setUsername(username.getText().toString());
                    app.setPassword(password.getText().toString());
                    app.setFirstLoginFlag(true);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", msg.obj.toString());
                    contentValues.put("username", username.getText().toString());
                    contentValues.put("password", password.getText().toString());
                    contentValues.put("state", 1);
                    userDao.insertUser(contentValues);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    TipDialog.showNormalDialog(RegisterActivity.this, "两次密码输入不一致");
                }
                break;
            case 201:
                TipDialog.showNormalDialog(RegisterActivity.this, "用户已存在");
                break;

        }
        return false;
    });

    private void registerJson() {
        if("".equals(username.getText().toString()) || "".equals(password.getText().toString())){
            TipDialog.showNormalDialog(RegisterActivity.this, "请输入完成之后再注册");
            return;
        }
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username.getText().toString());
            jsonObject.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, "http://10.0.2.2:8088/user/registervue", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = Message.obtain();
                        try {
                            if(response.getString("code").equals("200")){
                                message.what = 200;
                                message.obj = response.getString("userId");
                                handler.sendMessage(message);
                            }
                            else if(response.getString("code").equals("201")){
                                message.what = 201;
                                handler.sendMessage(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("TAG", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                }
        ) ;
        mQueue.add(jsonObjectRequest);
    }
}
