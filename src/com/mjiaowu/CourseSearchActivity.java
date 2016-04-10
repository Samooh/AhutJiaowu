package com.mjiaowu;

import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.entity.base.BaseActivity;
import com.ahutjw.utils.S;

/**
 * 
 * 课表查询
 * 
 */
public class CourseSearchActivity extends BaseActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String loginType = sharePre.getString(S.LOGIN_TYPE, "");
		if (loginType.equals("教师")) {
			openActivity(CourseResultTeaActivity.class);
		} else if (loginType.equals("本科生")) {
			openActivity(CourseSearchStuActivity.class);
		}
		finish();
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
}