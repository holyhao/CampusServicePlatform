/**
 * FirstPageFregment.java[v 1.0.0]
 * class:com.mydream.fragment.freg,FirstPageFregment
 * 鍛ㄨ埅 create at 2016-3-22 涓嬪崍7:40:18
 */
package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
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
import com.bdyjy.adapter.ListViewAdapter;
import com.bdyjy.adapter.NanyanNewsListViewAdapter;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.News;
import com.bdyjy.entity.NewsContentQueryResultBean;
import com.bdyjy.entity.NewsQueryResultBean;
import com.bdyjy.entity.news.NewsDetail;
import com.bdyjy.entity.news.NewsDetailRes;
import com.bdyjy.entity.news.NewsItem;
import com.bdyjy.entity.news.NewsList;
import com.bdyjy.util.LogUtil;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

/**
 * 首页fregment
 * 
 * @author 周航<br/>
 *         create at 2016-3-22 下午7:40:18
 */
@SuppressLint("NewApi")
public class NewsListFragment extends Fragment implements IXListViewListener
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
					listView1.onLoad();
					listView2.onLoad();
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
	// private ListView listView1;

	private XListView listView1;
	
	private XListView listView2;

	private ListViewAdapter listViewAdapter;//北大新闻adapter
	private List<Map<String, Object>> listItems;

	private TextView tv_news_type1;
	private TextView tv_news_type2;
	private static int type = 1;// 当前新闻类别值
	private int pageSize = 5;// 页面新闻条数-容量
	private int sizeStep = 5;// 每次加载的数目

	private NanyanNewsListViewAdapter nanyan_listViewAdapter;// 南燕要闻adapter
	private static List<NewsItem> nanyanNewsList;// 南燕要闻特别list

	// 3种类型的新闻列表的切换
	private LinearLayout ll_news_type1, ll_news_type2, ll_news_type3;

	public NewsListFragment(MainActivity ctx)
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
		View view = inflater.inflate(R.layout.newslist_fragment, null);

		view.findViewById(R.id.ll_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						ctx.jumpToFirstPageFregment();
					}
				});

		listView1 = (XListView) view.findViewById(R.id.newslist1);
		listView1.setPullLoadEnable(true);
		listView1.setXListViewListener(this);
		
		listView2=(XListView) view.findViewById(R.id.newslist2);
		listView2.setPullLoadEnable(true);
		listView2.setXListViewListener(this);
		
		listItems = new ArrayList<Map<String, Object>>();
		
		listView2.setOnItemClickListener(new OnItemClickListener()  //点击列表项的事件
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 设置选项的点击事件
				TextView tv = (TextView) view.findViewById(R.id.tv_news_id);
				String sid = tv.getText().toString();
				// 根据这个新闻id，来查询具体的新闻内容，显示出来
				// 查询后台

					getNewsContent(sid);

			}
		});

		

		// 选项的点击事件
		listView1.setOnItemClickListener(new OnItemClickListener()  //点击列表项的事件
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 设置选项的点击事件
				TextView tv = (TextView) view.findViewById(R.id.tv_news_id);
				String sid = tv.getText().toString();
				// 根据这个新闻id，来查询具体的新闻内容，显示出来
				// 查询后台
					System.out.println("现在查询南燕要闻的内容");
					getNanyanNewsContent(sid);
	

			}
		});

		initTab(view);
		// TODO 那就
		if (type == 1)
		{
					if (null == nanyanNewsList){
						tv_news_type1.setTextColor(this.getResources().getColor(R.color.BLACK_TITLE));
						getNanyanNews();
					}
					else{
						handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
					}
		} else
		{
			if (null == MainActivity.newsList)
			{
				listView1.setVisibility(View.GONE);
				listView2.setVisibility(View.VISIBLE);
				tv_news_type1.setTextColor(ctx.getResources().getColor(R.color.GRAY_TITLE));
				tv_news_type2.setTextColor(ctx.getResources().getColor(R.color.BLACK_TITLE));
				ll_news_type2.performClick();
			
			} else
			{
				listView1.setVisibility(View.GONE);
				listView2.setVisibility(View.VISIBLE);
				tv_news_type1.setTextColor(ctx.getResources().getColor(R.color.GRAY_TITLE));
				tv_news_type2.setTextColor(ctx.getResources().getColor(R.color.BLACK_TITLE));
				
				// 仅改变视图
				ll_news_type2.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg));
				ll_news_type1.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg));
				ll_news_type3.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg));

				handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
			}

		}

		//loadData();

		return view;
	}

	/**
	 * 初始化选项卡
	 * 
	 * @param view
	 */
	private void initTab(View view)
	{
		//新闻标题
		tv_news_type1=(TextView)view.findViewById(R.id.tv_news_type1);
		tv_news_type2=(TextView)view.findViewById(R.id.tv_news_type2);
		
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
				listView2.setVisibility(View.GONE);
				listView1.setVisibility(View.VISIBLE);
				tv_news_type1.setTextColor(ctx.getResources().getColor(R.color.BLACK_TITLE));
				tv_news_type2.setTextColor(ctx.getResources().getColor(R.color.GRAY_TITLE));
				// 并且将当前选中的项字体加粗变色，其他的回归原style
				ll_news_type1.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg));
				ll_news_type2.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg));
				ll_news_type3.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg));

				pageSize = 5;
				type = 1;
//				listViewAdapter.notifyDataSetChanged();
//				nanyan_listViewAdapter.notifyDataSetChanged();
				//getNewsList(type);
				getNanyanNews();// 调用南燕要闻特别方法
			}
		});

		ll_news_type2.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				listView1.setVisibility(View.GONE);
				listView2.setVisibility(View.VISIBLE);
				tv_news_type1.setTextColor(ctx.getResources().getColor(R.color.GRAY_TITLE));
				tv_news_type2.setTextColor(ctx.getResources().getColor(R.color.BLACK_TITLE));
				// 并且将当前选中的项字体加粗变色，其他的回归原style
				ll_news_type2.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg));
				ll_news_type1.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg));
				ll_news_type3.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg));

				pageSize = 5;
				type = 2;
				getNewsList(type);
				// loadData();

			}
		});

		ll_news_type3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style
				ll_news_type3.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg));
				ll_news_type2.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg));
				ll_news_type1.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg));

				pageSize = 5;
				type = 3;
				getNewsList(type);
				// loadData();

			}
		});
	}

	private static final String[] URLS =
	{
			"http://h.hiphotos.baidu.com/image/pic/item/faedab64034f78f0b00507c97e310a55b3191cf9.jpg",
			"http://www.pkusz.edu.cn/uploadfile/2016/0427/20160427091614202.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=fcae01763b87e9504217f3642039531b/2f2eb9389b504fc29fccbeb0e4dde71191ef6df7.jpg",
			"http://f.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=496abfc08db1cb133e3c3415ed647a76/b7003af33a87e95048e8274310385343fbf2b426.jpg",
			"http://d.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=603e37439313b07ebde8580c39e7bd15/a8014c086e061d9591b7875a7bf40ad163d9cadb.jpg", };

	private void loadData()
	{
		// 清空原有内容
		listItems.clear();
		Map<String, Object> map = null;

		if (type == 1)// 如果type是1，就说明是南燕要闻，调用南亚要闻的特别方法
		{
			if (null != nanyanNewsList)
			{
				for (int i = 0; i < nanyanNewsList.size(); i++)
				{
					map = new HashMap<String, Object>();
					String thumb = nanyanNewsList.get(i).getThumb();
					// http://img0.imgtn.bdimg.com/it/u=1619454554,2969607734&fm=21&gp=0.jpg
					// if(TextUtils.isEmpty(thumb)){
					// thumb =
					// "http://img0.imgtn.bdimg.com/it/u=1619454554,2969607734&fm=21&gp=0.jpg";
					// }
					map.put("thumb", thumb); //

					// 以下是使用测试图片，本地写死的
					// String thumb = "";
					// if (i >= 5)
					// thumb = URLS[i % 5];
					// else
					// thumb = URLS[i];
					// map.put("thumb", thumb);
					map.put("title", nanyanNewsList.get(i).getTitle()); // 物品标题
					map.put("news_id", nanyanNewsList.get(i).getId());
					map.put("sourcefrom", nanyanNewsList.get(i).getSourcefrom());
					map.put("date", nanyanNewsList.get(i).getDate());

					listItems.add(map);
				}
			}

			 if (null == nanyan_listViewAdapter)
			 {
			nanyan_listViewAdapter = new NanyanNewsListViewAdapter(ctx,
					listItems, listView1); // 创建适配器
			listView1.setAdapter(nanyan_listViewAdapter);
			 } else
			 {
			 nanyan_listViewAdapter.refresh(listItems);
			 }
		} else
		{
			if (null != MainActivity.newsList)
			{
				for (int i = 0; i < MainActivity.newsList.size(); i++)
				{

					System.out.println("bbbb:" + MainActivity.newsList.get(i));
					map = new HashMap<String, Object>();
					map.put("image", MainActivity.newsList.get(i).getLogo()); // 图片资源,
																				// 暂时先使用同一个图片作为新闻
					map.put("date", MainActivity.newsList.get(i)
							.getCreateTime()); //
					map.put("title", MainActivity.newsList.get(i).getTitle()); // 物品标题
					map.put("news_id", MainActivity.newsList.get(i).getId());
					map.put("pubFrom", MainActivity.newsList.get(i)
							.getPubFrom());
					listItems.add(map);
				}
			}

			 if (null == listViewAdapter)
			 {
			listViewAdapter = new ListViewAdapter(ctx, listItems); // 创建适配器
			listView2.setAdapter(listViewAdapter);
			 } else
			 {
			 listViewAdapter.refresh(listItems);
			 }
		}

	}

	// 在这里封装从后台一个获取新闻列表的方法
	private void getNewsList(final int type)
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
							"/admin/news/app/grid.do?pageNo=1&pageSize="
									+ pageSize + "&type=" + type + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取新闻内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求新闻：请求返回的结果是：" + res.trim());
				// 尝试将json串转化成bean对象
				NewsQueryResultBean nqrb = JSON.parseObject(res,
						NewsQueryResultBean.class);
				// System.out.println("xxxx:" + nqrb.getApp_result_key());

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取新闻内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = nqrb.getApp_result_key();
				if ("0".equals(app_result_key))
				{
					// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
					List<News> list = nqrb.getData().getRows();
					for (News n : list)
					{
						System.out.println("北大新闻的ID"+n.getId());;
					}
					// 将这些新闻对象存储到sp中
					MainActivity.newsList = list;
					// 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}
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
				}
				ctx.jumpToNewsContentFregment();
			}
		}.start();
	}

	/**
	 * 获取“南燕要闻”，tab1，使用特别方法
	 */
	private void getNanyanNews()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				String res = null;
				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().Get(
							"http://www.pkusz.edu.cn/api/get_news.php?pageSize="
									+ pageSize);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取新闻内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求新闻：请求返回的结果是：" + res.trim());
				// 尝试将json串转化成bean对象
				NewsList nl = JSON.parseObject(res, NewsList.class);
                
				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取新闻内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String code = nl.getCode();
				if ("OK".equals(code))
				{
					// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
					nanyanNewsList = nl.getList();					
					for (NewsItem n : nanyanNewsList)
					{
						System.out.println("南燕要闻的ID：" + n.getId());
					}
					// // 将这些新闻对象存储到sp中
					// MainActivity.newsList = list;
					// 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}
			}
		}.start();
	}

	// 在这里封装从后台一个获取新闻内容的方法
	private void getNanyanNewsContent(final String id)
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
					res = OkHttpUtils.getInstance()
							.Get("http://www.pkusz.edu.cn/api/get_news.php?id="
									+ id);					
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
				}

				System.out.println("新闻内容res：" + res);
				NewsDetailRes bean = JSON.parseObject(res, NewsDetailRes.class);
                
				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取新闻内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				String app_result_key = bean.getCode();
				if ("OK".equals(app_result_key))// 如果正常获得了新闻的内容,
												// 那么我要在这里取得新闻的标题，内容，显示在界面上
				{
					NewsDetail b = bean.getDetail();
					MainActivity.news = null;
					MainActivity.newsDetail = b;
				}
				ctx.jumpToNewsContentFregment();
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
		pageSize = 5;
		// 刷新数据
		if (type == 1)
		{
			getNanyanNews();
		} else
		{
			getNewsList(type);
		}

	}

	@Override
	public void onLoadMore()// 这是加载更多
	{
		// toastMsg = "检测到上拉加载动作";
		// handler.sendEmptyMessage(HandlerOrder.TOAST);
		pageSize += sizeStep;
		if (type == 1)
		{
			getNanyanNews();
		} else
		{
			getNewsList(type);
		}
	}
}
