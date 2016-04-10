package com.mjiaowu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiArticle;
import com.ahutjw.api.ApiBaseHttp;
import com.ahutjw.app.entity.ArticleBean;
import com.ahutjw.db.DbManager;
import com.ahutjw.entity.base.BaseSlidingFragmentActivity;
import com.ahutjw.slidingmenu.SlidingMenu;
import com.ahutjw.utils.ConfirmDialog;
import com.ahutjw.utils.HandyTextView;
import com.ahutjw.utils.S;
import com.ahutjw.utils.SelectPicPopupWindow;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseSlidingFragmentActivity implements
		OnClickListener, AnimationListener {
	public static boolean isForeground = false;
	private int keyBackClickCount = 0;
	private DbManager dbM;
	private TextView articletitle;
	private String[] articletitles;
	private List<Bitmap> articleimages;
	private float lastX = 0;
	private float lastY = 0;
	private List<ArticleBean> datas;
	private View title;
	private LinearLayout mlinear_listview;
	private ImageView imgMore;
	private FrameLayout mFrameTv;
	private ImageView mImgTv;

	// views
	private ViewPager mViewPager;
	private LinearLayout loadLayout;

	private LinearLayout llGoHome;
	private Button bn_refresh;

	// private TextView mAboveTitle;
	private SlidingMenu sm;
	private boolean mIsTitleHide = false;
	private boolean mIsAnim = false;

	@SuppressWarnings("unused")
	private String current_page;

	@SuppressWarnings("unused")
	private InputMethodManager imm;

	private boolean isShowPopupWindows = false;

	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow; // 弹出框

	private ImageView noticelist, clsrsearch, todaycourse, examinfo;

	private ImageView topersoninfo, tosetting;
	private TextView texttomy, texttosetting;

	public Timer timer = new Timer();
	private int imagecount = 2;
	// private LinearLayout ahutimagelayout;

	private ImageView imageviewmore;
	private ImageView focuse1, focuse2, focuse3, focuse4;
	private ImageView imageview_setmore;

	private ImageView menupersoni, menunewsi, menuclsri, menucleni, menuabouti;
	private TextView menupersont, menunewst, menuclsrt, menuclent, menuaboutt,menulocation;
	private ImageView exiti;
	private Button exitt;
	private TextView textnews, textclsrsearch, texttoday, textexaminfo;

	// private ImageView infoaboutme;
	private ImageView articleimage;

	private TextView title1, abstract1, title2, abstract2;
	private TextView getmorearticle;
	private ImageView nextarticle, prevarticle;
	public static MainActivity mactivity;
	private boolean isloadarticle = false;

	private final String widgetupdate = "com.wd.appWidgetUpdate";
	private LinearLayout ImageLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initSlidingMenu();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.above_slidingmenu);
		initControl();

		sharePre.setString("filepath", MainActivity.this.getCacheDir()
				.toString());

		initgoHome();
		// JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);

		texttomy.setOnClickListener(new onClickListener());
		texttosetting.setOnClickListener(new onClickListener());

		noticelist.setOnClickListener(new onClickListener());
		clsrsearch.setOnClickListener(new onClickListener());
		todaycourse.setOnClickListener(new onClickListener());
		examinfo.setOnClickListener(new onClickListener());
		topersoninfo.setOnClickListener(new onClickListener());
		tosetting.setOnClickListener(new onClickListener());

		menupersoni.setOnClickListener(new onClickListener());
		menunewsi.setOnClickListener(new onClickListener());
		menuclsri.setOnClickListener(new onClickListener());
		menucleni.setOnClickListener(new onClickListener());
		menuabouti.setOnClickListener(new onClickListener());

		menupersont.setOnClickListener(new onClickListener());
		menunewst.setOnClickListener(new onClickListener());
		menuclsrt.setOnClickListener(new onClickListener());
		menuclent.setOnClickListener(new onClickListener());
		menuaboutt.setOnClickListener(new onClickListener());
		menulocation.setOnClickListener(new onClickListener());

		textnews.setOnClickListener(new onClickListener());
		textclsrsearch.setOnClickListener(new onClickListener());
		texttoday.setOnClickListener(new onClickListener());
		textexaminfo.setOnClickListener(new onClickListener());

		exiti.setOnClickListener(new onClickListener());
		exitt.setOnClickListener(new onClickListener());

		getmorearticle.setOnClickListener(new onClickListener());
		title1.setOnClickListener(new onClickListener());
		abstract1.setOnClickListener(new onClickListener());
		title2.setOnClickListener(new onClickListener());
		abstract2.setOnClickListener(new onClickListener());
		articleimage.setOnClickListener(new onClickListener());
		articletitle.setOnClickListener(new onClickListener());

		nextarticle.setOnClickListener(new onClickListener());
		prevarticle.setOnClickListener(new onClickListener());

		imageviewmore.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});
		ImageLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					x1 = (int) event.getX();
					y1 = (int) event.getY();
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					int y;
					x2 = (int) event.getX();
					y2 = (int) event.getY();
					y = (y1 - y2);
					if (y < 0) {
						y = -y;
					}
					if (x2 - x1 > 50 && y < 60) {
						switch (imagecount) {
						case 1:
							articleimage.setImageBitmap(articleimages.get(0));
							articletitle.setText(articletitles[0]);
							focuse1.setImageResource(R.drawable.onfocuseimage);
							focuse4.setImageResource(R.drawable.unfocuseimage);
							imagecount++;
							break;
						case 2:
							articleimage.setImageBitmap(articleimages.get(1));
							articletitle.setText(articletitles[1]);
							focuse2.setImageResource(R.drawable.onfocuseimage);
							focuse1.setImageResource(R.drawable.unfocuseimage);
							imagecount++;
							break;
						case 3:
							articleimage.setImageBitmap(articleimages.get(2));
							articletitle.setText(articletitles[2]);
							focuse3.setImageResource(R.drawable.onfocuseimage);
							focuse2.setImageResource(R.drawable.unfocuseimage);
							imagecount++;
							break;
						case 4:
							articleimage.setImageBitmap(articleimages.get(3));
							articletitle.setText(articletitles[3]);
							focuse4.setImageResource(R.drawable.onfocuseimage);
							focuse3.setImageResource(R.drawable.unfocuseimage);
							imagecount++;
							break;
						}
						if (imagecount == 5) {
							imagecount = 1;
						}
					} else if (x2 - x1 > 50 && y < 60) {
						switch (imagecount) {
						case 1:
							articleimage.setImageBitmap(articleimages.get(2));
							articletitle.setText(articletitles[2]);
							focuse3.setImageResource(R.drawable.onfocuseimage);
							focuse4.setImageResource(R.drawable.unfocuseimage);
							imagecount--;
							break;
						case 2:
							articleimage.setImageBitmap(articleimages.get(3));
							articletitle.setText(articletitles[3]);
							focuse4.setImageResource(R.drawable.onfocuseimage);
							focuse1.setImageResource(R.drawable.unfocuseimage);
							imagecount--;
							break;
						case 3:
							articleimage.setImageBitmap(articleimages.get(0));
							articletitle.setText(articletitles[0]);
							focuse1.setImageResource(R.drawable.onfocuseimage);
							focuse2.setImageResource(R.drawable.unfocuseimage);
							imagecount--;
							break;
						case 4:
							articleimage.setImageBitmap(articleimages.get(1));
							articletitle.setText(articletitles[1]);
							focuse2.setImageResource(R.drawable.onfocuseimage);
							focuse3.setImageResource(R.drawable.unfocuseimage);
							imagecount--;
							break;
						}
						if (imagecount == 0) {
							imagecount = 4;
						}
					}
				}
				return false;
			}
		});
		/*
		 * infoaboutme.setOnClickListener(new ImageView.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new Intent();
		 * // TODO Auto-generated method stub if
		 * (sharePre.getString(S.LOGIN_TYPE, "").equals("教师")) {
		 * intent.putExtra("way", "1"); intent.setClass(getApplicationContext(),
		 * NoteTeaListActivity.class); startActivity(intent); } else {
		 * intent.putExtra("way", "5"); intent.setClass(getApplicationContext(),
		 * NoteTeaListActivity.class); startActivity(intent); } }
		 * 
		 * });
		 */
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String newname = ApiBaseHttp
							.get("http://211.70.149.139:8099/AHUTYDJWService.asmx/GetStartImage");
					String imagename = newname.split("<")[5].replaceAll(
							"Image>", "");
					String oldname = sharePre.getString("startimage", "none");
					File file = new File(sharePre.getString("filepath", "no")
							+ "/hello.jpg");

					if (!oldname.equals(imagename) || !file.exists()) {
						System.out.println(">>>>>>>>>>get new image:"
								+ imagename);
						Bitmap newBitimage = getPicture("http://211.70.149.139:5013"
								+ imagename);
						saveBitmap(newBitimage, "hello.jpg");
					}
					sharePre.setString("startimage", imagename);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		new Thread(runnable).start();
		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String newname = ApiBaseHttp
							.get("http://211.70.149.139:8099/AHUTYDJWService.asmx/GetCalendarImage");
					String imagename = newname.split("<")[5].replaceAll(
							"Image>", "");
					String oldname = sharePre.getString("calendarimage", "none");
					File file = new File(sharePre.getString("filepath", "no")
							+ "/calendar.jpg");

					if (!oldname.equals(imagename) || !file.exists()) {
						System.out.println(">>>>>>>>>>get new image:"
								+ imagename);
						Bitmap newBitimage = getPicture("http://211.70.149.139:5013"
								+ imagename);
						saveBitmap(newBitimage, "calendar.jpg");
					}
					sharePre.setString("calendarimage", imagename);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		new Thread(runnable1).start();

		imageview_setmore = (ImageView) findViewById(R.id.imageview_above_more);
		imageview_setmore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				uploadImage(MainActivity.this);
			}
		});

		Runnable runnableimage = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(5000);
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		};

		articletitles = new String[] { "", "", "", "" };
		articleimages = new ArrayList<Bitmap>();
		Resources res = getResources();
		articleimages.add(BitmapFactory.decodeResource(res,
				R.drawable.ahutimage1));
		articleimages.add(BitmapFactory.decodeResource(res,
				R.drawable.ahutimage2));
		articleimages.add(BitmapFactory.decodeResource(res,
				R.drawable.ahutimage3));
		articleimages.add(BitmapFactory.decodeResource(res,
				R.drawable.ahutimage4));
		datas = new ArrayList<ArticleBean>();
		new Thread(runnableimage).start();
		dbM = new DbManager(this);
		sendRequest();

		// JPushInterface.setDebugMode(true);
		JPushInterface.init(MainActivity.this);

		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);

	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				switch (imagecount) {
				case 1:
					articleimage.setImageBitmap(articleimages.get(0));
					articletitle.setText(articletitles[0]);
					focuse1.setImageResource(R.drawable.onfocuseimage);
					focuse4.setImageResource(R.drawable.unfocuseimage);
					imagecount++;
					break;
				case 2:
					articleimage.setImageBitmap(articleimages.get(1));
					articletitle.setText(articletitles[1]);
					focuse2.setImageResource(R.drawable.onfocuseimage);
					focuse1.setImageResource(R.drawable.unfocuseimage);
					imagecount++;
					break;
				case 3:
					articleimage.setImageBitmap(articleimages.get(2));
					articletitle.setText(articletitles[2]);
					focuse3.setImageResource(R.drawable.onfocuseimage);
					focuse2.setImageResource(R.drawable.unfocuseimage);
					imagecount++;
					break;
				case 4:
					articleimage.setImageBitmap(articleimages.get(3));
					articletitle.setText(articletitles[3]);
					focuse4.setImageResource(R.drawable.onfocuseimage);
					focuse3.setImageResource(R.drawable.unfocuseimage);
					imagecount++;
					break;

				}
			} else if (msg.what == 2) {
				for (int i = 0; i < 4; i++) {
					try {
						Bitmap newBitimage = getPicture("211.70.149.139:8099/image/article/"
								+ datas.get(i).getImageUrl());
						articleimages.set(i, newBitimage);
						saveBitmap(newBitimage, "image" + i + ".jpg");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else if (msg.what == 3) {

				for (int i = 0; i < 4; i++) {
					Bitmap bitmapimage = getDiskBitmap(sharePre.getString(
							"filepath", "no") + "/image" + i + ".jpg");
					if (bitmapimage != null) {
						articleimages.set(i, bitmapimage);
					}
				}

			}
			if (imagecount == 5) {
				imagecount = 1;
			}
			super.handleMessage(msg);
		}
	};
	private int x1, x2, y1, y2;

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
		return bm;
	}

	public void saveBitmap(Bitmap bm, String name) {
		String path = sharePre.getString("filepath", "no");
		File f = new File(path, "/" + name);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			System.out.println(">>>>>>>图片保存成功");
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 初始化滑动菜 单
	private void initSlidingMenu() {
		// 隐藏菜单
		setBehindContentView(R.layout.behind_menu);
		// customize the SlidingMenu
		sm = getSlidingMenu();
		// 设置滑动阴影
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// sm.setFadeDegree(0.35f);
		// 设置屏幕触摸的模式
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
		// sm.setShadowWidth(20);
		sm.setBehindScrollScale(0);
	}

	// 获取控件
	@SuppressLint("CutPasteId")
	private void initControl() {

		imm = (InputMethodManager) getApplicationContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		ImageLayout = (LinearLayout) findViewById(R.id.ahutimagelayout);
		loadLayout = (LinearLayout) findViewById(R.id.view_loading);
		imgMore = (ImageView) findViewById(R.id.imageview_above_more);
		imgMore.setOnClickListener((android.view.View.OnClickListener) this);
		mViewPager = (ViewPager) findViewById(R.id.above_pager);
		// lvTitle = (ListView) findViewById(R.id.behind_list_show);
		llGoHome = (LinearLayout) findViewById(R.id.Linear_above_toHome);

		title = findViewById(R.id.main_title);
		mlinear_listview = (LinearLayout) findViewById(R.id.main_linear_listview);
		mFrameTv = (FrameLayout) findViewById(R.id.fl_off);
		mImgTv = (ImageView) findViewById(R.id.iv_off);
		loadLayout.setVisibility(View.GONE);
		bn_refresh = (Button) findViewById(R.id.bn_refresh);
		bn_refresh.setOnClickListener(this);

		textnews = (TextView) findViewById(R.id.textnews);
		textclsrsearch = (TextView) findViewById(R.id.textclsrsearch);
		texttoday = (TextView) findViewById(R.id.texttoday);
		textexaminfo = (TextView) findViewById(R.id.textexaminfo);

		texttomy = (TextView) findViewById(R.id.texttomy);
		texttosetting = (TextView) findViewById(R.id.texttosetting);

		noticelist = (ImageView) findViewById(R.id.imageView5);
		clsrsearch = (ImageView) findViewById(R.id.imageView6);
		todaycourse = (ImageView) findViewById(R.id.imageView7);
		examinfo = (ImageView) findViewById(R.id.imageView8);
		imageviewmore = (ImageView) findViewById(R.id.imageview_above_more);

		topersoninfo = (ImageView) findViewById(R.id.my);

		tosetting = (ImageView) findViewById(R.id.setting);

		// ahutimagelayout = (LinearLayout) findViewById(R.id.ahutimagelayout);

		focuse1 = (ImageView) findViewById(R.id.focuse1);
		focuse2 = (ImageView) findViewById(R.id.focuse2);
		focuse3 = (ImageView) findViewById(R.id.focuse3);
		focuse4 = (ImageView) findViewById(R.id.focuse4);

		menupersoni = (ImageView) findViewById(R.id.menupersoni);
		menunewsi = (ImageView) findViewById(R.id.menunewsi);
		menuclsri = (ImageView) findViewById(R.id.menuclsri);
		menucleni = (ImageView) findViewById(R.id.menucleni);
		menuabouti = (ImageView) findViewById(R.id.menuabouti);
		exiti = (ImageView) findViewById(R.id.exiti);

		menupersont = (TextView) findViewById(R.id.menupersont);
		menunewst = (TextView) findViewById(R.id.menunewst);
		menuclsrt = (TextView) findViewById(R.id.menuclsrt);
		menuclent = (TextView) findViewById(R.id.menuclent);
		menuaboutt = (TextView) findViewById(R.id.menuaboutt);
		menulocation = (TextView) findViewById(R.id.mylocation);
		exitt = (Button) findViewById(R.id.exitt);

		// infoaboutme = (ImageView) findViewById(R.id.infoaboutme);
		articleimage = (ImageView) findViewById(R.id.articleimage);

		title1 = (TextView) findViewById(R.id.title1);
		abstract1 = (TextView) findViewById(R.id.abstract1);
		title2 = (TextView) findViewById(R.id.title2);
		abstract2 = (TextView) findViewById(R.id.abstract2);
		articletitle = (TextView) findViewById(R.id.articletitle);

		getmorearticle = (TextView) findViewById(R.id.getmorearticle);

		nextarticle = (ImageView) findViewById(R.id.nextarticle);
		prevarticle = (ImageView) findViewById(R.id.prevarticle);
	}

	private void initgoHome() {
		llGoHome.setOnClickListener(this);
	}

	// 用于反馈 关于 个人中心
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Linear_above_toHome:
			showMenu();
			break;
		case R.id.login_login:
			// 进入登陆界面
			startActivity(new Intent(this, LoginActivity.class));
			break;
		case R.id.imageview_above_more:
			// 关于
			if (isShowPopupWindows) {
				// new PopupWindowUtil(mViewPager).showActionWindow(v,
				// this,mBasePageAdapter.tabs);
			}
			break;

		}

	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
		JPushInterface.onResume(this);
		keyBackClickCount = 0;
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
		JPushInterface.onPause(this);
	}

	// 初始化返回次数

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				// 再按一次退出
				showCustomToast(getResources().getString(
						R.string.press_again_exit));
				Timer timer = new Timer();
				// 三秒后将back赋值0
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						keyBackClickCount = 0;
					}
				}, 3000);
				break;
			case 1:
				// 退出程序
				mFrameTv.setVisibility(View.VISIBLE);
				mImgTv.setVisibility(View.VISIBLE);
				// 定义退出动画
				Animation anim = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.tv_off);
				anim.setAnimationListener(new tvOffAnimListener());
				mImgTv.startAnimation(anim);
				finish();
				// 播放退出声音
				// MediaPlayer mMediaPlayer = MediaPlayer.create(this,
				// R.raw.exit);
				// mMediaPlayer.start();
				break;
			default:
				break;
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 显示菜单
			if (sm.isMenuShowing()) {
				toggle();
			} else {
				// showMenu();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		super.dispatchTouchEvent(event);
		if (mIsAnim || mViewPager.getChildCount() <= 1) {
			return false;
		}
		final int action = event.getAction();

		float x = event.getX();
		float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			lastX = x;
			return false;
		case MotionEvent.ACTION_MOVE:
			float dY = Math.abs(y - lastY);
			float dX = Math.abs(x - lastX);
			boolean down = y > lastY ? true : false;
			lastY = y;
			lastX = x;
			// 标题显示
			if (dX < 8 && dY > 8 && !mIsTitleHide && !down) {
				Animation anim = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.push_top_in);
				// anim.setFillAfter(true);
				anim.setAnimationListener(MainActivity.this);
				title.startAnimation(anim);
			} else if (dX < 8 && dY > 8 && mIsTitleHide && down) {
				// 不显示标题
				Animation anim = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.push_top_out);
				// anim.setFillAfter(true);
				anim.setAnimationListener(MainActivity.this);
				title.startAnimation(anim);
			} else {
				return false;
			}
			mIsTitleHide = !mIsTitleHide;
			mIsAnim = true;
			break;
		default:
			return false;
		}
		return false;
	}

	// 设置menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 动画监听
	class tvOffAnimListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			defaultFinish();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (mIsTitleHide) {
			title.setVisibility(View.GONE);
		} else {

		}
		mIsAnim = false;

	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public void onAnimationStart(Animation animation) {

		title.setVisibility(View.VISIBLE);
		if (mIsTitleHide) {
			FrameLayout.LayoutParams lp = (LayoutParams) mlinear_listview
					.getLayoutParams();
			lp.setMargins(0, 0, 0, 0);
			mlinear_listview.setLayoutParams(lp);
		} else {
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) title
					.getLayoutParams();
			lp.setMargins(0, 0, 0, 0);
			title.setLayoutParams(lp);
			FrameLayout.LayoutParams lp1 = (LayoutParams) mlinear_listview
					.getLayoutParams();
			lp1.setMargins(0,
					getResources().getDimensionPixelSize(R.dimen.title_height),
					0, 0);
			mlinear_listview.setLayoutParams(lp1);
		}
	}

	// destory并关闭数据
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 选择框
	 * 
	 * @param title
	 * @param message
	 * @param okButtonCallBack
	 */
	public void confirm(ConfirmDialog.OnButtonClickCallback okButtonCallBack) {
		ConfirmDialog dialog = new ConfirmDialog(this);
		dialog.setOkButtonCallBack(okButtonCallBack);
		dialog.show();
	}

	public boolean valid() {
		boolean bo = sharePre.getBoolean(S.LOGIN_IS, false);
		// boolean bo = false;
		if (!bo) {
			confirm(new ConfirmDialog.OnButtonClickCallback() {
				@Override
				public void onCallback(View v) {
					// 转到登陆界面
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							LoginActivity.class);
					startActivity(intent);
				}
			});
		}
		return bo;
	}

	public void uploadImage(final Activity context) {
		menuWindow = new SelectPicPopupWindow(MainActivity.this, null);

		// 显示窗口
		/*
		 * int h = MainActivity.this.getWindowManager().getDefaultDisplay()
		 * .getHeight();
		 */
		menuWindow.showAsDropDown(imageview_setmore, 50, 0); // 设置layout在PopupWindow中显示的位置
		if (!sharePre.getBoolean(S.LOGIN_IS, false)) {
			TextView login = (TextView) menuWindow.getContentView()
					.findViewById(R.id.menuoutlogin);
			login.setText("登录");
		}
		menuWindow.getContentView().findViewById(R.id.menuperson_info)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						menuWindow.dismiss();
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, LoginActivity.class);
						startActivity(intent);
					}
				});

		menuWindow.getContentView().findViewById(R.id.menuoutlogin)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						menuWindow.dismiss();
						if (!sharePre.getBoolean(S.LOGIN_IS, false)) {
							Intent intent = new Intent();
							intent.setClass(MainActivity.this,
									LoginActivity.class);
							startActivity(intent);
						} else {
							sharePre.setBoolean(S.LOGIN_IS, false);
							Intent intent1 = new Intent();
							intent1.putExtra("mark", "1");
							intent1.setAction(widgetupdate);
							sendBroadcast(intent1);
							showCustomToast("您已经退出当前账户");
							sharePre.setString(S.LOGIN_USERNAME, "");
							sharePre.setString(S.LOGIN_PWD, "");
							dbM.deleteAll(DbManager.course.TABLE_NAME);
							dbM.deleteAll(DbManager.exam.TABLE_NAME);
							dbM.deleteAll(DbManager.remark.TABLE_NAME);
							TextView login = (TextView) menuWindow.getContentView().findViewById(R.id.menuoutlogin);
							login.setText("登录");
						}
					}
				});

		menuWindow.getContentView().findViewById(R.id.menumynote)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						menuWindow.dismiss();
						// TODO Auto-generated method stub
						if (sharePre.getString(S.LOGIN_TYPE, "").equals("教师")) {
							Intent intent = new Intent();
							intent.putExtra("way", "1");
							intent.setClass(getApplicationContext(),
									NoteTeaListActivity.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent();
							intent.putExtra("way", "5");
							intent.setClass(getApplicationContext(),
									NoteTeaListActivity.class);
							startActivity(intent);
						}

					}
				});
		menuWindow.getContentView().findViewById(R.id.menuabout)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						menuWindow.dismiss();
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, AboutActivity.class);
						startActivity(intent);
					}
				});

	}

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(MainActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	@Override
	public String onReqestPre() {
		return "正在请求...";

	}

	// 请求通知
	@Override
	public Object onReqestDoing(String... params) {
		return ApiArticle.queryArticlelist("1");

	}

	// 请求完成
	@Override
	public void onReqestFinish(Object result) {
		@SuppressWarnings("unchecked")
		List<ArticleBean> temps = (List<ArticleBean>) result;
		if (temps.size() > 0) {
			dbM.deleteAll(DbManager.article.TABLE_NAME);
			System.out.println("正在缓存第一页");
			for (int i = 0; i < temps.size(); i++) {
				ArticleBean bean = temps.get(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DbManager.article.COLUMN_ID, (i + 1) + "");
				map.put(DbManager.article.COLUMNID, bean.getId());
				map.put(DbManager.article.COLUMN_TITLE, bean.getTitle());
				map.put(DbManager.article.COLUMN_ABSTRACT, bean.getAbstract());
				map.put(DbManager.article.COLUMN_CONTENT, bean.getContent());
				map.put(DbManager.article.COLUMN_IMAGEURL, bean.getImageUrl());
				map.put(DbManager.article.COLUMN_COUNT, bean.getCount());
				map.put(DbManager.article.COLUMN_DATE, bean.getDate());
				map.put(DbManager.article.COLUMN_OTHER, bean.getOther());
				SQLiteDatabase db = openOrCreateDatabase(
						"com.mjiaowu.temp_datas.db", 0, null);
				Cursor c = db.rawQuery(
						"SELECT count(*) FROM sqlite_master WHERE type='table' AND name='"
								+ DbManager.article.TABLE_NAME + "'", null);
				if (c.moveToNext()) {
					if (c.getInt(0) > 0) {
						dbM.insert(DbManager.article.TABLE_NAME,
								DbManager.article.columns, map);
					}
				}
			}
			System.out.println("缓存成功");
			if (temps.size() >= 6) {
				datas.addAll(temps);
				title1.setText(datas.get(4).getTitle());
				abstract1.setText("     " + datas.get(4).getAbstract());
				title2.setText(datas.get(5).getTitle());
				abstract2.setText("     " + datas.get(5).getAbstract());

				articletitles[0] = datas.get(0).getTitle();
				articletitles[1] = datas.get(1).getTitle();
				articletitles[2] = datas.get(2).getTitle();
				articletitles[3] = datas.get(3).getTitle();
				if (!temps.get(0).getId()
						.equals(sharePre.getString("articelId", "none"))) {
					sharePre.setString("articelId", temps.get(0).getId());
					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							for (int i = 0; i < 4; i++) {
								try {
									Bitmap newBitimage = getPicture("http://211.70.149.139:5013"
											+ datas.get(i).getImageUrl());
									// articleimages.set(i, newBitimage);
									saveBitmap(newBitimage, "image" + i
											+ ".jpg");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							Message message = new Message();
							message.what = 3;
							handler.sendMessage(message);
						}
					};
					new Thread(runnable).start();
				} else {
					File f = new File(sharePre.getString("filepath", "no")
							+ "/image0" + ".jpg");
					if (!f.exists()) {
						Runnable runnable = new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								for (int i = 0; i < 4; i++) {
									try {
										Bitmap newBitimage = getPicture("http://211.70.149.139:5013"
												+ datas.get(i).getImageUrl());
										// articleimages.set(i, newBitimage);
										saveBitmap(newBitimage, "image" + i
												+ ".jpg");
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Message message = new Message();
									message.what = 3;
									handler.sendMessage(message);
								}
							}
						};
						new Thread(runnable).start();
					} else {
						Message message = new Message();
						message.what = 3;
						handler.sendMessage(message);
					}
				}
				isloadarticle = true;
			}

		} else {
			System.out.println("加载第一页");
			SQLiteDatabase db = openOrCreateDatabase(
					"com.mjiaowu.temp_datas.db", 0, null);
			Cursor c = db.rawQuery(
					"SELECT count(*) FROM sqlite_master WHERE type='table' AND name='"
							+ DbManager.article.TABLE_NAME + "'", null);
			if (c.moveToNext()) {
				if (c.getInt(0) > 0) {
					ArrayList<HashMap<String, String>> datasLst = dbM.query(
							DbManager.article.TABLE_NAME,
							DbManager.article.columns);
					System.out.println("Length=" + datasLst.size());
					for (int i = 0; i < datasLst.size(); i++) {
						HashMap<String, String> map = datasLst.get(i);
						ArticleBean temp = new ArticleBean();
						temp.setTitle(map.get(DbManager.article.COLUMN_TITLE));
						System.out.println(map
								.get(DbManager.article.COLUMN_TITLE));
						temp.setId(map.get(DbManager.article.COLUMNID));
						temp.setAbstract(map
								.get(DbManager.article.COLUMN_ABSTRACT));
						temp.setContent(map
								.get(DbManager.article.COLUMN_CONTENT));
						temp.setCount(map.get(DbManager.article.COLUMN_COUNT));
						temp.setDate(map.get(DbManager.article.COLUMN_DATE));
						temp.setImageUrl(map
								.get(DbManager.article.COLUMN_IMAGEURL));
						temp.setOther(map.get(DbManager.article.COLUMN_OTHER));
						temps.add(temp);
					}
					System.out.println("Length temp=" + temps.size());
					if (temps.size() >= 6) {
						datas.addAll(temps);
						title1.setText(datas.get(4).getTitle());
						abstract1.setText("     " + datas.get(4).getAbstract());
						title2.setText(datas.get(5).getTitle());
						abstract2.setText("     " + datas.get(5).getAbstract());
						try {

						} catch (Exception e) {
							Toast.makeText(this, "Something must be wrong...",
									Toast.LENGTH_SHORT).show();
						}
						articletitles[0] = datas.get(0).getTitle();
						articletitles[1] = datas.get(1).getTitle();
						articletitles[2] = datas.get(2).getTitle();
						articletitles[3] = datas.get(3).getTitle();
						Message message = new Message();
						message.what = 3;
						handler.sendMessage(message);
						isloadarticle = true;
					}
				}
			}
		}

	}

	public class onClickListener implements View.OnClickListener {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			// TODO Auto-generated method stub

			if (v.getId() == R.id.texttomy) {
				Bundle bundle = new Bundle();
				openActivity(PersonInfoActivity.class, bundle);

			} else if (v.getId() == R.id.texttosetting) {
				Bundle bundle = new Bundle();
				openActivity(SettingActivity.class, bundle);
			} else if (v.getId() == R.id.imageView5
					|| v.getId() == R.id.menunewsi
					|| v.getId() == R.id.menunewst
					|| v.getId() == R.id.textnews) {
				intent.setClass(getApplicationContext(),
						NoticeListActivity.class);
				MainActivity.this.startActivity(intent);
			} else if (v.getId() == R.id.imageView6
					|| v.getId() == R.id.menuclsri
					|| v.getId() == R.id.menuclsrt
					|| v.getId() == R.id.textclsrsearch) {
				intent.setClass(getApplicationContext(),
						ClassroomSearchActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.imageView7
					|| v.getId() == R.id.texttoday) {
				String xn, xq;
				SimpleDateFormat sdfy = new SimpleDateFormat("yyyy");
				int year = Integer.parseInt(sdfy.format(new java.util.Date()));
				SimpleDateFormat sdfm = new SimpleDateFormat("MM");
				int month = Integer.parseInt(sdfm.format(new java.util.Date()));
				if (month >= 7) {
					xn = year + "-" + (year + 1);
					xq = "1";
				} else if (month == 1) {
					xn = (year - 1) + "-" + year;
					xq = "1";
				} else {
					xn = (year - 1) + "-" + year;
					xq = "2";
				}
				String week = "";
				int weekint = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
				System.out.println(">>>>>>>>>>>>>>>>>>>>" + weekint);
				switch (weekint) {
				case 1:
					week = "周一";
					break;
				case 2:
					week = "周二";
					break;
				case 3:
					week = "周三";
					break;
				case 4:
					week = "周四";
					break;
				case 5:
					week = "周五";
					break;
				case 6:
					week = "周六";
					break;
				case 0:
					week = "周日";
					break;
				}
				Bundle b = new Bundle();
				b.putString("xn", xn);
				b.putString("xq", xq);
				intent.putExtras(b);
				intent.putExtra("time", week);
				if (sharePre.getString(S.LOGIN_TYPE, "").equals("教师")) {
					intent.setClass(getApplicationContext(),
							CourseExcelTeaActivity.class);
					startActivity(intent);
				} else {

					intent.setClass(getApplicationContext(),
							CourseExcelActivity.class);
					startActivity(intent);
				}
			} else if (v.getId() == R.id.imageView8
					|| v.getId() == R.id.textexaminfo) {
				intent.setClass(getApplicationContext(), ExamActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.my) {
				Bundle bundle = new Bundle();
				openActivity(PersonInfoActivity.class, bundle);
			} else if (v.getId() == R.id.setting) {
				Bundle bundle = new Bundle();
				openActivity(SettingActivity.class, bundle);
			} else if (v.getId() == R.id.menupersoni
					|| v.getId() == R.id.menupersont) {
				intent.setClass(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.menucleni
					|| v.getId() == R.id.menuclent) {
				intent.setClass(getApplicationContext(), CalendarActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.mylocation) {
				intent.setClass(getApplicationContext(), MapLocationActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.getmorearticle) {
				intent.setClass(getApplicationContext(),
						ArticleListActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.title1 || v.getId() == R.id.abstract1) {
				if (isloadarticle) {
					Bundle bundle = new Bundle();
					bundle.putString("ID", datas.get(4).getId());
					bundle.putString("Title", datas.get(4).getTitle());
					bundle.putString("Date", datas.get(4).getDate());
					bundle.putString("Content", datas.get(4).getContent());
					bundle.putString("imageUrl", datas.get(4).getImageUrl());
					bundle.putString("Abstract", datas.get(4).getAbstract());
					openActivity(ArticleDetailActivity.class, bundle);
				}
			} else if (v.getId() == R.id.title2 || v.getId() == R.id.abstract2) {
				if (isloadarticle) {
					Bundle bundle = new Bundle();
					bundle.putString("ID", datas.get(5).getId());
					bundle.putString("Title", datas.get(5).getTitle());
					bundle.putString("Date", datas.get(5).getDate());
					bundle.putString("Content", datas.get(5).getContent());
					bundle.putString("imageUrl", datas.get(5).getImageUrl());
					bundle.putString("Abstract", datas.get(5).getAbstract());
					openActivity(ArticleDetailActivity.class, bundle);
				}
			} else if (v.getId() == R.id.articleimage
					|| v.getId() == R.id.articletitle) {
				int count;
				if (imagecount >= 2) {
					count = imagecount - 2;
				} else {
					count = 3;
				}
				if (isloadarticle) {
					Bundle bundle = new Bundle();
					bundle.putString("ID", datas.get(count).getId());
					bundle.putString("Title", datas.get(count).getTitle());
					bundle.putString("Date", datas.get(count).getDate());
					bundle.putString("Content", datas.get(count).getContent());
					bundle.putString("imageUrl", datas.get(count).getImageUrl());
					bundle.putString("Abstract", datas.get(count).getAbstract());
					openActivity(ArticleDetailActivity.class, bundle);
				}
			} else if (v.getId() == R.id.menuabouti
					|| v.getId() == R.id.menuaboutt) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), AboutActivity.class);
				startActivity(i);
			} else if (v.getId() == R.id.exiti || v.getId() == R.id.exitt) {
				toggle();
				mFrameTv.setVisibility(View.VISIBLE);
				mImgTv.setVisibility(View.VISIBLE);
				// 定义退出动画

				Animation anim = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.tv_off);
				anim.setAnimationListener(new tvOffAnimListener());
				mImgTv.startAnimation(anim);
				finish();
			} else if (v.getId() == R.id.nextarticle) {
				switch (imagecount) {
				case 1:
					articleimage.setImageBitmap(articleimages.get(0));
					articletitle.setText(articletitles[0]);
					focuse1.setImageResource(R.drawable.onfocuseimage);
					focuse4.setImageResource(R.drawable.unfocuseimage);
					imagecount++;
					break;
				case 2:
					articleimage.setImageBitmap(articleimages.get(1));
					articletitle.setText(articletitles[1]);
					focuse2.setImageResource(R.drawable.onfocuseimage);
					focuse1.setImageResource(R.drawable.unfocuseimage);
					imagecount++;
					break;
				case 3:
					articleimage.setImageBitmap(articleimages.get(2));
					articletitle.setText(articletitles[2]);
					focuse3.setImageResource(R.drawable.onfocuseimage);
					focuse2.setImageResource(R.drawable.unfocuseimage);
					imagecount++;
					break;
				case 4:
					articleimage.setImageBitmap(articleimages.get(3));
					articletitle.setText(articletitles[3]);
					focuse4.setImageResource(R.drawable.onfocuseimage);
					focuse3.setImageResource(R.drawable.unfocuseimage);
					imagecount++;
					break;
				}
				if (imagecount == 5) {
					imagecount = 1;
				}
			} else if (v.getId() == R.id.prevarticle) {
				switch (imagecount) {
				case 1:
					articleimage.setImageBitmap(articleimages.get(2));
					articletitle.setText(articletitles[2]);
					focuse3.setImageResource(R.drawable.onfocuseimage);
					focuse4.setImageResource(R.drawable.unfocuseimage);
					imagecount--;
					break;
				case 2:
					articleimage.setImageBitmap(articleimages.get(3));
					articletitle.setText(articletitles[3]);
					focuse4.setImageResource(R.drawable.onfocuseimage);
					focuse1.setImageResource(R.drawable.unfocuseimage);
					imagecount--;
					break;
				case 3:
					articleimage.setImageBitmap(articleimages.get(0));
					articletitle.setText(articletitles[0]);
					focuse1.setImageResource(R.drawable.onfocuseimage);
					focuse2.setImageResource(R.drawable.unfocuseimage);
					imagecount--;
					break;
				case 4:
					articleimage.setImageBitmap(articleimages.get(1));
					articletitle.setText(articletitles[1]);
					focuse2.setImageResource(R.drawable.onfocuseimage);
					focuse3.setImageResource(R.drawable.unfocuseimage);
					imagecount--;
					break;
				}
				if (imagecount == 0) {
					imagecount = 4;
				}

			}

		}
	}

}
