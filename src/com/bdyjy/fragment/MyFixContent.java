/**
 *我的维护报修的内容查看界面
 *create by songdebin  2016-04-05 
 * */

package com.bdyjy.fragment;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.custom.view.ImageCycleView;
import com.bdyjy.custom.view.ImageShow;
import com.bdyjy.custom.view.ImageCycleView.ImageCycleViewListener;
import com.bdyjy.entity.fix.MyFix;
import com.bdyjy.util.PropetiesFileReaderUtil;
import com.bdyjy.util.SPUtils;
import com.bdyjy.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

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
import com.bdyjy.entity.lostfind.LostFind;
import com.bdyjy.entity.attArryData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.image.SmartImageView;

@SuppressLint("ValidFragment")
public class MyFixContent extends Fragment
{

	private MainActivity ctx;
	private TextView tv_back;
	private Button btn_my;
	private ArrayList list;

	private TextView tv_type;
	private TextView tv_title;
	private TextView tv_location;
	private TextView tv_description;
	
	//图片展示
	private SmartImageView sv1;//
	private SmartImageView sv2;//
	private SmartImageView sv3;
	private SmartImageView sv4;

	// 构造器
	public MyFixContent(ArrayList list, MainActivity ctx)
	{
		this.list = list;
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	/**********返回功能**********/
	private void Back(){ 
		ctx.backToClickWithId(list);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.my_fix_content_fragment, null);

		// 返回上一界面
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.backToClickWithId(list);
			}
		});

		// 跳转至我发布界面
		btn_my = (Button) view.findViewById(R.id.setting);
		btn_my.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToClickWithId(Const.FRAGMENT_ASKFIX_ID, list);
			}
		});

		tv_type = (TextView) view.findViewById(R.id.tv_type_myfix_content);
		tv_title = (TextView) view.findViewById(R.id.tv_title_myfix_content);
		tv_location = (TextView) view
				.findViewById(R.id.tv_location_myfix_content);
		tv_description = (TextView) view
				.findViewById(R.id.tv_description_myfix_content);

		MyFix fix = MainActivity.myfixcontent;

		tv_type.setText(fix.getTypeShow());
		tv_title.setText(fix.getTitle());
		tv_location.setText(fix.getLocation());
		tv_description.setText(fix.getDescription());
		

		if(fix.getAttArry().size()==0) //如果为空  图片就不展示了
		{
			
			View hiddenView = (View)view.findViewById (R.id.rl_picture_show) ;  //在hidden_view.xml中hidden_layout是root layout
			if ( null != hiddenView ) {
			      ViewGroup parent = ( ViewGroup ) hiddenView.getParent() ;
			      parent.removeView ( hiddenView ) ;
			}
		

		}else{	
		try{
			 String filePath=fix.getAttArry().get(0).getFilePath().toString().trim();	 //因为通过请求Content 返回的attArry为空的 ，所以暂时使用 通过请求列表获取的图片地址来调试			
			 String attachmentPrefix=fix.getAttachmentPrefix().toString().trim();
		     String http=attachmentPrefix+filePath;
			
			
			sv1=(SmartImageView)view.findViewById(R.id.iv_show1);
		//	sv1.setImageUrl(http, R.drawable.default_image);
			Glide.with(ctx)  
		     .load(http) 
		     .dontAnimate()
		     .placeholder(R.drawable.default_image)
		     .error(R.drawable.default_image)
		     .diskCacheStrategy(DiskCacheStrategy.RESULT)
		     .into(sv1); 
			sv1.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					ImageShow(0);				   				
				}			
		});}catch (Exception e)
			{
			e.printStackTrace();
		}	
		try{
			 String filePath=fix.getAttArry().get(1).getFilePath().toString().trim();	 //因为通过请求Content 返回的attArry为空的 ，所以暂时使用 通过请求列表获取的图片地址来调试			
			 String attachmentPrefix=fix.getAttachmentPrefix().toString().trim();
		     String http=attachmentPrefix+filePath;			
			
			sv2=(SmartImageView)view.findViewById(R.id.iv_show2);
			//sv2.setImageUrl(http, R.drawable.default_image);
			 Glide.with(ctx)  
		     .load(http) 
		     .dontAnimate()
		     .placeholder(R.drawable.default_image)
		     .error(R.drawable.default_image)
		     .diskCacheStrategy(DiskCacheStrategy.RESULT)
		     .into(sv2); 
			sv2.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					ImageShow(1);	   				
				}			
		});}catch (Exception e)
			{
			e.printStackTrace();
		}	
		try{
			 String filePath=fix.getAttArry().get(2).getFilePath().toString().trim();	 //因为通过请求Content 返回的attArry为空的 ，所以暂时使用 通过请求列表获取的图片地址来调试			
			 String attachmentPrefix=fix.getAttachmentPrefix().toString().trim();
		     String http=attachmentPrefix+filePath;
			
			
			sv3=(SmartImageView)view.findViewById(R.id.iv_show3);
			//sv3.setImageUrl(http, R.drawable.default_image);
			Glide.with(ctx)  
		     .load(http) 
		     .dontAnimate()
		     .placeholder(R.drawable.default_image)
		     .error(R.drawable.default_image)
		     .diskCacheStrategy(DiskCacheStrategy.RESULT)
		     .into(sv3); 
			sv3.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					ImageShow(2);			   				
				}			
		});}catch (Exception e)
			{
			e.printStackTrace();
		}	
		try{
			 String filePath=fix.getAttArry().get(3).getFilePath().toString().trim();	 //因为通过请求Content 返回的attArry为空的 ，所以暂时使用 通过请求列表获取的图片地址来调试			
			 String attachmentPrefix=fix.getAttachmentPrefix().toString().trim();
		     String http=attachmentPrefix+filePath;
			
			
			sv4=(SmartImageView)view.findViewById(R.id.iv_show4);
			//sv4.setImageUrl(http, R.drawable.default_image);  
			Glide.with(ctx)  
		     .load(http) 
		     .dontAnimate()
		     .placeholder(R.drawable.default_image)
		     .error(R.drawable.default_image)
		     .diskCacheStrategy(DiskCacheStrategy.RESULT)
		     .into(sv4); 
			sv4.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					ImageShow(3);				   				
				}			
		});}catch (Exception e)
			{
			e.printStackTrace();
		}
		}
		return view;

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
//			Toast.makeText(ctx, mImageUrl.get(position) + position, 1).show();
		}
	};
	

	
	  private ImageShow mAdView1;
		private ImageShow.ImageCycleViewListener mAdCycleViewListener1 = new ImageShow.ImageCycleViewListener()
		{
			@Override
			public void onImageClick(int position, View imageView)
			{
//				Toast.makeText(ctx, mImageUrl.get(position) + position, 1).show();
			}
		};
		public void ImageShow(int currentItem){
			AlertDialog alertDialog=new AlertDialog.Builder(ctx).create();
		    alertDialog.show();
		    Window window=alertDialog.getWindow();
		    window.setContentView(R.layout.dialog_image1);
			//SmartImageView sv;
			//sv=(SmartImageView)alertDialog.getWindow().findViewById(R.id.iv_show);
		    MyFix fix=MainActivity.myfixcontent;
			String filePath=fix.getAttArry().get(0).getFilePath().toString().trim();		
		    String attachmentPrefix=fix.getAttachmentPrefix().toString().trim();
		   	String http=attachmentPrefix+filePath;
			//sv.setImageUrl(http, R.drawable.list_default_image);	
		   	
		   	List<attArryData> attArry=fix.getAttArry();
		   	mImageTitle = new ArrayList<String>();
			mImageUrl = new ArrayList<String>();
		   	//循环添加图片
			for(int i=0;i<attArry.size();i++){
				filePath=fix.getAttArry().get(i).getFilePath().toString().trim();	
				attachmentPrefix=fix.getAttachmentPrefix().toString().trim();
				http=attachmentPrefix+filePath;
				mImageUrl.add(http);
				mImageTitle.add("维护报修测试的的的的的");
			}
		   		   			
			mAdView1 = (ImageShow)alertDialog.getWindow().findViewById(R.id.iv_showx);
			//mAdView1.pushImageCycle();
			mAdView1.setImageResources(mImageUrl, mImageTitle, mAdCycleViewListener1,
					2,currentItem);
					
	    }

}
