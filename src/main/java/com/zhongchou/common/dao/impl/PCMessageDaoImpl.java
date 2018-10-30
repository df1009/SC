package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IPCMessageDao;

/**
 * 个人中心实现类。
 */

public class PCMessageDaoImpl extends BaseDao implements IPCMessageDao {

	//查询消息个数
	@Override
	public int getMessageCnt(Map reqMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT count(1) from message ");
		sql.append(" WHERE OID_USER_ID = ? ");
		params.add(reqMap.get("oidUserId"));
		if(!StringUtils.isEmpty(ConvUtils.convToString(reqMap.get("msgType")))){
			sql.append(" AND MSG_TYPE = ? ");
			params.add(reqMap.get("msgType"));
		}
		return getCount(sql.toString(), params.toArray());
	}
	//查询消息列表
	@Override
	public List<Row> getMessageList(Map reqMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT  ");
		sql.append(" MSG_TITLE ");
		sql.append(" ,MSG_STYLE_TITLE ");
		sql.append(" ,MSG_CONTENT ");
		sql.append(" ,MSG_TYPE ");
		sql.append(" ,MSG_STU_TP ");
		sql.append(" ,DATE_FORMAT(INS_DATE,'%Y-%m-%d %H:%i') INS_DATE ");
		sql.append(" ,DATE_FORMAT(INS_DATE,'%Y-%m-%d %H:%i:%S') INS_DATE1 ");
		sql.append(" from message ");
		sql.append(" WHERE OID_USER_ID = ? ");
		params.add(reqMap.get("oidUserId"));
		if(!StringUtils.isEmpty(ConvUtils.convToString(reqMap.get("msgType")))){
			sql.append(" AND MSG_TYPE = ? ");
			params.add(reqMap.get("msgType"));
		}
		sql.append(" ORDER BY INS_DATE DESC ");
		return queryForList(sql.toString(),(int)reqMap.get("curPage"),(int)reqMap.get("pageSize"),params.toArray());
	}
	@Override
	public int insMessage(Map reqMap) {
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO message  ");
		sql.append(" ( ");
		sql.append(" MSG_TITLE, ");
		sql.append(" MSG_STYLE_TITLE, ");
		sql.append(" MSG_CONTENT, ");
		sql.append(" MSG_TYPE, ");
		sql.append(" MSG_STU_TP, ");
		sql.append(" OID_USER_ID, ");
		sql.append(" INS_USER_ID) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?) ");

		Object[] params = new Object[]{
				reqMap.get("msgTitle"),
				reqMap.get("styleTitle"),
				reqMap.get("msgContent"),
				reqMap.get("msgType"),
				reqMap.get("msgStuType"),
				reqMap.get("oidUserId"),
				reqMap.get("oidUserId")
				};
		return update(sql.toString(), params);
	}
	
	
}
