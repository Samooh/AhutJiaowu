package com.mjiaowu;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiRemark;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.D;
import com.ahutjw.utils.SharePre;

/**
 * 教学评价
 * 
 * 
 * @author
 * 
 */
public class RemarkActivity extends BaseRequestActivity {

	private EditText goodEditText = null;
	private EditText justSoSoEditText = null;
	private EditText mediumEditText = null;
	private EditText passEditText = null;
	private EditText notPassEditText = null;
	private RadioGroup radioGroup = null;
	private RadioButton rb = null;
	private TextView saveTextView;
	// 课程id
	private String lesId;
	// 评价等级
	private String pjdj = null;
	// 登录信息
	private String loginUser, loginPwd;
	// 参数
	private String score;
	private String advice;
	private ImageView imgBack;
	// 记录评价是否
	SharePre share;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remark);
		share = SharePre.getInstance(this);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		goodEditText = (EditText) findViewById(R.id.good);
		justSoSoEditText = (EditText) findViewById(R.id.justsoso);
		mediumEditText = (EditText) findViewById(R.id.medium);
		passEditText = (EditText) findViewById(R.id.pass);
		notPassEditText = (EditText) findViewById(R.id.notPass);
		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		saveTextView = (TextView) findViewById(R.id.right_btn);
		// 获取配置内容
		saveTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("是否评价：" + share.getBoolean(lesId, false));
				/*
				 * if(share.getBoolean(lesId, false)){ toastShow("此课程您已评价过！");
				 * return; }
				 */
				if (pjdj.equals("-1")) {
					showShortToast("此课程您已评价过！");
					return;
				}
				System.out.println("准备保存");
				if (rb == null) {
					showShortToast("您还没有评价呢！");
					return;
				}
				System.out.println("准备保存1");
				System.out.println("rb=" + rb.getText().toString());
				System.out.println("准备保存2");
				if (rb.getText().equals("优")) {
					score = goodEditText.getText().toString().trim();
					System.out.println("score=" + score);
					if (score.trim() == null || score.trim() == "") {
						showShortToast("您忘了填写课程分数啦！");
						return;
					}
					try {
						if (Integer.parseInt(score) < 90
								|| Integer.parseInt(score) > 95) {
							showShortToast("您输入的分数不在90-95之内，请重新输入！");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						showShortToast("您忘了 填写课程分数啦！");
						return;
					}

					System.out.println("准备保存3");

				} else if (rb.getText().equals("良")) {
					score = justSoSoEditText.getText().toString();
					if (score == null || score.trim() == "") {
						showShortToast("您忘了 填写课程分数啦！");
						return;
					}
					try {
						if (Integer.parseInt(score) < 80
								|| Integer.parseInt(score) > 89) {
							showShortToast("您输入的分数不在80-89之内，请重新输入！");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						showShortToast("您忘了 填写课程分数啦！");
						return;
					}

				} else if (rb.getText().equals("中")) {
					score = mediumEditText.getText().toString();
					if (score == null || score.trim() == "") {
						showShortToast("您忘了 填写课程分数啦！");
						return;
					}
					try {
						if (Integer.parseInt(score) < 70
								|| Integer.parseInt(score) > 79) {
							showShortToast("您输入的分数不在70-79之内，请重新输入！");
							System.out.println("准备保存4");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						showShortToast("您忘了 填写课程分数啦！");
						return;
					}

				} else if (rb.getText().equals("及格")) {
					score = passEditText.getText().toString();
					if (score == null || score.trim() == "") {
						showShortToast("您忘了 填写课程分数啦！");
						return;
					}
					try {
						if (Integer.parseInt(score) < 60
								|| Integer.parseInt(score) > 69) {
							showShortToast("您输入的分数不在60-69之内，请重新输入！");
							System.out.println("准备保存5");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						showShortToast("您忘了 填写课程分数啦！");
						return;
					}

				} else if (rb.getText().equals("不及格")) {
					score = notPassEditText.getText().toString();
					if (score == null || score.trim() == "") {
						showShortToast("您忘了 填写课程分数啦！");
						return;
					}
					try {
						if (Integer.parseInt(score) < 50
								|| Integer.parseInt(score) > 59) {
							showShortToast("您输入的分数不在50-59之内，请重新输入！");
							System.out.println("准备保存6");
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						showShortToast("您忘了填写分数啦！");
						return;
					}

				}
				if (isWifiConnected()
						|| D.getNetworkType(getApplication()).equalsIgnoreCase(
								"2G/3G")) {
					sendRequest();
				} else {
					showShortToast("咦？您的网络好像出了问题...");
				}
				;
			}
		});
		// 课程id
		lesId = getIntent().getStringExtra("lesId");
		// 评价等级
		pjdj = getIntent().getStringExtra("pjdj");
		System.out.println("课程id=" + lesId);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// 获取变更后的选中项的ID
				int radioButtonId = group.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				rb = (RadioButton) findViewById(radioButtonId);
				if (rb.getText().equals("优")) {
					goodEditText.setVisibility(View.VISIBLE);
					justSoSoEditText.setVisibility(View.GONE);
					mediumEditText.setVisibility(View.GONE);
					passEditText.setVisibility(View.GONE);
					notPassEditText.setVisibility(View.GONE);
				} else if (rb.getText().equals("良")) {
					goodEditText.setVisibility(View.GONE);
					justSoSoEditText.setVisibility(View.VISIBLE);
					mediumEditText.setVisibility(View.GONE);
					passEditText.setVisibility(View.GONE);
					notPassEditText.setVisibility(View.GONE);
				} else if (rb.getText().equals("中")) {
					goodEditText.setVisibility(View.GONE);
					justSoSoEditText.setVisibility(View.GONE);
					mediumEditText.setVisibility(View.VISIBLE);
					passEditText.setVisibility(View.GONE);
					notPassEditText.setVisibility(View.GONE);
				} else if (rb.getText().equals("及格")) {
					goodEditText.setVisibility(View.GONE);
					justSoSoEditText.setVisibility(View.GONE);
					mediumEditText.setVisibility(View.GONE);
					passEditText.setVisibility(View.VISIBLE);
					notPassEditText.setVisibility(View.GONE);
				} else if (rb.getText().equals("不及格")) {
					goodEditText.setVisibility(View.GONE);
					justSoSoEditText.setVisibility(View.GONE);
					mediumEditText.setVisibility(View.GONE);
					passEditText.setVisibility(View.GONE);
					notPassEditText.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	public boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.isConnected();
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "正在提交中...";
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
		System.out.println("Score=" + score);
		try {
			String result = ApiRemark.saveRemarkLession(loginUser, loginPwd,
					lesId, rb.getText().toString().trim(), score, "", advice);
			return result;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("--------");
			showShortToast("提交出错，请稍后再试");
			return "提交出错，请稍后再试";

			// 451546365134
		}
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub

		if (result.toString().contains("成功")) {
			share.setBoolean(lesId, true);
			finish();
		}
		showShortToast(result.toString());
	}
}