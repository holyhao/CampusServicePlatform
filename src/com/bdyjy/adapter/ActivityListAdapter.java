package com.bdyjy.adapter;

import java.util.List;
import java.util.Map;

import com.bdyjy.R;
import com.bdyjy.adapter.ListViewAdapter.ListItemView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 社团活动适配器
 * 
 * @author cuicui create at 2016-04-02 16:40
 *
 */
public class ActivityListAdapter extends BaseAdapter {

	private Context context; // 运行上下文

	private List<Map<String, Object>> activityListItems; // 活动信息集合
	private LayoutInflater activityListContainer; // 视图容器

	public final class ListItemView { // 自定义控件集合
		public SmartImageView poster;
		public TextView subject;
		public TextView tv_activity_id;
		public TextView hoster;
		public TextView actPlace;
		public TextView actTime;
	}

	public ActivityListAdapter(Context context, List<Map<String, Object>> activityListItems) {
		this.context = context;
		activityListContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.activityListItems = activityListItems;
	}

	@Override
	public int getCount() {
		return activityListItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.e("method", "getView");
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = activityListContainer.inflate(R.layout.activity_item, null);
			// 获取控件对象
			listItemView.poster = (SmartImageView) convertView.findViewById(R.id.activity_item_poster);
			listItemView.subject = (TextView) convertView.findViewById(R.id.activity_item_subject);
			listItemView.tv_activity_id = (TextView) convertView.findViewById(R.id.tv_activity_id);
			listItemView.hoster = (TextView) convertView.findViewById(R.id.activity_item_hoster);
			listItemView.actPlace = (TextView) convertView.findViewById(R.id.activity_item_actPlace);
			listItemView.actTime = (TextView) convertView.findViewById(R.id.activity_item_actTime);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片

		try {
			// listItemView.poster.setImageUrl((String)
			// activityListItems.get(position).get("http"),
			// R.drawable.default_image);
			Glide.with(context).load((String) activityListItems.get(position).get("http"))
					.error(R.drawable.default_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(listItemView.poster);
		} catch (Exception e) {
			e.printStackTrace();
		}
		listItemView.subject.setText((String) activityListItems.get(position).get("subject"));
		listItemView.tv_activity_id.setText((String) activityListItems.get(position).get("tv_activity_id"));
		listItemView.hoster.setText((String) activityListItems.get(position).get("hoster"));
		listItemView.actPlace.setText((String) activityListItems.get(position).get("actPlace"));
		listItemView.actTime.setText((String) activityListItems.get(position).get("actTime"));

		return convertView;
	}

	public void refresh(List<Map<String, Object>> listItems) {
		// 编译this.listItems
		for (Map<String, Object> map : this.activityListItems) {
			System.out.println("map:" + map);
		}

		notifyDataSetChanged();
	}
}
