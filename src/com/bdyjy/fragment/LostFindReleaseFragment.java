/**
 * 失物招领发布界面
 * created by songdebin 
 * 
 * ***/

package com.bdyjy.fragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.ReleaseBean;
import com.bdyjy.entity.upload.ImgUploadResultBean;
import com.bdyjy.util.ImageFactory;
import com.bdyjy.util.ImageUpLoading;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class LostFindReleaseFragment extends Fragment
{
    private String filePath;
	private MainActivity ctx;
	private TextView tv_back;
	private Button btn_my; 
	private Button btn_commit;
	Handler handler = null;
	// 用于显示状态之间的切换
	private RelativeLayout l1;
	private RelativeLayout l2;

	private String imagePath1 = null;
	private String imagePath2 = null;
	private ImageView addimage1;
	private ImageView addimage2;
	private RelativeLayout lv_addImage;

	private TextView tv_tag1;
	private TextView tv_tag2;

	private View view_tag1;
	private View view_tag2;

	private EditText et_title;
	private EditText et_contacts;
	private EditText et_tel;
	private int type = 2;
	private EditText et_description;
	private int source;
	private List<ImgUploadResultBean> uploadResultList = new ArrayList<ImgUploadResultBean>();
	private Map<String, String> imgsMap = new HashMap<String, String>();

	public LostFindReleaseFragment(MainActivity ctx, int source)
	{
		this.ctx = ctx;
		this.source = source;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};	
	}
	
	/**********返回功能**********/
	private void Back(){ 
		if (source == Const.FRAGMENT2_ID)
		{
			ctx.jumpToClickById(Const.FRAGMENT_LOSTFINDLIST_ID);
		} else
		{
			ctx.jumpToMyLost(source);
		}
	}

	String toastMsg = null;

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
					// TODO
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.TO_MAIN:
					//btn_my.performClick();
					break;
				case HandlerOrder.PROCESSBAR_SHOW: //进度滚动条
					ctx.showRoundProcessDialog();
					break;
				case HandlerOrder.PROCESSBAR_HIDE://进度滚动条
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.UPLOAD_OK:  //图片上传 成功
					ctx.hideRoundProcessDialog();
					
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

					if (uploadResultList.size() == imgsMap.size())// 如果所有图片都已经上传完成 开始上传文字信息
					{
						Commit(); 
					}

					break;
				case HandlerOrder.POST_OK:  //信息上传成功
					 ctx.hideRoundProcessDialog();	
					 String res;
					 res=msg.getData().getString("body");
					 ReleaseBean bean = JSON.parseObject(res,ReleaseBean.class);					
						String app_result_key = bean.getApp_result_key();					
						if ("0".equals(app_result_key)) //返回的信息
						{
							toastMsg = "信息提交成功";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
							//自动跳转二手交易列表
							if (source == Const.FRAGMENT2_ID)
							{
								ctx.jumpToClickById(Const.FRAGMENT_LOSTFINDLIST_ID);
							} else
							{
								ctx.jumpToMyLost(source);
							}							
						}else
						{
							toastMsg = "信息提交失败，发布内容仅限于汉子英文以及标点符号";
							handler.sendEmptyMessage(HandlerOrder.TOAST);
						} 					
					break;
				case HandlerOrder.POST_ERROR:  //信息上传成功
					toastMsg = "信息提交失败，请检查你的网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					ctx.hideRoundProcessDialog();
					break;
				case HandlerOrder.UPLOAD_ERROR:
					toastMsg = "信息提交失败，请检查你的网络";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					ctx.hideRoundProcessDialog();
					break;		
				}
			}
		};
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.lost_release_fragment, null);
		initHandler();

		// 几个输入控件的初始化
		et_title = (EditText) view.findViewById(R.id.et_title_lostfind_release);
		et_contacts = (EditText) view
				.findViewById(R.id.et_contacts_lostfind_release);
		et_tel = (EditText) view.findViewById(R.id.et_tel_lostfind_release);
		et_description = (EditText) view
				.findViewById(R.id.et_description_lostfind_release);

		// 返回上一界面 失物招领列表
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (source == Const.FRAGMENT2_ID)
				{
					ctx.jumpToClickById(Const.FRAGMENT_LOSTFINDLIST_ID);
				} else
				{
					ctx.jumpToMyLost(source);
				}
			}
		});

		addimage1 = (ImageView) view
				.findViewById(R.id.iv_show1_lostfind_release);
		addimage2 = (ImageView) view
				.findViewById(R.id.iv_show2_lostfind_release);

		// 添加图片
		lv_addImage = (RelativeLayout) view
				.findViewById(R.id.lv_addimage_lostfind_release);
		lv_addImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// 开启Pictures画面Type设定为image
				intent.setType("image/*");
				// 使用Intent.ACTION_GET_CONTENT这个Action
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 200);
			}
		});
		
		

		if (source == Const.FRAGMENT2_ID)
		{

			// 跳转至我的失物招领
			btn_my = (Button) view.findViewById(R.id.setting);
			btn_my.setVisibility(View.VISIBLE);
			btn_my.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ctx.jumpToMyLost(Const.FRAGMENT2_ID);
				}
			});
		} else
		{

		}

		// 定义数据提交的按键
		btn_commit = (Button) view.findViewById(R.id.btn_lostfind_release);
		btn_commit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String title = et_title.getText().toString().trim();			
				String tel = et_tel.getText().toString().trim();
				String contacts = et_contacts.getText().toString().trim();
				String description = et_description.getText().toString().trim();
				if(TextUtils.isEmpty(title)){
					toastMsg = "请填写主题";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}else if(TextUtils.isEmpty(tel)){
					toastMsg = "请填写你的联系方式";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}else if(TextUtils.isEmpty(contacts)){
					toastMsg = "请填写联系人";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}else  if(TextUtils.isEmpty(description)){
					toastMsg = "请输入描述";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}else{
				// 如果有图片就上传图片	
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
									imgsMap.get(key), handler); //上传图片
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}else{// 如果没有图片直接上传文字信息
				  Commit();
				}	
			
			}
				
			
			}
		});

		initTab(view);
		return view;
	}

	/**
	 * 初始化选项卡
	 * 
	 * @param view
	 */
	private void initTab(View view)
	{
		// 新闻类型切换
		l1 = (RelativeLayout) view.findViewById(R.id.ly_lost1);
		l2 = (RelativeLayout) view.findViewById(R.id.ly_lost2);

		tv_tag1 = (TextView) view.findViewById(R.id.tv_lost_tag1);
		tv_tag2 = (TextView) view.findViewById(R.id.tv_lost_tag2);

		view_tag1 = (View) view.findViewById(R.id.view_lost_tag1);
		view_tag2 = (View) view.findViewById(R.id.view_lost_tag2);

		// 每次切换，都访问后台，然后加载到列表中
		l1.setOnClickListener(new OnClickListener()
		{
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v)
			{
				// 并且将当前选中的项字体加粗变色，其他的回归原style
				tv_tag1.setTextColor(0xFF5A5A5A);
				tv_tag2.setTextColor(0xFF9A9A9A);

				view_tag1.setBackgroundColor(0xFF5A5A5A);
				view_tag2.setBackgroundColor(0x005A5A5A);

				type = 2;

			}
		});

		l2.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				// 并且将当前选中的项字体加粗变色，其他的回归原style
				tv_tag1.setTextColor(0xFF9A9A9A);
				tv_tag2.setTextColor(0xFF5A5A5A);

				view_tag1.setBackgroundColor(0x005A5A5A);
				view_tag2.setBackgroundColor(0xFF5A5A5A);

				type = 1;

			}
		});

	}

	
	/**
	 * 
	 *提交数据包括图片和信息
	 * songdebin
	 * */		
	public void Commit(){
		
		new Thread()  //首先上传文字信息
		{
			@Override
			public void run()
			{

				String res = null;

				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");				

				String title = et_title.getText().toString().trim();			
				String tel = et_tel.getText().toString().trim();
				String contacts = et_contacts.getText().toString().trim();
				String description = et_description.getText().toString().trim();
				// 尝试post请求
				tryPost(title, tel, contacts,type, description, token,
						singnature, st);				
			}
		}.start();					
	}	
	
	private void tryPost(String title, String tel,String contacts,int type, 
			 String description, String token,
			String singnature, String st)
	{
		// 尝试使用异步post请求进行
		HashMap<String, String> map = new HashMap<String, String>();
		// 现在开始构造参数
		map.put("title", title);
		map.put("tel", tel);  
		//map.put("contacts",contacts);
		map.put("description", description);
		for (int i = 0; i < uploadResultList.size(); i++)  //图片信息
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

		String http = "/admin/lostfound/addSave.do?token=" + token +"&status="+0  //******************************路径没有做好
				+ "&singnature=" + singnature + "&st=" + st+"&type="+type;
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
		OkHttpUtils.getInstance().doPostAsync(ctx, http, map, handler);
		System.out.println("提交的内容是"+http+"   "+map);
	}
	
				
	// 从本地获取图片
	@SuppressLint("DefaultLocale")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method  stub
		super.onActivityResult(requestCode, resultCode, data);
			String picturePath="";
			if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				Log.e("URI",uri.toString());
			    ContentResolver cr =ctx.getContentResolver();
			    try{
			    // 	 Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

		                int sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		                Log.d("sdkVersion:", String.valueOf(sdkVersion));
		                Log.d("KITKAT:", String.valueOf(Build.VERSION_CODES.KITKAT));
		                if (sdkVersion >= 19) {  // 或者 android.os.Build.VERSION_CODES.KITKAT这个常量的值是19
		                	picturePath = uri.getPath();//5.0直接返回的是图片路径 Uri.getPath is ：  /document/image:46 ，5.0以下是一个和数据库有关的索引值
		                    System.out.println("path:" + picturePath);
		                    // path_above19:/storage/emulated/0/girl.jpg 这里才是获取的图片的真实路径
		                    picturePath =ImageUpLoading.getPath_above19(ctx, uri);
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
//
//			Cursor cursor = ctx.getContentResolver().query(uri, filePathColumn,
//					null, null, null);
//			if (cursor == null)
//			{
//				System.out.print("cursor :" + null);
//				return;
//
//			}
//			System.out.print("cursor :" + cursor);
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
				//Toast.makeText(ctx, picturePath, Toast.LENGTH_SHORT).show();

				if (picIndex == 1) {
					picIndex = 2;
			 		imagePath1=picturePath;
				    String image1= patrUri(Long.toString(System.currentTimeMillis()));
					Bitmap bt= compressBySize(imagePath1,1080,1920);
					saveFile(bt,image1);
					imgsMap.put(img1Key, image1);
				
					bt.recycle();
				      Glide.with(ctx)
						.load(imagePath1)
						.fitCenter()
						.dontAnimate()
					    .diskCacheStrategy(DiskCacheStrategy.RESULT)
					    .into(addimage1);
				   // ShowImg(imagePath1, addimage1,a,b);
				  //addimage1.setImageBitmap(bt);
				}else if(picIndex==2){
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
// 				      ShowImg(imagePath2, addimage2,a,b);
//				      addimage2.setImageBitmap(bt);
				
				}

			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// images.add(picturePath);// 上面是为了获取图片的路径
			// setAdapter(new MyAdapter(UploadActivity.this));
			Log.d("dd", picturePath);
	}
	
	private int picIndex = 1;
	private String img1Key = "img1";
	private String img2Key = "img2";

	
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
	

	
	public void ShowImg(String uri, ImageView headimage2,float a,float b) throws IOException
	{
		FileInputStream fs = new FileInputStream(uri);
		BufferedInputStream bs = new BufferedInputStream(fs);
		Bitmap btp = BitmapFactory.decodeStream(bs);
		Bitmap bt = ratio(btp,a,b);
		headimage2.setImageBitmap(bt);
		bs.close();
		fs.close();
		btp = null;
		bt = null;
	
	}		
	
	
	
	 public Bitmap ratio(Bitmap imgPath, float pixelW, float pixelH) {  
	       ByteArrayOutputStream os = new ByteArrayOutputStream();  
	       imgPath.compress(Bitmap.CompressFormat.JPEG, 100, os);  
	        if( os.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出      
	            os.reset();//重置baos即清空baos    
	            imgPath.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中    
	        }    
	        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());    
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();    
	        //开始读入图片，此时把options.inJustDecodeBounds 设回true了    
	        newOpts.inJustDecodeBounds = true;  
	        newOpts.inPreferredConfig = Config.RGB_565;  
	        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);    
	        newOpts.inJustDecodeBounds = false;    
	        int w = newOpts.outWidth;    
	        int h = newOpts.outHeight;    
	        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了  
	        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了  
	        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可    
	        int be = 1;//be=1表示不缩放    
	        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放    
	            be = (int) (newOpts.outWidth / ww);    
	        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放    
	            be = (int) (newOpts.outHeight / hh);    
	        }    
	        if (be <= 0) be = 1;    
	        newOpts.inSampleSize = be;//设置缩放比例    
	        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了    
	        is = new ByteArrayInputStream(os.toByteArray());    
	        bitmap = BitmapFactory.decodeStream(is, null, newOpts);  
	        //压缩好比例大小后再进行质量压缩  
//	      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除  
	        return bitmap;  
	    }  
	 
	
	 
	 
	/*
	 * (non-Javadoc)
	 * fragment 销毁时调用
	 * @see android.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(null, 0);
		super.onDestroy();
	}

}
