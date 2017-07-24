/**
 *失物招领的内容查看界面
 *create by songdebin  2016-04-05 
 **/

package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.ImageCycleView;
import com.bdyjy.custom.view.ImageShow;
import com.bdyjy.custom.view.ImageCycleView.ImageCycleViewListener;
import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.lostfind.LostFind;
import com.bdyjy.entity.lostfind.LostFindContentBean;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.fragment.base.FragmentWithCollection;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.image.SmartImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RelativeLayout;

@SuppressLint("ValidFragment")
public class MyLostFindContentFragment extends FragmentWithCollection
{

	private MainActivity ctx;
	private TextView tv_back;
	private Button btn_my;
	private int source=-1;
	private Button btn_cancel;

	private TextView title;
	private TextView name;
	private TextView contacts;
	private TextView tel;
	private TextView description;
	
	 private String id;

	// 图片展示
	private SmartImageView sv1;//
	private SmartImageView sv2;//
	private SmartImageView sv3;
	private SmartImageView sv4;

	// 构造器
	public MyLostFindContentFragment(MainActivity ctx, int source)
	{
		super(ctx, CollectionTypeConst.LOSTANDFOUND);
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
		ctx.jumpToMyLost(source);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.my_lostfind_content_fragment,
				null);

		// 返回上一界面
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToMyLost(source);
			}
		});

		title = (TextView) view.findViewById(R.id.tv_title_lostfind_content);
		contacts = (TextView) view
				.findViewById(R.id.tv_contacts_lostfind_content);
		tel = (TextView) view.findViewById(R.id.tv_tel_lostfind_content);
		description = (TextView) view
				.findViewById(R.id.tv_description_lostfind_content);

		final LostFind lf = MainActivity.lostfindcontent;

		title.setText(lf.getTitle());
		contacts.setText(lf.getContacts());
		tel.setText(lf.getTel());
		description.setText(lf.getDescription());
		id=lf.getId();

		if (lf.getAttArry().size() == 0) // 如果为空 图片就不展示了
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
				String filePath = lf.getAttArry().get(0).getFilePath()
						.toString().trim(); // 因为通过请求Content 返回的attArry为空的
											// ，所以暂时使用 通过请求列表获取的图片地址来调试
				String attachmentPrefix = lf.getAttachmentPrefix().toString()
						.trim();
				String http = attachmentPrefix + filePath;

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
				String filePath = lf.getAttArry().get(1).getFilePath()
						.toString().trim(); // 因为通过请求Content 返回的attArry为空的
											// ，所以暂时使用 通过请求列表获取的图片地址来调试
				String attachmentPrefix = lf.getAttachmentPrefix().toString()
						.trim();
				String http = attachmentPrefix + filePath;

				sv2 = (SmartImageView) view.findViewById(R.id.iv_show2);
			//	sv2.setImageUrl(http, R.drawable.default_image);
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
				String filePath = lf.getAttArry().get(2).getFilePath()
						.toString().trim(); // 因为通过请求Content 返回的attArry为空的
											// ，所以暂时使用 通过请求列表获取的图片地址来调试
				String attachmentPrefix = lf.getAttachmentPrefix().toString()
						.trim();
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
				String filePath = lf.getAttArry().get(3).getFilePath()
						.toString().trim(); // 因为通过请求Content 返回的attArry为空的
											// ，所以暂时使用 通过请求列表获取的图片地址来调试
				String attachmentPrefix = lf.getAttachmentPrefix().toString()
						.trim();
				String http = attachmentPrefix + filePath;

				sv4 = (SmartImageView) view.findViewById(R.id.iv_show4);
				//sv4.setImageUrl(http, R.drawable.default_image);
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
		if(source==-1){
		super.setMainView(view);
		dealCollect(lf.getId(), lf.getIsCollect(), lf.getTitle());
		}else{
			Button btn_collect0=(Button)view.findViewById(R.id.ll_not_collect);
			Button btn_collect1=(Button)view.findViewById(R.id.ll_collect);
			btn_collect0.setVisibility(View.GONE);
			btn_collect1.setVisibility(View.GONE);
		    btn_cancel=(Button)view.findViewById(R.id.ll_cansol);
			btn_cancel.setVisibility(View.VISIBLE);
			if(lf.getStatus()==1){
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
	
	
	
	private void cansol(final String id){
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);//打开进度条
		String token = (String) SPUtils.get(ctx, "token", "");
		String singnature = (String) SPUtils.get(ctx, "singnature", "");
		String st = (String) SPUtils.get(ctx, "st", "");
		
		String http = "/admin/lostfound/updateStatus.do?"
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
		getMyLostFindContent(id);
		if(MainActivity.lostfindcontent.getStatus()==1){
			btn_cancel.setText("已截止");	
			btn_cancel.setBackgroundColor(0xFFD6D6D6);
			btn_cancel.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
				  
				}
			});
		}	
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);//打开进度条
	}
	
	
	private void getMyLostFindContent(final String id)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
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
							"/admin/lostfound/findById.do?id=" + id + "&token="
									+ token + "&singnature=" + singnature
									+ "&st=" + st);

					LostFindContentBean bean = JSON.parseObject(res,
							LostFindContentBean.class);

					if (res.trim().length() == 0)
					{
						toastMsg = "获取失败,请重试";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					}

					String app_result_key = bean.getApp_result_key();

					if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
													// 那么我要在这里取得新闻的标题，内容，显示在界面上
					{
						LostFind b = bean.getEntity();
						MainActivity.lostfindcontent = b;
						// 跳转到内容页
						ctx.jumpToMyLostContent(source);

					} else
					{
						toastMsg = "获取内容失败，请检查网络";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					}

				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取失败，请重试";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				}
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
			}
		}.start();

	}

	/**
	 * 
	 * 
	 * 
	 * ********************************************/

	private ImageCycleView mAdView;
	private ArrayList<String> mImageUrl = null;
	private ArrayList<String> mImageTitle = null;

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener()
	{
		@Override
		public void onImageClick(int position, View imageView)
		{
			// Toast.makeText(ctx, mImageUrl.get(position) + position,
			// 1).show();
		}
	};

	private ImageShow mAdView1;
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
		LostFind lf = MainActivity.lostfindcontent;
		String filePath = lf.getAttArry().get(0).getFilePath().toString()
				.trim(); // 因为通过请求Content 返回的attArry为空的 ，所以暂时使用 通过请求列表获取的图片地址来调试
		String attachmentPrefix = lf.getAttachmentPrefix().toString().trim();
		String http = attachmentPrefix + filePath;
		// sv.setImageUrl(http, R.drawable.list_default_image);

		List<attArryData> attArry = lf.getAttArry();
		mImageTitle = new ArrayList<String>();
		mImageUrl = new ArrayList<String>();
		// 循环添加图片
		for (int i = 0; i < attArry.size(); i++)
		{
			filePath = lf.getAttArry().get(i).getFilePath().toString().trim(); // 因为通过请求Content
																				// 返回的attArry为空的
																				// ，所以暂时使用
																				// 通过请求列表获取的图片地址来调试
			attachmentPrefix = lf.getAttachmentPrefix().toString().trim();
			http = attachmentPrefix + filePath;
			mImageUrl.add(http);
			mImageTitle.add("");
		}

		mAdView1 = (ImageShow) alertDialog.getWindow().findViewById(
				R.id.iv_showx);
		// mAdView1.pushImageCycle();
		mAdView1.setImageResources(mImageUrl, mImageTitle,
				mAdCycleViewListener1, 2, currentItem);

	}

}
