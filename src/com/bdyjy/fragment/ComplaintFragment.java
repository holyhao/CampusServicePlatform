package com.bdyjy.fragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.complaint.ComplaintContentQueryResultBean;
import com.bdyjy.entity.upload.ImgUploadResultBean;
import com.bdyjy.util.ImageUpLoading;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 
 * 投诉建议fragment
 * 
 * @author holy 2016.3.28
 * 
 */
public class ComplaintFragment extends Fragment
{

	private TextView  tv_back;
	private TextView my_complaint;
	private Spinner complaint_sppiner;
	private int complaintChoice;
	private EditText telephone;
	private String mTelephone;
	private EditText describe;
	private String mDescribe;
	private Button submit;

	private String imagePath1 = null;
	private String imagePath2 = null;
	private ImageView addimage1;
	private ImageView addimage2;

	private Map<String, String> imgsMap = new HashMap<String, String>();
	private List<ImgUploadResultBean> uploadResultList = new ArrayList<ImgUploadResultBean>();
	private int imgCount = 2;

	private Handler handler;
	private String toastMsg;
	private MainActivity ctx;

	private void initHandler()
	{
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case HandlerOrder.TOAST:
					// TODO
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.UPDATE_LISTVIEW:
					// listView1.onLoad();
					// loadData() ;
					break;
				case HandlerOrder.PROCESSBAR_SHOW:
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE:
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.UPLOAD_ERROR:
					Toast.makeText(ctx, "上传出错，服务器异常", Toast.LENGTH_LONG).show();
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.UPLOAD_OK:
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					String result = msg.getData().get("body").toString();
					System.out.println("result:\n" + result);
					ImgUploadResultBean is = JSON.parseObject(result,
							ImgUploadResultBean.class);
					if ("0".equals(is.getApp_result_key()))
					{
						System.out.println(is.getAttachmentName());// 文件名
						System.out.println(is.getAttachmentUrl());// 在服务器上的存储路径
						System.out.println(is.getPrix());// 文件服务器地址
						System.out.println(is.getType());// 文件类别
						System.out.println(is.getSize());// 文件大小
						uploadResultList.add(is);
					} else
					{
						Toast.makeText(ctx, "上传报错：App_result_key！=0",
								Toast.LENGTH_LONG).show();
					}

					if (uploadResultList.size() == imgsMap.size())// 如果所有图片都已经上传完成
					{
						ComplaintCommit();
					}

					break;
				case HandlerOrder.POST_OK:
					// 在这里解析post之后的结果
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					// 返回值将会是JSON格式的数据，我要在这里解析
					String res = msg.getData().get("body").toString();
					System.out.println("post_result:\n" + res);
					ComplaintContentQueryResultBean bean = JSON.parseObject(
							res, ComplaintContentQueryResultBean.class);
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))
					{
						toastMsg = "信息提交成功";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						handler.sendEmptyMessage(HandlerOrder.TO_MAIN);
						ctx.jumpToMyComplaintListFregment();

					} else
					{
						toastMsg = bean.getApp_result_message_key();// "信息提交失败，请重试";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
					}

					break;

				}
			}
		};

	}

	public ComplaintFragment(MainActivity ctx)
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.complaint_fragment, null);
		// 返回首页
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToFirstPageFregment();
			}
		});

		// 进入投诉建议列表页
		my_complaint = (TextView) view.findViewById(R.id.my_compliant);

        Drawable drawable=getResources().getDrawable(R.drawable.my);
		drawable.setBounds(0, 0, (int)getResources().getDimension(R.dimen.x16), (int)getResources().getDimension(R.dimen.y12));
		my_complaint.setCompoundDrawables(drawable,null, null, null);
	    drawable=null;
		view.findViewById(R.id.ll_my_compliant).setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						// 跳入列表前，要刷新list
						// getNewsList();
						ctx.jumpToMyComplaintListFregment();
					}
				});

		// 下拉选择
		complaint_sppiner = (Spinner) view.findViewById(R.id.complaint_spinner);
		complaint_sppiner
				.setOnItemSelectedListener(new OnItemSelectedListener()
				{

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						// 获取到投诉类别
						complaintChoice = (int) complaint_sppiner
								.getSelectedItemId() + 1;
						// complaint_sppiner.getSelectedItem().toString();
						// System.out.println("complaintChoice"+complaintChoice);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
						// TODO Auto-generated method stub

					}

				});

		// 联系方式
		telephone = (EditText) view.findViewById(R.id.telephone);

		// 描述
		describe = (EditText) view.findViewById(R.id.describe);

		// 获取图片
		addimage1 = (ImageView) view.findViewById(R.id.add1);
		addimage2 = (ImageView) view.findViewById(R.id.add2);

		addimage2.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 200);
			}
		});

		// 点击确认
		submit = (Button) view.findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				mTelephone = telephone.getText().toString();
				mDescribe = describe.getText().toString();

				// 将数据送往服务器(无图片)
				// uploadResultList.clear();
				// ComplaintCommit();

				// 当点击提交按钮的时候，先检测是否已经选择了图片
				if (!TextUtils.isEmpty(imgsMap.get(img1Key))
						|| !TextUtils.isEmpty(imgsMap.get(img2Key)))
				{
					uploadResultList.clear();
					// 如果选择了图片
					try
					{
						// TODO 点击了提交，首先上传图片，循环调用上传的方法
						for (String key : imgsMap.keySet())
						{
							handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
							OkHttpUtils.getInstance().uploadImg(ctx,
									imgsMap.get(key), handler);
							handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				} else
				{
					// 如果没有选择图片，直接提交
					ComplaintCommit();
				}
				// updateContent(complaintChoice, mTelephone, mDescribe);
				// 跳往我的投诉列表页
				// ctx.jumpToMyComplaintListFregment();

			}
		});

		return view;
	}

	private int picIndex = 1;
	private String img1Key = "img1";
	private String img2Key = "img2";

	@SuppressLint("DefaultLocale")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 200 && resultCode == Activity.RESULT_OK)
		{
			String picturePath="";
			if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				Log.e("URI",uri.toString());
			    ContentResolver cr =ctx.getContentResolver();
			    try{
			    	// Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

		                int sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		                Log.d("sdkVersion:", String.valueOf(sdkVersion));
		                Log.d("KITKAT:", String.valueOf(Build.VERSION_CODES.KITKAT));
		                if (sdkVersion >= 19) {  // 或者 android.os.Build.VERSION_CODES.KITKAT这个常量的值是19
		                	picturePath = uri.getPath();//5.0直接返回的是图片路径 Uri.getPath is ：  /document/image:46 ，5.0以下是一个和数据库有关的索引值
		                    System.out.println("path:" + picturePath);
		                    // path_above19:/storage/emulated/0/girl.jpg 这里才是获取的图片的真实路径
		                    picturePath = ImageUpLoading.getPath_above19(ctx, uri);
		                    System.out.println("path_above19:" + picturePath);
		                } else {
		                	picturePath = ImageUpLoading.getFilePath_below19(ctx,uri);

		                }
		            } catch (Exception e) {
		                Log.e("Exception", e.getMessage(), e);
		            }
			    }
//			Log.d("dd", data.toString());
//			Uri uri = data.getData(); // 返回的结果
//			ContentResolver cr = ctx.getContentResolver();
//			String[] filePathColumn =
//			{ MediaStore.Images.Media.DATA };
//			Cursor cursor = ctx.getContentResolver().query(uri, filePathColumn,
//					null, null, null);
//			if (cursor == null)
//			{
//				System.out.print("cursor :" + null);
//				return;
//
//			}
//			System.out.println("cursor :" + cursor);
//			cursor.moveToFirst();
//			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//			String picturePath = cursor.getString(columnIndex);
//			if (!picturePath.toLowerCase().endsWith(".jpg")
//					&& !picturePath.toLowerCase().endsWith(".jpeg"))
//			{
//				Toast.makeText(ctx.getApplicationContext(), "请选择jpg格式的图片",
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
			try
			{
				Toast.makeText(ctx, picturePath, Toast.LENGTH_SHORT).show();

				if (picIndex == 1)
				{
					picIndex = 2;
			 		imagePath1=picturePath;
				    String image1= patrUri(Long.toString(System.currentTimeMillis()));
					Bitmap bt= compressBySize(imagePath1,1080,1920);
					saveFile(bt,image1);
					imgsMap.put(img1Key, image1);
					//addimage1.setImageBitmap(bt);
					Glide.with(ctx)
					.load(imagePath1)
					.fitCenter()
					.dontAnimate()
				    .diskCacheStrategy(DiskCacheStrategy.RESULT)
				    .into(addimage1); 
					bt.recycle();
					//  在这里将图1的绝对路径保存进去
//					imgsMap.put(img1Key, picturePath);
//					imagePath1 = picturePath;
//					ShowImg(imagePath1, addimage1);
//					picIndex = 2;
				} else if (picIndex == 2)
				{
					picIndex = 1;		
				    imagePath2=picturePath;
				    String image2=	patrUri(Long.toString(System.currentTimeMillis()));
					Bitmap bt=  compressBySize(imagePath2,1080,1920);
				    saveFile(bt,image2);
					imgsMap.put(img2Key,image2);	
					bt.recycle();
					Glide.with(ctx)
					.load(imagePath2)
					.fitCenter()
					.dontAnimate()
				    .diskCacheStrategy(DiskCacheStrategy.RESULT)
				    .into(addimage2);
					//addimage2.setImageBitmap(bt);
					// 在这里将图1的绝对路径保存进去
//					imgsMap.put(img2Key, picturePath);
//					imagePath2 = picturePath;
//					ShowImg(imagePath2, addimage2);
//					picIndex = 1;
				}

			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// images.add(picturePath);// 上面是为了获取图片的路径
			// setAdapter(new MyAdapter(UploadActivity.this));
			Log.d("dd", picturePath);

		}
	}
	
	 /**
	  * // 压缩图片尺寸
	  * @param pathName
	  * @param targetWidth
	  * @param targetHeight
	  * @return
	  */
	  private  Bitmap compressBySize(String pathName, int targetWidth,
	            int targetHeight) {
	        BitmapFactory.Options opts = new BitmapFactory.Options();
	        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
	        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
	        // 得到图片的宽度、高度；
	        float imgWidth = opts.outWidth;
	        float imgHeight = opts.outHeight;
	        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
	        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
	        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
	        opts.inSampleSize = 1;
	        if (widthRatio > 1 || widthRatio > 1) {
	            if (widthRatio > heightRatio) {
	                opts.inSampleSize = widthRatio;
	            } else {
	                opts.inSampleSize = heightRatio;
	            }
	        }
	        // 设置好缩放比例后，加载图片进内容；
	        opts.inJustDecodeBounds = false;
	        bitmap = BitmapFactory.decodeFile(pathName, opts);
	        return bitmap;
	    }
	 
	 
	    /**
	     * //保存压缩后的图片
	     * @param bitmap
	     * @param filePath2
	     * @throws IOException
	     */
	    private  void saveFile(Bitmap bitmap, String filePath2) throws IOException {
	        // TODO Auto-generated method stub
	        File testFile = new File(filePath2);
	        if (testFile.exists()) {
	            testFile.delete();
	        }

	        File myCaptureFile = new File(filePath2);
	        System.out.println("------filePath2==" + filePath2);
	        BufferedOutputStream bos = new BufferedOutputStream(
	                new FileOutputStream(myCaptureFile));
	        // 100表示不进行压缩，70表示压缩率为30%
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
	        bos.flush();
	        bos.close();
	    }
	    
	    /**
	     * 图片保存路径  这里是在SD卡目录下创建了MyPhoto文件夹
	     * @param fileName
	     * @return
	     */
	    private  String patrUri(String fileName) {  //指定了图片的名字，可以使用时间来命名
	        // TODO Auto-generated method stub
	        String strPhotoName = fileName+".jpg";    
	        String imagePath;
	        String savePath = Environment.getExternalStorageDirectory().getPath()
	                + "/MyPhoto/";
	        File dir = new File(savePath);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }
	        Uri.fromFile(new File(dir, strPhotoName));
	        imagePath = savePath + strPhotoName;
	        return imagePath;
	    }

	public void ShowImg(String uri, ImageView headimage2) throws IOException
	{
		FileInputStream fs = new FileInputStream(uri);
		BufferedInputStream bs = new BufferedInputStream(fs);
		Bitmap btp = BitmapFactory.decodeStream(bs);
		headimage2.setImageBitmap(btp);
		bs.close();
		fs.close();
		btp = null;
	}

	private void ComplaintCommit()
	{

		new Thread()
		{
			@Override
			public void run()
			{

				// 取得页面上的参数以及应用已经存储的参数
				String res = null;
				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");
				// 尝试post请求
				tryPost(complaintChoice, mTelephone, mDescribe, token,
						singnature, st);
				System.out.println("上传数据是：" + complaintChoice + mTelephone
						+ mDescribe);
			}
		}.start();
	}

	/**
	 * 尝试异步post方式进行提交
	 */
	private void tryPost(int complaintChoice, String mTelephone,
			String mDescribe, String token, String singnature, String st)
	{
		// 尝试使用异步post请求进行
		HashMap<String, String> map = new HashMap<String, String>();
		// 现在开始构造参数
		String Choice = String.valueOf(complaintChoice);
		map.put("title", mDescribe);
		map.put("type", Choice);
		map.put("contacts", mTelephone);
		map.put("content", mDescribe);
		for (int i = 0; i < uploadResultList.size(); i++)
		{
			map.put("attArry[" + i + "].filePath", uploadResultList.get(i)
					.getAttachmentUrl());
			map.put("attArry[" + i + "].fileName", uploadResultList.get(i)
					.getAttachmentName());
			map.put("attArry[" + i + "].fileSize", uploadResultList.get(i)
					.getSize());
			map.put("attArry[" + i + "].fileType", uploadResultList.get(i)
					.getType());
		}

		String http = "/admin/proposal/addSave.do?token=" + token
				+ "&singnature=" + singnature + "&st=" + st;
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
		OkHttpUtils.getInstance().doPostAsync(ctx, http, map, handler);
	}
}
