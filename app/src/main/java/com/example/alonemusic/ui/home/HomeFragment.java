package com.example.alonemusic.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.activity.LoginActivity;
import com.example.alonemusic.R;
import com.example.alonemusic.activity.MainActivity;
import com.example.alonemusic.activity.RegisterActivity;
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.UserDao;

public class HomeFragment extends Fragment {

    Button logout;
    TextView toolbarTitle;
    GlobalApplication app;
    private LinearLayout linearLayout;
    private UserDao userDao;
    private TextView homeName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Home");
        initView(view);

//        logout = view.findViewById(R.id.button_logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                app.setLogoutFlag(true);
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    private void initView(final View view){
        app = (GlobalApplication)getActivity().getApplication();
        userDao = new UserDao(getContext());
        homeName = view.findViewById(R.id.home_name);
        linearLayout = view.findViewById(R.id.home_layout);
        User lastUser = userDao.findLastUser();
        if(lastUser != null){
            if(lastUser.getState() != 0){
                homeName.setText(lastUser.getUsername());
                final Button logoutButton = new Button(getContext());
                logoutButton.setText("退出登录");
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                logoutButton.setLayoutParams(layoutParams);
                logoutButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDeepBlue));
                logoutButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_style));
                logoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        app.setUserId(0);
                        app.setLogoutFlag(true);
                        ContentValues values = new ContentValues();
                        values.put("state", 0);
                        userDao.updateLastUser(values);
                        homeName.setText("游客账号");
                        linearLayout.removeView(logoutButton);
                        setLogoutView();
                    }
                });
                linearLayout.addView(logoutButton);
            }
            if(lastUser.getState() == 0){
                setLogoutView();
            }
        }else {
            setLogoutView();
        }
    }

    private void setLogoutView(){
        Button loginButton = new Button(getContext());
        loginButton.setText("登录账号");
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(0, 0, 0, 20);
        loginButton.setLayoutParams(layoutParams1);
        loginButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDeepBlue));
        loginButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_style));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        linearLayout.addView(loginButton);

        Button registerButton = new Button(getContext());
        registerButton.setText("注册账号");
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        registerButton.setLayoutParams(layoutParams2);
        registerButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDeepBlue));
        registerButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_style));
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        linearLayout.addView(registerButton);
    }
}