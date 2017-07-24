package com.bdyjy.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.image.SmartImageView;

import com.bdyjy.R;
import com.bdyjy.constants.HandlerOrder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class SecondhandMarketAdapter extends BaseAdapter
{
	private Context context; // 运行上下文

	private List<Map<String, Object>> listItems; // 新闻信息集合
	private LayoutInflater listContainer; // 视图容器
	

	public final class ListItemView
	{ // 自定义控件集合
		public SmartImageView image;
		public TextView title;
		public TextView price;
		public TextView createTime;
		public TextView tv_flea_id;
		public TextView tv_statusShow;
		public TextView tv_goodsname;
		
	}
 
	public SecondhandMarketAdapter(Context context, List<Map<String, Object>> listItems)
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
		int sum;
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null)
		{
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.flea_list_item, null);
			// 获取控件对象
			listItemView.image = (SmartImageView) convertView
					.findViewById(R.id.iv_flea_item);
			listItemView.tv_statusShow = (TextView) convertView
					.findViewById(R.id.statusShow);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.tv_flea_item);
			listItemView.price= (TextView) convertView
					.findViewById(R.id.tv_flea_price_item);
			listItemView.createTime= (TextView) convertView
					.findViewById(R.id.tv_flea_date_item);
		   listItemView.tv_flea_id = (TextView) convertView
				    .findViewById(R.id.tv_flea_id_item);
		   listItemView.tv_goodsname = (TextView) convertView
				    .findViewById(R.id.tv_flea_goodsname);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else
		{
			listItemView = (ListItemView) convertView.getTag();
		}
      
	
		// 设置文字和图片
		try{
//		listItemView.image.setImageUrl((String) listItems.get(position)
//		.get("http"), R.drawable.default_image);
		String ss=(String) listItems.get(position).get("http");
			if( ss.equals("")||ss==null)
			{
				LayoutParams para;  
		        para = listItemView.image.getLayoutParams();  		          
		        para.height =(int) context. getResources().getDimension(R.dimen.y46);
		        para.width =(int) context.getResources().getDimension(R.dimen.x70);  
		        listItemView.image.setLayoutParams(para);  
		        Glide
			     .with(context)
		         .load((String) listItems.get(position).get("http"))
		         .placeholder(R.drawable.default_image)
		         .dontAnimate()
		         .centerCrop()
		         .diskCacheStrategy( DiskCacheStrategy.RESULT)
		         .error(R.drawable.default_image) 
		         .into(listItemView.image);

			}else
			{
				Glide
			      .with(context)
		         .load((String) listItems.get(position).get("http"))
		         .placeholder(R.drawable.default_image)
		         .dontAnimate()
		         .centerCrop()
		         .diskCacheStrategy( DiskCacheStrategy.RESULT)
		         .error(R.drawable.default_image) 
		         .into(listItemView.image);
			}
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
			
		
		listItemView.title.setText((String) listItems.get(position)
				.get("title"));
//		listItemView.price.setText("￥ "+(String) listItems.get(position)
//				.get("price"));
		String tv_priceTextView=(String) listItems.get(position).get("price");
		sum=tv_priceTextView.length();
		if(sum>7)
		{
			listItemView.price.setText("￥ "+tv_priceTextView.substring(0,7)+"...");
		}else
		{
			listItemView.price.setText("￥"+tv_priceTextView);
		}
		
		
		listItemView.createTime.setText((String) listItems.get(position)
				.get("time"));
		listItemView.tv_flea_id.setText((String) listItems.get(position).get(
				"id"));
		String tv_goodsname=(String) listItems.get(position).get(
				"tel");
	    sum=tv_goodsname.length();
		if(sum>8)
		{
			listItemView.tv_goodsname.setText("["+tv_goodsname.substring(0,8)+"..."+"]");
		}else
		{
			listItemView.tv_goodsname.setText("["+tv_goodsname+"]");
		}
		
		listItemView.tv_statusShow.setText((String)listItems.get(position).get("statusShow"));
		
		
		
		
		
//		String status=(String)listItems.get(position).get("status");
//		String status1=(String)listItems.get(position).get("statusShow");
//		listItemView.tv_statusShow.setText(status1);
//		try {
//			Log.v("statusShow的值是", status1);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		int a=0;
//		try {
//		    a = Integer.parseInt(status);
//		} catch (NumberFormatException e) {
//		    e.printStackTrace();
//		}
//		
//		System.out.println("status的值是"+status);
//		if(a==1){
//			listItemView.tv_statusShow.setText("发布中");	
//		}else if(a==2){
//			listItemView.tv_statusShow.setText("已截止");	
//		}else{
//			listItemView.tv_statusShow.setText("发布中");			
//		}



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
