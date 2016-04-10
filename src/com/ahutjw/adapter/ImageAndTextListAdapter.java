package com.ahutjw.adapter;

import java.util.List;

import com.ahutjw.adapter.AsyncImageLoader.ImageCallback;
import com.ahutjw.app.entity.ArticleBean;
import com.mjiaowu.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ImageAndTextListAdapter extends ArrayAdapter<ArticleBean> {

	private ListView listView;
	private AsyncImageLoader asyncImageLoader;

	public ImageAndTextListAdapter(Activity activity, List<ArticleBean> datas,
			ListView listView) {
		super(activity, 0, datas);
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();

		// Inflate the views from XML
		View rowView = convertView;
		ViewCache viewCache;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.articlelist_item, null);
			viewCache = new ViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) rowView.getTag();
		}
		ArticleBean bean = getItem(position);

		// Load the image and set it on the ImageView
		String imageUrl = bean.getImageUrl();
		if (imageUrl != null) {
			ImageView imageView = viewCache.getImageView();
			imageView.setTag(imageUrl);
			Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl,
					new ImageCallback() {
						public void imageLoaded(Drawable imageDrawable,
								String imageUrl) {
							ImageView imageViewByTag = (ImageView) listView
									.findViewWithTag(imageUrl);
							if (imageViewByTag != null) {
								imageViewByTag.setImageDrawable(imageDrawable);
							}
						}
					});
			if (cachedImage == null) {
				imageView.setImageResource(R.drawable.ahutimage1);
			} else {
				imageView.setImageResource(R.drawable.ahutimage1);
				imageView.setImageDrawable(cachedImage);
			}
		}
		// Set the text on the TextView
		TextView title = viewCache.getTitle();
		title.setText(bean.getTitle());

		TextView articleabstract = viewCache.getAbstract();
		articleabstract.setText(bean.getAbstract());
		return rowView;
	}

}