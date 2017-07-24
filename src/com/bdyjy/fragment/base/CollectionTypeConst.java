/**
 * CollectionTypeConst.java[v 1.0.0]
 * class:com.bdyjy.fragment.base,CollectionTypeConst
 * 周航 create at 2016-5-27 上午10:26:37
 */
package com.bdyjy.fragment.base;

/**
 * com.bdyjy.fragment.base.CollectionTypeConst
 * 
 * @author 周航<br/>
 *         create at 2016-5-27 上午10:26:37
 */
public class CollectionTypeConst
{
	/**
	 * 校内新闻
	 */
	public final static int NEWS = 1;
	/**
	 * 讲座信息
	 */
	public final static int LECTURE = 2;
	/**
	 * 社团活动
	 */
	public final static int ACTIVITIES = 3;

	/**
	 * 教务通知
	 */
	public final static int NOTICE = 4;
	/**
	 * 招聘信息
	 */
	public final static int RECRUIT = 5;
	/**
	 * 勤工助学
	 */
	public final static int WORK_STUDY = 6;
	/**
	 * 二手交易
	 */
	public final static int SECOND_HAND_TRADE = 7;
	/**
	 * 失物招领
	 */
	public final static int LOSTANDFOUND = 8;

	public static String getTypeTrans(int type)
	{
		String res = null;
		switch (type)
		{
		case 1:
			res = "[校内新闻]";
			break;
		case 2:
			res = "[讲座信息]";
			break;
		case 3:
			res = "[社团活动]";
			break;
		case 4:
			res = "[教务通知]";
			break;
		case 5:
			res = "[招聘信息]";
			break;
		case 6:
			res = "[勤工助学]";
			break;
		case 7:
			res = "[二手交易]";
			break;
		case 8:
			res = "[失物招领]";
			break;
		}
		return res;
	}
}
