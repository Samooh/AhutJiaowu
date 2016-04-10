package com.mjiaowu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
//import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiCourse;
import com.ahutjw.app.entity.CourseResultTeaBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.ClassPopupTeaWindow;
import com.ahutjw.utils.S;

/**
 * 课表--结果列表
 * 
 */
public class CourseExcelTeaActivity extends BaseRequestActivity {
	private ImageView imgBack;
	// 数据库管理
	private DbManager dbM;

	private CourseResultTeaBean bean;
	private List<CourseResultTeaBean> datas;
	// 登录信息
	private String loginUser, loginPwd;

	private List<TextView> group;
	private TextView text11, text12, text13, text14, text15, text16, text17;
	private TextView text21, text22, text23, text24, text25, text26, text27;
	private TextView text31, text32, text33, text34, text35, text36, text37;
	private TextView text41, text42, text43, text44, text45, text46, text47;
	private TextView text51, text52, text53, text54, text55, text56, text57;

	private ClassPopupTeaWindow coursedetail;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_exceltea);
		getView();

		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { // TODO 自动生成的方法存根
				finish();
			}

		});

		// 数据库管理器
		dbM = new DbManager(this);
		// 实例化

		datas = new ArrayList<CourseResultTeaBean>();
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
		datas.addAll(tempBeans);
		String time1 = "";
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
				// System.out.println(i + time1);
				bean = findMap(i + "", time1, tempBeans);
				if (bean != null) {
					int position = (i - 1) * 5 + j - 1;
					group.get(position).setText(bean.getName());
				}
			}
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
			for (int i = 0; i < group.size(); i++) {
				group.get(i).setTextColor(Color.BLACK);
			}
			text.setTextColor(Color.BLUE);
			if (text.getText() != null && text.getText() != "") {

				bean = findCourse(text.getText().toString());
				// System.out.println(bean.getName());
				coursedetail = new ClassPopupTeaWindow(
						CourseExcelTeaActivity.this, null);
				int h = CourseExcelTeaActivity.this.getWindowManager()
						.getDefaultDisplay().getHeight();
				int w = CourseExcelTeaActivity.this.getWindowManager()
						.getDefaultDisplay().getWidth();
				coursedetail.showAtLocation(
						CourseExcelTeaActivity.this.findViewById(R.id.back),
						Gravity.TOP | Gravity.RIGHT, w / 3, h / 3); // 设置layout在PopupWindow中显示的位置
				TextView courseplace = (TextView) coursedetail.getContentView()
						.findViewById(R.id.courseplace);
				TextView perweek = (TextView) coursedetail.getContentView()
						.findViewById(R.id.perweek);
				TextView studentmajor = (TextView) coursedetail
						.getContentView().findViewById(R.id.studentmajor);
				courseplace.setText(bean.getPlace());
				perweek.setText(bean.getTimePerWeek());
				studentmajor.setText("授课对象：" + bean.getStudentMajor());
				coursedetail.getContentView().findViewById(R.id.enterclass)
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								coursedetail.dismiss();
								Bundle bundle = new Bundle();
								bundle.putString("lessonname", bean.getName());
								bundle.putString("weektime", bean.getWeekDay());
								bundle.putString("time", bean.getTime());
								bundle.putString("weekcount",
										bean.getTimePerWeek());
								bundle.putString("place", bean.getPlace());

								openActivity(CourseDetailTeaActivity.class,
										bundle);

							}
						});
			}

		}
	}

	public CourseResultTeaBean findCourse(String Name) {
		for (int i = 0; i < datas.size(); i++) {
			CourseResultTeaBean bean = datas.get(i);
			if (bean.getName().equals(Name))//
			{
				return bean;
			}

		}
		return null;

	}
}