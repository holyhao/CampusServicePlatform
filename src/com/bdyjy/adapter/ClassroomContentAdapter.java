package com.bdyjy.adapter;

import java.util.List;
import java.util.Map;

import com.bdyjy.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * 课程内容和时间
 * @author parle
 *
 */
public class ClassroomContentAdapter extends BaseAdapter{
	
	private Context context;
	private List<Map<String, Object>> listItems;
	private LayoutInflater inflater;
	
	// 时间和课程
	public final class ListItemView { 	
		public TextView courseTime;
		public TextView courseName;
	}	
	
	public ClassroomContentAdapter(Context context, List<Map<String, Object>> listItems) {
		this.listItems = listItems;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	// 视图填充
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("method", "getView");
		final int selectID = position;
		
		// 自定义视图
		ListItemView listItemView = null;
		
		// 获取控件
		if (convertView == null) {	
			
			listItemView = new ListItemView();
			
			// 获取list_item布局文件的视图
			convertView = inflater.inflate(R.layout.classroom_listitem_content, null);
			
			// 获取控件对象，教室名称
			listItemView.courseName = (TextView) convertView
					.findViewById(R.id.course_name);
			//listItemView.courseName.setMaxWidth(500);
			
			listItemView.courseTime = (TextView) convertView
					.findViewById(R.id.course_time);
		
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		
		// 设置控件内容
		listItemView.courseName.setText((String) listItems.get(position).get("courseName"));
		listItemView.courseTime.setText((String) listItems.get(position).get("courseTime"));

		return convertView;
	}
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}
	
	public void refresh(List<Map<String, Object>> listItems) {
		// 编译this.listItems
		for (Map<String, Object> map : this.listItems) {
			//System.out.println("map:" + map);
		}
		
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);  
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
}
