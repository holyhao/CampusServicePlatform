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
import com.bdyjy.activity.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.image.SmartImageView;

public class MyComplaintListAdapter extends BaseAdapter
{
	private Context context; // 运行上下文

	private List<Map<String, Object>> listItems; // 新闻信息集合
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView
	{ // 自定义控件集合
		public SmartImageView complaint_image;
		public TextView complaint_title;
		public TextView complaint_id;

		public TextView complaint_classify;
		public TextView complaint_status;
	}

	public MyComplaintListAdapter(Context context,
			List<Map<String, Object>> listItems)
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
		Log.e("method", "getView");
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null)
		{
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.mycompliant_list_item,
					null);
			// 获取控件对象
//			listItemView.complaint_image = (SmartImageView) convertView
//					.findViewById(R.id.mycomplaint_imageItem);
//			listItemView.complaint_status = (TextView) convertView
//					.findViewById(R.id.status);
			listItemView.complaint_title = (TextView) convertView
					.findViewById(R.id.mycomplaint_titleItem);
			listItemView.complaint_id = (TextView) convertView
					.findViewById(R.id.mycomplaint_id);
			listItemView.complaint_classify = (TextView) convertView
					.findViewById(R.id.mycomplaint_item_time);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else
		{
			listItemView = (ListItemView) convertView.getTag();
		}

//		// 设置文字和图片
//		try
//		{
////			listItemView.complaint_image.setImageUrl(
////					(String) listItems.get(position).get("http"),
////					R.drawable.default_image);
//			//使用Glide框架缓存
//
//			  Glide
//			       .with(context)
//		            .load((String) listItems.get(position).get("http"))
//		            .placeholder(R.drawable.default_image)
//		            .dontAnimate()
//		            .centerCrop()
//		            .diskCacheStrategy( DiskCacheStrategy.RESULT)
//		            .error(R.drawable.default_image) 
//		            .into(listItemView.complaint_image);	
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
		
		
		
		String tv_priceTextView=(String) listItems.get(position)
				.get("complaint_title");
		int sum;
		sum=tv_priceTextView.length();
		if(sum>7)
		{
			listItemView.complaint_title.setText(tv_priceTextView.substring(0,10)+"...");
		}else
		{
			listItemView.complaint_title.setText(tv_priceTextView);
		}
		
		
		listItemView.complaint_id.setText((String) listItems.get(position).get(
				"complaint_id"));
		listItemView.complaint_classify.setText((String) listItems
				.get(position).get("creationTime").toString().substring(0,10));
//		listItemView.complaint_status.setText((String) listItems.get(position)
//				.get("complaint_status"));

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
