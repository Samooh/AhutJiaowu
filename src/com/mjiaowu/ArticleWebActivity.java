package com.mjiaowu;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ArticleWebActivity extends Activity {
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.articlewebview);
		webview=(WebView)findViewById(R.id.articlewebview);
		StringBuilder sb=new StringBuilder();
		sb.append("<div>Test</div>");
		sb.append("<p>Test dfgdf dfgdf dfgf dgfdsgkldfsjgkldfsj dfsgkldf dfsgjkldfg dfgjdfklg dfgjdfsklg fdgkjdfslgj dgdfskjlglk dfgjdfklsg</p>");
		sb.append("<img src='http://pubimage.360doc.com/newsite/logoo.gif'/>");
		sb.append("<p>Test dfgdf dfgdf dfgf dgfdsgkldfsjgkldfsj dfsgkldf dfsgjkldfg dfgjdfklg dfgjdfsklg fdgkjdfslgj dgdfskjlglk dfgjdfklsg</p>");
		sb.append("<p>Test dfgdf dfgdf dfgf dgfdsgkldfsjgkldfsj dfsgkldf dfsgjkldfg dfgjdfklg dfgjdfsklg fdgkjdfslgj dgdfskjlglk dfgjdfklsg</p>");
		sb.append("<img src='http://pubimage.360doc.com/newsite/logoo.gif'/>");
		sb.append("<p>Test dfgdf dfgdf dfgf dgfdsgkldfsjgkldfsj dfsgkldf dfsgjkldfg dfgjdfklg dfgjdfsklg fdgkjdfslgj dgdfskjlglk dfgjdfklsg</p>");
		sb.append("<img src='http://pubimage.360doc.com/newsite/logoo.gif'/>");
		webview.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
	}

}
