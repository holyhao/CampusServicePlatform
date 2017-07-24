/**
 * VenueBookingFragment.java[v 1.0.0]
 * class:com.bdyjy.fragment,VenueBookingFragment
 * 周航 create at 2016-4-5 下午2:50:41
 */
package com.bdyjy.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
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
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.venue.Data;
import com.bdyjy.entity.venue.ListRoom;
import com.bdyjy.entity.venue.VenueQueryResultBean;
import com.bdyjy.util.DateUtil;
import com.bdyjy.util.DialogUtil;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

/**
 * com.bdyjy.fragment.VenueBookingFragment
 * 
 * @author 周航<br/>  球场数据预定已过期已预定显示界面
 *         create at 2016-4-5 下午2:50:41
 */
public class VenueBookingFragment extends Fragment
{
	private MainActivity ctx;
	// private List<VenueRecordBean> list = new ArrayList<VenueRecordBean>();
	private Map<String, String> map = new HashMap<String, String>();

	private String date = DateUtil.getDateOfToday();

	List<Data> list;
	private Handler handler = null;

	private List<ListRoom> listRoom;

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
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.UPDATE_LISTVIEW:// 获取数据成功之后，回到这里更新视图
					initTable();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.PUBLIC_FLAG:

					TextView tv_v1 = (TextView) viewMain
							.findViewById(R.id.tv_v1);
					TextView tv_v2 = (TextView) viewMain
							.findViewById(R.id.tv_v2);
					TextView tv_v3 = (TextView) viewMain
							.findViewById(R.id.tv_v3);

					tv_v1.setText(listRoom.get(0).getName());
					tv_v2.setText(listRoom.get(1).getName());
					tv_v3.setText(listRoom.get(2).getName());

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

	/**
	 * 
	 */
	public VenueBookingFragment(MainActivity ctx)
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
		if ("lake".equals(MainActivity.orderFrom))
		{
			ctx.jumpToLakesideFragment();
			MainActivity.orderFrom = "";
		} else
		{
			ctx.jumpToFirstPageFregment();
		}
	}

	private View viewMain;
	private LayoutInflater inflater;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.inflater = inflater;
		viewMain = inflater.inflate(R.layout.venue_booking, null);

		// 标题的重命名
		TextView tv = (TextView) viewMain.findViewById(R.id.tv_title);
		tv.setText("场馆预订");

		// 返回按钮点击事件
		viewMain.findViewById(R.id.tv_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if ("lake".equals(MainActivity.orderFrom))
						{
							ctx.jumpToLakesideFragment();
							MainActivity.orderFrom = "";
						} else
						{
							ctx.jumpToFirstPageFregment();
						}
					}
				});

		viewMain.findViewById(R.id.ll_my).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						ctx.jumpToVenueBookingMyFregment();
					}
				});

		// 增加日期切换效果
		final View ll_1 = viewMain.findViewById(R.id.ll_1);
		final View ll_2 = viewMain.findViewById(R.id.ll_2);
		ll_1.setOnClickListener(new OnClickListener()
		{
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v)
			{
				ll_1.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg2));
				ll_2.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg2));

				// 并且重新查询数据
				TextView tv_date = (TextView) viewMain
						.findViewById(R.id.tv_today_date);
				date = tv_date.getText().toString();
				System.out.println("date:" + date);
				queryVenue(date);
			}
		});
		ll_2.setOnClickListener(new OnClickListener()
		{
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v)
			{
				ll_2.setBackground(getResources().getDrawable(
						R.drawable.selected_item_bg2));
				ll_1.setBackground(getResources().getDrawable(
						R.drawable.not_selected_item_bg2));

				TextView tv_date = (TextView) viewMain
						.findViewById(R.id.tv_tomorrow_date);
				date = tv_date.getText().toString();

				queryVenue(date);
			}
		});

		// 预定按钮点击事件
		viewMain.findViewById(R.id.ll_book_now).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						// 检测是否已经选择了场馆
						if (map.size() > 0)
						{
							MainActivity.venueBookingResultMap = map;
							MainActivity.order_data_from = "VenueBookingFragment";// 标记，是从list过来的
							ctx.jumpToVenueBookingPayMyFregment();
						} else
						{
							Toast.makeText(ctx, "你还没选择场馆", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

		//
		View ll_pervious = viewMain.findViewById(R.id.ll_previous);
		View ll_next = viewMain.findViewById(R.id.ll_next);

		// 点击往前一天，则当前显示的2个日期，就往前挪一天，并且重新更新数据
		ll_pervious.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				offset--;// 更新偏移量
				TextView tv_today_day = (TextView) viewMain
						.findViewById(R.id.tv_today_day);
				tv_today_day.setText(DateUtil.getDayOfTodayByOffset(offset));
				// 今天几号
				TextView tv_today_date = (TextView) viewMain
						.findViewById(R.id.tv_today_date);
				tv_today_date.setText(DateUtil.getDateOfTodayByOffset(offset));
				// 明天星期几
				TextView tv_tomorrow_day = (TextView) viewMain
						.findViewById(R.id.tv_tomorrow_day);
				tv_tomorrow_day.setText(DateUtil
						.getDayOfTomorrowByOffset(offset));
				// 明天几号
				TextView tv_tomorrow_date = (TextView) viewMain
						.findViewById(R.id.tv_tomorrow_date);
				tv_tomorrow_date.setText(DateUtil
						.getDateOfTomorrowByOffset(offset));

				// 并且更新当前日期
				date = tv_today_date.getText().toString();
				queryVenue(date);
			}
		});
		// 往后一天，同理,向后挪一天并且重新更新数据
		ll_next.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				offset++;
				TextView tv_today_day = (TextView) viewMain
						.findViewById(R.id.tv_today_day);
				tv_today_day.setText(DateUtil.getDayOfTodayByOffset(offset));
				// 今天几号
				TextView tv_today_date = (TextView) viewMain
						.findViewById(R.id.tv_today_date);
				tv_today_date.setText(DateUtil.getDateOfTodayByOffset(offset));
				// 明天星期几
				TextView tv_tomorrow_day = (TextView) viewMain
						.findViewById(R.id.tv_tomorrow_day);
				tv_tomorrow_day.setText(DateUtil
						.getDayOfTomorrowByOffset(offset));
				// 明天几号
				TextView tv_tomorrow_date = (TextView) viewMain
						.findViewById(R.id.tv_tomorrow_date);
				tv_tomorrow_date.setText(DateUtil
						.getDateOfTomorrowByOffset(offset));

				date = tv_tomorrow_date.getText().toString();
				queryVenue(date);
			}
		});

		// TODO 这是测试代码
		queryVenue(date);
		return viewMain;
	}

	// 在这里封装从后台一个获取新闻内容的方法
	String toastMsg;
	int offset = 0;// 日期偏移量

	/**
	 * 线程：查找当前场馆预订情况
	 * 
	 * @param id
	 */
	private void queryVenue(final String date)
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

				String httpStr = "/admin/appointmenrecord/initByDate.do?appDate="
						+ date
						+ "&token="
						+ token
						+ "&singnature="
						+ singnature + "&st=" + st;
				
				System.out.println(httpStr);

				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(ctx, httpStr);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取数据失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}

				VenueQueryResultBean bean = JSON.parseObject(res,
						VenueQueryResultBean.class);
				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取数据失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
				// 那么我要在这里取得新闻的标题，内容，显示在界面上
				{
					list = bean.getData();// 这就是我要展示的数据
					// TODO
					listRoom = bean.getListRoom();
					handler.sendEmptyMessage(HandlerOrder.PUBLIC_FLAG);

					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				}
			}
		}.start();
	}

	/**
	 * 将数据加载到表格中
	 * 
	 * @param list
	 */
	@SuppressLint("InflateParams")
	private void initTable()
	{

		// 每一次更新table数据，都要先把 旧的提交数据清除
		if (map != null)
		{
			map.clear();
		}

		LinearLayout ll_item_list = (LinearLayout) viewMain
				.findViewById(R.id.ll_item_list);

		ll_item_list.removeAllViews();

		// 日期选择项的设定:获取今天和明天的日期，并且获得星期
		// 今天星期几
		TextView tv_today_day = (TextView) viewMain
				.findViewById(R.id.tv_today_day);
		tv_today_day.setText(DateUtil.getDayOfTodayByOffset(offset));
		// 今天几号
		TextView tv_today_date = (TextView) viewMain
				.findViewById(R.id.tv_today_date);
		tv_today_date.setText(DateUtil.getDateOfTodayByOffset(offset));
		// 明天星期几
		TextView tv_tomorrow_day = (TextView) viewMain
				.findViewById(R.id.tv_tomorrow_day);
		tv_tomorrow_day.setText(DateUtil.getDayOfTomorrowByOffset(offset));
		// 明天几号
		TextView tv_tomorrow_date = (TextView) viewMain
				.findViewById(R.id.tv_tomorrow_date);
		tv_tomorrow_date.setText(DateUtil.getDateOfTomorrowByOffset(offset));

		// 加载场馆数据
		int index = 0;
		for (Data d : list)
		{
            
			String timeId = d.getId();// TODO 这个就是时间id
			String time = d.getStartTime() + " - " + d.getEndTime();// 时间段值

			View sperator = inflater.inflate(R.layout.item_sperator, null);
			ll_item_list.addView(sperator);
			View item = inflater.inflate(R.layout.venue_booking_item1, null);

			TextView tv_time = (TextView) item.findViewWithTag("time");
			tv_time.setText(time);

			// 在这里获取通过tag，获取节点，然后添加事件
			String sx1 = "场地一_";
			String sx2 = "场地二_";
			String sx3 = "场地三_";
			String[] tags ={ sx1 + index, sx2 + index, sx3 + index };
			
			item.findViewWithTag(sx1).setTag(sx1 + index);
			item.findViewWithTag(sx2).setTag(sx2 + index);
			item.findViewWithTag(sx3).setTag(sx3 + index);

			// 在添加监听事件之前，先对选项的tag进行标记
			List<ListRoom> listRoom = d.getListRoom();// 这是每个场馆的信息
			String[] flags = new String[3];
			String[] prices = new String[3];
			String[] roomId = new String[3];
			for (int ix = 0; ix < listRoom.size(); ix++)
			{				
				flags[ix] = listRoom.get(ix).getFlag();// 常关状态：-1已过期 0可预约 1已预约
				prices[ix] = listRoom.get(ix).getPrice();// 预约价格
				roomId[ix] = listRoom.get(ix).getId();// 场馆id
			}

			// TEST 测试代码
			// flags = new String[]
			// { "0", "0", "0" };
			for (int iy = 0; iy < tags.length; iy++)
			{
				View iv = item.findViewWithTag(tags[iy]);				
				// 先判断状态，如果是0，可预约；则直接赋予点击事件，如果不是0，则分别展示对应内容（已过期，已预约）
				if (flags[iy].equals("0"))
				{
					setItemCanAppointment(iv);
					iv.setOnClickListener(new MyOnClickListener(ll_item_list,
							item, tags, tags[iy], timeId, time, prices[iy],
							roomId[iy]));
				} else if (flags[iy].equals("-1"))// 已过期
				{
					// 只显示已过期view，其他的隐藏
					// 切换item状态
					setItemExpired(iv);
				} else if (flags[iy].equals("1"))// 已预约
				{
					// 只显示已预约view，其他的隐藏
					setItemAppointmented(iv);
				}
			}

			ll_item_list.addView(item);
			index++;
		}
	}

	/**
	 * 设置为可预约
	 */
	private void setItemCanAppointment(View v)
	{
		View v_checked = v.findViewWithTag("checked");
		v_checked.setVisibility(View.GONE);

		View v_not_checked = v.findViewWithTag("not_checked");
		v_not_checked.setVisibility(View.VISIBLE);

		View v_expired = v.findViewWithTag("expired");
		v_expired.setVisibility(View.GONE);

		View v_appointmented = v.findViewWithTag("appointmented");
		v_appointmented.setVisibility(View.GONE);
	}

	/**
	 * 设置为已过期
	 */
	private void setItemExpired(View v)
	{
		View v_checked = v.findViewWithTag("checked");
		v_checked.setVisibility(View.GONE);

		View v_not_checked = v.findViewWithTag("not_checked");
		v_not_checked.setVisibility(View.GONE);

		View v_expired = v.findViewWithTag("expired");
		v_expired.setVisibility(View.VISIBLE);

		View v_appointmented = v.findViewWithTag("appointmented");
		v_appointmented.setVisibility(View.GONE);
	}

	/**
	 * 设置为已预约
	 */
	private void setItemAppointmented(View v)
	{
		View v_checked = v.findViewWithTag("checked");
		v_checked.setVisibility(View.GONE);

		View v_not_checked = v.findViewWithTag("not_checked");
		v_not_checked.setVisibility(View.GONE);

		View v_expired = v.findViewWithTag("expired");
		v_expired.setVisibility(View.GONE);

		View v_appointmented = v.findViewWithTag("appointmented");
		v_appointmented.setVisibility(View.VISIBLE);
	}

	private class MyOnClickListener implements OnClickListener
	{
		View ll_item_list;
		String thisTag;
		String[] tags;
		View item;
		String timeId;
		String timeStr;
		String price;
		String roomId;

		MyOnClickListener(View ll_item_list, View item, String[] tags,
				String thisTag, String timeId, String timeStr, String price,
				String roomId)
		{
			this.ll_item_list = ll_item_list;
			this.item = item;
			this.tags = tags;
			this.thisTag = thisTag;
			this.timeId = timeId;
			this.timeStr = timeStr;
			this.price = price;
			this.roomId = roomId;
		}

		private final static int STATUS_CHECKED = 1;
		private final static int STATUS_NOT_CHECKED = 0;

		/**
		 * 获取当前动作，是要选中，还是要取消，选中返回1，取消返回0
		 * 
		 * @param v
		 * @return
		 */
		private int getAction(View v)
		{
			int x = -1;
			// 获取 当前动作
			View v_checked = v.findViewWithTag("checked");
			if (v_checked.getVisibility() == View.VISIBLE)
			{
				x = STATUS_NOT_CHECKED;
			} else
			{
				x = STATUS_CHECKED;
			}

			View v_not_checked = v.findViewWithTag("not_checked");
			if (v_not_checked.getVisibility() == View.VISIBLE)
			{
				x = STATUS_CHECKED;
			} else
			{
				x = STATUS_NOT_CHECKED;
			}
			return x;
		}

		private void switchToChecked(View v)
		{
			View v_checked = v.findViewWithTag("checked");
			v_checked.setVisibility(View.VISIBLE);
			View v_not_checked = v.findViewWithTag("not_checked");
			v_not_checked.setVisibility(View.GONE);
		}

		private void switchToNotChecked(View v)
		{
			View v_checked = v.findViewWithTag("checked");
			v_checked.setVisibility(View.GONE);
			View v_not_checked = v.findViewWithTag("not_checked");
			v_not_checked.setVisibility(View.VISIBLE);
		}

		@Override
		public void onClick(View v)
		{

			int x = getAction(v);

			String mapkey = date + "&" + timeId + "&" + timeStr;

			if (x == STATUS_CHECKED)// 如果动作是选中当前项
			{

				if (map.size() == 1)// 如果map中已经有一个选中的记录，当前又要增加一个选中记录的话，判断他们是否是同一个场地，如果不是同一个场地，则取消已经选中的那个；
				{
					String already_checked_item_tag = null;// 已经选中的item的tag
					String time = null;
					// 先判断他们是否是同一个场地
					for (String key : map.keySet())
					{
						time = key;
						already_checked_item_tag = map.get(key);
					}

					String sux = already_checked_item_tag.split("&")[0]
							.split("_")[0];
					String sux2 = thisTag.split("_")[0];

					if (sux.equals(sux2))// 先判断他们是否是同一个场地
					{
						// 如果是同一个场地，则不作处理
					} else
					{
						// 如果不是同一个场地，则取消之前的那个
						switchToNotChecked(ll_item_list
								.findViewWithTag(already_checked_item_tag
										.split("&")[0]));
						map.remove(time);
					}
				}

				if (map.size() == 2)
				{
					Toast.makeText(ctx, "最多选择2个时段,请先取消之后再选择其他",
							Toast.LENGTH_SHORT).show();
					return;
				}
				switchToChecked(v);
				for (String tag : tags)// 并且检测，如果当前场地被选择，其他场地自动触发点击事件
				{
					if (!tag.equals(thisTag))
					{
						switchToNotChecked(item.findViewWithTag(tag));
					}
				}
				map.put(mapkey, thisTag + "&" + price + "&" + roomId);
                
			} else
			{
				map.remove(mapkey);

				switchToNotChecked(v);
			}

			System.out.println("通过Map.keySet遍历key和value：");
			for (String key : map.keySet())
			{
				System.out.println("key= " + key + " and value= "
						+ map.get(key));
			}

		}
	}
}
