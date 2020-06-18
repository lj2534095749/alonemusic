package com.example.alonemusic.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.activity.LoginActivity;
import com.example.alonemusic.activity.RegisterActivity;
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.UserDao;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;

import butterknife.BindView;

@Page(name = "HomeFragment")
public class HomeFragment extends XPageFragment {

    TextView toolbarTitle;
    GlobalApplication app;
    private UserDao userDao;
    @BindView(R.id.home_layout) LinearLayout linearLayout;
    @BindView(R.id.home_name) TextView homeName;
    @BindView(R.id.login_layout) LinearLayout loginLayout;
    @BindView(R.id.button_logout) Button logoutBtn;
    @BindView(R.id.logout_layout) LinearLayout logoutLayout;
    @BindView(R.id.button_login) Button loginBtn;
    @BindView(R.id.button_register) Button registerBtn;

    @Override
    protected TitleBar initTitleBar() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews() {
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setVisibility(View.INVISIBLE);

        app = (GlobalApplication) getActivity().getApplication();
        userDao = new UserDao(getContext());
        User lastUser = userDao.findLastUser();
        if (lastUser != null) {
            if (lastUser.getState() != 0) {
                homeName.setText(lastUser.getUsername());
                loginLayout.setVisibility(View.VISIBLE);
                logoutLayout.setVisibility(View.INVISIBLE);
            }
            if (lastUser.getState() == 0) {
                setLogoutView();
            }
        } else {
            setLogoutView();
        }
    }

    @Override
    protected void initListeners() {
        logoutBtn.setOnClickListener(v -> {
            app.setUserId(0);
            app.setLogoutFlag(true);
            ContentValues values = new ContentValues();
            values.put("state", 0);
            userDao.updateLastUser(values);
            homeName.setText("游客账号");
            setLogoutView();
        });
        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void setLogoutView() {
        loginLayout.setVisibility(View.INVISIBLE);
        logoutLayout.setVisibility(View.VISIBLE);
    }
}