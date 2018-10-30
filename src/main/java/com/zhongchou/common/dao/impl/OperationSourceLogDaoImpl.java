package com.zhongchou.common.dao.impl;

import com.yanshang.dao.BaseDao;
import com.zhongchou.common.dao.IOperationSourceLogDao;
import com.zhongchou.common.dto.OperationSourceLogDto;

public class OperationSourceLogDaoImpl extends BaseDao implements IOperationSourceLogDao{

	@Override
    public int addOperationSourceLog(OperationSourceLogDto operationSourceLog){
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" OPERATION_SOURCE_LOG ");
		sql.append(" (OID_USER_ID, ");
		sql.append(" OPERATION_TYPE, ");
		sql.append(" SOURCE, ");
		sql.append(" CHANNEL, ");
		sql.append(" OPERATION_CONTEXT, ");
		sql.append(" TARGET_ID, ");
		sql.append(" INS_DATE) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?) ");
		Object[] params = new Object[]{
				operationSourceLog.getOidUserId(),
				operationSourceLog.getOperationType(),
				operationSourceLog.getSource(),
				operationSourceLog.getChannel(),
				operationSourceLog.getOperationContext(),
				operationSourceLog.getTargetId(),
				operationSourceLog.getInsDate()
				};
		return update(sql.toString(), params);
    }
}
