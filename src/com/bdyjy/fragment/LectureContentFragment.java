package com.bdyjy.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.lecture.Lecture;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.fragment.base.FragmentWithCollection;
import com.loopj.android.image.SmartImageView;

public class LectureContentFragment extends FragmentWithCollection
{

	private TextView tv_lecture_back;
	private MainActivity ctx;
	private int source=0;//用于标记返回途径

	public LectureContentFragment(final MainActivity ctx)
	{
		super(ctx, CollectionTypeConst.LECTURE);
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	public LectureContentFragment(final MainActivity ctx,int source)
	{
		super(ctx, CollectionTypeConst.LECTURE);
		this.ctx = ctx;
		this.source=source;
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
			ctx.jumpToLectureListFragment();
		}
			
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.lecture_content, null);

		view.findViewById(R.id.ll_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Back(); //返回
					}
				});

		SmartImageView poster = (SmartImageView) view
				.findViewById(R.id.lecture_content_image);
		TextView title = (TextView) view
				.findViewById(R.id.lecture_content_title);
		TextView speaker = (TextView) view
				.findViewById(R.id.lecture_content_name);
		TextView lecTime = (TextView) view
				.findViewById(R.id.lecture_content_time);
		TextView lecPlace = (TextView) view
				.findViewById(R.id.lecture_content_location);

		WebView content = (WebView) view
				.findViewById(R.id.lecture_content_webview);
		content.getSettings().setDefaultTextEncodingName("UTF-8");
		final Lecture n = MainActivity.lecture;

		String http = "";
		String http2 = "";
		String http1 = "";
		try
		{
			http1 = n.getPoster().toString().trim();
			http2 = n.getAttachmentPrefix();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// 图片地址
		http = http2 + http1;

		if (http != null)
		{
			String path = http;
//			SmartImageView image2 = (SmartImageView) view
//					.findViewById(R.id.image2);
			poster.setImageUrl(path);
		}

		title.setText(n.getTitle());
		speaker.setText(n.getSpeaker());
		lecTime.setText(n.getLecTime());
		lecPlace.setText(n.getLecPlace());
		
		
		String sHead=	"<html><head><meta name=\"viewport\" content=\"width=device-width, " +
				"initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" />"+
				"<style>img{max-width:100% !important;height:auto !important;}</style>"
				+"<style>body{max-width:100% !important;}</style>"+"</head><body>"+n.getContent()+"</body></html>";
		Log.e("px", n.getContent());
		content.loadDataWithBaseURL(null, sHead, "text/html", "UTF-8",
				null);

		// 以下到结尾是收藏相关的代码
		super.setMainView(view);
		dealCollect(n.getId(), n.getIsCollect(), n.getTitle());
		return view;
	}

}
