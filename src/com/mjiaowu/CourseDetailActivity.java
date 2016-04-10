package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiBaseHttp;
import com.ahutjw.api.ApiClassNotice;
import com.ahutjw.app.entity.ClassNoticeBean;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

public class CourseDetailActivity extends BaseRequestActivity {

	private String lessonname, teachername;
	private String lessonid;
	private TextView LessonName;
	private ImageView back;
	private TextView getmore, noticedate, classnotice, addnote, notecontent;
	private ArrayList<ClassNoticeBean> datas;
	private String teaNum;
	private Button mynote;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classdetail);
		LessonName = (TextView) findViewById(R.id.lessonname);
		getmore = (TextView) findViewById(R.id.getmore);
		noticedate = (TextView) findViewById(R.id.noticedate);
		classnotice = (TextView) findViewById(R.id.lessonnotice);
		mynote = (Button) findViewById(R.id.mynote);

		addnote = (TextView) findViewById(R.id.addnote);
		notecontent = (TextView) findViewById(R.id.notecontent);

		lessonname = getIntent().getStringExtra("lessonname");
		lessonid = getIntent().getStringExtra("lessonid");
		System.out.println(lessonid);
		String[] id = lessonid.split("-");
		System.out.println(">>>>>>>>>>>>>>" + id[4]);
		teaNum = id[4];
		teachername = getIntent().getStringExtra("teachername");
		LessonName.setText(lessonname + "(" + teachername + ")");
		back = (ImageView) findViewById(R.id.back);
		datas = new ArrayList<ClassNoticeBean>();
		sendRequest();
		mynote.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("way", "6");
				intent.putExtra("courseName", lessonname);
				intent.putExtra("teaNum", teaNum);
				intent.setClass(getApplicationContext(),
						NoteTeaListActivity.class);
				startActivity(intent);
			}

		});
		back.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		getmore.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("courseName", lessonname);
				intent.putExtra("teaNum", teaNum);
				intent.setClass(getApplicationContext(),
						ClassNoticeListActivity.class);
				startActivity(intent);
			}

		});
		addnote.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String note = notecontent.getText().toString();
				if (note.equals("") || note == null) {
					showCustomToast("请输入留言内容");
					return;
				} else {
					Runnable runnable = new Runnable() {

						@Override
						public void run() { // TODO Auto-generated method stub
							try {
								String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/addNote";
								System.out.println(url);
								System.out.println("stuNum="
										+ sharePre.getString(S.LOGIN_USERNAME,
												"") + "\npassword="
										+ sharePre.getString(S.LOGIN_PWD, "")
										+ "\ncourseName=" + lessonname
										+ "\nteaNum" + teaNum + "\nNote="
										+ note);
								String result = ApiBaseHttp
										.DoPost(url,
												new String[] { "stuNum",
														"password",
														"courseName", "teaNum",
														"Note" },
												new String[] {
														sharePre.getString(
																S.LOGIN_USERNAME,
																""),
														sharePre.getString(
																S.LOGIN_PWD, ""),
														lessonname, teaNum,
														note });
								System.out.println(result);
								String[] results = result.split(">");
								if (results[2].replace("</int", "").equals("1")) {
									Message message = new Message();
									message.what = 1;
									handler.sendMessage(message);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					new Thread(runnable).start();

				}

			}

		});
	};

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

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				showCustomToast("留言成功");
				notecontent.setText("");
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "正在请求中...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiClassNotice.queryClassNoticeList(lessonname, teaNum);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<ClassNoticeBean> temps = (List<ClassNoticeBean>) result;
		datas.addAll(temps);
		if (datas.size() > 0) {
			classnotice.setText(datas.get(0).getNoticeDetail());
			noticedate.setText("  (" + datas.get(0).getNoticeDate() + ")    ");
		}
	}
}
