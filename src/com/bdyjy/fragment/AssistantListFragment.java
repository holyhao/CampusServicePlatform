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
import com.bdyjy.adapter.AssistantAdapter;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.constants.ParleConstant;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.assistant.AssistantContentQueryResultBean;
import com.bdyjy.entity.assistant.AssistantNewBean;
import com.bdyjy.entity.assistant.AssistantQueryResultBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

/**
 * 勤工俭学列表 Fragment
 * 
 * @author parle
 * 
 */
public class AssistantListFragment extends Fragment implements
		IXListViewListener
{
	Handler handler = null;
	String toastMsg = null;
	final int type = 1;
	final int TIME_LENGTH = 10;

	private int pageSize = 5;// 加载的列表数目
	private int sizeStep = 5;// 每次加载的数目

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
				case HandlerOrder.POST_OK: // 关键字提交成功 异步获取用于 搜索框功能
					String res;
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					res = msg.getData().getString("body");
						
					AssistantQueryResultBean bean = JSON.parseObject(res,
							AssistantQueryResultBean.class);
					
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))
					{
						
						List<AssistantNewBean> list = bean.getData()
								.getRows();
						MainActivity.assistantNewsList = list;
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

	private MainActivity ctx;

	// 自定义新闻列表相关
	private XListView listView;
	private AssistantAdapter assistantAdapter;
	private List<Map<String, Object>> listItems;

	private TextView tv_back;
	private EditText et_search;
	private RelativeLayout rl_search_off ;
	private RelativeLayout rl_search_on;
	private TextView tv_search_cancel;
	private String searchKeys;// 搜索关键字
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
	
	
	
	public AssistantListFragment(MainActivity ctx)
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
		ctx.jumpToFirstPageFregment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.assistant_list_fragment, null);
		tv_back = (TextView) view.findViewById(R.id.assistant_list_tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToFirstPageFregment();
			}
		});

		listView = (XListView) view.findViewById(R.id.assistant_listview);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setHeaderDividersEnabled(false); 

		listItems = new ArrayList<Map<String, Object>>();

		// 选项的点击事件
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 设置选项的点击事件
				TextView tv = (TextView) view
						.findViewById(R.id.assistant_list_item_id);
				// 根据这个新闻id，来查询具体的新闻内容，显示出来

				// 查询后台
				getNewsContent(tv.getText().toString());
			}
		});

		// 搜索功能view
		rl_search_off = (RelativeLayout) view
				.findViewById(R.id.assistant_searchbar_off);
		rl_search_on = (RelativeLayout) view
				.findViewById(R.id.assistant_searchbar_on);
		et_search = (EditText) view.findViewById(R.id.assistant_et_search);
		tv_search_cancel = (TextView) view
				.findViewById(R.id.assistant_tv_search_cancel);

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
		// tv_search_cancel = (TextView)
		// view.findViewById(R.id.assistant_tv_search_cancel);
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

		// 搜索框输入监听，后台请求
		//et_search = (EditText) view.findViewById(R.id.et_search);
		et_search.addTextChangedListener(textWatcher);
		et_search.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event)
			{

				if ((actionId == 0 || actionId == 3) && event != null)
				{
					Toast.makeText(ctx, "正在搜索，请稍等", Toast.LENGTH_SHORT).show();
					// 点击搜索，关闭键盘
					InputMethodManager inputManager = (InputMethodManager) et_search
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(ctx.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					
					
					// 后台请求搜索
					search();//*************************************
				}
				return false;
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
		

		if (null == MainActivity.assistantNewsList)
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

		if (null != MainActivity.assistantNewsList)
		{
			for (int i = 0; i < MainActivity.assistantNewsList.size(); i++)
			{
				map = new HashMap<String, Object>();

				map.put("job", MainActivity.assistantNewsList.get(i).getTitle());
				map.put("pubFrom", MainActivity.assistantNewsList.get(i)
						.getSector());
				map.put("number", MainActivity.assistantNewsList.get(i)
						.getAsiNum());
				map.put("time", MainActivity.assistantNewsList.get(i)
						.getCreateTime().substring(0, TIME_LENGTH));
				map.put("id", MainActivity.assistantNewsList.get(i).getId());
				listItems.add(map);
			}
		}

		if (null == assistantAdapter)
		{
			assistantAdapter = new AssistantAdapter(ctx, listItems); // 创建适配器
			listView.setAdapter(assistantAdapter);
		} else
		{
			assistantAdapter.refresh(listItems);
		}
	}

	/**
	 * 在这里封装从后台一个获取兼职新闻列表的方法
	 * 
	 */
	private void getNewsList()
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
					res = OkHttpUtils.getInstance()
							.doGet(ctx,
									"/admin/asistance/grid.do?pageNo=1&pageSize="+ pageSize 
											+ "&token=" + token
											+ "&singnature=" + singnature
											+ "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取勤工助学列表失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}

				if (res.trim().length() == 0)
				{
					toastMsg = "获取勤工助学列表失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求新闻：请求返回的结果是：" + res.trim());
				Log.d("ATAG", res.trim());

				// 尝试将json串转化成bean对象
				AssistantQueryResultBean assistantQRB = JSON.parseObject(res,
						AssistantQueryResultBean.class);

				// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
				String app_result_key = assistantQRB.getApp_result_key();
				System.out.println("******xxxx:" + app_result_key);

				if ("0".equals(app_result_key))
				{
					List<AssistantNewBean> list = assistantQRB.getData()
							.getRows();
					for (AssistantNewBean n : list)
					{
						System.out.println(n.getTitle());
					}
					MainActivity.assistantNewsList = list;
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}
				handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);

			}
		};

		thread.start();
	}

	/**
	 * 在这里封装从后台一个获取新闻内容的方法
	 * 
	 * @param id
	 */
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
				}
				ctx.jumpToClickById(ParleConstant.JUMP_ASSISTANT_CONTENT);
			}
		}.start();
	}
	
	
	// 搜索功能 列表页的搜索功能
	private void search() {

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
		String http = "/admin/asistance/grid.do?pageNo=1&pageSize" + pageSize
				+ "&type=" + type + "&token=" + token + "&singnature="
				+ singnature + "&st=" + st;
		OkHttpUtils.getInstance().doPostAsync(ctx, http, map1, handler);
	}
	

	/**
	 * 后台请求搜索
	 */

	private void searchNewsList() {

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
				String searchKeys = et_search.getText().toString().trim();

				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(
							ctx,
							"/admin/asistance/grid.do?pageNo=1&pageSize="
									+ pageSize + "&type="

									+ type + "&pageSearchKeys=" + searchKeys
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取勤工助学列表失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}

				if (res.trim().length() == 0)
				{
					toastMsg = "获取勤工助学列表失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求新闻：请求返回的结果是：" + res.trim());

				// 尝试将json串转化成bean对象
				AssistantQueryResultBean assistantQRB = JSON.parseObject(res,
						AssistantQueryResultBean.class);

				// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
				String app_result_key = assistantQRB.getApp_result_key();
				System.out.println("******xxxx:" + app_result_key);

				if ("0".equals(app_result_key))
				{
					List<AssistantNewBean> list = assistantQRB.getData()
							.getRows();
					if (list.size() == 0)
					{
						toastMsg = "没找到您要的数据";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
					}
					for (AssistantNewBean n : list)
					{
						System.out.println(n.getTitle());
					}
					MainActivity.assistantNewsList = list;
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}
				handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);

			}
		};

		thread.start();
	}

	@Override
	public void onRefresh()
	{
		// TODO Auto-generated method stub
		getNewsList();
	}

	@Override
	public void onLoadMore()
	{
		// TODO Auto-generated method stub
		pageSize += sizeStep;
		getNewsList();
	}
}
