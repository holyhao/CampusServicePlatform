package com.bdyjy.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdyjy.R;
import com.loopj.android.image.SmartImageView;

public class NoticeListViewAdapter extends BaseAdapter {
	private Context context; // 运行上下文

	private List<Map<String, Object>> noticeListItems; // 新闻信息集合
	private LayoutInflater noticeListContainer; // 视图容器

	public final class ListItemView { // 自定义控件集合
		// public SmartImageView image;
		public TextView title;
		public TextView tv_notice_id;
		public TextView sourcefrom;
		public TextView date;
	}

	public NoticeListViewAdapter(Context context, List<Map<String, Object>> noticeListItems) {
		this.context = context;
		noticeListContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.noticeListItems = noticeListItems;
	}

	@Override
	public int getCount() {
		return noticeListItems.size();
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
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = noticeListContainer.inflate(R.layout.notice_item, null);
			// 获取控件对象
			// 图片问题待定
			// listItemView.image = (SmartImageView)
			// convertView.findViewById(R.id.notice_item_image);
			listItemView.title = (TextView) convertView.findViewById(R.id.notice_item_title);
			listItemView.tv_notice_id = (TextView) convertView.findViewById(R.id.tv_notice_id);
			listItemView.sourcefrom = (TextView) convertView.findViewById(R.id.notice_item_college);
			listItemView.date = (TextView) convertView.findViewById(R.id.notice_item_date);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		// try {
		// listItemView.image.setImageUrl((String)
		// noticeListItems.get(position).get("http"), R.drawable.add_image2);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		listItemView.title.setText((String) noticeListItems.get(position).get("title"));
		listItemView.tv_notice_id.setText((String) noticeListItems.get(position).get("id"));
		listItemView.sourcefrom.setText((String) noticeListItems.get(position).get("sourcefrom"));
		listItemView.date.setText((String) noticeListItems.get(position).get("date"));

		return convertView;
	}

	public void refresh(List<Map<String, Object>> listItems) {
		// 编译this.listItems
		for (Map<String, Object> map : this.noticeListItems) {
			System.out.println("map:" + map);
		}

		notifyDataSetChanged();
	}

}
