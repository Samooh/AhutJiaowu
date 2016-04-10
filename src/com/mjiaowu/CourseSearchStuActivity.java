package com.mjiaowu;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.adapter.SimpleListDialogAdapter;
import com.ahutjw.entity.base.BaseActivity;
import com.ahutjw.utils.SimpleListDialog;
import com.ahutjw.utils.SimpleListDialog.onSimpleListItemClickListener;

/**
 * 
 * 课表查询--学生
 * 
 */
public class CourseSearchStuActivity extends BaseActivity implements
		onSimpleListItemClickListener {
	private ImageView imgBack;
	private TextView mSelectYear, mSelectSemester;
	//
	private String[] xns = new String[5];
	private SimpleListDialog mSimpleListDialog;
	// 控制共用onItemClick产生的二义性
	public static int flag = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_search_stu);
		// MediaPlayer mPlayer=MediaPlayer.create(getApplicationContext(),
		// R.raw.tips);
		// mPlayer.start();
		initXn();
		mSelectYear = (TextView) findViewById(R.id.spXn);// 学年
		mSelectSemester = (TextView) findViewById(R.id.spXq);// 学期
		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		mSelectYear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 0;
				mSimpleListDialog = new SimpleListDialog(
						CourseSearchStuActivity.this);
				mSimpleListDialog.setTitle("请选择学年");
				mSimpleListDialog.setTitleLineVisibility(View.GONE);
				mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
						CourseSearchStuActivity.this, xns));
				mSimpleListDialog
						.setOnSimpleListItemClickListener(CourseSearchStuActivity.this);
				mSimpleListDialog.show();
			}
		});
		mSelectSemester.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 1;
				mSimpleListDialog = new SimpleListDialog(
						CourseSearchStuActivity.this);
				mSimpleListDialog.setTitle("请选择学期");
				mSimpleListDialog.setTitleLineVisibility(View.GONE);
				mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
						CourseSearchStuActivity.this, getResources()
								.getStringArray(R.array.scoreSearchXq)));
				mSimpleListDialog
						.setOnSimpleListItemClickListener(CourseSearchStuActivity.this);
				mSimpleListDialog.show();
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

	// 查询按钮事件
	public void searchClick(View v) {

		String xn = mSelectYear.getText().toString();
		String xq = mSelectSemester.getText().toString();
		//
		Bundle b = new Bundle();
		b.putString("xn", xn);
		b.putString("xq", xq);
		Intent intent = new Intent();
		intent.putExtras(b);
		intent.setClass(CourseSearchStuActivity.this,
				CourseResultActivity.class);
		CourseSearchStuActivity.this.startActivity(intent);
	}

	// 下拉事件
	public void linSpinnerClick(View v) {
		String tag = v.getTag().toString();
		if (tag.equals("0")) {
			flag = 0;
			mSimpleListDialog = new SimpleListDialog(
					CourseSearchStuActivity.this);
			mSimpleListDialog.setTitle("请选择学期");
			mSimpleListDialog.setTitleLineVisibility(View.GONE);
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
					CourseSearchStuActivity.this, getResources()
							.getStringArray(R.array.scoreSearchXq)));
			mSimpleListDialog
					.setOnSimpleListItemClickListener(CourseSearchStuActivity.this);
			mSimpleListDialog.show();
		} else {
			flag = 1;
			mSimpleListDialog = new SimpleListDialog(
					CourseSearchStuActivity.this);
			mSimpleListDialog.setTitle("请选择学期");
			mSimpleListDialog.setTitleLineVisibility(View.GONE);
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
					CourseSearchStuActivity.this, getResources()
							.getStringArray(R.array.scoreSearchXq)));
			mSimpleListDialog
					.setOnSimpleListItemClickListener(CourseSearchStuActivity.this);
			mSimpleListDialog.show();
		}
	}

	private void initXn() {
		Calendar nowCalendar = Calendar.getInstance();
		int year = nowCalendar.get(Calendar.YEAR) + 1;
		for (int i = 0; i < xns.length; i++) {
			xns[i] = (year - (i + 1)) + "-" + (year - i);
		}
	}

	@Override
	public void onItemClick(int position) {
		// TODO Auto-generated method stub
		if (flag == 0) {
			System.out.println("Click position:" + position);
			String year = xns[position];
			System.out.println("学年：" + year);
			mSelectYear.setText(year);
		} else {
			String semester = getResources().getStringArray(
					R.array.scoreSearchXq)[position];
			System.out.println("学期：" + semester);
			mSelectSemester.setText(semester);
		}

	}

}