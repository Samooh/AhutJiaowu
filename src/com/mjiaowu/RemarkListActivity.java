package com.mjiaowu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.RemarkListAdapter;
import com.ahutjw.api.ApiRemark;
import com.ahutjw.app.entity.RemarkLessonInfoBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

/**
 * 
 * 教学评价---待评价的课程列表
 * 
 * @author
 * 
 */
public class RemarkListActivity extends BaseRequestActivity {
	// 数据库管理
	private DbManager dbM;

	private ListView lstView;
	private RemarkListAdapter adapter;
	private List<RemarkLessonInfoBean> datas;
	// 登录信息
	private String loginUser, loginPwd;
	// 请求类型 0加载列表（默认） 1 提交
	private int requestType = 0;
	private ImageView imgBack;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remark_list);
		// 获取配置内容
		// loginType = sharePre.getString(S.LOGIN_TYPE, "");
		loginUser = sharePre.getString(S.LOGIN_USERNAME, "");
		loginPwd = sharePre.getString(S.LOGIN_PWD, "");
		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		// 实例化
		lstView = (ListView) findViewById(R.id.list);
		datas = new ArrayList<RemarkLessonInfoBean>();
		adapter = new RemarkListAdapter(this, datas);
		lstView.setAdapter(adapter);
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				System.out.println("PJDJ=" + datas.get(position).PJDJ);
				if (datas.get(position).PJDJ.equals("优")
						|| datas.get(position).PJDJ.equals("良")
						|| datas.get(position).PJDJ.equals("中")
						|| datas.get(position).PJDJ.equals("及格")
						|| datas.get(position).PJDJ.equals("不及格")) {
					showShortToast("此课程您已评价过！");
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putString("title", datas.get(position).KCMC);
				bundle.putString("lesId", datas.get(position).XKKH);
				bundle.putString("pjdj", datas.get(position).PJDJ);
				openActivity(RemarkActivity.class, bundle);
			}
		});
		// 数据库管理器
		dbM = new DbManager(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 请求
		if (sharePre.getBoolean(S.LOGIN_IS, false)) {
			sendRequest(loginUser, loginPwd);
		}
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		dbM.close();
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		if (requestType == 1) {
			return "提交中...";
		}
		datas.clear();
		return "数据请求中...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		try {
			if (requestType == 1) {
				return ApiRemark.submiRemarkLession(loginUser, loginPwd);
			}
			return ApiRemark.queryRemarkLessionInfo(params[0], params[1]);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(),
				R.raw.tips);
		mPlayer.start();
		if (requestType == 1) {
			showShortToast(result + "");
			return;
		}
		//
		@SuppressWarnings("unchecked")
		List<RemarkLessonInfoBean> temps = (List<RemarkLessonInfoBean>) result;
		// 缓存数据库
		if (temps.size() > 0) {
			// --只缓存第一页
			System.out.println("存储缓存……");
			dbM.deleteAll(DbManager.remark.TABLE_NAME);
			for (int i = 0; i < temps.size(); i++) {
				RemarkLessonInfoBean bean = temps.get(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DbManager.remark.COLUMN_ID, (i + 1) + "");
				map.put(DbManager.remark.COLUMN_TITLE, bean.KCMC + "("
						+ bean.JSXM + ")");
				map.put(DbManager.remark.COLUMN_STATE, bean.PJDJ);
				map.put(DbManager.remark.COLUMN_URL, bean.XKKH);
				// 添加数据库
				dbM.insert(DbManager.remark.TABLE_NAME,
						DbManager.remark.columns, map);
			}
		} else {// 从数据库中加载
			System.out.println("读取缓存……");
			ArrayList<HashMap<String, String>> datasLst = dbM.query(
					DbManager.remark.TABLE_NAME, DbManager.remark.columns);
			for (int i = 0; i < datasLst.size(); i++) {
				HashMap<String, String> map = datasLst.get(i);
				RemarkLessonInfoBean temp = new RemarkLessonInfoBean();
				temp.KCMC = map.get(DbManager.remark.COLUMN_TITLE);
				temp.XKKH = map.get(DbManager.remark.COLUMN_URL);
				temp.PJDJ = map.get(DbManager.remark.COLUMN_STATE);
				temps.add(temp);
			}
		}
		datas.addAll(temps);
		adapter.notifyDataSetChanged();
	}
}