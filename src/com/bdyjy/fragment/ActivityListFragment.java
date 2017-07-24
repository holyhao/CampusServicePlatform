package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.adapter.ActivityListAdapter;

import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.activity.Activity;
import com.bdyjy.entity.activity.ActivityContentQueryResultBean;
import com.bdyjy.entity.activity.ActivityQueryResultBean;

import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 社团活动列表
 * 
 * @author cuicui create at 2016-04-02 17:02
 * 
 */
public class ActivityListFragment extends Fragment implements IXListViewListener {
	Handler handler = null;
	String toastMsg = null;

	private MainActivity ctx;

	// 自定义新闻列表相关
	private XListView activityListView;
	private ActivityListAdapter activityListViewAdapter;
	private List<Map<String, Object>> activityListItems;

	private TextView tv_activity_back;

	private int pageSize = 5;// 页面新闻条数-容量
	private int sizeStep = 5;// 每次加载的数目

	public ActivityListFragment(MainActivity ctx) {
		this.ctx = ctx;
		initHandler();
		ctx.keydown = new KeyDown() {
			@Override
			public void OnkeyDown() {// 在这里重写 返回事件
				Back();
			}
		};
	}

	/********** 返回功能 **********/
	private void Back() {
		ctx.jumpToFirstPageFregment();
	}

	private void initHandler() {
		handler = new Handler(ctx.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HandlerOrder.TOAST:
					// TODO
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.UPDATE_LISTVIEW:
					activityListView.onLoad();
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activitylist_fragment, null);

		// tv_activity_back = (TextView)
		// view.findViewById(R.id.tv_activity_back);
		// tv_activity_back.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// ctx.jumpToFirstPageFregment();
		// }
		// });
		view.findViewById(R.id.ll_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ctx.jumpToFirstPageFregment();
			}
		});

		activityListView = (XListView) view.findViewById(R.id.activity_listview);
		activityListView.setDivider(null);
		activityListView.setPullLoadEnable(true);
		activityListView.setXListViewListener(this);
		activityListItems = new ArrayList<Map<String, Object>>();

		// 选项的点击事件
		activityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 设置选项的点击事件
				TextView tv_activity_id = (TextView) view.findViewById(R.id.tv_activity_id);
				// 根据这个社团活动id，来查询具体的社团活动，显示出来
				// 查询后台
				getActivityContent(tv_activity_id.getText().toString());
				System.out.println("请求具体活动内容" + tv_activity_id.getText().toString());
			}
		});
		if (null == MainActivity.activityList)
			getActivityList();
		else
			loadData();

		return view;
	}

	private void loadData() {
		// 清空原有内容
		activityListItems.clear();
		Map<String, Object> map = null;
		if (MainActivity.activityList == null) {
			System.out.println("activityList为空");
		} else {
			for (int i = 0; i < MainActivity.activityList.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("poster", R.drawable.news_1); // 图片资源,
														// 暂时先使用同一个图片作为新闻
				map.put("subject", MainActivity.activityList.get(i).getSubject()); // 物品标题
				map.put("tv_activity_id", MainActivity.activityList.get(i).getId());
				map.put("hoster", MainActivity.activityList.get(i).getHoster()); // 图片资源,
																					// 暂时先使用同一个图片作为新闻
				map.put("actPlace", MainActivity.activityList.get(i).getActplace()); // 物品标题
				map.put("actTime", MainActivity.activityList.get(i).getActtime());
				map.put("content", MainActivity.activityList.get(i).getContent());

				String http = "";
				String http2 = "";
				String http1 = "";
				try {
					http1 = MainActivity.activityList.get(i).getPoster().toString().trim();
					http2 = MainActivity.activityList.get(i).getAttachmentPrefix();
				} catch (Exception e) {
					e.printStackTrace();
				}
				http = http2 + http1;
				System.out.println("s社团活动图片的地址为：" + http);
				map.put("http", http);
				activityListItems.add(map);
			}
		}

		if (null == activityListViewAdapter) {
			activityListViewAdapter = new ActivityListAdapter(ctx, activityListItems); // 创建适配器
			activityListView.setAdapter(activityListViewAdapter);
		} else {
			activityListViewAdapter.refresh(activityListItems);
		}
	}

	// 在这里封装从后台一个获取社团活动列表的方法
	private void getActivityList() {
		new Thread() {
			@Override
			public void run() {
				// 试试get请求
				String res = null;

				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");

				try {
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(

							ctx, "/admin/activity/grid.do?pageNo=1&pageSize=" + pageSize

									+ "&token=" + token + "&singnature=" + singnature + "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e) {
					e.printStackTrace();
					toastMsg = "获取社团活动内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求社团活动：" + res.trim());

				// 尝试将json串转化成bean对象
				ActivityQueryResultBean nqrb = JSON.parseObject(res, ActivityQueryResultBean.class);

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0) {
					toastMsg = "获取社团活动内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = nqrb.getApp_result_key();
				if ("0".equals(app_result_key)) {
					// 到了这里说明正常返回了数据，我要在这里获取社团活动的标题
					List<Activity> list = nqrb.getData().getRows();

					MainActivity.activityList = list;
					// 使用handler去通知主线程更新activityListView
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}
			}
		}.start();
	}

	// 在这里封装从后台一个获取社团活动内容的方法
	private void getActivityContent(final String id) {
		new Thread() {
			@Override
			public void run() {
				// 试试get请求
				String res = null;

				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");

				try {
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(ctx, "/admin/activity/findById.do?id=" + id + "&token="
							+ token + "&singnature=" + singnature + "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("请求具体的活动内容：" + res.trim());
				ActivityContentQueryResultBean bean = JSON.parseObject(res, ActivityContentQueryResultBean.class);

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0) {
					toastMsg = "获取社团活动内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))// 如果正常获得了社团活动的内容,
												// 那么我要在这里取得社团活动的标题，内容，显示在界面上
				{
					Activity b = bean.getEntity();
					MainActivity.activity = b;
				}
				ctx.jumpToActivityContentFragment();
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
		getActivityList();
	}

	@Override
	public void onLoadMore()// 这是加载更多
	{
		// toastMsg = "检测到上拉加载动作";
		// handler.sendEmptyMessage(HandlerOrder.TOAST);
		pageSize += sizeStep;
		getActivityList();
	}

}
