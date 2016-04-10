package com.mjiaowu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

/**
 * 课表--结果列表
 * 
 * 
 */

public class CourseResultActivity extends BaseRequestActivity {
	// 数据库管理
	private DbManager dbM;

	// 登录信息
	private ImageView imgBack, morecourse;
	private Bundle bundle;

	private TextView courseview, courseview1, courseview2, courseview3,
			courseview4, courseview5, courseview6, courseview7;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_result);
		if (!sharePre.getBoolean(S.LOGIN_IS, false)) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
		}
		bundle = getIntent().getExtras();
		String data = bundle.getString("xn");
		System.out.println(data);
		courseview = (TextView) findViewById(R.id.Allcourse);
		courseview1 = (TextView) findViewById(R.id.stucourse1);
		courseview2 = (TextView) findViewById(R.id.stucourse2);
		courseview3 = (TextView) findViewById(R.id.stucourse3);
		courseview4 = (TextView) findViewById(R.id.stucourse4);
		courseview5 = (TextView) findViewById(R.id.stucourse5);
		courseview6 = (TextView) findViewById(R.id.stucourse6);
		courseview7 = (TextView) findViewById(R.id.stucourse7);

		courseview.setOnClickListener(new CourseListener());
		courseview1.setOnClickListener(new CourseListener());
		courseview2.setOnClickListener(new CourseListener());
		courseview3.setOnClickListener(new CourseListener());
		courseview4.setOnClickListener(new CourseListener());
		courseview5.setOnClickListener(new CourseListener());
		courseview6.setOnClickListener(new CourseListener());
		courseview7.setOnClickListener(new CourseListener());

		// 数据库管理器
		dbM = new DbManager(this);
		// 实例化
		imgBack = (ImageView) findViewById(R.id.back);
		morecourse = (ImageView) findViewById(R.id.morecourse);

		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { // TODO 自动生成的方法存根 finish();
				finish();
			}
		});
		morecourse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { // TODO 自动生成的方法存根 finish();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						CourseSearchActivity.class);
				startActivity(intent);
				finish();
				// overridePendingTransition(R.anim.push_left_in,
				// R.anim.push_right_out)
			}
		});

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

	public class CourseListener implements TextView.OnClickListener {
		Intent intent = new Intent();

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.Allcourse) {
				intent.putExtras(bundle);
				intent.putExtra("time", "周");
				intent.setClass(CourseResultActivity.this,
						CourseExcelActivity.class);
				CourseResultActivity.this.startActivity(intent);
			} else if (v.getId() == R.id.stucourse1) {
				intent.putExtras(bundle);
				intent.putExtra("time", "周一");
				intent.setClass(CourseResultActivity.this,
						CourseStuDetailActivity.class);
				CourseResultActivity.this.startActivity(intent);

			} else if (v.getId() == R.id.stucourse2) {
				intent.putExtras(bundle);
				intent.putExtra("time", "周二");
				intent.setClass(CourseResultActivity.this,
						CourseStuDetailActivity.class);
				CourseResultActivity.this.startActivity(intent);

			} else if (v.getId() == R.id.stucourse3) {
				intent.putExtras(bundle);
				intent.putExtra("time", "周三");
				intent.setClass(CourseResultActivity.this,
						CourseStuDetailActivity.class);
				CourseResultActivity.this.startActivity(intent);

			} else if (v.getId() == R.id.stucourse4) {
				intent.putExtras(bundle);
				intent.putExtra("time", "周四");
				intent.setClass(CourseResultActivity.this,
						CourseStuDetailActivity.class);
				CourseResultActivity.this.startActivity(intent);

			} else if (v.getId() == R.id.stucourse5) {
				intent.putExtras(bundle);
				intent.putExtra("time", "周五");
				intent.setClass(CourseResultActivity.this,
						CourseStuDetailActivity.class);
				CourseResultActivity.this.startActivity(intent);
			} else if (v.getId() == R.id.stucourse6) {
				intent.putExtras(bundle);
				intent.putExtra("time", "周六");
				intent.setClass(CourseResultActivity.this,
						CourseStuDetailActivity.class);
				CourseResultActivity.this.startActivity(intent);
			} else if (v.getId() == R.id.stucourse7) {
				intent.putExtras(bundle);
				intent.putExtra("time", "周日");
				intent.setClass(CourseResultActivity.this,
						CourseStuDetailActivity.class);
				CourseResultActivity.this.startActivity(intent);
			}

		}
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub

	}
}
