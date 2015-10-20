package com.pmjyzy.android.frame.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pmjyzy.android.frame.utils.ImageUtil;

/**
 * 通用的viewHodler
 * 
 * @author HrcmChan
 * 
 */

public class ViewHolder {
	// 装view的map
	private final SparseArray<View> mViews;

	private  View mConvertView;
	private Context context;
	private ImageUtil imageUtil;

	

	private ViewHolder(Context context, ViewGroup parent, int layoutId) {
		//Log.i("result", "viewholder");
		this.context = context;


		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId) {

		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId);
		}
		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setTextViewText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为EditText设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setEditText(int viewId, String text) {
		EditText view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为Button设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setButtonText(int viewId, String text) {
		Button view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为RadioButton设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setRadioBtnText(int viewId, String text) {
		RadioButton view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 设置view的点击事件
	 * @param viewId
	 * @param listener
	 * @return
	 */
	public ViewHolder setOnClick(int viewId, OnClickListener listener) {
		View view = getView(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	/**
	 * 为ImageView设置图片,通过资源文件
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageByResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * 为ImageView设置图片，通过bitmap
	 * 
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public ViewHolder setImageByBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置图片，通过网络下载
	 * 
	 * @param viewId
	 * @param url
	 * @return
	 */
	public ViewHolder setImageByUrl(int viewId, String url) {
		ImageUtil.getImageUtil(context).setUrlImage(url,
				(ImageView) getView(viewId));
		return this;
	}

	/**
	 * 为ImageView设置图片，通过网络下载
	 *
	 * @param viewId
	 * @param url
	 * @param loadingDrawableId 下载时显示的图片，没有时传-1.
	 * @param errorDrawableId   错误时显示的图片，没有时传-1.
	 * @return
	 */
	public ViewHolder setImageByUrl(int viewId, String url, int loadingDrawableId, int errorDrawableId) {
		ImageUtil.getImageUtil(context,loadingDrawableId,errorDrawableId).setUrlImage(url,
				(ImageView) getView(viewId));
		return this;
	}


	/**
	 * 给checkbox修改状态
	 * 
	 * @param viewId
	 * @param isChecked
	 * @return
	 */
	public ViewHolder setCheckBoxChecked(int viewId, boolean isChecked) {
		CheckBox cb = getView(viewId);
		cb.setChecked(isChecked);
		return this;
	}

	/**
	 * 给Radiobtn修改状态
	 * 
	 * @param viewId
	 * @param isChecked
	 * @return
	 */
	public ViewHolder setRadioBtnChecked(int viewId, boolean isChecked) {
		RadioButton rb = getView(viewId);
		rb.setChecked(isChecked);
		return this;
	}

	/**
	 * 给Edittext设置不可编辑
	 * 
	 * @param viewId
	 * @param
	 * @return
	 */
	public ViewHolder setEditTextNotEdit(int viewId) {
		EditText et = getView(viewId);
		et.setFocusable(false);
		et.setFocusableInTouchMode(false);
		// 设置光标隐藏
		et.setCursorVisible(false);
		return this;
	}

	/**
	 * 给Edittext设置可编辑
	 * 
	 * @param viewId
	 * @param
	 * @return
	 */
	public ViewHolder setEditTextYesEdit(int viewId) {
		EditText et = getView(viewId);
		et.setFocusable(true);
		et.setFocusableInTouchMode(true);
		et.requestFocus();
		// 设置光标显示
		et.setCursorVisible(true);
		return this;
	}


	/**
	 * 设置view的可见性
	 * @param viewId
	 * @param Visibility
	 * @return
	 */
	public ViewHolder setViewVisibility(int viewId, int Visibility) {
		View view = getView(viewId);
		view.setVisibility(Visibility);
		return this;
	}

	/*
	 * public int getPosition() { return mPosition; }
	 */

}
