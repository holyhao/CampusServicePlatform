package com.bdyjy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.entity.News;
import com.bdyjy.util.RoundImageView;

/**
 * holy个人信息
 * 
 * @author
 */
@SuppressLint("NewApi")
public class PersonalInfoFragment extends Fragment
{
	private TextView tv_back;
	private TextView modify;
	private MainActivity ctx;

	private RoundImageView headimage;
	private TextView userName;
	private TextView identifierNumber;
	private TextView email;
	private TextView college;
	private TextView major;
	private TextView tel;

	public PersonalInfoFragment(MainActivity ctx)
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

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.personal_info_fragment, null);

		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToPersonalCenterFragment();
			}
		});

		// 个人信息的设置
		headimage = (RoundImageView) view.findViewById(R.id.headimage);
		// 拼接头像链接
		String ur1 = MainActivity.personalInfo.getAttachmentPrefix();
		String ur2 = MainActivity.personalInfo.getUserImg();
		// TODO
		headimage.setImageUrl(ur1 + ur2);

		userName = (TextView) view.findViewById(R.id.username);
		userName.setText(MainActivity.personalInfo.getUserName());
		identifierNumber = (TextView) view.findViewById(R.id.identifierNumber);
		identifierNumber.setText(MainActivity.personalInfo
				.getIdentifierNumber());
		email = (TextView) view.findViewById(R.id.email);
		email.setText(MainActivity.personalInfo.getEmail());
		college = (TextView) view.findViewById(R.id.college);
		college.setText(MainActivity.personalInfo.getDepartment());
		major = (TextView) view.findViewById(R.id.major);
		major.setText(MainActivity.personalInfo.getMajor());
		tel = (TextView) view.findViewById(R.id.tel);
		tel.setText(MainActivity.personalInfo.getMobile());

		modify = (TextView) view.findViewById(R.id.modify);
		modify.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToPersonalInfoModifyFragment();
			}
		});

		return view;
	}

}
