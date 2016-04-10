package com.mjiaowu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
//import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.CourseResultTeaAdapter;
import com.ahutjw.api.ApiCourse;
import com.ahutjw.app.entity.CourseResultTeaBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

/**
 * 课表--结果列表
 * 
 */
public class CourseResultTeaActivity extends BaseRequestActivity {
	private ImageView imgBack;
	// 数据库管理
	private DbManager dbM;

	private ListView lstView;
	private CourseResultTeaAdapter adapter;
	private CourseResultTeaBean bean;
	private List<CourseResultTeaBean> datas;
	// 登录信息
	private String loginUser, loginPwd;
	private String time = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_result_tea);
		if (!sharePre.getBoolean(S.LOGIN_IS, false)) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
		}
		time = getIntent().getStringExtra("time");
		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		// 数据库管理器
		dbM = new DbManager(this);
		// 实例化
		lstView = (ListView) findViewById(R.id.list);
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Bundle bundle = new Bundle();

				bundle.putString("lessonname", datas.get(position).getName());
				bundle.putString("weektime", datas.get(position).getWeekDay());
				bundle.putString("time", datas.get(position).getTime());
				bundle.putString("weekcount", datas.get(position)
						.getTimePerWeek());
				bundle.putString("place", datas.get(position).getPlace());

				openActivity(CourseDetailTeaActivity.class, bundle);
			}
		});
		datas = new ArrayList<CourseResultTeaBean>();
		adapter = new CourseResultTeaAdapter(this, datas);
		lstView.setAdapter(adapter);
		// 获取配置内容
		// loginType = sharePre.getString(S.LOGIN_TYPE, "");
		loginUser = sharePre.getString(S.LOGIN_USERNAME, "");
		loginPwd = sharePre.getString(S.LOGIN_PWD, "");
		// 加载数据
		if (sharePre.getBoolean(S.LOGIN_IS, false)) {
			sendRequest(loginUser, loginPwd);
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
	public void onDestroy() {
		super.onDestroy();
		dbM.close();
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "数据加载中...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiCourse.queryCourseResultTea(params[0], params[1]);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		// MediaPlayer mPlayer=MediaPlayer.create(getApplicationContext(),
		// R.raw.tips);
		// mPlayer.start();
		@SuppressWarnings("unchecked")
		List<CourseResultTeaBean> tempBeans = (List<CourseResultTeaBean>) result;
		if (tempBeans.size() > 0) {
			dbM.deleteAll(DbManager.tcourse.TABLE_NAME);
			for (int i = 0; i < tempBeans.size(); i++) {
				CourseResultTeaBean bean = tempBeans.get(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DbManager.tcourse.COLUMN_ID, i + "");
				map.put(DbManager.tcourse.COLUMN_WEEKDAY, bean.getWeekDay());
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
					DbManager.tcourse.TABLE_NAME, DbManager.tcourse.columns);
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
				temp.setWeekDay(map.get(DbManager.tcourse.COLUMN_WEEKDAY));
				tempBeans.add(temp);
			}
		}
		if (time.equals("周")) {
			datas.addAll(tempBeans);
			adapter.notifyDataSetChanged();
		} else {
			if (time.equals("周一")) {
				time = "1";
			} else if (time.equals("周二")) {
				time = "2";
			} else if (time.equals("周三")) {
				time = "3";
			} else if (time.equals("周四")) {
				time = "4";
			} else if (time.equals("周五")) {
				time = "5";
			} else if (time.equals("周六")) {
				time = "6";
			} else if (time.equals("周日")) {
				time = "7";
			}
			String time1 = "";
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
				System.out.println(time + time1);
				bean = findMap(time, time1, tempBeans);
				if (bean != null) {
					datas.add(bean);
				}
			}
			if (datas.size() < 0) {
				bean = new CourseResultTeaBean();
				datas.add(bean);
			}
			adapter.notifyDataSetChanged();
		}
	}

	public CourseResultTeaBean findMap(String time1, String time2,
			List<CourseResultTeaBean> tempBeans) {
		for (int i = 0; i < tempBeans.size(); i++) {
			CourseResultTeaBean bean = tempBeans.get(i);
			if (bean.getTime().equals(time2) && bean.getWeekDay().equals(time1)) {
				return bean;
			}

		}
		return null;
	}
}