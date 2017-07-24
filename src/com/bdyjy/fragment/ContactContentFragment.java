package com.bdyjy.fragment;

import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.entity.contact.ContactContent;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通讯录内容Fragment
 * 
 * @author cuicui create at 2016-03-29
 * 
 */
public class ContactContentFragment extends Fragment
{

	private TextView tv_contact_content_back;
	private MainActivity ctx;

	public ContactContentFragment(MainActivity ctx)
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
		ctx.jumpToContactListFragment();	
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.contact_content, null);

		tv_contact_content_back = (TextView) view
				.findViewById(R.id.tv_contact_content_back);
		tv_contact_content_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToContactListFragment();
			}
		});

		TextView name = (TextView) view.findViewById(R.id.name);
		TextView department = (TextView) view.findViewById(R.id.department);

		final TextView telphone = (TextView) view.findViewById(R.id.telephone);

		final TextView mobilephone = (TextView) view
				.findViewById(R.id.mobilephone);

		TextView office = (TextView) view.findViewById(R.id.office);

		TextView email = (TextView) view.findViewById(R.id.mail);

		ImageView sex = (ImageView) view.findViewById(R.id.contact_content_sex);

		ContactContent n = MainActivity.contact;

		// 判断性别
		if (n.getSex().equals("男"))
		{
			sex.setImageResource(R.drawable.man);
		} else if (n.getSex().equals(""))
		{
			sex.setVisibility(View.GONE);
		}

		name.setText(n.getName() == null ? "" : n.getName());
		department.setText(n.getDepartment() == null ? "" : n.getDepartment());
		telphone.setText(n.getTelphone() == null ? "" : n.getTelphone());
		mobilephone.setText(n.getMobilephone() == null ? "" : n
				.getMobilephone());
		office.setText(n.getOffice() == null ? "" : n.getOffice());
		email.setText(n.getEmail() == null ? "" : n.getEmail());

		// TODO Auto-generated method stub
		view.findViewById(R.id.phone_icon).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + telphone.getText().toString()));
						ctx.startActivity(intent);
					}
				});

		view.findViewById(R.id.phone_icon2).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:"
										+ mobilephone.getText().toString()));
						ctx.startActivity(intent);
					}

				});

		// telphone.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
		// telphone.toString().trim()));
		// ctx.startActivity(intent);
		// }
		//
		// });
		//
		// mobilephone.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
		// mobilephone.toString().trim()));
		// ctx.startActivity(intent);
		// }
		//
		// });
		return view;
	}
}
