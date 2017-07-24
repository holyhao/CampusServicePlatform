/**
 * LoginActivity.java[v 1.0.0]
 * class:com.example.exampleandroidproject.activity,LoginActivity
 * 周航 create at 2016-3-19 上午11:31:26
 */
package com.bdyjy.activity;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdyjy.R;
import com.bdyjy.activity.base.BaseActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.firstPageImg.BannaKImgsResultBean;
import com.bdyjy.entity.firstPageImg.Rows;
import com.bdyjy.entity.personalinfo.PersonalInfo;
import com.bdyjy.entity.personalinfo.PersonalInfoContentQueryResultBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

/**
 * com.example.exampleandroidproject.activity.LoginActivity
 * 
 * @author 周航<br/>
 *         create at 2016-3-19 上午11:31:26
 */
public class LoginActivity extends BaseActivity
{

	private EditText et_username, et_password;
	private Button btn_login;
    private Button btn_clearusername;
	private String toastMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initWidget();
		initHandler();
		
//		    DisplayMetrics metric = new DisplayMetrics();
//	        getWindowManager().getDefaultDisplay().getMetrics(metric);
//	        int width = metric.widthPixels;  // 屏幕宽度（像素）
//	        int height = metric.heightPixels;  // 屏幕高度（像素）
//	        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
//	        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
//	       //  px = dp * (densityDpi / 160);
//	        
//	        Log.e("px","屏幕宽度（像素）=="+width);
//	        Log.e("px","屏幕高度（像素）=="+height);
//	        Log.e("px","屏幕密度=="+density);
//	        Log.e("px","屏幕密度DPI=="+densityDpi);
	        
		// Intent i = new Intent(ctx, MainActivity.class);//临时所加，以便测试 宋德宾
		// startActivity(i);//临时所加，以便测试 宋德宾
	}

	@Override
	protected void initWidget()
	{
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
	    if(SPUtils.get(ctx, "loginName","")!=null||SPUtils.get(ctx, "password","")!=null)
	    {
	    	SPUtils.get(ctx, "loginName","");
			SPUtils.get(ctx, "password","");
			et_username.setText((String)SPUtils.get(ctx, "loginName",""));
			et_password.setText((String)SPUtils.get(ctx, "password",""));
	    }
		
		
		btn_login = (Button) findViewById(R.id.btn_login);

		btn_login.setOnClickListener(this);
		btn_clearusername=(Button) findViewById(R.id.btn_clearusername);
		btn_clearusername.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				et_username.setText("");
				et_password.setText("");
				
			}
		});
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void initHandler()
	{
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case HandlerOrder.TO_MAIN:
					hideRoundProcessDialog();
					Intent i = new Intent(ctx, MainActivity.class);
					startActivity(i);
					// 并且终结掉LoginActivity
					// LoginActivity.this.finish();
					break;
				case HandlerOrder.TOAST:
					hideRoundProcessDialog();
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					hideRoundProcessDialog();
					break;
				case HandlerOrder.FIRST_PAGE_IMG:
					getImgList();
					break;
				case  HandlerOrder.GET_PERSON_INFORMATION:
					getPersomalInfoContent();
					break;
					
				}
			}
		};
	}

	@Override
	protected void widgetClick(View v)
	{
		// switch (v.getId())
		// {
		// case
		if (v.getId() == R.id.btn_login)
		{
			showRoundProcessDialog();

			final String loginName = et_username.getText().toString();
			final String password = et_password.getText().toString();
			// 在这里开始请求后台
			final HashMap<String, String> map = new HashMap<String, String>();
			map.put("loginName", loginName);
			map.put("password", password);
           
			
			// 测试一下属性文件是否可以读取
			// System.out.println(PropetiesFileReaderUtil.get(ctx, "ip"));

			// handler.sendEmptyMessage(HandlerOrder.TO_MAIN);
			new Thread()
			{
				public void run()
				{

					// 试试get请求
					String res = null;
					try
					{
						res = OkHttpUtils.getInstance().doGet(
								ctx,
								"/admin/clientuser/login.do?loginName="
										+ loginName + "&password=" + password);
						Log.e("px", res);
						System.out.println("请求返回的结果是：" + res.trim());
					} catch (Exception e)
					{
						e.printStackTrace();
						toastMsg = "连接服务器超时";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					}
					// 返回值将会是JSON格式的数据，我要在这里解析
					if (null == res || res.length() == 0
							|| res.trim().length() == 0
							|| res.contains("error"))
					{
						toastMsg = "登录失败";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					}

					JSONObject obj = JSON.parseObject(res);
					String app_result_key = (String) obj.get("app_result_key");
					if ("0".equals(app_result_key))
					// 如果app_result_key这个变量是0，就说明登录正常
					{
						// 登录正常我们就进行正常跳转，并且把相关参数保存在本地

						String token = (String) obj.get("token");
						String singnature = (String) obj.get("singnature");
						String st = (String) obj.get("st");

						SPUtils.put(ctx, "token", token);
						SPUtils.put(ctx, "singnature", singnature);
						SPUtils.put(ctx, "st", st);
						SPUtils.put(ctx, "loginName", loginName);
						SPUtils.put(ctx, "password", password);
						handler.sendEmptyMessage(HandlerOrder.FIRST_PAGE_IMG);
					} else
					// 反之就是登录异常，我们取异常信息
					{
						String app_result_message_key = (String) obj
								.get("app_result_message_key");
						String system_result_message_key = (String) obj
								.get("system_result_message_key");

						if (TextUtils.isEmpty(app_result_message_key))
						{
							app_result_message_key = "";
						}else{
							toastMsg = app_result_message_key;
						}
						if (TextUtils.isEmpty(system_result_message_key))
						{
							system_result_message_key = "";
						}else{
							toastMsg = system_result_message_key;
						}

//						System.out.println(app_result_message_key + ""
//								+ app_result_message_key + " " + toastMsg);

						handler.sendEmptyMessage(HandlerOrder.TOAST);
					}
				};
			}.start();
		}
	}

	private int picCount = 5;

	private void getImgList()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				// 试试get请求
				String res = null;

				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");

				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance()
							.doGet(ctx,
									"/picture/grid.do?pageNo=1&pageSize="
											+ picCount + "&status=1&token="
											+ token + "&singnature="
											+ singnature + "&st=" + st);
				} catch (Exception e)
				{ 
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					e.printStackTrace();
					toastMsg = "获取讲座信息失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				System.out.println("getImgList取首页轮播图结果：" + res.trim());

				// 尝试将json串转化成bean对象
				BannaKImgsResultBean bean = JSON.parseObject(res,
						BannaKImgsResultBean.class);

				// 循环往轮播图list里面注入数据
				// TODO

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取轮播图失败";
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))
				{
					MainActivity.bannerImgList.clear();// 清空集合
					List<Rows> rows = bean.getData().getRows();
					MainActivity.bannerList=rows;
					for (Rows r : rows)
					{
						String ImgUrl = r.getAttachmentPrefix()
								+ r.getPicture();
						MainActivity.bannerImgList.add(ImgUrl);
					}
					handler.sendEmptyMessage( HandlerOrder.GET_PERSON_INFORMATION);
				} else
				{
					toastMsg = "获取轮播图失败";
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}
			}
		}.start();
	}
	
	
	private void getPersomalInfoContent()
	{

		new Thread()
		{
			@Override
			public void run()
			{
				// 试试get请求
				String res = null;

				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");

				try
				{
					//handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(
							ctx,
							"/admin//clientuser/findById.do?id=" + token
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
					//handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					//handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				System.out.println("请求个人信息的结果是：" + res.trim());
				PersonalInfoContentQueryResultBean bean = JSON.parseObject(res,
						PersonalInfoContentQueryResultBean.class);

				// System.out.println("xxxx:" + bean.getApp_result_key());

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
												// 那么我要在这里取得新闻的标题，内容，显示在界面上
				{
					PersonalInfo b = bean.getEntity();
					MainActivity.personalInfo = b;
//					System.out.println("personalInfo:" + b);
//					System.out.println("personalInfo:" + b.getPermission());					
					handler.sendEmptyMessage(HandlerOrder.TO_MAIN);
					} 
				}
		}.start();
	}
	
	
	public void help_click(View v){
		hint("说明信息");
	}
	
	AlertDialog alertDialog;
	public void hint(String hint)
	{
	    alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.help_view);
		RelativeLayout btn_quit=(RelativeLayout) alertDialog.getWindow().findViewById(
				R.id.quit_ly);
		btn_quit.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				alertDialog.cancel();
			}
		});
	
	}
}
