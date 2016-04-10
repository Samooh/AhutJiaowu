package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahutjw.app.entity.CourseResultTeaBean;
import com.mjiaowu.R;

/**
 * 
 * 课表
 * 
 * @author Administrator
 * 
 */
public class CourseResultTeaAdapter extends BaseAdapter {
	// 上下文
	private Context context;
	// 数据
	private List<CourseResultTeaBean> datas;

	public CourseResultTeaAdapter(Context context,
			List<CourseResultTeaBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public CourseResultTeaBean getItem(int position) {
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
			view = inflater.inflate(R.layout.course_result_tea_item, null);
			//
			holder.titleView = (TextView) view.findViewById(R.id.title);
			holder.lesLocationView = (TextView) view
					.findViewById(R.id.lesLocation);
			holder.lessionimeView = (TextView) view
					.findViewById(R.id.lessiontime);
			holder.studentMajor = (TextView) view
					.findViewById(R.id.studentMajor);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		// 赋值
		CourseResultTeaBean bean = getItem(position);
		holder.titleView.setText(bean.getName());
		holder.lesLocationView.setText(bean.getPlace());
		holder.lessionimeView.setText("周" + bean.getWeekDay() + " 第"
				+ bean.getTime() + "节  " + bean.getTimePerWeek());
		holder.studentMajor.setText(bean.getStudentMajor());

		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView lesLocationView;
		TextView lessionimeView;
		TextView studentMajor;
	}
}
