package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.ISmsDao;
import com.zhongchou.common.dto.SendSmsDto;
import com.zhongchou.common.enums.SmsType;

public class SmsDaoImpl extends BaseDao implements ISmsDao {
	@Override
	public Row getLastVerifyCode(SendSmsDto sendSms) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" A.* ");
		sql.append(" FROM ");
		sql.append(" SMS_VERIFY_CODE A, ");
		sql.append(" (SELECT ");
		sql.append(" MAX(INS_DATE) AS INS_DATE ");
		sql.append(" FROM ");
		sql.append(" SMS_VERIFY_CODE ");
		sql.append(" WHERE ");
		sql.append(" PHONE = ? ");
		sql.append(" AND OPERATE_TYPE = ? ");
		sql.append(" AND VALID_FLG = '0' ");
		sql.append(" GROUP BY PHONE) B ");
		sql.append(" WHERE A.INS_DATE = B.INS_DATE ");

		return query(sql.toString(), new Object[]{sendSms.getPhone(),sendSms.getOperateType()});
	}

	@Override
	public int updateErrorSms(SendSmsDto sendSms,String vId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" SMS_VERIFY_CODE ");
		sql.append(" SET ERRORTIMES = ERRORTIMES + 1 ");
		sql.append(" WHERE ");
		sql.append(" PHONE = ? ");
		sql.append(" AND ID = ? ");
		sql.append(" AND VALID_FLG = '0' ");
		return update(sql.toString(), new Object[]{sendSms.getPhone(), vId});
	}

	@Override
	public int updateErrorVerifyCodeInvalid(SendSmsDto sendSms,String vId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" SMS_VERIFY_CODE ");
		sql.append(" SET VALID_FLG = '1' ");
		sql.append(" WHERE ");
		sql.append(" PHONE = ? ");
		sql.append(" AND ID = ? ");
		sql.append(" AND VALID_FLG = '0' ");
		return update(sql.toString(), new Object[]{sendSms.getPhone(), vId});
	}


	@Override
	public int updateVerifyCodeInvalid(SendSmsDto sendSms) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" SMS_VERIFY_CODE ");
		sql.append(" SET VALID_FLG = '1' ");
		sql.append(" WHERE ");
		sql.append(" PHONE = ? ");
		sql.append(" AND OPERATE_TYPE = ? ");
		sql.append(" AND VALID_FLG = '0' ");
		return update(sql.toString(), new Object[]{sendSms.getPhone(), sendSms.getOperateType()});
	}

	@Override
	public int getSmsHistoryCntByChk(SendSmsDto sendSms) {
		// SQL文生成
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	COUNT(T.ID) AS CNT ");
		sql.append(" FROM SMS_SEND_HISTORY T ");
		sql.append(" WHERE 1 = 1 ");
		if ("mysql".equalsIgnoreCase(getDbType())) {
			sql.append(" AND T.INS_DATE >= DATE_SUB(NOW(),INTERVAL 1 DAY) ");
		} else if ("oracle".equalsIgnoreCase(getDbType())) {
			sql.append(" AND T.INS_DATE >= TO_CHAR(SYSDATE-1,'yyyy-MM-dd hh24:mi:ss') ");
		}

		List<Object> params = new ArrayList<>();

		if (!StringUtils.isEmpty(sendSms.getSendStatus())) {
			sql.append(" AND T.SEND_STATUS = ? ");
			params.add(sendSms.getSendStatus());
		}

		if (!StringUtils.isEmpty(sendSms.getPhone())) {
			sql.append(" AND T.PHONE = ? ");
			params.add(sendSms.getPhone());
		}

		if (!StringUtils.isEmpty(sendSms.getSendIp())) {
			sql.append(" AND T.INS_IP = ? ");
			params.add(sendSms.getSendIp());
		}

		return getCount(sql.toString(), params.toArray());
	}

	@Override
	public int saveNightSms(SendSmsDto sendSms) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" NIGHT_SMS_RESEND_M ");
		sql.append(" ( ");
		sql.append("   PHONE ");
		sql.append("  ,SEND_STATUS ");
		sql.append("  ,SMS_CONTENT ");
		sql.append(" ) ");
		sql.append(" VALUES ( ");
		sql.append("   ? ");
		sql.append("  ,? ");
		sql.append("  ,? ");
		sql.append(" ) ");
		Object[] params = new Object[]{
				sendSms.getPhone(),
				"0",
				sendSms.getContents()};
		return update(sql.toString(), params);
	}


	@Override
	public int saveHistory(SendSmsDto sendSms, boolean isSendSucc) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" SMS_SEND_HISTORY ");
		sql.append(" ( ");
		sql.append("   PHONE ");
		sql.append("  ,SEND_STATUS ");
		sql.append("  ,SMS_CONTENT ");
		sql.append("  ,SMS_TYPE ");
		sql.append("  ,INS_IP ");
		sql.append(" ) ");
		sql.append(" VALUES ( ");
		sql.append("   ? ");
		sql.append("  ,? ");
		sql.append("  ,? ");
		sql.append("  ,? ");
		sql.append("  ,? ");
		sql.append(" ) ");
		Object[] params = new Object[]{sendSms.getPhone(),
				isSendSucc ? "1" : "0",
				sendSms.getContents(),
				sendSms.getSmsType() == SmsType.TEXT ? "0" : "1",
				sendSms.getSendIp()};
		return update(sql.toString(), params);
	}


	@Override
	public int saveVerifyCode(SendSmsDto sendSms) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" SMS_VERIFY_CODE ");
		sql.append(" (PHONE, VERIFY_CODE, OPERATE_TYPE, TIMES) ");
		sql.append(" VALUES( ?, ?, ?, 1 ) ");
		return update(sql.toString(), new Object[]{sendSms.getPhone(), sendSms.getVerifyCode(), sendSms.getOperateType()});
	}

	@Override
	public int updateVerifyCode(SendSmsDto sendSms) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" SMS_VERIFY_CODE ");
		sql.append(" SET TIMES = TIMES + 1 ");
		sql.append(" WHERE ");
		sql.append(" PHONE = ? ");
		sql.append(" AND ID = ? ");
		sql.append(" AND VALID_FLG = '0' ");
		return update(sql.toString(), new Object[]{sendSms.getPhone(), sendSms.getSmsVerifyCodeId()});
	}

	@Override
	public int selSendSmsTenMinute(String phone) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUM(TIMES) FROM SMS_VERIFY_CODE ");
		sql.append(" WHERE PHONE=? ");
		sql.append(" AND INS_DATE>date_add(now(), interval -10 MINUTE)");
		return getCount(sql.toString(), new Object[]{phone});
	}

	@Override
	public int saveSms(SendSmsDto sendSms) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" SMS_SEND_M ");
		sql.append(" ( ");
		sql.append("   PHONE ");
		sql.append("  ,SEND_STATUS ");
		sql.append("  ,SMS_CONTENT ");
		sql.append(" ) ");
		sql.append(" VALUES ( ");
		sql.append("   ? ");
		sql.append("  ,? ");
		sql.append("  ,? ");
		sql.append(" ) ");
		Object[] params = new Object[]{
				sendSms.getPhone(),
				"0",
				sendSms.getContents()};
		return update(sql.toString(), params);
	}
	//查询验证码
	@Override
	public Row getUserVerifyCode(String phone) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT PHONE phone, VERIFY_CODE code,DATE_FORMAT(INS_DATE,'%Y-%m-%d %H:%i:%s') time FROM sms_verify_code ");
		sql.append(" WHERE PHONE = ? ORDER BY INS_DATE DESC ");
		return query(sql.toString(), new Object[]{phone});
	}

}
