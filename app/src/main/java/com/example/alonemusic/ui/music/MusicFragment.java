package com.example.alonemusic.ui.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.activity.MusicActivity;
import com.example.alonemusic.adapter.MusicAdapter;
import com.example.alonemusic.adapter.RecyclerAdapter;
import com.example.alonemusic.bean.LoveMusic;
import com.example.alonemusic.bean.Notification;
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.MusicDao;
import com.example.alonemusic.dao.NotificationDao;
import com.example.alonemusic.dao.UserDao;
import com.example.alonemusic.service.MusicPlayer;
import com.example.alonemusic.ui.ContentPage;
import com.example.alonemusic.ui.util.SpacesItemDecoration;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xui.widget.tabbar.EasyIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Page(name = "MusicFragment")
public class MusicFragment extends XPageFragment {

    private GlobalApplication app;
    private TextView toolbarTitle;
    private MediaPlayer mediaPlayer;
    @BindView(R.id.list_music) ListView mListView;
    @BindView(R.id.music_login_layout) LinearLayout loginLayout;
    @BindView(R.id.music_logout_layout) LinearLayout logoutLayout;
    @BindView(R.id.love_music_layout) LinearLayout loveMusicLayout;
    @BindView(R.id.love_notification_layout) LinearLayout loveNotificationLayout;
    @BindView(R.id.notification_recycler) RecyclerView recyclerView;
    private int position;
    private ArrayList<String> loveMusicFilePathList;
    private List<LoveMusic> loveMusicList;
    private List<Notification> notificationList = new ArrayList<>();
    private ArrayList<String> musicFilePathLits = new ArrayList<>();
    private List<String> musicFileNameList = new ArrayList<>();
    private UserDao userDao;
    private MusicDao musicDao;
    private NotificationDao notificationDao;

    @BindView(R.id.easy_indicator) EasyIndicator mEasyIndicator;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music;
    }

    @Override
    protected TitleBar initTitleBar() {
        return null;
    }

    @Override
    protected void initViews() {
        app = (GlobalApplication) getActivity().getApplication();
        userDao = new UserDao(getContext());
        User lastUser = userDao.findLastUser();
        if(lastUser != null){
            if(lastUser.getState() == 1){
                loginLayout.setVisibility(View.VISIBLE);
                logoutLayout.setVisibility(View.INVISIBLE);
            }
            if(lastUser.getState() == 0){
                loginLayout.setVisibility(View.INVISIBLE);
                logoutLayout.setVisibility(View.VISIBLE);
            }
        }else {
            loginLayout.setVisibility(View.INVISIBLE);
            logoutLayout.setVisibility(View.VISIBLE);
        }

        musicDao = new MusicDao(getContext());

        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setVisibility(View.INVISIBLE);

        initLoveMusicLayout();
        initLoveNotificationLayout();

        mEasyIndicator.setTabTitles(ContentPage.getPageNames());
    }

    @Override
    protected void initListeners() {
        mEasyIndicator.setOnTabClickListener((title, position) -> {
            if(position == 0){
                loveMusicLayout.setVisibility(View.VISIBLE);
                loveNotificationLayout.setVisibility(View.INVISIBLE);
            }
            if(position == 1){
                loveNotificationLayout.setVisibility(View.VISIBLE);
                loveMusicLayout.setVisibility(View.INVISIBLE);
            }
        });
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isPlayingButton", false);
            app.setCurrentMusicList(loveMusicFilePathList);
            app.setCurrentMusicNameList(musicFileNameList);
            app.setCurrentMusicPosition(position);
            app.setFindMusic(false);
            Intent intent = new Intent(getContext(), MusicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
        });
    }

    private void initLoveMusicLayout(){
        loveMusicList = musicDao.queryLoveMusicList();
        musicFileNameList = getLoveMusicNameList();
        loveMusicFilePathList = getLoveMusicPathList();
        MusicAdapter musicAdapter = new MusicAdapter(getActivity(), musicFileNameList, loveMusicFilePathList, app.getUserId());
        mListView.setAdapter(musicAdapter);
    }

    private void initLoveNotificationLayout(){
        initLoveNotificationList();
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerAdapter(notificationList, getActivity()));
    }

    private void initLoveNotificationList(){
        notificationDao = new NotificationDao(getActivity());
        notificationList  = notificationDao.queryNotificationListByUserIdInLoveNotification();
    }

    @Override
    public void onStart() {
        mediaPlayer = MusicPlayer.getMusicPlayer();
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private List<String> getLoveMusicNameList() {
        ArrayList<String> loveMusicNameList = new ArrayList<>();
        for(int i = 0; i < loveMusicList.size(); i++){
            loveMusicNameList.add(loveMusicList.get(i).getName());
        }
        return loveMusicNameList;
    }

    private ArrayList<String> getLoveMusicPathList() {
        ArrayList<String> loveMusicPathList = new ArrayList<>();
        for(int i = 0; i < loveMusicList.size(); i++){
            loveMusicPathList.add(loveMusicList.get(i).getPath());
        }
        return loveMusicPathList;
    }
}