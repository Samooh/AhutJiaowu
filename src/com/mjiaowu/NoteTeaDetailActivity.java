package com.mjiaowu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiBaseHttp;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

public class NoteTeaDetailActivity extends BaseRequestActivity {
	private TextView notecontent, notedate, savereply, myreply;
	private EditText notereply;
	private View backImageView;
	private String content, date, reply, ID, replydate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noteteadetail);

		ID = getIntent().getStringExtra("ID");
		content = getIntent().getStringExtra("Note");
		date = getIntent().getStringExtra("noteDate");
		reply = getIntent().getStringExtra("Reply");
		replydate = getIntent().getStringExtra("replyDate");

		notecontent = (TextView) findViewById(R.id.notecontent);
		notedate = (TextView) findViewById(R.id.notedate);
		savereply = (TextView) findViewById(R.id.savereply);
		myreply = (TextView) findViewById(R.id.myreply);
		notereply = (EditText) findViewById(R.id.notereply);

		notecontent.setText(content);
		notedate.setText("留言:(" + date + ")");
		notereply.setText(reply);
		if (!replydate.equals("")) {
			myreply.setText("我的回复:(" + replydate + ")");
		}
		if (sharePre.getString(S.LOGIN_TYPE, "").equals("本科生")) {
			savereply.setText("");
			notereply.setFocusable(false);
		}
		backImageView = (ImageView) findViewById(R.id.back);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		savereply.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (sharePre.getString(S.LOGIN_TYPE, "none").equals("本科生")) {
					return;
				}
				reply = notereply.getText().toString();
				if (reply == null || reply.equals("")) {
					showCustomToast("请输入回复内容");
					return;
				} else {
					Runnable runnable = new Runnable() {

						@Override
						public void run() { // TODO Auto-generated method stub
							String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/saveReply";
							try {
								String result = ApiBaseHttp.DoPost(
										url,
										new String[] { "teaNum", "password",
												"Reply", "ID" },
										new String[] {
												sharePre.getString(
														S.LOGIN_USERNAME, ""),
												sharePre.getString(S.LOGIN_PWD,
														""), reply, ID });
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
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				showCustomToast("回复保存成功");
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub

	}

}
