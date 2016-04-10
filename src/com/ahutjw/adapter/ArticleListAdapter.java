package com.ahutjw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahutjw.app.entity.ArticleBean;
import com.mjiaowu.R;

public class ArticleListAdapter extends BaseAdapter {
	private Context context;
	private List<ArticleBean> datas;

	public ArticleListAdapter(Context context, List<ArticleBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public ArticleBean getItem(int position) {
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
			view = inflater.inflate(R.layout.articlelist_item, null);
			//
			holder.titleView = (TextView) view.findViewById(R.id.title);
			holder.subtitleView = (TextView) view
					.findViewById(R.id.articleabstract);
			//holder.image = (ImageView) view.findViewById(R.id.articleimage);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		ArticleBean bean = getItem(position);
		holder.titleView.setText(bean.getTitle());
		holder.subtitleView.setText("     " + bean.getAbstract());
		/*if (!bean.getImageUrl().equals("")) {
			try {
				System.out.println(bean.getImageUrl());
				Bitmap newBitimage = null;
				newBitimage = getPicture("http://211.70.149.139:8990/image/article/"
						+ bean.getImageUrl());
				holder.image.setImageBitmap(newBitimage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			holder.image.setImageResource(R.drawable.ahutimage1);
		}*/
		return view;
	}

	/*private Bitmap getPicture(String pictureurl) throws IOException {
		URL url = new URL(pictureurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream in = conn.getInputStream();
		Bitmap bm = BitmapFactory.decodeStream(in);
		// 保存本地图片
		in.close();
		System.out.println(">>>>>>>图片获取成功");
		return bm;
	}*/

	private class ItemHolder {
		TextView titleView;
		TextView subtitleView;
		//ImageView image;
	}
}
