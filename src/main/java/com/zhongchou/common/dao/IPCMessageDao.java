package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;

/**
 * 个人中心接口。
 */
public interface IPCMessageDao {

	/**
	 * 获取我的项目数据条数
	 */
	public int getMessageCnt(Map reqMap);

	/**
	 * 获取我的项目数据
	 */
	List<Row> getMessageList(Map reqMap);

	/**
	 * 添加消息
	 */
	public int insMessage(Map reqMap);
}
