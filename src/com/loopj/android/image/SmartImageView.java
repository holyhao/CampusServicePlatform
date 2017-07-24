package com.loopj.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmartImageView extends ImageView
{
	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors.newFixedThreadPool(4);
	private SmartImageTask currentTask;

	public SmartImageView(Context paramContext)
	{
		super(paramContext);
	}

	public SmartImageView(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
	}

	public SmartImageView(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
	}

	public void setImageUrl(String paramString)
	{
		setImage(new WebImage(paramString));
	}

	public void setImageUrl(String paramString, Integer paramInteger)
	{
		setImage(new WebImage(paramString), paramInteger);
	}

	public void setImageUrl(String paramString, Integer paramInteger1,
			Integer paramInteger2)
	{
		setImage(new WebImage(paramString), paramInteger1, paramInteger2);
	}

	public void setImageContact(long paramLong)
	{
		setImage(new ContactImage(paramLong));
	}

	public void setImageContact(long paramLong, Integer paramInteger)
	{
		setImage(new ContactImage(paramLong), paramInteger);
	}

	public void setImageContact(long paramLong, Integer paramInteger1,
			Integer paramInteger2)
	{
		setImage(new ContactImage(paramLong), paramInteger1, paramInteger1);
	}

	public void setImage(SmartImage paramSmartImage)
	{
		setImage(paramSmartImage, null, null);
	}

	public void setImage(SmartImage paramSmartImage, Integer paramInteger)
	{
		setImage(paramSmartImage, paramInteger, paramInteger);
	}

	public void setImage(SmartImage paramSmartImage,
			final Integer paramInteger1, Integer paramInteger2)
	{
		if (paramInteger2 != null)
			setImageResource(paramInteger2.intValue());
		if (this.currentTask != null)
		{
			this.currentTask.cancel();
			this.currentTask = null;
		}
		this.currentTask = new SmartImageTask(getContext(), paramSmartImage);
		this.currentTask
				.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler()
				{
					public void onComplete(Bitmap paramAnonymousBitmap)
					{
						if (paramAnonymousBitmap != null)
						{
							SmartImageView.this
									.setImageBitmap(paramAnonymousBitmap);
							//在这里改，对图片进行压缩，然后再设置bitmap
						} else if (paramInteger1 != null)
							SmartImageView.this.setImageResource(paramInteger1
									.intValue());
					}
				});
		threadPool.execute(this.currentTask);
	}

	public static void cancelAllTasks()
	{
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(4);
	}
}