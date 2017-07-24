package com.bdyjy.fragment;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.ParleConstant;
import com.bdyjy.entity.recruit.RecruitNewBean;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.fragment.base.FragmentWithCollection;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RecruitContentFragment extends FragmentWithCollection
{

	private TextView tv_back;
	private MainActivity ctx;
	final int TIME_LENGTH = 10;

	public RecruitContentFragment(MainActivity ctx)
	{
		super(ctx, CollectionTypeConst.RECRUIT);
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
			ctx.jumpToClickById(ParleConstant.JUMP_RECRUIT);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.recruit_content_fragment, null);

		tv_back = (TextView) view.findViewById(R.id.recruit_content_tv_back);
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
					ctx.jumpToClickById(ParleConstant.JUMP_RECRUIT);
			}
		});

		// 招聘详情的内容
		TextView tv_title = (TextView) view
				.findViewById(R.id.recruit_content_title);
		TextView tv_time = (TextView) view
				.findViewById(R.id.recruit_content_time);
		TextView tv_pubFrom = (TextView) view
				.findViewById(R.id.recruit_content_pubFrom);
		TextView tv_type = (TextView) view
				.findViewById(R.id.recruit_content_type);
		TextView tv_content = (TextView) view
				.findViewById(R.id.recruit_content);
		// 暂时没有图片资源
		ImageView iv_img = (ImageView) view
				.findViewById(R.id.recruit_content_image);

		RecruitNewBean n = MainActivity.recruitNew;

		// 新闻内容填充
		tv_title.setText(n.getTitle());
		tv_time.setText(n.getCreateTime().substring(0, TIME_LENGTH));
		tv_pubFrom.setText(n.getCompany());
		tv_type.setText(n.getPosition());
		tv_content.setText(n.getContent());

		// 以下到结尾是收藏相关的代码
		super.setMainView(view);
		dealCollect(n.getId(), n.getIsCollect(), n.getTitle());
		return view;
	}
}
