package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.CommentBean;

public class ApiComment {
	public static List<CommentBean> queryArticleCommentList(String articleId,
			String page, String user) {
		String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/getComment";
		final List<CommentBean> list = new ArrayList<CommentBean>();
		String result;
		try {
			result = ApiBaseHttp.DoPost(url, new String[] { "articleId",
					"page", "user" }, new String[] { articleId, page, user });
			System.out.println(result);
			try {
				ApiBaseXml.parseXml(result, new OnXmlCallback() {
					CommentBean item = null;

					@Override
					public void onTagStart(String tagName,
							XmlPullParser xmlParser) throws Exception {
						// 一条记录
						String txt = "";
						if (tagName.equalsIgnoreCase("Comment")) {
							item = new CommentBean();
						} else if (tagName.equalsIgnoreCase("ID")) {
							txt = xmlParser.nextText();
							item.setID(txt);
						} else if (tagName.equalsIgnoreCase("articleId")) {
							txt = xmlParser.nextText();
							item.setArticleId(txt);
						} else if (tagName.equalsIgnoreCase("Reviewer")) {
							txt = xmlParser.nextText();
							item.setReviewer(txt);
						} else if (tagName.equalsIgnoreCase("Content")) {
							txt = xmlParser.nextText();
							item.setContent(txt);
						} else if (tagName.equalsIgnoreCase("Date")) {
							txt = xmlParser.nextText();
							item.setDate(txt);
						} else if (tagName.equalsIgnoreCase("Count")) {
							txt = xmlParser.nextText();
							item.setCount(txt);
						} else if (tagName.equalsIgnoreCase("Like")) {
							txt = xmlParser.nextText();
							item.setLike(txt);
						}
					}

					@Override
					public void onTagEnd(String tagName, XmlPullParser xmlParser)
							throws Exception {
						if (xmlParser.getName().equalsIgnoreCase("Comment")
								&& item != null) {
							list.add(item);
							item = null;
						}
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}
}
