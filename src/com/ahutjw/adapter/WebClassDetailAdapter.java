package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ahutjw.app.entity.WebClassDetailBean;
import com.mjiaowu.R;

/**
 * 
 * 网上选课列表
 * 
 * @author Administrator
 * 
 */
public class WebClassDetailAdapter extends BaseAdapter {
	// 上下文
	private Context context;
	// 数据
	private List<WebClassDetailBean> datas;
	// 选择状态
	private boolean[] selStates;

	public WebClassDetailAdapter(Context context, List<WebClassDetailBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public WebClassDetailBean getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public void notifyDataSetChanged() {

		// 第一次调用，初始化
		if (this.selStates == null) {
			this.selStates = new boolean[getCount()];
			for (int i = 0; i < getCount(); i++) {
				String bo = getItem(i).getWhetherPicked().trim();
				if (bo.equals("true")) {
					this.selStates[i] = true;
				} else {
					this.selStates[i] = false;
				}
			}
		}
		super.notifyDataSetChanged();
	}

	public View getView(int position, View view, ViewGroup parent) {
		ItemHolder holder;
		if (view == null) {
			holder = new ItemHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.webclass_detail_item, null);
			//
			holder.radiobtnView = (RadioButton) view
					.findViewById(R.id.radiobtn);
			holder.titleView = (TextView) view.findViewById(R.id.title);
			holder.weektimeView = (TextView) view.findViewById(R.id.weektime);
			holder.lessiontimeView = (TextView) view
					.findViewById(R.id.lessiontime);
			holder.lessionlocationView = (TextView) view
					.findViewById(R.id.lessionlocation);
			holder.schoolView = (TextView) view.findViewById(R.id.school);
			holder.hasnumView = (TextView) view.findViewById(R.id.hasnum);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		// 赋值
		WebClassDetailBean bean = getItem(position);
		holder.radiobtnView.setChecked(selStates[position]);
		holder.titleView.setText(bean.getTeacherName());
		holder.weektimeView.setText("");
		holder.lessiontimeView.setText(bean.getLessonTime());
		holder.lessionlocationView.setText(bean.getLessonPlace());
		holder.schoolView.setText(bean.getSchoolArea());
		holder.hasnumView.setText(bean.getPickedByAll());

		return view;
	}

	// 复位状态---false状态
	private void resetSelStates() {
		for (int i = 0; i < selStates.length; i++) {
			selStates[i] = false;
		}
	}

	/**
	 * 选择
	 * 
	 * @param position
	 */
	public void setSel(int position) {
		// 首先复位，模拟radio效果
		resetSelStates();
		selStates[position] = true;
		// 刷新
		notifyDataSetChanged();
	}

	/**
	 * 获取选中的item
	 * 
	 * @return 如果未选择返回null，选择了则返回当前的对象
	 */
	public WebClassDetailBean getSelItem() {
		int n = -1;// 默认为未选
		for (int i = 0; i < selStates.length; i++) {
			if (selStates[i]) {// 找到，退出
				n = i;
				break;
			}
		}
		if (n == -1) {
			return null;
		}
		return getItem(n);
	}

	private class ItemHolder {
		RadioButton radiobtnView;
		TextView titleView;
		TextView weektimeView;
		TextView lessiontimeView;
		TextView lessionlocationView;
		TextView schoolView;
		TextView hasnumView;
	}
}
