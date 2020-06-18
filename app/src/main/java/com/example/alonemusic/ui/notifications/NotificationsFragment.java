package com.example.alonemusic.ui.notifications;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Page(name = "NotificationsFragment")
public class NotificationsFragment extends XPageFragment {

    private GlobalApplication app;
    TextView toolbarTitle;
    List<Notification> notificationList = new ArrayList<>();
    @BindView(R.id.alone_recycler) RecyclerView recyclerView;
    private NotificationDao notificationDao;
    @BindView(R.id.button_add_notification) ImageButton addButton;
    @BindView(R.id.notification_login_layout) RelativeLayout loginLayout;
    @BindView(R.id.notification_logout_layout) LinearLayout logoutLayout;
    private UserDao userDao;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private MaterialHeader mMaterialHeader;
    private RecyclerAdapter recyclerAdapter;
    private int loadMoreCount = 0;
    private int refreshCount = 0;

    @Override
    protected TitleBar initTitleBar() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notifications;
    }

    @Override
    protected void initViews() {
        mMaterialHeader = (MaterialHeader)mRefreshLayout.getRefreshHeader();
        mMaterialHeader.setShowBezierWave(true);

        notificationDao = new NotificationDao(getActivity());
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setVisibility(View.INVISIBLE);

        app = (GlobalApplication) getActivity().getApplication();
        userDao = new UserDao(getContext());
        User lastUser = userDao.findLastUser();
        if(lastUser != null){
            if(lastUser.getState() == 1){
                loginLayout.setVisibility(View.VISIBLE);
                logoutLayout.setVisibility(View.INVISIBLE);
                addButton.setOnClickListener(v ->  {
                    Intent intent = new Intent(getActivity(), NotificationActivity.class);
                    startActivity(intent);
                });
            }
            if(lastUser.getState() == 0){
                loginLayout.setVisibility(View.INVISIBLE);
                logoutLayout.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(v ->  {
                    Toast toast = Toast.makeText(getContext(), "登录后才能操作", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                });
            }
        }else {
            loginLayout.setVisibility(View.INVISIBLE);
            logoutLayout.setVisibility(View.VISIBLE);
            addButton.setOnClickListener(v ->  {
                Toast toast = Toast.makeText(getContext(), "登录后才能操作", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });
        }

        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        initNotificationList();
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            recyclerAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
        }, 2000));
        //上拉加载
        mRefreshLayout.setOnLoadMoreListener(refreshLayout1 -> refreshLayout1.getLayout().postDelayed(() -> {
            loadMoreCount++;
            recyclerAdapter.addVisibleListSize(notificationList, 3 * loadMoreCount);
            refreshLayout1.finishLoadMore();
            //refreshLayout1.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }, 2000));
    }

    private void initNotificationList(){
        notificationList  = notificationDao.findAllNotification();
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new RecyclerAdapter(notificationList, getActivity());
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        notificationList  = notificationDao.findAllNotification();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}