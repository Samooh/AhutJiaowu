package com.ahutjw.entity.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.ahutjw.utils.SharePre;
import com.umeng.analytics.MobclickAgent;

public class BaseFragmentActivity extends FragmentActivity {
	protected SharePre sharePre;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		MobclickAgent.onError(this);
		sharePre = SharePre.getInstance(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void finish()
	{
		super.finish();
		//overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}
	
	public void defaultFinish()
	{
		super.finish();
	}
}
