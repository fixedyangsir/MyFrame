package com.pmjyzy.android.frame.utils;
/**
 * 为了给tabhost的activity设置跳转动画
 * @author HrcmChan
 *
 */
public class AnimCommon {

	public static int in = 0;

	public static int out = 0;

	public static void set(int a, int b) {

		in = a;

		out = b;

	}

	public static void clear() {

		in = 0;

		out = 0;

	}

}
