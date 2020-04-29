package com.example.alonemusic.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alonemusic.R;
import com.example.alonemusic.adapter.RecyclerAdapter;
import com.example.alonemusic.bean.Notification;
import com.example.alonemusic.dao.NotificationDao;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

//    private NotificationsViewModel notificationsViewModel;
    TextView toolbarTitle;
    List<Notification> notificationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationDao notificationDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        notificationsViewModel =
//                ViewModelProviders.of(this).get(NotificationsViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        notificationDao = new NotificationDao(getActivity());

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Notifications");
        initNotificationList();
        recyclerView = view.findViewById(R.id.alone_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerAdapter(notificationList, getActivity()));

        return view;
    }

    private void initNotificationList(){
        notificationList  = notificationDao.findAllNotification();
    }

    private void initData()
    {
        notificationList = new ArrayList<>();
        notificationList.add(new Notification("红樱花", R.drawable.ic_icons8_lol_24));
        notificationList.add(new Notification("橙樱花", R.drawable.ic_icons8_lol_24));
        notificationList.add(new Notification("黄樱花", R.drawable.ic_icons8_lol_24));
        notificationList.add(new Notification("绿樱花", R.drawable.ic_icons8_lol_24));
        notificationList.add(new Notification("青樱花", R.drawable.ic_icons8_lol_24));
        notificationList.add(new Notification("蓝樱花", R.drawable.ic_icons8_lol_24));
        notificationList.add(new Notification("紫樱花", R.drawable.ic_icons8_lol_24));
    }


}