package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.adapter.MyComplaintListAdapter;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.complaint.ComplaintContentQueryResultBean;
import com.bdyjy.entity.complaint.ComplaintQueryResultBean;
import com.bdyjy.entity.complaint.Complaints;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

/**
 * 
 * @author holy 我的投诉建议列表项
 *
 */

/**
 * 有些问题暂不处理
 * 
 * */
public class MyComplaintListFragment extends Fragment implements
		IXListViewListener
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
					listView.onLoad();
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

	private MainActivity ctx;

	// 自定义新闻列表相关
	private XListView listView;
	private MyComplaintListAdapter complaintViewAdapter;
	private List<Map<String, Object>> listItems;

	private TextView tv_back;
	private int pageSize = 5;// 页面新闻条数-容量
	private int sizeStep = 5;// 每次加载的数目
	/**
	 * 1代表食堂，2代表校园安全，3代表网络
	 */
	private static int type = 1;

	private TextView tv_news_type1,tv_news_type2,tv_news_type3;
	
	public MyComplaintListFragment(MainActivity ctx)
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
		if ("personCenter".equals(MainActivity.orderFrom))
		{
			ctx.jumpToPersonalCenterFragment();
			MainActivity.orderFrom = "";// 跳转之后，立即清空
		} else
		{
			ctx.jumpToComplaintFregment();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.mycomplaint_fragment, null);

		tv_news_type1=(TextView)view.findViewById(R.id.tv_news_type1);
		tv_news_type2=(TextView)view.findViewById(R.id.tv_news_type2);
		tv_news_type3=(TextView)view.findViewById(R.id.tv_news_type3);
		// 返回键
		// tv_back = (TextView) view.findViewById(R.id.tv_back);
		view.findViewById(R.id.ll_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if ("personCenter".equals(MainActivity.orderFrom))
						{
							ctx.jumpToPersonalCenterFragment();
							MainActivity.orderFrom = "";// 跳转之后，立即清空
						} else
						{
							ctx.jumpToComplaintFregment();
						}

					}
				});

		listView = (XListView) view.findViewById(R.id.compliantlist);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		

		listItems = new ArrayList<Map<String, Object>>();

		// 选项的点击事件
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 设置选项的点击事件
				TextView tv = (TextView) view.findViewById(R.id.mycomplaint_id);
				// 根据这个项目id，来查询具体的内容，显示出来
				// 查询后台
				// Toast.makeText(getActivity(), tv.getText().toString(),
				// Toast.LENGTH_SHORT).show();
				getContent(tv.getText().toString());

			}
		});

		pageSize = 5;
		System.out.println(MainActivity.newsList);
		initTab(view);
		return view;
	}

	private void loadData()
	{
		// 清空原有内容
		listItems.clear();
		Map<String, Object> map = null;
		if (MainActivity.complaintsList == null)
		{
			System.out.println("complaintsList为空");
		} else
		{
			for (int i = 0; i < MainActivity.complaintsList.size(); i++)
			{
				Log.i("complainsList.i=", i+"");
				map = new HashMap<String, Object>();
				map.put("complaint_image", R.drawable.news_1); // 图片资源,
																// 暂时先使用同一个图片作为新闻
				map.put("complaint_title", MainActivity.complaintsList.get(i)
						.getTitle()); // 物品标题
				map.put("complaint_id", MainActivity.complaintsList.get(i)
						.getId());
				map.put("complaint_classify", MainActivity.complaintsList
						.get(i).getTypeShow());
				map.put("complaint_status", MainActivity.complaintsList.get(i)
						.getStatusShow());
				map.put("creationTime", MainActivity.complaintsList.get(i)
						.getCreateTime());
				
				List<attArryData> attArry = MainActivity.complaintsList.get(i)
						.getAttArry();
				
				String http = "";
				String http2 = "";
				String http1 = "";
				try
				{
					if(attArry.size() != 0){
						http1 = attArry.get(0).getFilePath().toString().trim();
						http2 = MainActivity.complaintsList.get(i)
								.getAttachmentPrefix();
					}else{
						//没有投诉建议图片
						Log.i("attArry", "第"+i+"条投诉建议没有图片");
					}
					
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				http = http2 + http1;
				map.put("http", http);
				System.out.println("图片地址为：" + http);
				listItems.add(map);

			}
			if (null == complaintViewAdapter)
			{
				complaintViewAdapter = new MyComplaintListAdapter(ctx,
						listItems); // 创建适配器
				listView.setAdapter(complaintViewAdapter);
			} else
			{
				complaintViewAdapter.refresh(listItems);
			}
		}
	}

	// 在这里封装从后台一个获取投诉建议列表的方法
	private void getList(final int type)
	{
		Thread thread = new Thread()
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
							"/admin/proposal/grid.do?pageNo=1&pageSize="
									+ pageSize + "&type=" + type + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st+"&userId="+token);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取投诉建议列表信息失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求我的投诉建议列表返回的结果是：" + res.trim());

				// 尝试将json串转化成bean对象
				ComplaintQueryResultBean nqrb = JSON.parseObject(res,
						ComplaintQueryResultBean.class);
				// System.out.println("xxxx:" + nqrb.getApp_result_key());

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取投诉列表失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = nqrb.getApp_result_key();
				if ("0".equals(app_result_key))
				{
					// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
					List<Complaints> list = nqrb.getData().getRows();
					for (Complaints n : list)
					{
						System.out.println(n.getTitle());
					}
					// 将这些新闻对象存储到sp中
					MainActivity.complaintsList = list;
					// 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}
			}
		};
		thread.start();
	}

	// 在这里封装从后台一个获取投诉列表内容的方法
	private void getContent(final String id)
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
							"/admin/proposal/findById.do?id=" + id + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);
					
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取内容失败";
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					handler.sendEmptyMessage(HandlerOrder.TOAST);

					return;
				}
				System.out.println("请求投诉建议内容返回的结果是：" + res.trim());
				ComplaintContentQueryResultBean bean = JSON.parseObject(res,
						ComplaintContentQueryResultBean.class);

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
					Complaints b = bean.getEntity();
					MainActivity.complaints = b;
					System.out.println("news:" + b);
				}
				ctx.jumpToComplaintContentFregment();
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
		getList(type);
	}

	@Override
	public void onLoadMore()// 这是加载更多
	{
		// toastMsg = "检测到上拉加载动作";
		// handler.sendEmptyMessage(HandlerOrder.TOAST);
		pageSize += sizeStep;
		getList(type);
	}

	// 3种类型的新闻列表的切换
	private LinearLayout ll_news_type1, ll_news_type2, ll_news_type3;

	/**
	 * 初始化选项卡
	 * 
	 * @param view
	 */
	@SuppressLint("NewApi")
	private void initTab(View view)
	{
		// 新闻类型切换
		ll_news_type1 = (LinearLayout) view.findViewById(R.id.ll_news_type1);
		ll_news_type2 = (LinearLayout) view.findViewById(R.id.ll_news_type2);
		ll_news_type3 = (LinearLayout) view.findViewById(R.id.ll_news_type3);

		// 每次切换，都访问后台，然后加载到列表中
		ll_news_type1.setOnClickListener(new OnClickListener()
		{
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style


				pageSize = 5;
				type = 1;
				// getNewsList(type);
				tabChange(type);
				getList(type);// 调用南燕要闻特别方法
			}
		});

		ll_news_type2.setOnClickListener(new OnClickListener()
		{

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style


				pageSize = 5;
				type = 2;
				tabChange(type);
				getList(type);

			}
		});

		ll_news_type3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style
	

				pageSize = 5;
				type = 3;
				tabChange(type);
				getList(type);

			}
		});
		tabChange(type);
		getList(type);// 调用南燕要闻特别方法
	}
	
private void tabChange(int type){
switch(type){
case 1:

	ll_news_type1.setBackgroundResource(R.drawable.selected_item_bg);	
	ll_news_type2.setBackgroundResource(R.drawable.not_selected_item_bg);
	ll_news_type3.setBackgroundResource(R.drawable.not_selected_item_bg);
	tv_news_type1.setTextColor(getResources().getColor(R.color.BLACK_TITLE));
	tv_news_type2.setTextColor(getResources().getColor(R.color.GRAY_TITLE));
	tv_news_type3.setTextColor(getResources().getColor(R.color.GRAY_TITLE));
	break;
case 2:
	ll_news_type1.setBackgroundResource(R.drawable.not_selected_item_bg);
	ll_news_type2.setBackgroundResource(R.drawable.selected_item_bg);
	ll_news_type3.setBackgroundResource(R.drawable.not_selected_item_bg);
	tv_news_type1.setTextColor(getResources().getColor(R.color.GRAY_TITLE));
	tv_news_type2.setTextColor(getResources().getColor(R.color.BLACK_TITLE));
	tv_news_type3.setTextColor(getResources().getColor(R.color.GRAY_TITLE));
	break;
case 3:
	ll_news_type1.setBackgroundResource(R.drawable.not_selected_item_bg);
	ll_news_type2.setBackgroundResource(R.drawable.not_selected_item_bg);
	ll_news_type3.setBackgroundResource(R.drawable.selected_item_bg);
	tv_news_type1.setTextColor(getResources().getColor(R.color.GRAY_TITLE));
	tv_news_type2.setTextColor(getResources().getColor(R.color.GRAY_TITLE));
	tv_news_type3.setTextColor(getResources().getColor(R.color.BLACK_TITLE));
	break;
}
	
}	
	
	
	
}
