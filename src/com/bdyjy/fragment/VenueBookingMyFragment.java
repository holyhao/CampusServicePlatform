/**
 * VenueBookingFragment.java[v 1.0.0]
 * class:com.bdyjy.fragment,VenueBookingFragment
 * 周航 create at 2016-4-5 下午2:50:41
 */
package com.bdyjy.fragment;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.venue.ListTime;
import com.bdyjy.entity.venue.MyOrder;
import com.bdyjy.entity.venue.MyOrderData;
import com.bdyjy.entity.venue.MyOrderDataRow;
import com.bdyjy.util.HttpUtilPost;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

/**
 * com.bdyjy.fragment.VenueBookingFragment
 * 
 * @author 周航<br/> 场馆预定订单显示界面
 *         create at 2016-4-5 下午2:50:41
 */
@SuppressLint("NewApi")
public class VenueBookingMyFragment extends Fragment
{
	
	private static int status=-1;
	MainActivity ctx;
	private Handler handler = null;
	String toastMsg;
	private List<MyOrderDataRow> rows;

	View ll_all;
	View ll_to_pay;
	View ll_not_finish;
	View ll_finished;

	private void initHandler()
	{
		handler = new Handler(ctx.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case HandlerOrder.TOAST:
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_SHORT).show();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.UPDATE_LISTVIEW:
					initTable();
					break;
				case HandlerOrder.CANCLE_BOOKING_FAILE:
					Dialog(toastMsg);
					break;
				case HandlerOrder.CANCLE_BOOKING_SUCCESS:
					queryMyOrders(status);
					break;
				}
			}
		};
	}

	// // 进度条相关
	// private Dialog mDialog;
	//
	// public void showRoundProcessDialog()
	// {
	// mDialog = new AlertDialog.Builder(ctx).create();
	// mDialog.setCancelable(false);
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

	/**
	 * 
	 */
	public VenueBookingMyFragment(MainActivity ctx)
	{
		this.ctx = ctx;
		initHandler();
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	/**********返回功能**********/
	private void Back(){ 
		if ("psersonCenter".equals(MainActivity.orderFrom))
		{
			ctx.jumpToPersonalCenterFragment();
			MainActivity.orderFrom = "";
		} else
			ctx.jumpToFirstPageFregment();
	}

	private LayoutInflater inflater;
	private View viewMain;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.inflater = inflater;
		viewMain = inflater.inflate(R.layout.venue_booking_my, null);
		TextView tv = (TextView) viewMain.findViewById(R.id.tv_title);
		tv.setText("场馆预订-我的预订");

		viewMain.findViewById(R.id.ll_my).setVisibility(View.INVISIBLE);
		// 返回按钮
		viewMain.findViewById(R.id.tv_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if ("psersonCenter".equals(MainActivity.orderFrom))
						{
							ctx.jumpToPersonalCenterFragment();
							MainActivity.orderFrom = "";
						} else
							ctx.jumpToFirstPageFregment();
					}
				});

		ll_all = viewMain.findViewById(R.id.ll_all);
		ll_to_pay = viewMain.findViewById(R.id.ll_to_pay);
		ll_not_finish = viewMain.findViewById(R.id.ll_not_finish);
		ll_finished = viewMain.findViewById(R.id.ll_finished);

		// 增加切换效果
		// 全部
		ll_all.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initButton();
				ll_all.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg));
				// 并且重新加载数据
				queryMyOrders(-1);
			}
		});

		ll_to_pay.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initButton();
				ll_to_pay.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg));
				status=0;
				queryMyOrders(0);
			}
		});

		ll_not_finish.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initButton();
				ll_not_finish.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg));
				status=2;
				queryMyOrders(2);
			}
		});

		ll_finished.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initButton();
				ll_finished.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg));
				status=1;
				queryMyOrders(1);
			}
		});

		queryMyOrders(-1);

		return viewMain;
	}

	private void initButton()
	{
		ll_finished.setBackground(getResources().getDrawable(
				R.drawable.not_selected_item_bg));
		ll_to_pay.setBackground(getResources().getDrawable(
				R.drawable.not_selected_item_bg));
		ll_not_finish.setBackground(getResources().getDrawable(
				R.drawable.not_selected_item_bg));
		ll_all.setBackground(getResources().getDrawable(
				R.drawable.not_selected_item_bg));
	}

	/**
	 * 线程：查找我的订单
	 * 
	 * 参数：全部 -1;0 待支付;1已支付; 2 已取消;
	 * 
	 * @param id
	 */
	private void queryMyOrders(final int status)
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

				StringBuffer timeStr = new StringBuffer();
				if (status != -1)
				{
					timeStr.append("&status=" + status);
				}

				String httpStr = "/admin/appointmenrecord/grid.do?" + "token="
						+ token + "&singnature=" + singnature + "&st=" + st
						+ timeStr.toString();

				System.out.println(httpStr);

				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(ctx, httpStr);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					System.out.println(res);

					MyOrder bean = JSON.parseObject(res, MyOrder.class);
					MyOrderData mod = bean.getData();
					rows = mod.getRows();
					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);

				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取数据失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
			}
		}.start();
	}

	/**
	 * 将数据加载到界面
	 */
	private void initTable()
	{

		LinearLayout ll_list = (LinearLayout) viewMain
				.findViewById(R.id.ll_list);// 这是列表
		ll_list.removeAllViews();

		// 遍历list
		for (MyOrderDataRow r : rows)
		{
			View item = inflater.inflate(R.layout.venue_order_item, null);// 每一项item

			r.getId();// 订单id
			r.getRoomId();// 场馆id
			r.getAmount();// 预订费用
			r.getAppDate();// 预订日期
			r.getRoomName();// 场馆名称
			r.getStatus();// 支付状态
			
            Log.e("id",r.getId()+"==="+r.getRoomId());
            
			TextView tv_venue_name = (TextView) item
					.findViewById(R.id.tv_venue_name);
			tv_venue_name.setText(r.getRoomName());
			tv_venue_name.setTag(r.getRoomId());

			TextView tv_app_date = (TextView) item
					.findViewById(R.id.tv_app_date);
			tv_app_date.setText(r.getAppDate());

			List<ListTime> list = r.getListTime();// 时间安排id

			TextView tv_amount = (TextView) item.findViewById(R.id.tv_amount);
			if(MainActivity.personalInfo.getPermission() == 2)
			{
				//学生
				tv_amount.setText("￥" + Double.parseDouble(r.getAmount()));
				
			}else
			{
				//教师
				tv_amount.setText("￥" + Double.parseDouble(r.getAmount())* 2);
			}
			
			TextView tv_time_1 = (TextView) item.findViewById(R.id.tv_time_1);
			TextView tv_time_2 = (TextView) item.findViewById(R.id.tv_time_2);

			int i = 1;

			for (ListTime lt : list)
			{
				if (i == 1)
				{
					tv_time_1.setText(lt.getStartTime() + " - "
							+ lt.getEndTime());
					tv_time_1.setTag(lt.getId());
				} else if (i == 2)
				{
					tv_time_2.setText(lt.getStartTime() + " - "
							+ lt.getEndTime());
					tv_time_2.setTag(lt.getId());
				}

				lt.getId();
				lt.getStartTime();
				lt.getEndTime();

				System.out.println(lt.getId());
				System.out.println(lt.getStartTime());
				System.out.println(lt.getEndTime());

				i++;
			}

			View v = item.findViewById(R.id.tv_to_pay);
			TextView tv_status_name = (TextView) item
					.findViewById(R.id.tv_status_name);
			if ("1".equals(r.getStatus()))
			{
				v.setVisibility(View.INVISIBLE);
				tv_status_name.setText("已支付");
			} else if ("2".equals(r.getStatus()))
			{
				v.setVisibility(View.INVISIBLE);
				tv_status_name.setText("已取消");
			} else
			// if ("0".equals(r.getStatus()))// 0 是未支付
			{
				v.setVisibility(View.VISIBLE);
				v.setOnClickListener(new MyOnClickListener(item,r.getId()));
				tv_status_name.setText("未支付");
			}
			// else
			// {
			// v.setVisibility(View.INVISIBLE);
			// tv_status_name.setText("未知状态值");
			// }

			System.out.println("=====================================");

			ll_list.addView(item);
		}
	}

	private class MyOnClickListener implements OnClickListener
	{

		View item;
		String id;

		MyOnClickListener(View item,String id)
		{
			this.item = item;
			this.id=id;
		}

		@Override
		public void onClick(View v)
		{
			new Thread(){
				
				public void run()
				{
					//
					String token = (String) SPUtils.get(ctx, "token", "");
					String singnature = (String) SPUtils.get(ctx, "singnature", "");
					String st = (String) SPUtils.get(ctx, "st", "");
					String res = null;
					try {
						res = OkHttpUtils.getInstance().doGet(
								ctx,
								"appointmenrecord/cancelOrderById.do?id=" + id+ "&token=" + token + "&singnature="
								+ singnature + "&st=" + st);
						Log.e("px", res);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						toastMsg = "网络错误，请重试";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						e.printStackTrace();
						return ;
					}
					
					if (null == res || res.length() == 0
							|| res.trim().length() == 0
							|| res.contains("error"))
					{
						toastMsg = "删除失败";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					}
					
					JSONObject obj = JSON.parseObject(res);
					String system_result_key = (String) obj.get("system_result_key");
					String app_result_key=(String)obj.get("app_result_key");
					String system_result_message_key = (String) obj.get("system_result_message_key");
					if ("0".equals(app_result_key))
					{
					toastMsg ="删除成功";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.CANCLE_BOOKING_SUCCESS);
					}else if("0".equals(system_result_key))
					{
						toastMsg=system_result_message_key;
						handler.sendEmptyMessage(HandlerOrder.CANCLE_BOOKING_FAILE);
					}
					
				};
				
			}.start();
			
			
			
			
			// 跳转之前，先整理一下数据
			// 2016-04-06&1834B71BABF74EA9ABC72647CC185DE4&20:30 - 21:00
			// 足球馆_3&30.00&A77387170AC340DF9EF81D493B54DD40
			// TODO 下一步，将上述参数塞到这里来
			// MainActivity.venueBookingResultMap = map;
			
		
			/*一下部分代码屏蔽需求变动
			TextView tv_app_date = (TextView) item
					.findViewById(R.id.tv_app_date);// app_date
			String app_date = tv_app_date.getText().toString();

			TextView tv_time_1 = (TextView) item.findViewById(R.id.tv_time_1);// tv_time_1
			TextView tv_time_2 = (TextView) item.findViewById(R.id.tv_time_2);// tv_time_2

			String time_1_id = tv_time_1.getTag().toString();
			String time_2_id = null;
			if (tv_time_2.getTag() != null)
			{
				time_2_id = tv_time_2.getTag().toString();
			} else
			{
				time_2_id = "";
			}

			String time_1 = tv_time_1.getText().toString();
			String time_2 = tv_time_2.getText().toString();

			TextView tv_venue_name = (TextView) item
					.findViewById(R.id.tv_venue_name);
			String venue_name = tv_venue_name.getText().toString();
			String venue_id = tv_venue_name.getTag().toString();

			TextView tv_amount = (TextView) item.findViewById(R.id.tv_amount);
			String amount = tv_amount.getText().toString().substring(1);

			HashMap<String, String> map = new HashMap<String, String>();
			if (time_1_id.trim().length() > 0)
			{
				map.put(app_date + "&" + time_1_id + "&" + time_1, venue_name
						+ "&" + amount + "&" + venue_id);
			}

			if (time_2_id.trim().length() > 0)
			{
				map.put(app_date + "&" + time_2_id + "&" + time_2, venue_name
						+ "&" + amount + "&" + venue_id);
			}

			for (String key : map.keySet())
			{
				System.out.println(key + " --------- " + map.get(key));
			}

			MainActivity.venueBookingResultMap = map;

			MainActivity.order_data_from = "VenueBookingMyFragment";
			ctx.jumpToVenueBookingPayMyFregment();*/
		}
	}
	
	public void Dialog(String message)
	{
		AlertDialog ad = new AlertDialog.Builder(getActivity())
		.setTitle("提示消息")
		.setMessage(message)
		.setPositiveButton("确定",
		new DialogInterface.OnClickListener() {


		public void onClick(DialogInterface dialog,
		int which) {
		}
		})
	    .create();
		ad.show();
	}
}
