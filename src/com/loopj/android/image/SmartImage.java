package com.loopj.android.image;

import android.content.Context;
import android.graphics.Bitmap;

public abstract interface SmartImage
{
  public abstract Bitmap getBitmap(Context paramContext);
  
  /**
   * 可显示图片的最大长度
   */
  public static final long maxLength = 2000000;
}