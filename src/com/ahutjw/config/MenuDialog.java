package com.ahutjw.config;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mjiaowu.R;

/**
 * 菜单对话框--在底部显示
 * 
 */
public class MenuDialog extends Dialog {

	//
	private Context context;
	private View view;
	//
	private OnMenuItemClickCallback onMenuItemClickCallback;

	public MenuDialog(Context context) {
		super(context, R.style.menuDialog);
		this.context = context;
		// 设置宽度
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		getWindow().setAttributes(lp);
		// 设置位置
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.menuAnim); // 添加动画
		setCanceledOnTouchOutside(true);
		// 设置layout
		view = LayoutInflater.from(context).inflate(R.layout.dialog_menu, null);
		setContentView(view);
		// 绑定“取消”事件
		view.findViewById(R.id.btnCancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
	}

	/**
	 * 设置标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		((TextView) view.findViewById(R.id.title)).setText(text);
	}

	/**
	 * 设置list数据
	 * 
	 * @param resId
	 *            数组资源id
	 * 
	 */
	public void setList(int resId) {
		ListView lstView = (ListView) view.findViewById(R.id.list);
		lstView.setAdapter(new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, context.getResources()
						.getStringArray(resId)));
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (onMenuItemClickCallback != null) {
					onMenuItemClickCallback.onItemClick(position);
				}
				dismiss();
			}
		});

	}

	public void setList(String[] resIds) {
		ListView lstView = (ListView) view.findViewById(R.id.list);
		lstView.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, resIds));
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (onMenuItemClickCallback != null) {
					onMenuItemClickCallback.onItemClick(position);
				}
				dismiss();
			}
		});

	}

	/**
	 * item点击回调
	 * 
	 * @param onMenuItemClickCallback
	 */
	public void setMenuItemClickCallback(
			OnMenuItemClickCallback onMenuItemClickCallback) {
		this.onMenuItemClickCallback = onMenuItemClickCallback;
	}

	public static interface OnMenuItemClickCallback {
		public void onItemClick(int position);
	}
}
