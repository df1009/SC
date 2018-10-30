package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IUserBindbankDao;

/**
 * 个人中心-银行卡管理
 */
public class UserBindbankDaoImpl extends BaseDao implements IUserBindbankDao {

	/**
	 * 根据oid获取我的账户下的银行卡
	 * 用户的IOD  oidUserId
	 * @return
	 */
	public List<Row> getUserBindbanks(String oidUserId){

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 T1.OID_ACCOUNT_BIND_BANK_ID ");
		sql.append(" 	,T1.CARD_NO ");
		sql.append(" 	,T1.BANK ");
		sql.append(" 	,T1.PAY_CHANNEL ");
		sql.append(" 	,T1.NEED_BANK_SIGN ");
		sql.append(" 	,T1.MAIN_FLG ");
		sql.append(" FROM ");
		sql.append(" 	USER_ACCOUNT_BIND_BANK T1 ");
		sql.append(" WHERE 1=1");
		sql.append(" 	AND T1.OID_USER_ID = ? ");
		sql.append(" ORDER BY T1.MAIN_FLG DESC,T1.INS_DATE DESC");

		return queryForList(sql.toString(), new Object[]{oidUserId});
	}

	/**
	 * 删除银行卡
	 * 参数 param
	 * @return
	 */
	public int delUserBindbanks(Map<String,Object> param){

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER_ACCOUNT_BIND_BANK ");
		sql.append(" SET DEL_FLG = 1 ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" AND OID_ACCOUNT_BIND_BANK_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add((String)param.get("loginId"));
		params.add((String)param.get("oidAccountBindBankId"));

		return update(sql.toString(), params.toArray());
	}
}
