package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.ClassNoticeListAdapter;
import com.ahutjw.api.ApiClassNotice;
import com.ahutjw.app.entity.ClassNoticeBean;
import com.ahutjw.entity.base.BaseRequestActivity;

public class ClassNoticeListActivity extends BaseRequestActivity {

	private ListView lstView;
	private ImageView backImageView;
	private ArrayList<ClassNoticeBean> datas;
	private ClassNoticeListAdapter adapter;
	private String courseName, teaNum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.classnoticelist);
		courseName = getIntent().getStringExtra("courseName");
		teaNum = getIntent().getStringExtra("teaNum");
		System.out.println(">>>>>>>>" + courseName + teaNum);
		lstView = (ListView) findViewById(R.id.noticelist);

		backImageView = (ImageView) findViewById(R.id.back);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});

		datas = new ArrayList<ClassNoticeBean>();
		adapter = new ClassNoticeListAdapter(this, datas);
		// lstView.addFooterView(footerBtn);
		lstView.setAdapter(adapter);
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString("lessonname", datas.get(position)
						.getCourseName());
				bundle.putString("noticedate", datas.get(position)
						.getNoticeDate());
				bundle.putString("noticedetail", datas.get(position)
						.getNoticeDetail());
				openActivity(ClassNoticeDetailActivity.class, bundle);
			}
		});

		sendRequest();

		// JPushInterface.setDebugMode(true);

	}

	@Override
	public String onReqestPre() {

		return "正在请求中...";
	}

	// 请求通知
	@Override
	public Object onReqestDoing(String... params) {

		return ApiClassNotice.queryClassNoticeList(courseName, teaNum);
	}

	// 请求完成
	@Override
	public void onReqestFinish(Object result) {
		@SuppressWarnings("unchecked")
		List<ClassNoticeBean> temps = (List<ClassNoticeBean>) result;
		datas.addAll(temps);
		adapter.notifyDataSetChanged();

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
