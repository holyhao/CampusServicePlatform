package com.bdyjy.fragment.base;

import java.util.HashMap;

import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.NewsContentQueryResultBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

public abstract class FragmentWithCollection extends Fragment
{

	protected Context ctx;
	public Handler handler;
	public String toastMsg;
	public View mainView;

	/**
	 * 设置主体view
	 * 
	 * @param v
	 */
	public void setMainView(View v)
	{
		mainView = v;
	}

	/**
	 * 收藏类型
	 */
	private int type;

	public FragmentWithCollection(final MainActivity ctx, int type)
	{
		this.ctx = ctx;
		this.type = type;
		handler = new Handler(ctx.getMainLooper())
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
				case HandlerOrder.ADD_COLLECT_SUCC:// 添加收藏成功之后，应该是显示已收藏按钮
					mainView.findViewById(R.id.ll_not_collect).setVisibility(
							View.GONE);
					mainView.findViewById(R.id.ll_collect).setVisibility(
							View.VISIBLE);
					break;
				case HandlerOrder.CANCEL_COLLECT_SUCC:// 取消收藏成功之后，显示 收藏按钮
					mainView.findViewById(R.id.ll_not_collect).setVisibility(
							View.VISIBLE);
					mainView.findViewById(R.id.ll_collect).setVisibility(
							View.GONE);
					break;
				case HandlerOrder.POST_OK:
					// TODO
					ctx.hideRoundProcessDialog();
					String result = msg.getData().get("body").toString();
					System.out.println("新闻内容res：" + result);
					NewsContentQueryResultBean bean = JSON.parseObject(result,
							NewsContentQueryResultBean.class);
					System.out.println("xxxx:" + bean.getApp_result_key());
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
					// 那么我要在这里取得新闻的标题，内容，显示在界面上
					{
						addSuccess();
					} else
					{
						addFailed();
					}
					break;
				}
			}
		};
	}

	/**
	 * 当切换到其他fratment的时候，会执行此方法的内容
	 */
	@Override
	public void onDestroyView()
	{
		// 执行"收藏事件"，或者"取消收藏"

		System.out.println("FragmentWithCollection:onDestroyView");

		super.onDestroyView();
	}

	// 现在我需要一个标记，表示fragment退出的时候，是不是要执行收藏事件

	// 先写2个方法：执行添加收藏的动作，以及 取消收藏的动作，对照文档

	// 添加收藏
	/**
	 * @param title
	 *            内容标题
	 * @param foreignId
	 *            记录id
	 */
	public void addCollection(final String title, final String foreignId)
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
					// res = OkHttpUtils.getInstance().doGet(
					// ctx,
					// "/collect/addSave.do?foreignId=" + foreignId
					// + "&type=" + type + "&title=" + title
					// + "&token=" + token + "&singnature="
					// + singnature + "&st=" + st);

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("title", title);
					map.put("type", type + "");
					map.put("foreignId", foreignId);
					String http = "/collect/addSave.do?token=" + token
							+ "&singnature=" + singnature + "&st=" + st;

					OkHttpUtils.getInstance().doPostAsync(ctx, http, map,
							handler);

				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 
	 * 取消收藏
	 * 
	 * @param title
	 *            内容标题
	 * @param foreignId
	 *            记录id
	 */
	public void cancelCollection(final String foreignId)
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
					res = OkHttpUtils.getInstance()
							.doGet(ctx,
									"/collect/cancelCollect.do?foreignId="
											+ foreignId + "&type=" + type
											+ "&token=" + token
											+ "&singnature=" + singnature
											+ "&st=" + st);
				} catch (Exception e)
				{
					e.printStackTrace();
				} finally
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				}

				System.out.println("新闻内容res：" + res);
				NewsContentQueryResultBean bean = JSON.parseObject(res,
						NewsContentQueryResultBean.class);
				System.out.println("xxxx:" + bean.getApp_result_key());
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
				// 那么我要在这里取得新闻的标题，内容，显示在界面上
				{
					cancelSuccess();
				} else
				{
					cancelFailed();
				}

			}
		}.start();
	}

	/**
	 * 收藏相关组件处理
	 * 
	 * @param id
	 * @param isCollect
	 * @param title
	 */
	protected void dealCollect(final String id, final String isCollect,
			final String title)
	{
		if (null == isCollect)
		{
			System.out.println("error：isCollect为空");
			mainView.findViewById(R.id.ll_not_collect).setVisibility(
					View.INVISIBLE);
			mainView.findViewById(R.id.ll_collect).setVisibility(View.GONE);
			return;
		}
		if ("0".equals(isCollect))// 0，表示未收藏，其他的均表示已收藏
		{
			mainView.findViewById(R.id.ll_not_collect).setVisibility(
					View.VISIBLE);
			mainView.findViewById(R.id.ll_collect).setVisibility(View.GONE);
		} else
		{
			mainView.findViewById(R.id.ll_collect).setVisibility(View.VISIBLE);
			mainView.findViewById(R.id.ll_not_collect).setVisibility(View.GONE);
		}

		View v1 = mainView.findViewById(R.id.ll_not_collect);
		v1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				addCollection(title, id);
			}
		});

		View v2 = mainView.findViewById(R.id.ll_collect);
		v2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				cancelCollection(id);
			}
		});
	}

	/**
	 * 收藏成功的时候，调用此方法
	 */
	public void addSuccess()
	{
		toastMsg = "收藏添加成功";
		handler.sendEmptyMessage(HandlerOrder.TOAST);
		handler.sendEmptyMessage(HandlerOrder.ADD_COLLECT_SUCC);
	}

	/**
	 * 收藏失败的时候，调用此方法
	 */
	public void addFailed()
	{
		toastMsg = "收藏添加失败";
		handler.sendEmptyMessage(HandlerOrder.TOAST);
	}

	/**
	 * 收藏成功的时候，调用此方法
	 */
	public void cancelSuccess()
	{
		toastMsg = "取消收藏成功";
		handler.sendEmptyMessage(HandlerOrder.TOAST);
		handler.sendEmptyMessage(HandlerOrder.CANCEL_COLLECT_SUCC);
	}

	/**
	 * 收藏失败的时候，调用此方法
	 */
	public void cancelFailed()
	{
		toastMsg = "取消收藏失败";
		handler.sendEmptyMessage(HandlerOrder.TOAST);
	}
}
