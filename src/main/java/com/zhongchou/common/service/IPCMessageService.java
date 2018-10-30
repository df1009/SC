package com.zhongchou.common.service;


import java.util.List;
import java.util.Map;

import com.zhongchou.common.dto.DealRecordDto;
import com.zhongchou.common.dto.MyProjectlDto;

/**
 * 消息中心
 */
public interface IPCMessageService {

	/**
	 * 消息中心数据条数
	 */
	public int getMessageCnt(Map reqMap);

	/**
	 * 消息中心数据获取
	 */
	List getMessageList(Map reqMap);

	/**
	 * 添加消息
	 */
	boolean insMessageList(Map reqMap);



}
