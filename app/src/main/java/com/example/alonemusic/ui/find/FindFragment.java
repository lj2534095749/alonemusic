package com.example.alonemusic.ui.find;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.adapter.MusicAdapter;
import com.example.alonemusic.service.MusicPlayer;
import com.example.alonemusic.service.MusicService;
import com.example.alonemusic.service.MusicServiceConnection;
import com.example.alonemusic.ui.RadiusImageBanner;
import com.example.util.FileUtil;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseBanner;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Page(name = "FindFragment")
public class FindFragment extends XPageFragment {

    private GlobalApplication app;
    @BindView(R.id.find_list_music) ListView mListView;
    private TextView toolbarTitle;
    private MaterialSearchView searchView;
    private List<String> musicFileNameList = new ArrayList<>();
    private List<String> musicFileNameFindList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private MusicAdapter musicAdapter;
    private String toolbarText;
    private ServiceConnection serviceConnection;
    private ArrayList<String> musicFilePathList;
    private int clickListViewNumber = 0;
    private List<BannerItem> mData;
    @BindView(R.id.rib_simple_usage)
    RadiusImageBanner rib_simple_usage;

    @Override
    protected TitleBar initTitleBar() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initViews() {
        app = (GlobalApplication) getActivity().getApplication();
        mediaPlayer = MusicPlayer.getMusicPlayer();
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setVisibility(View.VISIBLE);
        //searchView = getActivity().findViewById(R.id.search_view);
        musicFileNameList = getMusicFileNameList();
        musicFilePathList = getMusicFilePathList();
        musicAdapter = new MusicAdapter(getActivity(), musicFileNameList, musicFilePathList, app.getUserId());
        initMusicAdapter();
        mListView.setAdapter(musicAdapter);
        // 轮播图数据
        mData = DemoDataProvider.getBannerList();
        // 轮播图开始滚动
        rib_simple_usage.setSource(mData).startScroll();
    }

    @Override
    protected void initListeners() {
        initListener();
        mListViewSetOnItemClickListener();
        rib_simple_usage.setOnItemClickListener(new BaseBanner.OnItemClickListener<BannerItem>() {
            @Override
            public void onItemClick(View view, BannerItem item, int position) {
                Toast.makeText(getContext(), item.imgUrl, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMusicAdapter() {
        if (app.getMusicFileNameFindList().size() > 0 && app.getMusicFileNameFind().equals("") == false) {
            musicFileNameFindList = app.getMusicFileNameFindList();
            toolbarTitle.setText(app.getMusicFileNameFind());
            musicAdapter = new MusicAdapter(getActivity(), musicFileNameFindList, musicFilePathList, app.getUserId());
        }
    }

    private void initListener() {
        toolbarTitle.setOnEditorActionListener((v, actionId, event) -> {
            // 输入法中点击搜索
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //这里调用搜索方法
                resetListView();
            }
            return false;
        });
        toolbarTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                resetListView();
            }
        });
        //findMusicBtn.setOnClickListener(v -> resetListView());
        serviceConnection = MusicServiceConnection.getInstance(getContext());
    }

    private void mListViewSetOnItemClickListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mediaPlayer.isPlaying() && clickListViewNumber > 0) {
                    try {
                        app.setConnected(false);
                        getActivity().unbindService(serviceConnection);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPlayingButton", false);
                app.setCurrentMusicList(musicFilePathList);
                app.setCurrentMusicNameList(musicFileNameList);
                app.setCurrentMusicPosition(position);
                app.setFindMusic(true);
                Intent intent = new Intent(getContext(), MusicService.class);
                intent.putExtras(bundle);
                getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                app.setConnected(true);
                clickListViewNumber++;
            }
        });
    }

    private void resetListView() {
        toolbarText = toolbarTitle.getText().toString();
        if (toolbarText.isEmpty()) {
            musicAdapter = new MusicAdapter(getActivity(), musicFileNameList, musicFilePathList, app.getUserId());
            mListView.setAdapter(musicAdapter);
            mListViewSetOnItemClickListener();
            return;
        }
        findMusicInMusicList(toolbarText);
        musicAdapter = new MusicAdapter(getActivity(), musicFileNameFindList, musicFilePathList, app.getUserId());
        mListView.setAdapter(musicAdapter);
        mListViewSetOnItemClickListener();
    }

    private void findMusicInMusicList(String musicName) {
        musicFileNameFindList.clear();
        for (int i = 0; i < musicFileNameList.size(); i++) {
            if (musicFileNameList.get(i).contains(musicName)) {
                musicFileNameFindList.add(musicFileNameList.get(i));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        app.setMusicFileNameFindList(musicFileNameFindList);
        app.setMusicFileNameFind(toolbarText);
        if (mediaPlayer.isPlaying() && clickListViewNumber > 0) {
            try {
                app.setConnected(false);
                getActivity().unbindService(serviceConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        rib_simple_usage.recycle();
        super.onDestroyView();
    }

    private List<String> getMusicFileNameList() {
        Log.d("music", getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath());
        List<String> musicFileNameList = FileUtil.listMusicFileName(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC));
        return musicFileNameList;
    }

    private ArrayList<String> getMusicFilePathList() {
        ArrayList<String> musicFilePathList = FileUtil.listMusicFilePath(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC));
        return musicFilePathList;
    }
}