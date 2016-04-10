package com.mjiaowu;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassNoticeDetailActivity extends Activity {
	private TextView lessonname, noticedate, noticedetail;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classnoticedetail);
		lessonname = (TextView) findViewById(R.id.lessonname);
		noticedate = (TextView) findViewById(R.id.noticedate);
		noticedetail = (TextView) findViewById(R.id.noticedetail);
		lessonname.setText(getIntent().getStringExtra("lessonname"));
		noticedate.setText(getIntent().getStringExtra("noticedate"));
		noticedetail.setText(getIntent().getStringExtra("noticedetail"));
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
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

}
