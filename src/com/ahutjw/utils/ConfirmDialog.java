package com.ahutjw.utils;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.mjiaowu.R;

/**
 * 选择对话框
 */
public class ConfirmDialog extends BaseDialog {
	private Button btnConfirmButton;
	private Button btnCancelButton;

	private OnButtonClickCallback onButtonClickCallback;

	public ConfirmDialog(Context context) {
		super(context, R.layout.dialog_confirm);
		btnConfirmButton=(Button)findViewById(R.id.btn_Ok);
		btnCancelButton=(Button)findViewById(R.id.btn_Cancel);
		btnConfirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (onButtonClickCallback != null)
					onButtonClickCallback.onCallback(v);
				dismiss();
			}
		});
		btnCancelButton.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						dismiss();
						
					}
				});
	}



	/**
	 * ok按钮回调事件
	 * 
	 * @param onButtonClickCallback
	 */
	public void setOkButtonCallBack(OnButtonClickCallback onButtonClickCallback) {
		this.onButtonClickCallback = onButtonClickCallback;
	}

	/**
	 * 按钮事件回调函数
	 * 
	 */
	public static interface OnButtonClickCallback {
		public void onCallback(View v);
	}
}
