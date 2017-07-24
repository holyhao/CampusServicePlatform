/**
 * StringUtil.java[v 1.0.0]
 * class:com.bdyjy.util,StringUtil
 * 周航 create at 2016-4-21 下午5:45:47
 */
package com.bdyjy.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * com.bdyjy.util.StringUtil
 * 
 * @author 周航<br/>
 *         create at 2016-4-21 下午5:45:47
 */
public class StringUtil
{
	/**
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String transStr(String msg)
	{
		String res = null;
		try
		{
			res = new String(msg.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}



	/**
	 * 将字符串转码成为ISO8859-1
	 * 
	 * @return
	 */
	public static String transStrToIso8859(String str)
	{
		String res = null;
		try
		{
			res = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return res;
	}

}
