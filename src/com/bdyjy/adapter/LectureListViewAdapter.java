package com.bdyjy.adapter;

import java.util.List;
import java.util.Map;

import com.bdyjy.R;
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

public class LectureListViewAdapter extends BaseAdapter {
	private Context context; // 运行上下文

	private List<Map<String, Object>> lectureListItems; // 新闻信息集合
	private LayoutInflater lectureListContainer; // 视图容器

	public final class LectureListItemView { // 自定义控件集合
		public SmartImageView poster;
		public TextView title;
		public TextView lecPlace;
		public TextView lecTime;
		public TextView tv_lecture_id;
	}

	public LectureListViewAdapter(Context context, List<Map<String, Object>> lectureListItems) {
		this.context = context;
		lectureListContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.lectureListItems = lectureListItems;
	}

	@Override
	public int getCount() {
		return lectureListItems.size();
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
		LectureListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new LectureListItemView();
			// 获取list_item布局文件的视图
			convertView = lectureListContainer.inflate(R.layout.lecture_item, null);
			// 获取控件对象
			listItemView.poster = (SmartImageView) convertView.findViewById(R.id.lecture_image_Item);
			listItemView.title = (TextView) convertView.findViewById(R.id.lecture_title_item);
			listItemView.lecPlace = (TextView) convertView.findViewById(R.id.lecture_location_item);
			listItemView.lecTime = (TextView) convertView.findViewById(R.id.lecture_date_item);
			listItemView.tv_lecture_id = (TextView) convertView.findViewById(R.id.tv_lecture_id);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (LectureListItemView) convertView.getTag();
		}

		// 设置文字和图片

		try {
		//	listItemView.poster.setImageUrl((String) lectureListItems.get(position).get("http"), R.drawable.default_image);
			Glide
	        .with(context)
	        .load((String) lectureListItems.get(position).get("http"))
	        .centerCrop()
	        .dontAnimate()
	        .diskCacheStrategy(DiskCacheStrategy.RESULT )
	        .placeholder(R.drawable.default_image)
	        .error(R.drawable.default_image)
	        .into(listItemView.poster);
		} catch (Exception e) {
			e.printStackTrace();
		}
		listItemView.title.setText((String) lectureListItems.get(position).get("title"));
		listItemView.tv_lecture_id.setText((String) lectureListItems.get(position).get("tv_lecture_id"));

		listItemView.lecTime.setText((String) lectureListItems.get(position).get("lecTime"));
		listItemView.lecPlace.setText((String) lectureListItems.get(position).get("lecPlace"));

		return convertView;
	}

	public void refresh(List<Map<String, Object>> listItems) {
		// 编译this.listItems
		for (Map<String, Object> map : this.lectureListItems) {
			System.out.println("map:" + map);
		}

		notifyDataSetChanged();
	}

}
