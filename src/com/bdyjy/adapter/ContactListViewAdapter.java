package com.bdyjy.adapter;

import java.util.List;
import java.util.Map;

import com.bdyjy.R;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.adapter.ListViewAdapter.ListItemView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 通讯录适配器
 * 
 * @author cuicui create at 2016-03-29 0:39
 * 
 */
public class ContactListViewAdapter extends BaseAdapter
{

	private Context context; // 运行上下文

	private List<Map<String, Object>> contactListItems; // 通讯录集合
	private LayoutInflater listContainer; // 视图容器

	public ContactListViewAdapter(Context context,
			List<Map<String, Object>> contactListItems)
	{
		super();
		this.context = context;
		this.contactListItems = contactListItems;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文;
	}

	public final class ContactListItemView
	{ // 自定义控件集合
		public TextView name;
		public TextView mobilephone;
		public TextView id;
		public TextView department;
		public TextView total;
		public ImageView phone_icon;
		public LinearLayout department_info;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return contactListItems.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * contactListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub

		Log.e("method", "getView");
		// 自定义视图
		ContactListItemView contactListItemView = null;
		if (convertView == null)
		{
			contactListItemView = new ContactListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.contact_item, null);
			// 获取控件对象
			contactListItemView.department_info = (LinearLayout) convertView
					.findViewById(R.id.department_info);
			contactListItemView.name = (TextView) convertView
					.findViewById(R.id.name);
			contactListItemView.mobilephone = (TextView) convertView
					.findViewById(R.id.mobilephone);
			contactListItemView.id = (TextView) convertView
					.findViewById(R.id.tv_contact_id);
			contactListItemView.department = (TextView) convertView
					.findViewById(R.id.department_name);
			contactListItemView.total = (TextView) convertView
					.findViewById(R.id.total);
			contactListItemView.phone_icon = (ImageView) convertView
					.findViewById(R.id.phone_icon);
			// 设置控件集到convertView
			convertView.setTag(contactListItemView);
		} else
		{
			contactListItemView = (ContactListItemView) convertView.getTag();
		}

		String currentDepartmentName = ((String) contactListItems.get(position)
				.get("department")).trim();

		String total = (String) contactListItems.get(position).get("total")
				+ "人";

		if (position == 0)
		{
			contactListItemView.department_info.setVisibility(View.VISIBLE);
			contactListItemView.department.setText(currentDepartmentName);
			contactListItemView.total.setText(total);
		} else
		{
			String lastDepartmentName = ((String) contactListItems.get(
					position - 1).get("department")).trim();
			if (currentDepartmentName.equals(lastDepartmentName))
			{
				contactListItemView.department_info.setVisibility(View.GONE);
			} else
			{
				contactListItemView.department_info.setVisibility(View.VISIBLE);
				contactListItemView.department.setText(currentDepartmentName);
				contactListItemView.total.setText(total);
			}
		}

		// 设置文字和图片
		contactListItemView.name.setText((String) contactListItems
				.get(position).get("name"));
		contactListItemView.mobilephone.setText((String) contactListItems.get(
				position).get("telphone"));
		contactListItemView.id.setText((String) contactListItems.get(position)
				.get("id"));

		// 点击拨打电话
		final String mobilephone = ((String) contactListItems.get(position)
				.get("telphone")).trim();
		if (mobilephone != null)
		{
			contactListItemView.phone_icon
					.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							// TODO Auto-generated method stub
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + mobilephone));
							context.startActivity(intent);
						}

					});
		}
		return convertView;
	}

	public void refresh()
	{
		// 编译this.contactlistItems
		for (Map<String, Object> map : this.contactListItems)
		{
			System.out.println("map:" + map);
		}
		notifyDataSetChanged();
	}

}
