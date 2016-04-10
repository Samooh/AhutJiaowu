package com.mjiaowu;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.entity.base.BaseActivity;

public class SplashActivity extends BaseActivity {

	private Handler mHandler = new Handler();
	private Bitmap bitmapimage;
	private ImageView startimage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.start_activity, null);
		setContentView(view);
		startimage = (ImageView) findViewById(R.id.imagestart);
		bitmapimage = getDiskBitmap(sharePre.getString("filepath", "no")
				+ "/hello.jpg");
		if (bitmapimage != null) {
			startimage.setImageBitmap(bitmapimage);
		}
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
		view.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						goHome();
					}
				}, 300);
			}
		});
		// UmengUpdateAgent.setUpdateOnlyWifi(false);
		// UmengUpdateAgent.update(this);

	}

	@Override
	public void onStart() {
		super.onStart();
		// MediaPlayer mPlayer= MediaPlayer.create(this, R.raw.welcome);
		// mPlayer.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	private void goHome() {
		openActivity(MainActivity.class);
		defaultFinish();
	};

	private Bitmap getDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return bitmap;
	}

}
