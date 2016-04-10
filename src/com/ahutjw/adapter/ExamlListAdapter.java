package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahutjw.app.entity.ExamListBean;
import com.mjiaowu.R;

/**
 * 
 * 考试安排列表
 * 
 * @author Administrator
 * 
 */
public class ExamlListAdapter extends BaseAdapter {
	// 上下文
	private Context context;
	// 数据
	private List<ExamListBean> datas;
	private String[] names;

	public ExamlListAdapter(Context context, String[] names,
			List<ExamListBean> datas) {
		this.context = context;
		this.datas = datas;
		this.names = names;
	}

	public int getCount() {
		return datas.size();
	}

	public ExamListBean getItem(int position) {
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
			view = inflater.inflate(R.layout.exam_list_item, null);
			// name
			((TextView) view.findViewById(R.id.name1)).setText(names[0]);
			((TextView) view.findViewById(R.id.name2)).setText(names[1]);
			((TextView) view.findViewById(R.id.name3)).setText(names[2]);
			((TextView) view.findViewById(R.id.name4)).setText(names[3]);
			//
			holder.title1View = (TextView) view.findViewById(R.id.title1);
			holder.title2View = (TextView) view.findViewById(R.id.title2);
			holder.title3View = (TextView) view.findViewById(R.id.title3);
			holder.title4View = (TextView) view.findViewById(R.id.title4);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		ExamListBean bean = getItem(position);
		holder.title1View.setText(bean.getTitle1());
		holder.title2View.setText(bean.getTitle2());
		holder.title3View.setText(bean.getTitle3());
		holder.title4View.setText(bean.getTitle4());

		return view;
	}

	private class ItemHolder {
		TextView title1View;
		TextView title2View;
		TextView title3View;
		TextView title4View;
	}
}
