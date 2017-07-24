/**
 * BaseActivity.java[v 1.0.0]
 * class:com.yikong.activity,BaseActivity
 * 周航 create at 2015-9-25 上午10:44:17
 */
package com.bdyjy.activity.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import com.bdyjy.R;
import com.bdyjy.activity.manager.MyActivityManager;

/**
 * com.yikong.activity.BaseActivity
 * 
 * @author 周航<br/>
 *         create at 2015-9-25 上午10:44:17
 */
public abstract class BaseActivity extends Activity implements OnClickListener
{

	public Handler handler;// 当创建耗时操作线程的时候，把这个handler作为参数传递过去，然后耗时操作完成，根据请求的结果来调用handler给activity返回消息，改变视图
	protected Context ctx;

	/**
	 * 标记此activity是否在pause的时候直接关闭,默认是true
	 */
	protected boolean ifOnPauseFinish = true;

	/**
	 * 初始化所有组件
	 */
	protected abstract void initWidget();

	protected abstract void initHandler();

	/**
	 * 本activity内的所有组件的点击事件
	 * 
	 * @param v
	 */
	protected abstract void widgetClick(View v);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ctx = this;
		MyActivityManager.getInstance().addActivity(this);
	}

	@Override
	public void onClick(View v)// baseActivity本身继承了OnClickListener，所有的监听器就是activity本身
	{
		widgetClick(v);
	}

	/***************************************************************************
	 * 
	 * Activity生命周期
	 * 
	 ***************************************************************************/
	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onResume()// 当activity被唤醒
	{
		super.onResume();
	}

	@Override
	protected void onStop()// 锁屏或者退回home，都会导致线程停止
	{
		super.onStop();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	// // 进度条相关
	// private Dialog mDialog;
	//
	// public void showRoundProcessDialog()
	// {
	// if (null != mDialog && mDialog.isShowing())
	// {
	// mDialog.hide();
	// }
	// mDialog = new AlertDialog.Builder(this).create();
	// mDialog.setCancelable(false);
	// mDialog.setTitle("加载中...");
	// // mDialog.setOnKeyListener(keyListener);
	// mDialog.show();
	// // 注意此处要放在show之后 否则会报异常
	// mDialog.setContentView(R.layout.loading_process_dialog_anim);
	// }
	//
	// public void hideRoundProcessDialog()
	// {
	// if (null != mDialog && mDialog.isShowing())
	// mDialog.hide();
	// }

	ProgressDialog dialog;

	public void hideRoundProcessDialog()
	{
		if (dialog != null)
			dialog.dismiss();
	}

	public void showRoundProcessDialog()
	{
		if (dialog != null && dialog.isShowing())
			return;

		dialog = new ProgressDialog(this);
		// if (content == null)
		// {
		// content = "loading...";
		// }

		dialog.setMessage("加载中..."); // 设置说明文字
		dialog.setIndeterminate(false); // 设置进度条是否为不明确(来回旋转)
		dialog.setCanceledOnTouchOutside(false); // 设置点击屏幕不消失
		dialog.setCancelable(false); // 设置进度条是否可以按退回键取消

		// 3.设置dialog的显示透明度等
		Window wd = dialog.getWindow(); // 获取屏幕管理器
		WindowManager.LayoutParams lp = wd.getAttributes();
		lp.alpha = 0.8f; // 设置循环框的透明度
		lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		wd.setAttributes(lp); // 设置弹出框的透明度
		wd.setGravity(Gravity.CENTER); // 设置水平居中

		// 4.显示弹出dialog
		dialog.show();
	}

}
