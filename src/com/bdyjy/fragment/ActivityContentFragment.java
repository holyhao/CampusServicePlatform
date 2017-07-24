package com.bdyjy.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.entity.activity.Activity;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.fragment.base.FragmentWithCollection;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.image.SmartImageView;

/**
 * 社团活动内容
 * 
 * @author cuicui create at 2016-04-02 21:39
 * 
 */
public class ActivityContentFragment extends FragmentWithCollection
{
	// private TextView tv_activity_content_back;
	private MainActivity ctx;
	private int source;

	public ActivityContentFragment(MainActivity ctx)
	{
		super(ctx, CollectionTypeConst.ACTIVITIES);
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	
	public ActivityContentFragment(MainActivity ctx,int source)
	{
		super(ctx, CollectionTypeConst.ACTIVITIES);
		this.source=source;
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	/**********返回功能**********/
	private void Back(){ 
		if(source==1)
		{
			ctx.jumpToFirstPageFregment();
		}else
		{
		if ("myCollectionList".equals(MainActivity.orderFrom))
		{
			ctx.jumpToMyCollectionListFragment();
			MainActivity.orderFrom = "";
		} else if ("firstPage".equals(MainActivity.orderFrom))
		{
			ctx.jumpToFirstPageFregment();
			MainActivity.orderFrom = "";
		} else
			ctx.jumpToActivityListFragment();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_content, null);

		// tv_activity_content_back = (TextView) view
		// .findViewById(R.id.tv_activity_content_back);

		view.findViewById(R.id.ll_activity_content_back).setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						if ("myCollectionList".equals(MainActivity.orderFrom))
						{
							ctx.jumpToMyCollectionListFragment();
							MainActivity.orderFrom = "";
						} else if ("firstPage".equals(MainActivity.orderFrom))
						{
							ctx.jumpToFirstPageFregment();
							MainActivity.orderFrom = "";
						} else
							ctx.jumpToActivityListFragment();
					}
				});

		SmartImageView poster = (SmartImageView) view
				.findViewById(R.id.activity_content_poster);
		TextView subject = (TextView) view
				.findViewById(R.id.activity_content_subject);
		TextView hoster = (TextView) view
				.findViewById(R.id.activity_content_hoster);
		TextView actTime = (TextView) view
				.findViewById(R.id.activity_content_actTime);
		TextView actPlace = (TextView) view
				.findViewById(R.id.activity_content_actPlace);
		WebView content = (WebView) view
				.findViewById(R.id.activity_content_detail);
		content.getSettings().setDefaultTextEncodingName("UTF-8");

		Activity n = MainActivity.activity;

		String http = "";
		String http2 = "";
		String http1 = "";
		try
		{
			http1 = MainActivity.activity.getPoster().toString().trim();
			http2 = MainActivity.activity.getAttachmentPrefix();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// 图片地址
		http = http2 + http1;
		System.out.println("社团活动图片的地址为：" + http);

		if (http != null)
		{
			String path = http;
//			SmartImageView image2 = (SmartImageView) view
//					.findViewById(R.id.image2);
		//	poster.setImageUrl(path);
			Glide.with(this)  
		     .load(path)  
		     .dontAnimate()
		     .diskCacheStrategy(DiskCacheStrategy.RESULT)  
		     .into(poster); 
		}

		subject.setText(n.getSubject());
		hoster.setText(n.getHoster());
		actTime.setText(n.getActtime());
		actPlace.setText(n.getActplace());
		content.loadDataWithBaseURL(null, n.getContent(), "text/html", "UTF-8",
				null);

		super.setMainView(view);
		dealCollect(n.getId(), n.getIsCollect(), n.getSubject());
		return view;
	}
}
