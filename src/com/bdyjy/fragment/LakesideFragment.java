/**
 * Fragment2.java[v 1.0.0]
 * class:com.mydream.fragment.freg,Fragment2
 * 周航 create at 2016-3-22 下午7:54:01
 */
package com.bdyjy.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.R;

/**
 * com.mydream.fragment.freg.Fragment2
 * 
 * @author 周航<br/>  镜湖边
 *         create at 2016-3-22 下午7:54:01 revised by songdebin 2016-04-02
 * 
 */
@SuppressLint("ValidFragment")
public class LakesideFragment extends Fragment
{

	private MainActivity ctx;

	// 定义一个构造器
	public LakesideFragment(MainActivity ctx)
	{
		this.ctx = ctx;
		ctx.keydown=ctx.key1;			
	}
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.lakeside_fragment, null);

		// holy 校园指南设置监听，跳转
		Button btn_guide = (Button) view.findViewById(R.id.btn_campusguide);
		btn_guide.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				ctx.jumpToCampusGuideFragment();
			}
		});

		// songdebin 跳转至二手市场
		Button btn_more = (Button) view
				.findViewById(R.id.btn_fragment2_fleamarket);
		btn_more.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				ctx.jumpToClickById(Const.FRAGMENT_FLEAMARKET_LIST_ID);
			}
		});

		// songdebin 跳转至失物招领
		Button btn_lostfind = (Button) view
				.findViewById(R.id.btn_fragment2_shiwuzhangling);
		btn_lostfind.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				ctx.jumpToClickById(Const.FRAGMENT_LOSTFINDLIST_ID);
			}
		});

		view.findViewById(R.id.btn_fragment2_changguanyuding).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						MainActivity.orderFrom = "lake";
						ctx.jumpToVenueBookingFregment();
					}
				});

		return view;
	}
}
