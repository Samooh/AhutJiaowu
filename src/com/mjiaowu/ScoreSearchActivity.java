package com.mjiaowu;

import java.util.Calendar;

//import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahutjw.adapter.SimpleListDialogAdapter;
import com.ahutjw.entity.base.BaseActivity;
import com.ahutjw.utils.SimpleListDialog;
import com.ahutjw.utils.SimpleListDialog.onSimpleListItemClickListener;

/**
 * 成绩查询
 * 
 * 
 */
public class ScoreSearchActivity extends BaseActivity implements onSimpleListItemClickListener{

	private EditText etXh, etSfzh;
	private TextView mSelectYear, mSelectSemester;
	private CheckBox stateBox;
	//控制共用onItemClick产生的二义性
		public static int flag=0;
	private SimpleListDialog mSimpleListDialog;
	private String[] xns = new String[5];
	private ImageView imgBack;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_search);
		System.out.println("ScoreSearchActivity start!");
		//MediaPlayer mPlayer=MediaPlayer.create(getApplicationContext(), R.raw.tips);
		//mPlayer.start();
		initXn();
		// 实例化
		imgBack=(ImageView)findViewById(R.id.back);
		etXh = (EditText) findViewById(R.id.etXh);// 学号
		etSfzh = (EditText) findViewById(R.id.etSfzh);// 身份证号
		mSelectYear = (TextView) findViewById(R.id.spXn);// 学年
		mSelectSemester = (TextView) findViewById(R.id.spXq);// 学期
		stateBox = (CheckBox) findViewById(R.id.state);// 状态
		// 初始化值
		etXh.setText(sharePre.getString("etXh", ""));
		etSfzh.setText(sharePre.getString("etSfzh", ""));
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
				flag=0;
				mSimpleListDialog = new SimpleListDialog(ScoreSearchActivity.this);
				mSimpleListDialog.setTitle("请选择学年");
				mSimpleListDialog.setTitleLineVisibility(View.GONE);
				mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(ScoreSearchActivity.this, xns));
				mSimpleListDialog.setOnSimpleListItemClickListener(ScoreSearchActivity.this);
				mSimpleListDialog.show();
			}
		});
		mSelectSemester.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag=1;
				mSimpleListDialog = new SimpleListDialog(ScoreSearchActivity.this);
				mSimpleListDialog.setTitle("请选择学期");
				mSimpleListDialog.setTitleLineVisibility(View.GONE);
				mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(ScoreSearchActivity.this, getResources().getStringArray(R.array.scoreSearchXq)));
				mSimpleListDialog.setOnSimpleListItemClickListener(ScoreSearchActivity.this);
				mSimpleListDialog.show();
			}
		});
	}

	// 查询按钮事件
	public void searchClick(View v) {
		String xh = etXh.getText().toString().trim();
		String sfzh = etSfzh.getText().toString().trim();
		String xn = mSelectYear.getText().toString();
		String xq = mSelectSemester.getText().toString();
		if (xh.equals("") || sfzh.equals("")) {
			showCustomToast("学号和身份证不能为空");
			return;
		}
		// 处理学期，学年
		xn = xn.replaceAll("-.*-", "");
		xq = xq.replaceAll("-.*-", "");
		//
		Bundle b = new Bundle();
		b.putString("xh", xh);
		b.putString("sfzh", sfzh);
		b.putString("xn", xn);
		b.putString("xq", xq);
		b.putBoolean("state", stateBox.isChecked());
		openActivity(ScoreResultListActivity.class, b);
	}

	// 下拉事件
	public void linSpinnerClick(View v) {
		String tag = v.getTag().toString();
		if (tag.equals("0")) {
			flag=0;
			mSimpleListDialog = new SimpleListDialog(ScoreSearchActivity.this);
			mSimpleListDialog.setTitle("请选择学期");
			mSimpleListDialog.setTitleLineVisibility(View.GONE);
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(ScoreSearchActivity.this, getResources().getStringArray(R.array.scoreSearchXq)));
			mSimpleListDialog.setOnSimpleListItemClickListener(ScoreSearchActivity.this);
			mSimpleListDialog.show();
		} else {
			flag=1;
			mSimpleListDialog = new SimpleListDialog(ScoreSearchActivity.this);
			mSimpleListDialog.setTitle("请选择学期");
			mSimpleListDialog.setTitleLineVisibility(View.GONE);
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(ScoreSearchActivity.this, getResources().getStringArray(R.array.scoreSearchXq)));
			mSimpleListDialog.setOnSimpleListItemClickListener(ScoreSearchActivity.this);
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
		if(flag==0){
			System.out.println("Click position:"+position);
			String year=xns[position];
			System.out.println("学年："+year);
			mSelectYear.setText(year);
		}else {
			String semester=getResources().getStringArray(R.array.scoreSearchXq)[position];
			System.out.println("学期："+semester);
			mSelectSemester.setText(semester);
		}
	}


}