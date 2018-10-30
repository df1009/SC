package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IUserAccountBindBankDao;

/**
 * 用户数据操作的实现类。
 */
public class UserAccountBindBankDaoImpl extends BaseDao implements IUserAccountBindBankDao {

	@Override
	public Row getUserAccountBindBank(Map<String, Object> parame) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 T1.OID_USER_ID ");
		sql.append(" FROM USER_ACCOUNT_BIND_BANK T1  ");
		sql.append(" WHERE T1.OID_USER_ID = ? ");
		sql.append(" 	AND T1.DEL_FLG = 0 ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("oidUserId"));
		return singleQuery(sql.toString(), params);
	}

	@Override
	public int insUserAccountBindBank(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER_ACCOUNT_BIND_BANK ");
		sql.append(" (OID_ACCOUNT_BIND_BANK_ID,OID_USER_ID,CARD_NO,BANK,MAIN_FLG,PAY_CHANNEL,NEED_BANK_SIGN,MOBILE) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?) ");
		Object[] params = new Object[]{
				(String)parame.get("oidAccountBindBankId"),
				(String)parame.get("loginId"),
				(String)parame.get("bankCode"),
				(String)parame.get("bankCd"),
				(String)parame.get("mainFlg"),
				(String)parame.get("payChannel"),
				(String)parame.get("needBankSign"),
				(String)parame.get("mobileNo")};
		return update(sql.toString(), params);
	}

	@Override
	public int updUserAccountBindBank(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER_ACCOUNT_BIND_BANK ");
		sql.append(" SET CARD_NO = ? ");
		sql.append(" , BANK = ? ");
		sql.append(" , MAIN_FLG = ? ");
		sql.append(" , PAY_CHANNEL = ? ");
		sql.append(" , NEED_BANK_SIGN = ? ");
		sql.append(" , MOBILE = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" and DEL_FLG = 0 ");
		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("bankCode"));
		params.add((String)parame.get("bankCd"));
		params.add((String)parame.get("mainFlg"));
		params.add((String)parame.get("payChannel"));
		params.add((String)parame.get("needBankSign"));
		params.add((String)parame.get("mobileNo"));
		params.add((String)parame.get("loginId"));

		return update(sql.toString(), params.toArray());
	}

	/**
	 * 查询是否为主卡
	 */
	@Override
	public Row queryIsMasterCard(Map<String, Object> paramMap) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT 'Y' FROM USER_ACCOUNT_BIND_BANK WHERE MAIN_FLG = '1' AND CARD_NO = ? AND OID_USER_ID = ?" );
		return singleQuery(sql.toString(), new Object[]{paramMap.get("bankCode"),paramMap.get("loginId")});
	}
	/**
	 * 逻辑取消银行卡
	 */

	@Override
	public int cancelBackCard(Map<String, Object> paramMap) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE USER_ACCOUNT_BIND_BANK SET DEL_FLG = ? WHERE CARD_NO = ? AND MAIN_FLG = ? AND OID_USER_ID = ?" );
		List<Object> params = new ArrayList<>();
		params.add("1");
		params.add((String)paramMap.get("bankCode"));
		params.add("0");
		params.add((String)paramMap.get("loginId"));
		return update(sql.toString(), params.toArray());
	}



	@Override
	public List<Row> selBankWithholdQuota(String bankCd) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * from bank_withhold_quota ");
		sql.append(" WHERE bank_cd = ?");
		return queryForList(sql.toString(),new Object[]{ bankCd});
	}
	/**
	 * 查询用户所有有效银行卡
	 */

	@Override
	public List<Row> queryBankCard(String loginId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT UA.CARD_NO ")
		   .append(" ,UA.BANK ")
		   .append(" ,UA.PROVINCE ")
		   .append(" ,UA.CITY ")
		   .append(" ,UA.INS_USER_ID ")
		   .append(" ,UA.NO_AGREE ")
		   .append(" ,UA.BIND_ID ")
		   .append(" ,UA.PAY_PROVIDER ")
		   .append(" ,UA.PAY_CHANNEL ")
		   .append(" ,UA.NEED_BANK_SIGN ")
		   .append(" ,UA.MOBILE ")
		   .append(" ,UA.MAIN_FLG ")
		   .append(" ,UA.DEL_FLG ")
		   .append(" ,BW.BANK_NM ")
		   .append(" ,BW.BANK_CD ")
		   .append(" FROM USER_ACCOUNT_BIND_BANK UA LEFT JOIN BANK_WITHHOLD_QUOTA BW ON UA.BANK=BW.BANK_CD")
		   .append(" WHERE DEL_FLG = 0 ")
		   .append(" AND OID_USER_ID = ? ORDER BY UA.INS_DATE ASC");

		return queryForList(sql.toString(), new Object[]{loginId});
	}
	/**
	 * 重新绑定之前的银行卡更新状态
	 * @param paramMap
	 */

	@Override
	public int updateBankCard(Map<String, Object> paramMap) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE USER_ACCOUNT_BIND_BANK SET DEL_FLG = ? WHERE OID_USER_ID = ? AND CARD_NO = ?");
		return update(sql.toString(), new Object[]{"0",(String)paramMap.get("loginId"),(String)paramMap.get("cardNo")});
	}

	/**
	 * 购买时查询用户卡号的id
	 * @param paramMap
	 */

	@Override
	public Row selOidAccountBindBankId(String loginId,String bankNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT OID_ACCOUNT_BIND_BANK_ID from USER_ACCOUNT_BIND_BANK ");
		sql.append(" where OID_USER_ID = ? ");
		sql.append(" and DEL_FLG = 0 ");
		sql.append(" and CARD_NO = ? ");
		return singleQuery(sql.toString(), new Object[]{loginId,bankNo});
	}

	/**
	 * 壹理财老用户注册第二部返显信息
	 * @param paramMap
	 */

	@Override
	public Row selBankSmg(String loginId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT b.CARD_NO cardNo,u.USER_NAME userName,u.ID_CARD idCard from USER_ACCOUNT_BIND_BANK b ");
		sql.append(" INNER JOIN user_detail u ");
		sql.append(" ON u.OID_USER_ID = b.OID_USER_ID ");
		sql.append(" WHERE u.OID_USER_ID=? ");
		sql.append(" AND b.DEL_FLG=0 ");
		return singleQuery(sql.toString(), new Object[]{loginId});
	}

	/**
	 * 查询中证支持的银行列表
	 * @param paramMap
	 */
	@Override
	public List<Row> selSupportBankList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * from bank_withhold_quota ");
		return queryForList(sql.toString(),new Object[]{ });
	}
	/**
	 * 查询用户绑卡绑定手机号
	 * @param paramMap
	 */
	@Override
	public Row queryBankMobile(String loginId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  ")
		   .append(" UA.MOBILE mobile ")
		   .append(" FROM USER_ACCOUNT_BIND_BANK UA ")
		   .append(" WHERE DEL_FLG = 0 ")
		   .append(" AND OID_USER_ID = ? ");

		return query(sql.toString(), new Object[]{loginId});
	}
}
