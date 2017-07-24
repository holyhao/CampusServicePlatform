package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.adapter.ContactListViewAdapter;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.contact.Contact;
import com.bdyjy.entity.contact.ContactContent;
import com.bdyjy.entity.contact.ContactDepartment;
import com.bdyjy.util.HttpUtilPost;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

/**
 * 通讯录fragment
 * 
 * @author cuicui create at 2016-03-29 0:23
 * 
 */

public class ContactListFragment extends Fragment implements IXListViewListener
{
	Handler handler = null;
	String toastMsg = null;

	private View viewMain;
	private LayoutInflater inflaterMain;

	/* 定义通讯录列表相关 */
	private XListView contactlistView;
	private MainActivity ctx;
	private ContactListViewAdapter contactListViewAdapter;
	private List<Map<String, Object>> contactListItems;
	private TextView tv_back;
	private EditText et_search; // 搜索状态下的输入框
	private TextView tv_search_cancel;// 关闭搜索功能
	private RelativeLayout rl_searchbar_off; // 关闭状态下的输入框
	private RelativeLayout rl_searchbar_on;// 工作状态下的输入框
	private int pageSize = 5;// 页面新闻条数-容量
	private int sizeStep = 20;// 每次加载的数目
	private String searchKeys;// 搜索关键字
	private boolean search_tag = false;// 定义搜索状态 true 处于搜索状态 false处于正常状态
										// 关于搜索功能的逻辑问题还没有解决

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
					contactlistView.onLoad();
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

	public ContactListFragment(MainActivity ctx)
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
		ctx.jumpToPersonalCenterFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.contactlist_fragment, null);

		// tv_back = (TextView) view.findViewById(R.id.tv_contact_back);
		// tv_back.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// ctx.jumpToFirstPageFregment();
		// }
		// });

		view.findViewById(R.id.ll_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						ctx.jumpToPersonalCenterFragment();
					}
				});

		contactlistView = (XListView) view
				.findViewById(R.id.department_1_contact_listview);
		contactlistView.setPullLoadEnable(true);
		contactlistView.setXListViewListener(this);
		contactListItems = new ArrayList<Map<String, Object>>();

		// 初始化输入框
		rl_searchbar_off = (RelativeLayout) view
				.findViewById(R.id.searchbar_off);
		rl_searchbar_on = (RelativeLayout) view.findViewById(R.id.searchbar_on);

		// 开启搜索框
		rl_searchbar_off.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				rl_searchbar_off.setVisibility(View.INVISIBLE);
				rl_searchbar_on.setVisibility(View.VISIBLE);
				et_search.setFocusable(true);
				et_search.requestFocus();
				// 设置自动弹出输入法
				InputMethodManager imm = (InputMethodManager) et_search
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

				Timer timer = new Timer();

				timer.schedule(new TimerTask()
				{
					@Override
					public void run()

					{
						InputMethodManager inputManager = (InputMethodManager) et_search
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						inputManager.showSoftInput(et_search, 0);
					}
				}, 400);
			}
		});

		tv_search_cancel = (TextView) view.findViewById(R.id.tv_search_cancel);
		// 关闭搜索框
		tv_search_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				rl_searchbar_off.setVisibility(View.VISIBLE);
				rl_searchbar_on.setVisibility(View.INVISIBLE);
				et_search.setText("");
			}
		});

		// 搜索框输入监听
		et_search = (EditText) view.findViewById(R.id.et_search);
		et_search.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event)
			{
				Toast.makeText(ctx, "正在搜索，请稍等", Toast.LENGTH_SHORT).show();
				String sName= et_search.getText().toString().trim();
				if(ifChina(sName)==1)
				{
					searchChina();//中文搜索
				}else
				{
					searchLostList();//英文字母搜索
				}
				return false;
			}
		});
		// 选项的点击事件
		contactlistView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 设置选项的点击事件
				TextView tv = (TextView) view.findViewById(R.id.tv_contact_id);
				System.out.println("提取通讯录id为：" + id);
				// 根据这个通讯录id，来查询具体的通讯录内容，显示出来
				// 查询后台
				getContactContent(tv.getText().toString());
			}
		});
		if (null == MainActivity.contactlistByDepartment)
			getContactList();
		else
			loadData();

		return view;
	}

	private void loadData()
	{
		// 清空原有内容
		contactListItems.clear();
		Map<String, Object> map = null;
		for (int i = 0; i < MainActivity.contactlistByDepartment.size(); i++)
		{

			List<ContactContent> contactList = MainActivity.contactlistByDepartment
					.get(i).getData();
			for (int j = 0; j < contactList.size(); j++)
			{
				map = new HashMap<String, Object>();
				// 从上一层json中获取部门人数
				map.put("total", MainActivity.contactlistByDepartment.get(i)
						.getTotal());
				map.put("name", contactList.get(j).getName()); // 物品标题
				map.put("id", contactList.get(j).getId());
				map.put("telphone", contactList.get(j).getTelphone());
				map.put("office", contactList.get(j).getOffice());
				map.put("telphone", contactList.get(j).getTelphone());
				map.put("sex", contactList.get(j).getSex());
				map.put("email", contactList.get(j).getEmail());
				map.put("department", contactList.get(j).getDepartment());
				contactListItems.add(map);
			}

		}
		if (null == contactListViewAdapter)
		{
			contactListViewAdapter = new ContactListViewAdapter(ctx,
					contactListItems); // 创建适配器
			contactlistView.setAdapter(contactListViewAdapter);
		} else
		{
			contactListViewAdapter.refresh();
		}
	}

	// 在这里封装从后台一个获取通讯录列表的方法
	private void getContactList()
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
					res = HttpUtilPost.doPost(

							ctx,
							"/admin/contact/get_list.do"

							+ "?token=" + token + "&singnature=" + singnature
									+ "&st=" + st);
		           	   Log.e("555", res);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取通讯录内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求通讯录：请求返回的结果是：" + res.trim());

				// 尝试将json串转化成bean对象
				Contact bean = JSON.parseObject(res, Contact.class);
				// System.out.println("xxxx:" + nqrb.getApp_result_key());

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取通讯录列表失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))
				{

					// 到了这里说明正常返回了数据，我要在这里获取通讯录的标题
					List<ContactDepartment> contactlistbydepartment = bean
							.getData();

					// 将这些通讯录对象存储到sp中
					MainActivity.contactlistByDepartment = contactlistbydepartment;
					// 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}
			}
		}.start();

	}

	// 搜索功能
	private void searchLostList()
	{
		new Thread()
		{

			@Override
			public void run()
			{
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
				// 试试get请求
				String res = null;
				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");
				String search = et_search.getText().toString().trim();			
				try
				{
//					res = OkHttpUtils.getInstance().doGet(
//
//							ctx,
//							"/admin/contact/get_list.do" + "?search=" + search
//									+ "&token=" + token + "&singnature="
//									+ singnature + "&st=" + st);
					
					StringEntity reqEntity = new StringEntity(search,HTTP.UTF_8);			
					res = HttpUtilPost.doPost(

							ctx,
							"/admin/contact/get_list.do" + "?search=" + search
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);

					
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取内容失败，请检查网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				System.out.println("搜索通讯录：请求返回的结果是：" + res.trim());
 
				// 尝试将json串转化成bean对象
           try {
        	   Contact bean = JSON.parseObject(res, Contact.class);
        		// System.out.println("xxxx:" + bean.getApp_result_key());
        		if (res.trim().length() == 0)
				{
					toastMsg = "获取内容失败，请检查网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))
				{
					// 到了这里说明正常返回了数据，我要在这里获取通讯录的标题
					List<ContactDepartment> contactlistbydepartment = bean
							.getData();
					if (contactlistbydepartment.size() == 0)
					{
						toastMsg = "没找到您要的数据";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
					}
					// 将这些通讯录对象存储到sp中
					MainActivity.contactlistByDepartment = contactlistbydepartment;
					// 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
					// 临时设置
				} else
				{
					toastMsg = "获取内容失败，请检查网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
				
			

			
//				if (res.trim().length() == 0)
//				{
//					toastMsg = "获取内容失败，请检查网络";
//					handler.sendEmptyMessage(HandlerOrder.TOAST);
//					return;
//				}
//				String app_result_key = bean.getApp_result_key();
//				if ("0".equals(app_result_key))
//				{
//					// 到了这里说明正常返回了数据，我要在这里获取通讯录的标题
//					List<ContactDepartment> contactlistbydepartment = bean
//							.getData();
//					if (contactlistbydepartment.size() == 0)
//					{
//						toastMsg = "没找到您要的数据";
//						handler.sendEmptyMessage(HandlerOrder.TOAST);
//					}
//					// 将这些通讯录对象存储到sp中
//					MainActivity.contactlistByDepartment = contactlistbydepartment;
//					// 使用handler去通知主线程更新listview
//					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
//					// 临时设置
//				} else
//				{
//					toastMsg = "获取内容失败，请检查网络";
//					handler.sendEmptyMessage(HandlerOrder.TOAST);
//					return;
//				}
			}
		}.start();
	}

	// 在这里封装从后台一个获取通讯录内容的方法
	private void getContactContent(final String id)
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
							"/admin/contact/get_list.do?id=" + id + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取通讯录内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}

				System.out.println("请求通讯录详情：请求返回的结果是：" + res.trim() + "id为 ："
						+ id);

				ContactDepartment bean = JSON.parseObject(res,
						ContactDepartment.class);
				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取通讯录内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				// String app_result_key = bean.getApp_result_key();
				// if ("0".equals(app_result_key))// 如果正常获得了通讯录的内容,
				// 那么我要在这里取得通讯录的标题，内容，显示在界面上
				// {
				ContactContent b = bean.getData().get(0);
				MainActivity.contact = b;
				// }
				ctx.jumpToContactContentFragment();
			}
		}.start();
	}

	/**
	 * 以下是上拉加载，下拉刷新相关代码
	 */
	@Override
	public void onRefresh()// 这是刷新
	{
		// toastMsg = "检测到下拉刷新动作";
		// handler.sendEmptyMessage(HandlerOrder.TOAST);

		// 刷新数据
		getContactList();
	}

	@Override
	public void onLoadMore()// 这是加载更多
	{
		// toastMsg = "检测到上拉加载动作";
		// handler.sendEmptyMessage(HandlerOrder.TOAST);
		pageSize += sizeStep;
		getContactList();
	}
	
//	public void searchChina()
//	{
//		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
//		// 试试get请求
//		String res = null;
//
//		// 从sharePreference中取出之前存储的参数
//		String token = (String) SPUtils.get(ctx, "token", "");
//		String singnature = (String) SPUtils.get(ctx, "singnature", "");
//		String st = (String) SPUtils.get(ctx, "st", "");
//		String searchKeys = et_search.getText().toString().trim();		
//		try
//		{			
//			
//		HashMap<String, String> map1 = new HashMap<String, String>();
//		searchKeys = et_search.getText().toString().trim();
//
//		//Log.i("pageSearchKey is", searchKeys);
//
//		map1.put("pageSearchKeys", searchKeys);
//		String http = "/admin/contact/get_list.do" + "?search=" + searchKeys
//				+ "&token=" + token + "&singnature="
//				+ singnature + "&st=" + st;
//		res=  OkHttpUtils.getInstance().doPostAsync(ctx, http, map1, handler);
//
//	}catch(Exception e)
//	{
//		e.printStackTrace();
//	}
//	}
	
	private void searchChina()
	{
		// 清空原有内容
		contactListItems.clear();
		Map<String, Object> map = null;
		String sName=et_search.getText().toString().trim();
		for (int i = 0; i < MainActivity.contactlistByDepartment.size(); i++)
		{

			List<ContactContent> contactList = MainActivity.contactlistByDepartment
					.get(i).getData();
			for (int j = 0; j < contactList.size(); j++)
			{
				map = new HashMap<String, Object>();
				// 从上一层json中获取部门人数
				String xName=contactList.get(j).getName();
				if(xName.contains(sName))
				{
					map.put("total", MainActivity.contactlistByDepartment.get(i)
							.getTotal());
					map.put("name", contactList.get(j).getName()); // 物品标题
					map.put("id", contactList.get(j).getId());
					map.put("telphone", contactList.get(j).getTelphone());
					map.put("office", contactList.get(j).getOffice());
					map.put("telphone", contactList.get(j).getTelphone());
					map.put("sex", contactList.get(j).getSex());
					map.put("email", contactList.get(j).getEmail());
					map.put("department", contactList.get(j).getDepartment());
					contactListItems.add(map);
				}
				
			}

		}
		if (null == contactListViewAdapter)
		{
			contactListViewAdapter = new ContactListViewAdapter(ctx,
					contactListItems); // 创建适配器
			contactlistView.setAdapter(contactListViewAdapter);
		} else
		{
			contactListViewAdapter.refresh();
		}
	}
	
	public int ifChina(String stx)
	{
		 Pattern p = Pattern.compile("[0-9]*"); 
	     Matcher m = p.matcher(stx); 
//	     if(m.matches() ){
//	      Toast.makeText(Main.this,"输入的是数字", Toast.LENGTH_SHORT).show();
//	      } 
//	     p=Pattern.compile("[a-zA-Z]");
//	     m=p.matcher(txt);
//	     if(m.matches()){
//	      Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
//	     }
	     p=Pattern.compile("[\u4e00-\u9fa5]");
	     m=p.matcher(stx);
	     if(m.matches()){
	      return 1;
	     }
	     return 0;
	}
	
	
}
