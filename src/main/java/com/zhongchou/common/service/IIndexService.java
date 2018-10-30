package com.zhongchou.common.service;


import java.util.List;
import java.util.Map;

import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.CompanionDto;
import com.zhongchou.common.dto.CooperateDto;

/**
 * 主页逻辑的接口。
 */
public interface IIndexService {

	/**
	 * 获取banner方法
	 *
	 * @return banner的list
	 */
	List<BannerDto> getBannerList(String type);


	/**
	 * 获取合作机构
	 *
	 * @return 合作机构的list
	 */
	List<CompanionDto> getCompanionList();

	/**
	 * 获取战略合作
	 *
	 * @return 战略合作的list
	 */
	List<CooperateDto> getCooperateList();

	/**
	 * 验证状态方法
	 *
	 * @return 未通过验证的标识
	 */
	int checkState(String userId);

	/**
	 * 验证状态方法
	 *
	 * @return 未通过验证的标识
	 */
	int checkRsState(String userId);
	//添加反馈
	int addFeedback(Map insMap);
}
