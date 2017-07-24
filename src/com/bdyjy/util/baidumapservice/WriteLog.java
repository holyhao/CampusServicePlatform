package com.bdyjy.util.baidumapservice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.os.Environment;


/***
 * @author baidu
 *
 */
public class WriteLog {
	private static WriteLog instance = null;
	private File file = null;
	private FileWriter writer;
	private final String LOG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/loc.log";
	private String timeStr = null;

	public static WriteLog getInstance() {
		if (instance == null)
			instance = new WriteLog();
		return instance;
	}

	public void init() {
		try {
			file = new File(LOG_PATH);
			if (!file.exists())
				file.createNewFile();
			writer = new FileWriter(file,false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeLog(String log) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("{MM-dd HH:mm:ss.SSS}", Locale.CHINA);
		timeStr = simpleDateFormat.format(System.currentTimeMillis());
		try {
			writer.write(timeStr + log+"\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
