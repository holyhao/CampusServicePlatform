package com.bdyjy.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.activity.manager.MyActivityManager;

/**
 * holy 个人设置
 * 
 * @author
 */
@SuppressLint("NewApi")
public class PersonalSettingFragment extends Fragment
{
	private TextView tv_back;
	private MainActivity ctx;

	private RelativeLayout reset_password;
	private RelativeLayout clear_cache;
	private RelativeLayout about_us;
	private RelativeLayout feedback;
	private RelativeLayout version_update;

	public PersonalSettingFragment(MainActivity ctx)
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
		ctx.jumpToPersonalCenterFragment();
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.personal_setting_fragment, null);

		// tv_back = (TextView) view.findViewById(R.id.tv_back);
		view.findViewById(R.id.ll_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						ctx.jumpToPersonalCenterFragment();
					}
				});

		OnClickListener settingListener = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				switch (v.getId())
				{
				case R.id.reset_password:
					ctx.jumpToResetPasswordFragment();
					break;
				case R.id.clear_cache:
					Toast.makeText(getActivity(), "缓存已清理", Toast.LENGTH_SHORT)
							.show();
					break;
				case R.id.about_us:
					ctx.jumpToAboutUsFragment();
					break;
				case R.id.feedback:
					ctx.jumpToFeedbackFragment();
					break;
				case R.id.version_update:
					Toast.makeText(getActivity(), "已经最新版本", Toast.LENGTH_SHORT)
							.show();
				case R.id.rl_exit:
					showDialog(ctx);
					break;
				default:
					break;
				}

			}
		};
		// 修改密码
		reset_password = (RelativeLayout) view
				.findViewById(R.id.reset_password);
		reset_password.setOnClickListener(settingListener);
		// 清理缓存
		clear_cache = (RelativeLayout) view.findViewById(R.id.clear_cache);
		clear_cache.setOnClickListener(settingListener);
		// 关于我们
		about_us = (RelativeLayout) view.findViewById(R.id.about_us);
		about_us.setOnClickListener(settingListener);
		// 建议反馈
		feedback = (RelativeLayout) view.findViewById(R.id.feedback);
		feedback.setOnClickListener(settingListener);
		// 版本更新
		version_update = (RelativeLayout) view
				.findViewById(R.id.version_update);
		version_update.setOnClickListener(settingListener);

		RelativeLayout tv_exit = (RelativeLayout) view
				.findViewById(R.id.rl_exit);
		tv_exit.setOnClickListener(settingListener);
		return view;
	}

	// 显示基本的AlertDialog
	private void showDialog(Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("确定退出登录么");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				MyActivityManager.getInstance().toLogin();
			}
		});

		builder.setNegativeButton("取消", null);
		builder.show();
	}
}
