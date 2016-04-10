package com.mjiaowu;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiBaseHttp;
import com.ahutjw.api.ApiClassNotice;
import com.ahutjw.app.entity.ClassNoticeBean;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

public class CourseDetailTeaActivity extends BaseRequestActivity {

	private String lessonname;
	// private String lessonid;
	private TextView LessonName;
	private TextView getmore;
	private ImageView back;
	private EditText editdetail;
	private String detail, teaNum;
	private TextView addnotice;
	private ArrayList<ClassNoticeBean> datas;
	private TextView classnotice;
	private TextView noticedate;
	private Button getnote;
	private TextView studentlist;
	private String weektime;
	private String time;
	private String weekcount;
	private String place;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classdetail_tea);

		LessonName = (TextView) findViewById(R.id.lessonname);
		getmore = (TextView) findViewById(R.id.getmore);
		addnotice = (TextView) findViewById(R.id.addnotice);
		editdetail = (EditText) findViewById(R.id.noticedetail);
		classnotice = (TextView) findViewById(R.id.lessonnotice);
		noticedate = (TextView) findViewById(R.id.noticedate);
		studentlist = (TextView) findViewById(R.id.studentlist);
		getnote = (Button) findViewById(R.id.getnote);

		lessonname = getIntent().getStringExtra("lessonname");
		weektime = getIntent().getStringExtra("weektime");
		time = getIntent().getStringExtra("time");
		weekcount = getIntent().getStringExtra("weekcount");
		place = getIntent().getStringExtra("place");

		LessonName.setText(lessonname);
		teaNum = sharePre.getString(S.LOGIN_USERNAME, "");
		back = (ImageView) findViewById(R.id.back);
		datas = new ArrayList<ClassNoticeBean>();
		sendRequest();
		if (weektime.equals("1")) {
			weektime = "周一";
		} else if (weektime.equals("2")) {
			weektime = "周二";
		} else if (weektime.equals("3")) {
			weektime = "周三";
		} else if (weektime.equals("4")) {
			weektime = "周四";
		} else if (weektime.equals("5")) {
			weektime = "周五";
		} else if (weektime.equals("6")) {
			weektime = "周六";
		} else if (weektime.equals("7")) {
			weektime = "周七";
		}
		if (time.equals("1")) {
			time = "第1,2节";
		} else if (time.equals("3")) {
			time = "第3,4节";
		} else if (time.equals("5")) {
			time = "第5,6节";
		} else if (time.equals("7")) {
			time = "第7,8节";
		} else if (time.equals("9")) {
			time = "第9,10,11节";
		}
		weekcount = weekcount.split("周")[1];
		weekcount = weekcount.replace(")", "");
		weekcount = weekcount.replace("(0", "");
		weekcount = weekcount.replace("(", "");

		studentlist.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				// System.out.println(lessonname +
				// "【"+weektime+time+"{第"+weekcount+"周}/"+place+"】");
				intent.putExtra("classname", lessonname + "【" + weektime + time
						+ "{第" + weekcount + "周}/" + place + "】");// 计算机图形学B【周三第9,10,11节{第4-14周}/东教D205】
				intent.setClass(getApplicationContext(),
						StudentListActivity.class);
				startActivity(intent);
			}

		});
		getnote.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("way", "2");
				intent.putExtra("courseName", lessonname);
				intent.setClass(getApplicationContext(),
						NoteTeaListActivity.class);
				startActivity(intent);
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
		back.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		addnotice.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detail = editdetail.getText().toString();
				if (detail.equals("") || detail == null) {
					showCustomToast("请输入公告内容");
					return;
				} else {
					Runnable runnable = new Runnable() {

						@Override
						public void run() { // TODO Auto-generated method stub
							try {
								String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/addNotice";
								// System.out.println(">>>>>>>>>>>>>" + url);
								// System.out.println("teaNum=" + teaNum +
								// "&courseName="
								// + lessonname + "&noticeTitle=" +
								// "&noticeDetail="
								// + detail);
								String result = ApiBaseHttp
										.DoPost(url,
												new String[] { "teaNum",
														"courseName",
														"noticeTitle",
														"noticeDetail" },
												new String[] { teaNum,
														lessonname, "", detail });
								// System.out.println(result);
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
				showCustomToast("公告添加成功");
				editdetail.setText("");
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
