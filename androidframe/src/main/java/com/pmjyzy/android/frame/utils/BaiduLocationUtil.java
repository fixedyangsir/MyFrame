package com.pmjyzy.android.frame.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import com.pmjyzy.android.frame.config.Constants;

/**
 * 
 * 需要在manifest配置以下信息
 * 
 * 
 * <!-- 百度定位相关 --> <service android:name="com.baidu.location.f"
 * android:enabled="true" android:process=":remote" > </service>
 *
 * <meta-data android:name="com.baidu.lbsapi.API_KEY"
 * android:value="pVGQB93dVZGxChvOewO7oMeF" />
 * 
 * 
 * @author Administrator
 *
 */
public class BaiduLocationUtil {
	// 定位信息
	private Map<String, String> locationInfo;
	private LocationClient mLocationClient;

	private BaiduLBSLisener baiduLBSLisener;

	/**
	 * 开启百度定位<br/>
	 * 定位信息存在
	 * 
	 * @param baiduLBSLisener
	 *            定位成功回调监听，不需要时传null
	 */
	public void startBDLocation(Context context, BaiduLBSLisener baiduLBSLisener) {
		this.baiduLBSLisener = baiduLBSLisener;
		mLocationClient = new LocationClient(context.getApplicationContext());
		mLocationClient.registerLocationListener(new LocListener());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	/** 获取定位信息 */
	public Map<String, String> getLocationInfo() {
		return locationInfo;
	}

	/**
	 * 实位回调监听
	 * 
	 * @author Zero
	 *
	 *         2014年12月22日
	 */
	private class LocListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			if (arg0 == null)
				return;
			locationInfo = new HashMap<String, String>();
			locationInfo.put(Constants.LATITUDE,
					String.valueOf(arg0.getLatitude()));
			locationInfo.put(Constants.LONGITUDE,
					String.valueOf(arg0.getLongitude()));
			if (arg0.hasAddr()) {
				locationInfo.put(Constants.ADDRESS, arg0.getAddrStr());
				locationInfo.put(Constants.CITY, arg0.getCity());
				locationInfo.put(Constants.DISTRICT, arg0.getDistrict());
				locationInfo.put(Constants.FLOOR, arg0.getFloor());
				locationInfo.put(Constants.PROVINCE, arg0.getProvince());
				locationInfo.put(Constants.STREET, arg0.getStreet());
				locationInfo.put(Constants.STREET_NUMBER,
						arg0.getStreetNumber());
			}
			if (baiduLBSLisener != null)
				baiduLBSLisener.onSuccessLocation(locationInfo);
			mLocationClient.stop();
		}

	}

	public interface BaiduLBSLisener {

		public void onSuccessLocation(Map<String, String> locationInfo);
	}

}
