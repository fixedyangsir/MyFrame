package com.pmjyzy.android.frame.utils;

/**
 * 此工具来定位，获得经纬度
 * @author HrcmChan
 *
 */
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * 获得百度定位
 * 
 * @author panmingjie 2014.11.6
 */
public class MyLocationUtil {

	private double lat; // 纬度
	private double lon; // 经度
	private String address; //位置
	private MyLocationListener locationListener;
	private int errorCode;
	private LocationClient mLocationClient;
	public MyLocationUtil(Context context,MyLocationListener locationListener) {
		mLocationClient = new LocationClient(context.getApplicationContext());
		this.locationListener=locationListener;
		startLocation();
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}
	public String getAddress() {
		return address;
	}

	/**
	 * 访问网络，开始点位
	 */
	public void Getcoordinatr() {
		
		//设置定位参数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值bd09ll
		option.setScanSpan(2000);// 设置发起定位请求的间隔时间为2000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(listener);
		// mLocationClient.registerNotify(notifyListener);
		//启动定位服务
		mLocationClient.start();
		
		if (mLocationClient != null && mLocationClient.isStarted()) {
			//开启定位
			int i = mLocationClient.requestLocation();
			
		} else
			Log.i("result", "locClient is null or not started");
	}
	
	// 监听
	BDLocationListener listener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// 错误返回码
			errorCode = location.getLocType();
			lat = location.getLatitude();
			lon = location.getLongitude();
			address=location.getAddrStr();
		
		}
	};

	// 停止定位
	public void stopLocation() {
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}

	
	public void startLocation(){
		
		new Thread() {
			public void run() {
				// 用来判断是否定位成功
				boolean isgetD = true;
				while (isgetD) {
					try {
						Thread.sleep(1000);
						Getcoordinatr();
						if (getLat() > 0) {
							// 定位成功，就不需要继续执行
							isgetD = false;
							// 发送位置的消息
							locationListener.sussessLocation(getLat(), getLon());
							// 关闭定位
							stopLocation();
						}
						Log.i("result", "lat+lon:" + getLat()
								+ "------" + getLon());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			};

		}.start();
		
		
	}
	
	public interface MyLocationListener{
		
		
		public void sussessLocation(double lat, double lon);
		
		
		
	}
			
			
			
	
	
}
