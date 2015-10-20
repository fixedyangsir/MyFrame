package com.pmjyzy.android.frame.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.pmjyzy.android.frame.R;
import com.pmjyzy.android.frame.adapter.AdapterCallback;
import com.pmjyzy.android.frame.http.HttpHelper.HttpHelperCallBack;
import com.pmjyzy.android.frame.utils.ScreenUtils;


/**
 * 主Fragment类，继承此类之后才能使用BaseAty中的与Fragment交互的方法
 * 
 * @author Zero
 * 
 *         2014年12月19日
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment implements 
		AdapterCallback, HttpHelperCallBack {

	private FrameLayout content;
	private View progress;
	private View error;
	private View fragmentView;
	private ProgressDialog progressDialog;

	protected Object mDataIn;
	private boolean isShowContent; // 当出错时标识是否显示错误页面
	protected boolean hasAnimiation = true; // 启动页面时是否带启动动画

	protected Context mContext;
	/** 是否初始化请求网络数据 */
	protected boolean isInitRequestData = false;

	protected LinearLayout ll_error_group;
	protected LinearLayout ll_error;
	protected View loadingView;
	protected View dialog;
	protected RelativeLayout loadingContent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 向用户展示信息前的准备工作在这个方法里处理
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frame_yzypmj_base_fragment_content_layout, container, false);
		content =   (FrameLayout) view.findViewById(R.id.content);
		
		fragmentView =inflater.inflate(getLayoutResId(),null);
		content.addView(fragmentView);
		initView(content);
		
		// 给最底层的layout设置一个点击监听防止切换页面之后还会点击到别的页面的BUG
		content.setOnClickListener(null);
		
		
		
		return view;
	}

	private void initView(View layout) {

	}

	public abstract int getLayoutResId();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		// 设置是否是初始化网络操作
		isInitRequestData = setIsInitRequestData();

		loadingView = LayoutInflater.from(mContext).inflate(
				R.layout.frame_yzypmj_loading_layout, null);
		loadingContent=(RelativeLayout) loadingView.findViewById(R.id.frame_rl_loading_content);
		
		
		dialog = LayoutInflater.from(mContext).inflate(
				R.layout.frame_yzypmj_customprogressbar_layout, null);

		ll_error_group = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.frame_yzypmj_error_layout, null);

		ll_error = (LinearLayout) ll_error_group.findViewById(R.id.ll_error);
		// 添加错误界面
//		FrameLayout rootFrameLayout = (FrameLayout) getActivity().findViewById(
//				android.R.id.content);
		LayoutParams layoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		ll_error_group.setLayoutParams(layoutParams);
		ll_error_group.setVisibility(View.VISIBLE);
		content.addView(ll_error_group);
		content.addView(loadingView);
		content.addView(dialog);
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

		// 请求数据
		initViews();
		initData();
		initListener();
		requestData();
	}


	/**
	 * 初始化view
	 */
	public abstract void initViews();

	/**
	 * 初始化数据
	 */
	public abstract void initData();

	/**
	 * 初始化监听
	 */
	public abstract void initListener();

	/**
	 * 请求数据，需要写 showProgressContent / showProgressDialog <br/>
	 * 若调用showProgressContent的情况下出现联网失败则会显示ERROR界面
	 */
	public abstract void requestData();

	/**
	 * 是否是初始化数据操作
	 */
	public abstract boolean setIsInitRequestData();

	/**
	 * view的点击事件
	 */
	public abstract void btnClick(View view);

	/**
	 * 刷新数据
	 */
	public void refurbishData() {
		isInitRequestData = true;

	}


	public void onClick(View v) {
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	/**
	 * 设置activity title与主界面距离风格
	 * 
	 * @param dimens
	 *            title高度
	 * @param
	 * 
	 */
	public void SetAppThemeSpace(int dimens) {
		LayoutParams layoutParams = new LayoutParams(
				ScreenUtils.getScreenWidth(getActivity()),
				(ScreenUtils.getScreenHeight(getActivity()))
						- (int) getResources().getDimension(dimens));
		layoutParams.topMargin = (int) getResources().getDimension(dimens);
		loadingContent.setLayoutParams(layoutParams);
	}

	// =========================================================== 启动Activity

	/**
	 * 启动一个Activity
	 * 
	 * @param className
	 *            将要启动的Activity的类名
	 * @param options
	 *            传到将要启动Activity的Bundle，不传时为null
	 */
	public void startActivity(Class<?> className, Bundle options) {
		Intent intent = new Intent(getActivity(), className);
		if (options != null) {
			intent.putExtras(options);
		}
		startActivity(intent);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		if (hasAnimiation) {
			getActivity().overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
		}
	}

	// ================ 信息提示方式 ===================

	/**
	 * 含有标题、内容和确定按钮的对话框 -- 一般用来显示信息
	 * 
	 * @param title
	 * @param message
	 */
	public void showDialog(String title, String message) {
		showDialog(title, message, "确定", null);
	}

	/**
	 * 含有标题、内容和一个按钮的对话框
	 * 
	 * @param title
	 * @param message
	 * @param positiveText
	 * @param listener
	 */
	public void showDialog(String title, String message,
			String positiveText, DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(getActivity()).setTitle(title)
				.setMessage(message).setPositiveButton(positiveText, listener)
				.create().show();
	}

	/**
	 * 含有标题、内容和两个按钮的对话框
	 * 
	 * @param title
	 * @param message
	 * @param positiveText
	 * @param negativeText
	 * @param positiveListener
	 * @param negativeListener
	 */
	public void showDialog(String title, String message,
			String positiveText, String negativeText,
			DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener negativeListener) {
		new AlertDialog.Builder(getActivity()).setTitle(title)
				.setMessage(message)
				.setPositiveButton(positiveText, positiveListener)
				.setNegativeButton(negativeText, negativeListener).create()
				.show();
	}

	/**
	 * 带有item的Dialog
	 * 
	 * @param title
	 * @param itemsId
	 * @param listener
	 */
	public void showItemsDialog(String title, int itemsId,
			DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(getActivity()).setTitle(title)
				.setItems(itemsId, listener).show();
	}

	/**
	 * 带有item的Dialog
	 * 
	 * @param title
	 * @param listener
	 */
	public void showItemsDialog(String title, CharSequence[] items,
			DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(getActivity()).setTitle(title)
				.setItems(items, listener).show();
	}

	public void showToast(String text) {
		Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setText(text);
		toast.show();
	}

	// =========================================================================
	/**
	 * 通过控件的Id获取对于的控件,该方法好像要在子类中重写才有用
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {

		return (T) content.findViewById(viewId);
	}

	// ===================== 加载条 ==================
	/**
	 * 全屏显示的进度界面，用来遮挡背后的模拟数据 //使用需在调取网络之前
	 */
	public void showLoadingContent() {
		loadingView.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏全屏显示的进度框
	 */
	public void dismissLoadingContent() {
		loadingView.setVisibility(View.GONE);
	}

	// ===================一些进度框以及提示信息==========================
	public void showLoadingDialog() {
		dialog.setVisibility(View.VISIBLE);
	}

	public void dismissLoadingDialog() {
		dialog.setVisibility(View.GONE);
	}

	// =========================================================== API回调方法

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

}
