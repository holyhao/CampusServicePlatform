
package com.bdyjy.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;


/**
 * holy 关于我们
 * 
 * @author 
 */

public class AboutUsFragment extends Fragment
{
	private TextView tv_back;
	private MainActivity ctx;

	public AboutUsFragment(MainActivity ctx)
	{
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};		
	}
	
	/**********返回功能**********/
	private void Back(){ 
		ctx.jumpToPersonalSettingFragment();
		
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.about_us_fragment, null);

		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{  
				ctx.jumpToPersonalSettingFragment();
			}
		});

		return view;
	}
}
