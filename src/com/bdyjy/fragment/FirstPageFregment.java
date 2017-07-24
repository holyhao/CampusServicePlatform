/**
 * Fragment2.java[v 1.0.0]
 * class:com.mydream.fragment.freg,Fragment2
 * 周航 create at 2016-3-22 下午7:54:01
 */
package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.constants.ParleConstant;
import com.bdyjy.custom.view.ImageCycleView;
import com.bdyjy.custom.view.ImageCycleView.ImageCycleViewListener;
import com.bdyjy.entity.News;
import com.bdyjy.entity.NewsContentQueryResultBean;
import com.bdyjy.entity.NewsQueryResultBean;
import com.bdyjy.entity.activity.Activity;
import com.bdyjy.entity.activity.ActivityContentQueryResultBean;
import com.bdyjy.entity.activity.ActivityQueryResultBean;
import com.bdyjy.entity.lecture.Lecture;
import com.bdyjy.entity.lecture.LectureContentQueryResultBean;
import com.bdyjy.entity.lecture.LectureQueryResultBean;
import com.bdyjy.entity.news.NewsDetail;
import com.bdyjy.entity.news.NewsDetailRes;
import com.bdyjy.fragment.factory.FragmentFactory;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;
import com.loopj.android.image.SmartImageView;

/**
 * com.mydream.fragment.freg.Fragment2
 * 
 * @author 周航<br/>
 *         create at 2016-3-22 下午7:54:01
 */
@SuppressLint("NewApi")
public class FirstPageFregment extends Fragment
{

	private MainActivity ctx;

	private Handler handler;

	private String toastMsg;
	private View viewMain;
	private LayoutInflater inflaterMain;
	private static List<Activity> activityList;
	private static String lect_count;
	private static List<Lecture> lectureList;
	private static String activity_count;
	private ScrollView scl;
	private static int SCLocation;

	private TextView tv_book;

	@SuppressLint("HandlerLeak")
	private void initHandler()
	{
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case HandlerOrder.TOAST:
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.UPDATE_LECTURE_LIST:
					initFirstPageLectureLayout();
					break;
				case HandlerOrder.UPDATE_ACTIVITY_LIST:
					initFirstPageActivitiesLayout();
					break;
				case HandlerOrder.UPDATE_LECTURE_COUNT:

					TextView tv = (TextView) viewMain
							.findViewById(R.id.tv_lect_num);
					tv.setText(lect_count);

					break;
				case HandlerOrder.UPDATE_ACTIVITY_COUNT:

					TextView tv2 = (TextView) viewMain
							.findViewById(R.id.tv_activity_count);
					tv2.setText(activity_count);

					break;
				case HandlerOrder.GET_ROLLING_NEWS://此处获得完整新闻内容
					// TODO
					
					startImageTimerTask();//获取新闻内容之后  开启新闻滚动显示
//                  List<News> list = MainActivity.newsList;
//					StringBuffer nx = new StringBuffer();
//					// 解析list，并且加载到跑马灯中
//					for (News n : list)
//					{
//						nx.append(n.getTitle());
//					}
//					// TODO
//					tv_book.setText(nx.toString());

					break;
				}
			}
		};

	}

	public FirstPageFregment(MainActivity ctx)
	{
		this.ctx = ctx;
		ctx.keydown=ctx.key1;	
		initHandler();
	
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.firstpage_fragment, null);
		viewMain = view;
		inflaterMain = inflater;
		
		
		scl=(ScrollView) view.findViewById(R.id.firstpage_scl);
		
		Button tv = (Button) view.findViewById(R.id.btn_news);
		tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// getNewsList();
				ctx.jumpToNewsListFregment();
			}
		});

		// guocuicui 讲座信息设置监听，跳转
		Button btn_news1 = (Button) view.findViewById(R.id.btn_news1);
		btn_news1.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				ctx.jumpToLectureListFragment();

			}
		});

		// guocuicui 社团活动设置监听，跳转
		Button btn_news2 = (Button) view.findViewById(R.id.btn_news2);
		btn_news2.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				ctx.jumpToActivityListFragment();

			}
		});

		// guocuicui 教务通知设置监听，跳转
		Button btn_news4 = (Button) view.findViewById(R.id.btn_news4);
		btn_news4.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				ctx.jumpToNoticeListFragment();

			}
		});

		Button btn_news3 = (Button) view.findViewById(R.id.btn_news3);
		btn_news3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToVenueBookingFregment();
			}
		});

		// holy 投诉建议设置监听，跳转
		Button btn_new10 = (Button) view.findViewById(R.id.btn_news10);
		btn_new10.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				ctx.jumpToComplaintFregment();

			}
		});

		// holy 校园指南设置监听，跳转
		Button btn_more = (Button) view.findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				ctx.jumpToCampusGuideFragment();
			}
		});

		// songdebin 跳转至维护报修 revised 2016-04-07
		Button btn_fix = (Button) view.findViewById(R.id.btn_news9);
		btn_fix.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				ArrayList list = new ArrayList();
				list.add(Const.FRAGMENT1_ID);
				ctx.jumpToClickWithId(Const.FRAGMENT_MYFIX_ID, list);
			}
		});

		/******************* 裴超 修改 开始 *****************/
		/**
		 * 跳转图书馆 调用此java文件中我实现的方法
		 * 
		 * @author parle
		 */
		Button tv_library = (Button) view.findViewById(R.id.btn_news8);
		tv_library.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getLibrary();
			}
		});

		/**
		 * 跳转勤工俭学 调用此java文件中我实现的方法
		 * 
		 * @author parle
		 */
		Button tv_ptj = (Button) view.findViewById(R.id.btn_news6);
		tv_ptj.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getPartTimeJob();
			}
		});

		/**
		 * 跳转招聘 调用此java文件中我实现的方法
		 * 
		 * @author parle
		 */
		Button tv_recruit = (Button) view.findViewById(R.id.btn_news5);
		tv_recruit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getRecruit();
			}
		});

		Button tv_classroom = (Button) view.findViewById(R.id.btn_news7);
		tv_classroom.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getClassroom();
			}
		});

		/******************* 裴超 修改 结束 *****************/

		// 在这里先进行判断，如果数据变量不为空，就不进行重新获取；
		initCycleView(view);// 初始化轮播图
		if (null == activityList)
			getActivityList();// 初始化活动列表
		else
		{
			handler.sendEmptyMessage(HandlerOrder.UPDATE_ACTIVITY_LIST);
			handler.sendEmptyMessage(HandlerOrder.UPDATE_ACTIVITY_COUNT);
		}

		if (null == lectureList)
			getLectureList();// 初始化讲座列表
		else
		{
			handler.sendEmptyMessage(HandlerOrder.UPDATE_LECTURE_LIST);
			handler.sendEmptyMessage(HandlerOrder.UPDATE_LECTURE_COUNT);
		}

		// 顶部的点击事件，刷新首页
		view.findViewById(R.id.iv_top).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivityList();
				// getLectureList();
			}
		});

		getRollingNews();//获取滚动新闻
		tv_book = (TextView) view.findViewById(R.id.tv_book);
		tv_book.setMovementMethod(ScrollingMovementMethod.getInstance());
		tv_book.setFocusable(true);
		tv_book.requestFocus();
		tv_book.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{				 
				getNewsContent(NewsId);
			}
		}									
		);	
		Timer timer = new Timer();

		timer.schedule(new TimerTask()
		{
			public void run()

			{
				scl.smoothScrollTo(0, SCLocation);	
				SCLocation=0;
			}
		}, 0);

		return view;
		
	}
	
	
	//有关动态新闻的内容，使用定时器切换新闻内容
	/***********动态新闻*************/
    private String NewsTitle;
    private String NewsId;
    
	private Handler mHandler = new Handler();
	
	/**
	 * 图片自动轮播Task
	 */
	private int NewsNum=0;//表示第几个新闻
	List<News> NewsRolList;

//  List<News> list = MainActivity.newsList;//放在此处用来参考
//	StringBuffer nx = new StringBuffer();
//	// 解析list，并且加载到跑马灯中
//	for (News n : list)
//	{
//		nx.append(n.getTitle());
//	}
//	// TODO
//	tv_book.setText(nx.toString());
	private Runnable mImageTimerTask = new Runnable()
	{
		@Override
		public void run()
		{		    
			NewsNum++;
			if(NewsNum==NewsRolList.size()||NewsNum>NewsRolList.size())
			NewsNum=0;
			//处理滚动新闻的地方
			NewsTitle=NewsRolList.get(NewsNum).getTitle();//获取标题
			NewsId=NewsRolList.get(NewsNum).getId();//获取新闻id 
			tv_book.setText(NewsTitle);
		    mHandler.postDelayed(mImageTimerTask, 10000);//延时  时间					
		}
	};
	
	
	/**
	 * 图片滚动任务
	 */
	private void startImageTimerTask()
	{
		// 图片滚动
		mHandler.postDelayed(mImageTimerTask, 100);
	}
	
	
	

	// 在这里封装从后台一个获取新闻列表的方法
	private void getNewsList()
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
							"/admin/news/app/grid.do?pageNo=1&pageSize=5&type="
									+ 1 + "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取新闻列表失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}

				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);

				System.out.println("请求新闻：请求返回的结果是：" + res.trim());

				// 尝试将json串转化成bean对象
				NewsQueryResultBean nqrb = JSON.parseObject(res,
						NewsQueryResultBean.class);
				System.out.println("xxxx:" + nqrb.getApp_result_key());

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取新闻列表失败";

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
						System.out.println(n.getTitle() + "-" + n.getId());
					}
					// 将这些新闻对象存储到sp中
					MainActivity.newsList = list;

					ctx.jumpToNewsListFregment();
				}
			}

		}.start();
	}

	/******************* 裴超 修改2 开始 *****************/

	/**
	 * 跳转进入图书馆
	 * 
	 * @author Itachi
	 */
	private void getLibrary()
	{
		new Thread()
		{

			@Override
			public void run()
			{
				ctx.jumpToClickById(ParleConstant.JUMP_LIBRARY);
			}
		}.start();
	}

	/**
	 * 跳转进入勤工助学
	 * 
	 * @author Itachi
	 */
	private void getPartTimeJob()
	{
		new Thread()
		{

			@Override
			public void run()
			{
				ctx.jumpToClickById(ParleConstant.JUMP_ASSISTANT);
			}
		}.start();
	}

	/**
	 * 跳转进入招聘信息
	 * 
	 * @author Itachi
	 */
	private void getRecruit()
	{
		new Thread()
		{

			@Override
			public void run()
			{
				ctx.jumpToClickById(ParleConstant.JUMP_RECRUIT);
			}
		}.start();
	}

	private void getClassroom()
	{
		new Thread()
		{

			@Override
			public void run()
			{
				ctx.jumpToClickById(ParleConstant.JUMP_CLASSROOM_QUERY);
			}
		}.start();
	}

	/******************* 裴超 修改2 结束 *****************/

	// 轮播图相关
	private ImageCycleView mAdView;
	private ArrayList<String> mImageUrl = null;

	private ArrayList<String> mImageTitle = null;
	// private String imageUrl1 =
	// "https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/news/q=100/sign=854bc2a175cf3bc7ee00c9ece101babd/83025aafa40f4bfb19afa0f8064f78f0f736181a.jpg";
	// private String imageUrl2 =
	// "https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/news/q=100/sign=9f3227a309d79123e6e090749d355917/9825bc315c6034a80b6dcf2ace13495408237689.jpg";
	// private String imageUrl3 =
	// "https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/news/q=100/sign=32bf684cf203918fd1d139ca613c264b/3b87e950352ac65c41a059dffef2b21192138af0.jpg";
	//
	// private String imageTitle1 = "广州";
	// private String imageTitle2 = "北京";
	// private String imageTitle3 = "深圳";

	public int stype = 0;

	// private List<String> imgList = new ArrayList<String>();

	/**
	 * 初始化轮播图
	 * 
	 * @param view
	 */
	private void initCycleView(View view)
	{
		mImageUrl = new ArrayList<String>();
		mImageTitle = new ArrayList<String>();
		// 从这里获取首页轮播图的3张图片的路径
		for (String s : MainActivity.bannerImgList)// 然后循环加载
		{
			mImageUrl.add(s);
			mImageTitle.add("");
		}

		mAdView = (ImageCycleView) view.findViewById(R.id.ad_view);
		mAdView.setImageResources(mImageUrl, mImageTitle, mAdCycleViewListener,
				stype);
	}
	
	

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener()
	{
		@Override
		public void onImageClick(int position, View imageView)
		{
			  
			  int type=MainActivity.bannerList.get(position).getType();
			  switch(type){
			  case 1:  //跳转到南燕要闻详情页
				  getNanyanNewsContent(MainActivity.bannerList.get(position).getForeignId().trim());
				  break;
			  case 2:  //跳转到北大新闻详情页
				  getNewsContent(MainActivity.bannerList.get(position).getForeignId().trim());
				  break;
			  case 3:  //跳转到讲座信息详情页
				  getLectureContent(MainActivity.bannerList.get(position).getForeignId().trim());
				  break;
			  case 4:   //跳转到社团活动详情页
				  getActivityContent(MainActivity.bannerList.get(position).getForeignId().trim());
				  break;
			  } 
			 
//			 Toast.makeText(ctx,ctx.bannerList.get(position).getTitle()+" Type  类型是"+type+"   ID是  "+ctx.bannerList.get(position).getId(),
//			 1).show();
			  System.out.println("题目是       "+MainActivity.bannerList.get(position).getTitle()+" Type  类型是"+type+"   ID是  "+MainActivity.bannerList.get(position).getId());
		}
	};

	Lecture lt = null;

	/**
	 * 初始化首页下面的讲座信息摘要
	 */
	private void initFirstPageLectureLayout()
	{
		LinearLayout ll_lecture = (LinearLayout) viewMain
				.findViewById(R.id.ll_lecture);// 讲座信息
		ll_lecture.removeAllViews();

		viewMain.findViewById(R.id.ll_lect_num).setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{		
					    SCLocation=scl.getScrollY();
						ctx.jumpToLectureListFragment();
					}
				});

		// ll_lecture.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View arg0)
		// {
		//
		// // ctx.jumpToActivityListFragment();
		// }
		// });

		for (int i = 0; i < lectureList.size(); i++)
		{

			lt = lectureList.get(i);
			final Lecture temp = lectureList.get(i);
			// lt.getLecTime();// 时间
			// lt.getLecPlace();// 地点
			// lt.getSpeaker();// 讲师
			// lt.getTitle();// 主题

			View sperator = inflaterMain.inflate(R.layout.item_line, null);
			if (i != 0)
				ll_lecture.addView(sperator);

			View item = inflaterMain.inflate(R.layout.first_page_lecture, null);
			// TextView tv_speaker = (TextView)
			// item.findViewById(R.id.tv_speaker);
			// tv_speaker.setText(lt.getSpeaker());

			TextView tv_title = (TextView) item.findViewById(R.id.tv_title);
			tv_title.setText(lt.getTitle());
			
			TextView tv_content=(TextView)item.findViewById(R.id.tv_content);
			String s_Context=lt.getContent().replaceAll("<p>", "");
			tv_content.setText(s_Context.replaceAll("</p>", "").replaceAll("<br/>", ""));
			

			TextView tv_lecplace = (TextView) item
					.findViewById(R.id.tv_lecplace);
			tv_lecplace.setText("["+lt.getLecPlace()+"]");
			TextView tv_date = (TextView) item.findViewById(R.id.tv_date);
			tv_date.setText(lt.getLecTime());

			SmartImageView iv_pic = (SmartImageView) item
					.findViewById(R.id.activity_item_poster);
			iv_pic.setImageUrl(lt.getAttachmentPrefix() + lt.getPoster());

			// View spe = item.findViewById(R.id.spe);
			// if (i == lectureList.size() - 1)
			// {
			// spe.setVisibility(View.GONE);
			// }

			item.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					MainActivity.lecture = temp;
					MainActivity.orderFrom = "firstPage";
					SCLocation=scl.getScrollY();
//					System.out.println("___________________________________________________________________");
//					System.out.println(SCLocation+""+"位置信息是++++++++++++++");
//					System.out.println("___________________________________________________________________");
//					System.out.println(scl.getScrollX()+"XXXXX位置信息是++++++++++++++");
					ctx.jumpToLectureContentFragment();
				}
			});

			ll_lecture.addView(item);
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
					res = OkHttpUtils.getInstance().doGet(
							ctx,
							"/admin/lecture/grid.do?pageNo=1&pageSize=" + 3
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
					Log.e("px","讲座信息"+res);
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
				System.out.println("LectureListFragment请求讲座信息：请求返回的结果是："
						+ res.trim());

				// 尝试将json串转化成bean对象
				LectureQueryResultBean nqrb = JSON.parseObject(res,
						LectureQueryResultBean.class);
				System.out.println("nqrb.getApp_result_key()xxxx00:"
						+ nqrb.getApp_result_key());

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
					lectureList = nqrb.getData().getRows();
					lect_count = nqrb.getData().getTotal();
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LECTURE_COUNT);
					// 使用handler去通知主线程更新lectureListView
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LECTURE_LIST);
				}
			}
		}.start();

	}

	Activity ac = null;

	private void initFirstPageActivitiesLayout()
	{
		LinearLayout ll_activities = (LinearLayout) viewMain
				.findViewById(R.id.ll_activities);// 活动
		ll_activities.removeAllViews();

		viewMain.findViewById(R.id.ll_activity_count).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{   
						SCLocation=scl.getScrollY();
						ctx.jumpToActivityListFragment();
						
					}
				});

		viewMain.findViewById(R.id.ll_show_all_activities).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						SCLocation=scl.getScrollY();
						ctx.jumpToActivityListFragment();
					}
				});
		// ll_activities.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View arg0)
		// {
		// // ctx.jumpToLectureListFragment();
		// ctx.jumpToActivityListFragment();
		// //那就改成跳转到活动内容，要带ID
		// }
		// });

		for (int i = 0; i < activityList.size(); i++)
		{
			ac = activityList.get(i);
			final Activity temp = activityList.get(i);

			View sperator = inflaterMain.inflate(R.layout.item_sperator, null);
			if (i != 0)
			ll_activities.addView(sperator);
			View item = inflaterMain.inflate(R.layout.first_page_activities,
					null);

			SmartImageView siv = (SmartImageView) item
					.findViewById(R.id.iv_pic);
			String imageUrl = ac.getAttachmentPrefix() + ac.getPoster().trim();
			try
			{
				siv.setImageUrl(imageUrl, R.drawable.add_image2);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			TextView tv_title = (TextView) item.findViewById(R.id.tv_title);
			tv_title.setText(ac.getSubject());
			TextView tv_hoster = (TextView) item.findViewById(R.id.tv_hoster);
			tv_hoster.setText("[" + ac.getHoster() + "]");
			TextView tv_actplace = (TextView) item
					.findViewById(R.id.tv_actplace);
			tv_actplace.setText(ac.getActplace());
			TextView tv_date = (TextView) item.findViewById(R.id.tv_date);
			tv_date.setText(ac.getActtime());

			item.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					MainActivity.activity = temp;
					MainActivity.orderFrom = "firstPage";
					SCLocation=scl.getScrollY();
				//	ctx.jumpToActivityContentFragment();
					
					FragmentManager	fragmentManager = getFragmentManager();
					FragmentTransaction transaction = fragmentManager.beginTransaction();
					Fragment fragment = FragmentFactory.getInstanceByIndex(14,
							ctx);	
					transaction.hide(FirstPageFregment.this);
					transaction.add(R.id.content, fragment);				
					transaction.addToBackStack(null);
					transaction.commit();
				}
			});

			ll_activities.addView(item);
		}
	}

	// 在这里封装从后台一个获取社团活动列表的方法
	private void getActivityList()
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
							"/admin/activity/grid.do?pageNo=1&pageSize=3"
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取社团活动内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求社团活动：" + res.trim());

				// 尝试将json串转化成bean对象
				ActivityQueryResultBean nqrb = JSON.parseObject(res,
						ActivityQueryResultBean.class);

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取社团活动内容失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = nqrb.getApp_result_key();
				if ("0".equals(app_result_key))
				{
					// 到了这里说明正常返回了数据，我要在这里获取社团活动的标题
					activityList = nqrb.getData().getRows();
					activity_count = nqrb.getData().getTotal();
					handler.sendEmptyMessage(HandlerOrder.UPDATE_ACTIVITY_COUNT);
					// 使用handler去通知主线程更新activityListView
					handler.sendEmptyMessage(HandlerOrder.UPDATE_ACTIVITY_LIST);
				}
			}
		}.start();
	}

	// 获取滚动新闻的方法
	private void getRollingNews()
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
//					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(
							ctx,
							"/news/grid.do?type=0&pageNo=1&pageSize=3"
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st);
//					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					// toastMsg = "获取内容失败";
					// handler.sendEmptyMessage(HandlerOrder.TOAST);
//					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					return;
				}
				System.out.println("请求滚动新闻：" + res.trim());

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
						System.out.println(n.getTitle());
					}
					// // 将这些新闻对象存储到sp中
					MainActivity.newsList = list;
					NewsRolList=list;
					// // 使用handler去通知主线程更新listview
					handler.sendEmptyMessage(HandlerOrder.GET_ROLLING_NEWS);
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
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
						res = OkHttpUtils.getInstance().doGet(
								ctx,
								"/admin//news/app/initView.do?id=" + id + "&token="
										+ token + "&singnature=" + singnature
										+ "&st=" + st);
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
						System.out.println("新闻内容res：" + res);
						NewsContentQueryResultBean bean = JSON.parseObject(res,
								NewsContentQueryResultBean.class);
						if (res.trim().length() == 0)
						{
							toastMsg = "获取新闻内容失败";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
							handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
							return;
						}else
						{
							String app_result_key = bean.getApp_result_key();
							if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
															// 那么我要在这里取得新闻的标题，内容，显示在界面上
							{
								News b = bean.getEntity();
								MainActivity.news = b;
								MainActivity.newsDetail = null;
								if(MainActivity.news==null){
									toastMsg = "内容有误，请重试";
									handler.sendEmptyMessage(HandlerOrder.TOAST);
								}
								else
								ctx.jumpToNewsContentFregment(1);
							}
							
						}
						
						
					} catch (Exception e)
					{
						toastMsg = "内容有误，请重试";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						e.printStackTrace();
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
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
					ctx.jumpToNewsContentFregment(1);
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
						LectureContentQueryResultBean bean = JSON.parseObject(res,
								LectureContentQueryResultBean.class);
						System.out.println("请求具体讲座内容" + res.trim());
						// 返回值将会是JSON格式的数据，我要在这里解析
						if (res.trim().length() == 0)
						{
							toastMsg = "获取讲座信息内容失败";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
							return;
						}else
						{
						String app_result_key = bean.getApp_result_key();
						if ("0".equals(app_result_key))// 如果正常获得了讲座信息的内容,
														// 那么我要在这里取得讲座信息的标题，内容，显示在界面上
						{
							Lecture b = bean.getEntity();
							MainActivity.lecture = b;
						}
						 System.out.println("------------执行到这里------------------");
						 System.out.println(MainActivity.lecture==null);
						 if(MainActivity.lecture==null)
						 {
							 toastMsg = "内容有误，请重试";
					         handler.sendEmptyMessage(HandlerOrder.TOAST);
						 }else
							 ctx.jumpToLectureContentFragment(1);
						}
						
					} catch (Exception e)
					{
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
						e.printStackTrace();
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
						System.out.println("请求具体的活动内容：" + res.trim());
						ActivityContentQueryResultBean bean = JSON.parseObject(res,
								ActivityContentQueryResultBean.class);
						// 返回值将会是JSON格式的数据，我要在这里解析
						if (res.trim().length() == 0)
						{
							toastMsg = "获取社团活动内容失败";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
							return;
						}else
						{
							String app_result_key = bean.getApp_result_key();
							if ("0".equals(app_result_key))// 如果正常获得了社团活动的内容,
															// 那么我要在这里取得社团活动的标题，内容，显示在界面上
							{														    
								Activity b = bean.getEntity();
								MainActivity.activity = b;
								if(MainActivity.activity==null){
									toastMsg = "内容有误，请重试";
							        handler.sendEmptyMessage(HandlerOrder.TOAST);
								}else{
									ctx.jumpToActivityContentFragment(1);
								}
							}else{
								toastMsg = "内容有误，请重试";
						        handler.sendEmptyMessage(HandlerOrder.TOAST);
							}
						}
						
						
					} catch (Exception e)
					{
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
						e.printStackTrace();
					}																		
				}
			}.start();
		}

		
		
		@Override
		public void onAttach(android.app.Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
		}

		@Override
		public void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
		}

		@Override
		public void onDestroyView() {
			// TODO Auto-generated method stub
			super.onDestroyView();
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
		}

		
		
}
