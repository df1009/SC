package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IUserDetailDao;

/**
 * 用户数据操作的实现类。
 */
public class UserDetailDaoImpl extends BaseDao implements IUserDetailDao {

	@Override
	public Row getUserDetail(Map<String, Object> parame) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 T1.OID_USER_ID ");
		sql.append(" FROM USER_DETAIL T1  ");
		sql.append(" WHERE T1.OID_USER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("oidUserId"));

		return singleQuery(sql.toString(), params);
	}

	@Override
	public int updUserDetail(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER_DETAIL ");
		sql.append(" SET USER_NAME = ? ");
		sql.append(" , ID_CARD = ? ");
		sql.append(" , ID_CARD_VERIFY_FLG = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("userName"));
		params.add((String)parame.get("idCard"));
		params.add((String)parame.get("idCardVerifyFlg"));
		params.add((String)parame.get("loginId"));

		return update(sql.toString(), params.toArray());
	}
	//记录用户的变更记录
	@Override
	public int addUserModifyLog(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO user_modify_log ");
		sql.append(" (MODIFY_CONTENT,PRE_MODIFY,TAIL_MODIFY,OID_USER_ID) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?) ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("count"));
		params.add((String)parame.get("beforeChange"));
		params.add((String)parame.get("afterChange"));
		params.add((String)parame.get("loginId"));

		return update(sql.toString(), params.toArray());
	}
}
