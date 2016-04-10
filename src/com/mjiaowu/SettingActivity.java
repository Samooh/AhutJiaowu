package com.mjiaowu;

import cn.jpush.android.api.JPushInterface;

import com.ahutjw.db.DbManager;
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

public class SettingActivity extends BaseRequestActivity {

	private LinearLayout setttingback, settingxl, settingabout;
	private TextView textsethome,textsetmy;

	private ImageView settohome, settomy, setback;
	private TextView mylogin;
	private LinearLayout update, clear,login;

	private int x1, x2;

	private int y1, y2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		setttingback = (LinearLayout) findViewById(R.id.settingback);
		settingxl = (LinearLayout) findViewById(R.id.settingxl);
		settingabout = (LinearLayout) findViewById(R.id.settingabout);
		textsethome = (TextView) findViewById(R.id.textsethome);
		textsetmy = (TextView) findViewById(R.id.textsetmy);
		mylogin = (TextView) findViewById(R.id.islogin);
		update = (LinearLayout) findViewById(R.id.update);
		clear = (LinearLayout) findViewById(R.id.clear);
		login = (LinearLayout) findViewById(R.id.login);

		setttingback.setOnClickListener(new onClickListener());
		settingxl.setOnClickListener(new onClickListener());
		settingabout.setOnClickListener(new onClickListener());
		textsethome.setOnClickListener(new onClickListener());
		textsetmy.setOnClickListener(new onClickListener());
		mylogin.setOnClickListener(new onClickListener());
		update.setOnClickListener(new onClickListener());
		clear.setOnClickListener(new onClickListener());
		login.setOnClickListener(new onClickListener());

		setback = (ImageView) findViewById(R.id.setback);
		settohome = (ImageView) findViewById(R.id.sethome);
		settomy = (ImageView) findViewById(R.id.setmy);

		setback.setOnClickListener(new imageClickListener());
		settohome.setOnClickListener(new imageClickListener());
		settomy.setOnClickListener(new imageClickListener());

	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (sharePre.getBoolean(S.LOGIN_IS, false)) {
			mylogin.setText("账号:"
					+ sharePre.getString(S.LOGIN_USERNAME,
							"未登录                 "));
		} else {
			mylogin.setText("未登录                    ");
		}
	}

	public class imageClickListener implements ImageView.OnClickListener {
		Intent intent = new Intent();

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.setback) {
				intent.setClass(SettingActivity.this, MainActivity.class);
				SettingActivity.this.startActivity(intent);
			} else if (v.getId() == R.id.sethome) {
				intent.setClass(SettingActivity.this, MainActivity.class);
				SettingActivity.this.startActivity(intent);
			} else if (v.getId() == R.id.setmy) {
				intent.setClass(SettingActivity.this, PersonInfoActivity.class);
				SettingActivity.this.startActivity(intent);
			}
		}

	}

	public class onClickListener implements TextView.OnClickListener {
		Intent intent = new Intent();

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.settingback) {
				intent.setClass(SettingActivity.this, FeedBackActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.settingxl) {
				intent.setClass(SettingActivity.this, CalendarActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.settingabout) {
				intent.setClass(SettingActivity.this, AboutActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.textsethome) {
				intent.setClass(SettingActivity.this, MainActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.textsetmy) {
				intent.setClass(SettingActivity.this, PersonInfoActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.login||v.getId() == R.id.islogin) {
				intent.setClass(SettingActivity.this, LoginActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.update) {
				showCustomToast("已经是最新版本");
			} else if (v.getId() == R.id.clear) {
				DbManager dm = new DbManager(SettingActivity.this);
				dm.deleteAll(DbManager.course.TABLE_NAME);
				dm.deleteAll(DbManager.exam.TABLE_NAME);
				dm.deleteAll(DbManager.remark.TABLE_NAME);
				dm.close();
				showCustomToast("缓存清除完成");
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
			if (x2 - x1 > 50 && y < 60) {
				// tohome
				openActivity(PersonInfoActivity.class, new Bundle());
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
