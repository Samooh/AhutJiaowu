package com.ahutjw.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.WebClassDetailBean;
import com.ahutjw.app.entity.WebClassListBean;
import com.ahutjw.utils.D;

/**
 * 网上评教
 * 
 */
public class ApiWebClass {

	/**
	 * 查询待选课程
	 * 
	 * @param xh
	 *            学号
	 * @param pwd
	 *            密码
	 * @return
	 */
	public static List<WebClassListBean> queryPickLessonList(String xh,
			String pwd) {
		String url = "studentService.asmx/getPickLessonList";
		// 返回结果
		final List<WebClassListBean> items = new ArrayList<WebClassListBean>();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { "xh", "pwd" },
					new String[] { xh, pwd });
			D.out(">>>queryPickLessonList>>>" + ret);

			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				WebClassListBean item = null;

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("PickLesson")) {
						item = new WebClassListBean();
					} else if (tagName.equalsIgnoreCase("lessonCode")) {
						txt = xmlParser.nextText();
						item.setLessonCode(txt);
					} else if (tagName.equalsIgnoreCase("lessonId")) {
						txt = xmlParser.nextText();
						item.setLessonId(txt);
					} else if (tagName.equalsIgnoreCase("lessonName")) {
						txt = xmlParser.nextText();
						item.setLessonName(txt);
					} else if (tagName.equalsIgnoreCase("lessonProperty")) {
						txt = xmlParser.nextText();
						item.setLessonProperty(txt);
					} else if (tagName.equalsIgnoreCase("lessonScore")) {
						txt = xmlParser.nextText();
						item.setLessonScore(txt);
					} else if (tagName.equalsIgnoreCase("whetherPicked")) {
						txt = xmlParser.nextText();
						item.setWhetherPicked(txt);
					} else if (tagName.equalsIgnoreCase("leftNum")) {
						txt = xmlParser.nextText();
						item.setLeftNum(txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase("PickLesson")
							&& item != null) {
						items.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryPickLessonList.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * 查看课程详细进行选课
	 * 
	 * @param xh
	 *            学号
	 * @param pwd
	 *            密码
	 * @param lesCode
	 *            课程码
	 * @return
	 */
	public static List<WebClassDetailBean> queryPickLessonDetail(String xh,
			String pwd, String lesCode) {
		String url = "studentService.asmx/getPickLessonTeacherList";
		// 返回结果
		final List<WebClassDetailBean> items = new ArrayList<WebClassDetailBean>();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { "xh", "pwd",
					"lessonCode" }, new String[] { xh, pwd, lesCode });
			D.out(">>>queryPickLessonDetail>>>" + ret);

			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				WebClassDetailBean item = null;

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("PickLessonTeacher")) {
						item = new WebClassDetailBean();
					} else if (tagName.equalsIgnoreCase("lessonId")) {
						txt = xmlParser.nextText();
						item.setLessonId(txt);
					} else if (tagName.equalsIgnoreCase("teacherName")) {
						txt = xmlParser.nextText();
						item.setTeacherName(txt);
					} else if (tagName.equalsIgnoreCase("lessonTime")) {
						txt = xmlParser.nextText();
						item.setLessonTime(txt);
					} else if (tagName.equalsIgnoreCase("lessonPlace")) {
						txt = xmlParser.nextText();
						item.setLessonPlace(txt);
					} else if (tagName.equalsIgnoreCase("schoolArea")) {
						txt = xmlParser.nextText();
						item.setSchoolArea(txt);
					} else if (tagName.equalsIgnoreCase("lessonCapacity")) {
						txt = xmlParser.nextText();
						item.setWhetherPicked(txt);
					} else if (tagName.equalsIgnoreCase("pickedByMyMajor")) {
						txt = xmlParser.nextText();
						item.setPickedByMyMajor(txt);
					} else if (tagName.equalsIgnoreCase("pickedByAll")) {
						txt = xmlParser.nextText();
						item.setPickedByAll(txt);
					} else if (tagName.equalsIgnoreCase("whetherPicked")) {
						txt = xmlParser.nextText();
						item.setWhetherPicked(txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase(
							"PickLessonTeacher")
							&& item != null) {
						items.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryPickLessonDetail.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * 提交选课
	 * 
	 * @param xh
	 *            学号
	 * @param pwd
	 *            密码
	 * @param lesCode
	 *            课程码
	 * @param lesId
	 *            课程id
	 * @return
	 */
	public static String submitPickLesson(String xh, String pwd,
			String lesCode, String lesId) {
		String url = "studentService.asmx/pickLesson";
		// 返回结果
		final HashMap<String, String> item = new HashMap<String, String>();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { "xh", "pwd",
					"lessonCode", "lessonId" }, new String[] { xh, pwd,
					lesCode, lesId });
			D.out(">>>submitPickLesson>>>" + ret);

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
					if (xmlParser.getName().equalsIgnoreCase(
							"PickLessonTeacher")
							&& item != null) {

					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>submitPickLesson.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return item.get("value");
	}

	/**
	 * 删除选课
	 * 
	 * @param xh
	 *            学号
	 * @param pwd
	 *            密码
	 * @param lesCode
	 *            课程码
	 * @param lesId
	 *            课程id
	 * @return
	 */
	public static String deletePickLesson(String xh, String pwd,
			String lesCode, String lesId) {
		String url = "studentService.asmx/unpickLesson";
		// 返回结果
		final HashMap<String, String> item = new HashMap<String, String>();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { "xh", "pwd",
					"lessonCode", "lessonId" }, new String[] { xh, pwd,
					lesCode, lesId });
			D.out(">>>deletePickLesson>>>" + ret);

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
					if (xmlParser.getName().equalsIgnoreCase(
							"PickLessonTeacher")
							&& item != null) {
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>deletePickLesson.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return item.get("value");
	}
}
