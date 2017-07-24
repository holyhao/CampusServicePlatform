
package com.bdyjy.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.entity.News;

import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;//这个特别容易导错  
import org.jsoup.select.Elements; 


/**
 * 校内指南内容页
 * @author holy
**/

public class CampusGuideContentFragment extends Fragment
{
	private TextView tv_back,school_life;
	private MainActivity ctx;
	
	private TextView title;
	private TextView typeShow;
	private WebView content;

	public CampusGuideContentFragment(MainActivity ctx)
	{
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	/**********返回功能**********/
	private void Back(){ 
		ctx.jumpToCampusGuideFragment();
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.campusguide_content_fragment, null);
       
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToCampusGuideFragment();
			}
		});
        
		school_life=(TextView)view.findViewById(R.id.school_life);
		school_life.setText(MainActivity.School_Life);
		
		title=(TextView)view.findViewById(R.id.campusguide_title);
		title.setText(MainActivity.campusGuides.getTitle());
		
		content=(WebView)view.findViewById(R.id.campusguide_content);
		WebSettings webSettings =   content .getSettings();       
		//webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
	
		
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);  
		webSettings.setUseWideViewPort(true);//关键点  
		
		
		
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);  
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		content.requestFocusFromTouch();
		content.getSettings().setDefaultTextEncodingName("UTF-8");
		
		String sHead=	"<html><head><meta name=\"viewport\" content=\"width=device-width, " +
				"initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" />"+
				"<style>img{max-width:100% !important;height:auto !important;}</style>"
				+"<style>body{max-width:100% !important;}</style>"+"</head><body>";
	
		Log.e("HTML==", MainActivity.campusGuides.getContent());
		MainActivity.campusGuides.setContent(sHead+
				MainActivity.campusGuides.getContent().replace("/../", MainActivity.sIp)
				+"</body></html>");
	    content.loadDataWithBaseURL(null, MainActivity.campusGuides.getContent(), "text/html", "UTF-8", null);
	 	//"<head><style>img{max-width:100%  !important;height:auto !important;}</style></head>"+
	    /*
	     * 此方法只适合
	     */
	    // content.loadDataWithBaseURL(null,getNewContent(MainActivity.campusGuides.getContent()), "text/html", "utf-8", null);
		//auto
		
	
	
	    return view;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MainActivity.School_Life="";
		super.onDestroy();
	}
	
	
	
	
	
//	private String getNewContent(String htmltext){  
//	      
//	    Document doc=Jsoup.parse(htmltext);  
//	       Elements elements=doc.getElementsByTag("img");  
//	       for (Element element : elements) {  
//	        element.attr("width","100%").attr("height","auto");  
//	    }  
//	         
//	       Log.d("VACK", doc.toString());  
//	       return doc.toString();  
//	   }  

}
