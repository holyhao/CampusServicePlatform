package com.bdyjy.fragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bdyjy.R;
import com.bdyjy.activity.KeyDown;
import com.bdyjy.activity.MainActivity;
import com.bdyjy.constants.HandlerOrder;
import com.bdyjy.entity.personalinfo.PersonalInfoContentQueryResultBean;
import com.bdyjy.entity.upload.ImgUploadResultBean;
import com.bdyjy.util.ImageUpLoading;
import com.bdyjy.util.OkHttpUtils;
import com.bdyjy.util.RoundImageView;
import com.bdyjy.util.SPUtils;

/**
 * holy个人信息修改页面
 * 
 * @author
 */
@SuppressLint("NewApi")
public class PersonalInfoModifyFragment extends Fragment
{
	private TextView tv_back;
	private RoundImageView headimage;
	private TextView userName;
	private String mUserName;
	private TextView identifierNumber;
	private EditText email;
	private String mEmail;
	private TextView college;
	private TextView major;
	private EditText tel;
	private String mTel;
	private RelativeLayout submit;

	private String userImg = "";
	private String imagePath1 = null;
	private String imagePath2 = null;
	private ImageView addimage1;
	private ImageView addimage2;
	private Map<String, String> imgsMap = new HashMap<String, String>();
	private List<ImgUploadResultBean> uploadResultList = new ArrayList<ImgUploadResultBean>();

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
					// loadData();
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
						userImg = is.getAttachmentUrl();
					} else
					{
						Toast.makeText(ctx, "上传报错：App_result_key！=0",
								Toast.LENGTH_LONG).show();
					}

					if (uploadResultList.size() == imgsMap.size())// 如果所有图片都已经上传完成
					{
						InfoCommit();
					}

					break;
				case HandlerOrder.POST_OK:
					// 在这里解析post之后的结果
					handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_HIDE);
					// 返回值将会是JSON格式的数据，我要在这里解析
					String res = msg.getData().get("body").toString();
					System.out.println("post_result:\n" + res);
					PersonalInfoContentQueryResultBean bean = JSON.parseObject(
							res, PersonalInfoContentQueryResultBean.class);
					String app_result_key = bean.getApp_result_key();
					if ("0".equals(app_result_key))
					{
						toastMsg = "信息提交成功";
						handler.sendEmptyMessage(HandlerOrder.TOAST);
						// handler.sendEmptyMessage(HandlerOrder.TO_MAIN);
						// ctx.jumpToMyComplaintListFregment();
						MainActivity.person_info_ifupdate=1;
						ctx.jumpToPersonalCenterFragment();
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

	public PersonalInfoModifyFragment(MainActivity ctx)
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
		ctx.jumpToPersonalInfoFragment();
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.personal_info_modify_fragment,
				null);

		tv_back = (TextView) view.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ctx.jumpToPersonalInfoFragment();
			}
		});

		// 个人信息的设置
		headimage = (RoundImageView) view.findViewById(R.id.headimage);
		String ur1 = MainActivity.personalInfo.getAttachmentPrefix();
		String ur2 = MainActivity.personalInfo.getUserImg();
		// TODO
		headimage.setImageUrl(ur1 + ur2);

		userName = (TextView) view.findViewById(R.id.username);
		userName.setText(MainActivity.personalInfo.getUserName());
		identifierNumber = (TextView) view.findViewById(R.id.identifierNumber);
		identifierNumber.setText(MainActivity.personalInfo
				.getIdentifierNumber());
		email = (EditText) view.findViewById(R.id.email);
		email.setText(MainActivity.personalInfo.getEmail());
		college = (TextView) view.findViewById(R.id.college);
		college.setText(MainActivity.personalInfo.getCollege());
		major = (TextView) view.findViewById(R.id.major);
		major.setText(MainActivity.personalInfo.getMajor());
		tel = (EditText) view.findViewById(R.id.tel);
		tel.setText(MainActivity.personalInfo.getMobile());

		headimage = (RoundImageView) view.findViewById(R.id.headimage);
		headimage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 1);
			}
		});

		submit = (RelativeLayout) view.findViewById(R.id.info_submit);
		submit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				mUserName = userName.getText().toString();
				mEmail = email.getText().toString();
				mTel = tel.getText().toString();
				// Toast.makeText(ctx, mUserName+mEmail+mTel,
				// Toast.LENGTH_SHORT).show();

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
					InfoCommit();
				}
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
		if (requestCode == 1 && resultCode == Activity.RESULT_OK)
		{

			String picturePath="";

				Uri uri = data.getData();
			    ContentResolver cr =ctx.getContentResolver();
			    try{
			    	// Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
		                int sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		                if (sdkVersion >= 19) {  // 或者 android.os.Build.VERSION_CODES.KITKAT这个常量的值是19
		                	picturePath = uri.getPath();//5.0直接返回的是图片路径 Uri.getPath is ：  /document/image:46 ，5.0以下是一个和数据库有关的索引值
		                    // path_above19:/storage/emulated/0/girl.jpg 这里才是获取的图片的真实路径
		                    picturePath = ImageUpLoading.getPath_above19(ctx, uri);
		                } else {
		                	picturePath = ImageUpLoading.getFilePath_below19(ctx,uri);

		                }
		            } catch (Exception e) {
		                Log.e("Exception", e.getMessage(), e);	            
			    }
			    
			try
			{
				   imagePath1=picturePath;
				   String image1= patrUri(Long.toString(System.currentTimeMillis()));
				   Bitmap bt= compressBySize(imagePath1,1080,1920);
				   saveFile(bt,image1);
				   imgsMap.put(img1Key, image1);
				   ShowImg(image1, headimage);
				   bt.recycle();				  
			   //  imgsMap.put(img1Key, picturePath);
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

	private void InfoCommit()
	{

		new Thread()
		{
			@Override
			public void run()
			{
				System.out.println("修改信息提交的数据是:" + userImg + mUserName + mEmail
						+ mTel);
				// 取得页面上的参数以及应用已经存储的参数
				String res = null;
				// 从sharePreference中取出之前存储的参数
				String token = (String) SPUtils.get(ctx, "token", "");
				String singnature = (String) SPUtils.get(ctx, "singnature", "");
				String st = (String) SPUtils.get(ctx, "st", "");
				// 尝试post请求
				tryPost(userImg, mUserName, mEmail, mTel, token, singnature, st);
			}
		}.start();
	}

	/**
	 * 尝试异步post方式进行提交
	 */
	private void tryPost(String userImg, String mUserName, String mEmail,
			String mTel, String token, String singnature, String st)
	{
		// 尝试使用异步post请求进行
		HashMap<String, String> map = new HashMap<String, String>();
		// 现在开始构造参数(获取)
		map.put("userName", mUserName);

		// map.put("tel", mTel);
		// map.put("mobile","123");
		map.put("tel", "123");
		map.put("mobile", mTel);

		map.put("email", mEmail);
		map.put("userImg", userImg);
		// 现在开始构造参数(不变的)
		map.put("college", MainActivity.personalInfo.getCollege());
		map.put("grade", MainActivity.personalInfo.getGrade());
		map.put("major", MainActivity.personalInfo.getMajor());
		map.put("department", MainActivity.personalInfo.getDepartment());
		map.put("sex", MainActivity.personalInfo.getSex());
		System.out.println("map:" + map);
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

		String http = "/admin/clientuser/updateSave.do?token=" + token
				+ "&singnature=" + singnature + "&st=" + st;
		handler.sendEmptyMessage(HandlerOrder.PROCESSBAR_SHOW);
		OkHttpUtils.getInstance().doPostAsync(ctx, http, map, handler);
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
    
}
