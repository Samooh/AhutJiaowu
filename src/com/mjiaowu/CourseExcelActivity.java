package com.mjiaowu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiCourse;
import com.ahutjw.app.entity.CourseResultStuBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.ClassPopupWindow;
import com.ahutjw.utils.S;

public class CourseExcelActivity extends BaseRequestActivity {

	private DbManager dbM;
	private List<CourseResultStuBean> datas;
	private String time;
	private String loginUser, loginPwd, xn, xq;
	private ImageView back;
	private List<TextView> group;
	private ClassPopupWindow coursedetail;
	private Bundle bundle;
	private TextView text11, text12, text13, text14, text15, text16, text17;
	private TextView text21, text22, text23, text24, text25, text26, text27;
	private TextView text31, text32, text33, text34, text35, text36, text37;
	private TextView text41, text42, text43, text44, text45, text46, text47;
	private TextView text51, text52, text53, text54, text55, text56, text57;
	private CourseResultStuBean bean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_excel);
		if (!sharePre.getBoolean(S.LOGIN_IS, false)) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
		}
		getView();
		dbM = new DbManager(this);
		bundle = getIntent().getExtras();

		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated methodstub
				finish();
			}
		});

		datas = new ArrayList<CourseResultStuBean>();

		loginUser = sharePre.getString(S.LOGIN_USERNAME, "");
		loginPwd = sharePre.getString(S.LOGIN_PWD, "");
		if (loginUser == null || loginUser == "" || loginPwd == ""
				|| loginPwd == null) {
			Intent intent = new Intent();
			intent.setClass(CourseExcelActivity.this, LoginActivity.class);
			startActivity(intent);

		}
		// 加载数据

		//
		if (sharePre.getBoolean(S.LOGIN_IS, false)) {
			sendRequest(loginUser, loginPwd, bundle.getString("xn"),
					bundle.getString("xq"));
		}
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "数据加载中...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiCourse.queryCourseResultList(params[0], params[1], params[2],
				params[3]);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loginUser = sharePre.getString(S.LOGIN_USERNAME, "");
		loginPwd = sharePre.getString(S.LOGIN_PWD, "");
		if (loginUser == null || loginUser == "" || loginPwd == ""
				|| loginPwd == null) {
			Intent intent = new Intent();
			intent.setClass(CourseExcelActivity.this, LoginActivity.class);
			startActivity(intent);

		}
		// 加载数据

		//
		if (sharePre.getBoolean(S.LOGIN_IS, false)) {
			sendRequest(loginUser, loginPwd, bundle.getString("xn"),
					bundle.getString("xq"));
		}
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<CourseResultStuBean> tempBeans = (List<CourseResultStuBean>) result;
		// 缓存数据库
		if (tempBeans.size() > 0) {
			dbM.deleteAll(DbManager.course.TABLE_NAME);
			for (int i = 0; i < tempBeans.size(); i++) {
				CourseResultStuBean bean = tempBeans.get(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DbManager.course.COLUMN_ID, i + "");
				map.put(DbManager.course.COLUMN_XN, xn);
				map.put(DbManager.course.COLUMN_XQ, xq);
				map.put(DbManager.course.COLUMN_LESSONID, bean.getLessonId());
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
					DbManager.course.TABLE_NAME, DbManager.course.columns);
			for (int i = 0; i < datasLst.size(); i++) {
				HashMap<String, String> map = datasLst.get(i);
				CourseResultStuBean temp = new CourseResultStuBean();
				temp.setLessonId(map.get(DbManager.course.COLUMN_LESSONID));
				temp.setLessonName(map.get(DbManager.course.COLUMN_LESSONNAME));
				temp.setLessonPlace(map
						.get(DbManager.course.COLUMN_LESSONPLACE));
				temp.setLessonProperty(map
						.get(DbManager.course.COLUMN_LESSONPROPERTY));
				temp.setLessonScore(map
						.get(DbManager.course.COLUMN_LESSONSCORE));
				temp.setLessonTime(map.get(DbManager.course.COLUMN_LESSONTIME));
				temp.setTeacherName(map
						.get(DbManager.course.COLUMN_TEACHERNAME));
				tempBeans.add(temp);
			}
		}
		datas.addAll(tempBeans);
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
				System.out.println(time + time1);
				bean = findMap(time, time1, tempBeans);
				if (bean != null) {
					int position = (i - 1) * 5 + j - 1;
					System.out.println(position + "");
					group.get(position).setText(bean.getLessonName());
				}

			}

		}

	}

	// adapter.notifyDataSetChanged();

	public CourseResultStuBean findMap(String time1, String time2,
			List<CourseResultStuBean> tempBeans) {
		for (int i = 0; i < tempBeans.size(); i++) {
			CourseResultStuBean bean = tempBeans.get(i);
			if (bean.getLessonTime().contains(time1 + time2))//
			{

				return bean;
			}

		}
		return null;
	}

	public void getView() {
		group = new ArrayList<TextView>();
		text11 = (TextView) findViewById(R.id.class11);
		text12 = (TextView) findViewById(R.id.class12);
		text13 = (TextView) findViewById(R.id.class13);
		text14 = (TextView) findViewById(R.id.class14);
		text15 = (TextView) findViewById(R.id.class15);
		text16 = (TextView) findViewById(R.id.class16);
		text17 = (TextView) findViewById(R.id.class17);

		text21 = (TextView) findViewById(R.id.class21);
		text22 = (TextView) findViewById(R.id.class22);
		text23 = (TextView) findViewById(R.id.class23);
		text24 = (TextView) findViewById(R.id.class24);
		text25 = (TextView) findViewById(R.id.class25);
		text26 = (TextView) findViewById(R.id.class26);
		text27 = (TextView) findViewById(R.id.class27);

		text31 = (TextView) findViewById(R.id.class31);
		text32 = (TextView) findViewById(R.id.class32);
		text33 = (TextView) findViewById(R.id.class33);
		text34 = (TextView) findViewById(R.id.class34);
		text35 = (TextView) findViewById(R.id.class35);
		text36 = (TextView) findViewById(R.id.class36);
		text37 = (TextView) findViewById(R.id.class37);

		text41 = (TextView) findViewById(R.id.class41);
		text42 = (TextView) findViewById(R.id.class42);
		text43 = (TextView) findViewById(R.id.class43);
		text44 = (TextView) findViewById(R.id.class44);
		text45 = (TextView) findViewById(R.id.class45);
		text46 = (TextView) findViewById(R.id.class46);
		text47 = (TextView) findViewById(R.id.class47);

		text51 = (TextView) findViewById(R.id.class51);
		text52 = (TextView) findViewById(R.id.class52);
		text53 = (TextView) findViewById(R.id.class53);
		text54 = (TextView) findViewById(R.id.class54);
		text55 = (TextView) findViewById(R.id.class55);
		text56 = (TextView) findViewById(R.id.class56);
		text57 = (TextView) findViewById(R.id.class57);

		group.add(text11);
		group.add(text21);
		group.add(text31);
		group.add(text41);
		group.add(text51);

		group.add(text12);
		group.add(text22);
		group.add(text32);
		group.add(text42);
		group.add(text52);

		group.add(text13);
		group.add(text23);
		group.add(text33);
		group.add(text43);
		group.add(text53);

		group.add(text14);
		group.add(text24);
		group.add(text34);
		group.add(text44);
		group.add(text54);

		group.add(text15);
		group.add(text25);
		group.add(text35);
		group.add(text45);
		group.add(text55);

		group.add(text16);
		group.add(text26);
		group.add(text36);
		group.add(text46);
		group.add(text56);

		group.add(text17);
		group.add(text27);
		group.add(text37);
		group.add(text47);
		group.add(text57);

		for (int i = 0; i < group.size(); i++) {
			group.get(i).setOnClickListener(new ClassListener());
		}

	}

	public class ClassListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TextView text = (TextView) findViewById(v.getId());
			int index = group.lastIndexOf(text);
			int mark = 0;
			System.out.println(">>>>>>>>>>>" + index + "");
			for (int i = 0; i < index; i++) {
				if (group.get(i).getText().equals(text.getText())) {
					mark = 1;
				}
			}
			for (int i = 0; i < group.size(); i++) {
				group.get(i).setTextColor(Color.BLACK);
			}
			text.setTextColor(Color.BLUE);
			if (text.getText() != null && text.getText() != "") {
				bean = findCourse(text.getText().toString());
				System.out
						.println(bean.getLessonName() + bean.getLessonPlace());
				coursedetail = new ClassPopupWindow(CourseExcelActivity.this,
						null);
				int h = CourseExcelActivity.this.getWindowManager()
						.getDefaultDisplay().getHeight();
				int w = CourseExcelActivity.this.getWindowManager()
						.getDefaultDisplay().getWidth();
				coursedetail.showAtLocation(
						CourseExcelActivity.this.findViewById(R.id.back),
						Gravity.TOP | Gravity.RIGHT, w / 3, h / 3); // 设置layout在PopupWindow中显示的位置
				TextView courseplace = (TextView) coursedetail.getContentView()
						.findViewById(R.id.courseplace);
				TextView teachername = (TextView) coursedetail.getContentView()
						.findViewById(R.id.teachername);
				TextView coursescore = (TextView) coursedetail.getContentView()
						.findViewById(R.id.coursescore);
				if (mark == 1) {
					courseplace.setText(bean.getLessonPlace().split(";")[1]);
				} else {
					courseplace.setText(bean.getLessonPlace().split(";")[0]);
				}

				teachername.setText("教师：" + bean.getTeacherName());
				coursescore.setText("学分：" + bean.getLessonScore());
				coursedetail.getContentView().findViewById(R.id.enterclass)
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								coursedetail.dismiss();
								Bundle bundle = new Bundle();
								bundle.putString("lessonname",
										bean.getLessonName());
								bundle.putString("lessonid", bean.getLessonId());
								bundle.putString("teachername",
										bean.getTeacherName());
								openActivity(CourseDetailActivity.class, bundle);

							}
						});
			}

		}
	}

	public CourseResultStuBean findCourse(String Name) {
		for (int i = 0; i < datas.size(); i++) {
			CourseResultStuBean bean = datas.get(i);
			if (bean.getLessonName().equals(Name))//
			{
				return bean;
			}

		}
		return null;

	}
}
