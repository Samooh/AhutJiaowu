package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahutjw.app.entity.WebClassListBean;
import com.mjiaowu.R;

/**
 * 
 * 网上选课列表
 * 
 * @author Administrator
 * 
 */
public class WebClassListAdapter extends BaseAdapter {
	// 上下文
	private Context context;
	// 数据
	private List<WebClassListBean> datas;

	public WebClassListAdapter(Context context, List<WebClassListBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public WebClassListBean getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		ItemHolder holder;
		if (view == null) {
			holder = new ItemHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.webclass_list_item, null);
			//
			holder.titleView = (TextView) view.findViewById(R.id.title);
			holder.stateView = (TextView) view.findViewById(R.id.state);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		WebClassListBean bean = getItem(position);
		holder.titleView.setText(bean.getLessonName() + "（"
				+ bean.getLessonProperty() + "）");
		holder.stateView.setText("选否/余量 ：" + bean.getWhetherPicked() + "/"
				+ bean.getLeftNum());

		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView stateView;
	}
}
