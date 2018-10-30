package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dto.UserDto;



/**
 * 用户数据操作的实现类。
 */
public class UserDaoImpl extends BaseDao implements IUserDao {
	@Override
	public Row getUserByMobile(String mobile) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 T1.OID_USER_ID ");
		sql.append(" 	,T1.USER_ID ");
		sql.append(" 	,T1.MOBILE ");
		sql.append(" 	,T1.EMAIL ");
		sql.append(" 	,T1.LOGIN_TIMES ");
		sql.append(" 	,T1.LOGIN_ERROR_TIMES ");
		sql.append(" 	,T1.LAST_LOGIN_DATE ");
		sql.append(" 	,T1.DEL_FLG ");
		sql.append(" 	,T1.USER_TYPE ");
		sql.append(" 	,T1.INVESTMENT_FLG ");
		sql.append(" 	,T1.FINANCING_FLG ");
		sql.append(" 	,T1.EMAIL_VERIFY_FLG ");
		sql.append(" 	,T1.INS_DATE ");
		sql.append(" 	,T1.TYPE ");
		sql.append(" 	,T2.NICKNAME ");
		sql.append(" 	,T2.USER_ICON_FILE_ID ");
		sql.append(" 	,T2.USER_NAME ");
		sql.append(" 	,T2.ID_CARD ");
		sql.append(" 	,T2.USER_POINT ");
		sql.append(" 	,T2.ID_CARD_VERIFY_FLG ");
		sql.append(" 	,T2.ID_CARD_ERROR_TIMES ");
		sql.append(" 	,T2.WEALTH_MANAGER_FLG ");
		sql.append(" 	,T3.INTRODUCER_OID_USER_ID ");
		sql.append(" 	,T4.USER_NAME AS INTRODUCER_NAME ");
		sql.append(" FROM USER T1  ");
		sql.append(" INNER JOIN USER_DETAIL T2 ON ");
		sql.append(" T1.OID_USER_ID = T2.OID_USER_ID ");
		sql.append(" LEFT JOIN USER_INTRODUCER_M T3 ON ");
		sql.append(" T1.OID_USER_ID = T3.OID_USER_ID ");
		sql.append(" LEFT JOIN USER_DETAIL T4 ON ");
		sql.append(" T3.INTRODUCER_OID_USER_ID = T4.OID_USER_ID ");
		sql.append(" WHERE T1.MOBILE = ? ");

		return singleQuery(sql.toString(), new Object[]{mobile});
	}

	@Override
	public Row checkFinanPhoneInput(String mobile) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from finacing_manage where MOBILE = ? ");

		return singleQuery(sql.toString(), new Object[]{mobile});
	}

	@Override
	public Row getOrgByContactMobile(String mobile) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 T1.OID_USER_ID ");
		sql.append(" 	,T1.USER_ID ");
		sql.append(" 	,T1.MOBILE ");
		sql.append(" 	,T1.EMAIL ");
		sql.append(" 	,T1.LOGIN_TIMES ");
		sql.append(" 	,T1.LOGIN_ERROR_TIMES ");
		sql.append(" 	,T1.LAST_LOGIN_DATE ");
		sql.append(" 	,T1.DEL_FLG ");
		sql.append(" 	,T1.USER_TYPE ");
		sql.append(" 	,T1.INVESTMENT_FLG ");
		sql.append(" 	,T1.FINANCING_FLG ");
		sql.append(" 	,T1.EMAIL_VERIFY_FLG ");
		sql.append(" 	,T1.INS_DATE ");
		sql.append(" 	,T2.ORG_ICON_FILE_ID ");
		sql.append(" 	,T2.ORG_NAME ");
		sql.append(" 	,T2.PROVINCE ");
		sql.append(" 	,T2.CITY ");
		sql.append(" 	,T2.ADDRESS ");
		sql.append(" 	,T2.ORG_TYPE ");
		sql.append(" 	,T2.CONTACT_NAME ");
		sql.append(" 	,T2.CONTACT_TEL ");
		sql.append(" 	,T2.CONTACT_MOBILE ");
		sql.append(" FROM USER T1  ");
		sql.append(" INNER JOIN ORG_DETAIL T2 ON ");
		sql.append(" T1.OID_USER_ID = T2.OID_USER_ID ");
		sql.append(" WHERE T2.CONTACT_MOBILE = ? ");

		return singleQuery(sql.toString(), new Object[]{mobile});
	}

	@Override
	public Row checkRefcodeExists(String invitationCode) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 		REFCODE ");
		sql.append(" FROM 	USER_DETAIL ");
		sql.append(" WHERE 	REFCODE = ? ");

		return singleQuery(sql.toString(), new Object[]{invitationCode});
	}



	@Override
	public int insUserAccount(UserDto user) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER_ACCOUNT ");
		sql.append(" (OID_USER_ID) ");
		sql.append(" VALUES ");
		sql.append(" (?) ");
		return update(sql.toString(), new Object[]{user.getOidUserId()});
	}


	@Override
	public int insUserPoint(UserDto user) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER_POINT_M ");
		sql.append(" (OID_USER_ID) ");
		sql.append(" VALUES ");
		sql.append(" (?) ");
		return update(sql.toString(), new Object[]{user.getOidUserId()});
	}


	@Override
	public int insUserAddress(UserDto user) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER_ADDRESS ");
		sql.append(" (OID_USER_ID) ");
		sql.append(" VALUES ");
		sql.append(" (?) ");
		return update(sql.toString(), new Object[]{user.getOidUserId()});
	}

	@Override
	public int insUserIdVerification(UserDto user) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER_ID_VERIFICATION ");
		sql.append(" (OID_USER_ID) ");
		sql.append(" VALUES ");
		sql.append(" (?) ");
		return update(sql.toString(), new Object[]{user.getOidUserId()});
	}

	@Override
	public int insUser(UserDto user) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER ");
		sql.append(" (OID_USER_ID,MOBILE,EMAIL,USER_PWD,USER_PWD_STRENGTH,USER_TYPE,INVESTMENT_FLG,FINANCING_FLG, OID_CUSTOMER_SERVICE_ID, GUARANTEE_FLG,SALT,REGISTER_SOURCE) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?,?,?,?,?) ");
		Object[] params = new Object[]{
				user.getOidUserId(),
				user.getMobile(),
				user.getEmail(),
				user.getUserPwd(),
				user.getUserPwdStrength(),
				user.getUserType(),
				user.getInvestmentFlg(),
				user.getFinancingFlg(),
				user.getOidCustomerserviceId(),
				user.getGuaranteeFlg(),
				user.getSalt(),
				user.getRegisterSource()};
		return update(sql.toString(), params);
	}

	@Override
	public int insUserDetail(UserDto user) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER_DETAIL   ");
		sql.append(" (OID_USER_ID  ");
		sql.append(" ,REFCODE)	   ");
		sql.append(" VALUES ");
		sql.append(" (?,?) ");
		return update(sql.toString(), new Object[]{user.getOidUserId(),user.getRefcode()});
	}

	@Override
	public int insIntroducer(UserDto user) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER_INTRODUCER_M ");
		sql.append(" (OID_USER_ID,INTRODUCER_OID_USER_ID,INS_USER_ID,UPD_USER_ID) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?) ");
		Object[] params = new Object[]{
				user.getOidUserId(),
				user.getIntroducerUserId(),
				user.getUpdUserId(),
				user.getUpdUserId()};
		return update(sql.toString(), params);
	}

	@Override
	public Row getUser(UserDto user) {
		// SQL文生成
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 T1.OID_USER_ID ");
		sql.append(" 	,T1.USER_ID ");
		sql.append(" 	,T1.MOBILE ");
		sql.append(" 	,T1.EMAIL ");
		sql.append(" 	,T1.USER_PWD ");
		sql.append(" 	,T1.USER_PAY_PWD ");
		sql.append(" 	,T1.LOGIN_TIMES ");
		sql.append(" 	,T1.LOGIN_ERROR_TIMES ");
		sql.append(" 	,T1.LAST_LOGIN_DATE ");
		sql.append(" 	,T1.DEL_FLG ");
		sql.append(" 	,T1.USER_TYPE ");
		sql.append(" 	,T1.INVESTMENT_FLG ");
		sql.append(" 	,T1.FINANCING_FLG ");
		sql.append(" 	,T1.EMAIL_VERIFY_FLG ");
		sql.append(" 	,T1.INS_DATE ");
		sql.append(" 	,T1.SALT ");
		sql.append(" 	,T1.TYPE ");
		sql.append(" 	,T1.SS_FLAG ");
		sql.append(" 	,T1.TC_FLAG ");
		sql.append(" 	,T1.RA_FLAG ");
		sql.append(" FROM USER T1  ");
		sql.append(" WHERE T1.DEL_FLG = '0' ");
		List<Object> params = new ArrayList<>();
		if (!StringUtils.isEmpty(user.getEmail())) {
			sql.append(" AND T1.EMAIL = ? ");
			params.add(user.getEmail());
		}
		if (!StringUtils.isEmpty(user.getMobile())) {
			sql.append(" AND T1.MOBILE = ? ");
			params.add(user.getMobile());
		}
		return singleQuery(sql.toString(), params.toArray());
	}


	@Override
	public Row getExistUser(UserDto user) {
		// SQL文生成
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 'Y' ");
		sql.append(" FROM USER T1  ");
		sql.append(" WHERE T1.DEL_FLG = '0' ");
		sql.append(" AND T1.OID_USER_ID = ? ");

		return singleQuery(sql.toString(), user.getOidUserId());
	}

	@Override
	public int updUser(UserDto user) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER ");
		sql.append(" SET DEL_FLG = DEL_FLG ");
		List<Object> params = new ArrayList<>();
		if (!StringUtils.isEmpty(user.getMobile())) {
			sql.append(" ,MOBILE = ? ");
			params.add(user.getMobile());
		}
		if (!StringUtils.isEmpty(user.getEmail())) {
			sql.append(" ,EMAIL = ? ");
			params.add(user.getEmail());
		}
		if (!StringUtils.isEmpty(user.getUserPwd())) {
			sql.append(" ,USER_PWD = ? ");
			params.add(user.getUserPwd());
		}
		if (!StringUtils.isEmpty(user.getUserPwdStrength())) {
			sql.append(" ,USER_PWD_STRENGTH = ? ");
			params.add(user.getUserPwdStrength());
		}
		if (!StringUtils.isEmpty(user.getUserPayPwd())) {
			sql.append(" ,USER_PAY_PWD = ? ");
			params.add(user.getUserPayPwd());
		}
		if (!StringUtils.isEmpty(user.getUserType())) {
			sql.append(" ,USER_TYPE = ? ");
			params.add(user.getUserType());
		}
		if (!StringUtils.isEmpty(user.getInvestmentFlg())) {
			sql.append(" ,INVESTMENT_FLG = ? ");
			params.add(user.getInvestmentFlg());
		}
		if (!StringUtils.isEmpty(user.getFinancingFlg())) {
			sql.append(" ,FINANCING_FLG = ? ");
			params.add(user.getFinancingFlg());
		}
		if (!StringUtils.isEmpty(user.getEmailVerifyFlg())) {
			sql.append(" ,EMAIL_VERIFY_FLG = ? ");
			params.add(user.getEmailVerifyFlg());
		}
		if (!StringUtils.isEmpty(user.getLoginTimes())) {
			sql.append(" ,LOGIN_TIMES = ? ");
			params.add(user.getLoginTimes());
		}
		if (!StringUtils.isEmpty(user.getLoginErrorTimes())) {
			sql.append(" ,LOGIN_ERROR_TIMES = ? ");
			params.add(user.getLoginErrorTimes());
		}
		if (user.getLastLoginDate() != null) {
			sql.append(" ,LAST_LOGIN_DATE = ? ");
			params.add(user.getLastLoginDate());
		}
		if (!StringUtils.isEmpty(user.getOidCustomerserviceId())) {
			sql.append(" ,OID_CUSTOMER_SERVICE_ID = ? ");
			params.add(user.getOidCustomerserviceId());
		}
		if (!StringUtils.isEmpty(user.getAutoFinanceTendeFlg())) {
			sql.append(" ,AUTO_FINANCE_TENDER_FLG = ? ");
			params.add(user.getAutoFinanceTendeFlg());
		}
		if (!StringUtils.isEmpty(user.getAutoFinanceProdFlg())) {
			sql.append(" ,AUTO_FINANCE_PROD_FLG = ? ");
			params.add(user.getAutoFinanceProdFlg());
		}
		if (!StringUtils.isEmpty(user.getAutoShowOrHideFlg())) {
			sql.append(" ,AUTO_SHOW_OR_HIDE_FLG = ? ");
			params.add(user.getAutoShowOrHideFlg());
		}
		if (!StringUtils.isEmpty(user.getType())) {
			sql.append(" ,TYPE = ? ");
			params.add(user.getType());
		}
		if (!StringUtils.isEmpty(user.getSignFlag())) {
			sql.append(" ,SIGN_FLAG = ? ");
			params.add(user.getSignFlag());
		}
		sql.append(" WHERE OID_USER_ID = ? ");
		params.add(user.getOidUserId());
		sql.append(" AND DEL_FLG = '0' ");
		return update(sql.toString(), params.toArray());
	}



	@Override
	public Row getUserPersonal(String oidUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER ");
		sql.append(" SET DEL_FLG = DEL_FLG ");
		List<Object> params = new ArrayList<>();
		return singleQuery(sql.toString(), params.toArray());
	}

	@Override
	public int updRegistUser(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER ");
		sql.append(" SET SS_FLAG = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" AND DEL_FLG = '0' ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("ssFlag"));
		params.add((String)parame.get("loginId"));

		return update(sql.toString(), params.toArray());
	}

	@Override
	public int updBankUser(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER ");
		sql.append(" SET TC_FLAG = ? ");
		sql.append(" , USER_PAY_PWD = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" AND DEL_FLG = '0' ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("tcFlag"));
		params.add((String)parame.get("payPassword"));
		params.add((String)parame.get("loginId"));

		return update(sql.toString(), params.toArray());
	}

	@Override
	public int updRiskUser(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER ");
		sql.append(" SET RA_FLAG = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" AND DEL_FLG = '0' ");

		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("raFlag"));
		params.add((String)parame.get("loginId"));
		return update(sql.toString(), params.toArray());
	}


	@Override
	public int updRiskUserDetail(Map<String, Object> parame) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER_DETAIL ");
		sql.append(" SET RISK_LEVEL = ? ");
		sql.append(" ,INVEST_TYPE = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");
		List<Object> params = new ArrayList<>();
		params.add((String)parame.get("riskLevel"));
		params.add((String)parame.get("investType"));
		params.add((String)parame.get("loginId"));
		return update(sql.toString(), params.toArray());
	}




	@Override
	public Row getUserInfo(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 T1.OID_USER_ID ");
		sql.append(" 	,T1.MOBILE ");
		sql.append(" 	,T1.EMAIL ");
		sql.append(" 	,T1.USER_PWD ");
		sql.append(" 	,T1.USER_TYPE ");
		sql.append(" 	,T1.USER_PAY_PWD ");
		sql.append(" 	,T1.TC_FLAG ");
		sql.append(" 	,T1.RA_FLAG ");
		sql.append(" 	,T1.INVESTMENT_FLG ");
		sql.append(" 	,T2.USER_ICON_FILE_ID ");
		sql.append(" 	,T2.ID_CARD ");
		sql.append(" 	,T2.USER_NAME ");
		sql.append(" 	,T2.RISK_LEVEL ");
		sql.append(" FROM USER T1  ");
		sql.append(" LEFT JOIN USER_DETAIL T2 ON ");
		sql.append(" T1.OID_USER_ID = T2.OID_USER_ID ");
		sql.append(" WHERE T1.OID_USER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add(loginId);

		return singleQuery(sql.toString(), params.toArray());
	}

	@Override
	public int updUserIconFileId(String loginId,String headPortrait) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER_DETAIL ");
		sql.append(" SET USER_ICON_FILE_ID = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add(headPortrait);
		params.add(loginId);

		return update(sql.toString(), params.toArray());
	}

	@Override
	public int updUserPhone(String loginId,String userPhone) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER ");
		sql.append(" SET MOBILE = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add(userPhone);
		params.add(loginId);

		return update(sql.toString(), params.toArray());
	}

	@Override
	public int updPayPassword(String loginId, String payPassword) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER ");
		sql.append(" SET USER_PAY_PWD = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add(payPassword);
		params.add(loginId);

		return update(sql.toString(), params.toArray());
	}
	@Override
	public boolean saveConfirmRiskWarning(String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER_DETAIL ");
		sql.append(" SET CONFIRM_RISK_WARNING = 1,CONFIRMDATE=now() ");
		sql.append(" WHERE OID_USER_ID = ? ");

		return update(sql.toString(), new Object[]{userId})>0;
	}

	@Override
	public List<Row> selIntroducerPreson(String uId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u.MOBILE,d.USER_NAME,DATE_FORMAT(u.INS_DATE,'%Y-%m-%d %H:%i:%S') INS_DATE from USER_INTRODUCER_M m");
		sql.append(" INNER JOIN USER u");
		sql.append(" ON m.OID_USER_ID=u.OID_USER_ID");
		sql.append(" INNER JOIN USER_DETAIL d");
		sql.append(" ON m.OID_USER_ID=d.OID_USER_ID");
		sql.append(" where INTRODUCER_OID_USER_ID = ?");
		sql.append(" ORDER BY INS_DATE DESC");
		return queryForList(sql.toString(),new Object[]{ uId});
	}
	/**
	 * 更新用户身份表的支付密码
	 * @param loginId
	 * @param newPayPassword
	 * @return
	 */
	@Override
	public int updPayPwdForUserIdVer(String loginId, String newPayPassword) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" USER_ID_VERIFICATION ");
		sql.append(" SET PAY_PWD = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add(newPayPassword);
		params.add(loginId);

		return update(sql.toString(), params.toArray());
	}
	//邀请码查询邀请人手机号
	@Override
	public Row selInvitationPhone(String invitationCode) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u.MOBILE from USER_DETAIL d,user u ");
		sql.append(" WHERE d.OID_USER_ID = u.OID_USER_ID ");
		sql.append("  AND	REFCODE = ? ");
		return singleQuery(sql.toString(), new Object[]{invitationCode});
	}
	//查询用户是否有我的项目权限
	@Override
	public int getMyProject(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(1)");
		sql.append(" FROM projects_info_des   ");
		sql.append(" WHERE OID_USER_ID = ? ");
		List<Object> params = new ArrayList<>();
		params.add(loginId);

		return getCount(sql.toString(), params.toArray());
	}

	//设置用户敏感信息查看方式
	@Override
	public int setSensitiveInfo(String loginId,String status) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE user SET SENSITIVEINFO_FLAG = ?");
		sql.append(" WHERE OID_USER_ID = ?");
		List<Object> params = new ArrayList<>();
		params.add(status);
		params.add(loginId);
		return update(sql.toString(), params.toArray());
	}

	//设置用户认证投资人
	@Override
	public int setcertifiedInvestor(String loginId,String investor) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE user SET CERTIFIED_INVESTOR = ?");
		sql.append(" WHERE OID_USER_ID = ?");
		List<Object> params = new ArrayList<>();
		params.add(investor);
		params.add(loginId);
		return update(sql.toString(), params.toArray());
	}
	//查询用户银行卡当日限额
	@Override
	public Row selDayLimitAmt(String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(DAY_LIMIT_AMT/100,0) amt,SINGLE_LIMIT_AMT singleLimit from bank_withhold_quota ");
		sql.append(" WHERE BANK_CD = ");
		sql.append(" (SELECT BANK from USER_ACCOUNT_BIND_BANK ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" AND MAIN_FLG = 1)");
		return singleQuery(sql.toString(), new Object[]{userId});
	}

	@Override
	public Row selIntroducer(String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u.MOBILE mobile,d.USER_NAME userName,DATE_FORMAT(m.INS_DATE,'%Y-%m-%d') insDate from USER_INTRODUCER_M m");
		sql.append(" INNER JOIN USER u");
		sql.append(" ON m.INTRODUCER_OID_USER_ID=u.OID_USER_ID");
		sql.append(" INNER JOIN USER_DETAIL d");
		sql.append(" ON m.INTRODUCER_OID_USER_ID=d.OID_USER_ID");
		sql.append(" where m.OID_USER_ID = ?");
		return singleQuery(sql.toString(),new Object[]{ userId});
	}
	/**
	 * 认证投资人
	 * @param realInvest
	 * @return
	 */

	@Override
	public int certifiedInvestor(Map<String, String> realInvest) {
		StringBuilder sql = new StringBuilder();
		List<Object> paramList = new ArrayList<>();
		sql.append("UPDATE `user` set AUTH_FLAG = ?,CERTIFIED_INVESTOR=? where OID_USER_ID=?");
		if("4".equals(realInvest.get("investment"))){
			paramList.add("0");
		}else{
			paramList.add("1");

		}
		paramList.add(realInvest.get("investment"));
		paramList.add(realInvest.get("userId"));
		int updUser = update(sql.toString(), paramList.toArray());
		sql.setLength(0);
		paramList.clear();

		sql.append("UPDATE user_detail t set t.ID_CARD = ?,t.USER_NAME = ? where t.OID_USER_ID = ?");
		paramList.add(realInvest.get("idCard"));
		paramList.add(realInvest.get("realName"));
		paramList.add(realInvest.get("userId"));
		int updUserDetail = update(sql.toString(), paramList.toArray());
		if(updUserDetail>0&&updUser>0){
			return 1;
		}else{
			return 0;
		}
	}
	/**
	 * 获取用户已有状态信息
	 * @param userId
	 * @return
	 */
	@Override
	public Row getUserState(String userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select u.AUTH_FLAG,");
		sql.append("u.MOBILE,");
		sql.append("u.ss_flag,");
		sql.append("u.tc_flag,");
		sql.append("u.ra_flag,");
		sql.append("u.SENSITIVEINFO_FLAG,");
		sql.append("u.CERTIFIED_INVESTOR,");
		sql.append("u.SIGN_FLAG,");
		sql.append("(select ud.USER_NAME from user_detail ud where ud.OID_USER_ID=u.OID_USER_ID) USER_NAME,");
		sql.append("(select ud.ID_CARD from user_detail ud where ud.OID_USER_ID=u.OID_USER_ID) ID_CARD,");
		sql.append("(select ud.RISK_LEVEL from user_detail ud where ud.OID_USER_ID=u.OID_USER_ID) RISK_LEVEL,");
		sql.append("(select ub.CARD_NO from user_account_bind_bank ub where ub.OID_USER_ID=u.OID_USER_ID) CARD_NO,");
		sql.append("(select ub.BANK from user_account_bind_bank ub where ub.OID_USER_ID=u.OID_USER_ID) BANK,");
		sql.append("(select ub.NEED_BANK_SIGN from user_account_bind_bank ub where ub.OID_USER_ID=u.OID_USER_ID) NEED_BANK_SIGN,");
		sql.append("(select ub.MOBILE from user_account_bind_bank ub where ub.OID_USER_ID=u.OID_USER_ID) RESERVE_MOBILE,");
		sql.append("(select bq.BANK_NM from user_account_bind_bank ub,bank_withhold_quota bq where ub.OID_USER_ID=u.OID_USER_ID and bq.BANK_CD=ub.BANK) BANK_NM ");
		sql.append(" FROM user u ");
		sql.append(" where u.OID_USER_ID=?");
		return singleQuery(sql.toString(), userId);
	}

}
