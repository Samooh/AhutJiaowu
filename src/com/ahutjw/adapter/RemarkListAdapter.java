package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahutjw.app.entity.RemarkLessonInfoBean;
import com.mjiaowu.R;

/**
 * 
 * 教学评价---待评价课程列表
 * 
 * @author Administrator
 * 
 */
public class RemarkListAdapter extends BaseAdapter {
	SharedPreferences sharedPreferences;
	// 上下文
	private Context context;
	// 数据
	private List<RemarkLessonInfoBean> datas;

	public RemarkListAdapter(Context context, List<RemarkLessonInfoBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public RemarkLessonInfoBean getItem(int position) {
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
			view = inflater.inflate(R.layout.remark_list_item, null);
			//
			holder.titleView = (TextView) view.findViewById(R.id.title);
			holder.stateView = (TextView) view.findViewById(R.id.state);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		try {
			if(datas!=null){
				RemarkLessonInfoBean bean = getItem(position);
				if(bean.JSXM!=null)
				holder.titleView.setText(bean.KCMC+"("+bean.JSXM+")");
				else {
					holder.titleView.setText(bean.KCMC);
				}
				System.out.println("PJDF="+bean.PJDF);
				holder.stateView.setText(bean.PJDF.equals("-1") ? "未评价" :"已评价");
				}
		} catch (Exception e) {
			e.printStackTrace();
			return view;
		}
		
		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView stateView;
	}
}
