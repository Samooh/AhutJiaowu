package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.WebClassDetailAdapter;
import com.ahutjw.api.ApiWebClass;
import com.ahutjw.app.entity.WebClassDetailBean;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

/**
 * 
 * 网上选课---详细
 * 
 */
public class WebClassDetailActivity extends BaseRequestActivity {

	// 请求数据类型--0（默认值） 初始化数据； 1 选定按钮操作 ；2 删除按钮操作
	private int type = 0;

	private ListView lstView;
	private WebClassDetailAdapter adapter;
	private List<WebClassDetailBean> datas;
	// 登录信息
	private String loginUser, loginPwd;
	// 课程参数
	private String lesCode, lesId;
	private ImageView imgBack;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webclass_detail);
		// 获取配置内容
		imgBack = (ImageView) findViewById(R.id.back);
		// loginType = sharePre.getString(S.LOGIN_TYPE, "");
		loginUser = sharePre.getString(S.LOGIN_USERNAME, "");
		loginPwd = sharePre.getString(S.LOGIN_PWD, "");
		// 课程参数
		lesCode = getIntent().getStringExtra("lesCode");
		// 实例化
		lstView = (ListView) findViewById(R.id.list);
		datas = new ArrayList<WebClassDetailBean>();
		adapter = new WebClassDetailAdapter(this, datas);
		lstView.setAdapter(adapter);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 选中radio
				adapter.setSel(position);
			}
		});
		// 请求数据
		sendRequest();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
		String txt = "";
		if (type == 0) {
			txt = "数据请求中...";
		} else if (type == 1) {
			txt = "正在提交...";
		} else {
			txt = "正在删除...";
		}
		return txt;
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		if (type == 0) {
			return ApiWebClass.queryPickLessonDetail(loginUser, loginPwd,
					lesCode);
		} else if (type == 1) {
			return ApiWebClass.submitPickLesson(loginUser, loginPwd, lesCode,
					lesId);
		} else {
			return ApiWebClass.deletePickLesson(loginUser, loginPwd, lesCode,
					lesId);
		}
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		if (type == 0) {
			@SuppressWarnings("unchecked")
			List<WebClassDetailBean> beans = (List<WebClassDetailBean>) result;
			datas.addAll(beans);
			adapter.notifyDataSetChanged();
		} else {
			showShortToast(result + "");
			WebClassDetailActivity.this.finish();
		}
	}

	// button click event
	public void btnClick(View view) {
		// 选择的课程id
		lesId = adapter.getSelItem().getLessonId();
		//
		int tag = Integer.parseInt(view.getTag().toString());
		switch (tag) {
		case 0:// 选定
			type = 1;
			sendRequest();
			break;
		case 1:// 删除
			/*
			 * confirm("提示", "确定要退选此门课程么？", new OnButtonClickCallback() {
			 * 
			 * @Override public void onCallback(View v) { type = 2;
			 * sendRequest(); } });
			 */
			break;

		default:
			break;
		}
	}
}