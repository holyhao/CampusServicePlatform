/**
 *我的维护报修
 *create by songdebin  2016-04-06
 * */
package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.adapter.MyFixAdapter;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.fix.MyFix;
import com.bdyjy.entity.fix.MyFixListBean;
import com.bdyjy.entity.fix.MyFixContentBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;
import com.bdyjy.util.StringUtil;
import com.bumptech.glide.Glide;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class MyFixFragment extends Fragment implements IXListViewListener
{

	private TextView tv_back;
	private Button btn_release;
	private ArrayList list;
	private MainActivity ctx;
	private XListView lv_fix;// 定义展示列表
	private MyFixAdapter adpater;// 定义适配器
	private List<Map<String, Object>> listItems;
	private Handler handler = null;

	
	private static int type = 1;// 获取的数据类型   1代表宿舍   2代表网络   3代表其他
	private int pageSize = 5;// 加载的列表数目
	private int sizeStep = 5;// 每次加载的数目
	
	// 用于显示状态之间的切换
	private RelativeLayout l1;
	private RelativeLayout l2;
	private RelativeLayout l3;

	private TextView tv_tag1;
	private TextView tv_tag2;
	private TextView tv_tag3;

	private View view_tag1;
	private View view_tag2;
	private View view_tag3;

	String toastMsg = null;

	public MyFixFragment(ArrayList list, MainActivity ctx)
	{
		this.ctx = ctx;
		this.list = list;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	
	/**********返回功能**********/
	private void Back(){ 
		ctx.backToClickWithId(list);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.my_fix_fragment, null);
		// 返回上一界面
		tv_back = (TextView) view.findViewById(R.id.tv_back);
//		tv_back.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				ctx.backToClickWithId(list);
//			}
//		});
		
		view.findViewById(R.id.ll_back).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.backToClickWithId(list);
			}
		});
		

		// 跳转至发布页
		btn_release = (Button) view.findViewById(R.id.setting);
		btn_release.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				ctx.jumpToClickWithId(Const.FRAGMENT_ASKFIX_ID, list);
			}
		});

		lv_fix = (XListView) view.findViewById(R.id.lv_fix_list);
		lv_fix.setPullLoadEnable(true);
		lv_fix.setXListViewListener(this);
		
		listItems = new ArrayList<Map<String, Object>>();

		// 点击一项
		lv_fix.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				TextView tv_id = (TextView) view
						.findViewById(R.id.tv_myfix_id_item);
				getMyFixContent(tv_id.getText().toString());

			}
		});
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
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.UPDATE_LISTVIEW:
					lv_fix.onLoad();
					loadData();
					break;
				}
			}
		};
		initTab(view);

		return view;
	}

	private void initTab(View view)
	{
		// 新闻类型切换
		l1 = (RelativeLayout) view.findViewById(R.id.ly_lost1);
		l2 = (RelativeLayout) view.findViewById(R.id.ly_lost2);
		l3 = (RelativeLayout) view.findViewById(R.id.ly_lost3);

		tv_tag1 = (TextView) view.findViewById(R.id.tv_lost_tag1);
		tv_tag2 = (TextView) view.findViewById(R.id.tv_lost_tag2);
		tv_tag3 = (TextView) view.findViewById(R.id.tv_lost_tag3);

		view_tag1 = (View) view.findViewById(R.id.view_lost_tag1);
		view_tag2 = (View) view.findViewById(R.id.view_lost_tag2);
		view_tag3 = (View) view.findViewById(R.id.view_lost_tag3);

		// 每次切换，都访问后台，然后加载到列表中
		l1.setOnClickListener(new OnClickListener()
		{
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v)
			{
				// 并且将当前选中的项字体加粗变色，其他的回归原style
				pageSize=5;
				type=1;
				tabChange(type);
				getMyFixList(type);
			}
		});

		l2.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style
				pageSize=5;
				type=2;
				tabChange(type);
				getMyFixList(type);
			}
		});

		l3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style	
				pageSize=5;
                type=3;
                tabChange(type);
				getMyFixList(type);

			}
		});
	    tabChange(type);
		getMyFixList(type);
	}
	
	private void tabChange(int type){
	switch(type){
	case 1:
		tv_tag1.setTextColor(0xFF5A5A5A);
		tv_tag2.setTextColor(0xFF9A9A9A);
		tv_tag3.setTextColor(0xFF9A9A9A);

		view_tag1.setBackgroundColor(0xFF5A5A5A);
		view_tag2.setBackgroundColor(0x005A5A5A);
		view_tag3.setBackgroundColor(0x005A5A5A);
		break;
	case 2:
		tv_tag1.setTextColor(0xFF9A9A9A);
		tv_tag2.setTextColor(0xFF5A5A5A);
		tv_tag3.setTextColor(0xFF9A9A9A);

		view_tag1.setBackgroundColor(0x005A5A5A);
		view_tag2.setBackgroundColor(0xFF5A5A5A);
		view_tag3.setBackgroundColor(0x005A5A5A);
		break;
	case 3:
		tv_tag1.setTextColor(0xFF9A9A9A);
		tv_tag2.setTextColor(0xFF9A9A9A);
		tv_tag3.setTextColor(0xFF5A5A5A);

		view_tag1.setBackgroundColor(0x005A5A5A);
		view_tag2.setBackgroundColor(0x005A5A5A);
		view_tag3.setBackgroundColor(0xFF5A5A5A);
		break;
	
	}				
	}
	
   

	// 给listview加载数据
	private void loadData()
	{
		// 清空原有内容
		listItems.clear();
		Map<String, Object> map = null;


		for (int i = 0; i < MainActivity.myfixlist.size(); i++)
		{
			map = new HashMap<String, Object>();
			// map.put("title",
			// StringUtil.transStr(MainActivity.myfixlist.get(i)
			// .getTitle())); // 物品标题
			map.put("title", MainActivity.myfixlist.get(i).getTitle()); // 物品标题
			map.put("time", MainActivity.myfixlist.get(i).getCreateTime());
			map.put("id", MainActivity.myfixlist.get(i).getId());
           // map.put("filePath",MainActivity.myfixlist.get(i).getAttArry().get(0).getFilePath().trim()); //传递图片地址
			listItems.add(map);
		}

		if (null == adpater)
		{
			adpater = new MyFixAdapter(ctx, listItems); // 创建适配器
			lv_fix.setAdapter(adpater);
		} else
		{
			adpater.refresh(listItems);
		}
	}

	// 获取我的维护报修列表
	private void getMyFixList(final int type)
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
							.doGet(

							ctx,
									"/admin/maintenance/grid.do?pageNo=1"+"&pageSize="+pageSize+
											"&type=" + type + "&token="
											+ token + "&singnature="
											+ singnature + "&st=" + st+"&userId="+token);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取失败,请重试";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				//System.out.println("请求我的报修列表：请求返回的结果是：" + res.trim());

				// // 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取失败,请重试";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				MyFixListBean bean = JSON.parseObject(res, MyFixListBean.class);
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))
				{
					// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
					List<MyFix> list = bean.getData().getRows();
//					 for (MyFix n : list)
//					 {
//					   System.out.println("这个类是"+n.getAttArry().get(0).getFilePath());
//					 }
					 //将这些新闻对象存储到sp中
					MainActivity.myfixlist = list;
					// // 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW); // 接收消息成功
																			// 临时设置
				} else
				{
					toastMsg = "获取失败,请重试";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}
			}
		}.start();
	}

	// 获取具体内容
	private void getMyFixContent(final String id)
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
							"/admin/maintenance/findById.do?id=" + id
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
				}

				//System.out.println("content的内容是" + res.trim());
				
				//System.out.println("这个类是"+bean.getAttArry().get(0).getFilePath());

				//System.out.println("xxxx:" + bean.getApp_result_key());

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取新闻内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				MyFixContentBean bean = JSON.parseObject(res,
						MyFixContentBean.class);
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
												// 那么我要在这里取得新闻的标题，内容，显示在界面上
				{
					MyFix b = bean.getEntity();
					MainActivity.myfixcontent = b;
				}

				ctx.jumpToClickWithId(Const.FRAGMENT_MYFIXCONTENT_ID, list);
			}
		}.start();

	}
	
	
	/**
	 * 以下是上拉加载，下拉刷新相关代码
	 */
	@Override
	public void onRefresh()// 这是刷新
	{
				
		getMyFixList(type);
	}

	@Override
	public void onLoadMore()// 这是加载更多
	{
		
		 pageSize += sizeStep;
		 getMyFixList(type);
	}


}
