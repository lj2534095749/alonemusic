package com.example.alonemusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alonemusic.R;

import java.util.List;

/**
 * 自定义视图
 *
 */
public class MusicAdapter extends BaseAdapter {

	private Context mContext;

	private List<String> fileList;

	public MusicAdapter(Context context, List<String> fileList) {
		this.mContext = context;
		this.fileList = fileList;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// 优化ListView
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.music_item,null);
			ItemViewCache viewCache = new ItemViewCache();
			viewCache.mTextView = convertView.findViewById(R.id.itemTxt);
			convertView.setTag(viewCache);
		}
		ItemViewCache cache = (ItemViewCache) convertView.getTag();
		// 设置文本和图片，然后返回这个View，用于ListView的Item的展示
		cache.mTextView.setText(fileList.get(position));
		return convertView;
	}

	// 元素的缓冲类,用于优化ListView
	private class ItemViewCache {
		public TextView mTextView;
	}
}
