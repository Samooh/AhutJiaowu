package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahutjw.app.entity.ScoreResultBean;
import com.mjiaowu.R;

/**
 * 
 * 成绩查询--结果列表
 * 
 * @author Administrator
 * 
 */
public class ScoreResultListAdapter extends BaseAdapter {
	// 上下文
	private Context context;
	// 数据
	private List<ScoreResultBean> datas;

	public ScoreResultListAdapter(Context context, List<ScoreResultBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public ScoreResultBean getItem(int position) {
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
			view = inflater.inflate(R.layout.score_result_list_item, null);
			//
			holder.titleView = (TextView) view.findViewById(R.id.title);
			holder.score1View = (TextView) view.findViewById(R.id.score1);
			holder.score2View = (TextView) view.findViewById(R.id.score2);
			holder.score3View = (TextView) view.findViewById(R.id.score3);
			holder.score4View = (TextView) view.findViewById(R.id.score4);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		ScoreResultBean bean = getItem(position);
		holder.titleView.setText(bean.getLessonName());
		holder.score1View.setText("折算成绩：" + bean.getZscj());
		holder.score2View.setText("总评成绩：" + bean.getZpcj());
		holder.score3View.setText("期末成绩：" + bean.getQmcj());
		holder.score4View.setText("平时成绩：" + bean.getPscj());

		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView score1View;
		TextView score2View;
		TextView score3View;
		TextView score4View;
	}
}
