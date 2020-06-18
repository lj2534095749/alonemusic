package com.example.alonemusic.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.MusicDao;
import com.example.alonemusic.dao.UserDao;
import com.example.util.TipDialog;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;

import java.util.List;

/**
 * 自定义视图
 *
 */
public class MusicAdapter extends BaseAdapter {

	private GlobalApplication app;
	private Context mContext;
	private List<String> fileNameList;
	private List<String> filePathList;
	private Integer userId;
	private ItemViewCache cache;
	private UserDao userDao;
	private User lastUser;
	private MusicDao musicDao;

	public MusicAdapter(Context context, List<String> fileNameList, Integer userId) {
		this.app = (GlobalApplication) ((Activity)context).getApplication();
		this.mContext = context;
		this.fileNameList = fileNameList;
		this.userId = userId;
	}

	public MusicAdapter(Context context, List<String> fileNameList, List<String> filePathList, Integer userId) {
		this.app = (GlobalApplication) ((Activity)context).getApplication();
		this.mContext = context;
		this.fileNameList = fileNameList;
		this.filePathList = filePathList;
		this.userId = userId;
	}

	/**
	 * 元素的个数
	 */
	public int getCount() {
		return fileNameList.size();
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
		cache = (ItemViewCache) convertView.getTag();
		// 设置文本和图片，然后返回这个View，用于ListView的Item的展示
		cache.mTextView.setText(fileNameList.get(position));
		cache.imageButton.setTag("color = deepBlue");
		userDao = new UserDao(mContext);
		musicDao = new MusicDao(mContext);
		lastUser = userDao.findLastUser();
		if(lastUser != null){
			app.setUserId(lastUser.getId());
		}
		if(musicDao.hasLoveMusicInLoveMusicList(fileNameList.get(position))){
			cache.imageButton.setTag("color = red");
			cache.imageButton.setChecked(true);
			//cache.imageButton.setColorFilter(ContextCompat.getColor(mContext, R.color.colorRed));
		}
		cache.imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(lastUser != null){
					if(lastUser.getState() != 0){
						if(v.getTag().equals("color = deepBlue")){
							ContentValues values = new ContentValues();
							values.put("user_id", userId);
							values.put("name", fileNameList.get(position));
							values.put("path", filePathList.get(position));
							values.put("state", 1);
							musicDao.insertLoveMusic(values);
							v.setTag("color = red");
							//((ImageButton)v).setColorFilter(ContextCompat.getColor(mContext, R.color.colorRed));
						}else {
							ContentValues values = new ContentValues();
							values.put("state", 0);
							musicDao.updateLoveMusicByName(values, fileNameList.get(position));
							v.setTag("color = deepBlue");
							//((ImageButton)v).setColorFilter(ContextCompat.getColor(mContext, R.color.colorDeepBlue));
						}
					}else {
						TipDialog.showNormalDialog(mContext, "登录后才能收藏");
					}
				}else {
					TipDialog.showNormalDialog(mContext, "登录后才能收藏");
				}
			}
		});
		return convertView;
	}

	// 元素的缓冲类,用于优化ListView
	private class ItemViewCache {
		public TextView mTextView;
		public ShineButton imageButton;
	}
}
