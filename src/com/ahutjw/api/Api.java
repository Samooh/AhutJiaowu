package com.ahutjw.api;

import com.ahutjw.utils.D;

/**
 * 一些状态信息
 * 
 * @author 
 * 
 */
public class Api {
	public static String errorStr;// 错误内容

	/**
	 * 分析错误异常，错误内容保存在 errorStr
	 * 
	 * @param e
	 */
	public static void analyseError(Exception e) {
		if (e instanceof org.apache.http.conn.ConnectTimeoutException) {
			errorStr = "请求超时";
		} else if (e instanceof java.io.IOException) {
			errorStr = "无法连接到远程服务器";
		} else if (e instanceof org.apache.http.client.ClientProtocolException) {
			errorStr = "不合理的请求协议";
		} else if (e instanceof org.xmlpull.v1.XmlPullParserException) {
			errorStr = "数据解析错误";
		} else {
			D.out("erClass>>>" + e.getCause());
			errorStr = "请求失败";
		}
	}
}
