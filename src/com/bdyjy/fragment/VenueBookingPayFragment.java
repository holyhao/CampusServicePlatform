/**
 * VenueBookingFragment.java[v 1.0.0]
 * class:com.bdyjy.fragment,VenueBookingFragment
 * 周航 create at 2016-4-5 下午2:50:41
 */
package com.bdyjy.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.venue.CommitResultBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;
import com.bdyjy.util.alipay.PayResult;
import com.bdyjy.util.alipay.SignUtils;

/**
 * com.bdyjy.fragment.VenueBookingFragment
 * 
 * @author 周航<br/>
 *         create at 2016-4-5 下午2:50:41
 */
public class VenueBookingPayFragment extends Fragment
{
	MainActivity ctx;
	TimeCount timeCount;
	private Handler handler = null;
	String[] timeIds = new String[2];// 时间段id
	String roomId = null;// 场馆id
	String appDate = null;

	private TextView tv_venue_name, tv_booking_date, tv_time_1, tv_time_2,
			tv_fee;
    private Button payButton;
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
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.SDK_PAY_FLAG:
				{
					PayResult payResult = new PayResult((String) msg.obj);
					/**
					 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/
					 * doc2/ detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=
					 * 103665& docType=1) 建议商户依赖异步通知
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					// 在这里要调用支付结果验证的请求
					// TODO
					System.out.println(resultInfo);

					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000"))
					{
						Toast.makeText(ctx, "支付成功", Toast.LENGTH_SHORT).show();
					} else
					{
						// 判断resultStatus 为非"9000"则代表可能支付失败
						// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000"))
						{
							Toast.makeText(ctx, "支付结果确认中", Toast.LENGTH_SHORT)
									.show();

						} else
						{
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(ctx, "支付失败", Toast.LENGTH_SHORT)
									.show();

						}
					}
					break;
				}
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
	public VenueBookingPayFragment(MainActivity ctx)
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
		ctx.jumpToVenueBookingMyFregment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.venue_booking_pay, null);
		tv_venue_name = (TextView) view.findViewById(R.id.tv_venue_name);
		tv_booking_date = (TextView) view.findViewById(R.id.tv_booking_date);
		tv_time_1 = (TextView) view.findViewById(R.id.tv_time_1);
		tv_time_2 = (TextView) view.findViewById(R.id.tv_time_2);
		tv_fee = (TextView) view.findViewById(R.id.tv_fee);

		TextView tv = (TextView) view.findViewById(R.id.tv_title);
		tv.setText("场馆预订-支付");

		view.findViewById(R.id.tv_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						showDialog();
					}
				});
		// 隐藏返回键，返回键没有意义
		// view.findViewById(R.id.tv_back).setVisibility(View.INVISIBLE);

		view.findViewById(R.id.ll_my).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				toastMsg = "您取消了支付，你可以在“我的订单”查看你的所有订单记录";
				handler.sendEmptyMessage(HandlerOrder.TOAST);
				ctx.jumpToVenueBookingMyFregment();
			}
		});

		view.findViewById(R.id.btn_pay_now).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						// 支付提交
						toastMsg = "暂未开通支付，敬请期待";
						handler.sendEmptyMessage(HandlerOrder.TOAST);

						// 在这里调用支付宝的支付代码
						// TODO
//						String venue_name = tv_venue_name.getText().toString();
//						String date = tv_booking_date.getText().toString();
//						String time_1 = tv_time_1.getText().toString();
//						String time_2 = tv_time_2.getText().toString();
//						String fee = tv_fee.getText().toString();
//
//						if (fee.contains("￥"))
//						{
//							fee = fee.substring(1);
//						}
//
//						// 球场名，以及时间段，还有价格
//						pay(venue_name + "_" + date, time_1 + ";" + time_2, fee);
					}
				});
		payButton=(Button)view.findViewById(R.id.btn_pay_now);
		
		initData(view);
		timeCount = new TimeCount(5000, 1000);
		timeCount.start();
		return view;
	}

	private void showDialog()
	{
		// 创建一个AlertDialog.Builder对象
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(
				"支付尚未完成，您要返回首页么").setCancelable(false);

		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						ctx.jumpToFirstPageFregment();
						timeCount.cancel();
					}
				});

		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

	private void initData(View view)
	{
		// 初始化界面：从
		Map<String, String> data = MainActivity.venueBookingResultMap;
		String venueName = null;

		String[] timeStrs = new String[2];

		int fee = 0;
		String s = null;
		int i = 0;
		for (String key : data.keySet())
		{
			
			String value = data.get(key);
			// key: 2016-04-06&1834B71BABF74EA9ABC72647CC185DE4&20:30 - 21:00

			appDate = key.split("&")[0];
			String timeStr = key.split("&")[2];
			timeStrs[i] = timeStr;
			timeIds[i] = key.split("&")[1];
			i++;

			// value: 足球馆_3&5.00&A77387170AC340DF9EF81D493B54DD40
			venueName = value.split("&")[0].split("_")[0];
			if(MainActivity.personalInfo.getPermission() == 2)
			{
				//学生
				fee+=Integer.parseInt(value.split("&")[1].substring(0,1));
				
			}else
			{
				//教师
            	fee += (Integer.parseInt(value.split("&")[1].substring(0,1)))*2;
			}
			
			
			
			
			roomId = value.split("&")[2];
		}

		// 开始赋值到界面
		TextView tv_venue_name = (TextView) view
				.findViewById(R.id.tv_venue_name);
		tv_venue_name.setText(venueName);

		TextView tv_booking_date = (TextView) view
				.findViewById(R.id.tv_booking_date);
		tv_booking_date.setText(appDate);

		// 场地时段
		TextView tv_time_1 = (TextView) view.findViewById(R.id.tv_time_1);
		TextView tv_time_2 = (TextView) view.findViewById(R.id.tv_time_2);

		if (timeStrs[0] != null && timeStrs[0].length() > 0)
		{
			tv_time_1.setText(timeStrs[0]);
		} else
		{
			view.findViewById(R.id.ll_time_1).setVisibility(View.GONE);
		}

		if (timeStrs[1] != null && timeStrs[1].length() > 0)
		{
			tv_time_2.setText(timeStrs[1]);
		} else
		{
			view.findViewById(R.id.ll_time_2).setVisibility(View.GONE);
		}

		TextView tv_fee = (TextView) view.findViewById(R.id.tv_fee);
		tv_fee.setText("￥" + fee);

		// 初始化界面完成之后，生成未完成订单
		String order_data_from = MainActivity.order_data_from;
		if (order_data_from.equals("VenueBookingMyFragment"))// 从我的订单界面过来的话，不需要做操作
		{

		} else if (order_data_from.equals("VenueBookingFragment"))// 从场馆选择页面过来的话，要先生成订单
		{
			commitBooking();
		}
		
	}

	String toastMsg;

	/**
	 * 线程：提交预订
	 * 
	 * @param id
	 */
	private void commitBooking()
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

				if (timeIds[0] != null && timeIds[0].length() > 0)
				{
					timeStr.append("&timeIds[0]=" + timeIds[0]);
				}
				if (timeIds[1] != null && timeIds[1].length() > 0)
				{
					timeStr.append("&timeIds[1]=" + timeIds[1]);
				}

				String httpStr = "/admin/appointmenrecord/addSave.do?roomId="
						+ roomId + timeStr.toString() + "&appDate=" + appDate
						+ "&token=" + token + "&singnature=" + singnature
						+ "&st=" + st;

				System.out.println(httpStr);

				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(ctx, httpStr);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					System.out.println(res);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取数据失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				CommitResultBean bean = JSON.parseObject(res,
						CommitResultBean.class);
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
					// handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
					toastMsg = "订单已生成，请支付！";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				} else
				{
					toastMsg = bean.getSystem_result_message_key();
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}
			}
		}.start();
	}

	// 以下是集成支付宝相关功能代码
	// 商户PID
	public static final String PARTNER = "2088511831644162";
	// 商户收款账号
	public static final String SELLER = "fso@pkusz.edu.cn";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMUq65KcuJZjXnqB"
			+ "7GQ7hV2VTZfAiKcynD0TLYHIozoL6A3HnnA5X+dbsJk8OyqBKe6ljWzu1F/nljye"
			+ "fS2CT4xPxim1wnUpPT0aMnFifqTokIlgpKsMaoNtrAUJOGsxJgB3yTXJgS28TetN"
			+ "M5y5T9k6YPKrpvOTEqpz5XbwZLPvAgMBAAECgYBkrFiigMFDkLNyDBrexwF+2jhH"
			+ "Tvi3noDmMn+e8ApgHio7tvxwQut0H34ZkAaJ2m/FueRvhbcMzRwKEcbK0Om6TbTH"
			+ "T5zOUkuwTLIjgrEj6oRyGDPR+fgsFd7NyrwNwawDw5t4n3xtHThlhig9j3+MtGR8"
			+ "qj4dabNhbkwqpoBxoQJBAPYDqNQ8pO507ZPyxmJ4DtwDmDL1YRbTU/d08JJRlbWl"
			+ "CeD0jwIapvnvu0hlzlghCeu/ctCq+/aZOcjrEpB6TD8CQQDNK7Gxsz20KzNkO31G"
			+ "oel2U+HGCEKEa18+rtL3dR/n9JAMWhPbOsMu/WIlhKYqD+DZfx5qlq5BJbrsb8cc"
			+ "eGxRAkAwMgFWKtdI2+FbNZmF2u1WTuH3QbkWhqoRdXhXweHpZv6rcl291MjXeA6h"
			+ "nUgyXa6a37O3i3e3FMeMQ/ksErlzAkAmJxjtHZK83Ue6POHH77X1XhXe+Iw+CaV1"
			+ "h5cNk0cyoCTlVx7YJq5DH7VPmsI3kipS57OlSI3w6W5AJ20UW0RxAkBIHSSQmC1K"
			+ "pfjLMEZYnkYrVT5tzjmrtch94dG128gqa5TNVUDmHQe+PkhggrSZmtNSzo0Bzy9p"
			+ "CX5b5Avm+6qY";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFKuuSnLiWY156gexkO4VdlU2X"
			+ "wIinMpw9Ey2ByKM6C+gNx55wOV/nW7CZPDsqgSnupY1s7tRf55Y8nn0tgk+MT8Yp"
			+ "tcJ1KT09GjJxYn6k6JCJYKSrDGqDbawFCThrMSYAd8k1yYEtvE3rTTOcuU/ZOmDy"
			+ "q6bzkxKqc+V28GSz7wIDAQAB";

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String goodsTitle, String description, String price)
	{
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
				|| TextUtils.isEmpty(SELLER))
		{
			toastMsg = "系统参数错误！";
			handler.sendEmptyMessage(HandlerOrder.TOAST);
			return;
		}
		// String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
		String orderInfo = getOrderInfo(goodsTitle, description, price);

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try
		{
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable()
		{

			@Override
			public void run()
			{
				// 构造PayTask 对象
				PayTask alipay = new PayTask(ctx);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = HandlerOrder.SDK_PAY_FLAG;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price)
	{

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo()
	{
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content)
	{
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType()
	{
		return "sign_type=\"RSA\"";
	}
	
	class TimeCount extends CountDownTimer{

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			payButton.setClickable(false);
			payButton.setText("在" + millisUntilFinished / 1000 + "后跳转到我的预定");
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			payButton.setText("开始跳转");
			payButton.setClickable(true);
			ctx.jumpToVenueBookingMyFregment();
		}
		
	}
	
	
}
