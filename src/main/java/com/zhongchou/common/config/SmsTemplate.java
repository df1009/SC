package com.zhongchou.common.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public final class SmsTemplate {

	/**
	 * 保存此类的单一实例。
	 */
	private static SmsTemplate m_instance = new SmsTemplate();

	private static Properties m_props = new Properties();

	static{
		try {
			m_props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("sms_template.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构造函数。
	 *
	 * 禁止从外部生成实例。
	 */
	private SmsTemplate() {
	}

	/**
	 * 返回该类的实例方法。 <br>
	 * 使用单例模式，并且始终返回同一个实例。
	 *
	 * @return 此类的实例
	 */
	public static SmsTemplate getInstance() {
		return m_instance;
	}

	/**
	 * 通过指定的key从资源文件获取对应的值的方法。不存在的情况null返回。
	 *
	 * @param key 资源文件中的key
	 * @return key对应的值
	 */
	public static String getString(String key) {
		return m_props.getProperty(key);
	}

	/**
	 * 通过指定的key从资源文件获取对应的值的方法。不存在的情况null返回。
	 *
	 * @param key 资源文件中的key
	 * @param params 参数
	 * @return key对应的值
	 * @see java.text.MessageFormat#format(java.lang.String, java.lang.Object[])
	 */
	public static String getString(String key, Object... params) {
		return MessageFormat.format(getString(key), params);
	}

	public static boolean containsKey(String key) {
		return m_props.containsKey(key);
	}

}
