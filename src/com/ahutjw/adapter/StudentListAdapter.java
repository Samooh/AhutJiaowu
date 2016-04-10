package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ahutjw.app.entity.StudentBean;
import com.mjiaowu.R;

public class StudentListAdapter extends BaseAdapter {
	private Context context;
	private List<StudentBean> datas;

	public StudentListAdapter(Context context, List<StudentBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public StudentBean getItem(int position) {
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
			view = inflater.inflate(R.layout.studentlist_item, null);
			//
			holder.studentname = (TextView) view.findViewById(R.id.studentname);
			holder.studentclass = (TextView) view
					.findViewById(R.id.studentclass);
			holder.Count = (TextView) view
					.findViewById(R.id.count);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		StudentBean bean = getItem(position);
		holder.studentname.setText(bean.getName() + "(" + bean.getNumber()
				+ ")");
		holder.studentclass.setText(bean.getMajorName() + "("
				+ bean.getClassName() + ")");
		holder.Count.setText(position+1+"");
		return view;
	}

	private class ItemHolder {
		TextView studentname;
		TextView studentclass;
		TextView Count;
	}
}
