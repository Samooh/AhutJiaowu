package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.ExamArrangementBean;
import com.ahutjw.app.entity.ExamStuBean;
import com.ahutjw.utils.D;

/**
 * 考试信息
 * 
 * @author 郭建亮
 * 
 */
public class ApiExam {

	// 列表内容--教师
	public static final String URL_LIST_TEA = "teacherService.asmx/getExamArrangement";
	public static final String PARAM_LIST_TEA_1 = "zgh";
	public static final String PARAM_LIST_TEA_2 = "pwd";
	// 学生
	public static final String URL_LIST_STU = "studentService.asmx/getStudentExamInfo";
	public static final String PARAM_LIST_STU_1 = "xh";
	public static final String PARAM_LIST_STU_2 = "pwd";

	/**
	 * 考试安排
	 * 
	 * @param type
	 * @param username
	 * @param pwd
	 * @return
	 */
	public static Object queryExam(String type, String username, String pwd) {
		if (type.equals("教师")) {
			return queryExamTea(username, pwd);
		} else if (type.equals("本科生")) {
			return queryExamStu(username, pwd);
		}
		return null;
	}

	/**
	 * 考试安排---老师
	 * 
	 * @param username
	 * @param pwd
	 * @return
	 */
	public static List<ExamArrangementBean> queryExamTea(String username,
			String pwd) {
		// 地址拼接
		String url = URL_LIST_TEA;
		// 返回结果
		final List<ExamArrangementBean> items = new ArrayList<ExamArrangementBean>();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] {
					PARAM_LIST_TEA_1, PARAM_LIST_TEA_2 }, new String[] {
					username, pwd });
			D.out(">>>queryExamTea>>>" + ret);
			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				ExamArrangementBean item = null;

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("ExamArrangementInfo")) {
						item = new ExamArrangementBean();
					} else if (tagName.equalsIgnoreCase("lessonName")) {
						txt = xmlParser.nextText();
						item.setLessonName(txt);
					} else if (tagName.equalsIgnoreCase("examDate")) {
						txt = xmlParser.nextText();
						item.setExamDate(txt);
					} else if (tagName.equalsIgnoreCase("lessonTeacher")) {
						txt = xmlParser.nextText();
						item.setLessonTeacher(txt);
					} else if (tagName.equalsIgnoreCase("classroom")) {
						txt = xmlParser.nextText();
						item.setClassroom(txt);
					} else if (tagName.equalsIgnoreCase("studentNum")) {
						txt = xmlParser.nextText();
						item.setStudentNum(txt);
					} else if (tagName.equalsIgnoreCase("overseeTeacher1")) {
						txt = xmlParser.nextText();
						item.setOverseeTeacher1(txt);
					} else if (tagName.equalsIgnoreCase("overseeTeacher2")) {
						txt = xmlParser.nextText();
						item.setOverseeTeacher2(txt);
					} else if (tagName.equalsIgnoreCase("overseeTeacher3")) {
						txt = xmlParser.nextText();
						item.setOverseeTeacher3(txt);
					} else if (tagName.equalsIgnoreCase("overseeTeacher4")) {
						txt = xmlParser.nextText();
						item.setOverseeTeacher4(txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase(
							"ExamArrangementInfo")
							&& item != null) {
						items.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryExamTea.error>>>");
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * 考试安排-学生
	 * 
	 * @param username
	 * @param pwd
	 * @return
	 */
	public static List<ExamStuBean> queryExamStu(String username, String pwd) {
		// 地址拼接
		String url = URL_LIST_STU;
		// 返回结果
		final List<ExamStuBean> items = new ArrayList<ExamStuBean>();
		try {

			String ret = ApiBaseHttp.doPost(url, new String[] {
					PARAM_LIST_STU_1, PARAM_LIST_STU_2 }, new String[] {
					username, pwd });
			D.out(">>>queryExamStu>>>" + ret);
			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				ExamStuBean item = null;

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("ExamInfo")) {
						item = new ExamStuBean();
					} else if (tagName.equalsIgnoreCase("lessonName")) {
						txt = xmlParser.nextText();
						item.setLessonName(txt);
					} else if (tagName.equalsIgnoreCase("examDate")) {
						txt = xmlParser.nextText();
						item.setExamDate(txt);
					} else if (tagName.equalsIgnoreCase("examPlace")) {
						txt = xmlParser.nextText();
						item.setExamPlace(txt);
					} else if (tagName.equalsIgnoreCase("seatNo")) {
						txt = xmlParser.nextText();
						item.setSeatNo(txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase("ExamInfo")
							&& item != null) {
						items.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryExamStu.error>>>");
			e.printStackTrace();
		}
		return items;
	}

}
