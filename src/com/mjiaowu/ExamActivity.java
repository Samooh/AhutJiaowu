package com.mjiaowu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.ExamlListAdapter;
import com.ahutjw.api.ApiExam;
import com.ahutjw.app.entity.ExamArrangementBean;
import com.ahutjw.app.entity.ExamListBean;
import com.ahutjw.app.entity.ExamStuBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

/**
 * 
 * 考试信息
 * 
 */
public class ExamActivity extends BaseRequestActivity {
	// 数据库管理
	private DbManager dbM;

	private ListView lstView;
	private ExamlListAdapter adapter;
	private List<ExamListBean> datas;
	// 登录信息
	private String loginType, loginUser, loginPwd;
	private ImageView imgBack;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_list);
		// 获取配置内容
		loginType = sharePre.getString(S.LOGIN_TYPE, "");
		loginUser = sharePre.getString(S.LOGIN_USERNAME, "");
		loginPwd = sharePre.getString(S.LOGIN_PWD, "");
		String[] names = new String[4];
		if (loginType.equals("教师")) {
			names = new String[] { "考试科目", "考试时间", "考试地点", "监考老师" };
		} else if (loginType.equals("本科生")) {
			names = new String[] { "考试科目", "考试时间", "考试地点", "座位号" };
		}
		imgBack = (ImageView) findViewById(R.id.back);
		// 实例化
		lstView = (ListView) findViewById(R.id.list);
		datas = new ArrayList<ExamListBean>();
		adapter = new ExamlListAdapter(this, names, datas);
		lstView.setAdapter(adapter);
		// 数据库管理器
		dbM = new DbManager(this);

		// 请求数据
		if (sharePre.getBoolean(S.LOGIN_IS, false)) {
			sendRequest(loginType, loginUser, loginPwd);
		}
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
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
	public void onDestroy() {
		super.onDestroy();
		dbM.close();
		// 启动服务
		startService(new Intent("ahut.exam"));
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "数据请求中...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiExam.queryExam(params[0], params[1], params[2]);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		List<ExamListBean> temps = new ArrayList<ExamListBean>();
		if (loginType.equals("教师")) {
			@SuppressWarnings("unchecked")
			List<ExamArrangementBean> beans = (List<ExamArrangementBean>) result;
			for (int i = 0; i < beans.size(); i++) {
				ExamArrangementBean bean = beans.get(i);
				ExamListBean temp = new ExamListBean();
				temp.setTitle1(bean.getLessonName());
				temp.setTitle2(bean.getExamDate());
				temp.setTitle3(bean.getClassroom());
				temp.setTitle4(bean.getOverseeTeacher1() + " "
						+ bean.getOverseeTeacher2() + " "
						+ bean.getOverseeTeacher3() + " "
						+ bean.getOverseeTeacher4());
				temps.add(temp);
			}
		} else if (loginType.equals("本科生")) {
			@SuppressWarnings("unchecked")
			List<ExamStuBean> beans = (List<ExamStuBean>) result;
			for (int i = 0; i < beans.size(); i++) {
				ExamStuBean bean = beans.get(i);
				ExamListBean temp = new ExamListBean();
				temp.setTitle1(bean.getLessonName());
				temp.setTitle2(bean.getExamDate());
				temp.setTitle3(bean.getExamPlace());
				temp.setTitle4(bean.getSeatNo());
				temps.add(temp);
			}
		}
		// 缓存数据库
		if (temps.size() > 0) {
			dbM.deleteAll(DbManager.exam.TABLE_NAME);
			for (int i = 0; i < temps.size(); i++) {
				ExamListBean bean = temps.get(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DbManager.exam.COLUMN_ID, (i + 1) + "");
				map.put(DbManager.exam.COLUMN_TITLE1, bean.getTitle1());
				map.put(DbManager.exam.COLUMN_TITLE2, bean.getTitle2());
				map.put(DbManager.exam.COLUMN_TITLE3, bean.getTitle3());
				map.put(DbManager.exam.COLUMN_TITLE4, bean.getTitle4());
				// 添加数据库
				dbM.insert(DbManager.exam.TABLE_NAME, DbManager.exam.columns,
						map);
			}
		} else {// 从数据库中加载
			ArrayList<HashMap<String, String>> datasLst = dbM.query(
					DbManager.exam.TABLE_NAME, DbManager.exam.columns);
			for (int i = 0; i < datasLst.size(); i++) {
				HashMap<String, String> map = datasLst.get(i);
				ExamListBean temp = new ExamListBean();
				temp.setTitle1(map.get(DbManager.exam.COLUMN_TITLE1));
				temp.setTitle2(map.get(DbManager.exam.COLUMN_TITLE2));
				temp.setTitle3(map.get(DbManager.exam.COLUMN_TITLE3));
				temp.setTitle4(map.get(DbManager.exam.COLUMN_TITLE4));
				temps.add(temp);
			}
		}
		datas.addAll(temps);
		adapter.notifyDataSetChanged();
		return;
	}
}