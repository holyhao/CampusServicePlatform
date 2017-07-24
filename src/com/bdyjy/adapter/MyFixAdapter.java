/**
 * 
 * 我的维护的报修列表的适配器
 * created by songdebin 
 * 2016-04-06
 * 
 * */


package com.bdyjy.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bdyjy.R;
import com.bdyjy.adapter.SecondhandMarketAdapter.ListItemView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bdyjy.entity.attArryData;


public class MyFixAdapter extends BaseAdapter{
	
	private Context context; // 运行上下文

	private List<Map<String, Object>> listItems; //信息集合
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView
	{ // 自定义控件集合
		public TextView tv_title;
		public TextView tv_time;
		public TextView tv_id;
	}

	public MyFixAdapter(Context context, List<Map<String, Object>> listItems)
	{
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
	}

	public int getCount()
	{
		return listItems.size();
	}

	public Object getItem(int arg0)
	{
		return null;
	}

	public long getItemId(int arg0)
	{
		return 0;
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		//Log.e("method", "getView");
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null)
		{
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.my_fix_item, null);
			// 获取控件对象
			listItemView.tv_id = (TextView) convertView
					.findViewById(R.id.tv_myfix_id_item);						
			listItemView.tv_title = (TextView) convertView
					.findViewById(R.id.tv_myfix_title_item);
			listItemView.tv_time = (TextView) convertView
					.findViewById(R.id.tv_myfix_datetime_item);
			convertView.setTag(listItemView);
		} else
		{
			listItemView = (ListItemView) convertView.getTag();
		}

		String tv_priceTextView=(String) listItems.get(position)
				.get("title");
		int sum;
		sum=tv_priceTextView.length();
		if(sum>7)
		{
			listItemView.tv_title.setText(tv_priceTextView.substring(0,10)+"...");
		}else
		{
			listItemView.tv_title.setText(tv_priceTextView);
		}
		// 设设置内容 和 发布日期
		listItemView.tv_id.setText((String) listItems.get(position)
				.get("id"));
//		listItemView.tv_title.setText((String) listItems.get(position)
//				.get("title"));
		
		try {
			String date=(String) listItems.get(position).get("time");//获取日期
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date); //将String格式  转化为date格式
			String now = new SimpleDateFormat("yyyy年MM月dd日").format(date1);//进行格式化
			listItemView.tv_time.setText((String) now);//赋值
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//dateFm.format(date);
		
       
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

}
