package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.ArticleBean;

public class ApiArticle {
	public static List<ArticleBean> queryArticlelist(String page) {
		String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/getArticleTitle";
		final List<ArticleBean> list = new ArrayList<ArticleBean>();
		String result;
		try {
			result = ApiBaseHttp.DoPost(url, new String[] { "page" },
					new String[] { page });
			System.out.println(result);

			try {
				ApiBaseXml.parseXml(result, new OnXmlCallback() {
					ArticleBean item = null;

					@Override
					public void onTagStart(String tagName,
							XmlPullParser xmlParser) throws Exception {
						// 一条记录
						String txt = "";
						if (tagName.equalsIgnoreCase("Article")) {
							item = new ArticleBean();
						} else if (tagName.equalsIgnoreCase("Id")) {
							txt = xmlParser.nextText();
							item.setId(txt);
						} else if (tagName.equalsIgnoreCase("Title")) {
							txt = xmlParser.nextText();
							item.setTitle(txt);
						} else if (tagName.equalsIgnoreCase("Abstract")) {
							txt = xmlParser.nextText();
							item.setAbstract(txt);
						} else if (tagName.equalsIgnoreCase("Content")) {
							txt = xmlParser.nextText();
							item.setContent(txt);
						} else if (tagName.equalsIgnoreCase("imageUrl")) {
							txt = xmlParser.nextText();
							item.setImageUrl(txt);
						} else if (tagName.equalsIgnoreCase("Count")) {
							txt = xmlParser.nextText();
							item.setCount(txt);
						} else if (tagName.equalsIgnoreCase("Date")) {
							txt = xmlParser.nextText();
							item.setDate(txt);
						} else if (tagName.equalsIgnoreCase("Other")) {
							txt = xmlParser.nextText();
							item.setOther(txt);
						}
					}

					@Override
					public void onTagEnd(String tagName, XmlPullParser xmlParser)
							throws Exception {
						if (xmlParser.getName().equalsIgnoreCase("Article")
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
