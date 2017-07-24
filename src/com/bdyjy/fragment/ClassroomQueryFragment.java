package com.bdyjy.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONArray;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.adapter.ClassroomAdapter;
import com.bdyjy.adapter.ClassroomContentAdapter;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.coursequery.Course;
import com.bdyjy.entity.coursequery.CourseQueryResultBean;
import com.bdyjy.util.DateUtil;
import com.bdyjy.util.Util;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ClassroomQueryFragment extends Fragment
{

	Handler handler = null;
	String toastMsg = null;
	private MainActivity ctx;
	private String date;
	private static final String[] mDate =
	{ "昨天", "今天", "明天" };

	private void initHandler()
	{
		handler = new Handler(ctx.getMainLooper())
		{

			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case HandlerOrder.TOAST:
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_SHORT).show();
					break;
				case HandlerOrder.UPDATE_LISTVIEW:
					loadData();
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				default:
					break;
				}
			}
		};
	}

	public ClassroomQueryFragment(MainActivity ctx)
	{
		this.ctx = ctx;
		initHandler();
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	
	/**********返回功能**********/
	private void Back(){ 
		ctx.jumpToFirstPageFregment();
	}

	private ListView listView;
	private ClassroomAdapter adapter;
	// 教室列表
	private List<List<Map<String, Object>>> classroomListItems;
	// 课程列表
	private List<Map<String, Object>> contentListItems;

	private Spinner sp_date;
	private TextView tv_back;
	private TextView tv_date;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.classroom_query_fragment, null);
		// tv_back = (TextView) view.findViewById(R.id.classroom_query_tv_back);
		// tv_back.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v)
		// {
		// ctx.jumpToFirstPageFregment();
		// }
		// });
		view.findViewById(R.id.ll_classroom_query_tv_back).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						ctx.jumpToFirstPageFregment();
					}
				});

		sp_date = (Spinner) view.findViewById(R.id.date_spinner);
		tv_date = (TextView) view.findViewById(R.id.classroom_query_date);

		String yesterday = DateUtil.getDateOfYesterday2();
		String today = DateUtil.getDateOfToday2();
		String tomorrow = DateUtil.getDateOfTomorrow2();
		String[] mDate2 =
		{ "昨天" + "   " + yesterday, "今天" + "   " + today,
				"明天" + "   " + tomorrow };

		ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(ctx,
				android.R.layout.simple_spinner_item, mDate2);
		dateAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		sp_date.setAdapter(dateAdapter);

		// 默认为今天
		sp_date.setSelection(1);

		tv_date.setText(DateUtil.getDateOfToday());

		sp_date.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				switch (position)
				{
				case 0:
					date = DateUtil.getDateOfYesterday();
					break;
				case 1:
					date = DateUtil.getDateOfToday();
					break;
				case 2:
					date = DateUtil.getDateOfTomorrow();
					break;
				}

				tv_date.setText(date);
				updateData();
			}

			public void onNothingSelected(AdapterView<?> parent)
			{
				tv_date.setText("没选中的");
			}
		});

		//
		// tv_date.setText(DateUtil.getDateOfToday()); // 设置默认日期为当天
		// tv_date.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// Calendar calendar = Calendar.getInstance();
		// new DatePickerDialog(ctx, AlertDialog.THEME_HOLO_LIGHT, new
		// DatePickerDialog.OnDateSetListener() {
		// @Override
		// public void onDateSet(DatePicker view, int year, int month, int day)
		// {
		//
		// //生成日期日期，小于10加0，便于传入url请求参数
		// date = new String(new StringBuilder().append(year).append("-")
		// .append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
		// .append("-")
		// .append((day < 10) ? "0" + day : day));
		//
		// tv_date.setText(date);
		// updateData();
		// }
		// }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
		// calendar.get(Calendar.DAY_OF_MONTH) ).show();
		//
		// }
		// });

		listView = (ListView) view.findViewById(R.id.classroom_list);
		classroomListItems = new ArrayList<List<Map<String, Object>>>();

		// 选项的点击事件
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// 点击事件
			}
		});

		updateData();
		return view;
	}

	private void updateData()
	{

		getClassroomList();
		loadData();
	}

	private void loadData()
	{

		Map<String, Object> contentMap = null;

		// 教室列表
		classroomListItems.clear();

		for (String key : MainActivity.classroomMap.keySet())
		{
			Log.d("key", key);
			// 一间教室的课程列表
			contentListItems = new ArrayList<Map<String, Object>>();
			contentListItems.clear();

			for (int j = 0; j < MainActivity.classroomMap.get(key).size(); ++j)
			{
				contentMap = new HashMap<String, Object>();
				contentMap.put("courseTime", MainActivity.classroomMap.get(key)
						.get(j).getStime()
						+ "-"
						+ MainActivity.classroomMap.get(key).get(j).getEtime());
				Log.e("TIME", contentMap.get("courseTime").toString());
				contentMap.put("courseName", MainActivity.classroomMap.get(key)
						.get(j).getCourse_name());
				contentMap.put("classroomName",
						MainActivity.classroomMap.get(key).get(j)
								.getClassroom_name());
				contentListItems.add(contentMap);
			}

			classroomListItems.add(contentListItems);
			Log.e("contentListItems",
					contentListItems.get(0).get("classroomName").toString());
		}
		for (List<Map<String, Object>> list : classroomListItems)
		{
			Log.e("classroomListItems", list.get(0).get("classroomName")
					.toString());
		}

		if (null == adapter)
		{
			adapter = new ClassroomAdapter(ctx, contentListItems,
					classroomListItems); // 创建适配器
			listView.setAdapter(adapter);
		} else
		{
			adapter.refresh(classroomListItems);
		}

	}

	// 从网络获取课室列表
	private void getClassroomList()
	{
		Thread thread = new Thread()
		{
			@Override
			public void run()
			{
				String res = null;
				String interfaceUrl = "http://ss.pkusz.edu.cn/events/get_data?time_now=";
				StringBuilder builder = new StringBuilder()
						.append(interfaceUrl).append(
								tv_date.getText().toString());
				String fullUrl = new String(builder);
				Log.d("url", fullUrl);
				Request request = new Request.Builder().url(fullUrl).build();

				OkHttpClient mOkHttpClient = new OkHttpClient();
				Response response;
				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					response = mOkHttpClient.newCall(request).execute();
					if (response.isSuccessful())
					{
						String body = response.body().string();
						res = body;
					} else
					{
						res = "error";
						toastMsg = "获取课室信息失败";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						return;
					}
				} catch (IOException e)
				{
					e.printStackTrace();
				} finally
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				}

				System.out.println("请求新闻：请求返回的结果是：" + res.trim());

				List<CourseQueryResultBean> resultList = new ArrayList<CourseQueryResultBean>();
				resultList = JSONArray.parseArray(res,
						CourseQueryResultBean.class);

				// 用于存放解析出来的教室名称
				TreeMap<String, List<Course>> map = new TreeMap<String, List<Course>>();

				int i = 0;
				for (CourseQueryResultBean item : resultList)
				{

					// 查询该课程的教室是否在教室Map列表中。
					Course course = item.getEvent();
					String classroomName = course.getClassroom_name();

					if (map.containsKey(classroomName))
					{ // 如果教室存在
						map.get(classroomName).add(course);
						Log.d("classroomName", "run if");
					} else
					{ // 如果教室不存在
						List<Course> list = new ArrayList<Course>();
						list.add(course);
						map.put(classroomName, list);
					}
				}

				// for(String key : map.keySet()) {
				// Log.d("key", key);
				// }

				MainActivity.classroomMap = map;

				handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);

			}

		};

		thread.start();
		try
		{
			thread.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
