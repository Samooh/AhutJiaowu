package com.mjiaowu;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiNotice;
import com.ahutjw.app.entity.NoticeDetailBean;
import com.ahutjw.entity.base.BaseRequestActivity;

public class NoticeDetailActivity extends BaseRequestActivity {

	private TextView tvTitle, tvSubtitle, tvSubtitle2, tvContent;
	private ImageView backImageView;
	private ImageView refresh;
	private LinearLayout contentLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_detail);
		// 实例化
		backImageView = (ImageView) findViewById(R.id.back);
		refresh = (ImageView) findViewById(R.id.refresh);
		contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
		tvTitle = (TextView) findViewById(R.id.title);
		tvSubtitle = (TextView) findViewById(R.id.subtitle);
		tvSubtitle2 = (TextView) findViewById(R.id.subtitle2);
		tvContent = (TextView) findViewById(R.id.content);
		tvContent.setText("");
		tvSubtitle.setText("");
		tvSubtitle2.setText("");
		tvTitle.setText("");
		// 请求
		String id = getIntent().getStringExtra("id");
		sendRequest(id);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Bundle bundle = new Bundle();
				openActivity(NoticeListActivity.class, bundle);
				// finish();
			}
		});
	}

	@Override
	public String onReqestPre() {
		// TODO 自动生成的方法存根
		return "数据请求中...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO 自动生成的方法存根
		return ApiNotice.queryNoticeDetail(params[0]);
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
	public void onReqestFinish(Object result) {
		// TODO 自动生成的方法存根
		NoticeDetailBean bean = (NoticeDetailBean) result;
		if (bean.getTitle() == null) {
			contentLayout.setVisibility(View.GONE);
			refresh.setVisibility(View.VISIBLE);
			return;
		}
		tvContent.setText(bean.getContent().trim());
		tvSubtitle.setText(bean.getDtmPublish().trim());
		tvSubtitle2.setText(bean.getSeeNumber().trim());
		tvTitle.setText(bean.getTitle().trim());
	}
}
