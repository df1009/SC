package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IDealRecordDao;
import com.zhongchou.common.dao.IMyProjectDao;

/**
 * 个人中心实现类。
 */

public class DealRecordDaoImpl extends BaseDao implements IDealRecordDao {

	//获取交易记录方法
	@Override
	public List<Row> getDealRecordList(String oidUserId,String beginDate,String endDate,String dateType,String projectType,String projectName, int pageSize,int curPage) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isEmpty(projectType)){
			sql.append("SELECT");
			sql.append(" OID_TENDER_ID,");
			sql.append(" INS_DATE,");
			sql.append(" PLATFORM_PROJECTS_SHORT_NM,");
			sql.append(" TENDER_AMOUNT,");
			sql.append(" CARD_NO,");
			sql.append(" BANK,");
			sql.append(" OID_PLATFORM_PROJECTS_ID,");
			sql.append(" TENDER_STATUS,");
			sql.append(" APP_LIST_IMG,");
			sql.append(" type");
			sql.append(" FROM(");
		}
		if("2".equals(projectType)||StringUtils.isEmpty(projectType)){//撤单
			sql.append(" SELECT ");
			sql.append(" T1.OID_TENDER_ID, ");
			sql.append(" T4.INS_DATE, ");
			sql.append(" T2.PLATFORM_PROJECTS_SHORT_NM, ");
			sql.append(" T1.TENDER_AMOUNT, ");
			sql.append(" T3.CARD_NO, ");
			sql.append(" T3.BANK, ");
			sql.append(" T1.OID_PLATFORM_PROJECTS_ID, ");
			sql.append(" T4.TENDER_STATUS, ");
			sql.append(" T5.APP_LIST_IMG, ");
			sql.append(" '2' type ");
			sql.append(" FROM ORDER_CANCELLATION_RECORD T4 ");
			sql.append(" INNER JOIN user_tender T1  ON T1.TENDER_SSN = T4.REQ_SSN ");
			sql.append(" INNER JOIN PROJECTS_INFO T2 ON T4.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" INNER JOIN USER_ACCOUNT_BIND_BANK T3 ON T1.OID_ACCOUNT_BIND_BANK_ID = T3.OID_ACCOUNT_BIND_BANK_ID ");
			sql.append(" LEFT JOIN PROJECTS_INFO_DES_ONLINE T5 ON T2.OID_PLATFORM_PROJECTS_ID = T5.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" WHERE T4.TENDER_STATUS IN(1,3)");
			if (!StringUtils.isEmpty(oidUserId)) {
				sql.append(" AND T4.OID_USER_ID = ? ");
				params.add(oidUserId);
			}
			if (!StringUtils.isEmpty(beginDate)) {
				sql.append(" AND DATE_FORMAT(T4.INS_DATE, '%Y-%m-%d') >= ? ");
				params.add(beginDate);
			}
			if (!StringUtils.isEmpty(endDate)) {
				sql.append(" AND DATE_FORMAT(T4.INS_DATE, '%Y-%m-%d') <= ? ");
				params.add(endDate);
			}
			if (!StringUtils.isEmpty(dateType)) {
				if("2".equals(dateType)){
					sql.append(" AND to_days(T4.INS_DATE) = to_days(now()) ");
				}
				if("3".equals(dateType)){
					sql.append(" AND DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(T4.INS_DATE) ");
				}
				if("4".equals(dateType)){
					sql.append(" AND T4.INS_DATE between date_sub(now(),interval 6 month) and now() ");
				}
				if("5".equals(dateType)){
					sql.append(" AND T4.INS_DATE between date_sub(now(),interval 12 month) and now() ");
				}
			}
		}
		if(StringUtils.isEmpty(projectType)){
			sql.append("UNION");
		}
		if("1".equals(projectType)||StringUtils.isEmpty(projectType)){//认购
			sql.append(" SELECT ");
			sql.append(" T1.OID_TENDER_ID, ");
			sql.append(" T1.PAY_DATE INS_DATE, ");
			sql.append(" T2.PLATFORM_PROJECTS_SHORT_NM, ");
			sql.append(" T1.TENDER_AMOUNT, ");
			sql.append(" T3.CARD_NO, ");
			sql.append(" T3.BANK, ");
			sql.append(" T1.OID_PLATFORM_PROJECTS_ID, ");
			sql.append(" T1.TENDER_STATUS, ");
			sql.append(" T5.APP_LIST_IMG, ");
			sql.append(" '1' type ");
			sql.append(" FROM user_tender T1 ");
			sql.append(" LEFT JOIN ");
			sql.append(" PROJECTS_INFO T2 ");
			sql.append(" ON T1.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" LEFT JOIN ");
			sql.append(" USER_ACCOUNT_BIND_BANK T3 ");
			sql.append(" ON T1.OID_ACCOUNT_BIND_BANK_ID = T3.OID_ACCOUNT_BIND_BANK_ID ");
			sql.append(" LEFT JOIN PROJECTS_INFO_DES_ONLINE T5 ON T2.OID_PLATFORM_PROJECTS_ID = T5.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" WHERE TENDER_STATUS IN(0,1,3)");

			if (!StringUtils.isEmpty(oidUserId)) {
				sql.append(" AND T1.OID_USER_ID = ? ");
				params.add(oidUserId);
			}
			if (!StringUtils.isEmpty(beginDate)) {
				sql.append(" AND DATE_FORMAT(T1.INS_DATE, '%Y-%m-%d') >= ? ");
				params.add(beginDate);
			}
			if (!StringUtils.isEmpty(endDate)) {
				sql.append(" AND DATE_FORMAT(T1.INS_DATE, '%Y-%m-%d') <= ? ");
				params.add(endDate);
			}
			if (!StringUtils.isEmpty(dateType)) {
				if("2".equals(dateType)){
					sql.append(" AND to_days(T1.INS_DATE) = to_days(now()) ");
				}
				if("3".equals(dateType)){
					sql.append(" AND DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(T1.INS_DATE) ");
				}
				if("4".equals(dateType)){
					sql.append(" AND T1.INS_DATE between date_sub(now(),interval 6 month) and now() ");
				}
				if("5".equals(dateType)){
					sql.append(" AND T1.INS_DATE between date_sub(now(),interval 12 month) and now() ");
				}
			}
		}
		if(StringUtils.isEmpty(projectType)){
			sql.append(" ) a ");
			if (!StringUtils.isEmpty(projectName)) {
				sql.append(" WHERE a.PLATFORM_PROJECTS_SHORT_NM like ? ");
				params.add("%" + projectName + "%");
			}
			sql.append(" ORDER BY a.INS_DATE DESC");
		}else{
			sql.append(" ORDER BY INS_DATE DESC");
		}
		return queryForList(sql.toString(),curPage,pageSize,params.toArray());
	}

	//获取交易条数
	@Override
	public int getDealRecordCnt(String oidUserId, String beginDate,
			String endDate, String dateType, String projectType,String projectName) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isEmpty(projectType)){
			sql.append("SELECT");
			sql.append(" sum(a)");
			sql.append(" FROM(");
		}
		if("2".equals(projectType)||StringUtils.isEmpty(projectType)){//撤单
			sql.append(" SELECT ");
			sql.append(" count(1) a ");
			sql.append(" FROM ORDER_CANCELLATION_RECORD T4 ");
			sql.append(" INNER JOIN user_tender T1  ON T1.TENDER_SSN = T4.REQ_SSN ");
			sql.append(" INNER JOIN PROJECTS_INFO T2 ON T4.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" INNER JOIN USER_ACCOUNT_BIND_BANK T3 ON T1.OID_ACCOUNT_BIND_BANK_ID = T3.OID_ACCOUNT_BIND_BANK_ID ");
			sql.append(" WHERE T4.TENDER_STATUS IN(1,3)");
			if (!StringUtils.isEmpty(projectName)) {
				sql.append(" AND T2.PLATFORM_PROJECTS_SHORT_NM like ? ");
				params.add("%" + projectName + "%");
			}
			if (!StringUtils.isEmpty(oidUserId)) {
				sql.append(" AND T4.OID_USER_ID = ? ");
				params.add(oidUserId);
			}
			if (!StringUtils.isEmpty(beginDate)) {
				sql.append(" AND DATE_FORMAT(T4.INS_DATE, '%Y-%m-%d') >= ? ");
				params.add(beginDate);
			}
			if (!StringUtils.isEmpty(endDate)) {
				sql.append(" AND DATE_FORMAT(T4.INS_DATE, '%Y-%m-%d') <= ? ");
				params.add(endDate);
			}
			if (!StringUtils.isEmpty(dateType)) {
				if("2".equals(dateType)){
					sql.append(" AND to_days(T4.INS_DATE) = to_days(now()) ");
				}
				if("3".equals(dateType)){
					sql.append(" AND DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(T4.INS_DATE) ");
				}
				if("4".equals(dateType)){
					sql.append(" AND T4.INS_DATE between date_sub(now(),interval 6 month) and now() ");
				}
				if("5".equals(dateType)){
					sql.append(" AND T4.INS_DATE between date_sub(now(),interval 12 month) and now() ");
				}
			}
		}
		if(StringUtils.isEmpty(projectType)){
			sql.append("UNION");
		}
		if("1".equals(projectType)||StringUtils.isEmpty(projectType)){//认购
			sql.append(" SELECT ");
			sql.append(" count(1) a");
			sql.append(" FROM user_tender T1 ");
			sql.append(" LEFT JOIN ");
			sql.append(" PROJECTS_INFO T2 ");
			sql.append(" ON T1.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" LEFT JOIN ");
			sql.append(" USER_ACCOUNT_BIND_BANK T3 ");
			sql.append(" ON T1.OID_ACCOUNT_BIND_BANK_ID = T3.OID_ACCOUNT_BIND_BANK_ID ");
			sql.append(" WHERE TENDER_STATUS IN(0,1,3)");
			if (!StringUtils.isEmpty(projectName)) {
				sql.append(" AND T2.PLATFORM_PROJECTS_SHORT_NM like ? ");
				params.add("%" + projectName + "%");
			}
			if (!StringUtils.isEmpty(oidUserId)) {
				sql.append(" AND T1.OID_USER_ID = ? ");
				params.add(oidUserId);
			}
			if (!StringUtils.isEmpty(beginDate)) {
				sql.append(" AND DATE_FORMAT(T1.INS_DATE, '%Y-%m-%d') >= ? ");
				params.add(beginDate);
			}
			if (!StringUtils.isEmpty(endDate)) {
				sql.append(" AND DATE_FORMAT(T1.INS_DATE, '%Y-%m-%d') <= ? ");
				params.add(endDate);
			}
			if (!StringUtils.isEmpty(dateType)) {
				if("2".equals(dateType)){
					sql.append(" AND to_days(T1.INS_DATE) = to_days(now()) ");
				}
				if("3".equals(dateType)){
					sql.append(" AND DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(T1.INS_DATE) ");
				}
				if("4".equals(dateType)){
					sql.append(" AND T1.INS_DATE between date_sub(now(),interval 6 month) and now() ");
				}
				if("5".equals(dateType)){
					sql.append(" AND T1.INS_DATE between date_sub(now(),interval 12 month) and now() ");
				}
			}


		}
		if(StringUtils.isEmpty(projectType)){
			sql.append(" ) b ");
		}
		return getCount(sql.toString(), params.toArray());
	}


	//获取银行名
	  @Override
		public Row getBankName(String tenderId) {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT ");
			sql.append(" bank_nm bankName,bank_cd bankCd");
			sql.append(" FROM bank_withhold_quota  ");
			sql.append(" WHERE ");
			sql.append(" bank_cd= ? ");
			return query(sql.toString(),new Object[]{tenderId});

		}

}
