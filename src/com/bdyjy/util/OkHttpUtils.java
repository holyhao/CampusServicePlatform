/**
 * OkHttpUtils.java[v 1.0.0]
 * class:com.example.exampleandroidproject.util,OkHttpUtils
 * 周航 create at 2016-3-19 下午2:36:57
 */
package com.bdyjy.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * com.example.exampleandroidproject.util.OkHttpUtils
 * 
 * @author 周航<br/>
 *         create at 2016-3-19 下午2:36:57
 */
public class OkHttpUtils
{

	// 将本类设置为单例
	private static OkHttpUtils okHttpUtils = null;

	private static OkHttpClient mOkHttpClient = new OkHttpClient();
	private String url;
	private String result;
	final int MSG_WHAT = 1;

	static
	{
		mOkHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
	}

	private OkHttpUtils()
	{
	}

	public static OkHttpUtils getInstance()
	{
		if (null == okHttpUtils)
		{
			okHttpUtils = new OkHttpUtils();
		}
		return okHttpUtils;
	}

	public void uploadImg(Context ctx, String fileUri, final Handler handler)
			throws IOException
	{

		Log.d("imgx", "正在上传图片");
		String ip = PropetiesFileReaderUtil.get(ctx, "ip");
		String port = PropetiesFileReaderUtil.get(ctx, "port");

		// 从sharePreference中取出之前存储的参数
		String token = (String) SPUtils.get(ctx, "token", "");
		String singnature = (String) SPUtils.get(ctx, "singnature", "");
		String st = (String) SPUtils.get(ctx, "st", "");

		String fullUrl = "http://" + ip + ":" + port
				+ "/contentFileUp/fileUp.do?token=" + token + "&singnature="
				+ singnature + "&st=" + st;

		File file = new File(fileUri);

		System.out.println("上传图片的大小是：" + file.length() + " byte");

		MediaType MEDIA_TYPE_IMAGE = MediaType
				.parse("image/jpeg; charset=utf-8");

		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM)
				.addFormDataPart("upFile", file.getName(),
						RequestBody.create(MEDIA_TYPE_IMAGE, file)).build();

		Request request = new Request.Builder().url(fullUrl).post(requestBody)
				.build();

		Call call = mOkHttpClient.newCall(request);
		call.enqueue(new Callback()
		{
			@Override
			public void onFailure(Request arg0, IOException arg1)
			{
				System.err.println("上传报错！onFailure");
				handler.sendEmptyMessage(HandlerOrder.UPLOAD_ERROR);
			}

			@Override
			public void onResponse(Response response) throws IOException
			{
				System.out.println(response.code());// 打印出response字符串
				String body = response.body().string().trim();
				if (response.code() == 200)// 如果服务器返回正常，那就解析结果
				{
					Message s = new Message();
					s.what = HandlerOrder.UPLOAD_OK;
					Bundle bd = new Bundle();
					bd.putString("body", body);
					s.setData(bd);
					handler.sendMessage(s);
				}
			}
		});
	}
	
	

	/**
	 * 所有涉及到中文提交的，全部使用此方法；异步post
	 * 
	 * @return
	 */
	public String doPostAsync(Context ctx, String url,
			HashMap<String, String> map, final Handler handler)
	{

		String ip = PropetiesFileReaderUtil.get(ctx, "ip");
		String port = PropetiesFileReaderUtil.get(ctx, "port");
		String server_project_name = PropetiesFileReaderUtil.get(ctx,
				"server_project_name");

//		url = "http://" + ip + ":" + port + url;
		if (!url.contains(server_project_name))// 如果不包含后台项目名
		{
			url = "/" + server_project_name + url;
		}

		url = "http://" + ip + ":" + port + url;
		
		System.out.println("xx:"+url);

		FormEncodingBuilder feb = new FormEncodingBuilder();
		for (String key : map.keySet())
		{
			feb.add(key, map.get(key));
			// feb.addEncoded(key, map.get(key));
		}

		RequestBody formBody = feb.build();

		Request request = new Request.Builder().url(url)
				.header("User-Agent", "OkHttp Headers.java")
				.addHeader("Accept", "application/json; q=0.5")
				.addHeader("Accept", "application/vnd.github.v3+json")
				.addHeader("Content-Type", "text/html; charset=UTF-8")// 这一行好像没起到作用
				.post(formBody).build();
		// enqueue
		mOkHttpClient.newCall(request).enqueue(new Callback()
		{
			@Override
			public void onFailure(Request request, IOException e)
			{
				result = "error";
			}

			@Override
			public void onResponse(Response response) throws IOException
			{

				// NOT UI Thread
				if (response.isSuccessful())
				{

					System.out.println(response.code());// 打印出response字符串
					String body = response.body().string().trim();
					if (response.code() == 200)// 如果服务器返回正常，那就解析结果
					{

						// System.out.println("responseCode:"+response.code());
						result = body;
						// System.out.println("body:"+body);
						Message s = new Message();
						s.what = HandlerOrder.POST_OK;
						Bundle bd = new Bundle();
						bd.putString("body", body);
						s.setData(bd);
						handler.sendMessage(s);

					} else
					{
						// System.out.println("其他问题");
						// System.out.println("responseCode:"+response.code());
						// result = body;
						// System.out.println("body:"+body);
						Message s = new Message();
						s.what = HandlerOrder.POST_ERROR;
						handler.sendMessage(s);

					}
				}
			}
		});

		return result;
	}

	/**
	 * 同步get
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public String doGet(Context ctx, String url) throws Exception
	{

		String ip = PropetiesFileReaderUtil.get(ctx, "ip");
		String port = PropetiesFileReaderUtil.get(ctx, "port");
		String server_project_name = PropetiesFileReaderUtil.get(ctx,
				"server_project_name");

		if (!url.contains(server_project_name))// 如果不包含后台项目名
		{
			url = "/" + server_project_name+"/" + url;
		}

		String fullUrl = "http://" + ip + ":" + port + url;
		System.out.println("get url:" + fullUrl);

		Request request = new Request.Builder().url(fullUrl).build();

		Response response = mOkHttpClient.newCall(request).execute();
		if (response.isSuccessful())
		{
			String body = response.body().string();
			result = body;
		} else
		{
			result = "error";
		}
		return result;
	}
	

	/*
	 * get guocuicui
	 */

	public String Get(String url) throws IOException
	{
		System.out.println("get url:" + url);
		Request request = new Request.Builder().url(url).build();

		Response response = mOkHttpClient.newCall(request).execute();
		if (response.isSuccessful())
		{
			String body = response.body().string();
			result = body;
		} else
		{
			result = "error";
		}
		return result;
	}

	// /**
	// * 异步get
	// *
	// * @return
	// */
	// public String doGetAsync(String url)
	// {
	// // TODO Auto-generated method stub
	// Request request = new Request.Builder().url(url).build();
	// // enqueue
	// mOkHttpClient.newCall(request).enqueue(new Callback()
	// {
	// @Override
	// public void onFailure(Request request, IOException e)
	// {
	//
	// }
	//
	// @Override
	// public void onResponse(Response response) throws IOException
	// {
	// // NOT UI Thread
	// if (response.isSuccessful())
	// {
	// System.out.println(response.code());
	// // System.out.println(response.body().string());
	// String body = response.body().string();
	// System.out.println(body);
	// result = body;
	// }
	// }
	// });
	//
	// return result;
	// }

	private static final String CHARSET_NAME = HTTP.UTF_8;

	/**
	 * 这里使用了HttpClinet的API。只是为了方便
	 * 
	 * @param params
	 * @return
	 */
	public static String formatParams(List<BasicNameValuePair> params)
	{
		return URLEncodedUtils.format(params, CHARSET_NAME);
	}

	/**
	 * 为HttpGet 的 url 方便的添加多个name value 参数。
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String attachHttpGetParams(String url,
			List<BasicNameValuePair> params)
	{
		return url + "?" + formatParams(params);
	}

	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");

	public String post(Context ctx, String url, String json) throws IOException
	{
		String ip = PropetiesFileReaderUtil.get(ctx, "ip");
		String port = PropetiesFileReaderUtil.get(ctx, "port");
		String server_project_name = PropetiesFileReaderUtil.get(ctx,
				"server_project_name");

		if (!url.contains(server_project_name))// 如果不包含后台项目名
		{
			url = "/" + server_project_name + url;
		}

		String fullUrl = "http://" + ip + ":" + port + url;
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(fullUrl).post(body).build();
		Response response = mOkHttpClient.newCall(request).execute();
		if (response.isSuccessful())
		{
			return response.body().string();
		} else
		{
			throw new IOException("Unexpected code " + response);
		}
	}
	// 写一个异步获取图片的方法

}
