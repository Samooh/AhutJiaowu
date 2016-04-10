package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.StudentBean;

public class ApiStudentList {
	public static List<StudentBean> queryStudentlist(String user,
			String password, String classname) {
		String url = "http://211.70.149.139:8990/teacherService.asmx/StudentList";
		final List<StudentBean> list = new ArrayList<StudentBean>();
		String result;
		try {
			result = ApiBaseHttp.DoPost(url, new String[] { "zgh", "pwd",
					"kcmc" }, new String[] { user, password, classname });
			try {
				ApiBaseXml.parseXml(result, new OnXmlCallback() {
					StudentBean item = null;

					@Override
					public void onTagStart(String tagName,
							XmlPullParser xmlParser) throws Exception {
						// 一条记录
						String txt = "";
						if (tagName.equalsIgnoreCase("SL")) {
							item = new StudentBean();
						} else if (tagName.equalsIgnoreCase("Nu")) {
							txt = xmlParser.nextText();
							item.setNumber(txt);
						} else if (tagName.equalsIgnoreCase("Na")) {
							txt = xmlParser.nextText();
							item.setName(txt);
							System.out.println(txt);
						} else if (tagName.equalsIgnoreCase("Ma")) {
							txt = xmlParser.nextText();
							item.setMajorName(txt);
						} else if (tagName.equalsIgnoreCase("Cl")) {
							txt = xmlParser.nextText();
							item.setClassName(txt);
						}
					}

					@Override
					public void onTagEnd(String tagName, XmlPullParser xmlParser)
							throws Exception {
						if (xmlParser.getName().equalsIgnoreCase("SL")
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
