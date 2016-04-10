package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.StudentListAdapter;
import com.ahutjw.api.ApiStudentList;
import com.ahutjw.app.entity.StudentBean;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

public class StudentListActivity extends BaseRequestActivity {

	private ImageView back;
	private ListView list;
	private ArrayList<StudentBean> datas;
	private StudentListAdapter adapter;
	private String ClassName;
	private TextView count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ClassName = getIntent().getStringExtra("classname");
		setContentView(R.layout.studentlist);
		back = (ImageView) findViewById(R.id.back);
		count = (TextView) findViewById(R.id.count);
		list = (ListView) findViewById(R.id.studentlist);
		back.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		datas = new ArrayList<StudentBean>();
		adapter = new StudentListAdapter(this, datas);
		list.setAdapter(adapter);
		sendRequest(sharePre.getString(S.LOGIN_USERNAME, "none"),
				sharePre.getString(S.LOGIN_PWD, ""), ClassName);
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
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiStudentList.queryStudentlist(params[0], params[1], params[2]);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<StudentBean> temps = (List<StudentBean>) result;
		if (temps.size() > 0) {
			System.out.println("add");
			datas.addAll(temps);
			adapter.notifyDataSetChanged();
			count.setText("人数:" + datas.size() + " ");
		}

	}

}
