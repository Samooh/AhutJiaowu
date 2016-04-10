package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ahutjw.app.entity.ClassBean;
import com.mjiaowu.R;

public class ClassroomListAdapter extends BaseAdapter {
	private Context context;
	private List<ClassBean> datas;

	public ClassroomListAdapter(Context context, List<ClassBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public ClassBean getItem(int position) {
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
			view = inflater.inflate(R.layout.classroomlist_item, null);
			//
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.style = (TextView) view.findViewById(R.id.style);
			holder.count = (TextView) view.findViewById(R.id.count);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		ClassBean bean = getItem(position);
		holder.name.setText(bean.getName());
		holder.style.setText(bean.getStyle());
		holder.count.setText(bean.getCount());
		return view;
	}

	private class ItemHolder {
		TextView name;
		TextView style;
		TextView count;
	}
}
