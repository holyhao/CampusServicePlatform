package com.bdyjy.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

/**
 * 
 * @author zengyi
 *
 */
public class HttpUtilPost {
	
	/**
	 *  post 获取通讯录
	 * @param ctx
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String doPost(Context ctx, String url) throws Exception
	{
		String ip = PropetiesFileReaderUtil.get(ctx, "ip");
		String port = PropetiesFileReaderUtil.get(ctx, "port");
		String server_project_name = PropetiesFileReaderUtil.get(ctx,
				"server_project_name");
		String result ="";
		if (!url.contains(server_project_name))// 如果不包含后台项目名
		{
			url = "/" + server_project_name + url;
		}
		String fullUrl = "http://" + ip + ":" + port + url;
		
//		  try {  
//              /**设置编码 **/  
//              entity = new UrlEncodedFormEntity("UTF-8");  
//          } catch (UnsupportedEncodingException e) {  
//              // TODO Auto-generated catch block  
//              e.printStackTrace();  
//          }    
		
		DefaultHttpClient client = new DefaultHttpClient();   
		UrlEncodedFormEntity entity=null;  
          /**新建一个post请求**/  
		   HttpPost post = new HttpPost(fullUrl);  
		   StringEntity reqEntity = new StringEntity(HTTP.UTF_8);
           post.setEntity(entity);    
           HttpResponse response=null;  
           try {  
               /**客服端向服务器发送请求**/  
               response = client.execute(post);  
           } catch (ClientProtocolException e) {
               // TODO Auto-generated catch block  
               e.printStackTrace();  
           } catch (IOException e) {  
               // TODO Auto-generated catch block  
               e.printStackTrace();  
           }    
              /**请求发送成功，并得到响应**/  
            if(response.getStatusLine().getStatusCode()==200){    
               try {  
                     /**读取服务器返回过来的json字符串数据**/  
            	   result = EntityUtils.toString(response.getEntity(), "UTF-8");   
               } catch (IllegalStateException e) {  
                   // TODO Auto-generated catch block  
                   e.printStackTrace();  
               } catch (IOException e) {  
                   // TODO Auto-generated catch block  
                   e.printStackTrace();  
               }  
            }
		return result;          
	}
	
}
