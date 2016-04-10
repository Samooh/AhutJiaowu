package com.mjiaowu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.entity.base.BaseActivity;

public class AboutActivity extends BaseActivity {

	private ImageView mBack;
	private TextView download;
	private Button btnScan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		System.out.println("AboutActivity");
		download = (TextView) findViewById(R.id.txtDownload);
		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);
		btnScan = (Button) findViewById(R.id.scan_user_key);
		download.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse("http://211.70.149.139");
				intent.setData(content_url);
				startActivity(intent);
			}
		});
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// startActivityForResult(new Intent(getApplicationContext(),
				// CaptureActivity.class),GET_CODE);
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
