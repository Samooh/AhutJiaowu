package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.ClassroomListAdapter;
import com.ahutjw.api.ApiClassroom;
import com.ahutjw.app.entity.ClassBean;
import com.ahutjw.entity.base.BaseRequestActivity;

public class ClassroomListActivity extends BaseRequestActivity {
	private ImageView back;
	private ListView list;
	private List<ClassBean> datas;
	private ClassroomListAdapter adapter;
	private String place;
	private String style;
	private String time1;
	private String time2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classroomlist);

		place = getIntent().getStringExtra("place");
		style = getIntent().getStringExtra("style");
		time1 = getIntent().getStringExtra("time1");
		time2 = getIntent().getStringExtra("time2");

		back = (ImageView) findViewById(R.id.back);
		list = (ListView) findViewById(R.id.list);
		back.setOnClickListener(new ImageView.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		datas = new ArrayList<ClassBean>();
		adapter = new ClassroomListAdapter(this, datas);
		list.setAdapter(adapter);
		sendRequest(place, style, time1, time2);
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiClassroom.queryClassroomlist(params[0], params[1], params[2],
				params[3]);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<ClassBean> temps = (List<ClassBean>) result;
		if (temps.size() > 0) {
			datas.addAll(temps);
			adapter.notifyDataSetChanged();
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
}
