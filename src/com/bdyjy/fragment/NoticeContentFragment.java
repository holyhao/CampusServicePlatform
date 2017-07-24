package com.bdyjy.fragment;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.entity.notice.NoticeContentById;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

public class NoticeContentFragment extends Fragment {
	private TextView tv_notice_content_back;
	private MainActivity ctx;

	public NoticeContentFragment(MainActivity ctx) {
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	
	/**********返回功能**********/
	private void Back(){ 
		ctx.jumpToNoticeListFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notice_content, null);

//		tv_notice_content_back = (TextView) view.findViewById(R.id.tv_notice_content_back);
//		tv_notice_content_back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ctx.jumpToNoticeListFragment();
//			}
//		});
		
		view.findViewById(R.id.ll_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ctx.jumpToNoticeListFragment();
			}
		});

		TextView title = (TextView) view.findViewById(R.id.notice_content_title);
		TextView date = (TextView) view.findViewById(R.id.notice_content_date);
		TextView sourcefrom = (TextView) view.findViewById(R.id.notice_content_publisher);
		WebView content = (WebView) view.findViewById(R.id.notice_content_webview);
		content.getSettings().setDefaultTextEncodingName("UTF-8");

		NoticeContentById n = MainActivity.notice;

		title.setText(n.getTitle());
		date.setText(n.getDate());
		sourcefrom.setText(n.getSourcefrom());
		content.loadDataWithBaseURL(null, n.getContent(), "text/html", "UTF-8", null);// (n.getContent(),
																						// "text/html",
																						// "UTF-8");
		return view;
	}
}