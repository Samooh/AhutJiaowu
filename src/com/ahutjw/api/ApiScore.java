package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.ScoreResultBean;
import com.ahutjw.utils.D;

/**
 * 成绩查询
 * 
 * 
 */
public class ApiScore {

	// 成绩查询
	public static final String URL_SEARCH = "studentScoreService.asmx/getStudentScore";
	public static final String PARAM_1 = "xh";
	public static final String PARAM_2 = "sfzh";
	public static final String PARAM_3 = "xn";
	public static final String PARAM_4 = "xq";

	/**
	 * 查询成绩
	 * 
	 * @param xh
	 *            学号
	 * @param sfzh
	 *            身份证号
	 * @param xn
	 *            学年
	 * @param xq
	 *            学期
	 * @return
	 */
	public static List<ScoreResultBean> queryScoreResultList(String xh,
			String sfzh, String xn, String xq) {
		String url = URL_SEARCH;
		// 返回结果
		final List<ScoreResultBean> items = new ArrayList<ScoreResultBean>();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { PARAM_1,
					PARAM_2, PARAM_3, PARAM_4 }, new String[] { xh, sfzh, xn,
					xq });
			D.out(">>>queryScoreResultList>>>" + ret);

			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				ScoreResultBean item = null;

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("StudentScore")) {
						item = new ScoreResultBean();
					} else if (tagName.equalsIgnoreCase("xn")) {
						txt = xmlParser.nextText();
						item.setXn(txt);
					} else if (tagName.equalsIgnoreCase("xq")) {
						txt = xmlParser.nextText();
						item.setXq(txt);
					} else if (tagName.equalsIgnoreCase("lessonId")) {
						txt = xmlParser.nextText();
						item.setLessonId(txt);
					} else if (tagName.equalsIgnoreCase("lessonName")) {
						txt = xmlParser.nextText();
						item.setLessonName(txt);
					} else if (tagName.equalsIgnoreCase("lessonScore")) {
						txt = xmlParser.nextText();
						item.setLessonScore(txt);
					} else if (tagName.equalsIgnoreCase("lessonProperty")) {
						txt = xmlParser.nextText();
						item.setLessonProperty(txt);
					} else if (tagName.equalsIgnoreCase("teacherId")) {
						txt = xmlParser.nextText();
						item.setTeacherId(txt);
					} else if (tagName.equalsIgnoreCase("teacherName")) {
						txt = xmlParser.nextText();
						item.setTeacherName(txt);
					} else if (tagName.equalsIgnoreCase("zscj")) {
						txt = xmlParser.nextText();
						item.setZscj(txt);
					} else if (tagName.equalsIgnoreCase("zpcj")) {
						txt = xmlParser.nextText();
						item.setZpcj(txt);
					} else if (tagName.equalsIgnoreCase("qmcj")) {
						txt = xmlParser.nextText();
						item.setQmcj(txt);
					} else if (tagName.equalsIgnoreCase("pscj")) {
						txt = xmlParser.nextText();
						item.setPscj(txt);
					} else if (tagName.equalsIgnoreCase("sycj")) {
						txt = xmlParser.nextText();
						item.setSycj(txt);
					} else if (tagName.equalsIgnoreCase("comment")) {
						txt = xmlParser.nextText();
						item.setComment(txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase("StudentScore")
							&& item != null) {
						items.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryScoreResultList.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return items;
	}
}
