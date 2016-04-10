package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.ClassNoticeBean;

public class ApiClassNotice {
	private static String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/getNotice";

	public static List<ClassNoticeBean> queryClassNoticeList(String courseName,
			String teaNum) {
		final List<ClassNoticeBean> list = new ArrayList<ClassNoticeBean>();
		try {
			String result = ApiBaseHttp.DoPost(url, new String[] { "teaNum",
					"courseName" }, new String[] { teaNum, courseName });
			System.out.println(result);
			ApiBaseXml.parseXml(result, new OnXmlCallback() {
				ClassNoticeBean item = null;
				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("ClassNotice")) {
						item = new ClassNoticeBean();
					} else if (tagName.equalsIgnoreCase("teaNum")) {
						txt = xmlParser.nextText();
						item.setTeaNum(txt);
					} else if (tagName.equalsIgnoreCase("courseName")) {
						txt = xmlParser.nextText();
						item.setCourseName(txt);
					} else if (tagName.equalsIgnoreCase("noticeDetail")) {
						txt = xmlParser.nextText();
						item.setNoticeDetail(txt);
					}else if (tagName.equalsIgnoreCase("Date")) {
						txt = xmlParser.nextText();
						String txts=txt.split(" ")[0];
						System.out.println(txts);
						item.setNoticeDate(txts);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase(
							"ClassNotice")
							&& item != null) {
						list.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

}
