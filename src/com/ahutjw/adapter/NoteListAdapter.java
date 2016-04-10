package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ahutjw.app.entity.NoteBean;
import com.mjiaowu.R;

public class NoteListAdapter extends BaseAdapter {
	private Context context;
	private List<NoteBean> datas;

	public NoteListAdapter(Context context, List<NoteBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public NoteBean getItem(int position) {
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
			view = inflater.inflate(R.layout.notelist_item, null);
			//
			holder.titleView = (TextView) view.findViewById(R.id.notecontent);
			holder.subtitleView = (TextView) view.findViewById(R.id.notedate);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		NoteBean bean = getItem(position);
		holder.titleView.setText(bean.getNote());
		holder.subtitleView.setText(bean.getNoteDate());
		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView subtitleView;
	}
}
