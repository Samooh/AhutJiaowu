package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.CourseResultStuBean;
import com.ahutjw.app.entity.CourseResultTeaBean;
import com.ahutjw.utils.D;
/**
 * 课表
 * 
 */
public class ApiCourse {

	/**
	 * 查询课表--学生
	 * 
	 * @param xh
	 *            学号
	 * @param pwd
	 *            密码
	 * @param xn
	 *            学年
	 * @param xq
	 *            学期
	 * @return
	 */
	public static List<CourseResultStuBean> queryCourseResultList(String xh,
			String pwd, String xn, String xq) {
		String url = "studentService.asmx/getLessonInfo";
		// 返回结果
		final List<CourseResultStuBean> items = new ArrayList<CourseResultStuBean>();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { "xh", "pwd",
					"xn", "xq" }, new String[] { xh, pwd, xn, xq });
			D.out(">>>queryCourseResultList>>>" + ret);

			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				CourseResultStuBean item = null;

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("LessonInfo")) {
						item = new CourseResultStuBean();
					} else if (tagName.equalsIgnoreCase("lessonScore")) {
						txt = xmlParser.nextText();
						item.setLessonScore(txt);
					} else if (tagName.equalsIgnoreCase("lessonTime")) {
						txt = xmlParser.nextText();
						item.setLessonTime(txt);
					} else if (tagName.equalsIgnoreCase("lessonId")) {
						txt = xmlParser.nextText();
						item.setLessonId(txt);
					} else if (tagName.equalsIgnoreCase("lessonName")) {
						txt = xmlParser.nextText();
						item.setLessonName(txt);
					} else if (tagName.equalsIgnoreCase("lessonPlace")) {
						txt = xmlParser.nextText();
						item.setLessonPlace(txt);
					} else if (tagName.equalsIgnoreCase("teacherName")) {
						txt = xmlParser.nextText();
						item.setTeacherName(txt);
					}
				}
				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase("LessonInfo")
							&& item != null) {
						items.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryCourseResultList.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * 查询课表--教师
	 * 
	 * @param username
	 *            教师号
	 * @param pwd
	 *            密码
	 * 
	 * @return
	 */
	public static List<CourseResultTeaBean> queryCourseResultTea(
			String username, String pwd) {
		String url = "teacherService.asmx/getTeacherLessonList";
		// 返回结果
		final List<CourseResultTeaBean> items = new ArrayList<CourseResultTeaBean>();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { "zgh", "pwd" },
					new String[] { username, pwd });
			D.out(">>>queryCourseResultTea>>>" + ret);

			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				CourseResultTeaBean item = null;
				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("TeacherLesson")) {
						item = new CourseResultTeaBean();
					} else if (tagName.equalsIgnoreCase("weekDay")) {
						txt = xmlParser.nextText();
						item.setWeekDay(txt);
					} else if (tagName.equalsIgnoreCase("time")) {
						txt = xmlParser.nextText();
						item.setTime(txt);
					} else if (tagName.equalsIgnoreCase("name")) {
						txt = xmlParser.nextText();
						item.setName(txt);
					} else if (tagName.equalsIgnoreCase("timePerWeek")) {
						txt = xmlParser.nextText();
						item.setTimePerWeek(txt);
					} else if (tagName.equalsIgnoreCase("place")) {
						txt = xmlParser.nextText();
						item.setPlace(txt);
					} else if (tagName.equalsIgnoreCase("studentMajor")) {
						txt = xmlParser.nextText();
						item.setStudentMajor(txt);
					}
				}
				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase("TeacherLesson")
							&& item != null) {
						items.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryCourseResultTea.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return items;
	}
}
