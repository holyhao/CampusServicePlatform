package com.bdyjy.adapter;

import java.util.List;
import java.util.Map;

import com.bdyjy.R;
import com.bdyjy.fragment.base.CollectionTypeConst;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 招聘信息适配器
 * 
 * @author parle
 * 
 */
public class MyCollectionListAdapter extends BaseAdapter
{

	/**
	 * 原类为private 为了不修改原类
	 */
	private Context context; // 运行上下文
	private List<Map<String, Object>> listItems; // 新闻信息集合
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView
	{
		public TextView job;
		public TextView id;
		public TextView time;
		public TextView pubFrom;
		public TextView salary;
		public TextView tv_type;
	}

	// 调用父类构造方法就行了
	public MyCollectionListAdapter(Context context,
			List<Map<String, Object>> listItems)
	{
		this.listItems = listItems;
		this.context = context;
		listContainer = LayoutInflater.from(context);
	}

	/**
	 * 重写 ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{

		Log.e("method", "getView");
		final int selectID = position;

		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null)
		{
			listItemView = new ListItemView();

			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.recruit_list_item2,
					null);

			// 获取控件对象
			listItemView.job = (TextView) convertView
					.findViewById(R.id.recruit_list_item_job);

			listItemView.id = (TextView) convertView
					.findViewById(R.id.recruit_list_item_id);

			listItemView.time = (TextView) convertView
					.findViewById(R.id.recruit_list_item_time);

			listItemView.pubFrom = (TextView) convertView
					.findViewById(R.id.recruit_list_item_pubFrom);

			listItemView.salary = (TextView) convertView
					.findViewById(R.id.recruit_list_item_salary);

			listItemView.tv_type = (TextView) convertView
					.findViewById(R.id.tv_type);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else
		{
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		listItemView.job.setText((String) listItems.get(position).get("title"));

		listItemView.id.setText((String) listItems.get(position).get(
				"foreignId"));

		listItemView.time.setText((String) listItems.get(position).get(
				"createTime"));

		String type = (String) listItems.get(position).get("type");

		listItemView.tv_type.setText(type);
		listItemView.pubFrom.setText(CollectionTypeConst.getTypeTrans(Integer
				.parseInt(type)));

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
	public int getCount()
	{
		return listItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
