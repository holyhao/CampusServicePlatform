package com.loopj.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class WebImage implements SmartImage
{
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 10000;
	private static WebImageCache webImageCache;
	private String url;

	public WebImage(String paramString)
	{
		this.url = paramString;
	}

	public Bitmap getBitmap(Context paramContext)
	{
		if (webImageCache == null)
			webImageCache = new WebImageCache(paramContext);
		Bitmap localBitmap = null;
		if (this.url != null)
		{
			localBitmap = webImageCache.get(this.url);
			if (localBitmap == null)
			{
				localBitmap = getBitmapFromUrl(this.url);
				if (localBitmap != null)
					webImageCache.put(this.url, localBitmap);
			}
		}
		return localBitmap;
	}

	private Bitmap getBitmapFromUrl(String paramString)
	{
		Bitmap localBitmap = null;
		try
		{
			System.out.println("paramString:" + paramString);
			if (TextUtils.isEmpty(paramString))
			{
				localBitmap = null;
			} else
			{

				URLConnection localURLConnection = new URL(paramString)
						.openConnection();

				localURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
				localURLConnection.setReadTimeout(READ_TIMEOUT);

				// 在这里判断图片的大小
				InputStream is = (InputStream) localURLConnection.getContent();

				System.out.println("位置：webImage-getBitmapFromUrl:"
						+ paramString + " ; "
						+ localURLConnection.getContentLength());

				if (localURLConnection.getContentLength() < SmartImage.maxLength)
				{
					localBitmap = BitmapFactory.decodeStream(is);
				} else
				{
					localBitmap = null;
				}
			}
		} catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return localBitmap;
	}

	public static void removeFromCache(String paramString)
	{
		if (webImageCache != null)
			webImageCache.remove(paramString);
	}
}