package com.zhongchou.common.dao;

import java.util.List;

import com.yanshang.util.Row;

/**
 * 个人中心接口。
 */
public interface IDealRecordDao {

	/**
	 * 获取我的项目数据条数
	 */
	public int getDealRecordCnt(String oidUserId,String beginDate,String endDate,String dateType,String projectType,String projectName);

	/**
	 * 获取我的项目数据
	 */
	List<Row> getDealRecordList(String oidUserId,String beginDate,String endDate,String dateType,String projectType,String projectName, int pageSize,int curPage);


	/**
	 * 获取撤单状态
	 */
	 Row getBankName(String bankId);
}
