package com.pmjyzy.android.frame.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pmjyzy.android.frame.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 此adapter没有缓存机制，用法一样
 */
public abstract class NewBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas;
    private LayoutInflater mInflater;
    protected AdapterCallback adapterCallback;
    protected int mItemLayoutId;
    protected View convertView;

    protected ViewNoHolder noHolder;

    public NewBaseAdapter(Context context, List<T> list, int mItemLayoutId) {
        this.mContext = context;
        this.mDatas = list;
        this.mInflater = LayoutInflater.from(context);
        this.mItemLayoutId = mItemLayoutId;
        noHolder = new ViewNoHolder();

    }

    public NewBaseAdapter(Context context, List<T> list, int mItemLayoutId,
                          AdapterCallback adapterCallback) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = list;
        this.mItemLayoutId = mItemLayoutId;
        this.adapterCallback = adapterCallback;
        noHolder = new ViewNoHolder();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View con, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = mInflater.inflate(mItemLayoutId, parent, false);


        convert(noHolder, getItem(position), position);


        return convertView;
    }


    public abstract void convert(ViewNoHolder noHolder, T item, int positon);


    /**
     * 添加一个对象
     */
    public void addInfo(T info) {
        if (mDatas == null) {
            mDatas = new ArrayList<T>();
        }
        mDatas.add(info);
        notifyDataSetChanged();
    }

    /**
     * 删除一个对象
     */
    public void removeInfo(T info) {
        if (mDatas != null) {
            mDatas.remove(info);
            notifyDataSetChanged();
        }

    }

    /**
     * 删除多个对象
     */
    public void removeInfos(List<T> list_) {
        if (mDatas != null) {
            if (list_ != null && list_.size() > 0) {
                for (T t : list_) {
                    mDatas.remove(t);
                }
                notifyDataSetChanged();
            }

        }


    }

    /**
     * 添加所有
     */
    public void addAll(List<T> list_) {
        List<T> temp = new ArrayList<T>();
        if (mDatas == null) {
            mDatas = new ArrayList<T>();
        }
        for (T t : list_) {
            temp.add(t);
        }
        Log.i("result", "mData.size=add=" + mDatas.size());
        mDatas.addAll(temp);

        notifyDataSetChanged();
        temp = null;
    }


    /**
     * 查找所有
     */
    public List<T> findAll() {
        if (mDatas != null) {
            return mDatas;
        } else {
            return null;
        }
    }

    /**
     * 删除所有
     */
    public void removeAll() {
        if (mDatas == null) {
            mDatas = new ArrayList<T>();
        }
        mDatas.clear();
        Log.i("result", "mData.size=clear=" + mDatas.size());
        notifyDataSetChanged();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {

        return (T) convertView.findViewById(viewId);
    }

    protected void startActivity(Class<?> className, Bundle options) {
        adapterCallback.adapterstartActivity(className, options);
    }

    /**
     * 为adapter设置一些常用的方法
     */
    public class ViewNoHolder {

        /**
         * 为TextView设置字符串
         *
         * @param viewId
         * @param text
         * @return
         */
        public ViewNoHolder setTextViewText(int viewId, String text) {
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
        public ViewNoHolder setEditText(int viewId, String text) {
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
        public ViewNoHolder setButtonText(int viewId, String text) {
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
        public ViewNoHolder setRadioBtnText(int viewId, String text) {
            RadioButton view = getView(viewId);
            view.setText(text);
            return this;
        }

        /**
         * 给view设置点击事件
         *
         * @param viewId
         * @param listener
         * @return
         */
        public ViewNoHolder setOnClick(int viewId, OnClickListener listener) {
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
        public ViewNoHolder setImageByResource(int viewId, int drawableId) {
            ImageView view = getView(viewId);
            view.setImageResource(drawableId);

            return this;
        }

        /**
         * 为ImageView设置图片,通过bitmap
         *
         * @param viewId
         * @param bm
         * @return
         */
        public ViewNoHolder setImageByBitmap(int viewId, Bitmap bm) {
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
        public ViewNoHolder setImageByUrl(int viewId, String url) {
            ImageUtil.getImageUtil(mContext).setUrlImage(url,
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
        public ViewNoHolder setImageByUrl(int viewId, String url, int loadingDrawableId, int errorDrawableId) {
            ImageUtil.getImageUtil(mContext, loadingDrawableId, errorDrawableId).setUrlImage(url,
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
        public ViewNoHolder setCheckBoxChecked(int viewId, boolean isChecked) {
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
        public ViewNoHolder setRadioBtnChecked(int viewId, boolean isChecked) {
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
        public ViewNoHolder setEditTextNotEdit(int viewId) {
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
        public ViewNoHolder setEditTextYesEdit(int viewId) {
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
         *
         * @param viewId
         * @param Visibility
         * @return
         */
        public ViewNoHolder setViewVisibility(int viewId, int Visibility) {
            View view = getView(viewId);
            view.setVisibility(Visibility);
            return this;
        }


    }

}
