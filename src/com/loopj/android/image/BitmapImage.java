package com.loopj.android.image;

import android.content.Context;
import android.graphics.Bitmap;

public class BitmapImage implements SmartImage
{
	private Bitmap bitmap;

	public BitmapImage(Bitmap paramBitmap)
	{
		this.bitmap = paramBitmap;
	}

	public Bitmap getBitmap(Context paramContext)
	{
		return this.bitmap;
	}
}