package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IUserIdVerificationDao;

/**
 * 用户数据操作的实现类。
 */
public class UserIdVerificationDaoImpl extends BaseDao implements IUserIdVerificationDao {

	@Override
	public Row getUserIdVerification(Map<String, Object> parame) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 T1.OID_USER_ID ");
		sql.append(" FROM USER_ID_VERIFICATION T1  ");
		sql.append(" WHERE T1.OID_USER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("oidUserId"));

		return singleQuery(sql.toString(), params);
	}

	@Override
	public int updUserIdVerification(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER_ID_VERIFICATION ");
		sql.append(" SET USER_NAME = ? ");
		sql.append(" , ID_CARD_TYPE = ? ");
		sql.append(" , ID_CARD_NO = ? ");
		sql.append(" , BANK_CARD_NO = ? ");
		sql.append(" , PAY_PWD = ? ");
		sql.append(" , YZM_SSN = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("userName"));
		params.add((String)parame.get("idTp"));
		params.add((String)parame.get("idCard"));
		params.add((String)parame.get("bankCode"));
		params.add((String)parame.get("payPassword"));
		params.add((String)parame.get("BankReqSsn"));
		params.add((String)parame.get("loginId"));

		return update(sql.toString(), params.toArray());
	}

	@Override
	public int insUserIdVerification(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER_ID_VERIFICATION ");
		sql.append(" ( ");
		sql.append(" USER_NAME");
		sql.append(" , ID_CARD_TYPE ");
		sql.append(" , ID_CARD_NO ");
		sql.append(" , BANK_CARD_NO");
		sql.append(" , PAY_PWD  ");
		sql.append(" , YZM_SSN ");
		sql.append(" , OID_USER_ID ");
		sql.append(" )VALUES ");
		sql.append(" (?,?,?,?,?,?,?) ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("userName"));
		params.add((String)parame.get("idTp"));
		params.add((String)parame.get("idCard"));
		params.add((String)parame.get("bankCode"));
		params.add((String)parame.get("payPassword"));
		params.add((String)parame.get("BankReqSsn"));
		params.add((String)parame.get("loginId"));

		return update(sql.toString(), params.toArray());
	}
}
