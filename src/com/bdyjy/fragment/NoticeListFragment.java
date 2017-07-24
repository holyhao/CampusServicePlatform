package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.alibaba.fastjson.JSON;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;

import com.bdyjy.adapter.NoticeListViewAdapter;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.notice.Notice;
import com.bdyjy.entity.notice.NoticeContent;
import com.bdyjy.entity.notice.NoticeContentById;
import com.bdyjy.entity.notice.NoticeResultBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

/**
 * 教务通知列表fragment
 * 
 * @author cuicui create at 2016-04-04 12:10
 * 
 */
public class NoticeListFragment extends Fragment implements IXListViewListener
{
	Handler handler = null;
	String toastMsg = null;
	private EditText et_search; // 搜索状态下的输入框
	private TextView tv_search_cancel;// 关闭搜索功能
	private RelativeLayout rl_searchbar_off; // 关闭状态下的输入框
	private RelativeLayout rl_searchbar_on;// 工作状态下的输入框

	private MainActivity ctx;

	// 自定义教务通知列表相关
	private XListView noticeListView;
	private NoticeListViewAdapter noticeListViewAdapter;
	private List<Map<String, Object>> noticeListItems;

	private TextView tv_notice_back;
	private int pageSize = 5;// 页面教务通知条数-容量
	private int sizeStep = 5;// 每次加载的数目

	public NoticeListFragment(MainActivity ctx)
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
					try
					{
						loadData();
					} catch (JSONException e)
					{
						e.printStackTrace();
					}
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.noticelist_fragment, null);

		tv_notice_back = (TextView) view.findViewById(R.id.tv_notice_back);
		tv_notice_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToFirstPageFregment();
			}
		});

		view.findViewById(R.id.ll_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						ctx.jumpToNoticeListFragment();
					}
				});

		noticeListView = (XListView) view.findViewById(R.id.notice_listview);
		noticeListView.setPullLoadEnable(true);
		noticeListView.setXListViewListener(this);
		noticeListItems = new ArrayList<Map<String, Object>>();

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
				// Toast.makeText(ctx, "搜索功能，尚未完成.", Toast.LENGTH_SHORT).show();

				searchNoticeList();
				return false;
			}
		});

		// 选项的点击事件
		noticeListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 设置选项的点击事件
				TextView tv = (TextView) view.findViewById(R.id.tv_notice_id);
				// 根据这个教务通知id，来查询具体的教务通知内容，显示出来
				// 查询后台
				getNoticeContent(tv.getText().toString());
			}
		});

		if (null == MainActivity.noticeList)
			getNoticeList();
		else{
			try
			{
				loadData();
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	

		return view;
	}

	private void loadData() throws JSONException
	{
		// 清空原有内容
		noticeListItems.clear();
		Map<String, Object> map = null;

		if (MainActivity.noticeList == null)
		{
			System.out.println("教务通知noticeList未获取");
		}

		// 这里是对应的第一种JSON解析形式
		// for (int i = 0; i < MainActivity.noticeList.length(); i++) {
		//
		// JSONObject listObj = MainActivity.noticeList.getJSONObject(i);
		// map = new HashMap<String, Object>();
		// map.put("thumb", R.drawable.news_1); // 图片资源,
		// // 暂时先使用同一个图片作为教务通知
		// map.put("title", listObj.getString("title")); // 物品标题
		// map.put("id", listObj.getString("id"));
		// map.put("sourcefrom", listObj.getString("sourcefrom")); // 物品标题
		//
		// map.put("date", listObj.getString("date"));
		// noticeListItems.add(map);
		//
		// }

		// 这里是对应的第二种JSON解析形式
		for (int i = 0; i < MainActivity.noticeList.size(); i++)
		{

			map = new HashMap<String, Object>();
			map.put("title", MainActivity.noticeList.get(i).getTitle()); // 物品标题
			map.put("id", MainActivity.noticeList.get(i).getId());
			map.put("sourcefrom", MainActivity.noticeList.get(i)
					.getSourcefrom()); // 物品标题

			map.put("date", MainActivity.noticeList.get(i).getDate());

			// List<attArryData> attArry =
			// MainActivity.noticeList.get(i).getAttArry();
			// String http = "";
			// String http2 = "";
			// String http1 = "";
			// try {
			// http1 = attArry.get(0).getFilePath().toString().trim();
			// http2 = MainActivity.secondMarket.get(i).getAttachmentPrefix();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// http = http2 + http1;
			//
			// map.put("http", http);
			noticeListItems.add(map);
		}

		if (null == noticeListViewAdapter)
		{
			noticeListViewAdapter = new NoticeListViewAdapter(ctx,
					noticeListItems); // 创建适配器
			noticeListView.setAdapter(noticeListViewAdapter);
		} else
		{
			noticeListViewAdapter.refresh(noticeListItems);
		}
	}

	// 搜索功能
	private void searchNoticeList()
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
				String searchtitle = et_search.getText().toString().trim();

				// 搜索有问题
				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().Get(

					"http://portal.pkusz.edu.cn/api/get_jiaowu.php?pageNo=1&pageSize=30"

					+ "&searchtitle=" + searchtitle);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("搜索教务通知：请求返回的结果是：" + res.trim());

				if (res.trim().equals("error"))
				{
					toastMsg = "获取内容失败，请重试;res=" + res.trim();
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				// 尝试将json串转化成bean对象
				Notice bean = JSON.parseObject(res, Notice.class);
				String code = bean.getCode();

				if ("OK".equals(code))
				{
					// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
					List<NoticeContent> list = bean.getList();
					// 将这些新闻对象存储到sp中
					MainActivity.noticeList = list;
					// 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW); // 接收消息成功
																			// 临时设置
				} else
				{
					toastMsg = "获取内容失败，请稍后重试...";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
			}
		}.start();
	}

	// 在这里封装从后台一个获取教务通知列表的方法
	private void getNoticeList()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				// 试试get请求
				String res = null;

				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().Get(

					"http://portal.pkusz.edu.cn/api/get_jiaowu.php");
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取教务通知内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				System.out.println("请求教务通知：请求返回的结果是：" + res.trim());

				// 第一种方法：尝试将json串依次解析成JSONObject，JSONArray，JSONObject

				// try {
				// JSONObject obj = new JSONObject(res.trim());
				// JSONArray list = obj.getJSONArray("list");
				// MainActivity.noticeList = list;
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				// 第二种方法：将JSON解析成bean对象。
				Notice noticeObj = JSON.parseObject(res, Notice.class);

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取教务通知内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}

				List<NoticeContent> list = new ArrayList<NoticeContent>();
				list = noticeObj.getList();

				// 将这些教务通知对象存储到sp中
				MainActivity.noticeList = list;
				// 使用handler去通知主线程更新noticeListView
				handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				// }
			}
		}.start();
	}

	// 在这里封装从后台一个获取教务通知内容的方法
	private void getNoticeContent(final String id)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				// 试试get请求
				String res = null;

				try
				{
					res = OkHttpUtils.getInstance().Get(
							"http://portal.pkusz.edu.cn/api/get_jiaowu.php?id="
									+ id);

				} catch (Exception e)
				{
					e.printStackTrace();
				}

				System.out.println("请求教务通知详情：请求返回的结果是：" + res.trim());
				NoticeResultBean bean = JSON.parseObject(res,
						NoticeResultBean.class);

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取教务通知内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String code = bean.getCode();

				if ("OK".equals(code)) // 如果正常获得了教务通知的内容,
				{
					NoticeContentById b = bean.getDetail();// 那么我要在这里取得教务通知的标题，内容，显示在界面上
					MainActivity.notice = b;
				}
				ctx.jumpToNoticeContentFragment();
			}
		}.start();
	}

	@Override
	public void onRefresh()
	{
		// TODO Auto-generated method stub
		getNoticeList();

	}

	@Override
	public void onLoadMore()
	{
		// TODO Auto-generated method stub
		pageSize += sizeStep;
		getNoticeList();

	}

}
