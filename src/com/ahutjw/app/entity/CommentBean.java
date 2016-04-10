package com.ahutjw.app.entity;

public class CommentBean {
	private String ID;
	private String articleId;
	private String Reviewer;
	private String Content;
	private String Date;
	private String Count;
	private String Like;

	public String getLike() {
		return Like;
	}

	public void setLike(String like) {
		Like = like;
	}

	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getReviewer() {
		return Reviewer;
	}

	public void setReviewer(String reviewer) {
		Reviewer = reviewer;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

}
