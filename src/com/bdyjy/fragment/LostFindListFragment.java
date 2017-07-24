/**
 * LostFindListFragment.java[v 1.0.0]
 * class:com.mydream.fragment.freg,LostFindListFragment
 * 宋德宾 create at 2016-3-30 
 * 主要功能：失物招领列表
 */

package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;
import com.bdyjy.adapter.LostFindListAdapter;
import com.bdyjy.entity.lostfind.LostFindListBean;
import com.bdyjy.entity.lostfind.LostFindContentBean;
import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.lostfind.LostFind;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/*
 * 失物招领界面
 */

@SuppressLint("NewApi")
public class LostFindListFragment extends Fragment implements
		IXListViewListener
{
	private MainActivity ctx;
	private TextView tv_back;
	private EditText et_search; // 搜索状态下的输入框
	private TextView tv_search_cancel;// 关闭搜索功能
	private RelativeLayout rl_searchbar_off; // 关闭状态下的输入框
	private RelativeLayout rl_searchbar_on;// 工作状态下的输入框

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

	private static int type = 0;// 获取的数据类型 0标志全部 1表示失物 2表示招领
	private int pageSize = 5;// 加载的列表数目
	private int sizeStep = 5;// 每次加载的数目
	private String searchKeys;// 搜索关键字
	private boolean is_search = false;// 定义搜索状态 true 处于搜索状态 false处于正常状态
	private Button delete_search; // 关于搜索功能的逻辑问题还没有解决

	private TextWatcher textWatcher = new TextWatcher()
	{ // 对搜索框输入内容做一个监视 目前的功能是 监控有无输入内容， 在有字时就 显示清空按键

		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count)
		{
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after)
		{
			// TODO Auto-generated method stub
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s)
		{ // 内容变动之后

			searchKeys = et_search.getText().toString().trim();
			if (TextUtils.isEmpty(searchKeys))
			{
				delete_search.setVisibility(View.INVISIBLE);
			} else
			{
				delete_search.setVisibility(View.VISIBLE);
			}

		}
	};

	private TextView tv_release;
	private XListView lv_lost;// 定义展示列表

	private LostFindListAdapter adpater;// 定义适配器
	private List<Map<String, Object>> listItems;

	public LostFindListFragment(MainActivity ctx)
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
		ctx.jumpToLakesideFragment();
	}

	/**************** 消息处理部分 *******************/

	String toastMsg = null;
	Handler handler = null;

	private void initHandler()
	{
		handler = new Handler(ctx.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case HandlerOrder.TOAST: // 获取数据失败
					// TODO
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.UPDATE_LISTVIEW: // 正常接收数据
					lv_lost.onLoad();
					loadData();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.POST_OK: // 关键字提交成功 异步获取用于 搜索框功能
					String res;
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					res = msg.getData().getString("body");
					LostFindListBean bean = JSON.parseObject(res,
							LostFindListBean.class);
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))
					{
						List<LostFind> list = bean.getData().getRows();
						MainActivity.lostfindlist = list;
						handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);

						return;
					} else
					{
						toastMsg = "获取内容失败，请检查网络";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
					}
					break;
				case HandlerOrder.POST_ERROR: // 获取失败
					toastMsg = "获取内容失败，请检查网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					break;
				}
			}
		};
	}

	/***********************************************/

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.lostfind_list_fragment, null);

		// 返回上一界面
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToLakesideFragment();
			}
		});

		// 跳转至发布界面
		tv_release = (TextView) view.findViewById(R.id.setting);
		tv_release.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToLostRelease(Const.FRAGMENT2_ID);
			}
		});

		lv_lost = (XListView) view.findViewById(R.id.lv_lostfind_list);
		lv_lost.setPullLoadEnable(true);
		lv_lost.setXListViewListener(this);
		lv_lost.setHeaderDividersEnabled(false);

		listItems = new ArrayList<Map<String, Object>>();

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

				is_search = true; // 进入开启状态

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

		tv_search_cancel = (TextView) view.findViewById(R.id.tv_search_cancel);
		// 关闭搜索框
		tv_search_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				is_search = false; // 搜索状态关闭
				rl_searchbar_off.setVisibility(View.VISIBLE);
				rl_searchbar_on.setVisibility(View.INVISIBLE);
				et_search.setText("");
				pageSize = 5;
				// 并且将当前选中的项字体加粗变色，其他的回归原style
				tv_tag1.setTextColor(0xFF5A5A5A);
				tv_tag2.setTextColor(0xFF9A9A9A);
				tv_tag3.setTextColor(0xFF9A9A9A);
				type = 0;
				view_tag1.setBackgroundColor(0xFF5A5A5A);
				view_tag2.setBackgroundColor(0x005A5A5A);
				view_tag3.setBackgroundColor(0x005A5A5A);
				pageSize = 5;
				getLostFindList(type);

			}
		});

		// 搜索框输入监听
		et_search = (EditText) view.findViewById(R.id.et_search);
		et_search.addTextChangedListener(textWatcher);
		et_search.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event)
			{
				if ((actionId == 0 || actionId == 3) && event != null)
				{

					search();

				}

				return false;
			}
		});

		lv_lost.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 设置选项的点击事件
				TextView tv = (TextView) view
						.findViewById(R.id.tv_lostfind_id_item);
				getLostFindContent(tv.getText().toString());

			}
		});

		// 初始化删除按钮
		delete_search = (Button) view.findViewById(R.id.bt_search_delete);
		delete_search.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				et_search.setText("");
			}
		});

		if (null == MainActivity.lostfindlist)
			getLostFindList(0);// 获取列表内容
		else
			loadData();
		initTab(view);

		return view;
	}

	/**
	 * 初始化选项卡
	 * 
	 * @param view
	 */
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
				type = 0;
				pageSize = 5;
				tagChange(type);
				getLostFindList(type);
			}
		});

		l2.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style

				type = 1;
				pageSize = 5;
				tagChange(type);
				getLostFindList(type);
			}
		});

		l3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style
				type = 2;
				pageSize = 5;
				tagChange(type);
				getLostFindList(type);
			}
		});
		tagChange(type);
	}
	
	public void tagChange(int type){
		switch(type){
		case 0://选中第一个 
			tv_tag1.setTextColor(0xFF5A5A5A);
			tv_tag2.setTextColor(0xFF9A9A9A);
			tv_tag3.setTextColor(0xFF9A9A9A);
			
			view_tag1.setBackgroundColor(0xFF5A5A5A);
			view_tag2.setBackgroundColor(0x005A5A5A);
			view_tag3.setBackgroundColor(0x005A5A5A);
						
			break;
		case 1://选中第2个
			tv_tag1.setTextColor(0xFF9A9A9A);
			tv_tag2.setTextColor(0xFF5A5A5A);
			tv_tag3.setTextColor(0xFF9A9A9A);
			
			view_tag1.setBackgroundColor(0x005A5A5A);
			view_tag2.setBackgroundColor(0xFF5A5A5A);
			view_tag3.setBackgroundColor(0x005A5A5A);
			
			break;
		case 2://选中第3个
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

		Log.i("lost", "ok");
		// 清空原有内容
		listItems.clear();
		Map<String, Object> map = null;

		for (int i = 0; i < MainActivity.lostfindlist.size(); i++)
		{
			map = new HashMap<String, Object>();
			String title = MainActivity.lostfindlist.get(i).getTitle();
			map.put("title", title); // 物品标题
			map.put("image", R.drawable.news_1); // 图片资源, 暂时先使用同一个图片作为新闻
			map.put("time", MainActivity.lostfindlist.get(i).getCreateTime());
			map.put("statusShow", MainActivity.lostfindlist.get(i)
					.getStatusShow());
			map.put("id", MainActivity.lostfindlist.get(i).getId());
			map.put("showtype", MainActivity.lostfindlist.get(i).getTypeShow());

			List<attArryData> attArry = MainActivity.lostfindlist.get(i)
					.getAttArry();
			String http = "";
			String http2 = "";
			String http1 = "";
			try
			{
				http1 = attArry.get(0).getFilePath().toString().trim();
				http2 = MainActivity.lostfindlist.get(i).getAttachmentPrefix();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			http = http2 + http1;

			map.put("http", http);
			listItems.add(map);

		}

		if (null == adpater)
		{
			adpater = new LostFindListAdapter(ctx, listItems); // 创建适配器
			lv_lost.setAdapter(adpater);
		} else
		{
			adpater.refresh(listItems);
		}

	}

	// 在这里封装从后台一个获取失物招领列表的方法
	private void getLostFindList(final int type)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
				// 试试get请求
				String res = null;
				Log.i("请求的类型是", "类型是" + type);

				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");

				try
				{
					res = OkHttpUtils.getInstance().doGet(

							ctx,
							"/admin/lostfound/grid.do?pageNo=1&pageSize="
									+ pageSize + "&type="

									+ type + "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取失败，请重试";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求失物招领：请求返回的结果是：" + res.trim());
				// ArrayList<String>
				// filePathList=MainActivity.lostfindlist.get(1).getfilePathList();
				// System.out.println(MainActivity.lostfindlist.get(1));
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				// 尝试将json串转化成bean对象
				LostFindListBean bean = JSON.parseObject(res,
						LostFindListBean.class);
				// System.out.println("xxxx:" + bean.getApp_result_key());

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取失败,请重试";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = bean.getApp_result_key();

				if ("0".equals(app_result_key))
				{
					// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
					List<LostFind> list = bean.getData().getRows();
					// 将这些新闻对象存储到sp中
					MainActivity.lostfindlist = list;
					// 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				} else
				{
					toastMsg = "获取内容失败,请重试";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}
			}
		}.start();
	}

	// 搜索功能 列表页的搜索功能
	private void search()
	{

		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
		// 试试get请求
		// String res = null;
		HashMap<String, String> map1 = new HashMap<String, String>();

		// 从sharePreference中取出之前存储的参数
		String token = (String) SPUtils.get(ctx, "token", "");
		String singnature = (String) SPUtils.get(ctx, "singnature", "");
		String st = (String) SPUtils.get(ctx, "st", "");
		searchKeys = et_search.getText().toString().trim();

		Log.i("pageSearchKey is", searchKeys);

		map1.put("pageSearchKeys", searchKeys);
		String http = "/admin/lostfound/grid.do?pageNo=1&pageSize" + pageSize
				+ "&type=" + type + "&token=" + token + "&singnature="
				+ singnature + "&st=" + st;
		OkHttpUtils.getInstance().doPostAsync(ctx, http, map1, handler);
	}

	// 在这里封装从后台一个获取内容的方法
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
							// 跳转到内容页
							ctx.jumpToClickById(Const.FRAGMENT_LOSTFINDCONTENT_ID);
							System.out.println("content的内容是" + res.trim());
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

				// Log.i("token的值", token);

				// Log.d("content", res.trim());

			}
		}.start();

	}

	/**
	 * 以下是上拉加载，下拉刷新相关代码
	 */
	@Override
	public void onRefresh()// 这是刷新
	{
		if (is_search == true)
		{
			search();
		} else
		{
			getLostFindList(type);
		}
	}

	@Override
	public void onLoadMore()// 这是加载更多
	{

		pageSize += sizeStep;
		if (is_search == true)
		{
			search();
		} else
		{
			getLostFindList(type);
		}

	}

}
