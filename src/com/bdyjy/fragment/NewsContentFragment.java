/**
 * Fragment2.java[v 1.0.0]
 * class:com.mydream.fragment.freg,Fragment2
 * 周航 create at 2016-3-22 下午7:54:01
 */
package com.bdyjy.fragment;

import android.annotation.SuppressLint;
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
import com.bdyjy.entity.News;
import com.bdyjy.entity.news.NewsDetail;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.fragment.base.FragmentWithCollection;

/**
 * 新闻内容，由新闻列表打开
 * 
 * @author 周航 create at 2016-3-22 下午7:54:01
 */
@SuppressLint("NewApi")
public class NewsContentFragment extends FragmentWithCollection
{
	private TextView tv_back;
	private MainActivity ctx;
	private int source=0;

	public NewsContentFragment(final MainActivity ctx)
	{
		super(ctx, CollectionTypeConst.NEWS);
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	 
	}
	
	public NewsContentFragment (final MainActivity ctx,int source){
		super(ctx, CollectionTypeConst.NEWS);
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
		if(source==1){
			ctx.jumpToFirstPageFregment();
		}else if(source==2)
		{
			ctx.jumpToMyCollectionListFragment();
		}else{
		ctx.jumpToNewsListFregment();
	    }
	}

	// private News n;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.news_content_fragment, null);
		view.findViewById(R.id.ll_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if(source==1){
							ctx.jumpToFirstPageFregment();
						}else if(source==2)
						{
							ctx.jumpToMyCollectionListFragment();
						}else{
						ctx.jumpToNewsListFregment();
						}
					}
				});

		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
		TextView tv_pub_from = (TextView) view.findViewById(R.id.tv_pub_from);

		WebView wv_content = (WebView) view.findViewById(R.id.wv_content);
		wv_content.getSettings().setLoadWithOverviewMode(true);
		wv_content.getSettings().setDefaultTextEncodingName("UTF-8");

		final News n = MainActivity.news;
		NewsDetail nd = MainActivity.newsDetail;

		if (n != null)
		{
			tv_title.setText(n.getTitle());
			tv_date.setText(n.getCreateTime().subSequence(0, 10));
			tv_pub_from.setText(n.getPubFrom());
			wv_content.loadDataWithBaseURL(null, getHtmlCode(n.getContent()),
					"text/html", "UTF-8", null);
			if("0".endsWith(n.getIsCollect()))
			{
				view.findViewById(R.id.ll_not_collect).setVisibility(View.VISIBLE);
				view.findViewById(R.id.ll_collect).setVisibility(View.GONE);
			}else
			{
				view.findViewById(R.id.ll_collect).setVisibility(View.VISIBLE);
				view.findViewById(R.id.ll_not_collect).setVisibility(View.GONE);
			}
			
			
			// 以下到结尾是收藏相关的代码
			super.setMainView(view);
			dealCollect(n.getId(),n.getIsCollect(), n.getTitle());
			
		} else if (nd != null)
		{
			tv_title.setText(nd.getTitle());
			tv_date.setText(nd.getDate());
			tv_pub_from.setText(nd.getSourcefrom());
			String sHead=	"<html><head><meta name=\"viewport\" content=\"width=device-width, " +
					"initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" />"+
					"<style>img{max-width:100% !important;height:auto !important;}</style>"
					+"<style>body{max-width:100% !important;}</style>"+"</head><body>";
			wv_content.loadDataWithBaseURL(null,sHead+nd.getContent()+"</body></html>",
					"text/html", "UTF-8", null);
			view.findViewById(R.id.ll_collect).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.ll_not_collect).setVisibility(View.GONE);
//			super.setMainView(view);
//			dealCollect(nd.getId(), "0", nd.getTitle());
		} else
		{
			System.out.println("都为空");
		}
	
		return view;
	}

	private String getHtmlCode(String content)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<style>img{width:100% !important;}</style>");
		sb.append(content);
		return sb.toString();
	}
}
