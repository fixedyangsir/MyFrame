package com.pmjyzy.android.frame.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通过正则表达式，验证字符串
 * @author HrcmChan
 *
 */
public class MatchString {

	
	/**
	* @param regex
	* 正则表达式字符串
	* @param str
	* 要匹配的字符串
	* @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	*/
	private static boolean match(String regex, String str) {
	Pattern pattern = Pattern.compile(regex);
	Matcher matcher = pattern.matcher(str);
	return matcher.matches();
	}

	/**
	* 验证邮箱
	* 
	* @param 待验证的字符串
	* @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
	*/
	public static boolean isEmail(String str) {
	String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	return match(regex, str);
	}
	
	/**
	* 验证输入手机号码
	* 
	* @param 待验证的字符串
	* @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	*/
	public static boolean isMobile(String str) {
	String regex = ("^0?(13[0-9]|15[012356789]|18[02356789]|14[57]|17[0-9])[0-9]{8}$");
	return match(regex, str);
	}
	/**
	 * 验证两个字符串是否一样
	 * 
	 * @param 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isSame(String oldString,String newString) {
		if (oldString.trim().equals(newString.trim())) {
			return true;
		}
		return false;
	}

}
