package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.ILoginDao;

/**
 * 登录业务数据操作的实现类。
 */

public class LoginDaoImpl extends BaseDao implements ILoginDao {

	@Override
	public Row adminLogin(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" T1.OID_ADMIN_USER_ID ");
		sql.append(" ,T1.ADMIN_USER_ID ");
		sql.append(" ,T1.ADMIN_USER_NAME ");
		sql.append(" ,T1.ADMIN_USER_PWD ");
		sql.append(" ,T1.LOGIN_ERROR_TIMES ");
		sql.append(" ,T1.SYS_FLG ");
		sql.append(" ,T1.OID_GROUP_ID AS GROUP_ID ");
		sql.append(" ,T5.GROUP_NAME ");
		if ("mysql".equalsIgnoreCase(getDbType())) {
			sql.append(" ,GROUP_CONCAT(DISTINCT T2.OID_ROLE_ID) AS ROLE_ID ");
		} else if ("oracle".equalsIgnoreCase(getDbType())) {
			sql.append(" ,WM_CONCAT(DISTINCT T2.OID_ROLE_ID) AS ROLE_ID ");
		}
		sql.append(" FROM ADMIN_USER T1 ");
		sql.append(" LEFT JOIN ADMIN_USER_ROLE T2 ON T1.OID_ADMIN_USER_ID = T2.OID_ADMIN_USER_ID ");
		sql.append(" LEFT JOIN GROUP_M T5 ON T1.OID_GROUP_ID = T5.OID_GROUP_ID ");
		sql.append(" WHERE ");
		sql.append(" T1.ADMIN_USER_ID = ? ");
		sql.append(" AND T1.DEL_FLG = '0' ");
		sql.append("  GROUP BY T1.ADMIN_USER_ID ");

		return query(sql.toString(), new Object[]{loginId});
	}

	@Override
	public Row login(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" T1.OID_USER_ID ");
		sql.append(" ,T1.USER_ID ");
		sql.append(" ,T1.MOBILE ");
		sql.append(" ,T1.EMAIL ");
		sql.append(" ,T1.USER_PWD ");
		sql.append(" ,T1.USER_PAY_PWD ");
		sql.append(" ,T1.USER_PWD_STRENGTH ");
		sql.append(" ,T1.USER_TYPE ");
		sql.append(" ,T1.INVESTMENT_FLG ");
		sql.append(" ,T1.FINANCING_FLG ");
		sql.append(" ,T1.EMAIL_VERIFY_FLG ");
		sql.append(" ,T1.LOGIN_ERROR_TIMES ");
		sql.append(" ,T1.LOGIN_TIMES ");
		sql.append(" ,T1.LAST_LOGIN_IP ");
		sql.append(" ,T1.LAST_LOGIN_DATE ");
		sql.append(" ,T1.SS_FLAG ");
		sql.append(" ,T1.TC_FLAG ");
		sql.append(" ,T1.RA_FLAG ");
		sql.append(" ,T1.SALT ");
		sql.append(" ,T1.INS_DATE ");
		sql.append(" ,T1.SENSITIVEINFO_FLAG ");
		sql.append(" ,T1.CERTIFIED_INVESTOR ");
		sql.append(" ,T1.SIGN_FLAG ");
		sql.append(" ,T1.AUTH_FLAG ");
		sql.append(" FROM USER T1 ");
		sql.append(" WHERE ");
		sql.append(" (T1.MOBILE = ? ");
		sql.append(" OR T1.EMAIL = ?) ");
		sql.append(" AND T1.DEL_FLG = '0' ");

		Object[] params = new Object[]{loginId, loginId};

		return query(sql.toString(), params);
	}

	@Override
	public int addAdminLoginErrorTimes(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append("  ADMIN_USER ");
		sql.append("  SET LOGIN_ERROR_TIMES = LOGIN_ERROR_TIMES + 1 ");
		sql.append(" WHERE ");
		sql.append(" ADMIN_USER_ID = ? ");

		return update(sql.toString(), new Object[]{loginId});
	}

	@Override
	public int addLoginTimes(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append("  USER ");
		sql.append("  SET LOGIN_TIMES = LOGIN_TIMES + 1 ");
		sql.append(" WHERE ");
		sql.append(" (MOBILE = ? ");
		sql.append(" OR EMAIL = ?) ");
		sql.append(" AND DEL_FLG = '0' ");

		Object[] params = new Object[]{loginId, loginId};

		return update(sql.toString(), params);
	}

	@Override
	public boolean isLoginErrorTime(String loginId, int time) {
		StringBuilder sql = new StringBuilder();
		// IS_FLG=0，解锁时间未到，处于锁定状态
		// IS_FLG=1，解锁时间已到，处于解锁状态（正常状态）
		sql.append(" SELECT  ");
		sql.append("   CASE ");
		sql.append("     WHEN DATE_ADD(UPD_DATE, INTERVAL ? HOUR) < NOW()  ");
		sql.append("     THEN 1  ");
		sql.append("     ELSE 0  ");
		sql.append("   END AS IS_FLG  ");
		sql.append(" FROM ");
		sql.append("   USER  ");
		sql.append(" WHERE ");
		sql.append(" (MOBILE = ? ");
		sql.append(" OR EMAIL = ?) ");
		sql.append(" AND DEL_FLG = '0' ");

		Object[] params = new Object[]{time, loginId, loginId};

		Row sqlData =  query(sql.toString(), params);

		if (!sqlData.isEmpty() && sqlData.size()>0) {

			if (sqlData.getInt("IS_FLG")==1) {
				// 解锁状态时，错误登录次数还原为0
				sql = new StringBuilder();
				sql.append("UPDATE ");
				sql.append("  USER ");
				sql.append("  SET LOGIN_ERROR_TIMES = 0 ");
				sql.append(" WHERE ");
				sql.append(" MOBILE = ? ");
				sql.append(" OR EMAIL = ? ");

				params = new Object[]{loginId, loginId};
				update(sql.toString(), params);

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int addLoginErrorTimes(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append("  USER ");
		sql.append("  SET LOGIN_ERROR_TIMES = LOGIN_ERROR_TIMES + 1 ");
		sql.append(" WHERE ");
		sql.append(" (MOBILE = ? ");
		sql.append(" OR EMAIL = ?) ");
		sql.append(" AND DEL_FLG = '0' ");

		Object[] params = new Object[]{loginId, loginId};

		return update(sql.toString(), params);
	}

	@Override
	public int updAdminLastLoginDate(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append("  ADMIN_USER ");
		sql.append("  SET LOGIN_ERROR_TIMES = 0 ");
		sql.append("  , LAST_LOGIN_DATE = now() ");
		sql.append(" WHERE ");
		sql.append(" ADMIN_USER_ID = ? ");

		return update(sql.toString(), new Object[]{loginId});
	}

	@Override
	public int updLastLoginDate(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append("  USER ");
		sql.append("  SET LOGIN_ERROR_TIMES = 0 ");
		sql.append("  , LAST_LOGIN_DATE = now() ");
		sql.append(" WHERE ");
		sql.append(" MOBILE = ? ");
		sql.append(" OR EMAIL = ? ");

		Object[] params = new Object[]{loginId, loginId};

		return update(sql.toString(), params);
	}

	@Override
	public List<Row> getACL(String[] roleId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("  DISTINCT T2.MENU_ID ");
		sql.append(" ,T2.PARENT_MENU_ID ");
		sql.append(" ,T2.MENU_NAME ");
		sql.append(" ,T2.URL ");
		sql.append(" ,T2.MENU_RANK ");
		sql.append(" ,CASE T2.MENU_RANK WHEN '4' THEN ");
		sql.append("  GET_MENU_NAV(T2.PARENT_MENU_ID) ");
		sql.append("  ELSE ");
		sql.append("  GET_MENU_NAV(T2.MENU_ID) ");
		sql.append("  END MENU_NAV ");
		sql.append(" FROM ACCESS_CONTROL T1 ");
		sql.append(" INNER JOIN ADMIN_MENU_M T2 ON T1.MENU_ID = T2.MENU_ID AND T2.DEL_FLG = '0' ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(" AND T1.OID_ROLE_ID IN (");
		List<Object> params = new ArrayList<>();
		for (String param : roleId) {
			sql.append("?,");
			params.add(param);
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(") ");
		sql.append(" ORDER BY T2.PARENT_MENU_ID, T2.SORT ");
		return queryForList(sql.toString(), params.toArray());
	}

	@Override
	public Row getUserInfo(String oidUserId, String userType) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		if ("0".equals(userType)) {
			sql.append(" T.NICKNAME ");
			sql.append(" ,T.USER_ICON_FILE_ID ");
			sql.append(" ,T.USER_NAME ");
			sql.append(" ,T.ID_CARD ");
			sql.append(" ,T.USER_POINT ");
			sql.append(" ,T.ID_CARD_VERIFY_FLG ");
			sql.append(" ,T.WEALTH_MANAGER_FLG ");
			sql.append(" ,T.RECOMMENDED_BY_PHONE ");
			sql.append(" ,T.RISK_LEVEL ");
			sql.append(" ,T.REFCODE ");
			sql.append(" FROM USER_DETAIL T ");

		} else if ("1".equals(userType)) {
			sql.append(" T.ORG_NAME ");
			sql.append(" ,T.ORG_ICON_FILE_ID ");
			sql.append(" ,T.PROVINCE ");
			sql.append(" ,T.CITY ");
			sql.append(" ,T.ADDRESS ");
			sql.append(" ,T.ORG_TYPE ");
			sql.append(" ,T.BANKLICENSE_CODE ");
			sql.append(" ,T.ORG_NO ");
			sql.append(" ,T.BUSINESS_CODE ");
			sql.append(" ,T.TAX_NO ");
			sql.append(" ,T.LEGAL_NAME ");
			sql.append(" ,T.LEGAL_ID_CARD ");
			sql.append(" ,T.CONTACT_NAME ");
			sql.append(" ,T.CONTACT_EMAIL ");
			sql.append(" ,T.CONTACT_TEL ");
			sql.append(" ,T.CONTACT_MOBILE ");
			sql.append(" ,T.CONTACT_MOBILE_VERIFY_FLG ");
			sql.append(" FROM ORG_DETAIL T ");
		}
		sql.append(" WHERE T.OID_USER_ID = ? ");
		return query(sql.toString(), new Object[]{oidUserId});
	}
	@Override
	public Row selImgFlag(String mobile) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" T1.LOGIN_ERROR_TIMES ");
		sql.append(" FROM USER T1 ");
		sql.append(" WHERE ");
		sql.append(" (T1.MOBILE = ? ");
		sql.append(" OR T1.EMAIL = ?) ");
		sql.append(" AND T1.DEL_FLG = '0' ");
		return query(sql.toString(), new Object[]{mobile,mobile});
	}

}
