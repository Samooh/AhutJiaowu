package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.CommentListAdapter;
import com.ahutjw.api.ApiComment;
import com.ahutjw.app.entity.CommentBean;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

public class CommentListActivity extends BaseRequestActivity {

	private ListView listview;
	private List<CommentBean> datas;
	private CommentListAdapter adapter;
	private ImageView back;
	private int page = 1;
	private String articleId;

	private boolean is_split = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentlist);
		articleId = getIntent().getStringExtra("articleId");
		listview = (ListView) findViewById(R.id.commentlistview);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		listview.setOnScrollListener(new ListView.OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
					is_split = true;
					System.out.println("is true");
					return;
				}

				is_split = false;
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (is_split
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// Toast.makeText(getActivity(), "分页中.....", 1000).show();

					// 开启异步任务
					System.out.println("start task" + page);
					page++;
					sendRequest(page + "");

				}
			}

		});
		datas = new ArrayList<CommentBean>();
		adapter = new CommentListAdapter(this, datas, sharePre.getString(
				S.LOGIN_USERNAME, ""), sharePre.getString(S.LOGIN_PWD, ""));
		listview.setAdapter(adapter);
		sendRequest(page + "");
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "正在请求...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiComment.queryArticleCommentList(articleId, params[0],
				sharePre.getString(S.LOGIN_USERNAME, ""));
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
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<CommentBean> temps = (List<CommentBean>) result;
		if (temps != null) {
			datas.addAll(temps);
			adapter.notifyDataSetChanged();
		}

	}

}
