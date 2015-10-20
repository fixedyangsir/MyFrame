package com.pmjyzy.android.frame.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.webkit.WebSettings.TextSize;

/**
 * 提供添加map的参数的方法；
 * 
 * @author Administrator
 * 
 */
public class HttpParameterHelper {

	private Map<String, Object> map = null;

	public HttpParameterHelper() {

		map = new HashMap<String, Object>();

	}

	/**
	 * 
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public HttpParameterHelper addParameter(String key, String value) {

		map.put(key, value);

		return this;
	}

	/**
	 * 
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public HttpParameterHelper addObjectParameter(String key, Object object) {

		map.put(key, object);

		return this;
	}
	/**
	 * 获得object型的map
	 * @return
	 */
	public Map<String, Object> getMapByObjectParameters() {
		
			
		
		return map;
	}

	/**
	 * 获得string型的map
	 * @return
	 */
	public Map<String, String> getMapByStringParameters() {
		Map<String, String> map_String =new HashMap<String, String>();

		Set set = map.keySet();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {

			String key = (String) iterator.next();

			String value = (String) map.get(key);

			map_String.put(key, value);

		}

		return map_String;
	}

	public void clearParameters() {

		map.clear();

	}

}
