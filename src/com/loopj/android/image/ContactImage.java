package com.loopj.android.image;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import java.io.InputStream;

public class ContactImage implements SmartImage
{
	private long contactId;

	public ContactImage(long paramLong)
	{
		this.contactId = paramLong;
	}

	public Bitmap getBitmap(Context paramContext)
	{
		Bitmap localBitmap = null;
		ContentResolver localContentResolver = paramContext
				.getContentResolver();
		try
		{
			Uri localUri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, this.contactId);
			InputStream localInputStream = ContactsContract.Contacts
					.openContactPhotoInputStream(localContentResolver, localUri);
			if (localInputStream != null)
				localBitmap = BitmapFactory.decodeStream(localInputStream);
		} catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return localBitmap;
	}
}