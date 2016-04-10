package com.mjiaowu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.CourseResultStuAdapter;
import com.ahutjw.api.ApiCourse;
import com.ahutjw.app.entity.CourseResultStuBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class CourseStuDetailActivity extends BaseRequestActivity {

	private DbManager dbM;
	private ListView listcourse;
	private List<CourseResultStuBean> datas;
	private CourseResultStuAdapter adapter;
	private CourseResultStuBean bean;
	private String time;
	private String loginUser, loginPwd, xn, xq;
	private ImageView back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_result_stu_list);
		Bundle bundle = getIntent().getExtras();
		time = getIntent().getStringExtra("time");

		System.out.println(time + bundle.getString("xn")
				+ bundle.getString("xq"));
		dbM = new DbManager(this);
		listcourse = (ListView) findViewById(R.id.courselistView);
		listcourse
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						Bundle bundle = new Bundle();
						bundle.putString("lessonname", datas.get(position)
								.getLessonName());
						bundle.putString("lessonid", datas.get(position)
								.getLessonId());
						bundle.putString("teachername", datas.get(position)
								.getTeacherName());
						openActivity(CourseDetailActivity.class, bundle);
					}
				});
		back = (ImageView) findViewById(R.id.courselistback);
		back.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		datas = new ArrayList<CourseResultStuBean>();
		adapter = new CourseResultStuAdapter(this, datas);
		listcourse.setAdapter(adapter);

		loginUser = sharePre.getString(S.LOGIN_USERNAME, "");
		loginPwd = sharePre.getString(S.LOGIN_PWD, "");
		if (loginUser == null || loginUser == "" || loginPwd == ""
				|| loginPwd == null) {
			Intent intent = new Intent();
			intent.setClass(CourseStuDetailActivity.this, LoginActivity.class);
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
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
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
		if (time.equals("周")) {
			datas.addAll(tempBeans);
			adapter.notifyDataSetChanged();
		} else {
			String time1 = "";
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
					datas.add(bean);
				}
			}
			if (datas.size() < 0) {
				bean = new CourseResultStuBean();
				datas.add(bean);
			}
			adapter.notifyDataSetChanged();

		}
	}

	// datas.addAll(tempBeans);
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

}
