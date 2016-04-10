package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahutjw.app.entity.CourseResultStuBean;
import com.mjiaowu.R;

public class CourseResultStuAdapter extends BaseAdapter {
	// 上下文
	private Context context;
	// 数据
	private List<CourseResultStuBean> datas;

	public CourseResultStuAdapter(Context context, List<CourseResultStuBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public CourseResultStuBean getItem(int position) {
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
			view = inflater.inflate(R.layout.course_result_stu_item, null);
			//
			holder.titleView = (TextView) view.findViewById(R.id.title);
			holder.lesScoreView = (TextView) view.findViewById(R.id.lesScore);
			holder.lessiontimeView = (TextView) view
					.findViewById(R.id.lessiontime);
			holder.lessionlocationView = (TextView) view
					.findViewById(R.id.lessionlocation);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		// 赋值
		CourseResultStuBean bean = getItem(position);
		holder.titleView.setText(bean.getLessonName() + "（"
				+ bean.getTeacherName() + "）");
		holder.lesScoreView.setText(bean.getLessonScore());
		holder.lessiontimeView.setText(bean.getLessonTime());
		holder.lessionlocationView.setText(bean.getLessonPlace());

		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView lesScoreView;
		TextView lessiontimeView;
		TextView lessionlocationView;
	}
}
