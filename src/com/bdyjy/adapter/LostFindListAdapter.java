package com.bdyjy.adapter;

import java.util.List;
import java.util.Map;

import com.bdyjy.R;
import com.bdyjy.adapter.SecondhandMarketAdapter.ListItemView;
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
import java.util.ArrayList;

public class LostFindListAdapter extends BaseAdapter {
	
	private static final String ArrayList = null;

	private Context context; // 运行上下文

	private List<Map<String, Object>> listItems; // 新闻信息集合
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView
	{ // 自定义控件集合
		public SmartImageView image;
		public TextView title;
		public TextView tv_lostfind_id;
		public TextView tv_showtype;
		public TextView tv_datetime;
		public TextView tv_statusShow;
		
		
		
	}

	public LostFindListAdapter(Context context, List<Map<String, Object>> listItems)
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
			convertView = listContainer.inflate(R.layout.lostfind_list_item, null);
			// 获取控件对象
			listItemView.image = (SmartImageView) convertView
					.findViewById(R.id.iv_lostfind_item);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.tv_lostfind_title_item);
			listItemView.tv_lostfind_id = (TextView) convertView
					.findViewById(R.id.tv_lostfind_id_item);
			listItemView.tv_showtype = (TextView) convertView
					.findViewById(R.id.tv_showtype_lostfind_item);
			listItemView.tv_datetime= (TextView) convertView
					.findViewById(R.id.tv_datetime_lostfind_item);
			listItemView.tv_statusShow= (TextView) convertView
					.findViewById(R.id.tv_statusshow_lostfind_item);						
			
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else
		{
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
//		listItemView.image.setImageUrl((String) listItems.get(position)
//		.get("http"), R.drawable.default_image);
		
		 Glide
	       .with(context)
          .load((String) listItems.get(position).get("http"))
          .placeholder(R.drawable.default_image)
          .dontAnimate()
          .centerCrop()
          .diskCacheStrategy( DiskCacheStrategy.RESULT)
          .error(R.drawable.default_image) 
          .into(listItemView.image);
		
		
		listItemView.title.setText((String) listItems.get(position)
				.get("title"));
		listItemView.tv_lostfind_id.setText((String) listItems.get(position).get(
				"id"));
		listItemView.tv_showtype.setText("["+(String) listItems.get(position).get(
				"showtype")+"]"); 
		listItemView.tv_datetime.setText((String) listItems.get(position).get(
				"time"));
		listItemView.tv_statusShow.setText((String) listItems.get(position).get(
				"statusShow"));
		
		return convertView;
	}
	

	public void refresh(List<Map<String, Object>> listItems)
	{
		// 编译this.listItems
		for (Map<String, Object> map : this.listItems)
		{
			//System.out.println("map:" + map);
		}

		notifyDataSetChanged();
	}	

}
