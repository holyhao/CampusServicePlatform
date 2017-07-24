/**
 * a.java[v 1.0.0]
 * class:com.bdyjy.util,a
 * 周航 create at 2016-4-8 下午4:17:10
 */
package com.bdyjy.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class CustomToast
{
	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable()
	{
		public void run()
		{
			mToast.cancel();
		}
	};

	public static void showToast(Context mContext, String text, int duration)
	{
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		mHandler.postDelayed(r, duration);

		mToast.show();
	}
}