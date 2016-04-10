package com.mjiaowu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiBaseHttp;
import com.ahutjw.api.ApiValid;
import com.ahutjw.config.MenuDialog;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

/**
 * 
 * 登录页面
 * 
 */
public class LoginActivity extends BaseRequestActivity {

	private LinearLayout layout1, layout2;
	private TextView tvType, tvUser;
	//
	private EditText etUser, etPwd;
	private TextView spType;
	private ImageView imgBack;
	private final String widgetupdate = "com.wd.appWidgetUpdate";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// 实例化
		etUser = (EditText) findViewById(R.id.etUser);
		etPwd = (EditText) findViewById(R.id.etPwd);
		spType = (TextView) findViewById(R.id.spType);
		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		tvType = (TextView) findViewById(R.id.tvType);
		tvUser = (TextView) findViewById(R.id.tvUser);
		imgBack = (ImageView) findViewById(R.id.back);

		// 判断显示的layout
		if (sharePre.getBoolean(S.LOGIN_IS, false)) {// 已经登录
			layout1.setVisibility(View.GONE);

			tvType.setText(sharePre.getString(S.LOGIN_TYPE, ""));
			tvUser.setText(sharePre.getString(S.LOGIN_USERNAME, ""));
		} else {// 尚未登录
			layout1.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
			// 动画
			// LinearLayout layout = (LinearLayout)
			// findViewById(R.id.layoutLogin);
			// layout.startAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
		}
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
	}

	// 登录绑定click事件
	public void loginClick(View view) {
		switch (view.getId()) {
		case R.id.login:
			String type = spType.getText() + "";
			String username = etUser.getText().toString();
			String pwd = etPwd.getText().toString();
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.shake);
			if (username.equals("")) {
				etUser.startAnimation(animation);
				showCustomToast("请输入用户名");
				return;
			} else if (pwd.equals("")) {
				etPwd.startAnimation(animation);
				showCustomToast("请输入密码");
				return;
			}
			sendRequest(type, username, pwd);
			break;
		case R.id.exit:
			Intent intent1 = new Intent();
			intent1.setAction(widgetupdate);
			intent1.putExtra("mark", "1");
			sendBroadcast(intent1);
			sharePre.setBoolean(S.LOGIN_IS, false);
			showCustomToast("您已经退出当前账户");
			sharePre.setString(S.LOGIN_PWD, "");
			sharePre.setString(S.LOGIN_USERNAME, "");
			DbManager dm = new DbManager(LoginActivity.this);
			dm.deleteAll(DbManager.course.TABLE_NAME);
			dm.deleteAll(DbManager.exam.TABLE_NAME);
			dm.deleteAll(DbManager.remark.TABLE_NAME);
			dm.close();
			finish();
			break;
		default:
			break;
		}
	}

	// 身份下拉==绑定事件
	public void linSpinnerClick(View v) {
		MenuDialog dialog = new MenuDialog(this);
		// dialog.setContentView(R.layout.dialog_menu);
		dialog.setTitle("请选择您的登录身份");
		dialog.setList(R.array.loginType);
		dialog.setMenuItemClickCallback(new MenuDialog.OnMenuItemClickCallback() {
			@Override
			public void onItemClick(int position) {
				if (position == 0) {
					spType.setText("本科生");
				} else if (position == 1) {
					spType.setText("教师");
				}
			}
		});
		dialog.show();
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
	public String onReqestPre() {
		// TODO Auto-generated method stub
		// BaseRequestActivity.dialog.setText("正在登录中,请稍后...");
		return "正在登录中,请稍后...";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return ApiValid.validUser(params[0], params[1], params[2]);
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub
		showShortToast(result + "");
		if (result.equals("登录成功")) {
			sharePre.setBoolean(S.LOGIN_IS, true);
			sharePre.setString(S.LOGIN_USERNAME, etUser.getText().toString());
			sharePre.setString(S.LOGIN_PWD, etPwd.getText().toString());
			sharePre.setString(S.LOGIN_TYPE, spType.getText().toString());
			Intent intent1 = new Intent();
			intent1.putExtra("mark", "1");
			intent1.setAction(widgetupdate);
			sendBroadcast(intent1);
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (sharePre.getString(S.LOGIN_TYPE, "本科生").equals("本科生")) {
						try {
							String addresult = ApiBaseHttp
									.get("http://211.70.149.139:8990/AHUTYDJWService.asmx/addStuInfo?stuNum="
											+ sharePre.getString(
													S.LOGIN_USERNAME, "0")
											+ "&password="
											+ sharePre.getString(S.LOGIN_PWD,
													"0"));
							System.out.println(addresult);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						try {
							String addresult = ApiBaseHttp
									.get("http://211.70.149.139:8990/AHUTYDJWService.asmx/addTeaInfo?teaNum="
											+ sharePre.getString(
													S.LOGIN_USERNAME, "0")
											+ "&password="
											+ sharePre.getString(S.LOGIN_PWD,
													"0"));
							System.out.println(addresult);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			new Thread(runnable).start();
			// 清除所有的数据库缓存--如果账号改变
			/*
			 * if (!sharePre.getString(S.LOGIN_USERNAME, "").equals(
			 * etUser.getText().toString())) { DbManager dm = new
			 * DbManager(LoginActivity.this);
			 * dm.deleteAll(DbManager.course.TABLE_NAME);
			 * dm.deleteAll(DbManager.exam.TABLE_NAME);
			 * dm.deleteAll(DbManager.remark.TABLE_NAME); dm.close(); }
			 */
			// 保存登录状态和账户密码

			JPushInterface.setAliasAndTags(getApplicationContext(), etUser
					.getText().toString(), null);
			System.out.println("success");
			finish();
		}
	}
}