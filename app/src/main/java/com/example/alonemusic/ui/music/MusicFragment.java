package com.example.alonemusic.ui.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.activity.MusicActivity;
import com.example.alonemusic.adapter.MusicAdapter;
import com.example.alonemusic.bean.LoveMusic;
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.MusicDao;
import com.example.alonemusic.dao.UserDao;
import com.example.alonemusic.service.MusicPlayer;
import com.example.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MusicFragment extends Fragment {

    private GlobalApplication app;
    private MusicDao musicDao;
    private TextView toolbarTitle;
    private List<String> musicFileNameList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private ListView mListView = null;
    private Button playing;
    private Button stopBtn;
    private int position;
    private SeekBar seekBar;
    private ArrayList<String> loveMusicFilePathList;
    private List<LoveMusic> loveMusicList;
    private LinearLayout loginLayout;
    private LinearLayout logoutLayout;
    private UserDao userDao;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music, container, false);

        app = (GlobalApplication) getActivity().getApplication();
        loginLayout = view.findViewById(R.id.music_login_layout);
        logoutLayout = view.findViewById(R.id.music_logout_layout);
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
        toolbarTitle.setText("Music");
        initAttributeListener(view);

        mListView = view.findViewById(R.id.list_music);
        loveMusicList = musicDao.queryLoveMusicList();
        musicFileNameList = getLoveMusicNameList();
        loveMusicFilePathList = getLoveMusicPathList();
        MusicAdapter musicAdapter = new MusicAdapter(getActivity(), musicFileNameList, loveMusicFilePathList, app.getUserId());
        mListView.setAdapter(musicAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPlayingButton", false);
                bundle.putInt("position", position);
                bundle.putStringArray("musicFileNames", musicFileNameList.toArray(new String[]{}));
                bundle.putStringArrayList("loveMusicFilePathList", loveMusicFilePathList);
                Intent intent = new Intent(getContext(), MusicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        return view;
    }

    private List<String> getLoveMusicNameList() {
        ArrayList<String> loveMusicNameList = new ArrayList<>();
        for(int i = 0; i < loveMusicList.size(); i++){
            loveMusicNameList.add(loveMusicList.get(i).getName());
        }
        return loveMusicNameList;
    }

    // TODO 获取喜欢的音乐
    private ArrayList<String> getLoveMusicPathList() {
        ArrayList<String> loveMusicPathList = new ArrayList<>();
        for(int i = 0; i < loveMusicList.size(); i++){
            loveMusicPathList.add(loveMusicList.get(i).getPath());
        }
        return loveMusicPathList;
    }

    private void initAttributeListener(View view){
        stopBtn = view.findViewById(R.id.stop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stopBtn.getText().equals("开始")){
                    stopBtn.setText("暂停");
                    mediaPlayer.start();
                    return;
                }
                if(stopBtn.getText().equals("暂停")){
                    stopBtn.setText("开始");
                    mediaPlayer.pause();
                    return;
                }
            }
        });
        playing = view.findViewById(R.id.playing_music);
        playing.setText("请选择歌曲");
        playing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playing.getText().equals("请选择歌曲") == false){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isPlayingButton", true);
                    bundle.putInt("position", position);
                    bundle.putStringArray("musicFileNames", musicFileNameList.toArray(new String[]{}));
                    bundle.putStringArrayList("loveMusicFilePathList", loveMusicFilePathList);
                    Intent intent = new Intent(getContext(), MusicActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            }
        });
        seekBar = view.findViewById(R.id.seek_music_fragment);
        if(app.getIsPlayingMusicName().equals("") == false){
            playing.setText(app.getIsPlayingMusicName());
        }
    }

    @Override
    public void onStart() {
        mediaPlayer = MusicPlayer.getMusicPlayer();
        new HandlerAsyncTask().execute(1000);
        if(mediaPlayer.isPlaying()){
            stopBtn.setText("暂停");
        }else {
            stopBtn.setText("开始");
        }
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            position = data.getIntExtra("position", 0);
            playing.setText(musicFileNameList.get(position));
        }
    }

    private class HandlerAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            //第二个执行方法,onPreExecute()执行完后执行
            try {
                Thread.sleep(params[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (mediaPlayer.isPlaying()){
                publishProgress(100 * mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration());
            }
            return "执行完毕";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            seekBar.setProgress(progress[0]);
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }
}