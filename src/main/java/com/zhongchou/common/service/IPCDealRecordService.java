package com.zhongchou.common.service;


import java.util.List;
import java.util.Map;

import com.zhongchou.common.dto.DealRecordDto;
import com.zhongchou.common.dto.MyProjectlDto;

/**
 * 个人中心逻辑的接口。
 */
public interface IPCDealRecordService {

	/**
	 * 我的项目数据条数
	 */
	public int getDealRecordCnt(Map reqMap);

	/**
	 * 我的项目数据获取
	 */
	List getDealRecordList(Map reqMap);





}
