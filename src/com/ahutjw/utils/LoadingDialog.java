package com.ahutjw.utils;

import com.mjiaowu.R;

import android.content.Context;
import android.widget.TextView;

/**
 * 自定义对话框
 */
public class LoadingDialog extends BaseDialog {

	public LoadingDialog(Context context) {
		super(context, R.layout.dialog_loading);
	}


	public void setMessage(String message) {
		TextView tView = (TextView) findViewById(R.id.message);
		tView.setText(message);
	}
}
