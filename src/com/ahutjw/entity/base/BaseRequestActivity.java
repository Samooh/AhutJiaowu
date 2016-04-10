package com.ahutjw.entity.base;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ahutjw.api.Api;
import com.ahutjw.utils.HandyTextView;
import com.mjiaowu.R;

public abstract class BaseRequestActivity extends BaseActivity {

	//public static FlippingLoadingDialog dialog;
	public Toast toast;
	public View toastRoot; 

	// public static FlippingLoadingDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//dialog = new FlippingLoadingDialog(this, "正在请求中,请稍后...");
		toastRoot = LayoutInflater.from(BaseRequestActivity.this).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText("正在请求中,请稍后...");
		toast = new Toast(BaseRequestActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
	
	}

	private class AsyncLoading extends AsyncTask<String, String, Object> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			toast.show();
			// dialog.setMessage(onReqestPre());
			// dialog.show();
		}

		@Override
		protected Object doInBackground(String... params) {
			return onReqestDoing(params);
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (!Api.errorStr.equals("")) {
				Toast.makeText(getApplicationContext(), Api.errorStr,
						Toast.LENGTH_SHORT).show();
			}
			onReqestFinish(result);
			toast.cancel();			
			//showCustomToast("请求完成");
			//dialog.dismiss();
		}
	}

	/**
	 * 
	 * @param params
	 */
	protected void sendRequest(String... params) {
		new AsyncLoading().execute(params);
	}

	public abstract String onReqestPre();

	public abstract Object onReqestDoing(String... params);

	/**
	 * 
	 * @param result
	 */
	public abstract void onReqestFinish(Object result);
}