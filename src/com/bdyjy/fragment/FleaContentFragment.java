/**
 *二手交易的内容查看界面
 *create by songdebin  2016-04-05 
 * */

package com.bdyjy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.custom.view.ImageShow;
import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.secondMarket.SecondMarket;
import com.bdyjy.fragment.base.CollectionTypeConst;
import com.bdyjy.fragment.base.FragmentWithCollection;
import com.loopj.android.image.SmartImageView;

@SuppressLint("ValidFragment")
public class FleaContentFragment extends FragmentWithCollection
{

	private MainActivity ctx;
	private TextView tv_back;
	private Button btn_my;

	private TextView tv_title;
	private TextView tv_goodsName;
	private TextView tv_price;
	private TextView tv_tel;
	private TextView tv_contacts;
	private TextView tv_description;
	// 图片展示
	private SmartImageView sv1;//
	private SmartImageView sv2;//
	private SmartImageView sv3;
	private SmartImageView sv4;

	// 构造器
	public FleaContentFragment(MainActivity ctx)
	{
		super(ctx, CollectionTypeConst.SECOND_HAND_TRADE);
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
		ctx.jumpToClickById(Const.FRAGMENT_FLEAMARKET_LIST_ID);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.flea_market_content_fragment,
				null);

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
				ctx.jumpToClickById(Const.FRAGMENT_FLEAMARKET_LIST_ID);
			}
		});

		// 跳转至我的二手交易
		btn_my = (Button) view.findViewById(R.id.setting);
		btn_my.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToMyFlea(Const.FRAGMENT_FLEACONTENT_ID);
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
				sv1.setImageUrl(http, R.drawable.default_image);
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
				sv2.setImageUrl(http, R.drawable.default_image);
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
				sv3.setImageUrl(http, R.drawable.default_image);
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
				sv4.setImageUrl(http, R.drawable.default_image);
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
		dealCollect(second.getId(), second.getIsCollect(), second.getTitle());

		return view;
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
			mImageTitle.add("");
		}

		mAdView1 = (ImageShow) alertDialog.getWindow().findViewById(
				R.id.iv_showx);
		// mAdView1.pushImageCycle();
		mAdView1.setImageResources(mImageUrl, mImageTitle,
				mAdCycleViewListener1, 2, currentItem);

	}

}
