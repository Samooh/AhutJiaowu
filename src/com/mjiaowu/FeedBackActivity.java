package com.mjiaowu;

import java.net.URLEncoder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiBaseHttp;
import com.ahutjw.entity.base.BaseActivity;
import com.ahutjw.utils.FlippingLoadingDialog;

public class FeedBackActivity extends BaseActivity {
	private ImageView mBack;
	private ImageView mPublish;
	private EditText mContent;
	private TextView mCount;
	private String result = null;
	private FlippingLoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedpublish);

		findViewById();
		mPublish.setOnClickListener(new OnClickListener() {

			private String url;

			public void onClick(View v) {
				if (mContent.getText().toString().trim().length() == 0) {
					System.out.println("检测有无输入内容");
					showCustomToast("您尚未输入内容,请输入后重试");
				} else {
					try {
						System.out.println("正在提交");
						dialog = new FlippingLoadingDialog(
								FeedBackActivity.this, "正在提交，请稍后...");
						dialog.show();
						url = "http://211.70.149.139:8990/feedBack.asmx/FeedBack?feedback="
								+ URLEncoder.encode(mContent.getText()
										.toString(), "UTF-8");
						System.out.println("url=" + url);
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									result = ApiBaseHttp.get(url);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									result = "<boolean>false</boolean>";
									mHandler.post(runnableUi);
									mHandler.sendEmptyMessage(1);

								}
								mHandler.post(runnableUi);
								mHandler.sendEmptyMessage(1);
							}
						}).start();
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}

				}
			}
		});
		mContent.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {
				int number = s.length();
				mCount.setText(String.valueOf(number));
				try {
					selectionStart = mContent.getSelectionStart();
					selectionEnd = mCount.getSelectionEnd();
					if (temp.length() > 60) {
						s.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionEnd;
						mContent.setText(s);
						mContent.setSelection(tempSelection);
					}
				} catch (Exception e) {
					showCustomToast("您输入的内容超过限制范围，请删减");
				}

			}
		});
		mBack.setOnClickListener(new OnClickListener() {

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

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				dialog.dismiss();
			}
		}
	};
	Runnable runnableUi = new Runnable() {

		@Override
		public void run() {
			if (result.contains("true")) {
				showCustomToast("提交成功");
				FeedBackActivity.this.finish();
			} else
				showCustomToast("很抱歉，提交失败");
			dialog.dismiss();
		}
	};

	private void findViewById() {
		mBack = (ImageView) findViewById(R.id.newsfeedpublish_back);
		mPublish = (ImageView) findViewById(R.id.newsfeedpublish_publish);
		mContent = (EditText) findViewById(R.id.newsfeedpublish_content);
		mCount = (TextView) findViewById(R.id.newsfeedpublish_count);
	}

}
