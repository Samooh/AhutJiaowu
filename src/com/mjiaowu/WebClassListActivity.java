package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.WebClassListAdapter;
import com.ahutjw.api.ApiWebClass;
import com.ahutjw.app.entity.WebClassListBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

/**
 * 
 * 网上选课
 * 
 */
public class WebClassListActivity extends BaseRequestActivity {
	// 数据库管理
	private DbManager dbM;

	private ListView lstView;
	private WebClassListAdapter adapter;
	private List<WebClassListBean> datas;
	private ImageView imgBack;
	// 登录信息
	private String loginUser, loginPwd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webclass_list);
		System.out.println("webClassListActivity start!");
		// 获取配置内容
		imgBack = (ImageView) findViewById(R.id.back);
		// loginType = sharePre.getString(S.LOGIN_TYPE, "");
		loginUser = sharePre.getString(S.LOGIN_USERNAME, "");
		loginPwd = sharePre.getString(S.LOGIN_PWD, "");

		// 实例化
		lstView = (ListView) findViewById(R.id.list);
		datas = new ArrayList<WebClassListBean>();
		adapter = new WebClassListAdapter(this, datas);
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
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				WebClassListBean bean = adapter.getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("lesCode", bean.getLessonCode());
				bundle.putString("lesName", bean.getLessonName());
				openActivity(WebClassDetailActivity.class, bundle);
			}
		});
		// 数据库管理器
		dbM = new DbManager(this);

	}


	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 请求数据
		if (!sharePre.getBoolean(S.LOGIN_IS, false)) {
			Toast.makeText(getApplicationContext(), "请登录后再进行选课",
					Toast.LENGTH_SHORT).show();
		} else if (sharePre.getString(S.LOGIN_TYPE, "").equals("教师")) {
			Toast.makeText(getApplicationContext(), "无法进行选课",
					Toast.LENGTH_SHORT).show();
		} else {
			sendRequest(loginUser, loginPwd);
		}
		JPushInterface.onResume(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbM.close();
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "数据请求中...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiWebClass.queryPickLessonList(params[0], params[1]);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(),
				R.raw.tips);
		mPlayer.start();
		@SuppressWarnings("unchecked")
		List<WebClassListBean> beans = (List<WebClassListBean>) result;
		// 缓存数据库
		// if (temps.size() > 0) {
		// dbM.deleteAll(DbManager.exam.TABLE_NAME);
		// for (int i = 0; i < temps.size(); i++) {
		// ExamListBean bean = temps.get(i);
		// HashMap<String, String> map = new HashMap<String, String>();
		// map.put(DbManager.exam.COLUMN_ID, (i + 1) + "");
		// map.put(DbManager.exam.COLUMN_TITLE1, bean.getTitle1());
		// map.put(DbManager.exam.COLUMN_TITLE2, bean.getTitle2());
		// map.put(DbManager.exam.COLUMN_TITLE3, bean.getTitle3());
		// map.put(DbManager.exam.COLUMN_TITLE4, bean.getTitle4());
		// // 添加数据库
		// dbM.insert(DbManager.exam.TABLE_NAME, DbManager.exam.columns,
		// map);
		// }
		// } else {// 从数据库中加载
		// ArrayList<HashMap<String, String>> datasLst = dbM.query(
		// DbManager.exam.TABLE_NAME, DbManager.exam.columns);
		// for (int i = 0; i < datasLst.size(); i++) {
		// HashMap<String, String> map = datasLst.get(i);
		// ExamListBean temp = new ExamListBean();
		// temp.setTitle1(map.get(DbManager.exam.COLUMN_TITLE1));
		// temp.setTitle2(map.get(DbManager.exam.COLUMN_TITLE2));
		// temp.setTitle3(map.get(DbManager.exam.COLUMN_TITLE3));
		// temp.setTitle4(map.get(DbManager.exam.COLUMN_TITLE4));
		// temps.add(temp);
		// }
		// }
		datas.clear();
		datas.addAll(beans);
		adapter.notifyDataSetChanged();
		Toast.makeText(getApplicationContext(), "很抱歉，现在不是选课时间",
				Toast.LENGTH_SHORT).show();
		return;
	}
}