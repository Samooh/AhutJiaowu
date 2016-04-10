package com.mjiaowu;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.ahutjw.api.ApiBaseHttp;
import com.ahutjw.api.ApiNotice;
import com.ahutjw.entity.base.BaseRequestActivity;
import com.ahutjw.utils.TestPopupWindow;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

public class MapLocationActivity extends BaseRequestActivity {

	/*
	 * private Button getlocation; private TextView longitude; private TextView
	 * latitude;
	 */
	private ImageView back;
	private String deviceID, deviceType, wifiInfo;
	MapView mMapView = null;
	private Button mylocation;
	private BaiduMap mBaiduMap;
	private Button maptype;
	private int intmaptype = 1;
	private double lat, lng;
	// private Button showmore;
	private boolean isshow = true;
	private ImageView menu;
	private TestPopupWindow menuWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);
		mMapView = (MapView) findViewById(R.id.bmapView);
		/*
		 * getlocation = (Button) findViewById(R.id.getlocation); longitude =
		 * (TextView) findViewById(R.id.longitude); latitude = (TextView)
		 * findViewById(R.id.latitude);
		 */
		back = (ImageView) findViewById(R.id.back);
		mylocation = (Button) findViewById(R.id.mylocation);
		maptype = (Button) findViewById(R.id.maptype);
		// showmore = (Button) findViewById(R.id.showmore);
		menu = (ImageView) findViewById(R.id.menu);

		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		TelephonyManager mTm = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		deviceType = android.os.Build.MODEL; // 手机型号
		deviceID = mTm.getDeviceId();
		System.out.println(deviceType + deviceID);
		getwifiInfo();
		openGPSSettings();
		LatLng ll = new LatLng(31.679353, 118.560867);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 17);
		mBaiduMap.animateMapStatus(u);
		initLocation();
		menu.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menuWindow = new TestPopupWindow(MapLocationActivity.this, null);

				// 显示窗口
				int h = MapLocationActivity.this.getWindowManager().getDefaultDisplay()
						.getHeight();
				menuWindow.showAtLocation(MapLocationActivity.this.findViewById(R.id.menu),
						Gravity.TOP | Gravity.RIGHT, 10, h / 8); // 设置layout在PopupWindow中显示的位置
				menuWindow.getContentView().findViewById(R.id.menumylocation)
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String serviceString = Context.LOCATION_SERVICE;
								LocationManager locationManager = (LocationManager) getSystemService(serviceString);
								String provider = LocationManager.GPS_PROVIDER;
								Location location = locationManager
										.getLastKnownLocation(provider);
								getLocationInfo(location);
								locationManager.requestLocationUpdates(
										provider, 2000, 0, locationListener);

							}
						});
				menuWindow.getContentView().findViewById(R.id.menumaptype)
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								TextView maptype = (TextView) menuWindow
										.getContentView().findViewById(
												R.id.menumaptype);
								if (intmaptype == 0) {
									mBaiduMap
											.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
									intmaptype = 1;
									maptype.setText("普通地图");
								} else {
									mBaiduMap
											.setMapType(BaiduMap.MAP_TYPE_NORMAL);
									intmaptype = 0;
									maptype.setText("卫星地图");
								}

							}
						});
				menuWindow.getContentView().findViewById(R.id.menushowmore)
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								TextView showmore = (TextView) menuWindow
										.getContentView().findViewById(
												R.id.menushowmore);
								if (isshow) {
									mBaiduMap.clear();
									isshow = false;
									showmore.setText("显示导引");
								} else {
									initLocation();
									isshow = true;
									showmore.setText("隐藏导引");
								}
							}
						});
			}
		});

		mylocation.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String serviceString = Context.LOCATION_SERVICE;
				LocationManager locationManager = (LocationManager) getSystemService(serviceString);
				String provider = LocationManager.GPS_PROVIDER;
				Location location = locationManager
						.getLastKnownLocation(provider);
				getLocationInfo(location);
				locationManager.requestLocationUpdates(provider, 2000, 0,
						locationListener);

			}
		});
		maptype.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (intmaptype == 0) {
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
					intmaptype = 1;
					maptype.setText("普通地图");
				} else {
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
					intmaptype = 0;
					maptype.setText("卫星地图");
				}

			}
		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				// 在此处理点击事件
				// 构建Marker图标
				mBaiduMap.clear();
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.publisher_poi_button_on);
				// 构建MarkerOption，用于在地图上添加Marker
				OverlayOptions option = new MarkerOptions().position(point)
						.icon(bitmap);
				// 在地图上添加Marker，并显示
				mBaiduMap.addOverlay(option);
				// 定义文字所显示的坐标点
				// 构建文字Option对象，用于在地图上添加文字
				OverlayOptions textOption = new TextOptions()
						.bgColor(0xAAFFFF00)
						.fontSize(24)
						.fontColor(0xFFFF00FF)
						.text("经度:" + point.longitude + "\n\n纬度:"
								+ point.latitude).rotate(0).position(point);
				// 在地图上添加该文字对象并显示
				mBaiduMap.addOverlay(textOption);
			}

			public boolean onMapPoiClick(MapPoi poi) {
				// 在此处理底图标注点击事件

				return false;
			}
		});

		back.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		/*
		 * getlocation.setOnClickListener(new Button.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub String serviceString = Context.LOCATION_SERVICE;
		 * LocationManager locationManager = (LocationManager)
		 * getSystemService(serviceString); String provider =
		 * LocationManager.GPS_PROVIDER; Location location = locationManager
		 * .getLastKnownLocation(provider); getLocationInfo(location);
		 * locationManager.requestLocationUpdates(provider, 2000, 0,
		 * locationListener); }
		 * 
		 * });
		 */

	}

	public void initLocation() {
		LatLng point = new LatLng(31.679353, 118.560867);
		OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00)
				.fontSize(15).fontColor(0xFFFF00FF).text("图书馆").rotate(0)
				.position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.678201, 118.563966);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("东区教学楼").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.677732, 118.561316);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("逸夫楼").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.682080, 118.563032);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("F#学生公寓").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.681711, 118.563032);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("E#学生公寓").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.679783, 118.558424);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("D栋教学楼").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.68258, 118.562897);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("第九食堂").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.680851, 118.560211);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("大学生活动中心").rotate(0)
				.position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.681627, 118.560211);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("第八食堂").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.680767, 118.563265);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("第六食堂").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.681181, 118.563005);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("第一澡堂").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.677317, 118.558972);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("电器楼").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.680513, 118.558406);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("机械楼").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.681089, 118.558406);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("建工楼").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.678078, 118.564766);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("阶梯教室").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.679069, 118.555711);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("运动场").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.683786, 118.560364);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("校医院").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.681888, 118.559951);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("第二澡堂").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.678654, 118.566113);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("体育馆").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.679100, 118.565628);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("工创中心").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.679507, 118.564864);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("鸟笼篮球场").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.680636, 118.561334);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("A栋学生公寓").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.681005, 118.561334);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("B栋学生公寓").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);
		point = new LatLng(31.678278, 118.556825);
		textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(15)
				.fontColor(0xFFFF00FF).text("东区篮球场").rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		mBaiduMap.addOverlay(textOption);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		JPushInterface.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		JPushInterface.onPause(this);

	}

	private void getwifiInfo() {
		String wserviceName = Context.WIFI_SERVICE;
		WifiManager wm = (WifiManager) getSystemService(wserviceName);
		List<ScanResult> results = wm.getScanResults();
		wifiInfo = "";
		if (results != null) {
			for (ScanResult result : results) {
				/*
				 * otherwifi += result.SSID + ":\n接入地址:" + result.BSSID +
				 * "\n信号强度:" + result.level + "\n热点频率:" + result.frequency +
				 * "\n\n";
				 */
				wifiInfo += result.BSSID + " " + result.level + " ";
			}
		}
	}

	private void getLocationInfo(Location location) {
		if (location != null) {
			lat = location.getLatitude() + 0.004150;
			lng = location.getLongitude() + 0.011500;
			/*
			 * longitude.setText("经度:" + lng); latitude.setText("纬度:" + lat);
			 */
			LatLng point = new LatLng(lat, lng);
			// 构建Marker图标
			BitmapDescriptor bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.publisher_poi_button_on);
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option = new MarkerOptions().position(point).icon(
					bitmap);
			// 在地图上添加Marker，并显示
			mBaiduMap.addOverlay(option);
			// 定义文字所显示的坐标点
			/*
			 * LatLng llText = new LatLng(lat, lng); // 构建文字Option对象，用于在地图上添加文字
			 * OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00)
			 * .fontSize(24).fontColor(0xFFFF00FF).text("我的位置").rotate(0)
			 * .position(llText); // 在地图上添加该文字对象并显示
			 * mBaiduMap.addOverlay(textOption);
			 */
			LatLng ll = new LatLng(lat, lng);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String url = "http://211.70.149.139:8990/AHUTYDJWService.asmx/SaveWifiInfo";
					String result = "";
					try {
						result = ApiBaseHttp.DoPost(url, new String[] {
								"deviceID", "deviceType", "longitude",
								"latitude", "wifiInfo" }, new String[] {
								deviceID, deviceType, lng + "", lat + "",
								wifiInfo });
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(result);
				}
			};
			new Thread(runnable).start();

		}
	}

	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			getLocationInfo(null);

		}

		@Override
		public void onProviderDisabled(String provider) {
			getLocationInfo(null);
		}

		@Override
		public void onLocationChanged(Location location) {
			getLocationInfo(location);
		}
	};

	private void openGPSSettings() {
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			// Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
			return;
		}

		Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面

	}

	public void Refresh() {
	}

	@Override
	public String onReqestPre() {

		return "正在请求中...";
	}

	// 请求通知
	@Override
	public Object onReqestDoing(String... params) {

		return ApiNotice.queryNoticeList(params[0]);
	}

	// 请求完成
	@Override
	public void onReqestFinish(Object result) {

	}

}