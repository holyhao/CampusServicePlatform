/**
 * DateUtil.java[v 1.0.0]
 * class:com.bdyjy.util,DateUtil
 * 周航 create at 2016-4-8 下午3:26:16
 */
package com.bdyjy.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * com.bdyjy.util.DateUtil
 * 
 * @author 周航<br/>
 *         create at 2016-4-8 下午3:26:16
 */
public class DateUtil
{
	static Date date = new Date();// 取时间

	/**
	 * 获取今年的年份
	 */
	public static String getThisYear()
	{
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-");
		return dateFm.format(date);
	}

	/**
	 * 获取以今天日期为标准进行平移，得出最后的星期
	 */
	public static String getDayOfTodayByOffset(int Offset)
	{
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, Offset);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		return dateFm.format(date);
	}

	/**
	 * 获取以今天日期为标准进行平移，得出最后的日期
	 */
	public static String getDateOfTodayByOffset(int Offset)
	{
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, Offset);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
		return dateFm.format(date);
	}

	/**
	 * 获取以明天日期为标准进行平移，得出最后的星期
	 */
	public static String getDayOfTomorrowByOffset(int Offset)
	{
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, Offset + 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		return dateFm.format(date);
	}

	/**
	 * 获取以明天日期为标准进行平移，得出最后的日期
	 */
	public static String getDateOfTomorrowByOffset(int Offset)
	{
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, Offset + 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
		return dateFm.format(date);
	}

	/**
	 * 获取今天是星期几
	 */
	public static String getDayOfToday()
	{
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		return dateFm.format(date);
	}

	/**
	 * 获取今天是几月几号
	 */
	public static String getDateOfToday()
	{
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
		return dateFm.format(date);
	}
	/**
	 * 获取今天是几月几号
	 * 以年月日的方式进行输出
	 */
	public static String getDateOfToday2()
	{
		SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日");
		return dateFm.format(date);
	}

	/**
	 * 获取明天是星期几
	 */
	public static String getDayOfTomorrow()
	{
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		return dateFm.format(date);
	}

	/**
	 * 获取明天是几月几号
	 */
	public static String getDateOfTomorrow()
	{
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		System.out.println("日期的格式是"+date);
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
		return dateFm.format(date);
	}
	
	/**
	 * 获取明天是几月几号
	 * 采用 年月日的格式进行输出
	 */
	public static String getDateOfTomorrow2()
	{
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		System.out.println("日期的格式是"+date);
		SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日");
		return dateFm.format(date);
	}
	
	/**
	 * 获取昨天是几月几号
	 */
	public static String getDateOfYesterday()
	{
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
		return dateFm.format(date);
	}
	/**
	 * 获取昨天是几月几号
	 * 以年月日的方式输出
	 */
	public static String getDateOfYesterday2()
	{
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日");
		return dateFm.format(date);
	}


}
