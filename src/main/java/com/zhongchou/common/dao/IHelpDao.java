package com.zhongchou.common.dao;

import java.util.List;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.BannerDto;

/**
 * 主页数据操作的接口。
 */
public interface IHelpDao {
	/**
	 *获取帮助中心页面数据
	 * @return 帮助中心页面数据
	 */
	Row getSafetyMessage(String showId);

	/**
	 *获取当前可下载的最新app
	 * @return
	 */
	Row getDownloadApp();
}
