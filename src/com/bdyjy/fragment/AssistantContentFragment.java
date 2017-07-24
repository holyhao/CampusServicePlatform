package com.bdyjy.fragment;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.ParleConstant;
import com.bdyjy.entity.assistant.AssistantNewBean;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.fragment.base.FragmentWithCollection;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AssistantContentFragment extends FragmentWithCollection
{

	private TextView tv_back;
	private MainActivity ctx;
	final int TIME_LENGTH = 10;

	public AssistantContentFragment(MainActivity ctx)
	{
		super(ctx, CollectionTypeConst.WORK_STUDY);
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};
	}
	
	/**********返回功能**********/
	private void Back(){ 
		if ("myCollectionList".equals(MainActivity.orderFrom))
		{
			ctx.jumpToMyCollectionListFragment();
			MainActivity.orderFrom = "";
		} else
		ctx.jumpToClickById(ParleConstant.JUMP_ASSISTANT);
	}
    
	

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.assistant_content_fragment, null);

		tv_back = (TextView) view.findViewById(R.id.assistant_content_tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if ("myCollectionList".equals(MainActivity.orderFrom))
				{
					ctx.jumpToMyCollectionListFragment();
					MainActivity.orderFrom = "";
				} else
				ctx.jumpToClickById(ParleConstant.JUMP_ASSISTANT);
			}
		});

		// 招聘详情的内容
		TextView tv_title = (TextView) view
				.findViewById(R.id.assistant_content_title);
		TextView tv_time = (TextView) view
				.findViewById(R.id.assistant_content_time);
		TextView tv_pubFrom = (TextView) view
				.findViewById(R.id.assistant_content_pubFrom);
		TextView tv_type = (TextView) view
				.findViewById(R.id.assistant_content_type);
		TextView tv_content = (TextView) view
				.findViewById(R.id.assistant_content);
		// 没有图片
		ImageView iv_img = (ImageView) view
				.findViewById(R.id.assistant_content_image);

		AssistantNewBean news = MainActivity.assistantNew;

		// 新闻内容填充
		tv_title.setText(news.getTitle());
		tv_time.setText(news.getCreateTime().substring(0, TIME_LENGTH));
		tv_pubFrom.setText(news.getSector());
		tv_type.setText("招聘类型");
		tv_content.setText(news.getContent());

		// 以下到结尾是收藏相关的代码
		super.setMainView(view);
		dealCollect(news.getId(), news.getIsCollect(), news.getTitle());
		return view;
	}
}
