package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.adapter.LectureListViewAdapter;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.lecture.Lecture;
import com.bdyjy.entity.lecture.LectureContentQueryResultBean;
import com.bdyjy.entity.lecture.LectureQueryResultBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 讲座信息列表Fragment
 * 
 * @author cuicui create at 2016-03-31 23:42
 * 
 */
public class LectureListFragment extends Fragment implements IXListViewListener
{

	private MainActivity ctx;

	// 自定义新闻列表相关
	private XListView lectureListView;
	private LectureListViewAdapter lectureListViewAdapter;
	private List<Map<String, Object>> lectureListItems;

	private TextView tv_back;

	private int pageSize = 5;// 页面讲座条数-容量
	private int sizeStep = 5;// 每次加载的数目

	// 2种类型的讲座信息列表的切换
	private LinearLayout lecture_academic_layout, lecture_forum_layout;

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
					lectureListView.onLoad();
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

	// 构造函数
	public LectureListFragment(MainActivity ctx)
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
		View view = inflater.inflate(R.layout.lecturelist_fragment, null);

		tv_back = (TextView) view.findViewById(R.id.tv_back);
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
						ctx.jumpToFirstPageFregment();
					}
				});

		lectureListView = (XListView) view.findViewById(R.id.lecture_listview);
		lectureListView.setPullLoadEnable(true);
		lectureListView.setXListViewListener(this);
		lectureListItems = new ArrayList<Map<String, Object>>();

		// 选项的点击事件
		lectureListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 设置选项的点击事件
				TextView tv = (TextView) view.findViewById(R.id.tv_lecture_id);
				// 根据这个讲座信息id，来查询具体的讲座信息内容，显示出来
				// 查询后台
				getLectureContent(tv.getText().toString());
				// System.out.println("请求具体讲座内容" + tv.getText().toString());
			}
		});

		initTab(view);

		if (null == MainActivity.lectureList)
			getLectureList();
		else
			loadData();

		return view;
	}

	/**
	 * 初始化选项卡
	 * 
	 * @param view
	 */
	@SuppressLint("NewApi")
	private void initTab(View view)
	{
		// 讲座信息类型切换
		lecture_academic_layout = (LinearLayout) view
				.findViewById(R.id.lecture_academic_layout);
		lecture_forum_layout = (LinearLayout) view
				.findViewById(R.id.lecture_forum_layout);

		// 每次切换，都访问后台，然后加载到列表中
		lecture_academic_layout.setOnClickListener(new OnClickListener()
		{
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style
				lecture_academic_layout.setBackground(getResources()
						.getDrawable(R.drawable.selected_item_bg));
				lecture_forum_layout.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg));
				pageSize = 5;
				getLectureList();
				// loadData();

			}
		});

		lecture_forum_layout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style
				lecture_forum_layout.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg));
				lecture_academic_layout.setBackground(getResources()
						.getDrawable(R.drawable.not_selected_item_bg));
				pageSize = 5;
				getLectureList();
			}
		});
	}

	private void loadData()
	{
		// 清空原有内容
		lectureListItems.clear();
		Map<String, Object> map = null;
		if (MainActivity.lectureList == null)
		{
			System.out.println("未获取到lecture数据");
		} else
		{
			for (int i = 0; i < MainActivity.lectureList.size(); i++)
			{
				map = new HashMap<String, Object>();
				map.put("poster", R.drawable.news_1); // 图片资源,
														// 暂时先使用同一个图片作为新闻
				map.put("title", MainActivity.lectureList.get(i).getTitle()); // 物品标题
				map.put("lecTime", MainActivity.lectureList.get(i).getLecTime());
				map.put("lecPlace", MainActivity.lectureList.get(i)
						.getLecPlace());
				map.put("speaker", MainActivity.lectureList.get(i).getSpeaker());
				map.put("content", MainActivity.lectureList.get(i).getContent());
				map.put("tv_lecture_id", MainActivity.lectureList.get(i)
						.getId());
				String http = "";
				String http2 = "";
				String http1 = "";
				try
				{
					http1 = MainActivity.lectureList.get(i).getPoster()
							.toString().trim();

					http2 = MainActivity.lectureList.get(i)
							.getAttachmentPrefix();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				http = http2 + http1;
				System.out.println("讲座信息图片的地址为：" + http);

				map.put("http", http);

				lectureListItems.add(map);
			}

		}

		if (null == lectureListViewAdapter)
		{
			lectureListViewAdapter = new LectureListViewAdapter(ctx,
					lectureListItems); // 创建适配器
			lectureListView.setAdapter(lectureListViewAdapter);
		} else
		{
			lectureListViewAdapter.refresh(lectureListItems);
		}
	}

	// 在这里封装从后台一个获取讲座信息列表的方法
	private void getLectureList()
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
							.doGet(ctx,
									"/admin/lecture/grid.do?pageNo=1&pageSize="
											+ pageSize + "&token=" + token
											+ "&singnature=" + singnature
											+ "&st=" + st);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取讲座信息失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				} finally
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				}
				 System.out.println("LectureListFragment请求讲座信息：请求返回的结果是：" +
				 res.trim());

				// 尝试将json串转化成bean对象
				LectureQueryResultBean nqrb = JSON.parseObject(res,
						LectureQueryResultBean.class);
				// System.out.println("nqrb.getApp_result_key()xxxx00:" +
				// nqrb.getApp_result_key());

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取讲座信息失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = nqrb.getApp_result_key();
				if ("0".equals(app_result_key))
				{
					// 到了这里说明正常返回了数据，我要在这里获取讲座信息的标题
					List<Lecture> lectureList = nqrb.getData().getRows();
					// 将这些新闻信息对象存储到sp中
					MainActivity.lectureList = lectureList;
					// 使用handler去通知主线程更新lectureListView
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}
			}
		}.start();

	}

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

				// System.out.println("请求具体讲座内容" + res.trim());

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
					MainActivity.lecture = b;
				}
				ctx.jumpToLectureContentFragment();
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
		pageSize = 5;
		getLectureList();
	}

	@Override
	public void onLoadMore()// 这是加载更多
	{
		// toastMsg = "检测到上拉加载动作";
		// handler.sendEmptyMessage(HandlerOrder.TOAST);
		pageSize += sizeStep;
		getLectureList();
	}

}
