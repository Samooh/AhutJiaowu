package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.ClassBean;

public class ApiClassroom {
	public static List<ClassBean> queryClassroomlist(String place,String style,String time1,String time2) {
		String url = "http://211.70.149.139:8990/studentService.asmx/ClassesQuery";
		final List<ClassBean> list = new ArrayList<ClassBean>();
		String result;
		try {
			result = ApiBaseHttp.DoPost(url, new String[] { "xiaoq","jslb","kssj","sjd"},
					new String[] { place,style,time1,time2 });
			System.out.println(result);
			try {
				ApiBaseXml.parseXml(result, new OnXmlCallback() {
					ClassBean item = null;

					@Override
					public void onTagStart(String tagName,
							XmlPullParser xmlParser) throws Exception {
						// 一条记录
						String txt = "";
						if (tagName.equalsIgnoreCase("CQ")) {
							item = new ClassBean();
						} else if (tagName.equalsIgnoreCase("c")) {
							txt = xmlParser.nextText();
							item.setName(txt);;
						} else if (tagName.equalsIgnoreCase("b")) {
							txt = xmlParser.nextText();
							item.setStyle(txt);
						} else if (tagName.equalsIgnoreCase("s")) {
							txt = xmlParser.nextText();
							item.setCount(txt);
						}
					}

					@Override
					public void onTagEnd(String tagName, XmlPullParser xmlParser)
							throws Exception {
						if (xmlParser.getName().equalsIgnoreCase("CQ")
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

}
