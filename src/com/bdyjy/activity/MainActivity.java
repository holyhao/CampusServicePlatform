package com.bdyjy.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.bdyjy.util.lazy_load_img.ImageDownloader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bdyjy.R;
import com.bdyjy.activity.base.BaseActivity;
import com.bdyjy.activity.manager.MyActivityManager;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;

import com.bdyjy.entity.News;
import com.bdyjy.entity.activity.Activity;
import com.bdyjy.entity.assistant.AssistantNewBean;
import com.bdyjy.entity.campusguide.CampusGuides;
import com.bdyjy.entity.coursequery.Course;
import com.bdyjy.entity.firstPageImg.Rows;
import com.bdyjy.entity.lecture.Lecture;
import com.bdyjy.entity.collection.MyCollection;
import com.bdyjy.entity.complaint.Complaints;
import com.bdyjy.entity.contact.Contact;
import com.bdyjy.entity.contact.ContactContent;
import com.bdyjy.entity.contact.ContactDepartment;
import com.bdyjy.entity.recruit.RecruitNewBean;
import com.bdyjy.fragment.FleaReleaseFragment;
import com.bdyjy.fragment.MyFleaContentFragment;
import com.bdyjy.fragment.MyFleaFragment;
import com.bdyjy.fragment.MyLostFindContentFragment;
import com.bdyjy.fragment.MyLostFragment;
import com.bdyjy.fragment.LostFindReleaseFragment;
import com.bdyjy.fragment.factory.FragmentFactory;
import com.bdyjy.entity.secondMarket.SecondMarket;
import com.bdyjy.entity.lostfind.LostFind;
import com.bdyjy.entity.news.NewsDetail;
import com.bdyjy.entity.notice.Notice;

import com.bdyjy.entity.notice.NoticeContent;
import com.bdyjy.entity.notice.NoticeContentById;
import com.bdyjy.entity.personalinfo.PersonalInfo;

import com.bdyjy.entity.fix.MyFix;

public class MainActivity extends BaseActivity implements
		OnLayoutChangeListener
{

	
	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	/**
	 * 公共变量，存放本地配置文件中的IP地址
	 */
	public  static  String sIp;
	public Handler staticHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case HandlerOrder.HIDE_BUTTOM:
				// TODO
				hideButtom();
				break;
			case HandlerOrder.SHOW_BUTTOM:
				showButtom();
				break;
			}
		}
	};

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		sIp=openProperties(this);
		activityRootView = findViewById(R.id.root_layout);
		// 获取屏幕高度
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;

		fragmentManager = getFragmentManager();

		radioGroup = (RadioGroup) findViewById(R.id.rg_tab);

		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId)
					{
                        int index=checkedId%3;
                        if(index==0)
                        	index=3;
						FragmentTransaction transaction = fragmentManager
								.beginTransaction();
						Fragment fragment = FragmentFactory.getInstanceByIndex(
								index, MainActivity.this);
						transaction.replace(R.id.content, fragment);
						transaction.commit();
					}
				});
		((RadioButton) radioGroup.getChildAt(0)).setChecked(true);// 默认设置第一项为选中状态

		//jumpToFirstPageFregment();
        
	}

	/**
	 * 隐藏底部
	 */
	public void hideButtom()
	{
		radioGroup.setVisibility(View.GONE);
	}

	/**
	 * 显示底部
	 */
	public void showButtom()
	{
		radioGroup.setVisibility(View.VISIBLE);
	}

	/**
	 * 跳转到首页
	 */
	@SuppressLint("NewApi")
	public void jumpToFirstPageFregment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(1,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
        
		transaction.commit();
	}
	/**
	 * 跳转到场馆预订
	 */
	@SuppressLint("NewApi")
	public void jumpToVenueBookingFregment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(11,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);

		transaction.commit();
	}

	/**
	 * 跳转到场馆预订-我的
	 */
	@SuppressLint("NewApi")
	public void jumpToVenueBookingMyFregment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		// 增加动画效果
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		Fragment fragment = FragmentFactory.getInstanceByIndex(12,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);

		transaction.commit();
	}

	/**
	 * 跳转到场馆预订-支付
	 */
	@SuppressLint("NewApi")
	public void jumpToVenueBookingPayMyFregment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(13,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);

		transaction.commit();
	}

	/**
	 * 跳转到新闻列表，可供外界调用
	 */
	@SuppressLint("NewApi")
	public void jumpToNewsListFregment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(4,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 跳转到新闻内容，可供外界调用
	 */
	@SuppressLint("NewApi")
	public void jumpToNewsContentFregment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(5,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}
	
	
	/**
	 * 跳转到新闻内容，可供外界调用
	 */
	@SuppressLint("NewApi")
	public void jumpToNewsContentFregment(int source)
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(5,
				MainActivity.this,source);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * guocuicui跳转到通讯录列表，可供外界调用
	 */

	public void jumpToContactListFragment()
	{
		// 默认显示通讯录列表的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(6,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * guocuicui 跳转到通讯录内容，可供外界调用
	 */

	public void jumpToContactContentFragment()
	{
		// 默认显示通讯录内容的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(7,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * guocuicui跳转到讲座信息列表，可供外界调用
	 */

	public void jumpToLectureListFragment()
	{
		// 默认显示讲座信息列表的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		Fragment fragment = FragmentFactory.getInstanceByIndex(8,
				MainActivity.this);

		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * guocuicui 跳转到讲座信息内容，可供外界调用
	 */

	public void jumpToLectureContentFragment()
	{

		// 默认显示讲座信息内容的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		Fragment fragment = FragmentFactory.getInstanceByIndex(9,
				MainActivity.this);

		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}
	public void jumpToLectureContentFragment(int source)
	{

		// 默认显示讲座信息内容的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		Fragment fragment = FragmentFactory.getInstanceByIndex(9,
				MainActivity.this,source);

		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * guocuicui跳转到社团活动列表，可供外界调用
	 */

	public void jumpToActivityListFragment()
	{

		// 默认显示社团活动列表的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		Fragment fragment = FragmentFactory.getInstanceByIndex(10,
				MainActivity.this);

		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * guocuicui 跳转到社团活动内容，可供外界调用
	 */

	public void jumpToActivityContentFragment()
	{
		// 默认显示社团活动内容的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(14,
				MainActivity.this);	
		
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}
	
	public void jumpToActivityContentFragment(int source)
	{
		// 默认显示社团活动内容的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(14,
				MainActivity.this,source);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * guocuicui跳转到教务通知列表，可供外界调用
	 */

	public void jumpToNoticeListFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(15,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * guocuicui 跳转到教务通知内容，可供外界调用
	 */

	public void jumpToNoticeContentFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(16,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy跳转到投诉建议页
	 */
	public void jumpToComplaintFregment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(106,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy跳转到我的投诉建议列表
	 */
	public void jumpToMyComplaintListFregment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(107,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy跳转到投诉建议内容页
	 */
	public void jumpToComplaintContentFregment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(108,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转校园指南页
	 */
	public void jumpToCampusGuideFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(109,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转校园指南内容页
	 */
	public void jumpToCampusGuideContentFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(110,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转到个人中心页
	 */
	public void jumpToPersonalCenterFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(3,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 跳转到，我的收藏列表
	 */
	public void jumpToMyCollectionListFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(117,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转到个人信息页
	 */
	public void jumpToPersonalInfoFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(111,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转到个人设置
	 */
	public void jumpToPersonalSettingFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(112,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转到镜湖边
	 */
	public void jumpToLakesideFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(2,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转到修改密码
	 */
	public void jumpToResetPasswordFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(113,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转到关于我们
	 */
	public void jumpToAboutUsFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(114,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转到建议反馈
	 */
	public void jumpToFeedbackFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(115,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * holy 跳转到个人信息修改页
	 */
	public void jumpToPersonalInfoModifyFragment()
	{
		// 默认显示首页的fragment
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(116,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 跳转至我的二手交易 created by songdebin 记录跳转的来源，以便返回过去
	 **/
	public void jumpToMyFlea(int source)
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = new MyFleaFragment(MainActivity.this, source);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 跳转至我的二手交易内容查看 created by songdebin 记录跳转的来源，以便返回过去
	 **/
	public void jumpToMyFleaContent(int source)
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = new MyFleaContentFragment(MainActivity.this, source);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 跳转至我的二手交易发布页面查看 created by songdebin 记录跳转的来源，以便返回过去
	 **/
	public void jumpToFleaRelease(int source)
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = new FleaReleaseFragment(MainActivity.this, source);
		// Fragment fragment = new MyFleaContentFragment(MainActivity.this,
		// source);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 跳转至我的失物招领列表 created by songdebin 定义 source 记录跳转的来源，以便返回过去
	 **/
	public void jumpToMyLost(int source)
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = new MyLostFragment(MainActivity.this, source);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 跳转至我的失物招领列表 created by songdebin 定义 source 记录跳转的来源，以便返回过去
	 **/
	public void jumpToLostRelease(int source)
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = new LostFindReleaseFragment(MainActivity.this,
				source);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 跳转至我的失物招领内容 created by songdebin 定义 source 记录跳转的来源，以便返回过去
	 **/
	public void jumpToMyLostContent(int source)
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = new MyLostFindContentFragment(MainActivity.this,
				source);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 用于我的报修之间复杂的跳转 可以记录跳转路径便于返回 created by songdebin id 将要跳转的地址 list跳转路径记录
	 * 定义一个arraylist 用于跳转的地址
	 **/

	public void jumpToClickWithId(int id, ArrayList list)
	{
		// 定义一个表来存储跳转进来的id
		list.add(id);// 添加一个id
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(id, list,
				MainActivity.this);  
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 
	 * 根据跳转路径进行返回 create
	 * 
	 **/

	public void backToClickWithId(ArrayList list)
	{
		int id;
		list.remove(list.size() - 1);// 移除掉当前的地址
		id = Integer.parseInt(list.get(list.size() - 1).toString());
		if (id == Const.FRAGMENT1_ID)
		{
			this.jumpToFirstPageFregment();
		} else if (id == Const.FRAGMENT2_ID)
		{

		} else if (id == Const.FRAGMENT3_ID)
		{
			this.jumpToClickById(Const.FRAGMENT3_ID);
		} else
		{
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			Fragment fragment = FragmentFactory.getInstanceByIndex(id, list,
					MainActivity.this);
			transaction.replace(R.id.content, fragment);
			transaction.commit();
		}
	}

	@Override
	protected void initWidget()
	{

	}

	@Override
	protected void initHandler()
	{

	}

	@Override
	protected void widgetClick(View v)
	{

	}
	
	


	// 连续按两次返回退出应用
	private long exitTime = 0;

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//		if (keyCode == KeyEvent.KEYCODE_BACK)
//		{
//			exit();
//			return false;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	   class keyback implements KeyDown{
			public void OnkeyDown(){
				exit();
			} 
	   }
	
    public keyback key1=new keyback();
    public KeyDown keydown= key1;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			 keydown.OnkeyDown();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	


	public void exit()
	{

		// 先判断当前fragment是不是3个base页
		// TODO 从底部切换layout是否显示来决定
		if (radioGroup.getVisibility() != View.VISIBLE)
		{
			return;
		}

		if ((System.currentTimeMillis() - exitTime) > 2000)
		{
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else
		{
			finish();
			MyActivityManager.getInstance().finishApp();
		}
	}

	/**
	 * 公用变量，用于存放新闻列表
	 */
	public static List<News> newsList;

	/**
	 * 公用变量，用于存放新闻
	 */
	public static News news;

	/**
	 * 公用变量，用于存放新闻(南燕要闻专用)
	 */
	public static NewsDetail newsDetail;

	/**
	 * 图片加载器, 用于显示列表中的图片
	 */
	public static ImageDownloader mDownloader;

	/**
	 * holy公用变量，用于存放我的投诉列表
	 */
	public static List<Complaints> complaintsList;
	/**
	 * holy公用变量，用于存放投诉
	 */
	public static Complaints complaints;

	/**
	 * holy公用变量，用于存放校园生活指南内容
	 */
	public static CampusGuides campusGuides;
	/**
	 * holy公用变量，用于存放校园生活指南内容
	 */
	public static PersonalInfo personalInfo;
	/**
	 * holy公用变量，
	 */
	public static Bitmap bitmap;

	/************* guocuicui 修改部分 开始 ******************/
	/**
	 * 公用变量，用于存放通讯录列表
	 */
	public static List<ContactDepartment> contactlistByDepartment;

	/**
	 * 公用变量，用于存放通讯录
	 */
	public static ContactContent contact;

	/**
	 * 公用变量，用于存放讲座信息列表
	 */
	public static List<Lecture> lectureList;

	/**
	 * 公用变量，用于存放讲座信息
	 */
	public static Lecture lecture;

	/**
	 * 公用变量，用于存放新闻列表
	 */
	public static List<Activity> activityList;

	/**
	 * 公用变量，用于存放活动
	 */
	public static Activity activity;

	/**
	 * 公用变量，用于存放新闻列表
	 */
	public static List<NoticeContent> noticeList;

	/**
	 * 公用变量，用于存放通知
	 */
	public static NoticeContentById notice;

	/**
	 * 公用变量，用于存放二手交易 defined by songdebin
	 */
	public static List<SecondMarket> secondMarket;

	/**
	 * 公用变量，用于存放二手交易内容页 defined by songdebin
	 */
	public static SecondMarket secondmarketcontent;

	/**
	 * 公用变量，用于存放失物招领 defined by songdebin
	 */
	public static List<LostFind> lostfindlist;

	/**
	 * 公用变量，用于存放失物招领 defined by songdebin
	 */
	public static LostFind lostfindcontent;

	/**
	 * 公用变量，用于我的维护报修 defined by songdebin
	 */
	public static List<MyFix> myfixlist;

	/**
	 * 公用变量，用于我的维护报修 defined by songdebin
	 */
	public static MyFix myfixcontent;
	
	/**
	 * 公共变量，用于校园生活指南标题
	 */
	public static String School_Life;
	
	/**
	 * 公共变量，用于判断个人信息修改后的更新
	 */
	public static int  person_info_ifupdate;

	/************* guocuicui 修改部分 结束 ******************/

	/*********************** parle 修改内容 开始 ******************/

	/**
	 * 跳转 根据传入的id进行跳转 调用的时候传入fragment的预设id
	 * 
	 * @author parle
	 * 
	 */
	public void jumpToClickById(int id)
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = FragmentFactory.getInstanceByIndex(id,
				MainActivity.this);
		transaction.replace(R.id.content, fragment);
		transaction.commit();
	}

	/**
	 * 公用变量，用于存放勤工俭学新闻列表
	 */
	public static List<AssistantNewBean> assistantNewsList;

	/**
	 * 公用变量，用于存放勤工俭学新闻
	 */
	public static AssistantNewBean assistantNew;

	/**
	 * 公用变量，用于存放招聘信息新闻列表
	 */
	public static List<RecruitNewBean> recruitNewsList;

	/**
	 * 公用变量，用于存放招聘信息新闻
	 */
	public static RecruitNewBean recruitNew;
	
	public static List<MyCollection> myCollectionList;

	/**
	 * 公用变量，用于存放课室列表 用一个Map来存储一个课室的课程信息 Sting是教室的名称，List是教室的课程
	 */
	public static TreeMap<String, List<Course>> classroomMap;

	/*********************** parle 修改内容 结束 ******************/

	/*************************** 周航 修改 *******************/
	public static Map<String, String> venueBookingResultMap;
	public static String order_data_from;
	public static List<String> bannerImgList = new ArrayList<String>();
	public static List<Rows> bannerList;//存储  首页图片的相关信息
	/**
	 * 跳转命令来源
	 */
	public static String orderFrom = "";


	/*************************** 周航 修改 完 *******************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bdyjy.activity.base.BaseActivity#onResume()
	 */
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 添加layout大小发生改变监听器
		activityRootView.addOnLayoutChangeListener(this);
	}

	/**
	 * 以下代码监听软键盘
	 */
	// Activity最外层的Layout视图
	private View activityRootView;
	// 屏幕高度
	private int screenHeight = 0;
	// 软件盘弹起后所占高度阀值
	private int keyHeight = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View.OnLayoutChangeListener#onLayoutChange(android.view.
	 * View, int, int, int, int, int, int, int, int)
	 */
	@Override
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
	{
		// old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

		// System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " +
		// oldBottom);
		// System.out.println(left + " " + top +" " + right + " " + bottom);

		// 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight))
		{

			// Toast.makeText(MainActivity.this, "监听到软键盘弹起...",
			// Toast.LENGTH_SHORT)
			// .show();
			this.hideButtom();
		} else if (oldBottom != 0 && bottom != 0
				&& (bottom - oldBottom > keyHeight))
		{

			// Toast.makeText(MainActivity.this, "监听到软件盘关闭...",
			// Toast.LENGTH_SHORT)
			// .show();
			this.showButtom();
		}
	}

	// 图片预览的功能 弹出提示框
	public void dialogShowImage()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
				.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.id.wage_bufagongzi);
	}
	
	private String openProperties(MainActivity context)
	{

		Properties pro = new Properties();  
		InputStream is;
		String s = null;
		try {
			is = context.getAssets().open("server.properties");
			pro.load(is); 
		    s=	"http://"+pro.getProperty("ip")+":"+pro.getProperty("port")+"/";
     	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   			
		
		//Properties pro = new Properties();  
		//pro.load(FileLoad.class.getResourceAsStream("/assets/test.properties"));
		return s;
	}
	
	public static String ToDBC(String input) {
		   char[] c = input.toCharArray();
		   for (int i = 0; i< c.length; i++) {
		       if (c[i] == 12288) {
		         c[i] = (char) 32;
		         continue;
		       }if (c[i]> 65280&& c[i]< 65375)
		          c[i] = (char) (c[i] - 65248);
		       }
		   return new String(c);
		}

	

	
	public static String StringFilter(String str) throws PatternSyntaxException{
	    str=str.replaceAll("【","[").replaceAll("】","]").replaceAll("！","!");//替换中文标号
	    String regEx="[『』]"; // 清除掉特殊字符
	    Pattern p = Pattern.compile(regEx);
	    Matcher m = p.matcher(str);
	 return m.replaceAll("").trim();
	}
	

}
