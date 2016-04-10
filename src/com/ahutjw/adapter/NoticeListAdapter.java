package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahutjw.app.entity.NoticeListBean;
import com.mjiaowu.R;


public class NoticeListAdapter extends BaseAdapter {
	private Context context;
	private List<NoticeListBean> datas;

	public NoticeListAdapter(Context context, List<NoticeListBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public NoticeListBean getItem(int position) {
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
			view = inflater.inflate(R.layout.notice_list_item, null);
			//
			holder.titleView = (TextView) view.findViewById(R.id.title);
			holder.subtitleView = (TextView) view.findViewById(R.id.subtitle);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		NoticeListBean bean = getItem(position);
		holder.titleView.setText(bean.getTitle());
		holder.subtitleView.setText(bean.getDtmPublish());
		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView subtitleView;
	}
}
