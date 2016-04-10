package com.ahutjw.adapter;

import com.mjiaowu.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCache {
	private View baseView;
	private TextView articleabstract;
	private ImageView imageView;
	private TextView title;

	public ViewCache(View baseView) {
		this.baseView = baseView;
	}

	public TextView getTitle() {
		if (title == null) {
			title = (TextView) baseView.findViewById(R.id.title);
		}
		return title;
	}
	public TextView getAbstract() {
		if (articleabstract == null) {
			articleabstract = (TextView) baseView.findViewById(R.id.articleabstract);
		}
		return articleabstract;
	}

	public ImageView getImageView() {
		if (imageView == null) {
			imageView = (ImageView) baseView.findViewById(R.id.articleimage);
		}
		return imageView;
	}
}
