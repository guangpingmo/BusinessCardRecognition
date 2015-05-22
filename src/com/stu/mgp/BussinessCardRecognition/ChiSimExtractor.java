package com.stu.mgp.BussinessCardRecognition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChiSimExtractor extends Extractor {

	// 中文字符的区间
	String chiSimRegex = "[\u4e00-\u9fa5]";

	@Override
	public void extract(String inputText) {
		Pattern p;
		Matcher m;
		String regexString;

		/*
		 * 匹配名字
		 * 配置两到四个字符,
		 * 匹配:郭靖, 令狐冲, 西门吹雪
		 */

		regexString = "^[\u4e00-\u9fa5]{2,4}\\s*";

		p = Pattern.compile(regexString, Pattern.MULTILINE);
		m = p.matcher(inputText);

		if (m.find()) {
			name = m.group().toString();
		}

		/*
		 * 配置电子邮件地址
		 * 配置: guangpingmo @ 163 .com
		 *		guangpingmo@126.com
		 */

		regexString = "([^ \n]+ *@ *.+(\\..{2,4})+)$";

		p = Pattern.compile(regexString, Pattern.MULTILINE);
		m = p.matcher(inputText);

		if (m.find()) {
			email = m.group(1).toString();
		}
		
		/*
		 * 配置手机号码
		 * 匹配11位连续的数字
		 * 
		 * 
		 */
		regexString = "\\s*(\\d{13})\\s*";
		
		p = Pattern.compile(regexString, Pattern.MULTILINE);
		m = p.matcher(inputText);

		if (m.find()) {
			phoneNumber = m.group(1).toString();
		}
	}

	
}
