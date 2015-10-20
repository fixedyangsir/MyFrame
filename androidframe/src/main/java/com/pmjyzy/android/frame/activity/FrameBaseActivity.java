package com.pmjyzy.android.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.pmjyzy.android.frame.R;
import com.pmjyzy.android.frame.adapter.AdapterCallback;
import com.pmjyzy.android.frame.http.HttpHelper.HttpHelperCallBack;
import com.pmjyzy.android.frame.utils.AppManager;
import com.pmjyzy.android.frame.utils.ScreenUtils;

import java.util.List;

/**
 * 我的一个activty，快速构造一些方法；
 * 
 * @author HrcmChan
 * 
 */



public abstract class FrameBaseActivity extends FragmentActivity implements

		AdapterCallback, HttpHelperCallBack {

	protected Context mContext;

	protected boolean hasAnimiation = true; // 启动页面时是否带启动动画

	/** 是否初始化请求网络数据 */
	protected boolean isInitRequestData = false;

	protected BaseFragment currentFragment;

	private List<BaseFragment> fragments;

	protected LinearLayout ll_error_group;
	protected LinearLayout ll_error;
	protected View loadingView;
	protected RelativeLayout loadingContent;
	
	protected View dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// 管理Activity
		AppManager.getInstance().addActivity(this);
		int layoutId = getLayoutId();
		if (layoutId != 0) {
			setContentView(layoutId);
		}
	
		// 设置是否是初始化网络操作
		isInitRequestData = setIsInitRequestData();

		loadingView = LayoutInflater.from(this).inflate(
				R.layout.frame_yzypmj_loading_layout, null);
		loadingContent=(RelativeLayout) loadingView.findViewById(R.id.frame_rl_loading_content);
		
		
		dialog = LayoutInflater.from(this).inflate(
				R.layout.frame_yzypmj_customprogressbar_layout, null);

		ll_error_group = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.frame_yzypmj_error_layout, null);

		ll_error = (LinearLayout) ll_error_group.findViewById(R.id.ll_error);
		// 添加错误界面
		FrameLayout rootFrameLayout = (FrameLayout) findViewById(android.R.id.content);
		LayoutParams layoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		ll_error_group.setLayoutParams(layoutParams);
		ll_error_group.setVisibility(View.VISIBLE);
		rootFrameLayout.addView(ll_error_group);
		rootFrameLayout.addView(loadingView);
		rootFrameLayout.addView(dialog);
		// 让点击事件不穿透
		ll_error.setClickable(true);
		loadingView.setClickable(true);
		dialog.setClickable(true);

		Button btn_resh = (Button) ll_error_group.findViewById(R.id.btn_resh);
		btn_resh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isInitRequestData = true;
				requestData();
			}
		});

		ll_error_group.setVisibility(LinearLayout.GONE);
		loadingView.setVisibility(View.GONE);
		dialog.setVisibility(View.GONE);
		
		initViews();
		initData();
		initListener();
		requestData();

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 点击空白隐藏键盘
		InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null) {
				if (getCurrentFocus().getWindowToken() != null) {
					mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dialog != null) {
			dialog = null;
		}
		AppManager.getInstance().killActivity(this);
	}

	/**
	 * 设置activity title与主界面距离风格
	 * 
	 * @param dimens
	 *            title高度
	 * @param
	 * 
	 */
	public void setAppThemeSpace(int dimens) {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				ScreenUtils.getScreenWidth(this),
				(ScreenUtils.getScreenHeight(this))
						- (int) getResources().getDimension(dimens));
		layoutParams.topMargin = (int) getResources().getDimension(dimens);
		loadingContent.setLayoutParams(layoutParams);
	}

	/**
	 * 布局文件ID
	 * 
	 * @return
	 */
	public abstract int getLayoutId();



	/**
	 * 初始化view
	 */
	public abstract void initViews();

	/**
	 * 初始化数据
	 */
	public abstract void initData();

	/**
	 * 请求网络初始化数据
	 */
	public abstract void requestData();

	/**
	 * 是否是初始化数据操作
	 */
	public abstract boolean setIsInitRequestData();

	

	/**
	 * 初始化监听
	 */
	public abstract void initListener();

	/**
	 * view的点击事件
	 */
	public abstract void btnClick(View view);

	/**
	 * 获取context
	 * 
	 * @return
	 */
	public Context getContext() {
		return mContext;
	}
	

	// ===================一些进度框以及提示信息==========================
	public void showLoadingDialog() {
		if (dialog!=null) {
			
			dialog.setVisibility(View.VISIBLE);
		}
	}

	public void dismissLoadingDialog() {
		if (dialog!=null) {
			
			dialog.setVisibility(View.GONE);
		}
	}

	/**
	 * 全屏显示的进度界面，用来遮挡背后的模拟数据 //使用需在调取网络之前
	 */
	public void showLoadingContent() {
		if (loadingView!=null) {
			
			loadingView.setVisibility(View.VISIBLE);
		}
		
	}

	/**
	 * 隐藏全屏显示的进度框
	 */
	public void dismissLoadingContent() {
		if (loadingView!=null) {
			
			loadingView.setVisibility(View.GONE);
		}
	}

	protected void showdialog(String message) {

	}

	// ============================== 启动Activity ==============================

	/**
	 * 启动一个Activity
	 * 
	 * @param className
	 *            将要启动的Activity的类名
	 * @param options
	 *            传到将要启动Activity的Bundle，不传时为null
	 */
	public void startActivity(Class<?> className, Bundle options) {
		Intent intent = new Intent(this, className);
		if (options != null) {
			intent.putExtras(options);
		}
		startActivity(intent);
		if (hasAnimiation) {
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);

		}
	}
	

	/**
	 * 启动一个有会返回值的Activity
	 * 
	 * @param className
	 *            将要启动的Activity的类名
	 * @param options
	 *            传到将要启动Activity的Bundle，不传时为null
	 * @param requestCode
	 *            请求码
	 */
	public void startActivityForResult(Class<?> className, Bundle options,
			int requestCode) {
		Intent intent = new Intent(this, className);
		if (options != null) {
			intent.putExtras(options);
		}
		startActivityForResult(intent, requestCode);
		if (hasAnimiation) {
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);

		}
	}

	@Override
	public void finish() {
		super.finish();
		if (hasAnimiation) {
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_right_out);
		}
	}

	public abstract void showToast(String message);
	


	// ==================adapter与acitivity沟通==========================
	@Override
	public void adapterstartActivity(Class<?> className, Bundle options) {
		startActivity(className, options);
	}

	@Override
	public void adapterInfotoActiity(Object data) {

	}
	@Override
	public void adapterInfotoActiity(Object data,int what){
		
	}
	
	// ==============================联网之后的回调==================
	@Override
	public void onHttpSuccessResponse(Object info, int what) {
		loadingView.setVisibility(View.GONE);
		ll_error_group.setVisibility(LinearLayout.GONE);
		isInitRequestData = false;
		dismissLoadingDialog();
		dismissLoadingContent();

	}

	@Override
	public void onHttpSuccessHaveExceptionResponse(String result, int what) {
		loadingView.setVisibility(View.GONE);
		ll_error_group.setVisibility(LinearLayout.GONE);
		isInitRequestData = false;
		dismissLoadingDialog();
		dismissLoadingContent();

	};

	@Override
	public void onHttpErrorResponse(VolleyError error, int what) {
		dismissLoadingContent();
		if (isInitRequestData) {
			ll_error_group.setVisibility(LinearLayout.VISIBLE);
		}

		dismissLoadingDialog();
		showToast(getResources().getString(R.string.error_net_tip));

	}

	@Override
	public void onHttpFailResponse(Object info, int what) {
		dismissLoadingContent();
		ll_error_group.setVisibility(LinearLayout.GONE);
		isInitRequestData = false;
		dismissLoadingDialog();
	}

	// =========================================================================
	/**
	 * 通过控件的Id获取对于的控件
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {

		return (T) findViewById(viewId);
	}
}
