/**
 * Util.java[v 1.0.0]
 * class:com.example.exampleandroidproject.util,Util
 * 周航 create at 2016-3-16 上午11:08:58
 */
package com.bdyjy.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * com.example.exampleandroidproject.util.Util
 * 
 * @author 周航<br/>
 *         create at 2016-3-16 上午11:08:58
 */
public class Util
{
	/**
	 * @author 裴超
	 * 解决嵌套listview中，子listview高度显示不完全问题
	 * 获得数据之后，计算每个子listview有多少个item，总共有多少height，然后设置子listview的height
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if(listAdapter == null) {
			return;
		}
		
		int totalHeight = 0;
		for(int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
