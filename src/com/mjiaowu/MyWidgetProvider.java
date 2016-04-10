package com.mjiaowu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.ahutjw.api.ApiCourse;
import com.ahutjw.app.entity.CourseResultStuBean;
import com.ahutjw.app.entity.CourseResultTeaBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.utils.S;
import com.ahutjw.utils.SharePre;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {
	// 定义我们要发送的事件
	private static List<CourseResultTeaBean> tCourses;
	private static List<CourseResultTeaBean> tdatas;
	private static List<CourseResultStuBean> datas;
	private static List<MyLesson> Courses;
	private static List<MyLesson> Lessons;
	private static DbManager dbM;
	private static SharePre sharePre;
	private static CourseResultStuBean bean;
	private final String widgetupdate = "com.wd.appWidgetUpdate";

	private final String widgetupdaterefresh = "com.wd.appWidgetUpdaterefresh";
	private final String widgetupdateweekprev = "com.wd.appWidgetUpdateWeekprev";
	private final String widgetupdateweeknext = "com.wd.appWidgetUpdateWeeknext";
	private final String widgetupdatepageprev = "com.wd.appWidgetUpdatePageprev";
	private final String widgetupdatepagenext = "com.wd.appWidgetUpdatePagenext";
	private static int nowweek = 0;

	// private static int page;

	@SuppressLint("SimpleDateFormat")
	public void updateAllInfo(Context context) {
		datas = new ArrayList<CourseResultStuBean>();
		Courses = new ArrayList<MyLesson>();
		tdatas = new ArrayList<CourseResultTeaBean>();
		tCourses = new ArrayList<CourseResultTeaBean>();
		Lessons = new ArrayList<MyLesson>();
		sharePre = SharePre.getInstance(context);
		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		if (sharePre.getBoolean(S.LOGIN_IS, false)) {

			rv.setTextViewText(R.id.mylogin,
					"账号:" + sharePre.getString(S.LOGIN_USERNAME, "none"));
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentName = new ComponentName(context,
					MyWidgetProvider.class);
			appWidgetManager.updateAppWidget(componentName, rv);
			dbM = new DbManager(context);
			if (sharePre.getString(S.LOGIN_TYPE, "").equals("本科生")) {

				String xn, xq;
				SimpleDateFormat sdfy = new SimpleDateFormat("yyyy");
				int year = Integer.parseInt(sdfy.format(new java.util.Date()));
				SimpleDateFormat sdfm = new SimpleDateFormat("MM");
				int month = Integer.parseInt(sdfm.format(new java.util.Date()));
				if (month >= 7) {
					xn = year + "-" + (year + 1);
					xq = "1";
				} else if (month == 1) {
					xn = (year - 1) + "-" + year;
					xq = "1";
				} else {
					xn = (year - 1) + "-" + year;
					xq = "2";
				}
				List<CourseResultStuBean> tempBeans = (List<CourseResultStuBean>) ApiCourse
						.queryCourseResultList(
								sharePre.getString(S.LOGIN_USERNAME, "none"),
								sharePre.getString(S.LOGIN_PWD, "none"), xn, xq);
				if (tempBeans.size() > 0) {
					dbM.deleteAll(DbManager.course.TABLE_NAME);
					for (int i = 0; i < tempBeans.size(); i++) {
						CourseResultStuBean bean = tempBeans.get(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(DbManager.course.COLUMN_ID, i + "");
						map.put(DbManager.course.COLUMN_XN, xn);
						map.put(DbManager.course.COLUMN_XQ, xq);
						map.put(DbManager.course.COLUMN_LESSONID,
								bean.getLessonId());
						map.put(DbManager.course.COLUMN_LESSONNAME,
								bean.getLessonName());
						map.put(DbManager.course.COLUMN_LESSONPROPERTY,
								bean.getLessonProperty());
						map.put(DbManager.course.COLUMN_TEACHERNAME,
								bean.getTeacherName());
						map.put(DbManager.course.COLUMN_LESSONSCORE,
								bean.getLessonScore());
						map.put(DbManager.course.COLUMN_LESSONTIME,
								bean.getLessonTime());
						map.put(DbManager.course.COLUMN_LESSONPLACE,
								bean.getLessonPlace());
						// 添加数据库
						dbM.insert(DbManager.course.TABLE_NAME,
								DbManager.course.columns, map);

					}

				} else {// 从数据库中加载
					ArrayList<HashMap<String, String>> datasLst = dbM.query(
							DbManager.course.TABLE_NAME,
							DbManager.course.columns);
					for (int i = 0; i < datasLst.size(); i++) {
						HashMap<String, String> map = datasLst.get(i);
						CourseResultStuBean temp = new CourseResultStuBean();
						temp.setLessonId(map
								.get(DbManager.course.COLUMN_LESSONID));
						temp.setLessonName(map
								.get(DbManager.course.COLUMN_LESSONNAME));
						temp.setLessonPlace(map
								.get(DbManager.course.COLUMN_LESSONPLACE));
						temp.setLessonProperty(map
								.get(DbManager.course.COLUMN_LESSONPROPERTY));
						temp.setLessonScore(map
								.get(DbManager.course.COLUMN_LESSONSCORE));
						temp.setLessonTime(map
								.get(DbManager.course.COLUMN_LESSONTIME));
						temp.setTeacherName(map
								.get(DbManager.course.COLUMN_TEACHERNAME));
						tempBeans.add(temp);
					}
				}
				String time = "";
				String time1 = "";
				for (int i = 1; i <= 7; i++) {
					switch (i) {
					case 1:
						time = "周一";
						break;
					case 2:
						time = "周二";
						break;
					case 3:
						time = "周三";
						break;
					case 4:
						time = "周四";
						break;
					case 5:
						time = "周五";
						break;
					case 6:
						time = "周六";
						break;
					case 7:
						time = "周日";
						break;
					}
					for (int j = 1; j <= 5; j++) {
						switch (j) {
						case 1:
							time1 = "第1";
							break;
						case 2:
							time1 = "第3";
							break;
						case 3:
							time1 = "第5";
							break;
						case 4:
							time1 = "第7";
							break;
						case 5:
							time1 = "第9";
							break;
						}
						bean = findMapbean(time, time1, tempBeans);
						if (bean != null) {
							MyLesson lesson = new MyLesson();
							lesson.setName(bean.getLessonName());
							lesson.setTeacher(bean.getTeacherName());
							if (j == 5) {
								lesson.setTime("9-11");
							} else {
								lesson.setTime((j * 2 - 1) + "-" + (j * 2));
							}
							if (findSameLesson(bean.getLessonName())) {
								lesson.setPlace(bean.getLessonPlace()
										.split(";")[1]);
							} else {
								lesson.setPlace(bean.getLessonPlace()
										.split(";")[0]);
							}
							lesson.setWeek(time);

							datas.add(bean);
							Lessons.add(lesson);
						}
					}

				}

			} else if (sharePre.getString(S.LOGIN_TYPE, "none").equals("教师")) {
				List<CourseResultTeaBean> tempBeans = (List<CourseResultTeaBean>) ApiCourse
						.queryCourseResultTea(
								sharePre.getString(S.LOGIN_USERNAME, ""),
								sharePre.getString(S.LOGIN_PWD, ""));
				if (tempBeans.size() > 0) {
					dbM.deleteAll(DbManager.tcourse.TABLE_NAME);
					for (int i = 0; i < tempBeans.size(); i++) {
						CourseResultTeaBean bean = tempBeans.get(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(DbManager.tcourse.COLUMN_ID, i + "");
						map.put(DbManager.tcourse.COLUMN_WEEKDAY,
								bean.getWeekDay());
						map.put(DbManager.tcourse.COLUMN_TIME, bean.getTime());
						map.put(DbManager.tcourse.COLUMN_NAME, bean.getName());
						map.put(DbManager.tcourse.COLUMN_TIMEPERWEEK,
								bean.getTimePerWeek());
						map.put(DbManager.tcourse.COLUMN_PLACE, bean.getPlace());
						map.put(DbManager.tcourse.COLUMN_STUDENTMAJOR,
								bean.getStudentMajor());

						// 添加数据库
						dbM.insert(DbManager.tcourse.TABLE_NAME,
								DbManager.tcourse.columns, map);
					}
				} else {
					ArrayList<HashMap<String, String>> datasLst = dbM.query(
							DbManager.tcourse.TABLE_NAME,
							DbManager.tcourse.columns);
					for (int i = 0; i < datasLst.size(); i++) {
						HashMap<String, String> map = datasLst.get(i);
						CourseResultTeaBean temp = new CourseResultTeaBean();
						temp.setTime(map.get(DbManager.tcourse.COLUMN_TIME));
						temp.setName(map.get(DbManager.tcourse.COLUMN_NAME));
						temp.setPlace(map.get(DbManager.tcourse.COLUMN_PLACE));
						temp.setStudentMajor(map
								.get(DbManager.tcourse.COLUMN_STUDENTMAJOR));
						temp.setTimePerWeek(map
								.get(DbManager.tcourse.COLUMN_TIMEPERWEEK));
						temp.setWeekDay(map
								.get(DbManager.tcourse.COLUMN_WEEKDAY));
						tempBeans.add(temp);
					}
				}
				String time1 = "";
				CourseResultTeaBean tbean = null;
				for (int i = 1; i <= 7; i++) {

					for (int j = 1; j <= 5; j++) {
						switch (j) {
						case 1:
							time1 = "1";
							break;
						case 2:
							time1 = "3";
							break;
						case 3:
							time1 = "5";
							break;
						case 4:
							time1 = "7";
							break;
						case 5:
							time1 = "9";
							break;
						}
						tbean = tfindMap(i + "", time1, tempBeans);
						if (tbean != null) {
							tdatas.add(tbean);
						}
					}
				}

			}

			dbM.close();
			updateweek(context);
		} else {

			rv.setTextViewText(R.id.mylogin, "点击头像登录");
			rv.setTextViewText(R.id.firstplace, " ");

			rv.setTextViewText(R.id.firsttime, " ");
			rv.setTextViewText(R.id.firstcourse, " ");
			// rv.setViewVisibility(R.id.firstline, View.GONE);
			rv.setViewVisibility(R.id.firstimage, View.GONE);
			rv.setTextViewText(R.id.secondcourse, " ");
			rv.setViewVisibility(R.id.secondimage, View.GONE);
			// rv.setViewVisibility(R.id.secondline, View.GONE);
			rv.setTextViewText(R.id.secondplace, " ");
			rv.setTextViewText(R.id.secondtime, " ");
			// rv.setViewVisibility(R.id.thirdline, View.GONE);
			rv.setViewVisibility(R.id.downcourse, View.GONE);
			rv.setViewVisibility(R.id.upcourse, View.GONE);
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentName = new ComponentName(context,
					MyWidgetProvider.class);
			appWidgetManager.updateAppWidget(componentName, rv);

		}

	}

	public CourseResultTeaBean tfindMap(String time1, String time2,
			List<CourseResultTeaBean> tempBeans) {
		if (tempBeans != null) {
			for (int i = 0; i < tempBeans.size(); i++) {
				CourseResultTeaBean bean = tempBeans.get(i);
				if (bean.getTime().equals(time2)
						&& bean.getWeekDay().equals(time1)) {
					return bean;
				}

			}
		}
		return null;
	}

	public boolean findSameLesson(String LessonName) {
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).getLessonName().equals(LessonName)) {
				return true;
			}
		}
		return false;
	}

	public void updateweek(Context context) {

		sharePre = SharePre.getInstance(context);
		int intweek = sharePre.getInt("nowweek", 1);
		if (sharePre.getString(S.LOGIN_TYPE, "none").equals("本科生")) {
			String week = "";
			String weekTime = "";
			switch (intweek) {
			case 1:
				week = "星期一";
				weekTime = "周一";
				break;
			case 2:
				week = "星期二";
				weekTime = "周二";
				break;
			case 3:
				week = "星期三";
				weekTime = "周三";
				break;
			case 4:
				week = "星期四";
				weekTime = "周四";
				break;
			case 5:
				week = "星期五";
				weekTime = "周五";
				break;
			case 6:
				week = "星期六";
				weekTime = "周六";
				break;
			case 0:
				week = "星期日";
				weekTime = "周日";
				break;
			}
			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			if (sharePre.getBoolean(S.LOGIN_IS, false)) {

				rv.setTextViewText(R.id.mylogin,
						"账号:" + sharePre.getString(S.LOGIN_USERNAME, "none"));
			}
			/*
			 * rv.setTextViewText(R.id.mylogin, "账号:" +
			 * sharePre.getString(S.LOGIN_USERNAME, "none"));
			 */
			rv.setTextViewText(R.id.week, week);
			String time1 = "";
			for (int j = 1; j <= 5; j++) {
				switch (j) {
				case 1:
					time1 = "1-2";
					break;
				case 2:
					time1 = "3-4";
					break;
				case 3:
					time1 = "5-6";
					break;
				case 4:
					time1 = "7-8";
					break;
				case 5:
					time1 = "9-11";
					break;
				}
				int position = findMapCourse(weekTime, time1, Lessons);
				if (position != -1) {
					Courses.add(Lessons.get(position));
				}

			}
			if (Courses.size() > 2) {
				rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
				rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
				rv.setViewVisibility(R.id.downcourse, View.VISIBLE);
				rv.setViewVisibility(R.id.upcourse, View.GONE);
				rv.setViewVisibility(R.id.firstline, View.VISIBLE);
				rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

				rv.setTextViewText(R.id.firsttime, Courses.get(0).getTime());
				rv.setTextViewText(R.id.firstcourse, Courses.get(0).getName()
						+ " (" + Courses.get(0).getTeacher() + ")");
				rv.setTextViewText(R.id.firstplace, Courses.get(0).getPlace());

				rv.setTextViewText(R.id.secondtime, Courses.get(1).getTime());
				rv.setTextViewText(R.id.secondcourse, Courses.get(1).getName()
						+ " (" + Courses.get(1).getTeacher() + ")");
				rv.setTextViewText(R.id.secondplace, Courses.get(1).getPlace());
			} else if (Courses.size() == 2) {

				rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
				rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
				rv.setViewVisibility(R.id.downcourse, View.GONE);
				rv.setViewVisibility(R.id.upcourse, View.GONE);
				rv.setViewVisibility(R.id.firstline, View.VISIBLE);
				rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

				rv.setTextViewText(R.id.firsttime, Courses.get(0).getTime());
				rv.setTextViewText(R.id.firstcourse, Courses.get(0).getName()
						+ " (" + Courses.get(0).getTeacher() + ")");
				rv.setTextViewText(R.id.firstplace, Courses.get(0).getPlace());

				rv.setTextViewText(R.id.secondtime, Courses.get(1).getTime());
				rv.setTextViewText(R.id.secondcourse, Courses.get(1).getName()
						+ " (" + Courses.get(1).getTeacher() + ")");
				rv.setTextViewText(R.id.secondplace, Courses.get(1).getPlace());
			} else if (Courses.size() == 1) {
				rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
				rv.setViewVisibility(R.id.secondimage, View.GONE);
				rv.setViewVisibility(R.id.downcourse, View.GONE);
				rv.setViewVisibility(R.id.upcourse, View.GONE);
				rv.setViewVisibility(R.id.firstline, View.VISIBLE);
				rv.setViewVisibility(R.id.thirdline, View.GONE);

				rv.setTextViewText(R.id.firsttime, Courses.get(0).getTime());
				rv.setTextViewText(R.id.firstcourse, Courses.get(0).getName()
						+ " (" + Courses.get(0).getTeacher() + ")");
				rv.setTextViewText(R.id.firstplace, Courses.get(0).getPlace());
			} else {
				rv.setTextViewText(R.id.firstplace, " ");

				rv.setTextViewText(R.id.firsttime, " ");
				rv.setTextViewText(R.id.firstcourse, " ");
				rv.setViewVisibility(R.id.firstline, View.GONE);
				rv.setViewVisibility(R.id.firstimage, View.GONE);
				rv.setTextViewText(R.id.secondcourse, " ");
				rv.setViewVisibility(R.id.secondimage, View.GONE);
				// rv.setViewVisibility(R.id.secondline, View.GONE);
				rv.setTextViewText(R.id.secondplace, " ");
				rv.setTextViewText(R.id.secondtime, " ");
				rv.setViewVisibility(R.id.thirdline, View.GONE);
				rv.setViewVisibility(R.id.downcourse, View.GONE);
				rv.setViewVisibility(R.id.upcourse, View.GONE);
			}
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentName = new ComponentName(context,
					MyWidgetProvider.class);
			appWidgetManager.updateAppWidget(componentName, rv);
		} else if (sharePre.getString(S.LOGIN_TYPE, "").equals("教师")) {
			String week = "";
			switch (intweek) {
			case 1:
				week = "星期一";
				break;
			case 2:
				week = "星期二";
				break;
			case 3:
				week = "星期三";
				break;
			case 4:
				week = "星期四";
				break;
			case 5:
				week = "星期五";
				break;
			case 6:
				week = "星期六";
				break;
			case 0:
				week = "星期日";
				break;
			}
			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			/*
			 * rv.setTextViewText(R.id.mylogin, "账号:" +
			 * sharePre.getString(S.LOGIN_USERNAME, "none"));
			 */
			rv.setTextViewText(R.id.week, week);
			String time1 = "";
			CourseResultTeaBean tbean = null;
			for (int j = 1; j <= 5; j++) {
				switch (j) {
				case 1:
					time1 = "1";
					break;
				case 2:
					time1 = "3";
					break;
				case 3:
					time1 = "5";
					break;
				case 4:
					time1 = "7";
					break;
				case 5:
					time1 = "9";
					break;
				}
				tbean = tfindMap(intweek + "", time1, tdatas);
				if (tbean != null) {
					tCourses.add(tbean);
				}
			}
			int time;
			if (tCourses.size() > 2) {
				rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
				rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
				rv.setViewVisibility(R.id.downcourse, View.VISIBLE);
				rv.setViewVisibility(R.id.upcourse, View.GONE);
				rv.setViewVisibility(R.id.firstline, View.VISIBLE);
				rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

				time = Integer.parseInt(tCourses.get(0).getTime());
				if (time == 9) {
					rv.setTextViewText(R.id.firsttime, "9-11");
				} else {
					rv.setTextViewText(R.id.firsttime, time + "-" + (time + 1));
				}
				rv.setTextViewText(R.id.firstcourse, tCourses.get(0).getName());
				rv.setTextViewText(R.id.firstplace, tCourses.get(0).getPlace());

				time = Integer.parseInt(tCourses.get(1).getTime());
				if (time == 9) {
					rv.setTextViewText(R.id.firsttime, "9-11");
				} else {
					rv.setTextViewText(R.id.firsttime, time + "-" + (time + 1));
				}
				rv.setTextViewText(R.id.firstcourse, tCourses.get(1).getName());
				rv.setTextViewText(R.id.firstplace, tCourses.get(1).getPlace());
			} else if (tCourses.size() == 2) {

				rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
				rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
				rv.setViewVisibility(R.id.downcourse, View.GONE);
				rv.setViewVisibility(R.id.upcourse, View.GONE);
				rv.setViewVisibility(R.id.firstline, View.VISIBLE);
				rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

				time = Integer.parseInt(tCourses.get(0).getTime());
				if (time == 9) {
					rv.setTextViewText(R.id.firsttime, "9-11");
				} else {
					rv.setTextViewText(R.id.firsttime, time + "-" + (time + 1));
				}
				rv.setTextViewText(R.id.firstcourse, tCourses.get(0).getName());
				rv.setTextViewText(R.id.firstplace, tCourses.get(0).getPlace());

				time = Integer.parseInt(tCourses.get(1).getTime());
				if (time == 9) {
					rv.setTextViewText(R.id.firsttime, "9-11");
				} else {
					rv.setTextViewText(R.id.firsttime, time + "-" + (time + 1));
				}
				rv.setTextViewText(R.id.firstcourse, tCourses.get(1).getName());
				rv.setTextViewText(R.id.firstplace, tCourses.get(1).getPlace());
			} else if (tCourses.size() == 1) {

				rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
				rv.setViewVisibility(R.id.secondimage, View.GONE);
				rv.setViewVisibility(R.id.downcourse, View.GONE);
				rv.setViewVisibility(R.id.upcourse, View.GONE);
				rv.setViewVisibility(R.id.firstline, View.VISIBLE);
				rv.setViewVisibility(R.id.thirdline, View.GONE);

				time = Integer.parseInt(tCourses.get(0).getTime());
				if (time == 9) {
					rv.setTextViewText(R.id.firsttime, "9-11");
				} else {
					rv.setTextViewText(R.id.firsttime, time + "-" + (time + 1));
				}
				rv.setTextViewText(R.id.firstcourse, tCourses.get(0).getName());
				rv.setTextViewText(R.id.firstplace, tCourses.get(0).getPlace());
			} else {
				rv.setTextViewText(R.id.firstplace, " ");

				rv.setTextViewText(R.id.firsttime, " ");
				rv.setTextViewText(R.id.firstcourse, " ");
				rv.setViewVisibility(R.id.firstline, View.GONE);
				rv.setViewVisibility(R.id.firstimage, View.GONE);
				rv.setTextViewText(R.id.secondcourse, " ");
				rv.setViewVisibility(R.id.secondimage, View.GONE);
				// rv.setViewVisibility(R.id.secondline, View.GONE);
				rv.setTextViewText(R.id.secondplace, " ");
				rv.setTextViewText(R.id.secondtime, " ");
				rv.setViewVisibility(R.id.thirdline, View.GONE);
				rv.setViewVisibility(R.id.downcourse, View.GONE);
				rv.setViewVisibility(R.id.upcourse, View.GONE);
			}
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentName = new ComponentName(context,
					MyWidgetProvider.class);
			appWidgetManager.updateAppWidget(componentName, rv);

		}
	}

	public void updatepage(Context context) {

		sharePre = SharePre.getInstance(context);
		int intpage = sharePre.getInt("page", 1);
		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		if (sharePre.getString(S.LOGIN_TYPE, "").equals("本科生")) {
			if (intpage == 1) {
				if (Courses.size() > 2) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.upcourse, View.GONE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);
					rv.setTextViewText(R.id.firsttime, Courses.get(0).getTime());
					rv.setTextViewText(R.id.firstcourse, Courses.get(0)
							.getName()
							+ " ("
							+ Courses.get(0).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.firstplace, Courses.get(0)
							.getPlace());

					rv.setTextViewText(R.id.secondtime, Courses.get(1)
							.getTime());
					rv.setTextViewText(R.id.secondcourse, Courses.get(1)
							.getName()
							+ " ("
							+ Courses.get(1).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.secondplace, Courses.get(1)
							.getPlace());
				} else if (Courses.size() == 2) {

					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.GONE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					rv.setTextViewText(R.id.firsttime, Courses.get(0).getTime());
					rv.setTextViewText(R.id.firstcourse, Courses.get(0)
							.getName()
							+ " ("
							+ Courses.get(0).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.firstplace, Courses.get(0)
							.getPlace());

					rv.setTextViewText(R.id.secondtime, Courses.get(1)
							.getTime());
					rv.setTextViewText(R.id.secondcourse, Courses.get(1)
							.getName()
							+ " ("
							+ Courses.get(1).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.secondplace, Courses.get(1)
							.getPlace());
				} else if (Courses.size() == 1) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.GONE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.GONE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.GONE);

					rv.setTextViewText(R.id.firsttime, Courses.get(0).getTime());
					rv.setTextViewText(R.id.firstcourse, Courses.get(0)
							.getName()
							+ " ("
							+ Courses.get(0).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.firstplace, Courses.get(0)
							.getPlace());
				} else {
					rv.setTextViewText(R.id.firstplace, " ");

					rv.setTextViewText(R.id.firsttime, " ");
					rv.setTextViewText(R.id.firstcourse, " ");
					rv.setViewVisibility(R.id.firstline, View.GONE);
					rv.setViewVisibility(R.id.firstimage, View.GONE);
					rv.setTextViewText(R.id.secondcourse, " ");
					rv.setViewVisibility(R.id.secondimage, View.GONE);
					// rv.setViewVisibility(R.id.secondline, View.GONE);
					rv.setTextViewText(R.id.secondplace, " ");
					rv.setTextViewText(R.id.secondtime, " ");
					rv.setViewVisibility(R.id.thirdline, View.GONE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.GONE);
				}
			} else if (intpage == 2) {
				if (Courses.size() > 3) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.upcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					rv.setTextViewText(R.id.firsttime, Courses.get(1).getTime());
					rv.setTextViewText(R.id.firstcourse, Courses.get(1)
							.getName()
							+ " ("
							+ Courses.get(1).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.firstplace, Courses.get(1)
							.getPlace());

					rv.setTextViewText(R.id.secondtime, Courses.get(2)
							.getTime());
					rv.setTextViewText(R.id.secondcourse, Courses.get(2)
							.getName()
							+ " ("
							+ Courses.get(2).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.secondplace, Courses.get(2)
							.getPlace());
				} else if (Courses.size() == 3) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					rv.setTextViewText(R.id.firsttime, Courses.get(1).getTime());
					rv.setTextViewText(R.id.firstcourse, Courses.get(1)
							.getName()
							+ " ("
							+ Courses.get(1).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.firstplace, Courses.get(1)
							.getPlace());

					rv.setTextViewText(R.id.secondtime, Courses.get(2)
							.getTime());
					rv.setTextViewText(R.id.secondcourse, Courses.get(2)
							.getName()
							+ " ("
							+ Courses.get(2).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.secondplace, Courses.get(2)
							.getPlace());
				}
			} else if (intpage == 3) {
				if (Courses.size() > 4) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.upcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					rv.setTextViewText(R.id.firsttime, Courses.get(2).getTime());
					rv.setTextViewText(R.id.firstcourse, Courses.get(2)
							.getName()
							+ " ("
							+ Courses.get(2).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.firstplace, Courses.get(2)
							.getPlace());

					rv.setTextViewText(R.id.secondtime, Courses.get(3)
							.getTime());
					rv.setTextViewText(R.id.secondcourse, Courses.get(3)
							.getName()
							+ " ("
							+ Courses.get(3).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.secondplace, Courses.get(3)
							.getPlace());
				} else if (Courses.size() == 4) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					rv.setTextViewText(R.id.firsttime, Courses.get(2).getTime());
					rv.setTextViewText(R.id.firstcourse, Courses.get(2)
							.getName()
							+ " ("
							+ Courses.get(2).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.firstplace, Courses.get(2)
							.getPlace());

					rv.setTextViewText(R.id.secondtime, Courses.get(3)
							.getTime());
					rv.setTextViewText(R.id.secondcourse, Courses.get(3)
							.getName()
							+ " ("
							+ Courses.get(3).getTeacher()
							+ ")");
					rv.setTextViewText(R.id.secondplace, Courses.get(3)
							.getPlace());
				}
			} else if (intpage == 4) {
				rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
				rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
				rv.setViewVisibility(R.id.downcourse, View.GONE);
				rv.setViewVisibility(R.id.firstline, View.VISIBLE);
				rv.setViewVisibility(R.id.thirdline, View.VISIBLE);
				rv.setViewVisibility(R.id.upcourse, View.VISIBLE);

				rv.setTextViewText(R.id.firsttime, Courses.get(3).getTime());
				rv.setTextViewText(R.id.firstcourse, Courses.get(3).getName()
						+ " (" + Courses.get(3).getTeacher() + ")");
				rv.setTextViewText(R.id.firstplace, Courses.get(3).getPlace());

				rv.setTextViewText(R.id.secondtime, Courses.get(4).getTime());
				rv.setTextViewText(R.id.secondcourse, Courses.get(4).getName()
						+ " (" + Courses.get(4).getTeacher() + ")");

			}
		} else if (sharePre.getString(S.LOGIN_TYPE, "").equals("教师")) {
			int time;
			if (intpage == 1) {
				if (tCourses.size() > 2) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.upcourse, View.GONE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					time = Integer.parseInt(tCourses.get(0).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(0)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(0)
							.getPlace());

					time = Integer.parseInt(tCourses.get(1).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(1)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(1)
							.getPlace());
				} else if (tCourses.size() == 2) {

					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.GONE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					time = Integer.parseInt(tCourses.get(0).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(0)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(0)
							.getPlace());

					time = Integer.parseInt(tCourses.get(1).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(1)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(1)
							.getPlace());
				} else if (tCourses.size() == 1) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.GONE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.GONE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.GONE);

					time = Integer.parseInt(tCourses.get(0).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(0)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(0)
							.getPlace());
				} else {
					rv.setTextViewText(R.id.firstplace, " ");

					rv.setTextViewText(R.id.firsttime, " ");
					rv.setTextViewText(R.id.firstcourse, " ");
					rv.setViewVisibility(R.id.firstline, View.GONE);
					rv.setViewVisibility(R.id.firstimage, View.GONE);
					rv.setTextViewText(R.id.secondcourse, " ");
					rv.setViewVisibility(R.id.secondimage, View.GONE);
					// rv.setViewVisibility(R.id.secondline, View.GONE);
					rv.setTextViewText(R.id.secondplace, " ");
					rv.setTextViewText(R.id.secondtime, " ");
					rv.setViewVisibility(R.id.thirdline, View.GONE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.GONE);
				}
			} else if (intpage == 2) {
				if (tCourses.size() > 3) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.upcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					time = Integer.parseInt(tCourses.get(1).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(1)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(1)
							.getPlace());

					time = Integer.parseInt(tCourses.get(2).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(2)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(2)
							.getPlace());
				} else if (Courses.size() == 3) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					time = Integer.parseInt(tCourses.get(1).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(1)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(1)
							.getPlace());

					time = Integer.parseInt(tCourses.get(2).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(2)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(2)
							.getPlace());
				}
			} else if (intpage == 3) {
				if (tCourses.size() > 4) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.upcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					time = Integer.parseInt(tCourses.get(2).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(2)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(2)
							.getPlace());

					time = Integer.parseInt(tCourses.get(3).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(3)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(3)
							.getPlace());
				} else if (Courses.size() == 4) {
					rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
					rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
					rv.setViewVisibility(R.id.downcourse, View.GONE);
					rv.setViewVisibility(R.id.upcourse, View.VISIBLE);
					rv.setViewVisibility(R.id.firstline, View.VISIBLE);
					rv.setViewVisibility(R.id.thirdline, View.VISIBLE);

					time = Integer.parseInt(tCourses.get(2).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(2)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(2)
							.getPlace());

					time = Integer.parseInt(tCourses.get(3).getTime());
					if (time == 9) {
						rv.setTextViewText(R.id.firsttime, "9-11");
					} else {
						rv.setTextViewText(R.id.firsttime, time + "-"
								+ (time + 1));
					}
					rv.setTextViewText(R.id.firstcourse, tCourses.get(3)
							.getName());
					rv.setTextViewText(R.id.firstplace, tCourses.get(3)
							.getPlace());
				}
			} else if (intpage == 4) {
				rv.setViewVisibility(R.id.firstimage, View.VISIBLE);
				rv.setViewVisibility(R.id.secondimage, View.VISIBLE);
				rv.setViewVisibility(R.id.downcourse, View.GONE);
				rv.setViewVisibility(R.id.firstline, View.VISIBLE);
				rv.setViewVisibility(R.id.thirdline, View.VISIBLE);
				rv.setViewVisibility(R.id.upcourse, View.VISIBLE);

				time = Integer.parseInt(tCourses.get(3).getTime());
				if (time == 9) {
					rv.setTextViewText(R.id.firsttime, "9-11");
				} else {
					rv.setTextViewText(R.id.firsttime, time + "-" + (time + 1));
				}
				rv.setTextViewText(R.id.firstcourse, tCourses.get(3).getName());
				rv.setTextViewText(R.id.firstplace, tCourses.get(3).getPlace());

				time = Integer.parseInt(tCourses.get(4).getTime());
				if (time == 9) {
					rv.setTextViewText(R.id.firsttime, "9-11");
				} else {
					rv.setTextViewText(R.id.firsttime, time + "-" + (time + 1));
				}
				rv.setTextViewText(R.id.firstcourse, tCourses.get(4).getName());
				rv.setTextViewText(R.id.firstplace, tCourses.get(4).getPlace());

			}
		}
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName componentName = new ComponentName(context,
				MyWidgetProvider.class);
		appWidgetManager.updateAppWidget(componentName, rv);

	}

	public int findMap(String time1, String time2,
			List<CourseResultStuBean> tempBeans) {
		for (int i = 0; i < tempBeans.size(); i++) {
			CourseResultStuBean bean = tempBeans.get(i);
			if (bean.getLessonTime().contains(time1 + time2))//
			{

				return i;
			}

		}
		return -1;
	}

	public int findMapCourse(String time1, String time2,
			List<MyLesson> tempBeans) {

		if (tempBeans.size() > 0) {
			for (int i = 0; i < tempBeans.size(); i++) {
				MyLesson bean = tempBeans.get(i);
				if ((bean.getWeek().equals(time1))
						&& (bean.getTime().equals(time2)))//
				{
					return i;
				}
			}
		}
		return -1;
	}

	public CourseResultStuBean findMapbean(String time1, String time2,
			List<CourseResultStuBean> tempBeans) {
		for (int i = 0; i < tempBeans.size(); i++) {
			CourseResultStuBean bean = tempBeans.get(i);
			if (bean.getLessonTime().contains(time1 + time2)) {

				return bean;
			}

		}
		return null;
	}

	public String getWeek() {
		String week = "";
		if (nowweek == 0) {
			nowweek = 6;
		} else if (nowweek == 1) {
			nowweek = 0;
		} else {
			nowweek = nowweek - 1;
		}
		switch (nowweek) {
		case 1:
			week = "星期一";
			break;
		case 2:
			week = "星期二";
			break;
		case 3:
			week = "星期三";
			break;
		case 4:
			week = "星期四";
			break;
		case 5:
			week = "星期五";
			break;
		case 6:
			week = "星期六";
			break;
		case 0:
			week = "星期日";
			break;
		}
		return week;
	}

	public class MyLesson {
		private String time;
		private String name;
		private String place;
		private String teacher;
		private String week;

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPlace() {
			return place;
		}

		public void setPlace(String place) {
			this.place = place;
		}

		public String getTeacher() {
			return teacher;
		}

		public void setTeacher(String teacher) {
			this.teacher = teacher;
		}

		public String getWeek() {
			return week;
		}

		public void setWeek(String week) {
			this.week = week;
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	// private static Timer myTimer;

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		updateAllInfo(context);
		updateweek(context);
		updatepage(context);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		sharePre = SharePre.getInstance(context);
		nowweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		updateAllInfo(context);
		// updateweek(context);

		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		for (int i = 0; i < appWidgetIds.length; i++) {
			System.out.println("update");
			Intent intent1 = new Intent();
			intent1.setAction(widgetupdateweekprev);
			PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
					0, intent1, 0);
			rv.setOnClickPendingIntent(R.id.weekprev, pendingIntent1);

			Intent intent2 = new Intent(context, LoginActivity.class);
			PendingIntent pendingIntent2 = PendingIntent.getActivity(context,
					0, intent2, 0);
			rv.setOnClickPendingIntent(R.id.loginimage, pendingIntent2);

			Intent intent3 = new Intent();
			intent3.setAction(widgetupdateweeknext);
			PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context,
					0, intent3, 0);
			rv.setOnClickPendingIntent(R.id.weeknext, pendingIntent3);

			Intent intent4 = new Intent();
			intent4.setAction(widgetupdatepageprev);
			PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context,
					0, intent4, 0);
			rv.setOnClickPendingIntent(R.id.upcourse, pendingIntent4);

			Intent intent5 = new Intent();
			intent5.setAction(widgetupdatepagenext);
			PendingIntent pendingIntent5 = PendingIntent.getBroadcast(context,
					0, intent5, 0);
			rv.setOnClickPendingIntent(R.id.downcourse, pendingIntent5);

			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);

	}

	@Override
	public void onReceive(Context context, Intent intent) {

		// TODO Auto-generated method stub

		super.onReceive(context, intent);
		sharePre = SharePre.getInstance(context);
		System.out.println("onReceive" + datas + " " + tdatas + " ");
		if ((datas == null && tdatas == null)
				|| (datas.size() == 0 && tdatas.size() == 0)) {
			nowweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
			updateAllInfo(context);
			updateweek(context);
			updatepage(context);
		} else {
			if (intent.getAction().equals(widgetupdate)) {

				Courses = new ArrayList<MyLesson>();
				tCourses = new ArrayList<CourseResultTeaBean>();
				int intweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
				sharePre.setInt("page", 1);
				sharePre.setInt("nowweek", intweek);
				updateAllInfo(context);
			} else if (intent.getAction().equals(widgetupdaterefresh)) {
				nowweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

				Courses = new ArrayList<MyLesson>();
				tCourses = new ArrayList<CourseResultTeaBean>();
				System.out.println("update");
				updateweek(context);

			} else if (intent.getAction().equals(widgetupdateweekprev)) {
				Courses = new ArrayList<MyLesson>();
				tCourses = new ArrayList<CourseResultTeaBean>();
				int intweek = sharePre.getInt("nowweek", 1);
				if (intweek == 0) {
					intweek = 6;
				} else if (intweek == 1) {
					intweek = 0;
				} else {
					intweek--;
				}
				sharePre.setInt("nowweek", intweek);
				sharePre.setInt("page", 1);
				updateweek(context);

			} else if (intent.getAction().equals(widgetupdateweeknext)) {

				Courses = new ArrayList<MyLesson>();
				tCourses = new ArrayList<CourseResultTeaBean>();
				int intweek = sharePre.getInt("nowweek", 1);
				if (intweek == 6) {
					intweek = 0;
				} else {
					intweek = intweek + 1;
				}
				sharePre.setInt("nowweek", intweek);
				sharePre.setInt("page", 1);
				updateweek(context);

			} else if (intent.getAction().equals(widgetupdatepageprev)) {
				int intpage = sharePre.getInt("page", 1);
				if (intpage > 1) {
					intpage = intpage - 1;
					sharePre.setInt("page", intpage);
					updatepage(context);
				}

			} else if (intent.getAction().equals(widgetupdatepagenext)) {
				int intpage = sharePre.getInt("page", 1);
				intpage = intpage + 1;
				sharePre.setInt("page", intpage);
				updatepage(context);

			}
		}
	}

}