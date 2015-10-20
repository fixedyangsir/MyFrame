package com.pmjyzy.android.frame.utils;

import java.text.DecimalFormat;

public class MyStringUtils {

	/**
	 * 保留小数点后面，两位
	 * 
	 * @param str
	 * @return
	 */
	public static String toRetainTwoDecimal(String str) {
		double d = Double.parseDouble(str);
		DecimalFormat df = new DecimalFormat("####0.00");
		return df.format(d);
	}
}
