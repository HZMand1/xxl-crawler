package com.xuxueli.crawler.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * regex tool
 *
 * @author xuxueli 2015-5-12 23:57:48
 */
public class RegexUtil {

	/**
	 * 正则匹配
	 * @param regex	: 正则表达式
	 * @param str	: 待匹配字符串
	 * @return boolean
	 */
	public static boolean matches(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	private static final String URL_REGEX = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

	/**
	 * url格式校验
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isUrl(String str) {
		if (str==null || str.trim().length()==0) {
			return false;
		}
		return matches(URL_REGEX, str);
	}

	public static void main(String[] args) {
		String regex = "https://www.amazon.com/-/zh/international-sales-offers/b/?.*";
		String str = "https://www.amazon.com/-/zh/international-sales-offers/b/?ie=UTF8&node=15529609011&ref_=nav_cs_gb_intl_52df97a2eee74206a8343034e85cd058";
		System.out.println(RegexUtil.matches(regex,str));
	}

}