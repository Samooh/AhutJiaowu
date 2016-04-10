package com.mjiaowu;

import java.io.File;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.entity.base.BaseActivity;

public class CalendarActivity extends BaseActivity {

	private ImageView mback;
	private ImageView calendar;
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;
	private float oldDist;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private Bitmap CalendarImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		mback = (ImageView) findViewById(R.id.calendar_back);
		calendar = (ImageView) findViewById(R.id.calendar);
		CalendarImage = getDiskBitmap(sharePre.getString("filepath", "no")+ "/calendar.jpg");
		if (CalendarImage != null) {
			calendar.setImageBitmap(CalendarImage);
			calendar.setOnTouchListener(new ImageView.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					ImageView view = (ImageView) v;
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						savedMatrix.set(matrix);
						start.set(event.getX(), event.getY());
						mode = DRAG;
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_POINTER_UP:
						mode = NONE;
						break;
					case MotionEvent.ACTION_POINTER_DOWN:
						oldDist = spacing(event);
						if (oldDist > 10f) {
							savedMatrix.set(matrix);
							midPoint(mid, event);
							mode = ZOOM;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (mode == DRAG) {
							matrix.set(savedMatrix);
							matrix.postTranslate(event.getX() - start.x,
									event.getY() - start.y);
						} else if (mode == ZOOM) {
							float newDist = spacing(event);
							if (newDist > 10f) {
								matrix.set(savedMatrix);
								float scale = newDist / oldDist;
								matrix.postScale(scale, scale, mid.x, mid.y);
							}
						}
						break;
					}

					view.setImageMatrix(matrix);
					return true;
				}

				@SuppressLint("FloatMath")
				private float spacing(MotionEvent event) {
					float x = event.getX(0) - event.getX(1);
					float y = event.getY(0) - event.getY(1);
					return FloatMath.sqrt(x * x + y * y);
				}

				private void midPoint(PointF point, MotionEvent event) {
					float x = event.getX(0) + event.getX(1);
					float y = event.getY(0) + event.getY(1);
					point.set(x / 2, y / 2);
				}
			});
		}
		mback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

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
