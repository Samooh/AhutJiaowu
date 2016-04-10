package com.ahutjw.api;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.NoticeDetailBean;
import com.ahutjw.app.entity.NoticeListBean;
import com.ahutjw.utils.D;

/**
 * 通知公告
 * 
 * 
 */
public class ApiNotice {

	// 通知公告--列表内容
	public static final String URL_LIST = "getEducationalNotice.asmx/getEducationalNoticeList";
	public static final String PARAM_LIST = "page";
	// 通知公告--详细内容
	public static final String URL_DETAIL = "getEducationalNotice.asmx/getEducationalNoticeDetail";
	public static final String PARAM_DETAIL = "id";

	/**
	 * 通知公告===列表
	 * 
	 * @param page
	 * @return
	 */
	public static List<NoticeListBean> queryNoticeList(String page) {
		String url = URL_LIST;
		// 返回结果
		final List<NoticeListBean> items = new ArrayList<NoticeListBean>();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { PARAM_LIST },
					new String[] { page });
			D.out(">>>queryNoticeList>>>" + ret);

			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				NoticeListBean item = null;

				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase("EducationalNotice")) {
						item = new NoticeListBean();
					} else if (tagName.equalsIgnoreCase("title")) {
						txt = xmlParser.nextText();
						item.setTitle(txt);
					} else if (tagName.equalsIgnoreCase("dtmPublish")) {
						txt = xmlParser.nextText();
						item.setDtmPublish(txt);
					} else if (tagName.equalsIgnoreCase("id")) {
						txt = xmlParser.nextText();
						item.setId(txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (xmlParser.getName().equalsIgnoreCase(
							"EducationalNotice")
							&& item != null) {
						items.add(item);
						item = null;
					}
				}
			});
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryNoticeList.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * 通知公告===详细
	 * 
	 * @param id
	 * @return
	 */
	public static NoticeDetailBean queryNoticeDetail(String id) {
		String url = URL_DETAIL;
		// 返回结果
		final NoticeDetailBean item = new NoticeDetailBean();
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { PARAM_DETAIL },
					new String[] { id });
			D.out(">>>queryNoticeDetail>>>" + ret);

			// 解析xml文件
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					String txt = "";
					if (tagName.equalsIgnoreCase("title")) {
						txt = xmlParser.nextText();
						item.setTitle(txt);
					} else if (tagName.equalsIgnoreCase("dtmPublish")) {
						txt = xmlParser.nextText();
						item.setDtmPublish(txt);
					} else if (tagName.equalsIgnoreCase("seeNumber")) {
						txt = xmlParser.nextText();
						item.setSeeNumber(txt);
					} else if (tagName.equalsIgnoreCase("content")) {
						txt = xmlParser.nextText();
						item.setContent(txt);
					}
				}

				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
				}
			});

		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>queryNoticeDetail.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return item;
	}
}
