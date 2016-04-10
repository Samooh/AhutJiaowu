package com.mjiaowu;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ReceiverDetailActivity extends Activity {

	private TextView detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receiverdetail);
		Bundle bundle = getIntent().getExtras();
		detail = (TextView) findViewById(R.id.pushdetail);
		detail.setText(bundle.getString(JPushInterface.EXTRA_ALERT));
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
