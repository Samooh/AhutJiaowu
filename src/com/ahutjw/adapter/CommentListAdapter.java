package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahutjw.api.ApiBaseHttp;
import com.ahutjw.app.entity.CommentBean;
import com.mjiaowu.R;

public class CommentListAdapter extends BaseAdapter {
	private Context context;
	private List<CommentBean> datas;
	private String user;
	private String password;

	public CommentListAdapter(Context context, List<CommentBean> datas,
			String user, String password) {
		this.context = context;
		this.datas = datas;
		this.user = user;
		this.password = password;
	}

	public int getCount() {
		return datas.size();
	}

	public CommentBean getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ItemHolder holder;
		if (view == null) {
			holder = new ItemHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.commentlist_item, null);
			//
			holder.content = (TextView) view.findViewById(R.id.content);
			holder.date = (TextView) view.findViewById(R.id.date);
			holder.count = (TextView) view.findViewById(R.id.count);
			holder.like = (ImageView) view.findViewById(R.id.like);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		final CommentBean bean = getItem(position);
		holder.content.setText(bean.getContent());
		holder.date.setText(bean.getDate());
		holder.count.setText(bean.getCount());
		System.out.println(bean.getLike());
		if (bean.getLike().equals("1")) {
			holder.like.setImageResource(R.drawable.yespng);
		} else {
			holder.like.setImageResource(R.drawable.nopng);
		}
		if (!user.equals("") && !password.equals("")) {
			holder.like.setOnClickListener(new ImageView.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					holder.like.setImageResource(R.drawable.yespng);
					if (bean.getLike().equals("0")) {
						holder.count.setText((Integer.parseInt(bean.getCount()) + 1)
								+ "");
						bean.setLike("1");
						bean.setCount((Integer.parseInt(bean.getCount()) + 1)
								+ "");
					}
					// TODO Auto-generated method stub
					new Thread() {
						@Override
						public void run() {
							// 你要执行的方法
							String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/updateComment";
							String result;
							try {
								result = ApiBaseHttp
										.DoPost(url,
												new String[] { "user",
														"password", "ID",
														"Count" },
												new String[] {
														user,
														password,
														bean.getID(),
														(Integer.parseInt(bean
																.getCount()) - 1)
																+ "" });
								System.out.println(">>>>>>>>>>>" + result);
								String[] results = result.split(">");
								if (results[2].replace("</int", "").equals("1")) {

								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// 执行完毕后给handler发送一个空消息

						}
					}.start();

				}

			});
		}
		return view;
	}

	private class ItemHolder {
		TextView content;
		TextView date;
		TextView count;
		ImageView like;
	}
}
