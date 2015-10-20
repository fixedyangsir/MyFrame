package com.pmjyzy.android.frame.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * volley的一个工具类
 * 
 * @author yzy
 * 
 */
public class HttpHelper {
	private RequestQueue mQueue;
	private StringRequest request;
	private ErrorListener errorListener;
	private Response.Listener<String> listener;
	private Context context;

	public HttpHelper(Context context) {
		this.context = context;
		mQueue = Volley.newRequestQueue(context);
	}

	/**
	 * 异步post获取网络数据
	 * 
	 * @param <T>
	 */
	public <T> void doPostAsyn(String url, final Map<String, String> param,
			final Class<T> clazz, final HttpHelperCallBack callBack,
			final int what) {

		newHttpListener(clazz, callBack, what);

		request = new StringRequest(Method.POST, url, listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				if (param != null && !param.isEmpty()) {
					return param;
				} else {
					return null;
				}

			}

		};

		mQueue.add(request);

	}

	private Map<String, File> files;
	private Map<String, String> params;

	/**
	 * post请求网络
	 * 
	 * @param <T>
	 * 
	 * @param url
	 * @param context
	 * @param param
	 * @param callBack
	 * @param what
	 */
	public <T> void doPostUrl(String url, final Map<String, Object> param,
			final Class<T> clazz, final HttpHelperCallBack callBack,
			final int what) {
		newHttpListener(clazz, callBack, what);

		files = new HashMap<String, File>();
		params = new HashMap<String, String>();

		if (param != null && !param.isEmpty()) {
			for (String key : param.keySet()) {
				Log.i("result", "++key++" + key);
				if (param.get(key) != null) {
					if (param.get(key) instanceof File) {
						Log.i("result", "==file==");
						files.put(key, (File) param.get(key));

					} else {
						Log.i("result", "==string==" + (String) param.get(key));
						params.put(key, (String) param.get(key));
					}
				}
			}
		}

		// 参数设置
		MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
				Request.Method.POST, url, listener, errorListener) {

			@Override
			public Map<String, File> getFileUploads() {
				return files;
			}

			@Override
			public Map<String, String> getStringUploads() {
				return params;
			}

		};

		mQueue = Volley.newRequestQueue(context,new MultiPartStack());

		mQueue.add(multiPartRequest);

	}

	/**
	 * 异步get获取网络数据
	 * 
	 * @param <T>
	 */
	public <T> void doGetAsyn(String url, Map<String, String> param,
			final Class<T> clazz, final HttpHelperCallBack callBack,
			final int what) {
		url = setUrl(url, param);
		newHttpListener(clazz, callBack, what);
		request = new StringRequest(Method.GET, url, listener, errorListener);
		mQueue.add(request);
	}

	public String setUrl(String url, Map<String, String> param) {
		if (param == null || param.isEmpty()) {
			return url;
		}
		StringBuffer sb = new StringBuffer(url);
		Iterator<String> it = param.keySet().iterator();
		sb.append("?");
		for (int i = 0; i < param.size(); i++) {
			String key = it.next();
			String value = param.get(key);
			if (i == (param.size() - 1)) {
				sb.append(key);
				sb.append("=");
				sb.append(value);
			} else {
				sb.append(key);
				sb.append("=");
				sb.append(value);
				sb.append("&");
			}
		}
		return sb.toString();
	}

	/**
	 * 初始化Volley网络监听Listener
	 * 
	 * @param clazz
	 * @param callBack
	 * @param what
	 */
	private <T> void newHttpListener(final Class<T> clazz,
			final HttpHelperCallBack callBack, final int what) {
		listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.i("result", response);
				Gson gson = new Gson();
				if (callBack != null) {
					// jsonObject数据请求
					if (response.startsWith("{") && response.endsWith("}")) {

						if (callBack.setIsJsonObjectFail(response)) {
							// jsonObject数据请求错误
							try {
								callBack.onHttpFailResponse(
										gson.fromJson(response,
												callBack.setJsonFailClass()),
										what);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								// jsonObject数据请求成功,json解析错误
								
							    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
							    e.printStackTrace(new PrintStream(baos));  
							    String exception = baos.toString();  
								
								Log.i("result", "json解析错误，请在onHttpSuccessHaveExceptionResponse获取,也有可能是其他错误，异常的原因"+ exception);
								callBack.onHttpSuccessHaveExceptionResponse(
										response, what);
							}
						} else {

							try {
								// jsonObject数据请求成功
								callBack.onHttpSuccessResponse(
										gson.fromJson(response, clazz), what);
								Log.i("result", "json解析成功");
							} catch (Exception e) {
								// jsonObject数据请求成功,json解析错误
								  ByteArrayOutputStream baos = new ByteArrayOutputStream();  
								    e.printStackTrace(new PrintStream(baos));  
								    String exception = baos.toString();  
									
								Log.i("result", "json解析错误，请在onHttpSuccessHaveExceptionResponse获取,也有可能是其他错误，异常的原因"+exception);
								callBack.onHttpSuccessHaveExceptionResponse(
										response, what);
							}

						}

					} else {
						// 解析jsonArray
						List<Object> list = new ArrayList<Object>();
						try {
							JsonParser parser = new JsonParser();
							JsonElement el = parser.parse(response);
							JsonArray jsonArray = null;
							if (el.isJsonArray()) {
								jsonArray = el.getAsJsonArray();
							}
							Iterator it = jsonArray.iterator();
							while (it.hasNext()) {
								JsonElement e = (JsonElement) it.next();
								// JsonElement转换为JavaBean对象
								list.add(gson.fromJson(e, clazz));
							}
							callBack.onHttpSuccessResponse(list, what);
						} catch (Exception e) {
							 ByteArrayOutputStream baos = new ByteArrayOutputStream();  
							    e.printStackTrace(new PrintStream(baos));  
							    String exception = baos.toString();  
							Log.i("result", "json解析错误，请在onHttpSuccessHaveExceptionResponse获取,也有可能是其他错误，异常的原因"+exception);
							callBack.onHttpSuccessHaveExceptionResponse(
									response, what);
						}

					}

				}
			}
		};

		errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (callBack != null) {
					callBack.onHttpErrorResponse(error, what);
				}
			}

		};
	}

	public interface HttpHelperCallBack {
		/**
		 * 数据请求成功,调用该方法
		 * 
		 * @param info
		 * @param what
		 */
		public void onHttpSuccessResponse(Object info, int what);

		/**
		 * 数据请求成功，但Json解析发生错误，需要自己做处理
		 * 
		 * @param result
		 * @param what
		 */
		public void onHttpSuccessHaveExceptionResponse(String result, int what);

		/**
		 * 请求发生错误，返回一个错误信息的类（如参数错误，或者密码错误等）
		 * 
		 * @param info
		 * @param what
		 */
		public void onHttpFailResponse(Object info, int what);

		/**
		 * 网络发生异常，调用该方法
		 * 
		 * @param error
		 * @param what
		 */
		public void onHttpErrorResponse(VolleyError error, int what);

		/**
		 * 根据接口返回的数据，设置数据请求失败的条件
		 * 
		 * @param respson
		 * @return
		 */
		abstract boolean setIsJsonObjectFail(String response);

		/**
		 * 根据接口返回的数据，设置数据失败返回的类
		 * 
		 * @param respson
		 * @return
		 */
		abstract <T> Class<T> setJsonFailClass();

	}

	/**
	 * 清除所有消息队列
	 */
	public void clearAll() {
		mQueue.cancelAll(request);
	}

}
