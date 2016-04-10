package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.ahutjw.utils.D;

/**
 * 网络请求
 * 
 */
public class ApiBaseHttp {
	private static final int TIMEOUT_CONNECTION = 8000;
	private static final int TIMEOUT_SOCKET = 8000;
	private static HttpParams httpParameters = new BasicHttpParams();

	//public static String HOST = "http://192.168.1.107:8990/";
	public static String HOST = "http://211.70.149.139:8990/";
	
	/**
	 * 
	 * get 请求
	 * 
	 * @param URL
	 * @return
	 * @throws Exception
	 */
	public static String getURL(String url) throws Exception {
		// 先清除缓存异常信息
		Api.errorStr = "";

		String URL = HOST + url;
		HttpConnectionParams.setConnectionTimeout(httpParameters,TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
		HttpGet request = new HttpGet(URL);
		String strResult = null;
		try {
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParameters);
			HttpResponse httpResponse = defaultHttpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
			defaultHttpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return strResult;
	}
	public static String get(String url) throws Exception {
		// 先清除缓存异常信息
		Api.errorStr = "";
		HttpConnectionParams.setConnectionTimeout(httpParameters,TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
		HttpGet request = new HttpGet(url);
		String strResult = null;
		try {
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParameters);
			HttpResponse httpResponse = defaultHttpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
			defaultHttpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(strResult);
		return strResult;
	}
	/**
	 * 
	 * post 请求
	 * 
	 * @param URL
	 * @param paramNames
	 * @param paramValues
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String[] paramNames,
			String[] paramValues) throws Exception {
		// 先清除缓存异常信息
		Api.errorStr = "";
		// 服务地址
		String URL = HOST  + url;
		// 打印日志
		D.out("doPost..url>>>" + URL);
		String p = "";
		for (int i = 0; i < paramValues.length; i++) {
			p += paramNames[i] + "=" + paramValues[i] + "  ";
		}
		D.out("doPost..params>>>" + p);
		// 发送请求
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
		HttpPost post = new HttpPost(URL);
		post.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		// 建立参数
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		for (int i = 0; i < paramNames.length; i++) {
			param.add(new BasicNameValuePair(paramNames[i], paramValues[i]));
		}
		post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
		// 响应请求
		String strResult = null;
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(
				httpParameters);
		HttpResponse httpResponse = defaultHttpClient.execute(post);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			strResult = EntityUtils.toString(httpResponse.getEntity());
		}
		defaultHttpClient.getConnectionManager().shutdown();

		return strResult;
	}
	public static String DoPost(String url, String[] paramNames,
			String[] paramValues) throws Exception {
		// 先清除缓存异常信息
		Api.errorStr = "";
		// 服务地址
		String URL = url;
		// 打印日志
		D.out("doPost..url>>>" + URL);
		String p = "";
		for (int i = 0; i < paramValues.length; i++) {
			p += paramNames[i] + "=" + paramValues[i] + "  ";
		}
		D.out("doPost..params>>>" + p);
		// 发送请求
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
		HttpPost post = new HttpPost(URL);
		post.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		// 建立参数
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		for (int i = 0; i < paramNames.length; i++) {
			param.add(new BasicNameValuePair(paramNames[i], paramValues[i]));
		}
		post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
		// 响应请求
		String strResult = null;
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(
				httpParameters);
		HttpResponse httpResponse = defaultHttpClient.execute(post);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			strResult = EntityUtils.toString(httpResponse.getEntity());
		}
		defaultHttpClient.getConnectionManager().shutdown();

		return strResult;
	}
	
}
