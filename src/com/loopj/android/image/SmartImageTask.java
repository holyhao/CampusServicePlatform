package com.loopj.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class SmartImageTask
  implements Runnable
{
  private static final int BITMAP_READY = 0;
  private boolean cancelled = false;
  private OnCompleteHandler onCompleteHandler;
  private SmartImage image;
  private Context context;

  public SmartImageTask(Context paramContext, SmartImage paramSmartImage)
  {
    this.image = paramSmartImage;
    this.context = paramContext;
  }

  public void run()
  {
    if (this.image != null)
    {
      complete(this.image.getBitmap(this.context));
      this.context = null;
    }
  }

  public void setOnCompleteHandler(OnCompleteHandler paramOnCompleteHandler)
  {
    this.onCompleteHandler = paramOnCompleteHandler;
  }

  public void cancel()
  {
    this.cancelled = true;
  }

  public void complete(Bitmap paramBitmap)
  {
    if ((this.onCompleteHandler != null) && (!this.cancelled))
      this.onCompleteHandler.sendMessage(this.onCompleteHandler.obtainMessage(0, paramBitmap));
  }

  public static class OnCompleteHandler extends Handler
  {
    public void handleMessage(Message paramMessage)
    {
      Bitmap localBitmap = (Bitmap)paramMessage.obj;
      onComplete(localBitmap);
    }

    public void onComplete(Bitmap paramBitmap)
    {
    }
  }
}