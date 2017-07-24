package com.bdyjy.util;

import android.util.Log;

public class LogUtil {

	//可以全局控制是否打印log日志  
    private static boolean isPrintLog = true;  
  
    private static int LOG_MAXLENGTH = 2000;  
  
    public static void LogPrint(String msg) {  
        if (isPrintLog) {  
  
            int strLength = msg.length();  
            int start = 0;  
            int end = LOG_MAXLENGTH;  
            for (int i = 0; i < 100; i++) {  
                if (strLength > end) {  
                    Log.e("print___" + i, msg.substring(start, end));  
                    start = end;  
                    end = end + LOG_MAXLENGTH;  
                } else {  
                    Log.e("print___" + i, msg.substring(start, strLength));  
                    break;  
                }  
            }  
        }  
    }  
  
    public static void LogPrint(String type, String msg) {  
  
        if (isPrintLog) {  
  
            int strLength = msg.length();  
            int start = 0;  
            int end = LOG_MAXLENGTH;  
            for (int i = 0; i < 100; i++) {  
                if (strLength > end) {  
                    Log.e(type + "___" + i, msg.substring(start, end));  
                    start = end;  
                    end = end + LOG_MAXLENGTH;  
                } else {  
                    Log.e(type + "___" + i, msg.substring(start, strLength));  
                    break;  
                }  
            }  
        }  
    }  
  
}  


