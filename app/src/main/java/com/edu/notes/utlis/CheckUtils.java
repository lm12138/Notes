package com.edu.notes.utlis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式判断输入内容
 */
public class CheckUtils {
	
	
	private CheckUtils()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");  
    } 

//正则表达式检测手机号
	public static final String MOBILE = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$";


	public static boolean isMobile(String str) {
		return Regular(str, MOBILE);
	}


	private static boolean Regular(String str, String pattern) {
		if (null == str || str.trim().length() <= 0)
			return false;
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}
}
