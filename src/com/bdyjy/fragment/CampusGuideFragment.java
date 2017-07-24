package com.bdyjy.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.LocationActivity;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.ImageCycleView;
import com.bdyjy.custom.view.ImageCycleView.ImageCycleViewListener;
import com.bdyjy.entity.campusguide.CampusGuides;
import com.bdyjy.entity.campusguide.CampusGuidesContentQueryResultBean;
import com.bdyjy.entity.complaint.ComplaintContentQueryResultBean;
import com.bdyjy.entity.complaint.Complaints;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

import android.R.integer;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author holy 校园指南
 *
 */
public class CampusGuideFragment extends Fragment {
	
	private TextView tv_back;
	private MainActivity ctx;
	private Handler handler;
	private String toastMsg;
	
	private void initHandler()
	{
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case HandlerOrder.TOAST:
					// TODO
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.UPDATE_LISTVIEW:
//					listView1.onLoad();
//					loadData();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				}
			}
		};

	}
	
	
	public CampusGuideFragment(MainActivity ctx)
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
		ctx.jumpToLakesideFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.campusguide_fragment, null);
		
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{  
				ctx.jumpToLakesideFragment();
			}
		});
		//对所有按钮设置监听
		

		OnClickListener clickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int type =0;
				switch (v.getId()) {
				case R.id.btn_guide1:
					type=4;
				Intent intent=new Intent(ctx,LocationActivity.class);
                startActivity(intent);
					break;
				case R.id.btn_guide2:
					type=2;
					MainActivity.School_Life="住宿信息";
					break;
				case R.id.btn_guide3:
					MainActivity.School_Life="餐饮服务";
					type=1;
					break;
				case R.id.btn_guide4:
					MainActivity.School_Life="快递服务";
					type=5;
					break;
				case R.id.btn_guide5:
					MainActivity.School_Life="体育锻炼";
					type=6;
					break;
				case R.id.btn_guide6:
					MainActivity.School_Life="医疗服务";
					type=3;
					break;
				case R.id.btn_guide7:
					MainActivity.School_Life="交通信息";
					type=7;
					break;
				default:
					break;
				}
				
				if (type!=4) {
					getContent(type);
				}
				
				//ctx.jumpToCampusGuideContentFragment();
			}
		};
		//校内新闻
		Button btn_map=(Button)view.findViewById(R.id.btn_guide1);
		Drawable drawable=getResources().getDrawable(R.drawable.guide_map_map);
		drawable.setBounds(0, 0, (int)getResources().getDimension(R.dimen.x72), (int)getResources().getDimension(R.dimen.y50));
	    btn_map.setCompoundDrawables(null, drawable, null, null);
	    drawable=null;
		btn_map.setOnClickListener(clickListener);
		
		//住宿信息
		Button btn_recommadation=(Button)view.findViewById(R.id.btn_guide2);
		drawable=getResources().getDrawable(R.drawable.guide_recommadation_guide);
		drawable.setBounds(0, 0, (int)getResources().getDimension(R.dimen.x72),(int)getResources().getDimension(R.dimen.y50));
		btn_recommadation.setCompoundDrawables(null,drawable,null,null); 
		drawable=null;
		btn_recommadation.setOnClickListener(clickListener);
		
		//餐饮服务
		Button btn_catering=(Button)view.findViewById(R.id.btn_guide3);
		btn_catering.setOnClickListener(clickListener);
		drawable=getResources().getDrawable(R.drawable.guide_catering_cater);
		drawable.setBounds(0, 0,(int)getResources().getDimension(R.dimen.x72),(int)getResources().getDimension(R.dimen.y50));
		btn_catering.setCompoundDrawables(null, drawable, null, null);
		drawable=null;
		
	    //快递服务
		Button btn_express=(Button)view.findViewById(R.id.btn_guide4);
		drawable=getResources().getDrawable(R.drawable.guide_express_express);
		drawable.setBounds(0, 0,(int)getResources().getDimension(R.dimen.x72),(int)getResources().getDimension(R.dimen.y50));
		btn_express.setCompoundDrawables(null, drawable, null, null);
		drawable=null;
		btn_express.setOnClickListener(clickListener);
		
		//体育锻炼 
		Button btn_exercise=(Button)view.findViewById(R.id.btn_guide5);
		drawable=getResources().getDrawable(R.drawable.guide_exercise_exercise);
		drawable.setBounds(0, 0,(int)getResources().getDimension(R.dimen.x72),(int)getResources().getDimension(R.dimen.y50));
		btn_exercise.setCompoundDrawables(null, drawable, null, null);
		drawable=null;
		btn_exercise.setOnClickListener(clickListener);
		
		//医疗服务  
		Button btn_medical=(Button)view.findViewById(R.id.btn_guide6);
		drawable=getResources().getDrawable(R.drawable.guide_medical_medical);
		drawable.setBounds(0, 0, (int)getResources().getDimension(R.dimen.x72),(int)getResources().getDimension(R.dimen.y50));
		btn_medical.setCompoundDrawables(null, drawable, null, null);
		drawable=null; 
		btn_medical.setOnClickListener(clickListener); 
		 
		//交通信息
		Button btn_traffic=(Button)view.findViewById(R.id.btn_guide7);
		drawable=getResources().getDrawable(R.drawable.guide_traffic_traffic);
		drawable.setBounds(0, 0, (int)getResources().getDimension(R.dimen.x72),(int)getResources().getDimension(R.dimen.y50));
		btn_traffic.setCompoundDrawables(null, drawable, null, null);
		drawable=null;
		btn_traffic.setOnClickListener(clickListener);
		
		initCycleView(view);


		return view;
		
	}
	
	
	// 在这里封装从后台一个获取内容的方法
		private void getContent(final int type)
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
						res = OkHttpUtils.getInstance().doGet(
								ctx,
								"/admin/lifeguide/findByType.do?type="+type
								+ "&token="+ token 
								+ "&singnature=" + singnature
								+ "&st=" + st);
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					} catch (Exception e)
					{
						e.printStackTrace();
						toastMsg = "获取内容失败";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					}
					System.out.println(type+"请求校园生活指南内容返回的结果是：" + res.trim());
					CampusGuidesContentQueryResultBean bean = JSON.parseObject(res,
							CampusGuidesContentQueryResultBean.class);

					// System.out.println("xxxx:" + bean.getApp_result_key());

					// 返回值将会是JSON格式的数据，我要在这里解析
					if (res.trim().length() == 0)
					{
						toastMsg = "获取内容失败";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					}
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
													// 那么我要在这里取得新闻的标题，内容，显示在界面上
					{
						CampusGuides b = bean.getEntity();
						MainActivity.campusGuides= b;
						Log.i("NewMessage",MainActivity.campusGuides.getContent());
						System.out.println("news:" + b);
						
					}
					ctx.jumpToCampusGuideContentFragment();
				}
			}.start();
		}
	
	
	
	// 轮播图相关
		private ImageCycleView mAdView;
		private ArrayList<String> mImageUrl = null;

		private ArrayList<String> mImageTitle = null;
		private String imageUrl1 = "http://pic.baike.soso.com/p/20131115/20131115144549-353378746.jpg";
		private String imageUrl2 = "http://ww1.sinaimg.cn/large/832a46c7jw1dz6cgksg52j.jpg";
		private String imageUrl3 = "https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/news/q=100/sign=32bf684cf203918fd1d139ca613c264b/3b87e950352ac65c41a059dffef2b21192138af0.jpg";
		
		
		
		
		private String imageTitle1 = "广州";
		private String imageTitle2 = "北京";
		private String imageTitle3 = "深圳";

		public int stype = 1;

		/**
		 * 初始化轮播图
		 * 
		 * @param view
		 */
		private void initCycleView(View view)
		{
			mImageUrl = new ArrayList<String>();
			mImageUrl.add(imageUrl1);
			mImageUrl.add(imageUrl2);
			mImageUrl.add(imageUrl3);

			mImageTitle = new ArrayList<String>();
			mImageTitle.add(imageTitle1);
			mImageTitle.add(imageTitle2);
			mImageTitle.add(imageTitle3);
			mAdView = (ImageCycleView) view.findViewById(R.id.ad_view);
			mAdView.setImageResources(mImageUrl, mImageTitle, mAdCycleViewListener,
					stype);
		}

		private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener()
		{
			@Override
			public void onImageClick(int position, View imageView)
			{
//				Toast.makeText(ctx, mImageUrl.get(position) + position, 1).show();
			}
		};	

		
}
