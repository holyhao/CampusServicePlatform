package com.bdyjy.adapter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bdyjy.R;
import com.bdyjy.util.Util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ClassroomAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> contentList;
	private List<List<Map<String, Object>>> classroomList;
	private LayoutInflater inflater;
	
	public final class ListItemView { 	
		public TextView classroomName = null;
		public ListView courseList = null;
	}	
	
	public ClassroomAdapter(Context context, List<Map<String, Object>> contentList, List<List<Map<String, Object>>> classroomList) {
		this.context = context;
		this.classroomList = classroomList;
		this.contentList = contentList;
		this.inflater = LayoutInflater.from(context);
	}
	
	
	public void clearList() {  
        this.classroomList.clear();  
        this.contentList.clear();  
    }  
	
	public void updateList(List<Map<String, Object>> contentList, List<List<Map<String, Object>>> classroomList) {  
		this.classroomList.addAll(classroomList);
		this.contentList.addAll(contentList);
    }  
	  
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int selectID = position;
		
		ListItemView itemView = null;
		
		// 获取控件
		if(convertView == null) {
			itemView = new ListItemView();
			convertView = inflater.inflate(R.layout.classroom_query_list_item, null);
			itemView.courseList = (ListView)convertView.findViewById(R.id.course_list);
			itemView.classroomName = (TextView)convertView.findViewById(R.id.classroom_name);
			convertView.setTag(itemView);
		} else {
			itemView = (ListItemView) convertView.getTag();
		}
		
		
		// 设置控件内容
		itemView.classroomName.setText((String) classroomList.get(position).get(0).get("classroomName"));
		
		// 为课程ListView添加内容
		if (classroomList.get(position) != null)  {
			itemView.courseList.setAdapter(new ClassroomContentAdapter(context,  
					classroomList.get(position)));  
			Util.setListViewHeightBasedOnChildren(itemView.courseList);
		}
		
		return convertView;
	}
	
	
	
	public void refresh(List<List<Map<String, Object>>> classroomList) {
		// 编译this.listItems
		for (List<Map<String, Object>> list : this.classroomList) {
			System.out.println("list:" + list);
		}

		notifyDataSetChanged();
	}

	
	@Override
	public int getCount() {
		return classroomList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
