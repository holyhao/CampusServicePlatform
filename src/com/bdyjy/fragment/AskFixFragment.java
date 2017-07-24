/**
 * 维护报修主页
 * created by songdebin 
 * 2016-04-06
 * ***/

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

import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.Const;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.fix.MyFixContentBean;
import com.bdyjy.entity.fixtype.FixType;
import com.bdyjy.entity.fixtype.Type;
import com.bdyjy.entity.upload.ImgUploadResultBean;
import com.bdyjy.util.HttpXmlClient;
import com.bdyjy.util.ImageUpLoading;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.PropetiesFileReaderUtil;
import com.bdyjy.util.SPUtils;
import com.bdyjy.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

@SuppressLint("ValidFragment") 
public class AskFixFragment extends Fragment
{
	private MainActivity ctx;
	private TextView tv_back;
	private Button btn_my;
	private Button btn_commit;
	private ArrayList list;// 记录跳转的路径
	private Spinner sp_type;

	private String imagePath1 = null;
	private String imagePath2 = null;
	private ImageView addimage1;
	private ImageView addimage2;
	private RelativeLayout lv_addImage;

	private EditText et_title;
	private EditText et_location;
	private EditText et_tel;
	private EditText et_description;

	private Map<String, String> imgsMap = new HashMap<String, String>();
	private List<ImgUploadResultBean> uploadResultList = new ArrayList<ImgUploadResultBean>();
	private int imgCount = 2;
	private View mainView;
	private List<com.bdyjy.entity.fixtype.Type> TypeList;
	private Map<String, String> TypeMap;

	public AskFixFragment(ArrayList list, MainActivity ctx)
	{
		this.list = list;
		this.ctx = ctx;
		ctx.keydown=new KeyDown(){
			public void OnkeyDown(){//在这里重写 返回事件
				Back();
			}			
		};		
	}
	
	/**********返回功能**********/
	private void Back(){ 
		ctx.backToClickWithId(list);	
	}

	Handler handler = null;
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
					Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG).show();
					break;
				case HandlerOrder.TO_MAIN:
					btn_my.performClick();
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
						FixCommit();
					}

					break;
				case HandlerOrder.UPDATE_LISTVIEW:
					TypeMap = new HashMap<String, String>();
					List<String> typeNameList = new ArrayList<String>();
					for (Type t : TypeList)
					{
						TypeMap.put(t.getText(), t.getId());
						typeNameList.add(t.getText());
					}

					String type_spin[] = typeNameList.toArray(new String[] {});

					// 设置下拉菜单的样式
					sp_type = (Spinner) mainView
							.findViewById(R.id.spinner_askfix);
					ArrayAdapter<String> ad = new ArrayAdapter<String>(ctx,
							R.layout.fix_type, type_spin);
					sp_type.setAdapter(ad);
					Log.i("spinner", sp_type.getSelectedItem().toString());
					break;
				case HandlerOrder.POST_OK:
					// 在这里解析post之后的结果
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					// 返回值将会是JSON格式的数据，我要在这里解析
					String res = msg.getData().get("body").toString();
					System.out.println("post_result:\n" + res);
					MyFixContentBean bean = JSON.parseObject(res,
							MyFixContentBean.class);
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))
					{
						toastMsg = "信息提交成功";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						handler.sendEmptyMessage(HandlerOrder.TO_MAIN);

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

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.askfix_fragment, null);
		initHandler();
		mainView = view;

		// 返回上一界面 主界面
		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.backToClickWithId(list);
			}
		});

		// 几个输入控件的初始化
		et_title = (EditText) view.findViewById(R.id.et_title_fix_release);
		et_location = (EditText) view
				.findViewById(R.id.et_location_fix_release);
		et_tel = (EditText) view.findViewById(R.id.et_tel_fix_release);
		et_description = (EditText) view
				.findViewById(R.id.et_description_fix_release);

		// 调用方法访问设置下拉的样式
		getTypes();

		btn_commit = (Button) view.findViewById(R.id.btn_fix_release);
		btn_commit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO 暂时屏蔽提交
				String title = et_title.getText().toString().trim();			
				String tel = et_tel.getText().toString().trim();
				String location = et_location.getText().toString().trim();
				String description = et_description.getText().toString().trim();
				

				if(TextUtils.isEmpty(title)){
					toastMsg = "请填写主题";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}else if(TextUtils.isEmpty(tel)){
					toastMsg = "请填写你的联系方式";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}else if(TextUtils.isEmpty(location)){
					toastMsg = "请填你的地址";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}else  if(TextUtils.isEmpty(description)){
					toastMsg = "请输入描述";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}else{
				
				
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

						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				} else
				{
					// 如果没有选择图片，直接提交
					FixCommit();
				}

			}
			}
		});

		// 初始化两个放置图片的地方
		addimage1 = (ImageView) view.findViewById(R.id.iv_show1_fix_release);
		addimage2 = (ImageView) view.findViewById(R.id.iv_show2_fix_release);

		// 添加图片
		lv_addImage = (RelativeLayout) view
				.findViewById(R.id.lv_addimage_fix_release);
		lv_addImage.setOnClickListener(new OnClickListener()
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

		// 跳转至我的维护报修
		btn_my = (Button) view.findViewById(R.id.setting);
		btn_my.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToClickWithId(Const.FRAGMENT_MYFIX_ID, list);
			}
		});
		return view;
	}

	/**
	 * 
	 * 提交维护保修数据
	 * 
	 */
	public void FixCommit()
	{
		new Thread()
		{
			@Override
			public void run()
			{

				// 取得页面上的参数以及应用已经存储的参数
				String res = null;
				String type1 = sp_type.getSelectedItem().toString().trim();
				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");

				// int type = (type1 == "网络") ? 1 : (type1 == "宿舍") ? 2 : 3;
				String type = TypeMap.get(type1);

				String title = et_title.getText().toString().trim();
				String tel = et_tel.getText().toString().trim();
				String location = et_location.getText().toString().trim();
				String description = et_description.getText().toString().trim();
				// 尝试post请求
				tryPost(title, type, tel, location, description, token,
						singnature, st);

				// tryPost2(title, type, tel, location, description, token,
				// singnature, st);

				// useHttpClient(title, type, tel, location, description, token,
				// singnature, st);

				// 再尝试一下 带编码的get请求
				// String http = createHttpUrlWithEncoding(title, type, tel,
				// location, description, token, singnature, st);
				//
				// // 尝试未编码的get同步请求
				// // String http = createHttpUrl(title, type, tel, location,
				// // description,
				// // token, singnature, st);
				//
				// try
				// {
				// handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
				// res = OkHttpUtils.getInstance().doGet(ctx, http);
				// handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				// } catch (Exception e)
				// {
				// e.printStackTrace();
				// toastMsg = "信息提交失败，请重试";
				// handler.sendEmptyMessage(HandlerOrder.TOAST);
				// return;
				// }
				// System.out.println("提交返回值：" + res.trim());
				//
				// // 返回值将会是JSON格式的数据，我要在这里解析
				// MyFixContentBean bean = JSON.parseObject(res,
				// MyFixContentBean.class);
				// String app_result_key = bean.getApp_result_key();
				// if ("0".equals(app_result_key))
				// {
				// toastMsg = "信息提交成功";
				// handler.sendEmptyMessage(HandlerOrder.TOAST);
				// handler.sendEmptyMessage(HandlerOrder.TO_MAIN);
				//
				// } else
				// {
				// toastMsg = bean.getApp_result_message_key();// "信息提交失败，请重试";
				// handler.sendEmptyMessage(HandlerOrder.TOAST);
				// }
			}
		}.start();
	}

	/**
	 * 查询维护保修类别
	 */
	public void getTypes()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				// 试试get请求
				String res = null;
				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");

				String http = "/admin/maintenance/initAdd.do?token=" + token
						+ "&singnature=" + singnature + "&st=" + st;

				System.out.println(http);
				try
				{
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
					res = OkHttpUtils.getInstance().doGet(ctx, http);
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
				} catch (Exception e)
				{
					e.printStackTrace();
					toastMsg = "获取失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
					return;
				}
				System.out.println("提交返回值：" + res.trim());

				// 返回值将会是JSON格式的数据，我要在这里解析
				FixType bean = JSON.parseObject(res, FixType.class);
				String app_result_key = bean.getApp_result_key();
				if ("0".equals(app_result_key))
				{
					//
					TypeList = bean.getTypeData();

					handler.sendEmptyMessage(HandlerOrder.UPDATE_LISTVIEW);
				} else
				{
					toastMsg = "获取维护保修类别失败";
					handler.sendEmptyMessage(HandlerOrder.TOAST);
				}
			}
		}.start();
	}

	// 从本地获取图片
	@SuppressLint("DefaultLocale")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 200 && resultCode == Activity.RESULT_OK)
		{
			String picturePath="";
			if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				Log.e("URI",uri.toString());
			    ContentResolver cr =ctx.getContentResolver();
			    try{
			    	//  Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

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
//
//			Cursor cursor = ctx.getContentResolver().query(uri, filePathColumn,
//					null, null, null);
//			if (cursor == null)
//			{
//				System.out.print("cursor :" + null);
//				return;
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
			
			try
			{
				//Toast.makeText(ctx, picturePath, Toast.LENGTH_SHORT).show();
				if (picIndex == 1)
				{   
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
					//addimage1.setImageBitmap(bt);
					// 在这里将图1的绝对路径保存进去
//					imgsMap.put(img1Key, picturePath);
//					imagePath1 = picturePath;
//					ShowImg(imagePath1,addimage1);
					
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
					// addimage2.setImageBitmap(bt);
//					// 在这里将图1的绝对路径保存进去
//					imgsMap.put(img2Key, picturePath);
//					imagePath2 = picturePath;
//					ShowImg(imagePath2, addimage2);
//					picIndex = 1;
				}
		

			} catch (IOException e)
			{
				e.printStackTrace();
			}
			Log.d("dd", picturePath);

		}
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

	/*
	 * (non-Javadoc)
	 * 
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

	/**
	 * 构造未重新编码的get请求url
	 * 
	 * @param title
	 * @param type
	 * @param tel
	 * @param location
	 * @param description
	 * @param token
	 * @param singnature
	 * @param st
	 * @return
	 */
	private String createHttpUrl(String title, String type, String tel,
			String location, String description, String token,
			String singnature, String st)
	{
		// 以下是同步未编码的get方式
		String http = "/admin/maintenance/addSave.do?title=" + title + "&type="
				+ type + "&tel=" + tel + "&location=" + location
				+ "&description=" + description + "&token=" + token
				+ "&singnature=" + singnature + "&st=" + st;

		for (int i = 0; i < uploadResultList.size(); i++)
		{
			http += "&attArry[" + i + "].filePath="
					+ uploadResultList.get(i).getAttachmentUrl();
			http += "&attArry[" + i + "].fileName="
					+ uploadResultList.get(i).getAttachmentName();
			http += "&attArry[" + i + "].fileSize="
					+ uploadResultList.get(i).getSize();
			http += "&attArry[" + i + "].fileType="
					+ uploadResultList.get(i).getType();
		}
		return http;
	}

	/**
	 * 尝试带重新编码的get同步请求
	 * 
	 * @param title
	 * @param type
	 * @param tel
	 * @param location
	 * @param description
	 * @param token
	 * @param singnature
	 * @param st
	 * @return
	 */
	private String createHttpUrlWithEncoding(String title, String type,
			String tel, String location, String description, String token,
			String singnature, String st)
	{
		String http = "/admin/maintenance/addSave.do?token=" + token
				+ "&singnature=" + singnature + "&st=" + st;

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair b1 = new BasicNameValuePair("title", title);
		BasicNameValuePair b2 = new BasicNameValuePair("tel", tel);
		BasicNameValuePair b3 = new BasicNameValuePair("location", location);
		BasicNameValuePair b4 = new BasicNameValuePair("description",
				description);
		BasicNameValuePair bx = new BasicNameValuePair("type", type);
		params.add(b3);
		params.add(b2);
		params.add(b1);
		params.add(b4);
		params.add(bx);

		for (int i = 0; i < uploadResultList.size(); i++)
		{
			BasicNameValuePair b5 = new BasicNameValuePair("attArry[" + i
					+ "].filePath", uploadResultList.get(i).getAttachmentUrl());
			BasicNameValuePair b6 = new BasicNameValuePair("attArry[" + i
					+ "].fileName", uploadResultList.get(i).getAttachmentName());
			BasicNameValuePair b7 = new BasicNameValuePair("attArry[" + i
					+ "].fileSize", uploadResultList.get(i).getSize());
			BasicNameValuePair b8 = new BasicNameValuePair("attArry[" + i
					+ "].fileType", uploadResultList.get(i).getType());

			params.add(b5);
			params.add(b6);
			params.add(b7);
			params.add(b8);
		}
		http += "&" + OkHttpUtils.formatParams(params);

		System.out.println("format之后的参数" + OkHttpUtils.formatParams(params));
		return http;
	}

	/**
	 * 尝试异步post方式进行提交
	 * 
	 * @param title
	 * @param type
	 * @param tel
	 * @param location
	 * @param description
	 * @param token
	 * @param singnature
	 * @param st
	 */
	private void tryPost(String title, String type, String tel,
			String location, String description, String token,
			String singnature, String st)
	{
		// 尝试使用异步post请求进行
		HashMap<String, String> map = new HashMap<String, String>();
		// 现在开始构造参数
		map.put("title", title);
		map.put("type", type);
		map.put("tel", tel);
		map.put("location", location);
		map.put("description", description);
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

		String http = "/admin/maintenance/addSave.do?token=" + token
				+ "&singnature=" + singnature + "&st=" + st;
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
		OkHttpUtils.getInstance().doPostAsync(ctx, http, map, handler);
	}

	private void tryPost2(String title, String type, String tel,
			String location, String description, String token,
			String singnature, String st)
	{
		String json = "{\"title\":\"" + title + "\",";
		json += "\"type\":\"" + type + "\",";
		json += "\"tel\":\"" + tel + "\",";
		json += "\"location\":\"" + location + "\",";
		json += "\"description\":\"" + description + "\"}";

		System.out.println(json);

		String url = "/admin/maintenance/addSave.do?token=" + token
				+ "&singnature=" + singnature + "&st=" + st;
		String res = null;
		try
		{
			res = OkHttpUtils.getInstance().post(ctx, url, json);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		// 返回值将会是JSON格式的数据，我要在这里解析
		MyFixContentBean bean = JSON.parseObject(res, MyFixContentBean.class);
		String app_result_key = bean.getApp_result_key();
		if ("0".equals(app_result_key))
		{
			toastMsg = "信息提交成功";
			handler.sendEmptyMessage(HandlerOrder.TOAST);
			handler.sendEmptyMessage(HandlerOrder.TO_MAIN);

		} else
		{
			toastMsg = bean.getApp_result_message_key();// "信息提交失败，请重试";
			handler.sendEmptyMessage(HandlerOrder.TOAST);
		}

	}

	/**
	 * 最后一次的试试看，HttpClient
	 * 
	 * @return
	 */
	private void useHttpClient(String title, String type, String tel,
			String location, String description, String token,
			String singnature, String st)
	{

		String url = "/admin/maintenance/addSave.do?token=" + token
				+ "&singnature=" + singnature + "&st=" + st;
		String ip = PropetiesFileReaderUtil.get(ctx, "ip");
		String port = PropetiesFileReaderUtil.get(ctx, "port");
		String server_project_name = PropetiesFileReaderUtil.get(ctx,
				"server_project_name");

		if (!url.contains(server_project_name))// 如果不包含后台项目名
		{
			url = "/" + server_project_name + url;
		}

		String fullUrl = "http://" + ip + ":" + port + url;

		Map<String, String> params = new HashMap<String, String>();
		params.put("title", title);
		params.put("type", type);
		params.put("tel", tel);
		params.put("location", location);
		params.put("description", description);

		String res = HttpXmlClient.post(fullUrl, params);

		// 返回值将会是JSON格式的数据，我要在这里解析
		MyFixContentBean bean = JSON.parseObject(res, MyFixContentBean.class);
		String app_result_key = bean.getApp_result_key();
		if ("0".equals(app_result_key))
		{
			toastMsg = "信息提交成功";
			handler.sendEmptyMessage(HandlerOrder.TOAST);
			handler.sendEmptyMessage(HandlerOrder.TO_MAIN);

		} else
		{
			toastMsg = bean.getApp_result_message_key();// "信息提交失败，请重试";
			handler.sendEmptyMessage(HandlerOrder.TOAST);
		}
	}
}
