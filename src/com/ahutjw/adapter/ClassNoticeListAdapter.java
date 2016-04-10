package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahutjw.app.entity.ClassNoticeBean;
import com.mjiaowu.R;


public class ClassNoticeListAdapter extends BaseAdapter {
	private Context context;
	private List<ClassNoticeBean> datas;

	public ClassNoticeListAdapter(Context context, List<ClassNoticeBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public ClassNoticeBean getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		ItemHolder holder;
		if (view == null) {
			holder = new ItemHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.classnotice_list_item, null);
			//
			holder.titleView = (TextView) view.findViewById(R.id.noticetitle);
			holder.subtitleView = (TextView) view.findViewById(R.id.date);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		ClassNoticeBean bean = getItem(position);
		holder.titleView.setText(bean.getNoticeDetail());
		holder.subtitleView.setText(bean.getNoticeDate());
		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView subtitleView;
	}
}
