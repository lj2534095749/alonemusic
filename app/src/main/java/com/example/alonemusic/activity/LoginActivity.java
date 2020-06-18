package com.example.alonemusic.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.UserDao;
import com.example.util.TipDialog;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.volley.Request.Method.POST;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.button_login)
    Button login;
    @BindView(R.id.text_register)
    TextView register;
    @BindView(R.id.text_retrieve)
    TextView retrieve;
    @BindView(R.id.toolbar_project)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.login_remember)
    CheckBox checkBox;
    private GlobalApplication app;
    private UserDao userDao;

    private Handler handler = new Handler(msg -> {
        switch (msg.what){
            case 200:
                if(checkBox.isChecked()){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("user_id", msg.obj.toString());
                    contentValues.put("username", username.getText().toString());
                    contentValues.put("password", password.getText().toString());
                    contentValues.put("state", 1);
                    userDao.insertLastUser(contentValues);
                }
                app.setUserId(Integer.parseInt(msg.obj.toString()));
                app.setFirstLoginFlag(false);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 201:
                TipDialog.showNormalDialog(LoginActivity.this, "用户名或密码错误");
                break;

        }
        return false;
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userDao = new UserDao(this);

        toolbar.setTitle("");
        toolbarTitle.setText("Login");

        app = (GlobalApplication) getApplication();
        if (app.getFirstLoginFlag()) {
            username.setText(app.getUsername());
            password.setText(app.getPassword());
        }
        //如果不是刚注册成功
        if (!app.getFirstLoginFlag()) {
            //查询上一次登录的用户
            User lastUser = userDao.findLastUser();
            if (lastUser != null) {
                username.setText(lastUser.getUsername());
                password.setText(lastUser.getPassword());
                checkBox.setChecked(true);
                //如果用户没有执行登出操作，则自动登录
                if (lastUser.getState() == 1) {
                    app.setLogoutFlag(true);
                    app.setUserId(lastUser.getId());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }

        login.setOnClickListener(v -> {
            if("".equals(username.getText().toString()) || "".equals(password.getText().toString())){
                TipDialog.showNormalDialog(LoginActivity.this, "请输入完成之后再登录");
                return;
            }
            User user = userDao.findUserByUsername(username.getText().toString());
            if(user != null){
                loginLocal(user);
            }
            if(user == null){
                loginJson();
            }
        });
        register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        retrieve.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RetrieveActivity.class);
            startActivity(intent);
        });


    }

    private void loginJson() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username.getText().toString());
            jsonObject.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, "http://10.0.2.2:8088/user/loginvue", jsonObject,
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

    private void loginLocal(User user){
        if(user.getPassword().equals(password.getText().toString())){
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
}
