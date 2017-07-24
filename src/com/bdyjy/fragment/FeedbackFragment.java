
package com.bdyjy.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
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
import com.bdyjy.entity.upload.ImgUploadResultBean;
import com.bdyjy.entity.venue.CommitResultBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;


/**
 * holy 个人设置中的建议反馈
 * 
 * @author 
 */

public class FeedbackFragment extends Fragment
{
	
	
	private Handler handler;
	private String toastMsg;
	private TextView tv_back;
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
//					listView1.onLoad();
//					loadData();
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
				case HandlerOrder.UPLOAD_OK:
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);				
				case HandlerOrder.POST_OK:
					// 在这里解析post之后的结果
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					// 返回值将会是JSON格式的数据，我要在这里解析
					String res = msg.getData().get("body").toString();
					System.out.println("post_result:\n" + res);
					ComplaintContentQueryResultBean bean = JSON.parseObject(res,
							ComplaintContentQueryResultBean.class);
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))
					{
						toastMsg = "反馈信息提交成功";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						handler.sendEmptyMessage(HandlerOrder.TO_MAIN);					
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
	
	
	
	
	public FeedbackFragment(MainActivity ctx)
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
	
	private EditText tel;
	private String contact;
	private EditText info;
	private String details;
	private Button submit; 
	
	
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.feedback_fragment, null);

		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{  
				ctx.jumpToPersonalSettingFragment();
			}
		});
         tel=(EditText)view.findViewById(R.id.tel);
         info=(EditText)view.findViewById(R.id.infor);
		 submit=(Button)view.findViewById(R.id.submit);
		 
		 submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				contact=tel.getText().toString();
				details=info.getText().toString();
				//commit
				Commit();
			}
		});
		
		
		
		return view;
	}
	
	private void Commit ()
	{
		
		new Thread()
		{
			@Override
			public void run()
			{

				// 取得页面上的参数以及应用已经存储的参数
				String res = null;			
				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");				
				// 尝试post请求
				tryPost(contact, details,token,singnature, st);
				System.out.println("上传数据是："+contact+details);
			}
		}.start();
	}

	/**
	 * 尝试异步post方式进行提交
	 */
	private void tryPost( String contact, String details,String token,String singnature, String st)
	{
		// 尝试使用异步post请求进行
		HashMap<String, String> map = new HashMap<String, String>();
		// 现在开始构造参数	
		
		map.put("details",details);		
		map.put("contact", contact);
		String http = "/admin/feedback/addSave.do?token=" + token
				+ "&singnature=" + singnature + "&st=" + st;
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
		OkHttpUtils.getInstance().doPostAsync(ctx, http, map, handler);
	}
	
}
