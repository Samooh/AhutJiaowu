package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.ImageAndTextListAdapter;
import com.ahutjw.api.ApiArticle;
import com.ahutjw.app.entity.ArticleBean;
import com.ahutjw.entity.base.BaseRequestActivity;

public class ArticleListActivity extends BaseRequestActivity {
	private ListView listview;
	private ImageView back;
	private List<ArticleBean> datas;
	private ImageAndTextListAdapter adapter;
	private int page = 1;
	private boolean is_split = false;
	private int position;
	private Bitmap newBitimage;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.articlelist);
		listview = (ListView) findViewById(R.id.articlelistview);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		datas = new ArrayList<ArticleBean>();
		sendRequest(page + "");
		adapter = new ImageAndTextListAdapter(this, datas, listview);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString("ID", datas.get(position).getId());
				bundle.putString("Title", datas.get(position).getTitle());
				bundle.putString("Date", datas.get(position).getDate());
				bundle.putString("Content", datas.get(position).getContent());
				bundle.putString("imageUrl", datas.get(position).getImageUrl());
				bundle.putString("Abstract", datas.get(position).getAbstract());
				openActivity(ArticleDetailActivity.class, bundle);
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
					sendRequest(page + "");

				}
			}

		});

	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (newBitimage != null) {
					System.out.println(position + ">>>>>>>>>>"
							+ listview.getCount());
					ImageView v = ((ImageView) listview.getChildAt(position)
							.findViewById(R.id.articleimage));
					if (v != null) {
						v.setImageBitmap(newBitimage);
					}
					// newBitimage = null;
				}
			}
			super.handleMessage(msg);
		}
	};

	/*
	 * private Bitmap getPicture(String pictureurl) throws IOException { URL url
	 * = new URL(pictureurl); HttpURLConnection conn = (HttpURLConnection)
	 * url.openConnection(); conn.setRequestMethod("GET");
	 * conn.setConnectTimeout(5 * 1000); InputStream in = conn.getInputStream();
	 * Bitmap bm = BitmapFactory.decodeStream(in); // 保存本地图片 in.close();
	 * System.out.println(">>>>>>>图片获取成功"); return bm; }
	 */

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "正在请求...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiArticle.queryArticlelist(params[0]);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<ArticleBean> temps = (List<ArticleBean>) result;
		if (temps != null) {
			page++;
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
