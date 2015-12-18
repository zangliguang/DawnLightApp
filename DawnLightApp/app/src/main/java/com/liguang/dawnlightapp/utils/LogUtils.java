
package com.liguang.dawnlightapp.utils;

import android.util.Log;

import com.liguang.dawnlightapp.constants.LocalConstants;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by DawnLight on 15/10/19.
 */
public class LogUtils {
    static String logLabel = "DawnLight";
    public static void DebugerTest(String msg) {
        if(LocalConstants.DEBUGE){
            Log.v(logLabel, msg);
        }
    }
    public static void DebugerError(String msg,Throwable error) {
        if(LocalConstants.DEBUGE){
            Log.v(logLabel, msg + " Exception: " + getExceptionMsg(error));
        }
    }

    private static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter(1024);
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }
}
