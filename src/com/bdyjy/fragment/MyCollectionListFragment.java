package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
import com.bdyjy.adapter.MyCollectionListAdapter;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.constants.ParleConstant;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.News;
import com.bdyjy.entity.NewsContentQueryResultBean;
import com.bdyjy.entity.activity.Activity;
import com.bdyjy.entity.activity.ActivityContentQueryResultBean;
import com.bdyjy.entity.assistant.AssistantContentQueryResultBean;
import com.bdyjy.entity.assistant.AssistantNewBean;
import com.bdyjy.entity.collection.MyCollection;
import com.bdyjy.entity.collection.MyCollectionResultBean;
import com.bdyjy.entity.lecture.Lecture;
import com.bdyjy.entity.lecture.LectureContentQueryResultBean;
import com.bdyjy.entity.lostfind.LostFind;
import com.bdyjy.entity.lostfind.LostFindContentBean;
import com.bdyjy.entity.recruit.RecruitContentQueryResultBean;
import com.bdyjy.entity.recruit.RecruitNewBean;
import com.bdyjy.entity.recruit.RecruitQueryResultBean;
import com.bdyjy.entity.secondMarket.SecondMarket;
import com.bdyjy.entity.secondMarket.SecondMarketContentBean;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.util.LogUtil;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

/**
 * 我的收藏list
 * 
 * @author parle
 * 
 */
public class MyCollectionListFragment extends Fragment implements
		IXListViewListener
{

	Handler handler = null;
	String toastMsg = null;
	final int type = 1; // 新闻类型，对于这个页面， 只有一种type
	final int TIME_LENGTH = 10;

	private int pageSize = 5;// 加载的列表数目
	private int sizeStep = 5;// 每次加载的数目

	// 初始化Handler
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
	private MyCollectionListAdapter MyCollectionListAdapter;
	private List<Map<String, Object>> listItems;
	private TextView tv_back;
	private EditText et_search = null;
	private RelativeLayout rl_search_off = null;
	private RelativeLayout rl_search_on = null;
	private TextView tv_search_cancel;

	public MyCollectionListFragment(MainActivity ctx)
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

		View view = inflater.inflate(R.layout.recruit_list_fragment, null);

		view.findViewById(R.id.rl_search).setVisibility(View.GONE);

		tv_back = (TextView) view.findViewById(R.id.recruit_list_tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToPersonalCenterFragment();
			}
		});

		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("我的收藏");

		listView = (XListView) view.findViewById(R.id.recruit_listview);
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

				// // 设置选项的点击事件
				TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
				TextView tv_foreignId = (TextView) view
						.findViewById(R.id.recruit_list_item_id);

				// 拿到这2个东西，然后根据type的值，进行分别查询，分别跳转到对应的详情页,对应详情页的返回按钮也要根据来源进行返回事件的绑定
				String type = tv_type.getText().toString();
				String foreignId = tv_foreignId.getText().toString();
                
				System.out.println(type + " - " + foreignId);

				int typeInt = Integer.parseInt(type);
				LogUtil.LogPrint("px","类型是=="+ typeInt);
				switch (typeInt)
				{
				case CollectionTypeConst.NEWS:
					getNewsContent(foreignId);
					break;
				case CollectionTypeConst.LECTURE:
					// 查询讲座信息，并且带数据跳转到讲座详情
					getLectureContent(foreignId);
					break;
				case CollectionTypeConst.ACTIVITIES:
					getActivityContent(foreignId);
					break;
				case CollectionTypeConst.NOTICE:
					break;
				case CollectionTypeConst.RECRUIT:
					getRecruitContent(foreignId);
					break;
				case CollectionTypeConst.WORK_STUDY:
					getAssistantContent(foreignId);
					break;
				case CollectionTypeConst.SECOND_HAND_TRADE:
					getFleaContent(foreignId);
					break;
				case CollectionTypeConst.LOSTANDFOUND:
					getLostFindContent(foreignId);
					break;
				}

			}
		});

		// 搜索功能view
		rl_search_off = (RelativeLayout) view
				.findViewById(R.id.recruit_searchbar_off);
		rl_search_on = (RelativeLayout) view
				.findViewById(R.id.recruit_searchbar_on);
		et_search = (EditText) view.findViewById(R.id.recruit_et_search);
		tv_search_cancel = (TextView) view
				.findViewById(R.id.recruit_tv_search_cancel);

		// 点击开始输入
		rl_search_off.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				rl_search_off.setVisibility(View.INVISIBLE);
				rl_search_on.setVisibility(View.VISIBLE);
				et_search.setFocusable(true);
				et_search.requestFocus();

				// 自动弹出输入法
				InputMethodManager imm = (InputMethodManager) et_search
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

				Timer timer = new Timer();

				timer.schedule(new TimerTask()
				{
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

		// 取消搜索
		tv_search_cancel = (TextView) view
				.findViewById(R.id.recruit_tv_search_cancel);
		// 关闭搜索框
		tv_search_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				rl_search_off.setVisibility(View.VISIBLE);
				rl_search_on.setVisibility(View.INVISIBLE);
				et_search.setText("");
			}
		});

		// // 搜索框输入监听，后台请求
		// et_search = (EditText) view.findViewById(R.id.recruit_et_search);
		// et_search.setOnEditorActionListener(new OnEditorActionListener()
		// {
		// @Override
		// public boolean onEditorAction(TextView v, int actionId,
		// KeyEvent event)
		// {
		// if ((actionId == 0 || actionId == 3) && event != null)
		// {
		// // 点击搜索，关闭键盘
		// InputMethodManager inputManager = (InputMethodManager) et_search
		// .getContext().getSystemService(
		// Context.INPUT_METHOD_SERVICE);
		// inputManager.hideSoftInputFromWindow(ctx.getCurrentFocus()
		// .getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
		// // 后台请求搜索
		// // searchNewsList();
		// }
		// return false;
		// }
		// });

		// 设置列表数据;
		if (null == MainActivity.myCollectionList)
			getNewsList();
		else
			loadData();

		return view;
	}

	/**
	 * 对于兼职信息的内容，继承自新闻类
	 */
	private void loadData()
	{
		// 清空原有内容
		listItems.clear();

		Map<String, Object> map = null;

		if (null != MainActivity.myCollectionList)
		{
			for (int i = 0; i < MainActivity.myCollectionList.size(); i++)
			{
				map = new HashMap<String, Object>();
				// 列表显示的内容
				map.put("title", MainActivity.myCollectionList.get(i)
						.getTitle());
				map.put("type", MainActivity.myCollectionList.get(i).getType());
				map.put("createTime", MainActivity.myCollectionList.get(i)
						.getCreateTime().substring(0, TIME_LENGTH));
				map.put("foreignId", MainActivity.myCollectionList.get(i)
						.getForeignId());

				listItems.add(map);
			}
		}

		if (null == MyCollectionListAdapter)
		{
			MyCollectionListAdapter = new MyCollectionListAdapter(ctx,
					listItems); // 创建适配器
			listView.setAdapter(MyCollectionListAdapter);
		} else
		{
			MyCollectionListAdapter.refresh(listItems);
		}
	}

	private void getNewsList()
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
				String url = "/collect/grid.do?pageNo=1&pageSize=" + pageSize
						+ "&token=" + token + "&singnature=" + singnature
						+ "&st=" + st+"&userId="+token;

				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(ctx, url);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取我的收藏失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}

				if (res.trim().length() == 0)
				{
					toastMsg = "获取我的收藏失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}

				System.out.println("我的收藏res：\n" + res);

				// 返回值将会是JSON格式的数据，我要在这里解析
				// 尝试将json串转化成bean对象
				MyCollectionResultBean recruitQRB = JSON.parseObject(res,
						MyCollectionResultBean.class);

				String app_result_key = recruitQRB.getApp_result_key();

				if ("0".equals(app_result_key))
				{
					// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
					List<MyCollection> list = recruitQRB.getData().getRows();
					for (MyCollection n : list)
					{
						System.out.println(n.getTitle());
					}

					// 将这些新闻对象存储到sp中
					MainActivity.myCollectionList = list;

					// 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}

			}
		}.start();

	}

//	/**
//	 * 在这里封装从后台一个获取新闻内容的方法
//	 * 
//	 * @param id
//	 */
//	private void getNewsContent(final String id)
//	{
//		new Thread()
//		{
//			@Override
//			public void run()
//			{
//				// 试试get请求
//				String res = null;
//
//				// 从sharePreference中取出之前存储的参数
//				String token = (String) SPUtils.get(ctx, "token", "");
//				String singnature = (String) SPUtils.get(ctx, "singnature", "");
//				String st = (String) SPUtils.get(ctx, "st", "");
//
//				try
//				{
//					///admin/recruit/findById.do?id=
//					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
//					res = OkHttpUtils.getInstance().doGet(
//							ctx,
//							"/admin/news/initView.do?id=" + id + "&token="
//									+ token + "&singnature=" + singnature
//									+ "&st=" + st);
//					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//					toastMsg = "获取详情信息失败";
//					handler.sendEmptyMessage(HandlerOrder.TOAST);
//					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
//					return;
//				}
//
//				if (res.trim().length() == 0)
//				{
//					toastMsg = "获取新闻内容失败";
//					handler.sendEmptyMessage(HandlerOrder.TOAST);
//					return;
//				}
//
//				// JSON解析
//				RecruitContentQueryResultBean recruitCQR = JSON.parseObject(
//						res, RecruitContentQueryResultBean.class);
//
//				String app_result_key = recruitCQR.getApp_result_key();
//				// System.out.println("xxxx:" + app_result_key);
//
//				if ("0".equals(app_result_key))
//				{
//					RecruitNewBean recruit = recruitCQR.getEntity();
//					MainActivity.recruitNew = recruit;
//				}
//				ctx.jumpToClickById(ParleConstant.JUMP_RECRUIT_CONTENT);
//
//			}
//		}.start();
//	}

	// /**
	// * 搜索新闻
	// */
	// private void searchNewsList()
	// {
	//
	// Thread thread = new Thread()
	// {
	// @Override
	// public void run()
	// {
	// // 试试get请求
	// String res = null;
	//
	// // 从sharePreference中取出之前存储的参数
	// String token = (String) SPUtils.get(ctx, "token", "");
	// String singnature = (String) SPUtils.get(ctx, "singnature", "");
	// String st = (String) SPUtils.get(ctx, "st", "");
	// String searchKeys = et_search.getText().toString().trim();
	//
	// try
	// {
	// handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
	// res = OkHttpUtils.getInstance().doGet(
	// ctx,
	// "/admin/recruit/grid.do?pageNo=1&pageSize=15&type="
	//
	// + type + "&pageSearchKeys=" + searchKeys
	// + "&token=" + token + "&singnature="
	// + singnature + "&st=" + st);
	// handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
	// } catch (Exception e)
	// {
	// e.printStackTrace();
	// toastMsg = "搜索失败";
	// handler.sendEmptyMessage(HandlerOrder.TOAST);
	// handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
	// return;
	// }
	//
	// if (res.trim().length() == 0)
	// {
	// toastMsg = "搜索失败";
	// handler.sendEmptyMessage(HandlerOrder.TOAST);
	// handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
	// return;
	// }
	//
	// // 返回值将会是JSON格式的数据，我要在这里解析
	// // 尝试将json串转化成bean对象
	// RecruitQueryResultBean recruitQRB = JSON.parseObject(res,
	// RecruitQueryResultBean.class);
	// // System.out.println("xxxx:" + nqrb.getApp_result_key());
	//
	// String app_result_key = recruitQRB.getApp_result_key();
	//
	// if ("0".equals(app_result_key))
	// {
	// // 到了这里说明正常返回了数据，我要在这里获取新闻的标题
	// List<RecruitNewBean> list = recruitQRB.getData().getRows();
	//
	// if (list.size() == 0)
	// {
	// toastMsg = "没找到您要的数据";
	// handler.sendEmptyMessage(HandlerOrder.TOAST);
	// }
	//
	// for (RecruitNewBean n : list)
	// {
	// System.out.println(n.getTitle());
	// }
	//
	// // 将这些新闻对象存储到sp中
	// MainActivity.recruitNewsList = list;
	//
	// // 使用handler去通知主线程更新listview
	// handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
	// } else
	// {
	// toastMsg = "获取内容失败，请稍后重试...";
	// handler.sendEmptyMessage(HandlerOrder.TOAST);
	// return;
	// }
	//
	// }
	// };
	// thread.start();
	// }

	@Override
	public void onRefresh()
	{
		getNewsList();
	}

	@Override
	public void onLoadMore()
	{
		pageSize += sizeStep;
		getNewsList();
	}

	// 以下全都是查询具体条目内容的方法：
	// 在这里封装从后台一个获取讲座信息内容的方法
	private void getLectureContent(final String id)
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
							"/admin/lecture/findById.do?id=" + id + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
				}

				LectureContentQueryResultBean bean = JSON.parseObject(res,
						LectureContentQueryResultBean.class);

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取讲座信息内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))// 如果正常获得了讲座信息的内容,
												// 那么我要在这里取得讲座信息的标题，内容，显示在界面上
				{
					Lecture b = bean.getEntity();
					MainActivity.orderFrom = "myCollectionList";
					MainActivity.lecture = b;
					ctx.jumpToLectureContentFragment();
				} else
				{
					toastMsg = "查询异常...";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}

			}
		}.start();
	}

	// 在这里封装从后台一个获取社团活动内容的方法
	private void getActivityContent(final String id)
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
							"/admin/activity/findById.do?id=" + id + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
				}

				System.out.println("请求具体的活动内容：" + res.trim());
				ActivityContentQueryResultBean bean = JSON.parseObject(res,
						ActivityContentQueryResultBean.class);

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取社团活动内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))// 如果正常获得了社团活动的内容,
												// 那么我要在这里取得社团活动的标题，内容，显示在界面上
				{
					Activity b = bean.getEntity();
					MainActivity.orderFrom = "myCollectionList";
					MainActivity.activity = b;
					ctx.jumpToActivityContentFragment();
				} else
				{
					toastMsg = "查询异常...";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}

			}
		}.start();
	}

	/**
	 * 获取招聘信息详情
	 * 
	 * @param id
	 */
	private void getRecruitContent(final String id)
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
							"/admin/recruit/findById.do?id=" + id + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取详情信息失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}

				if (res.trim().length() == 0)
				{
					toastMsg = "获取新闻内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}

				// JSON解析
				RecruitContentQueryResultBean recruitCQR = JSON.parseObject(
						res, RecruitContentQueryResultBean.class);

				String app_result_key = recruitCQR.getApp_result_key();
				// System.out.println("xxxx:" + app_result_key);

				if ("0".equals(app_result_key))
				{
					RecruitNewBean recruit = recruitCQR.getEntity();
					MainActivity.orderFrom = "myCollectionList";
					MainActivity.recruitNew = recruit;
					ctx.jumpToClickById(ParleConstant.JUMP_RECRUIT_CONTENT);
				} else
				{
					toastMsg = "查询异常...";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}
			}
		}.start();
	}

	/**
	 * 获取勤工俭学详情
	 * 
	 * @param id
	 */
	private void getAssistantContent(final String id)
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
							"/admin/asistance/findById.do?id=" + id + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取详情信息失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}

				if (res.trim().length() == 0)
				{
					toastMsg = "获取新闻内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("内容详情页：" + res.trim());
				Log.d("content", res.trim());

				// JSON解析
				AssistantContentQueryResultBean assistantCQR = JSON
						.parseObject(res, AssistantContentQueryResultBean.class);

				String app_result_key = assistantCQR.getApp_result_key();
				// System.out.println("xxxx:" + app_result_key);

				if ("0".equals(app_result_key))
				{
					AssistantNewBean assistant = assistantCQR.getEntity();
					MainActivity.assistantNew = assistant;
					MainActivity.orderFrom = "myCollectionList";
					ctx.jumpToClickById(ParleConstant.JUMP_ASSISTANT_CONTENT);
				} else
				{
					toastMsg = "查询异常...";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}

			}
		}.start();
	}

	/**
	 * 获取二手交易详情
	 * 
	 * @param id
	 */
	private void getFleaContent(final String id)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);// 打开进度条
				// 试试get请求
				String res = null;

				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");

				try
				{
					res = OkHttpUtils.getInstance().doGet(
							ctx,
							"/admin//secondhanddeal/findById.do?id=" + id
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);// 隐藏进度条
					// System.out.println("content的内容是"+res.trim());

					// 返回值将会是JSON格式的数据，我要在这里解析
					if (res.trim().length() == 0)
					{
						toastMsg = "获取内容失败,请检查你的网络";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					} else
					{
						SecondMarketContentBean bean = JSON.parseObject(res,
								SecondMarketContentBean.class);
						String app_result_key = bean.getApp_result_key();
						if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
														// 那么我要在这里取得新闻的标题，内容，显示在界面上
						{
							SecondMarket b = bean.getEntity();
							MainActivity.secondmarketcontent = b;
							MainActivity.orderFrom = "myCollectionList";
							// 跳转至内容页
							ctx.jumpToClickById(Const.FRAGMENT_FLEACONTENT_ID);

						} else
						{
							toastMsg = "获取内容失败,请检查你的网络";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
						}

					}

				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取内容失败,请检查你的网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);// 隐藏进度条
				}

				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);// 隐藏进度条

			}
		}.start();
	}

	/**
	 * 获取失物招领内容
	 * 
	 * @param id
	 */
	private void getLostFindContent(final String id)
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

				try
				{
					res = OkHttpUtils.getInstance().doGet(
							ctx,
							"/admin/lostfound/findById.do?id=" + id + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);

					LostFindContentBean bean = JSON.parseObject(res,
							LostFindContentBean.class);

					if (res.trim().length() == 0)
					{
						toastMsg = "获取失败，请重试";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					} else
					{
						String app_result_key = bean.getApp_result_key();
						if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
														// 那么我要在这里取得新闻的标题，内容，显示在界面上
						{
							LostFind b = bean.getEntity();
							MainActivity.lostfindcontent = b;
							MainActivity.orderFrom = "myCollectionList";
							// 跳转到内容页
							ctx.jumpToClickById(Const.FRAGMENT_LOSTFINDCONTENT_ID);

						} else
						{
							toastMsg = "获取内容失败，请检查网络";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
							return;
						}

					}

				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取失败，请重试";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				}
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);

				System.out.println("content的内容是" + res.trim());
				// Log.i("token的值", token);
				// Log.d("content", res.trim());

			}
		}.start();

	}
	
	// 在这里封装从后台一个获取新闻内容的方法
		private void getNewsContent(final String id)
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
						///admin/news/app/initView.do?   
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
						res = OkHttpUtils.getInstance().doGet(
								ctx,
								"/admin/news/initView.do?id=" + id + "&token="
										+ token + "&singnature=" + singnature    
										+ "&st=" + st);
						LogUtil.LogPrint("px", res);
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					} catch (Exception e)
					{
						e.printStackTrace();
					}

					System.out.println("新闻内容res：" + res);
					NewsContentQueryResultBean bean = JSON.parseObject(res,
							NewsContentQueryResultBean.class);

					// System.out.println("xxxx:" + bean.getApp_result_key());

					// 返回值将会是JSON格式的数据，我要在这里解析
					if (res.trim().length() == 0)
					{
						toastMsg = "获取新闻内容失败";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
						return;
					}
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
													// 那么我要在这里取得新闻的标题，内容，显示在界面上
					{
						News b = bean.getEntity();
						MainActivity.news = b;
						MainActivity.newsDetail = null;
						ctx.jumpToNewsContentFregment(2);
					}
					
				}
			}.start();
		}
}