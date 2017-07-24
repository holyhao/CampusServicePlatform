package com.bdyjy.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.personalinfo.PersonalInfo;
import com.bdyjy.entity.personalinfo.PersonalInfoContentQueryResultBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.RoundImageView;
import com.bdyjy.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 个人中心
 * 
 * @author holy
 */

@SuppressLint("ValidFragment")
public class PersonalCenterFragment extends Fragment
{

	Handler handler = null;
	String toastMsg = null;

	private void initHandler()
	{
		handler = new Handler(ctx.getMainLooper())
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
					// listView.onLoad();
					loadData();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				}
			}
		};
	}

	private TextView setting;
	private RelativeLayout personl_info;
	private RelativeLayout my_compliant;
	private RelativeLayout wage_query;
	private RelativeLayout my_fleaMarket;
	private RelativeLayout my_lostfind;
	private RelativeLayout my_fix;
	private MainActivity ctx;

	private RoundImageView headimage;
	private TextView username;
	private TextView userid;
	private ImageView sex;
	private String url;


	public PersonalCenterFragment(MainActivity ctx)
	{
		this.ctx = ctx;
		ctx.keydown=ctx.key1;	
		initHandler();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.personal_center_fragment, null);

		
		

		view.findViewById(R.id.rl_top).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				getPersomalInfoContent();
			}
		});

		setting = (TextView) view.findViewById(R.id.setting);
		setting.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToPersonalSettingFragment();
			}
		});

		// 个人信息设置监听  
		headimage = (RoundImageView) view.findViewById(R.id.imagehead);
		username = (TextView) view.findViewById(R.id.username);
		userid = (TextView) view.findViewById(R.id.userid);
		sex = (ImageView) view.findViewById(R.id.sex);

		// 获取头像有错误，报错空指针
		// 已经让RoundImageView继承了SmartImageView 
		url = "http://219.223.223.202:8080/content_file_up/proposal/20160428/103359_1461810839223.jpg";
		//headimage.setImageUrl(url);

		if (null == MainActivity.personalInfo)
		{
			getPersomalInfoContent();
			System.out.println("测试ddddddddddd");
		} else
		{
			loadData(); 
		}
		
		
		if(MainActivity.person_info_ifupdate==1)
		{
			getPersomalInfoContent();
		}else
		{
			MainActivity.person_info_ifupdate=0;
		}

					

		personl_info = (RelativeLayout) view.findViewById(R.id.personl_info);
		personl_info.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// holy 跳往另一个activity
				// Intent intent=new Intent(ctx,PersonalInfoActivity.class); 
				// startActivity(intent);
				ctx.jumpToPersonalInfoFragment();

			}
		});

		my_compliant = (RelativeLayout) view.findViewById(R.id.my_complaint);
		my_compliant.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				// 在这里就要刷新列表
				MainActivity.orderFrom = "personCenter";
				ctx.jumpToMyComplaintListFregment();
			}
		});

		// 跳转至工资查询 宋德宾
		wage_query = (RelativeLayout) view
				.findViewById(R.id.layout_click_gongzichaxun);
		wage_query.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				// ctx.jumpToClickById(Const.FRAGMENT_WAGEQUERY_ID);
				hint("此功能暂未开启");
			}
		});

		// 跳转至通讯录 郭翠翠
		wage_query = (RelativeLayout) view
				.findViewById(R.id.layout_click_tongxunlu);
		wage_query.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ctx.jumpToContactListFragment();
			}
		});

		// 跳转至我的二手交易
		my_fleaMarket = (RelativeLayout) view
				.findViewById(R.id.layout_click_wodeershoujiaoyi);
		my_fleaMarket.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ctx.jumpToMyFlea(Const.FRAGMENT3_ID);
			}
		});

		// 跳转至我的失物招领
		my_lostfind = (RelativeLayout) view
				.findViewById(R.id.layout_click_wodeshiwuzhaoling);
		my_lostfind.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ctx.jumpToMyLost(Const.FRAGMENT3_ID);
			}
		});

		my_fix = (RelativeLayout) view
				.findViewById(R.id.layout_click_wodeweihubaoxiu);
		my_fix.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				java.util.ArrayList list = new java.util.ArrayList();
				list.add(Const.FRAGMENT3_ID);
				ctx.jumpToClickWithId(Const.FRAGMENT_MYFIX_ID, list);
			}
		});

		RelativeLayout wodechangguanyuding = (RelativeLayout) view
				.findViewById(R.id.layout_click_wodechangguanyuding);
		wodechangguanyuding.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MainActivity.orderFrom = "psersonCenter";
				ctx.jumpToVenueBookingMyFregment();
			}
		});

		RelativeLayout layout_click_wodeshoucang = (RelativeLayout) view
				.findViewById(R.id.layout_click_wodeshoucang);
		layout_click_wodeshoucang.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToMyCollectionListFragment();
			}
		});
		
		
try {
	//		System.out.println(MainActivity.personalInfo.getPermission()+"permissions的值的地方反复对方");
	//		
	if (MainActivity.personalInfo.getPermission() == 2) // 如果是学生身份就把这个功能隐藏掉
	{
		//  System.out.println("这个权限是2     "+permission);
		View hiddenView = (View) view.findViewById(R.id.layout_click_tongxunlu); // 在hidden_view.xml中hidden_layout是root
		// layout
		if (null != hiddenView) {
			ViewGroup parent = (ViewGroup) hiddenView.getParent();
			parent.removeView(hiddenView);
		}
	} 
} catch (Exception e) {
	// TODO: handle exception
   //  System.out.println("这个权限是2     "+permission);
		View hiddenView = (View) view.findViewById(R.id.layout_click_tongxunlu); // 在hidden_view.xml中hidden_layout是root
		// layout
		if (null != hiddenView) {
			ViewGroup parent = (ViewGroup) hiddenView.getParent();
			parent.removeView(hiddenView);
		}
}



		return view;
	}

	private void loadData()
	{
		if (MainActivity.personalInfo != null)
		{
			// 拼接头像链接
			String ur1 = MainActivity.personalInfo.getAttachmentPrefix();
			String ur2 = MainActivity.personalInfo.getUserImg();
			// TODO
			headimage.setImageUrl(ur1 + ur2);
			

			
 
			username.setText(MainActivity.personalInfo.getUserName());
			userid.setText(MainActivity.personalInfo.getIdentifierNumber());
			String sexchoice = MainActivity.personalInfo.getSex();
			System.out.println("性别是：" + sexchoice);
			if (sexchoice == "1")
			{
				sex.setBackgroundResource(R.drawable.man);
			} else
			{
				sex.setBackgroundResource(R.drawable.woman);
			}

		}

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
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(
							ctx,
							"/admin//clientuser/findById.do?id=" + token
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
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
					System.out.println("personalInfo:" + b);
					System.out.println("personalInfo:" + b.getPermission());
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);																								
					} 
				}
		}.start();
	}

	public void hint(String hint)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_hint_invaliable);
		TextView tv_hint = (TextView) alertDialog.getWindow().findViewById(
				R.id.dialog_hint);
		tv_hint.setText(hint);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MainActivity.person_info_ifupdate=0;
	}

	
}
