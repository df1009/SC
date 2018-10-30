package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;

/**
 * 个人中心接口。
 */
public interface IPCDealRecordDao {

	/**
	 * 获取我的项目数据条数
	 */
	public int getDealRecordCnt(Map reqMap);

	/**
	 * 获取我的项目数据
	 */
	List<Row> getDealRecordList(Map reqMap);


	/**
	 * 获取撤单状态
	 */
	 Row getBankName(String bankId);
}
