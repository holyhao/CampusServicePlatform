package com.bdyjy.fragment.factory;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;

import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.constants.ParleConstant;
import com.bdyjy.fragment.AboutUsFragment;
import com.bdyjy.fragment.ActivityContentFragment;
import com.bdyjy.fragment.ActivityListFragment;
import com.bdyjy.fragment.AskFixFragment;
import com.bdyjy.fragment.AssistantContentFragment;
import com.bdyjy.fragment.AssistantListFragment;
import com.bdyjy.fragment.CampusGuideContentFragment;
import com.bdyjy.fragment.CampusGuideFragment;
import com.bdyjy.fragment.ClassroomQueryFragment;
import com.bdyjy.fragment.ComplaintContentFragment;
import com.bdyjy.fragment.ComplaintFragment;
import com.bdyjy.fragment.ContactContentFragment;
import com.bdyjy.fragment.ContactListFragment;
import com.bdyjy.fragment.FeedbackFragment;
import com.bdyjy.fragment.FirstPageFregment;
import com.bdyjy.fragment.FleaContentFragment;
import com.bdyjy.fragment.FleaMarketFragment;
import com.bdyjy.fragment.LakesideFragment;
import com.bdyjy.fragment.LectureContentFragment;
import com.bdyjy.fragment.LectureListFragment;
import com.bdyjy.fragment.LibraryFragment;
import com.bdyjy.fragment.LostContentFragment;
import com.bdyjy.fragment.LostFindListFragment;
import com.bdyjy.fragment.MyCollectionListFragment;
import com.bdyjy.fragment.MyComplaintListFragment;
import com.bdyjy.fragment.MyFixContent;
import com.bdyjy.fragment.MyFixFragment;
import com.bdyjy.fragment.NewsContentFragment;
import com.bdyjy.fragment.NewsListFragment;
import com.bdyjy.fragment.NoticeContentFragment;
import com.bdyjy.fragment.NoticeListFragment;
import com.bdyjy.fragment.PersonalCenterFragment;
import com.bdyjy.fragment.PersonalInfoFragment;
import com.bdyjy.fragment.PersonalInfoModifyFragment;
import com.bdyjy.fragment.PersonalSettingFragment;
import com.bdyjy.fragment.RecruitContentFragment;
import com.bdyjy.fragment.RecruitListFragment;
import com.bdyjy.fragment.ResetPasswordFragment;
import com.bdyjy.fragment.VenueBookingFragment;
import com.bdyjy.fragment.VenueBookingMyFragment;
import com.bdyjy.fragment.VenueBookingPayFragment;
import com.bdyjy.fragment.WageQueryFragment;

;

/**
 * fregment工厂，用于fregment切换
 * 
 * Created by admin on 13-11-23.
 */
public class FragmentFactory
{
	/**
	 * 是否显示底部切换按钮,显示切换按钮的时候，可以使用连续两次点击返回按钮退出应用
	 * 
	 * @param index
	 * @return
	 */
	public static boolean ifShowButtom(int index)
	{
		return index == 1 || index == 2 || index == 3 || index == 200;
	}

	public static Fragment getInstanceByIndex(int index, Context ctx)
	{
		Fragment fragment = null;
		if (ifShowButtom(index))
		{
			((MainActivity) ctx).staticHandler
					.sendEmptyMessage(HandlerOrder.SHOW_BUTTOM);
		} else
		{
			((MainActivity) ctx).staticHandler
					.sendEmptyMessage(HandlerOrder.HIDE_BUTTOM);
		}

		switch (index)
		{
		case 1:// 功能首页
			fragment = new FirstPageFregment((MainActivity) ctx);
			break;

		// holy 将fragment2改为LakesideFragment，镜湖边，命名规范。
		case 2:// 镜湖边
			fragment = new LakesideFragment((MainActivity) ctx);
			break;
		// holy 将fragment3改为 PersonalCenterFragment，个人中心，命名规范。
		case 3:// 个人中心
			fragment = new PersonalCenterFragment((MainActivity) ctx);
			break;

		case 4:// 新闻列表
			fragment = new NewsListFragment((MainActivity) ctx);
			break;
		case 5:// 新闻详情
			fragment = new NewsContentFragment((MainActivity) ctx);
			break;
		case 11:// 场馆预订
			fragment = new VenueBookingFragment((MainActivity) ctx);
			break;
		case 12:// 场馆预订-我的预订
			fragment = new VenueBookingMyFragment((MainActivity) ctx);
			break;
		case 13:// 场馆预订-支付
			fragment = new VenueBookingPayFragment((MainActivity) ctx);
			break;

		/**
		 * guocuicui
		 */
		case 6:// 通讯录列表
			fragment = new ContactListFragment((MainActivity) ctx);///////
			break;
		case 7:// 通讯录详情
			fragment = new ContactContentFragment((MainActivity) ctx);
			break;
		case 8:// 讲座信息列表
			fragment = new LectureListFragment((MainActivity) ctx);
			break;
		case 9:// 讲座信息详情
			fragment = new LectureContentFragment((MainActivity) ctx);
			break;
		case 10:// 社团活动列表
			fragment = new ActivityListFragment((MainActivity) ctx);
			break;
		case 14:// 社团活动详情
			fragment = new ActivityContentFragment((MainActivity) ctx);
			break;
		case 15:// 教务通知列表
			fragment = new NoticeListFragment((MainActivity) ctx);
			break;
		case 16:// 教务通知详情
			fragment = new NoticeContentFragment((MainActivity) ctx);
			break;

		/**
		 * holy 投诉建议部分
		 */
		// holy
		case 106:// 投诉建议
			fragment = new ComplaintFragment((MainActivity) ctx);
			break;
		// holy 我的投诉建议列表
		case 107:
			fragment = new MyComplaintListFragment((MainActivity) ctx);
			break;
		// holy 我的投诉内容页
		case 108:
			fragment = new ComplaintContentFragment((MainActivity) ctx);
			break;
		// holy 校园指南
		case 109:
			fragment = new CampusGuideFragment((MainActivity) ctx);
			break;
		// holy 校园指南内容页
		case 110:
			fragment = new CampusGuideContentFragment((MainActivity) ctx);
			break;
		// holy 个人信息
		case 111:
			fragment = new PersonalInfoFragment((MainActivity) ctx);
			break;
		// holy 个人设置
		case 112:
			fragment = new PersonalSettingFragment((MainActivity) ctx);
			break;
		// holy 修改密码
		case 113:
			fragment = new ResetPasswordFragment((MainActivity) ctx);
			break;
		// holy 关于我们
		case 114:
			fragment = new AboutUsFragment((MainActivity) ctx);
			break;
		// holy 建议反馈
		case 115:
			fragment = new FeedbackFragment((MainActivity) ctx);
			break;
		// holy 个人信息修改
		case 116:
			fragment = new PersonalInfoModifyFragment((MainActivity) ctx);
			break;
		case 117:// 我的收藏MyCollectionListFragment
			fragment = new MyCollectionListFragment((MainActivity) ctx);
			break;

		/**
		 * @author parle
		 */
		case ParleConstant.JUMP_LIBRARY:
			fragment = new LibraryFragment((MainActivity) ctx);
			break;

		case ParleConstant.JUMP_ASSISTANT:
			fragment = new AssistantListFragment((MainActivity) ctx);
			break;

		case ParleConstant.JUMP_RECRUIT:
			fragment = new RecruitListFragment((MainActivity) ctx);
			break;

		case ParleConstant.JUMP_ASSISTANT_CONTENT:
			fragment = new AssistantContentFragment((MainActivity) ctx);
			break;

		case ParleConstant.JUMP_RECRUIT_CONTENT:
			fragment = new RecruitContentFragment((MainActivity) ctx);
			break;
		case ParleConstant.JUMP_CLASSROOM_QUERY:
			fragment = new ClassroomQueryFragment((MainActivity) ctx);
			break;

		/**
		 * @author 宋德宾 从个人中心跳转至工资查询 2016-03-31
		 **/
		//

		case Const.FRAGMENT_WAGEQUERY_ID:
			fragment = new WageQueryFragment((MainActivity) ctx);
			break;

		// 获取fragment3
		case Const.FRAGMENT3_ID:
			fragment = new PersonalCenterFragment((MainActivity) ctx);
			break;

		// 获取二手交易列表
		case Const.FRAGMENT_FLEAMARKET_LIST_ID:
			fragment = new FleaMarketFragment((MainActivity) ctx);
			break;
		// 获取二手交易发布
		case Const.FRAGMENT_FLEARELEASE_ID:
			// fragment = new FleaReleaseFragment((MainActivity) ctx);
			break;

		// case Const.FRAFGMENT_FLEACONTENT_ID:

		// 获取二手交易内容页
		case Const.FRAGMENT_FLEACONTENT_ID:

			fragment = new FleaContentFragment((MainActivity) ctx);
			break;

		// 获取失物招领列表
		case Const.FRAGMENT_LOSTFINDLIST_ID:
			fragment = new LostFindListFragment((MainActivity) ctx);
			break;
		// 获取失物招领发布界面
		case Const.FRAGMENT_LOSTFINDRELEASE_ID:
			// fragment = new LostFindReleaseFragment((MainActivity) ctx);
			break;
		// 获取失物招领内容页
		case Const.FRAGMENT_LOSTFINDCONTENT_ID:
			fragment = new LostContentFragment((MainActivity) ctx);
			break;
		// 获取维护报修
		}
		return fragment;
	}
	
	
	public static Fragment getInstanceByIndex(int index,
			Context ctx,int source)
	{
		Fragment fragment = null;
		if (ifShowButtom(index))
		{
			((MainActivity) ctx).staticHandler
					.sendEmptyMessage(HandlerOrder.SHOW_BUTTOM);
		} else
		{
			((MainActivity) ctx).staticHandler
					.sendEmptyMessage(HandlerOrder.HIDE_BUTTOM);
		}
		switch (index)
		{
		case 5:// 新闻详情
			fragment = new NewsContentFragment((MainActivity) ctx,source);
			break;
		case 9:// 讲座信息详情
			fragment = new LectureContentFragment((MainActivity) ctx,source);
			break;
		case 14:// 社团活动详情
			fragment = new ActivityContentFragment((MainActivity) ctx,source);
			break;
		

		}
		return fragment;
	}


	// 方法重写 对于不同参数的进行使用
	public static Fragment getInstanceByIndex(int index, ArrayList list,
			Context ctx)
	{
		Fragment fragment = null;
		if (ifShowButtom(index))
		{
			((MainActivity) ctx).staticHandler
					.sendEmptyMessage(HandlerOrder.SHOW_BUTTOM);
		} else
		{
			((MainActivity) ctx).staticHandler
					.sendEmptyMessage(HandlerOrder.HIDE_BUTTOM);
		}
		switch (index)
		{
		// 获取维护报修
		case Const.FRAGMENT_ASKFIX_ID:
			fragment = new AskFixFragment(list, (MainActivity) ctx);
			break;
		case Const.FRAGMENT_MYFIX_ID:
			fragment = new MyFixFragment(list, (MainActivity) ctx);
			break;

		case Const.FRAGMENT_MYFIXCONTENT_ID:
			fragment = new MyFixContent(list, (MainActivity) ctx);
			break;

		}
		return fragment;
	}
}
