package com.mjiaowu;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import com.ahutjw.api.ApiBaseHttp;
import com.ahutjw.api.ApiBaseXml;
import com.ahutjw.api.ApiBaseXml.OnXmlCallback;
import com.ahutjw.app.entity.ArticleBean;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.S;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleDetailActivity extends BaseRequestActivity {
	private TextView title, date, articleabstract;
	private WebView content;
	private ImageView image, back;
	private String imageurl;
	private Button getcomment;
	private ImageView addcomment;
	private EditText mycomment;
	private String articleId;
	private String comment;
	private Bitmap newBitimage = null;
	private ArticleBean articlebean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.articledetail);
		articleId = getIntent().getStringExtra("ID");
		title = (TextView) findViewById(R.id.title);
		date = (TextView) findViewById(R.id.date);
		content = (WebView) findViewById(R.id.content);
		articleabstract = (TextView) findViewById(R.id.articleabstract);
		getcomment = (Button) findViewById(R.id.getcomment);
		addcomment = (ImageView) findViewById(R.id.addcomment);
		mycomment = (EditText) findViewById(R.id.mycomment);

		image = (ImageView) findViewById(R.id.image);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});

		getcomment.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putString("articleId", articleId);
				openActivity(CommentListActivity.class, b);
			}

		});

		addcomment.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				comment = mycomment.getText().toString();
				if (comment.equals("") || comment == null) {
					showCustomToast("请输入评论内容");
					return;
				} else {
					if (sharePre.getBoolean(S.LOGIN_IS, false)) {
						new Thread() {
							@Override
							public void run() {
								// 你要执行的方法
								try {
									String url = "http://211.70.149.139:8099/AHUTYDJWService.asmx/addComment";
									System.out.println(url);
									String result = ApiBaseHttp.DoPost(
											url,
											new String[] { "articleId",
													"Reviewer", "password",
													"Content" },
											new String[] {
													articleId,
													sharePre.getString(
															S.LOGIN_USERNAME,
															""),
													sharePre.getString(
															S.LOGIN_PWD, ""),
													comment });

									System.out.println(result);
									String[] results = result.split(">");
									if (results[2].replace("</int", "").equals(
											"1")) {
										Message message = new Message();
										message.what = 1;
										handler.sendMessage(message);

									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// 执行完毕后给handler发送一个空消息

							}
						}.start();

					} else {
						showCustomToast("请登录后评论");
					}
				}
			}

		});
		new Thread() {
			@Override
			public void run() {
				// 你要执行的方法
				try {
					String url = "http://211.70.149.139:8099/AHUTYDJWService.asmx/getArticleDetail";
					System.out.println(url);
					String result = ApiBaseHttp.DoPost(url,
							new String[] { "Id" }, new String[] { articleId });
					System.out.println(result);
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
						public void onTagEnd(String tagName,
								XmlPullParser xmlParser) throws Exception {
							if (xmlParser.getName().equalsIgnoreCase("Article")
									&& item != null) {
								articlebean = item;
								Message message = new Message();
								message.what = 4;
								handler.sendMessage(message);
							}
						}
					});

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 执行完毕后给handler发送一个空消息
			}
		}.start();

		title.setText(getIntent().getStringExtra("Title"));
		date.setText(getIntent().getStringExtra("Date"));
		articleabstract.setText("摘要:" + getIntent().getStringExtra("Abstract"));
		imageurl = getIntent().getStringExtra("imageUrl");
		// ConnectivityManager con = (ConnectivityManager)
		// getSystemService(Activity.CONNECTIVITY_SERVICE);
		/*
		 * boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
		 * .isConnectedOrConnecting(); boolean internet =
		 * con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
		 * .isConnectedOrConnecting();
		 */
		new Thread() {
			@Override
			public void run() {
				// 你要执行的方法
				try {
					newBitimage = getPicture("http://211.70.149.139:5013"
							+ imageurl);
					System.out.println(newBitimage);
					if (newBitimage != null) {
						Message message = new Message();
						message.what = 2;
						handler.sendMessage(message);
					} else {
						Message message = new Message();
						message.what = 3;
						handler.sendMessage(message);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 执行完毕后给handler发送一个空消息

			}
		}.start();
		// 执行相关操作

	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int val = msg.what;
			if (val == 1) {
				showCustomToast("评论成功");
				mycomment.setText("");
			} else if (val == 2) {
				System.out.println("hi");
				image.setImageBitmap(newBitimage);
			} else if (val == 3) {

				image.setVisibility(View.GONE);
			} else if (val == 4) {
				content.loadDataWithBaseURL(null, articlebean.getContent()
						.toString(), "text/html", "utf-8", null);
			}
		}
	};
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO: http request.
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("value", "请求结果");
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};

	private Bitmap getPicture(String pictureurl) throws IOException {
		URL url = new URL(pictureurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream in = conn.getInputStream();
		Bitmap bm = BitmapFactory.decodeStream(in);
		// 保存本地图片
		in.close();
		System.out.println(">>>>>>>图片获取成功");
		Message message = new Message();
		message.what = 2;
		handler.sendMessage(message);
		return bm;
	}

	@Override
	public String onReqestPre() {
		// TODO Auto-generated method stub
		return "正在请求";
	}

	@Override
	public Object onReqestDoing(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onReqestFinish(Object result) {
		// TODO Auto-generated method stub

	}

}
