package com.bdyjy.custom.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdyjy.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.image.SmartImageView;

public class ImageShow extends LinearLayout{
	

	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 图片轮播视图
	 */
	private ViewPager mAdvPager = null;
	/**
	 * 滚动图片视图适配
	 */
	private ImageCycleAdapter mAdvAdapter;
	/**
	 * 图片轮播指示器控件
	 */
	private ViewGroup mGroup;

	private TextView viewGroup2;
	/**
	 * 图片轮播指示个图
	 */
	private ImageView mImageView = null;

	private TextView mTitleView = null;

	/**
	 * 滚动图片指示视图列表
	 */
	private ImageView[] mImageViews = null;

	private TextView[] mTitleViews = null;
	/**
	 * 图片滚动当前图片下标
	 */

	private boolean isStop;

	/**
	 * 游标是圆形还是长条，要是设置0是长条，要是1就是圆形 默认是圆
	 */
	public int stype = 1;

	/**
	 * @param context
	 */
	public ImageShow(Context context)
	{
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	@SuppressLint("Recycle")
	public ImageShow(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
		// 滚动图片右下指示器视
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);

		viewGroup2 = (TextView) findViewById(R.id.viewGroup2);
	}



	/**
	 * 装填图片数据
	 * 
	 * @param imageUrlList
	 * @param imageCycleViewListener
	 */
	public void setImageResources(ArrayList<String> imageUrlList,
			ArrayList<String> imageTitle,
			ImageCycleViewListener imageCycleViewListener, int stype,int conrentItem)
	{
		this.stype = stype;
		// 清除
		mGroup.removeAllViews();
		// viewGroup2.removeAllViews();
		// 图片广告数量
		final int imageCount = imageUrlList.size();
		mImageViews = new ImageView[imageCount];

		mTitleViews = new TextView[imageCount];
		for (int i = 0; i < imageCount; i++)
		{
			mImageView = new ImageView(mContext);
			mTitleView = new TextView(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.leftMargin = 30;
			mImageView.setScaleType(ScaleType.CENTER_CROP);
			mImageView.setLayoutParams(params);

			mImageViews[i] = mImageView;

			mTitleViews[i] = mTitleView;

			// mTitleViews[i] = mTitleView;
			if (i == 0)
			{
				if (this.stype == 1)
					mImageViews[i]
							.setBackgroundResource(R.drawable.banner_dian_focus);
				else
					mImageViews[i]
							.setBackgroundResource(R.drawable.cicle_banner_dian_focus);
			} else
			{
				if (this.stype == 1)
					mImageViews[i]
							.setBackgroundResource(R.drawable.banner_dian_blur);
				else
					mImageViews[i]
							.setBackgroundResource(R.drawable.cicle_banner_dian_blur);
			}
			mGroup.addView(mImageViews[i]);

			// viewGroup2.addView(mTitleViews[i]);
		}

		mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, imageTitle,
				imageCycleViewListener);
		mAdvPager.setAdapter(mAdvAdapter);
		mAdvPager.setCurrentItem(conrentItem);
	}



	/**
	 * 轮播图片监听
	 * 
	 * @author minking
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener
	{
		@Override
		public void onPageScrollStateChanged(int state)
		{
			//if (state == ViewPager.SCROLL_STATE_IDLE)
				//startImageTimerTask();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		@Override
		public void onPageSelected(int index)
		{
			index = index % mImageViews.length;
			// 设置当前显示的图片
			// 设置图片滚动指示器背
			if (stype == 1)
				mImageViews[index]
						.setBackgroundResource(R.drawable.banner_dian_focus);
			else
				mImageViews[index]
						.setBackgroundResource(R.drawable.cicle_banner_dian_focus);
			for (int i = 0; i < mImageViews.length; i++)
			{
				if (index != i)
				{
					if (stype == 1)
						mImageViews[i]
								.setBackgroundResource(R.drawable.banner_dian_blur);
					else
						mImageViews[i]
								.setBackgroundResource(R.drawable.cicle_banner_dian_blur);
				}

			}
		}
	}

	private class ImageCycleAdapter extends PagerAdapter
	{

		/**
		 * 图片视图缓存列表
		 */
		private ArrayList<SmartImageView> mImageViewCacheList;

		/**
		 * 图片资源列表
		 */
		private ArrayList<String> mAdList = new ArrayList<String>();
		private ArrayList<String> mATitle = new ArrayList<String>();
		/**
		 * 广告图片点击监听
		 */
		private ImageCycleViewListener mImageCycleViewListener;

		private Context mContext;

		public ImageCycleAdapter(Context context, ArrayList<String> adList,
				ArrayList<String> title,
				ImageCycleViewListener imageCycleViewListener)
		{
			this.mContext = context;
			this.mAdList = adList;
			this.mATitle = title;
			mImageCycleViewListener = imageCycleViewListener;
			mImageViewCacheList = new ArrayList<SmartImageView>();
		}

		@Override
		public int getCount()
		{
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj)
		{
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position)
		{
			String imageUrl = mAdList.get(position % mAdList.size());
			String title = mATitle.get(position % mATitle.size());
			Log.i("imageUrl", imageUrl + title);

			viewGroup2.setText(title);
			SmartImageView imageView = null;
			if (mImageViewCacheList.isEmpty())
			{
				imageView = new SmartImageView(mContext);
				imageView.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				// 之前设置图片点击监听写在了这里是错误
				// test
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			} else
			{
				imageView = mImageViewCacheList.remove(0);
			}

			// 设置图片点击监听
			imageView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mImageCycleViewListener.onImageClick(
							position % mAdList.size(), v);
				}
			});
			//imageView.setTag(imageUrl); 之前代码setTag（Onbect）表示给View添加一个格外的数据，以后可以用getTag()将这个数据取出来使用框架之后将不用这个方法
			container.addView(imageView);
			//imageView.setImageUrl(imageUrl);设置图片加载显示
			Glide.with(mContext)
			.load(imageUrl)
			.dontAnimate()
		    .diskCacheStrategy(DiskCacheStrategy.RESULT)
		    .into(imageView); 
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			SmartImageView view = (SmartImageView) object;
			mAdvPager.removeView(view);
			mImageViewCacheList.add(view);

		}

	}

	/**
	 * 轮播控件的监听事
	 * 
	 * @author minking
	 */
	public static interface ImageCycleViewListener
	{

		/**
		 * 单击图片事件
		 * 
		 * @param position
		 * @param imageView
		 */
		public void onImageClick(int position, View imageView);
	}
}
