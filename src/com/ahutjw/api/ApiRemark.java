package com.ahutjw.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.RemarkLessonInfoBean;
import com.ahutjw.utils.D;

/**
 * 教学评价
 * 
 * 
 */
public class ApiRemark {

	// 列表内容
	public static final String URL_LIST_LESSION = "studentService.asmx/getLessonInfoNow";
	public static final String PARAM_LIST_1 = "xh";
	public static final String PARAM_LIST_2 = "pwd";

	/**
	 * 教学评价===课程列表
	 * 
	 * @param xh
	 * @param pwd
	 * @return
	 */
	public static List<RemarkLessonInfoBean> queryRemarkLessionInfo(String xh,
			String pwd) {
		Calendar calendar=Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH)+1;
		String xq="2";
		if(month>=9||month<=2){
			xq="1";
		}
		int year=calendar.get(Calendar.YEAR);
		String y=String.valueOf(year)+"-"+String.valueOf(year+1);
		System.out.println("Current year="+y);
		String url = "http://211.70.149.139:8011/AHUTJXPJ.asmx/LoadXKJG?psw="+pwd+"&xh="+xh+"&xn="+y+"&xq="+xq;
		// 返回结果
		final List<RemarkLessonInfoBean> items = new ArrayList<RemarkLessonInfoBean>();
		try {
			String ret = ApiBaseHttp.get(url);
			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				RemarkLessonInfoBean item = null;

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					if (tagName.equalsIgnoreCase("XSXKB")) {
						item = new RemarkLessonInfoBean();
					} else if (tagName.equalsIgnoreCase("ID")) {
						item.ID=xmlParser.nextText();
					} else if (tagName.equalsIgnoreCase("XN")) {
						item.XN=xmlParser.nextText();
					} else if (tagName.equalsIgnoreCase("XQ")) {
						item.XQ=xmlParser.nextText();
					} else if (tagName.equalsIgnoreCase("XKKH")) {
						item.XKKH=xmlParser.nextText();
					} else if (tagName.equalsIgnoreCase("XH")) {
						item.XH=xmlParser.nextText();
					} else if (tagName.equalsIgnoreCase("KCMC")) {
						item.KCMC=xmlParser.nextText();
					} else if (tagName.equalsIgnoreCase("JSXM")) {
						item.JSXM=xmlParser.nextText();
					}else if (tagName.equalsIgnoreCase("PJDJ")) {
						item.PJDJ=xmlParser.nextText();
					} else if (tagName.equalsIgnoreCase("PJDF")) {
						item.PJDF=xmlParser.nextText();
					}

				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase("XSXKB")
							&& item != null) {
						items.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryRemarkLessionInfo.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * 教学评价===保存单门课程
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String saveRemarkLession(String xh, String psw, String xkkh,
			String dj,String df,String ip, String ly) throws UnsupportedEncodingException {
		Calendar calendar=Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH)+1;
		String xq="2";
		if(month>=9||month<=2){
			xq="1";
		}
		int year=calendar.get(Calendar.YEAR);
		String xn=String.valueOf(year)+"-"+String.valueOf(year+1);
		System.out.println("Current year="+xn);
		String url = "http://211.70.149.139:8011/AHUTJXPJ.asmx/SubmitPJJG?df="+df+"&dj="+URLEncoder.encode(dj, "UTF-8")+"&ip="+ip+"&ly="+ly+"&psw="+psw+"&xh="+xh+"&xkkh="+xkkh+"&xn="+xn+"&xq="+xq;
		System.out.println("Request url="+url);
		// 返回结果
		final HashMap<String, String> item = new HashMap<String, String>();
		try {
			String ret = ApiBaseHttp.get(url);
			D.out(">>>saveRemarkLession>>>" + ret);

			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("string")) {
						txt = xmlParser.nextText();
						item.put("value", txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase("string")
							&& item != null) {

					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>saveRemarkLession.error>>>" + e.getMessage());
			e.printStackTrace();
			return "咦？您的网络好像出了问题...";
		}
		System.out.println(item.get("value"));
		return item.get("value");
	}

	/**
	 * 教学评价===提交所有课程
	 * 
	 * @param xh
	 *            用户
	 * @param pwd
	 *            密码
	 * 
	 * @return
	 */
	public static String submiRemarkLession(String xh, String pwd) {
		// 地址拼接
		String url = "studentService.asmx/evaluateLesson";
		// 返回结果
		final HashMap<String, String> item = new HashMap<String, String>();
		try {

			String ret = ApiBaseHttp.doPost(url, new String[] { "xh", "pwd" },
					new String[] { xh, pwd });
			D.out(">>>submiRemarkLession>>>" + ret);

			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("string")) {
						txt = xmlParser.nextText();
						item.put("value", txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase("LessonInfo")
							&& item != null) {

					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>submiRemarkLession.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return item.get("value");
	}
}
