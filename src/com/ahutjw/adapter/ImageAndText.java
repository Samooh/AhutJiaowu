package com.ahutjw.adapter;

public class ImageAndText {
	private String imageUrl;
	private String title;
	private String articleabstract;

	public ImageAndText(String imageUrl, String title,String articleabstract) {
		this.imageUrl = imageUrl;
		this.title = title;
		this.articleabstract = articleabstract;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getTitle() {
		return title;
	}
	public String getAbstract() {
		return articleabstract;
	}
}
