package com.example.alonemusic.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.activity.LoginActivity;
import com.example.alonemusic.activity.MusicActivity;
import com.example.alonemusic.service.MusicService;
import com.example.alonemusic.service.MusicServiceConnection;
import com.example.alonemusic.ui.find.FindFragment;
import com.example.util.DBHelper;
import com.example.util.TipDialog;

import java.util.List;

/**
 * 自定义视图
 *
 */
public class MusicAdapter extends BaseAdapter {

	private GlobalApplication app;
	private DBHelper dbHelper;
	private Context mContext;
	private List<String> fileList;
	private Integer userId;
	private ServiceConnection serviceConnection;

	public MusicAdapter(Context context, List<String> fileList, Integer userId) {
		this.app = (GlobalApplication) ((Activity)context).getApplication();
		this.mContext = context;
		this.fileList = fileList;
		this.userId = userId;
		this.serviceConnection = MusicServiceConnection.getInstance(context);
	}

	/**
	 * 元素的个数
	 */
	public int getCount() {
		return fileList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// 用以生成在ListView中展示的一个个元素View
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 优化ListView
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.music_item,null);
			ItemViewCache viewCache = new ItemViewCache();
			viewCache.mTextView = convertView.findViewById(R.id.itemTxt);
			viewCache.imageButton = convertView.findViewById(R.id.music_love);
			convertView.setTag(viewCache);
		}
		ItemViewCache cache = (ItemViewCache) convertView.getTag();
		// 设置文本和图片，然后返回这个View，用于ListView的Item的展示
		cache.mTextView.setText(fileList.get(position));
		dbHelper = new DBHelper(mContext);
		cache.imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ContentValues values = new ContentValues();
				values.put("user_id", userId);
				values.put("name", fileList.get(position));
				dbHelper.insert("tb_music_love", values);
			}
		});
		return convertView;
	}

	// 元素的缓冲类,用于优化ListView
	private class ItemViewCache {
		public TextView mTextView;
		public ImageButton imageButton;
	}
}
