package com.ahutjw.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.mjiaowu.R;

public class BaseDialog extends Dialog {

	private View dialogView;

	public BaseDialog(Context context, int layout) {
		super(context, R.style.baseDialog);
		dialogView = LayoutInflater.from(context).inflate(layout, null);
		setContentView(dialogView);

		//
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (display.getWidth() - 40); // 设置宽度
		getWindow().setAttributes(lp);
	}

	/**
	 * 获取指定id的view
	 * 
	 * @return
	 */
	public View findViewById(int id) {
		return dialogView.findViewById(id);
	}

	/**
	 * 显示对话框
	 * 
	 */

	// public void show() {
	// this.show();
	// WindowManager windowManager = ((Activity) getContext())
	// .getWindowManager();
	// Display display = windowManager.getDefaultDisplay();
	// WindowManager.LayoutParams lp = getWindow().getAttributes();
	// lp.width = (int) (display.getWidth()); // 设置宽度
	// getWindow().setAttributes(lp);
	// }

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

}
