package com.zhongchou.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhongchou.common.dao.IOperationSourceLogDao;
import com.zhongchou.common.dto.OperationSourceLogDto;
import com.zhongchou.common.service.IOperationSourceLogService;

public class OperationSourceLogServiceImpl implements IOperationSourceLogService{

	@Autowired
	private IOperationSourceLogDao OperationSourceLogDao;

	/**
	 * 添加跟踪操作来源LOG
	 *
	 * @param operationSourceLog 操作来源实体类
	 * @return 是否成功
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int addOperationSourceLog(OperationSourceLogDto operationSourceLog){
		int request = OperationSourceLogDao.addOperationSourceLog(operationSourceLog);
		return request;
	}
}
