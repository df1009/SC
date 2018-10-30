package com.zhongchou.common.service;

import com.zhongchou.common.dto.OperationSourceLogDto;

public interface IOperationSourceLogService{

	/**
	 * 添加跟踪操作来源LOG
	 *
	 * @param operationSourceLog 操作来源实体类
	 * @return 是否成功
	 */
	public int addOperationSourceLog(OperationSourceLogDto operationSourceLog);
}
