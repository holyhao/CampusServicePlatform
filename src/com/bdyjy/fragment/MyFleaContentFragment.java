/**
 *我的二手交易
 *create by songdebin  2016-04-05 
 *功能：我的二手交易的有关内容
 * */

package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.ImageShow;
import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.secondMarket.SecondMarket;
import com.bdyjy.entity.secondMarket.SecondMarketContentBean;
import com.bdyjy.entity.secondMarket.SecondMarketQueryBean;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.fragment.base.FragmentWithCollection;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.image.SmartImageView;

@SuppressLint("ValidFragment")
public class MyFleaContentFragment extends FragmentWithCollection
{

	private MainActivity ctx;
	private TextView tv_back;
	private int source=-1; // 记住传递过来的路径 ,在使用中如果这个变量的值依然是-1的话，说明从我的收藏界面过来

	private TextView tv_title;
	private TextView tv_goodsName;
	private TextView tv_price;
	private TextView tv_tel;
	private TextView tv_contacts;
	private TextView tv_description;
	
	Button btn_cancel;

	private SmartImageView sv1;//
	private SmartImageView sv2;//
	private SmartImageView sv3;
	private SmartImageView sv4;
	
	 private String id;
	 
	 
	 
	 Handler handler = null;
		private void initHandler()
		{
			handler = new Handler(ctx.getMainLooper())
			{
				@Override
				public void handleMessage(Message msg)
				{
					switch (msg.what)
					{
					case HandlerOrder.TOAST: //获取数据失败
						// TODO
						Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
						break;
					case HandlerOrder.PROCESSBAR_SHOW:
						ctx.showRoundProcessDialog();
						break;
					case HandlerOrder.PROCESSBAR_HIDE:
						ctx.hideRoundProcessDialog();
						break;
					case HandlerOrder.POST_OK:  //关键字提交成功   异步获取用于  搜索框功能
						String res;
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					     res=msg.getData().getString("body");
					     System.out.println("请求截止交易，返回的结果是"+res);
//					     SecondMarketQueryBean bean = JSON.parseObject(res,SecondMarketQueryBean.class);					
//						String app_result_key = bean.getApp_result_key();					
//						if ("0".equals(app_result_key))
//						{
//							List<SecondMarket> list = bean.getData().getRows();
//							MainActivity.secondMarket = list;				
//							handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
//							
//							return;
//						}else
//						{
//							toastMsg = "获取内容失败，请检查网络";
//							handler.sendEmptyMessage(HandlerOrder.TOAST);
//						} 
				      break;
					case HandlerOrder.POST_ERROR:  //获取失败
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
						toastMsg = "获取内容失败，请检查网络";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						break;
					}
				}
			};
		} 
	 
	 

	public MyFleaContentFragment(MainActivity ctx, int source)
	{
		super(ctx, CollectionTypeConst.SECOND_HAND_TRADE);
		this.ctx = ctx;
		this.source = source;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	
	/**********返回功能**********/
	private void Back(){ 
		ctx.jumpToMyFlea(source);	
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.myflea_content_fragment, null);
		
		initHandler();

		// 返回上一界面
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToMyFlea(source);
			}
		});

		tv_title = (TextView) view.findViewById(R.id.tv_title_fleacontent_item);
		tv_goodsName = (TextView) view
				.findViewById(R.id.tv_goodsname_fleacontent_item);
		tv_price = (TextView) view.findViewById(R.id.tv_price_fleacontent_item);
		tv_tel = (TextView) view.findViewById(R.id.tv_tel_fleacontent_item);
		tv_contacts = (TextView) view
				.findViewById(R.id.tv_contacts_fleacontent_item);
		tv_description = (TextView) view
				.findViewById(R.id.tv_description_fleacontent_item);

		SecondMarket second = MainActivity.secondmarketcontent;

		tv_title.setText(second.getTitle());
		tv_goodsName.setText(second.getGoodsName());
		tv_price.setText(second.getPrice());
		tv_tel.setText(second.getContacts());
		tv_contacts.setText(second.getTel());
		tv_description.setText(second.getDescription());
		id=second.getId();

		if (second.getAttArry().size() == 0) // 如果为空 图片就不展示了
		{

			View hiddenView = (View) view.findViewById(R.id.rl_picture_show); // 在hidden_view.xml中hidden_layout是root
																				// layout
			if (null != hiddenView)
			{
				ViewGroup parent = (ViewGroup) hiddenView.getParent();
				parent.removeView(hiddenView);
			}

		} else
		{

			try
			{
				String filePath = second.getAttArry().get(0).getFilePath()
						.toString().trim(); // 因为通过请求Content 返回的attArry为空的
											// ，所以暂时使用 通过请求列表获取的图片地址来调试
				String attachmentPrefix = second.getAttachmentPrefix()
						.toString().trim();
				String http = attachmentPrefix + filePath;

				// System.out.println("限制的大厦发发地址"+http);

				sv1 = (SmartImageView) view.findViewById(R.id.iv_show1);
			//	sv1.setImageUrl(http, R.drawable.default_image);

				Glide.with(ctx)  
							     .load(http) 
							     .dontAnimate()
							     .placeholder(R.drawable.default_image)
							     .fitCenter()
							     .error(R.drawable.default_image)
							     .diskCacheStrategy(DiskCacheStrategy.RESULT)
							     .into(sv1); 
				sv1.setOnClickListener(new OnClickListener()
				{
					public void onClick(View view)
					{
						ImageShow(0);
					}
				});
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				String filePath = second.getAttArry().get(1).getFilePath()
						.toString().trim(); // 因为通过请求Content 返回的attArry为空的
											// ，所以暂时使用 通过请求列表获取的图片地址来调试
				String attachmentPrefix = second.getAttachmentPrefix()
						.toString().trim();
				String http = attachmentPrefix + filePath;

				sv2 = (SmartImageView) view.findViewById(R.id.iv_show2);
				//sv2.setImageUrl(http, R.drawable.default_image);
				Glide.with(ctx)  
			     .load(http) 
			     .dontAnimate()
			     .placeholder(R.drawable.default_image)
			     .fitCenter()
			     .error(R.drawable.default_image)
			     .diskCacheStrategy(DiskCacheStrategy.RESULT)
			     .into(sv2);
				sv2.setOnClickListener(new OnClickListener()
				{
					public void onClick(View view)
					{
						ImageShow(1);
					}
				});
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				String filePath = second.getAttArry().get(2).getFilePath()
						.toString().trim(); // 因为通过请求Content 返回的attArry为空的
											// ，所以暂时使用 通过请求列表获取的图片地址来调试
				String attachmentPrefix = second.getAttachmentPrefix()
						.toString().trim();
				String http = attachmentPrefix + filePath;

				sv3 = (SmartImageView) view.findViewById(R.id.iv_show3);
				//sv3.setImageUrl(http, R.drawable.default_image);
				Glide.with(ctx)  
			     .load(http) 
			     .dontAnimate()
			     .placeholder(R.drawable.default_image)
			     .fitCenter()
			     .error(R.drawable.default_image)
			     .diskCacheStrategy(DiskCacheStrategy.RESULT)
			     .into(sv3);
				sv3.setOnClickListener(new OnClickListener()
				{
					public void onClick(View view)
					{
						ImageShow(2);
					}
				});
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				String filePath = second.getAttArry().get(3).getFilePath()
						.toString().trim(); // 因为通过请求Content 返回的attArry为空的
											// ，所以暂时使用 通过请求列表获取的图片地址来调试
				String attachmentPrefix = second.getAttachmentPrefix()
						.toString().trim();
				String http = attachmentPrefix + filePath;

				sv4 = (SmartImageView) view.findViewById(R.id.iv_show4);
			//	sv4.setImageUrl(http, R.drawable.default_image);
				Glide.with(ctx)  
			     .load(http) 
			     .dontAnimate()
			     .placeholder(R.drawable.default_image)
			     .fitCenter()
			     .error(R.drawable.default_image)
			     .diskCacheStrategy(DiskCacheStrategy.RESULT)
			     .into(sv4);
				sv4.setOnClickListener(new OnClickListener()
				{
					public void onClick(View view)
					{
						ImageShow(3);
					}
				});
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		// 以下到结尾是收藏相关的代码
//		super.setMainView(view);
//		dealCollect(second.getId(), second.getIsCollect(), second.getTitle());
		if(source==-1)//保持初始值，说明是从我的收藏中进入的
		{
			super.setMainView(view);
			dealCollect(second.getId(), second.getIsCollect(), second.getTitle());	
		}
		else//从我的二手交易列表进入的
		{
			Button btn_collect0=(Button)view.findViewById(R.id.ll_not_collect);
			Button btn_collect1=(Button)view.findViewById(R.id.ll_collect);
			btn_collect0.setVisibility(View.GONE);
			btn_collect1.setVisibility(View.GONE);
		    btn_cancel=(Button)view.findViewById(R.id.ll_cansol);
			btn_cancel.setVisibility(View.VISIBLE);
			if(second.getStatus()==1){
				btn_cancel.setText("已截止");
				btn_cancel.setBackgroundColor(0xFFD6D6D6);
			}else{
			btn_cancel.setOnClickListener(new OnClickListener()
				{
					public void onClick(View view)
					{
					   //Toast.makeText(ctx, "截止交易", 1).show();
					   cansol(id);
					}
				});
			}
		}
		return view;
	}
	
	private void  cansol(final String id){
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);//打开进度条
		String token = (String) SPUtils.get(ctx, "token", "");
		String singnature = (String) SPUtils.get(ctx, "singnature", "");
		String st = (String) SPUtils.get(ctx, "st", "");
		
		String http = "/admin/secondhanddeal/updateStatus.do?"
				+"&status="+1+"&id="+id
			    +"&token=" + token + "&singnature=" + singnature + "&st=" + st;
		try {
			String res = OkHttpUtils.getInstance().doGet(ctx,http);
			//System.out.println("程序正常运行，返回的数为"+res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("程序异常");
			e.printStackTrace();
		}
		
		getMyFleaContent(id);
		if(MainActivity.secondmarketcontent.getStatus()==1){
			btn_cancel.setText("已截止");	
			btn_cancel.setBackgroundColor(0xFFD6D6D6);
			btn_cancel.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
				  
				}
			});
		}
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
	}
	

	
	private void getMyFleaContent(final String id)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);//打开进度条
				// 试试get请求
				String res = null;

				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");

				try
				{
					res = OkHttpUtils.getInstance().doGet(
							ctx,
							"/admin//secondhanddeal/findById.do?id=" + id + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);

				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取内容失败,请检查你的网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);//隐藏进度条
				}
                   
			 
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);//隐藏进度条
			    System.out.println("content的内容是"+res.trim());
				SecondMarketContentBean bean = JSON.parseObject(res,
						SecondMarketContentBean.class);

				// System.out.println("xxxx:" + bean.getApp_result_key());

				// 返回值将会是JSON格式的数据，我要在这里解析
				if (res.trim().length() == 0)
				{
					toastMsg = "获取内容失败,请检查你的网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
												// 那么我要在这里取得新闻的标题，内容，显示在界面上
				{
					SecondMarket b = bean.getEntity();
					MainActivity.secondmarketcontent = b;
					
					//跳转至内容页
					ctx.jumpToMyFleaContent(source);
				}
				else{
					toastMsg = "获取内容失败,请检查你的网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}
				
			}
		}.start();			
}
	

	private ImageShow mAdView1;
	private ArrayList<String> mImageUrl = null;
	private ArrayList<String> mImageTitle = null;

	private ImageShow.ImageCycleViewListener mAdCycleViewListener1 = new ImageShow.ImageCycleViewListener()
	{
		@Override
		public void onImageClick(int position, View imageView)
		{
			// Toast.makeText(ctx, mImageUrl.get(position) + position,
			// 1).show();
		}
	};

	public void ImageShow(int currentItem)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_image1);
		// SmartImageView sv;
		// sv=(SmartImageView)alertDialog.getWindow().findViewById(R.id.iv_show);
		SecondMarket second = MainActivity.secondmarketcontent;
		String filePath = second.getAttArry().get(0).getFilePath().toString()
				.trim(); // 因为通过请求Content 返回的attArry为空的 ，所以暂时使用 通过请求列表获取的图片地址来调试
		String attachmentPrefix = second.getAttachmentPrefix().toString()
				.trim();
		String http = attachmentPrefix + filePath;
		// sv.setImageUrl(http, R.drawable.list_default_image);

		List<attArryData> attArry = second.getAttArry();
		mImageTitle = new ArrayList<String>();
		mImageUrl = new ArrayList<String>();
		// 循环添加图片
		for (int i = 0; i < attArry.size(); i++)
		{
			filePath = second.getAttArry().get(i).getFilePath().toString()
					.trim(); // 因为通过请求Content 返回的attArry为空的 ，所以暂时使用
								// 通过请求列表获取的图片地址来调试
			attachmentPrefix = second.getAttachmentPrefix().toString().trim();
			http = attachmentPrefix + filePath;
			mImageUrl.add(http);
			mImageTitle.add("维护报修测试的的的的的");
		}

		mAdView1 = (ImageShow) alertDialog.getWindow().findViewById(
				R.id.iv_showx);
		// mAdView1.pushImageCycle();
		mAdView1.setImageResources(mImageUrl, mImageTitle,
				mAdCycleViewListener1, 2, currentItem);

	}
}
