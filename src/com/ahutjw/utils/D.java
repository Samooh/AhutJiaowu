package com.ahutjw.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 调试类
 * 
 * 
 */
public class D {
	public static final boolean debug = true;

	/**
	 * 打印：System.out
	 * 
	 * @param msg
	 */
	public static String getNetworkType(android.content.Context context) {
	    String netType = null;
	    ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo ni = cm.getActiveNetworkInfo();
	    if (null != ni) {
	        int nType = ni.getType();
	        if (nType == ConnectivityManager.TYPE_MOBILE&&(ni.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
	                        || ni.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || ni.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)) {
	            netType="2G/3G";
	        } else {
	            netType="else";
	        }
	    }
	    System.out.println("---->>newsType="+netType);
	    if(netType==null) 
	    	return "else"; 
	    return netType;
	}

	public static void out(String msg) {
		if (debug)
			System.out.println(msg);
	}
}
