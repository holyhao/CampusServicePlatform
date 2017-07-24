package com.bdyjy.constants;

public class HandlerOrder
{
	/**
	 * 跳转到主界面
	 */
	public static final int TO_MAIN = 0x01;
	/**
	 * TOAST信息
	 */
	public static final int TOAST = 0x02;

	/**
	 * 进度条显示
	 */
	public static final int PROCESSBAR_SHOW = 0x03;

	/**
	 * 进度条隐藏
	 */
	public static final int PROCESSBAR_HIDE = 0x04;

	/**
	 * 通知主线程更新listview
	 */
	public static final int UPDATE_LISTVIEW = 0x05;

	/**
	 * 上传成功
	 */
	public static final int UPLOAD_OK = 0x06;

	/**
	 * post成功
	 */
	public static final int POST_OK = 0x07;
	/**
	 * post失败
	 */
	public static final int POST_ERROR = 0x11;
	/**
	 * 上传报错
	 */
	public static final int UPLOAD_ERROR = 0x08;

	/**
	 * 显示底部
	 */
	public static final int SHOW_BUTTOM = 0x09;

	/**
	 * 隐藏底部
	 */
	public static final int HIDE_BUTTOM = 0x10;

	/**
	 * 首页——更新讲座信息摘要
	 */
	public static final int UPDATE_LECTURE_LIST = 0x11;

	/**
	 * 首页——更新活动列表摘要
	 */
	public static final int UPDATE_ACTIVITY_LIST = 0x12;

	/**
	 * 支付调用完成(不管支付结果如何)
	 */
	public static final int SDK_PAY_FLAG = 0x13;

	/**
	 * 首页——更新讲座信息总数
	 */
	public static final int UPDATE_LECTURE_COUNT = 0x14;

	/**
	 * 首页——更新讲座信息总数
	 */
	public static final int UPDATE_ACTIVITY_COUNT = 0x15;

	public static final int PUBLIC_FLAG = 0x111110;

	/**
	 * 首页-轮播图
	 */
	public static final int FIRST_PAGE_IMG = 0x16;

	/**
	 * 添加收藏成功
	 */
	public static final int ADD_COLLECT_SUCC = 0x17;

	/**
	 * 取消收藏成功
	 */
	public static final int CANCEL_COLLECT_SUCC = 0x18;

	/**
	 * 获取首页滚动新闻成功
	 */
	public static final int GET_ROLLING_NEWS = 0x19;
	
	public static final int GET_PERSON_INFORMATION=0x20;
	
	/**
	 * 取消球馆预定订单成功
	 */
	public static final int CANCLE_BOOKING_SUCCESS=0x21;
	/**
	 * 取消球馆预定失败
	 */
	public static final int CANCLE_BOOKING_FAILE=0X22;
	

}
