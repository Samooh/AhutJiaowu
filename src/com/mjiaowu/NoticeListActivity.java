package com.mjiaowu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.NoticeListAdapter;
import com.ahutjw.api.ApiNotice;
import com.ahutjw.app.entity.NoticeListBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.SelectPicPopupWindow;
import com.mjiaowu.RefreshableView.PullToRefreshListener;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class NoticeListActivity extends BaseRequestActivity {

	RefreshableView refreshableView;
	@SuppressWarnings("unused")
	private String current_page;

	@SuppressWarnings("unused")
	private InputMethodManager imm;

	private DbManager dbM;

	private ListView lstView;
	private NoticeListAdapter adapter;
	private List<NoticeListBean> datas;
	//
	private List<NoticeListBean> datas1;
	// private Button footerBtn;
	//
	private int page = 1;

	// private RefreshableView Relistview;
	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow; // 弹出框
	private ImageView backImageView;
	private boolean is_split = false;
	private boolean refresh = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notice_list);
		refreshableView = (RefreshableView) findViewById(R.id.trefreshable_view);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				// page = 1;
				// sendRequest(page + "");

				refresh = true;
				page = 1;
				sendRequest(page + "");
				refreshableView.finishRefreshing();
			}
		}, 0);
		lstView = (ListView) findViewById(R.id.tlist_view);

		lstView.setOnScrollListener(new ListView.OnScrollListener() {

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
		backImageView = (ImageView) findViewById(R.id.tback);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		datas = new ArrayList<NoticeListBean>();
		datas1 = new ArrayList<NoticeListBean>();
		adapter = new NoticeListAdapter(this, datas);
		// lstView.addFooterView(footerBtn);
		lstView.setAdapter(adapter);
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString("id", datas.get(position).getId());
				openActivity(NoticeDetailActivity.class, bundle);
			}
		});
		dbM = new DbManager(this);
		sendRequest(page + "");

	}

	public void Refresh() {
		// datas.clear();
		// page = 1;
		// sendRequest(page + "");
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

		return "正在请求中...";
	}

	// 请求通知
	@Override
	public Object onReqestDoing(String... params) {

		return ApiNotice.queryNoticeList(params[0]);
	}

	// 请求完成
	@Override
	public void onReqestFinish(Object result) {
		@SuppressWarnings("unchecked")
		List<NoticeListBean> temps = (List<NoticeListBean>) result;
		if (temps.size() > 0) {
			lstView.setVisibility(View.VISIBLE);
			// footerBtn.setVisibility(View.VISIBLE);
			System.out.println("footerBtn");
			if (page == 1) {
				dbM.deleteAll(DbManager.notice.TABLE_NAME);
				System.out.println("正在缓存第一页");
				for (int i = 0; i < temps.size(); i++) {
					NoticeListBean bean = temps.get(i);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(DbManager.notice.COLUMN_ID, (i + 1) + "");
					map.put(DbManager.notice.COLUMN_TITLE, bean.getTitle());
					map.put(DbManager.notice.COLUMN_TIME, bean.getDtmPublish());
					map.put(DbManager.notice.COLUMN_URL, bean.getId());
					dbM.insert(DbManager.notice.TABLE_NAME,
							DbManager.notice.columns, map);
				}
				System.out.println("缓存成功");
				datas1.clear();
				datas1.addAll(temps);
			}
		} else {
			System.out.println("加载第一页");
			ArrayList<HashMap<String, String>> datasLst = dbM.query(
					DbManager.notice.TABLE_NAME, DbManager.notice.columns);
			System.out.println("Length=" + datasLst.size());
			for (int i = 0; i < datasLst.size(); i++) {
				HashMap<String, String> map = datasLst.get(i);
				NoticeListBean temp = new NoticeListBean();
				temp.setTitle(map.get(DbManager.notice.COLUMN_TITLE));
				System.out.println(map.get(DbManager.notice.COLUMN_TITLE));
				temp.setDtmPublish(map.get(DbManager.notice.COLUMN_TIME));
				temp.setId(map.get(DbManager.notice.COLUMN_URL));
				temps.add(temp);
			}
			System.out.println("Length temp=" + temps.size());
		}
		if (refresh) {
			datas.clear();
			datas.addAll(temps);
			adapter.notifyDataSetChanged();
			refresh = false;
		} else {
			datas.addAll(temps);
			adapter.notifyDataSetChanged();
		}
		// datas.addAll(temps);
		// adapter.notifyDataSetChanged();
		// 加载声音
		// MediaPlayer mPlayer=MediaPlayer.create(this, R.raw.refresh);
		// mPlayer.start();
	}

}