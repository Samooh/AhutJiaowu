package com.mjiaowu;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.SimpleListDialogAdapter;
import com.ahutjw.entity.base.BaseActivity;
import com.ahutjw.utils.SimpleListDialog;
import com.ahutjw.utils.SimpleListDialog.onSimpleListItemClickListener;

public class ClassroomSearchActivity extends BaseActivity implements
		onSimpleListItemClickListener {
	private ImageView backImageView = null;
	private TextView monthTextView, dateTextView, campusTextView,
			teachBulingTextView, timeTextView;
	private Button searchButton = null;
	private String monthArr[] = new String[7];
	private String dateArr[] = new String[31];
	private String campusArr[] = new String[] { "东校区", "校本部" };
	private String teachBulingArr[] = new String[] { "多媒体", "实验室", "普通", "阶梯" };
	private String timeArr[] = new String[] { "第1,2节", "第3,4节", "第5,6节",
			"第7,8节", "第9,10,11节", "上午", "下午", "整天" };
	private int flag = 0;
	private int monthMax[] = new int[] { 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1 };
	private SimpleListDialog mSimpleListDialog;
	private String month, day;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classroom_search);
		findView();
		initMonth();
		initDate();
		SetListener();
		SimpleDateFormat sdfy = new SimpleDateFormat("dd");
		day = sdfy.format(new java.util.Date());
		SimpleDateFormat sdfm = new SimpleDateFormat("MM");
		month = sdfm.format(new java.util.Date());
		monthTextView.setText(month + "月");

		dateTextView.setText(day + "号");

		campusTextView.setText("东校区");

		teachBulingTextView.setText("多媒体");
		timeTextView.setText("第1,2节");

	}

	void setDialog(int temp) {

		flag = temp;
		mSimpleListDialog = new SimpleListDialog(ClassroomSearchActivity.this);
		mSimpleListDialog.setTitleLineVisibility(View.GONE);
		switch (flag) {
		case 0:
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
					ClassroomSearchActivity.this, monthArr));
			break;
		case 1:
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
					ClassroomSearchActivity.this, dateArr));
			break;
		case 2:
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
					ClassroomSearchActivity.this, campusArr));
			break;
		case 3:
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
					ClassroomSearchActivity.this, teachBulingArr));
			break;
		case 4:
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
					ClassroomSearchActivity.this, timeArr));
			break;
		default:
			break;
		}
		mSimpleListDialog
				.setOnSimpleListItemClickListener(ClassroomSearchActivity.this);
		mSimpleListDialog.show();
	}

	public class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.month_textView) {
				setDialog(0);
			} else if (v.getId() == R.id.date_textview) {
				setDialog(1);
			} else if (v.getId() == R.id.campus_textView) {
				setDialog(2);
			} else if (v.getId() == R.id.teachingBuling_textView) {
				setDialog(3);
			} else if (v.getId() == R.id.time) {
				setDialog(4);
			} else if (v.getId() == R.id.search_classRoom_btn) {
				if (judgeDateErro()) {
					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR);
					int nowmonth = Integer.parseInt(month);
					int nowday = Integer.parseInt(day);
					int monthInt = Integer.parseInt(monthTextView.getText()
							.toString().split("月")[0]);
					int dayInt = Integer.parseInt(dateTextView.getText()
							.toString().split("号")[0]);
					if (monthInt < nowmonth) {
						Toast.makeText(getApplicationContext(), "你穿越喽！",
								Toast.LENGTH_SHORT).show();
					} else if (monthInt == nowmonth && dayInt < nowday) {
						Toast.makeText(getApplicationContext(), "你穿越喽！",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent intent = new Intent();
						intent.putExtra("place", campusTextView.getText());
						intent.putExtra("style", teachBulingTextView.getText());
						intent.putExtra("time1", year + "-" + monthInt + "-"
								+ dayInt);
						intent.putExtra("time2", timeTextView.getText());
						intent.setClass(getApplicationContext(),
								ClassroomListActivity.class);
						ClassroomSearchActivity.this.startActivity(intent);
					}
				}
			}
		}

	}

	boolean judgeDateErro() {
		// Pattern pattern;
		// Matcher matcher;
		int monthInt, dateInt;
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		System.out.println("the year is" + year);

		String monthstr = monthTextView.getText().toString();
		String datestr = dateTextView.getText().toString();

		String monthStrArr[] = monthstr.split("月");
		String dateStrArr[] = datestr.split("号");

		monthInt = Integer.parseInt(monthStrArr[0]);
		dateInt = Integer.parseInt(dateStrArr[0]);

		System.out.println("month" + monthInt + "date" + dateInt + "monthMax"
				+ monthMax[monthInt - 1]);

		if (dateInt == 31 && monthMax[monthInt - 1] == 0) {
			Toast.makeText(getApplicationContext(), "您输入的日期有误！",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if ((monthInt == 2 && dateInt == 29 && !isRunYear(year))
				|| (dateInt == 30 && monthInt == 2)) {
			Toast.makeText(getApplicationContext(), "您输入的日期有误！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		/*
		 * try{ pattern = Pattern.compile("(.+?)月"); matcher =
		 * pattern.matcher(monthstr); monthstr = matcher.group(1);
		 * 
		 * pattern = Pattern.compile("(.+?)号"); matcher =
		 * pattern.matcher(datestr); datestr = matcher.group(1);
		 * 
		 * monthInt = Integer.parseInt(monthstr); dateInt =
		 * Integer.parseInt(datestr);
		 * 
		 * System.out.println(monthInt+"****************************"); } catch
		 * (Exception e) { // TODO: handle exception }
		 */
		return true;
	}

	boolean isRunYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
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

	void SetListener() {
		monthTextView.setOnClickListener(new MyOnClickListener());
		dateTextView.setOnClickListener(new MyOnClickListener());
		campusTextView.setOnClickListener(new MyOnClickListener());
		teachBulingTextView.setOnClickListener(new MyOnClickListener());
		timeTextView.setOnClickListener(new MyOnClickListener());
		searchButton.setOnClickListener(new MyOnClickListener());
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
	}

	void findView() {
		backImageView = (ImageView) findViewById(R.id.back);
		monthTextView = (TextView) findViewById(R.id.month_textView);
		dateTextView = (TextView) findViewById(R.id.date_textview);
		campusTextView = (TextView) findViewById(R.id.campus_textView);
		teachBulingTextView = (TextView) findViewById(R.id.teachingBuling_textView);
		searchButton = (Button) findViewById(R.id.search_classRoom_btn);
		timeTextView = (TextView) findViewById(R.id.time);
	}

	void initMonth() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		month++;
		for (int i = 0; i < monthArr.length; i++) {
			try {
				monthArr[i] = month + "月";
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (month == 12) {
				month = 1;
			} else {
				month++;
			}
		}
	}

	void initDate() {
		int temp = 0;
		for (int i = 0; i < dateArr.length; i++) {
			temp = i + 1;
			dateArr[i] = temp + "号";
		}
	}

	@Override
	public void onItemClick(int position) {
		// TODO Auto-generated method stub
		if (flag == 0) {
			monthTextView.setText(monthArr[position]);
		} else if (flag == 1) {
			dateTextView.setText(dateArr[position]);
		} else if (flag == 2) {
			campusTextView.setText(campusArr[position]);
		} else if (flag == 3) {
			teachBulingTextView.setText(teachBulingArr[position]);
		} else if (flag == 4) {
			timeTextView.setText(timeArr[position]);
		}
	}

}
