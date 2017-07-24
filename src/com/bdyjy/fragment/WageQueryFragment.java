/**
 * WageQueryFragment.java[v 1.0.0]
 * class:com.mydream.fragment.freg,WageQueryFragment
 * 宋德宾 create at 2016-3-30 
 * 主要功能：工资查询
 */

package com.bdyjy.fragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * 具体工资
 * 
 * @author 宋德宾 create at 2016-3-30 23：04
 */

@SuppressLint("ValidFragment")
public class WageQueryFragment extends Fragment{
	
	private MainActivity ctx;	
	private TextView tv_back;
	private Spinner sp_time;
	
	
	public WageQueryFragment(MainActivity ctx)
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
		ctx.jumpToPersonalCenterFragment();
	}
	
	
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	     {
		View view = inflater.inflate(R.layout.wage_query_fragment, null);	
		
		
	//返回上一界面
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToPersonalCenterFragment();
			}
		});
		
		String type_spin[]={"2016年1月","2016年2月","2016年3月"};
		
		sp_time=(Spinner)view.findViewById(R.id.spinner_wage);;
		ArrayAdapter<String> ad= new ArrayAdapter<String>(ctx,R.layout.fix_type,type_spin);
		sp_time.setAdapter(ad);	
//		Timer timer = new Timer();
//		timer.schedule(new TimerTask()
//		{
//			public void run()
//
//			{
//				
//			}
//		}, 400);
		hint();
		return view;
       }
	 
	
public void hint(){
	AlertDialog alertDialog=new AlertDialog.Builder(ctx).create();
    alertDialog.show();
    Window window=alertDialog.getWindow();
    window.setContentView(R.layout.dialog_hint_invaliable);
    alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
    		
    		@Override
    		public void onCancel(DialogInterface dialog) {
    			ctx.jumpToPersonalCenterFragment();
    		}	
    		
    }
    		);
}	
			
}