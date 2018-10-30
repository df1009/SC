package com.zhongchou.common.service;


import java.util.List;

import com.zhongchou.common.dto.DealRecordDto;
import com.zhongchou.common.dto.MyProjectlDto;

/**
 * 个人中心逻辑的接口。
 */
public interface IDealRecordService {

	/**
	 * 我的项目数据条数
	 */
	public int getDealRecordCnt(String oidUserId,String beginDate,String endDate,String dateType,String projectType,String projectName);

	/**
	 * 我的项目数据获取
	 */
	List<DealRecordDto> getDealRecordList(String oidUserId,String beginDate,String endDate,String dateType,String projectType,String projectName, int pageSize,int curPage);





}
