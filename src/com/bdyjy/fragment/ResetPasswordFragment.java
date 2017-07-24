package com.bdyjy.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.complaint.ComplaintContentQueryResultBean;
import com.bdyjy.entity.personalinfo.PersonalInfoContentQueryResultBean;
import com.bdyjy.entity.upload.ImgUploadResultBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

/**
 * holy 个人设置中的密码修改
 * 
 * @author
 */

public class ResetPasswordFragment extends Fragment
{
	private TextView tv_back;

	private Handler handler;
	private String toastMsg;
	private MainActivity ctx;

	private void initHandler()
	{
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case HandlerOrder.TOAST:
					// TODO
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.UPDATE_LISTVIEW:
					// listView1.onLoad();
					// loadData();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.UPLOAD_ERROR:
					Toast.makeText(ctx, "上传出错，服务器异常", Toast.LENGTH_LONG).show();
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.POST_OK:
					// 在这里解析post之后的结果
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					// 返回值将会是JSON格式的数据，我要在这里解析
					String res = msg.getData().get("body").toString();
					System.out.println("post_result:\n" + res);
					PersonalInfoContentQueryResultBean bean = JSON.parseObject(
							res, PersonalInfoContentQueryResultBean.class);
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))
					{
						toastMsg = "密码修改成功";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						ctx.jumpToPersonalSettingFragment();

					} else
					{
						toastMsg = bean.getApp_result_message_key();// "信息提交失败，请重试";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
					}

					break;

				}
			}
		};

	}

	public ResetPasswordFragment(MainActivity ctx)
	{
		this.ctx = ctx;
		initHandler();
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

	Button submit;

	EditText et_oldPwd;
	EditText et_newPwd;
	EditText et_newPwd2;

	String oldPwd;
	String newPwd;
	String newPwd2;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.reset_password_fragment, null);

		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToPersonalSettingFragment();
			}
		});

		et_oldPwd = (EditText) view.findViewById(R.id.origin_passwprd);
		et_newPwd = (EditText) view.findViewById(R.id.new_password);
		et_newPwd2 = (EditText) view.findViewById(R.id.new_password_again);

		submit = (Button) view.findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				oldPwd = et_oldPwd.getText().toString();
				newPwd = et_newPwd.getText().toString();
				newPwd2 = et_newPwd2.getText().toString();

				String valRes = ifParameterOK(oldPwd, newPwd, newPwd2);
				if (null != valRes)
				{
					toastMsg = valRes;
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}

				System.out.println("mNewPassword:" + newPwd);
				updatePassword();

			}
		});

		return view;
	}

	private void updatePassword()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				System.out.println("修改信息提交的数据是:" + newPwd);
				// 取得页面上的参数以及应用已经存储的参数
				String res = null;
				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");
				// 尝试post请求
				tryPost(newPwd,oldPwd, token, singnature, st);
			}
		}.start();
	}

	/**
	 * 尝试异步post方式进行提交
	 */
	private void tryPost(String mNewPassword,String oldPwd, String token, String singnature,
			String st)
	{
		// 尝试使用异步post请求进行
		HashMap<String, String> map = new HashMap<String, String>();
		// 现在开始构造参数(获取)
		map.put("password", mNewPassword);
		map.put("oldPassword", oldPwd);
		String http = "/admin/clientuser/updatePwd.do?token=" + token
				+ "&singnature=" + singnature + "&st=" + st;
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
		OkHttpUtils.getInstance().doPostAsync(ctx, http, map, handler);
	}

	private String ifParameterOK(String oldPwd, String new1, String news2)
	{
		if (TextUtils.isEmpty(oldPwd))
			return "旧密码不能为空";
		if (TextUtils.isEmpty(new1))
			return "新密码不能为空";
		if (TextUtils.isEmpty(news2))
			return "请再次填写新密码";
		if (!new1.equals(news2))
			return "两次填写的新密码不一样";
		return null;
	}
}
