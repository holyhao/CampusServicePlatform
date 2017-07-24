/**
 * ActivityManager.java[v 1.0.0]
 * class:com.yikong.activity.manager,ActivityManager
 * 周航 create at 2015-9-25 上午11:09:06
 */
package com.bdyjy.activity.manager;

import java.util.ArrayList;
import java.util.List;

import com.bdyjy.activity.LoginActivity;

import android.app.Activity;
import android.util.Log;

/**
 * activity管理器，每次打开的activity都会在这里有记录 com.yikong.activity.manager.ActivityManager
 * 
 * @author 周航 create at 2015-9-25 上午11:09:06
 */
public class MyActivityManager
{

	private static MyActivityManager manager;

	private List<Activity> activityList;

	private MyActivityManager()
	{
	}

	public static MyActivityManager getInstance()
	{
		if (null == manager)
		{
			manager = new MyActivityManager();
		}
		return manager;
	}

	/**
	 * 每打开一个activity，就往activityList里面放
	 * 
	 * @param act
	 */
	public void addActivity(Activity act)
	{
		if (null == activityList)
		{
			activityList = new ArrayList<Activity>();
		}
		// 判断集合中是否存在与当前activity同类的对象,如果有，就移除原来的
		for (int i = activityList.size() - 1; i >= 0; i--)
		{
			if (activityList.get(i).getClass().equals(act.getClass()))// 判断是否同类
			{
				activityList.get(i).finish();
				activityList.remove(i);
			}
		}
		activityList.add(act);
		Log.d("activity_count", activityList.size() + "");
	}

	/**
	 * 关闭整个应用
	 */
	public void finishApp()
	{
		if (null == activityList)
		{

		} else
		{
			for (int i = activityList.size() - 1; i >= 0; i--)
			{
				activityList.get(i).finish();// 结束每一个activity
				activityList.remove(i);
				Log.d("activity_count", activityList.size() + "");
			}
		}
	}

	/**
	 * 关闭整个应用
	 */
	public void toLogin()
	{
		if (null == activityList)
		{

		} else
		{
			for (int i = activityList.size() - 1; i >= 0; i--)
			{
				if (!(activityList.get(i) instanceof LoginActivity))
				{
					activityList.get(i).finish();// 结束每一个activity
					activityList.remove(i);
					Log.d("activity_count", activityList.size() + "");
				}
			}
		}
	}

}
