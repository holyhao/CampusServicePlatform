package com.bdyjy.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bdyjy.R;
import com.bdyjy.util.PropetiesFileReaderUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 南燕要闻新闻的adapter
 * 
 * @author 周航<br/>
 *         create at 2016-4-28 上午11:31:52
 */
public class NanyanNewsListViewAdapter extends BaseAdapter
{
	private Context context; // 运行上下文

	private List<Map<String, Object>> listItems; // 新闻信息集合
	private LayoutInflater listContainer; // 视图容器
	private ListView lv;

	public final class ListItemViewHolder
	{ // 自定义控件集合
		public com.loopj.android.image.SmartImageView image;
		public TextView title;
		public TextView tv_news_id;
		public TextView tv_sourcefrom;
		public TextView tv_date;
	}

	public NanyanNewsListViewAdapter(Context context,
			List<Map<String, Object>> listItems, ListView lv)
	{
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
		this.lv = lv;
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
		ListItemViewHolder listItemViewHolder = null;
		if (convertView == null)
		{
			listItemViewHolder = new ListItemViewHolder();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.list_item, null);
			// 获取控件对象
			listItemViewHolder.image = (com.loopj.android.image.SmartImageView) convertView
					.findViewById(R.id.imageItem);
			listItemViewHolder.title = (TextView) convertView
					.findViewById(R.id.titleItem);
			listItemViewHolder.tv_news_id = (TextView) convertView
					.findViewById(R.id.tv_news_id);
			listItemViewHolder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);
			listItemViewHolder.tv_sourcefrom = (TextView) convertView
					.findViewById(R.id.tv_sourcefrom);
			// 设置控件集到convertView
			convertView.setTag(listItemViewHolder);
		} else
		{
			listItemViewHolder = (ListItemViewHolder) convertView.getTag();
		}

		// 设置文字和图片
		String thumb = (String) listItems.get(position).get("thumb");
		// 如果图片thumb为空。就不显示图片layout
//		if (TextUtils.isEmpty(thumb))
//		{
//		//			listItemViewHolder.image.setVisibility(View.INVISIBLE);
//		listItemViewHolder.image.setImageResource(R.drawable.default_image);
//		} else
//		{
////			System.out.println("这里要显示图片:"+thumb);
//			//listItemViewHolder.image.setImageUrl(thumb, R.drawable.default_image);
//			
//		}
		Glide
        .with(context)
        .load(thumb)
        .centerCrop()
        .dontAnimate()
        .diskCacheStrategy(DiskCacheStrategy.RESULT )
        .placeholder(R.drawable.default_image)
        .error(R.drawable.default_image)
        .into(listItemViewHolder.image);
		
		
		listItemViewHolder.title.setText((String) listItems.get(position).get(
				"title"));
		listItemViewHolder.tv_news_id.setText((String) listItems.get(position)
				.get("news_id"));
		listItemViewHolder.tv_sourcefrom.setText((String) listItems.get(
				position).get("sourcefrom"));
		listItemViewHolder.tv_date.setText((String) listItems.get(position)
				.get("date"));

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
	
	
//	public Bitmap returnBitMap(String url)
//	{
//		URL myFileUrl = null;
//		Bitmap bitmap = null;
//		try
//		{
//			myFileUrl = new URL(url);
//		} catch (MalformedURLException e)
//		{
//			e.printStackTrace();
//		}
//		try
//		{
//			HttpURLConnection conn = (HttpURLConnection) myFileUrl
//					.openConnection();
//			conn.setDoInput(true);
//			conn.connect();
//			InputStream is = conn.getInputStream();
//			bitmap = BitmapFactory.decodeStream(is);
//			is.close();
//
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		return bitmap;
//	}
}
