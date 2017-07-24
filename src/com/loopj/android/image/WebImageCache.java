package com.loopj.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebImageCache
{
	private static final String DISK_CACHE_PATH = "/web_image_cache/";
	private ConcurrentHashMap<String, SoftReference<Bitmap>> memoryCache = new ConcurrentHashMap();
	private String diskCachePath;
	private boolean diskCacheEnabled = false;
	private ExecutorService writeThread;

	public WebImageCache(Context paramContext)
	{
		Context localContext = paramContext.getApplicationContext();
		this.diskCachePath = (localContext.getCacheDir().getAbsolutePath() + "/web_image_cache/");
		File localFile = new File(this.diskCachePath);
		localFile.mkdirs();
		this.diskCacheEnabled = localFile.exists();
		this.writeThread = Executors.newSingleThreadExecutor();
	}

	public Bitmap get(String paramString)
	{
		Bitmap localBitmap = null;
		localBitmap = getBitmapFromMemory(paramString);
		if (localBitmap == null)
		{
			localBitmap = getBitmapFromDisk(paramString);
			if (localBitmap != null)
				cacheBitmapToMemory(paramString, localBitmap);
		}
		return localBitmap;
	}

	public void put(String paramString, Bitmap paramBitmap)
	{
		cacheBitmapToMemory(paramString, paramBitmap);
		cacheBitmapToDisk(paramString, paramBitmap);
	}

	public void remove(String paramString)
	{
		if (paramString == null)
			return;
		this.memoryCache.remove(getCacheKey(paramString));
		File localFile = new File(this.diskCachePath, paramString);
		if ((localFile.exists()) && (localFile.isFile()))
			localFile.delete();
	}

	public void clear()
	{
		this.memoryCache.clear();
		File localFile1 = new File(this.diskCachePath);
		if ((localFile1.exists()) && (localFile1.isDirectory()))
		{
			File[] arrayOfFile1 = localFile1.listFiles();
			for (File localFile2 : arrayOfFile1)
				if ((localFile2.exists()) && (localFile2.isFile()))
					localFile2.delete();
		}
	}

	private void cacheBitmapToMemory(String paramString, Bitmap paramBitmap)
	{
		this.memoryCache.put(getCacheKey(paramString), new SoftReference(
				paramBitmap));
	}

	private void cacheBitmapToDisk(final String paramString,
			final Bitmap paramBitmap)
	{
		this.writeThread.execute(new Runnable()
		{
			public void run()
			{
				if (WebImageCache.this.diskCacheEnabled)
				{
					BufferedOutputStream localBufferedOutputStream = null;
					try
					{
						localBufferedOutputStream = new BufferedOutputStream(
								new FileOutputStream(new File(
										WebImageCache.this.diskCachePath,
										WebImageCache.this
												.getCacheKey(paramString))),
								2048);
						paramBitmap.compress(Bitmap.CompressFormat.PNG, 100,
								localBufferedOutputStream);
					} catch (FileNotFoundException localFileNotFoundException)
					{
						localFileNotFoundException.printStackTrace();
					} finally
					{
						try
						{
							if (localBufferedOutputStream != null)
							{
								localBufferedOutputStream.flush();
								localBufferedOutputStream.close();
							}
						} catch (IOException localIOException3)
						{
						}
					}
				}
			}
		});
	}

	private Bitmap getBitmapFromMemory(String paramString)
	{
		Bitmap localBitmap = null;
		SoftReference localSoftReference = (SoftReference) this.memoryCache
				.get(getCacheKey(paramString));
		if (localSoftReference != null)
			localBitmap = (Bitmap) localSoftReference.get();
		return localBitmap;
	}

	private Bitmap getBitmapFromDisk(String paramString)
	{
		Bitmap localBitmap = null;
		if (this.diskCacheEnabled)
		{
			String str = getFilePath(paramString);
			File localFile = new File(str);
			if (localFile.exists())
				try
				{
					long length = localFile.length();
					if (length < SmartImage.maxLength)
					{
						localBitmap = BitmapFactory.decodeFile(str);
					}else{
						localBitmap = null;
					}
					// System.out.println("smartImg:" + + "");
					
				} catch (Exception e)
				{
					System.out.println("捕获异常；" + str);
				}
		}
		return localBitmap;
	}

	private String getFilePath(String paramString)
	{
		return this.diskCachePath + getCacheKey(paramString);
	}

	private String getCacheKey(String paramString)
	{
		if (paramString == null)
			throw new RuntimeException("Null url passed in");
		return paramString.replaceAll("[.:/,%?&=]", "+")
				.replaceAll("[+]+", "+");
	}
}