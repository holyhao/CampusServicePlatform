package com.bdyjy.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

/**
 * 属性文件读取类，用于读取服务器配置
 * 
 * @author 周航<br/>
 *         create at 2016-3-29 上午11:13:09
 */
public class PropetiesFileReaderUtil
{
	public static String get(Context ctx, String key)
	{
		Properties pro = new Properties();

		String res = null;
		InputStream is;
		try
		{
			is = ctx.getAssets().open("server.properties");
			pro.load(is);
			res = pro.getProperty(key);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return res;
	}
}
