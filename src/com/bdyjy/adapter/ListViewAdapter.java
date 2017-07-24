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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ListViewAdapter extends BaseAdapter
{
	private Context context; // 运行上下文

	private List<Map<String, Object>> listItems; // 新闻信息集合
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView
	{ // 自定义控件集合
		public com.loopj.android.image.SmartImageView image;
		public TextView title;
		public TextView tv_news_id;
		public TextView tv_date;
		public TextView tv_sourcefrom;
	}

	public ListViewAdapter(Context context, List<Map<String, Object>> listItems)
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
//		Log.e("method", "getView");
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null)
		{
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.list_item, null);
			// 获取控件对象
			listItemView.image = (com.loopj.android.image.SmartImageView) convertView
					.findViewById(R.id.imageItem);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.titleItem);
			listItemView.tv_news_id = (TextView) convertView
					.findViewById(R.id.tv_news_id);
			listItemView.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);
			listItemView.tv_sourcefrom = (TextView) convertView
					.findViewById(R.id.tv_sourcefrom);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else
		{
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
//		System.out.println("xxxxxx-image:"
//				+ (String) listItems.get(position).get("image"));
//		listItemView.image.setImageUrl(
//				(String) listItems.get(position).get("image"),
//				R.drawable.default_image);
		Glide
        .with(context)
        .load((String) listItems.get(position).get("image"))
        .centerCrop()
        .dontAnimate()
        .diskCacheStrategy(DiskCacheStrategy.RESULT )
        .placeholder(R.drawable.default_image)
        .error(R.drawable.default_image)
        .into(listItemView.image);
		
		listItemView.tv_date.setText((String) listItems.get(position).get(
				"date"));
		listItemView.title.setText((String) listItems.get(position)
				.get("title"));
		listItemView.tv_news_id.setText((String) listItems.get(position).get(
				"news_id"));
		listItemView.tv_sourcefrom.setText((String) listItems.get(position)
				.get("pubFrom"));

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
