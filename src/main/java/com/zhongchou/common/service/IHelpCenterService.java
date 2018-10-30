package com.zhongchou.common.service;


import java.util.Map;

	/**
	 * 帮助中心的接口。
	 */
public interface IHelpCenterService {
	/**
	 * 根据所传id取后台 相关页面
	 * @return 后台相关页面数据
	 */
	String getSafetyInfo(String showId);

	/**
	 *获取当前可下载的最新app
	 * @return
	 */
	Map getDownloadApp();
}
