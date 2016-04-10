package com.mjiaowu;

import java.text.SimpleDateFormat;

import cn.jpush.android.api.JPushInterface;

import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonInfoActivity extends BaseRequestActivity {

	private TextView textmyhome, textmysetting;

	private ImageView myback, mytohome, mytosetting;
	private LinearLayout course,exam,score,select,just,info;

	private int x1, x2, y1, y2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personifo);

		course=(LinearLayout)findViewById(R.id.course);
		exam=(LinearLayout)findViewById(R.id.exam);
		score=(LinearLayout)findViewById(R.id.score);
		select=(LinearLayout)findViewById(R.id.select);
		just=(LinearLayout)findViewById(R.id.just);
		info=(LinearLayout)findViewById(R.id.info);				

		textmyhome = (TextView) findViewById(R.id.textmyhome);
		textmysetting = (TextView) findViewById(R.id.textmysetting);

		course.setOnClickListener(new onClickListener());
		exam.setOnClickListener(new onClickListener());
		score.setOnClickListener(new onClickListener());
		select.setOnClickListener(new onClickListener());
		just.setOnClickListener(new onClickListener());
		info.setOnClickListener(new onClickListener());
		
		textmysetting.setOnClickListener(new onClickListener());
		textmyhome.setOnClickListener(new onClickListener());

		myback = (ImageView) findViewById(R.id.myback);
		mytohome = (ImageView) findViewById(R.id.myhome);
		mytosetting = (ImageView) findViewById(R.id.mysetting);

		myback.setOnClickListener(new imageClickListener());
		mytohome.setOnClickListener(new imageClickListener());
		mytosetting.setOnClickListener(new imageClickListener());

	}

	public class imageClickListener implements ImageView.OnClickListener {
		Intent intent = new Intent();

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.myback) {
				intent.setClass(PersonInfoActivity.this, MainActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.myhome) {
				intent.setClass(PersonInfoActivity.this, MainActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.mysetting) {
				intent.setClass(PersonInfoActivity.this, SettingActivity.class);
				startActivity(intent);
			}
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

	public class onClickListener implements TextView.OnClickListener {
		Intent intent = new Intent();

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (sharePre.getBoolean(S.LOGIN_IS, false)) {
				if (v.getId() == R.id.course) {
					String xn, xq;
					SimpleDateFormat sdfy = new SimpleDateFormat("yyyy");
					int year = Integer.parseInt(sdfy
							.format(new java.util.Date()));
					SimpleDateFormat sdfm = new SimpleDateFormat("MM");
					int month = Integer.parseInt(sdfm
							.format(new java.util.Date()));
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
					Bundle b = new Bundle();
					b.putString("xn", xn);
					b.putString("xq", xq);
					intent.putExtra("time", "周");
					intent.putExtras(b);
					if (sharePre.getString(S.LOGIN_TYPE, "").equals("教师")) {
						intent.setClass(PersonInfoActivity.this,
								CourseExcelTeaActivity.class);
						startActivity(intent);
					} else {
						intent.setClass(PersonInfoActivity.this,
								CourseResultActivity.class);
						startActivity(intent);
					}
				} else if (v.getId() == R.id.exam) {
					intent.setClass(PersonInfoActivity.this, ExamActivity.class);
					startActivity(intent);
				} else if (v.getId() == R.id.score) {
					intent.setClass(PersonInfoActivity.this,
							ScoreSearchActivity.class);
					startActivity(intent);
				} else if (v.getId() == R.id.select) {
					intent.setClass(PersonInfoActivity.this,
							WebClassListActivity.class);
					startActivity(intent);
				} else if (v.getId() == R.id.just) {
					intent.setClass(PersonInfoActivity.this,
							RemarkListActivity.class);
					startActivity(intent);
				} else if (v.getId() == R.id.info) {
					if (sharePre.getString(S.LOGIN_TYPE, "").equals("教师")) {
						Intent intent = new Intent();
						intent.putExtra("way", "1");
						intent.setClass(getApplicationContext(),
								NoteTeaListActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent();
						intent.putExtra("way", "5");
						intent.setClass(getApplicationContext(),
								NoteTeaListActivity.class);
						startActivity(intent);
					}
				}
			} else {
				intent.setClass(PersonInfoActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			if (v.getId() == R.id.textmyhome) {
				intent.setClass(PersonInfoActivity.this, MainActivity.class);
				startActivity(intent);

			} else if (v.getId() == R.id.textmysetting) {
				intent.setClass(PersonInfoActivity.this, SettingActivity.class);
				startActivity(intent);

			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			x1 = (int) event.getX();
			y1 = (int) event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			int y;
			x2 = (int) event.getX();
			y2 = (int) event.getY();
			y = (y1 - y2);
			if (y < 0) {
				y = -y;
			}
			if (x1 - x2 > 35 && y < 30) {
				// tosetting
				openActivity(SettingActivity.class, new Bundle());
			} else if (x2 - x1 > 50 && y < 60) {
				// tohome
				openActivity(MainActivity.class, new Bundle());
			}
		}
		return super.onTouchEvent(event);
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
