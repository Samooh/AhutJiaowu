package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.NoteListAdapter;
import com.ahutjw.api.ApiNote;
import com.ahutjw.app.entity.NoteBean;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

public class NoteTeaListActivity extends BaseRequestActivity {
	private ListView listview;
	private ImageView back;
	private ArrayList<NoteBean> datas;
	private NoteListAdapter adapter;
	private String way;
	// private String courseName;
	// private String teaNum;
	private int page = 1;
	private String courseName, teaNum;
	private boolean is_split = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notetealist);
		way = getIntent().getStringExtra("way");
		courseName = getIntent().getStringExtra("courseName");
		teaNum = getIntent().getStringExtra("teaNum");

		listview = (ListView) findViewById(R.id.notetealistview);
		// courseName = getIntent().getStringExtra("courseName");
		// teaNum = getIntent().getStringExtra("teaNum");
		// way=1 获取老师所有的留言列表
		// way=2 获取老师本课程的留言列表
		// way=3 获取老师所有未查看留言
		// way=4获取老师该课程未查看留言

		// way=5获取学生所有留言列表
		// way=6获取学生该课程所有留言
		// way=7获取学生所有留言回复未查看
		// way=8获取学生该课程留言回复未查看

		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		datas = new ArrayList<NoteBean>();
		adapter = new NoteListAdapter(this, datas);
		listview.setAdapter(adapter);
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
					System.out.println("start task");
					page++;
					sendRequest(page + "");

				}
			}

		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (way.equals("1") || way.equals("2") || way.equals("3")
						|| way.equals("4")) {
					Bundle bundle = new Bundle();
					bundle.putString("ID", datas.get(position).getID());
					bundle.putString("Note", datas.get(position).getNote());
					bundle.putString("noteDate", datas.get(position)
							.getNoteDate());
					bundle.putString("Reply", datas.get(position).getReply());
					bundle.putString("replyDate", datas.get(position)
							.getReplyDate());
					openActivity(NoteTeaDetailActivity.class, bundle);
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("ID", datas.get(position).getID());
					bundle.putString("Note", datas.get(position).getNote());
					bundle.putString("noteDate", datas.get(position)
							.getNoteDate());
					bundle.putString("Reply", datas.get(position).getReply());
					bundle.putString("replyDate", datas.get(position)
							.getReplyDate());
					openActivity(NoteTeaDetailActivity.class, bundle);
				}

			}
		});
		sendRequest();
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "正在请求...";
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
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		if (way.equals("1")) {
			return ApiNote.queryTeaNoteAlllist(
					sharePre.getString(S.LOGIN_USERNAME, ""),
					sharePre.getString(S.LOGIN_PWD, ""), page + "");
		} else if (way.equals("2")) {
			return ApiNote.queryTeaNotelist(
					sharePre.getString(S.LOGIN_USERNAME, ""),
					sharePre.getString(S.LOGIN_PWD, ""), courseName, page + "");
		} else if (way.equals("5")) {
			return ApiNote.queryStuNoteAlllist(
					sharePre.getString(S.LOGIN_USERNAME, ""),
					sharePre.getString(S.LOGIN_PWD, ""), page + "");
		} else if (way.equals("6")) {
			return ApiNote.queryStuNotelist(
					sharePre.getString(S.LOGIN_USERNAME, ""),
					sharePre.getString(S.LOGIN_PWD, ""), courseName, teaNum,
					page + "");
		}
		return null;

	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<NoteBean> temps = (List<NoteBean>) result;
		if (temps != null) {
			datas.addAll(temps);
			adapter.notifyDataSetChanged();
		}
	}

}
