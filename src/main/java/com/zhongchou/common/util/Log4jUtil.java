package com.zhongchou.common.util;

public class Log4jUtil {
	//将
	public static String output(String content){
		return content.toString().replace("{", "\r\n{\r\n")
				.replace("}", "\r\n}\r\n")
				.replace(",", ",\r\n");
	}
}
