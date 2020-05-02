package com.example.alonemusic.ui.find;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.activity.MusicActivity;
import com.example.alonemusic.adapter.MusicAdapter;
import com.example.alonemusic.dao.UserDao;
import com.example.alonemusic.service.MusicPlayer;
import com.example.alonemusic.service.MusicService;
import com.example.alonemusic.service.MusicServiceConnection;
import com.example.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class FindFragment extends Fragment {

    private GlobalApplication app;

    private TextView toolbarTitle;
    private List<String> musicFileNameList = new ArrayList<>();
    private List<String> musicFileNameFindList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private ListView mListView = null;

    private ImageButton findMusicBtn;
    private EditText findMusicText;
    private MusicAdapter musicAdapter;
    private String findText;
    private ServiceConnection serviceConnection;
    private ArrayList<String> musicFilePathList;
    private int clickListViewNumber = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_find, container, false);
        app = (GlobalApplication) getActivity().getApplication();
        mediaPlayer = MusicPlayer.getMusicPlayer();
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Find");
        initAttribute(view);
        mListView = view.findViewById(R.id.find_list_music);
        musicFileNameList = getMusicFileNameList();
        musicFilePathList = getMusicFilePathList();
        musicAdapter = new MusicAdapter(getActivity(), musicFileNameList, musicFilePathList, app.getUserId());
        initMusicAdapter();
        mListView.setAdapter(musicAdapter);
        mListViewSetOnItemClickListener();
        return view;
    }

    private List<String> getMusicFileNameList() {
        List<String> musicFileNameList = FileUtil.listMusicFileName(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC));
        return musicFileNameList;
    }

    private ArrayList<String> getMusicFilePathList() {
        ArrayList<String> musicFilePathList = FileUtil.listMusicFilePath(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC));
        return musicFilePathList;
    }

    private void initMusicAdapter(){
        if(app.getMusicFileNameFindList().size() > 0 && app.getMusicFileNameFind().equals("") == false){
            musicFileNameFindList = app.getMusicFileNameFindList();
            findMusicText.setText(app.getMusicFileNameFind());
            musicAdapter = new MusicAdapter(getActivity(), musicFileNameFindList, musicFilePathList, app.getUserId());
        }
    }

    private void initAttribute(View view){
        findMusicBtn = view.findViewById(R.id.find_music_btn);
        findMusicText = view.findViewById(R.id.find_music_text);
        findMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetListView();
            }
        });
        serviceConnection = MusicServiceConnection.getInstance(getContext());
    }

    private void mListViewSetOnItemClickListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(mediaPlayer.isPlaying() && clickListViewNumber > 0){
                    try {
                        app.setConnected(false);
                        getActivity().unbindService(serviceConnection);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPlayingButton", false);
                bundle.putInt("position", position);
                bundle.putStringArrayList("musicFilePathList", musicFilePathList);
                app.setIsPlayingMusicName(musicFileNameList.get(position));
                Intent intent = new Intent(getContext(), MusicService.class);
                intent.putExtras(bundle);
                getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                app.setConnected(true);
                clickListViewNumber++;
            }
        });
    }

    private void resetListView(){
        findText = findMusicText.getText().toString();
        if(findText.isEmpty()){
            musicAdapter = new MusicAdapter(getActivity(), musicFileNameList, musicFilePathList, app.getUserId());
            mListView.setAdapter(musicAdapter);
            mListViewSetOnItemClickListener();
            return;
        }
        findMusicInMusicList(findText);
        musicAdapter = new MusicAdapter(getActivity(), musicFileNameFindList, musicFilePathList, app.getUserId());
        mListView.setAdapter(musicAdapter);
        mListViewSetOnItemClickListener();
    }

    private void findMusicInMusicList(String musicName){
        musicFileNameFindList.clear();
        for(int i = 0; i < musicFileNameList.size(); i++){
            if(musicFileNameList.get(i).contains(musicName)){
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
        app.setMusicFileNameFind(findText);
        if(mediaPlayer.isPlaying() && clickListViewNumber > 0){
            try {
                app.setConnected(false);
                getActivity().unbindService(serviceConnection);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}