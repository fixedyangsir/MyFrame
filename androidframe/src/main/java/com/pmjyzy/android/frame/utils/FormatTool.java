package com.pmjyzy.android.frame.utils;


public class FormatTool {

	/**
	 * 传入单位必须为秒        返回为数组  times[3] 天 times[2]时 times[1]分 times[0]秒
	 * 
	 * @param time
	 * @return
	 */
	public static long[] secondToTimes(long time) {
		long times[] = { 0, 0, 0, 0 };
		if (time<0) {
			time=0;
		}

		long day = 24 * 3600;
		long hours = 3600;
		long min = 60;

		times[3] = time/ day;
		
		time -= (times[3] * day);
		times[2] = time / hours;
		time -= times[2] * hours;
		times[1] = time / min;
		time -= times[1] * min;
		times[0] = time;
		return times;
	}
	/**
	 * 传入单位必须为秒        返回为数组  times[3] 天 times[2]时 times[1]分 times[0]秒
	 * 
	 * @param time
	 * @return
	 */
	public static String secondToTimesByString(String t) {
		long time=System.currentTimeMillis()/1000L-Long.parseLong(t);
		
		long times[] = { 0, 0, 0, 0 };
		if (time<0) {
			time=0;
		}
		
		
		long day = 24 * 3600;
		long hours = 3600;
		long min = 60;
		
		times[3] = time/ day;
		
		time -= (times[3] * day);
		times[2] = time / hours;
		time -= times[2] * hours;
		times[1] = time / min;
		time -= times[1] * min;
		times[0] = time;
		
		String resultTiem="";
		if (times[3]>0) {
			resultTiem=(times[3]+"天前");
		}else if (times[2]>0) {
			resultTiem=(times[2]+"小时前");
		}else if ( times[1]>0) {
				resultTiem=(times[2]+"分钟前");
		}else if (times[0]>0) {
			resultTiem=(times[0]+"秒前");
		}
		
		
		return resultTiem;
	}

	/**
	 * 讲数字转换成三位一个逗号的字符串，传入值必须为整型
	 * 
	 * @param num
	 * @return
	 */
	public static String getFormatMoney(String num) {
		String str1 = num;
		str1 = new StringBuilder(str1).reverse().toString(); // 先将字符串颠倒顺序
		String str2 = "";
		for (int i = 0; i < str1.length(); i++) {
			if (i * 3 + 3 > str1.length()) {
				str2 += str1.substring(i * 3, str1.length());
				break;
			}
			str2 += str1.substring(i * 3, i * 3 + 3) + ",";
		}
		if (str2.endsWith(",")) {
			str2 = str2.substring(0, str2.length() - 1);
		}
		return new StringBuilder(str2).reverse().toString();
	}
}
