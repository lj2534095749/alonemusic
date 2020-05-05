package com.example.alonemusic.ui.notifications;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.activity.NotificationActivity;
import com.example.alonemusic.adapter.RecyclerAdapter;
import com.example.alonemusic.bean.Notification;
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.NotificationDao;
import com.example.alonemusic.dao.UserDao;
import com.example.alonemusic.ui.util.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

//    private NotificationsViewModel notificationsViewModel;
    private GlobalApplication app;
    TextView toolbarTitle;
    List<Notification> notificationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationDao notificationDao;
    private ImageButton addButton;
    private LinearLayout loginLayout;
    private LinearLayout logoutLayout;
    private UserDao userDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        notificationDao = new NotificationDao(getActivity());
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Notifications");

        app = (GlobalApplication) getActivity().getApplication();
        loginLayout = view.findViewById(R.id.notification_login_layout);
        logoutLayout = view.findViewById(R.id.notification_logout_layout);
        addButton = view.findViewById(R.id.button_add_notification);
        userDao = new UserDao(getContext());
        User lastUser = userDao.findLastUser();
        if(lastUser != null){
            if(lastUser.getState() == 1){
                loginLayout.setVisibility(View.VISIBLE);
                logoutLayout.setVisibility(View.INVISIBLE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NotificationActivity.class);
                        startActivity(intent);
                    }
                });
            }
            if(lastUser.getState() == 0){
                loginLayout.setVisibility(View.INVISIBLE);
                logoutLayout.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(getContext(), "登录后才能操作", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
            }
        }else {
            loginLayout.setVisibility(View.INVISIBLE);
            logoutLayout.setVisibility(View.VISIBLE);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = Toast.makeText(getContext(), "登录后才能操作", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }

        recyclerView = view.findViewById(R.id.alone_recycler);
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        initNotificationList();

        return view;
    }

    private void initNotificationList(){
        notificationList  = notificationDao.findAllNotification();
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerAdapter(notificationList, getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        initNotificationList();
    }

    //    private void initData()
//    {
//        notificationList = new ArrayList<>();
//        notificationList.add(new Notification("红樱花", R.drawable.ic_icons8_lol_24));
//        notificationList.add(new Notification("橙樱花", R.drawable.ic_icons8_lol_24));
//        notificationList.add(new Notification("黄樱花", R.drawable.ic_icons8_lol_24));
//        notificationList.add(new Notification("绿樱花", R.drawable.ic_icons8_lol_24));
//        notificationList.add(new Notification("青樱花", R.drawable.ic_icons8_lol_24));
//        notificationList.add(new Notification("蓝樱花", R.drawable.ic_icons8_lol_24));
//        notificationList.add(new Notification("紫樱花", R.drawable.ic_icons8_lol_24));
//    }


}