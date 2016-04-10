package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.NoteBean;

public class ApiNote {

	public static List<NoteBean> queryTeaNoteAlllist(String teaNum,
			String password, String page) {
		String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/getTeaNoteAllList";
		String result;
		try {
			result = ApiBaseHttp.DoPost(url, new String[] { "teaNum",
					"password", "page" },
					new String[] { teaNum, password, page });
			System.out.println(result);
			return parsexml(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	public static List<NoteBean> queryTeaNotelist(String teaNum,
			String password, String courseName,String page) {
		String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/getTeaNoteList";
		String result;
		try {
			result = ApiBaseHttp.DoPost(url, new String[] { "teaNum",
					"password","courseName", "page" },
					new String[] { teaNum, password, courseName,page });
			System.out.println(result);
			return parsexml(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	

	public static List<NoteBean> queryStuNoteAlllist(String stuNum,
			String password, String page) {
		String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/getStuNoteAllList";
		String result;
		try {
			result = ApiBaseHttp.DoPost(url, new String[] { "stuNum",
					"password", "page" },
					new String[] { stuNum, password, page });
			System.out.println(result);
			return parsexml(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static List<NoteBean> queryStuNotelist(String stuNum,
			String password, String courseName, String teaNum, String page) {
		String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/getStuNoteList";
		String result;
		try {
			result = ApiBaseHttp.DoPost(url, new String[] { "stuNum",
					"password", "courseName", "teaNum", "page" }, new String[] {
					stuNum, password, courseName, teaNum, page });
			System.out.println(result);
			return parsexml(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static List<NoteBean> parsexml(String Xml) {
		final List<NoteBean> list = new ArrayList<NoteBean>();
		try {
			ApiBaseXml.parseXml(Xml, new OnXmlCallback() {
				NoteBean item = null;

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("stuNote")) {
						item = new NoteBean();
					} else if (tagName.equalsIgnoreCase("ID")) {
						txt = xmlParser.nextText();
						item.setID(txt);
					} else if (tagName.equalsIgnoreCase("courseName")) {
						txt = xmlParser.nextText();
						item.setCourseName(txt);
					} else if (tagName.equalsIgnoreCase("stuNum")) {
						txt = xmlParser.nextText();
						item.setStuNum(txt);
					} else if (tagName.equalsIgnoreCase("teaNum")) {
						txt = xmlParser.nextText();
						item.setTeaNum(txt);
					} else if (tagName.equalsIgnoreCase("Note")) {
						txt = xmlParser.nextText();
						item.setNote(txt);
					} else if (tagName.equalsIgnoreCase("noteDate")) {
						txt = xmlParser.nextText();
						item.setNoteDate(txt);
					} else if (tagName.equalsIgnoreCase("noteCheck")) {
						txt = xmlParser.nextText();
						item.setNoteCheck(txt);
					} else if (tagName.equalsIgnoreCase("Reply")) {
						txt = xmlParser.nextText();
						item.setReply(txt);
					} else if (tagName.equalsIgnoreCase("replyDate")) {
						txt = xmlParser.nextText();
						item.setReplyDate(txt);
					} else if (tagName.equalsIgnoreCase("replyCheck")) {
						txt = xmlParser.nextText();
						item.setReplyCheck(txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase("stuNote")
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
