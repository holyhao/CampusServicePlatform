package com.bdyjy.fragment;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.ParleConstant;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 
 * @author parle
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class LibraryFragment extends Fragment
{

	private WebView webView;
	// 在主页的基础上替换fragment，保持底栏
	private MainActivity context;

	private TextView tv_back;

	public LibraryFragment(MainActivity context)
	{
		this.context = context;
		context.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};
	}
	
	/**********返回功能**********/
	private void Back(){ 
		context.jumpToFirstPageFregment();
	}

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.library_fragment, null);

		tv_back = (TextView) view.findViewById(R.id.librart_tv_back);
		view.findViewById(R.id.ll_librart_tv_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						context.jumpToFirstPageFregment();
					}
				});

		webView = (WebView) view.findViewById(R.id.library_wv);
		setWeb(webView);
		return view;
	}

	private void setWeb(WebView webView)
	{
		webView.getSettings().setJavaScriptEnabled(true);
		// 设置网页自适应屏幕宽度
		WebSettings webSettings = webView.getSettings();
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSettings.setUseWideViewPort(true);

		webView.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}
		});

		webView.loadUrl("http://m.lib.utsz.edu.cn/");
	}
}
