/**
 *我的二手交易
 *create by songdebin  2016-04-05 
 * */
package com.bdyjy.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.adapter.SecondhandMarketAdapter;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.custom.view.XListView;
import com.bdyjy.custom.view.XListView.IXListViewListener;
import com.bdyjy.entity.attArryData;
import com.bdyjy.entity.secondMarket.SecondMarket;
import com.bdyjy.entity.secondMarket.SecondMarketContentBean;
import com.bdyjy.entity.secondMarket.SecondMarketQueryBean;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint("ValidFragment")

public class MyFleaFragment extends Fragment  implements IXListViewListener  {
	
private TextView tv_back;	
private TextView tv_set;
private MainActivity ctx;	
private XListView lv_market;// 定义展示列表
private SecondhandMarketAdapter s_adpater;//定义适配器
private List<Map<String, Object>> listItems;

private int pageSize = 5;// 加载的列表数目
private int sizeStep = 5;// 每次加载的数目

private String searchKeys;//搜索关键字
private EditText et_search; // 搜索状态下的输入框
private TextView tv_search_cancel;// 关闭搜索功能
private RelativeLayout rl_searchbar_off; // 关闭状态下的输入框
private RelativeLayout rl_searchbar_on;// 工作状态下的输入框
private boolean is_search = false;// 定义搜索状态 true 处于搜索状态 false处于正常状态

private Button delete_search;
private TextWatcher textWatcher = new TextWatcher() {  //对搜索框输入内容做一个监视   目前的功能是 监控有无输入内容， 在有字时就 显示清空按键
	
    private CharSequence temp;  
    private int editStart ;  
    private int editEnd ;  
    @Override  
    public void onTextChanged(CharSequence s, int start, int before, int count) {  
        // TODO Auto-generated method stub  
         temp = s;  
    }  
      
    @Override  
    public void beforeTextChanged(CharSequence s, int start, int count,  
            int after) {  
        // TODO Auto-generated method stub  
//      mTextView.setText(s);//将输入的内容实时显示  
    }  
      
    @Override      
    public void afterTextChanged(Editable s) {      //内容变动之后     

    	searchKeys = et_search.getText().toString().trim();
    	if(TextUtils.isEmpty(searchKeys)){
    		delete_search.setVisibility(View.INVISIBLE);
		}else{delete_search.setVisibility(View.VISIBLE);}	

    }  
};

private int source;//记录从哪里转来的，然后返回原来的界面
	public  MyFleaFragment(MainActivity ctx,int source)
	{
		this.ctx = ctx;
		this.source=source;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};								
	}
	
	/**********返回功能**********/
	private void Back(){ 
		if(source==Const.FRAGMENT2_ID){
			ctx.jumpToFleaRelease(source);
		}
		else{				 
		ctx.jumpToClickById(source); 
		}
	}
	
	String toastMsg = null;
	Handler handler = null;
	private void initHandler()
	{
		handler = new Handler(ctx.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case HandlerOrder.TOAST: //获取数据失败
					// TODO
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.UPDATE_LISTVIEW: //正常接收数据
					lv_market.onLoad();
					loadData();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.POST_OK:  //关键字提交成功   异步获取用于  搜索框功能
					String res;
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				     res=msg.getData().getString("body");
				     SecondMarketQueryBean bean = JSON.parseObject(res,SecondMarketQueryBean.class);					
					String app_result_key = bean.getApp_result_key();					
					if ("0".equals(app_result_key))
					{
						List<SecondMarket> list = bean.getData().getRows();
						MainActivity.secondMarket = list;				
						handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
						
						return;
					}else
					{
						toastMsg = "获取内容失败，请检查网络";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
					} 
			      break;
				case HandlerOrder.POST_ERROR:  //获取失败
					toastMsg = "获取内容失败，请检查网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					break;
				}
			}
		};
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	     {
		View view = inflater.inflate(R.layout.myflea_fragment, null);	

		initHandler();
	  //返回上一界面
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(source==Const.FRAGMENT2_ID){
					ctx.jumpToFleaRelease(source);
				}
				else{				 
				ctx.jumpToClickById(source); 
				}
			}
		});
		
		
		if(source==Const.FRAGMENT3_ID){
			tv_set=(TextView)view.findViewById(R.id.setting);
			tv_set.setVisibility(View.VISIBLE);
			tv_set.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//ctx.jumpToClickById(Const.FRAGMENT_FLEARELEASE_ID);
					ctx.jumpToFleaRelease(source);
				}
			});
			
		}
		
		/*********************搜索框部分*************************/
		rl_searchbar_off = (RelativeLayout) view
				.findViewById(R.id.searchbar_off);
		rl_searchbar_on = (RelativeLayout) view.findViewById(R.id.searchbar_on);

		// 开启搜索框
		rl_searchbar_off.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{ 
				is_search=true; //进入开启状态
				rl_searchbar_off.setVisibility(View.INVISIBLE);
				rl_searchbar_on.setVisibility(View.VISIBLE);
				et_search.setFocusable(true);
				et_search.requestFocus();
				// 设置自动弹出输入法
				InputMethodManager imm = (InputMethodManager) et_search
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

				Timer timer = new Timer();

				timer.schedule(new TimerTask()
				{
					public void run()

					{
						InputMethodManager inputManager = (InputMethodManager) et_search
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						inputManager.showSoftInput(et_search, 0);
					}
				}, 400);
			}
		});

		tv_search_cancel = (TextView) view.findViewById(R.id.tv_search_cancel);
		// 关闭搜索框
		tv_search_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				is_search=false; //进关闭状态
				rl_searchbar_off.setVisibility(View.VISIBLE);
				rl_searchbar_on.setVisibility(View.INVISIBLE);
				et_search.setText("");
				pageSize=5;
				getMySecondHandMarketList();
			}
		});

		// 搜索框输入监听
		et_search = (EditText) view.findViewById(R.id.et_search);
		et_search.addTextChangedListener(textWatcher);
		et_search.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event)
			{
				Toast.makeText(ctx, "正在搜索，请稍等", Toast.LENGTH_SHORT).show();
				search();
				return false;
			}
		});
		//初始化删除按钮
		delete_search=(Button)view.findViewById(R.id.bt_search_delete);
		delete_search.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				et_search.setText("");
			}
		});
/*********************************************/
		
		
		
		lv_market=(XListView)view.findViewById(R.id.lv_fleamarket_list);
		lv_market.setPullLoadEnable(true);
		lv_market.setXListViewListener(this);

		listItems = new ArrayList<Map<String, Object>>(); 
		
		//点击一项
		lv_market.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 设置选项的点击事件
				TextView tv = (TextView) view.findViewById(R.id.tv_flea_id_item);
				getMyFleaContent(tv.getText().toString());
			}
		});
						
		getMySecondHandMarketList();
		return view;
       }
	
	
	/******************************************
	 * 
	 * 给列表加载从后台获取的数据
	 * 
	 * *****************************************/
		private void loadData()
		{
			// 清空原有内容
			listItems.clear();
			Map<String, Object> map = null;
			 
		   //写入数据
			for (int i = 0; i < MainActivity.secondMarket.size(); i++)
			{
				map = new HashMap<String, Object>();
				map.put("title", MainActivity.secondMarket.get(i).getTitle()); // 物品标题
				map.put("price", MainActivity.secondMarket.get(i).getPrice());
				map.put("time", MainActivity.secondMarket.get(i).getCreateTime());
				map.put("id", MainActivity.secondMarket.get(i).getId());
				map.put("goodsname", MainActivity.secondMarket.get(i).getGoodsName());
				map.put("status", ""+MainActivity.secondMarket.get(i).getStatus());
				map.put("statusShow",MainActivity.secondMarket.get(i).getStatusShow());
				map.put("tel", MainActivity.secondMarket.get(i).getTel());
				List<attArryData> attArry=MainActivity.secondMarket.get(i).getAttArry();
				  String http="";			 
				  String http2="";
				  String http1="";
				try{ 
					   http1=attArry.get(0).getFilePath().toString().trim();
					   http2=MainActivity.secondMarket.get(i).getAttachmentPrefix();
					}catch (Exception e)
					{
						e.printStackTrace();
					}
						http=http2+http1;
				
							
				map.put("http", http);
				listItems.add(map);
			}
			
			if (null == s_adpater)
			{
				s_adpater = new SecondhandMarketAdapter(ctx, listItems); // 创建适配器
				lv_market.setAdapter(s_adpater);
			} else
			{
				s_adpater.refresh(listItems);
			}
		}
		
		/******************************************
		 * 
		 * 给列表加载从后台获取的数据
		 * 
		 * *****************************************/
		private void getMySecondHandMarketList()
		{
			new Thread()
			{
				@Override
				public void run()
				{
					
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);//打开进度条
					// 试试get请求
					String res = null;

					// 从sharePreference中取出之前存储的参数
					String token = (String) SPUtils.get(ctx, "token", "");
					String singnature = (String) SPUtils.get(ctx, "singnature", "");
					String st = (String) SPUtils.get(ctx, "st", "");

					try
					{
						res = OkHttpUtils.getInstance().doGet(

								ctx,
								"/admin/secondhanddeal/grid.do?pageNo=1"+"&pageSize="+pageSize
									+ "&token=" + token + "&singnature="
									+ singnature + "&st=" + st+"&userId="+token);
					} catch (Exception e)
					{
						e.printStackTrace();
						toastMsg = "获取失败，请检查您的网络";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);//隐藏进度条
						return;
					}
					 handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);//隐藏进度条
					 System.out.println("请求二手交易列表：请求返回的结果是：" + res.trim());

					// 尝试将json串转化成bean对象
					SecondMarketQueryBean bean = JSON.parseObject(res,
							SecondMarketQueryBean.class);
					 //System.out.println("xxxx:" + bean.getApp_result_key());

					// 
					if (res.trim().length() == 0)
					{
						toastMsg = "获取失败，请检查您的网络";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					}
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))
					{
						// 到了这里说明正常返回了数据，我要在这里获取新闻的标题
						List<SecondMarket> list = bean.getData().getRows();

						// 将这些新闻对象存储到sp中
						MainActivity.secondMarket = list;
						// 使用handler去通知主线程更新listview
						handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW); //接收消息成功  临时设置
					}
					else{
						toastMsg = "获取失败，请检查您的网络";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;						
					}
				}
			}.start();
		}	
	
	
		/******************************************
		 * 
		 * 获取列表具体内容
		 * 这一项的网络请求是多余的，
		 * 因为在之前获取列表的时候就已经获取到了全部信息
		 * 
		 * *****************************************/
			private void getMyFleaContent(final String id)
			{
				new Thread()
				{
					@Override
					public void run()
					{
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);//打开进度条
						// 试试get请求
						String res = null;

						// 从sharePreference中取出之前存储的参数
						String token = (String) SPUtils.get(ctx, "token", "");
						String singnature = (String) SPUtils.get(ctx, "singnature", "");
						String st = (String) SPUtils.get(ctx, "st", "");

						try
						{
							res = OkHttpUtils.getInstance().doGet(
									ctx,
									"/admin//secondhanddeal/findById.do?id=" + id + "&token="
											+ token + "&singnature=" + singnature
											+ "&st=" + st);

						} catch (Exception e)
						{
							e.printStackTrace();
							toastMsg = "获取内容失败,请检查你的网络";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
							handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);//隐藏进度条
						}
	                       
					 
						handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);//隐藏进度条
					    System.out.println("content的内容是"+res.trim());
						SecondMarketContentBean bean = JSON.parseObject(res,
								SecondMarketContentBean.class);

						// System.out.println("xxxx:" + bean.getApp_result_key());

						// 返回值将会是JSON格式的数据，我要在这里解析
						if (res.trim().length() == 0)
						{
							toastMsg = "获取内容失败,请检查你的网络";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
							return;
						}
						String app_result_key = bean.getApp_result_key();
						if ("0".equals(app_result_key))// 如果正常获得了新闻的内容,
														// 那么我要在这里取得新闻的标题，内容，显示在界面上
						{
							SecondMarket b = bean.getEntity();
							MainActivity.secondmarketcontent = b;
							
							//跳转至内容页
							ctx.jumpToMyFleaContent(source);
						}
						else{
							toastMsg = "获取内容失败,请检查你的网络";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
						}
						
					}
				}.start();			
		}
		

			/******************************************
			 * 
			 *搜索功能
			 * 
			 * *****************************************/

			// 搜索功能   列表页的搜索功能
			private void search()
			{
			
				handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
				// 试试get请求
				//String res = null;
				HashMap<String, String> map1 = new HashMap<String, String>();
				
				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");
				searchKeys = et_search.getText().toString().trim();
				
		       Log.i("pageSearchKey is", searchKeys);

		    	map1.put("pageSearchKeys", searchKeys);
		    	String http = "/admin/secondhanddeal/grid.do?pageNo=1&pageSize"
						+ pageSize+"&token=" + token   
				+ "&singnature=" + singnature + "&st=" + st+"&userId="+token;
		       OkHttpUtils.getInstance().doPostAsync(ctx, http, map1, handler);
			}
	
			/**
			 * 以下是上拉加载，下拉刷新相关代码
			 */
			@Override
			public void onRefresh()// 这是刷新
			{
						
				
				if(is_search==true){
					search();
				}else{
					getMySecondHandMarketList();
				}
			}

			@Override
			public void onLoadMore()// 这是加载更多
			{
				
				 pageSize += sizeStep;
					if(is_search==true){
						search();
					}else{
						getMySecondHandMarketList();
					}
			}	
	
	

}
