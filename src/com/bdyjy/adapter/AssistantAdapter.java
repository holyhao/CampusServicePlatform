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
 * 勤工俭学适配器
 *
 * @author parle
 *
 */
public class AssistantAdapter extends BaseAdapter{
	
	/**
	 * 原类为private
	 * 为了不修改原类
	 */
	private Context context; // 运行上下文
	private List<Map<String, Object>> listItems; // 新闻信息集合
	private LayoutInflater listContainer; // 视图容器
	
	public final class ListItemView
	{ 	
		public TextView job;
		public TextView tv_news_id;
		// 发布时间
		public TextView time;
		// 发布单位
		public TextView pubFrom;
		// 岗位所需人数
		public TextView number;
	}
	
	// 调用父类构造方法就行了
	public AssistantAdapter(Context context, List<Map<String, Object>> listItems) {
		this.listItems = listItems;
		this.context = context;
		listContainer = LayoutInflater.from(context);
	}
	
	
	/**
	 * 重写
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.e("method", "getView");
		final int selectID = position;
		
		// 自定义视图
		ListItemView listItemView = null;
		
		if (convertView == null) {
			listItemView = new ListItemView();
			
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.assistant_list_item, null);
			
			// 获取控件对象
			listItemView.job = (TextView) convertView
					.findViewById(R.id.assistant_list_item_job);
			
			listItemView.tv_news_id = (TextView) convertView
					.findViewById(R.id.assistant_list_item_id);
			
			listItemView.time = (TextView) convertView
					.findViewById(R.id.assistant_list_item_time);
			
			listItemView.pubFrom = (TextView) convertView
					.findViewById(R.id.assistant_list_item_pubFrom);
			
			listItemView.number = (TextView) convertView
					.findViewById(R.id.assistant_list_item_number);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		listItemView.job.setText((String) listItems.get(position)
				.get("job"));
		
		listItemView.tv_news_id.setText((String) listItems.get(position).get(
				"id"));
		
		listItemView.time.setText((String) listItems.get(position)
				.get("time"));
		
		listItemView.pubFrom.setText("["+(String) listItems.get(position).get(
				"pubFrom")+"]");
		
		listItemView.number.setText((String) listItems.get(position)
				.get("number")+"人");

		return convertView;
	}

	public void refresh(List<Map<String, Object>> listItems)
	{
		// 编译this.listItems
		for (Map<String, Object> map : this.listItems)
		{
			System.out.println("map:" + map);
		}

		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return listItems.size();
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
