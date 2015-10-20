package com.pmjyzy.android.frame.adapter;

import android.os.Bundle;

public interface AdapterCallback {
	
	public void adapterstartActivity(Class<?> className, Bundle options);
	
	public void adapterInfotoActiity(Object data);
	
	public void adapterInfotoActiity(Object data, int what);
}
