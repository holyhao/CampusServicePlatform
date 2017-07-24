/**
 *失物招领的内容查看界面
 *create by songdebin  2016-04-05 
 * */

package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.custom.view.ImageCycleView;
import com.bdyjy.custom.view.ImageCycleView.ImageCycleViewListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.lostfind.LostFind;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.fragment.base.FragmentWithCollection;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.image.SmartImageView;
import com.bdyjy.custom.view.ImageShow;

@SuppressLint("ValidFragment")
public class LostContentFragment extends FragmentWithCollection
{

	private MainActivity ctx;
	private TextView tv_back;
	private Button btn_my;

	private TextView title;
	private TextView name;
	private TextView contacts;
	private TextView tel;
	private TextView description;

	// 图片展示
	private SmartImageView sv1;//
	private SmartImageView sv2;//
	private SmartImageView sv3;
	private SmartImageView sv4;

	// 构造器
	public LostContentFragment(MainActivity ctx)
	{
		super(ctx, CollectionTypeConst.LOSTANDFOUND);
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};
	}
	
	/**********返回功能**********/
	private void Back(){ 
		if ("myCollectionList".equals(MainActivity.orderFrom))
		{
			ctx.jumpToMyCollectionListFragment();
			MainActivity.orderFrom = "";
		} else
		ctx.jumpToClickById(Const.FRAGMENT_LOSTFINDLIST_ID);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.lostfind_content_fragment, null);

		// 返回上一界面
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if ("myCollectionList".equals(MainActivity.orderFrom))
				{
					ctx.jumpToMyCollectionListFragment();
					MainActivity.orderFrom = "";
				} else
				ctx.jumpToClickById(Const.FRAGMENT_LOSTFINDLIST_ID);
			}
		});

		// 跳转至我的失物招领
		btn_my = (Button) view.findViewById(R.id.setting);
		btn_my.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToMyLost(Const.FRAGMENT_LOSTFINDCONTENT_ID);
			}
		});

		title = (TextView) view.findViewById(R.id.tv_title_lostfind_content);
		contacts = (TextView) view
				.findViewById(R.id.tv_contacts_lostfind_content);
		tel = (TextView) view.findViewById(R.id.tv_tel_lostfind_content);
		description = (TextView) view
				.findViewById(R.id.tv_description_lostfind_content);

		LostFind lf = MainActivity.lostfindcontent;

		title.setText(lf.getTitle());
		contacts.setText(lf.getContacts());
		tel.setText(lf.getTel());
		description.setText(lf.getDescription());

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
				//此处由于SmartImage不明白为何加载不出来，所以改成使用Glide而不是SmartImage加载图片
//				sv1.setImageUrl(http, R.drawable.default_image);
				 Glide.with(ctx)  
			     		.load(http)  
			     		.dontAnimate()
			     		.diskCacheStrategy(DiskCacheStrategy.RESULT) 
			     		.placeholder(R.drawable.default_image)
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
				//存在第二张图片
				try
				{
					
					String filePath = lf.getAttArry().get(1).getFilePath()
							.toString().trim(); // 因为通过请求Content 返回的attArry为空的
												// ，所以暂时使用 通过请求列表获取的图片地址来调试
					String attachmentPrefix = lf.getAttachmentPrefix().toString()
							.trim();
					String http = attachmentPrefix + filePath;

					sv2 = (SmartImageView) view.findViewById(R.id.iv_show2);
//					sv2.setImageUrl(http, R.drawable.default_image);
					Glide.with(ctx)  
		     				.load(http)  
		     				.dontAnimate()
		     				.diskCacheStrategy(DiskCacheStrategy.RESULT) 
		     				.placeholder(R.drawable.default_image)
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
				//存在第三张图片
				try
				{
					String filePath = lf.getAttArry().get(2).getFilePath()
							.toString().trim(); // 因为通过请求Content 返回的attArry为空的
												// ，所以暂时使用 通过请求列表获取的图片地址来调试
					String attachmentPrefix = lf.getAttachmentPrefix().toString()
							.trim();
					String http = attachmentPrefix + filePath;

					sv3 = (SmartImageView) view.findViewById(R.id.iv_show3);
//					Log.i("image http=",http);
//					sv3.setImageUrl(http, R.drawable.default_image);
					Glide.with(ctx)  
     						.load(http)  
     						.dontAnimate()
     						.diskCacheStrategy(DiskCacheStrategy.RESULT) 
     						.placeholder(R.drawable.default_image)
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
				//存在第四张图片
				try
				{
					String filePath = lf.getAttArry().get(3).getFilePath()
							.toString().trim(); // 因为通过请求Content 返回的attArry为空的
												// ，所以暂时使用 通过请求列表获取的图片地址来调试
					String attachmentPrefix = lf.getAttachmentPrefix().toString()
							.trim();
					String http = attachmentPrefix + filePath;

					sv4 = (SmartImageView) view.findViewById(R.id.iv_show4);
//					sv4.setImageUrl(http, R.drawable.default_image);
					Glide.with(ctx)  
     						.load(http)  
     						.dontAnimate()
     						.diskCacheStrategy(DiskCacheStrategy.RESULT) 
     						.placeholder(R.drawable.default_image)
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
		super.setMainView(view);
		dealCollect(lf.getId(), lf.getIsCollect(), lf.getTitle());
		
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
