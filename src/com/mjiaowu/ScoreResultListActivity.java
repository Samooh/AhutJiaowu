package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.ahutjw.adapter.ScoreResultListAdapter;
import com.ahutjw.api.ApiScore;
import com.ahutjw.app.entity.ScoreResultBean;
import com.ahutjw.entity.base.BaseRequestActivity;

/**
 * 成绩查询--结果列表
 * 
 */
public class ScoreResultListActivity extends BaseRequestActivity {

	private ListView lstView;
	private ScoreResultListAdapter adapter;
	private List<ScoreResultBean> datas;
	private ImageView imgBack;

	// 参数属性
	private String xh, sfzh, xn, xq;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_result_list);
		// 实例化
		imgBack=(ImageView)findViewById(R.id.back);
		lstView = (ListView) findViewById(R.id.list);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		datas = new ArrayList<ScoreResultBean>();
		adapter = new ScoreResultListAdapter(this, datas);
		lstView.setAdapter(adapter);
		// 参数获取
		Bundle b = getIntent().getExtras();
		xh = b.getString("xh");
		sfzh = b.getString("sfzh");
		xn = b.getString("xn");
		xq = b.getString("xq");

		// 加载数据
		sendRequest(xh, sfzh, xn, xq);
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "数据加载中...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiScore.queryScoreResultList(params[0], params[1], params[2],
				params[3]);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<ScoreResultBean> tempBeans = (List<ScoreResultBean>) result;
		// 如果登陆成功，保存信息
		// 如果“记住我”
		if (getIntent().getBooleanExtra("state", false)) {
			sharePre.setString("etXh", xh);
			sharePre.setString("etSfzh", sfzh);
		} else {
			sharePre.setString("etXh", "");
			sharePre.setString("etSfzh", "");
		}
		// 刷新
		datas.addAll(tempBeans);
		adapter.notifyDataSetChanged();
	}
}