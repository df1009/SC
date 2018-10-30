package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IPCDealRecordDao;

/**
 * 个人中心实现类。
 */

public class PCDealRecordDaoImpl extends BaseDao implements IPCDealRecordDao {

	//获取交易记录方法
	@Override
	public List<Row> getDealRecordList(Map reqMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isEmpty(reqMap.get("projectType"))){
			sql.append("SELECT");
			sql.append(" projectMainTitle,");
			sql.append(" date,");
			sql.append(" insDate,");
			sql.append(" statusTp,");
			sql.append(" amount,");
			sql.append(" oidUserId,");
			sql.append(" tenderSsn,");
			sql.append(" tenderId");
			sql.append(" FROM(");
		}
		if("2".equals(reqMap.get("projectType"))||StringUtils.isEmpty(reqMap.get("projectType"))){//退款成功
			sql.append(" SELECT ");
			sql.append(" p.PROJECT_MAIN_TITLE projectMainTitle ");
			sql.append(" ,DATE_FORMAT(b.UPD_DATE,'%Y-%m-%d') date ");
			sql.append(" ,b.INS_DATE insDate ");
			sql.append(" ,'2' statusTp ");
			sql.append(" ,b.BACK_AMT amount ");
			sql.append(" ,b.OID_USER_ID oidUserId ");
			sql.append(" ,b.TENDER_SSN tenderSsn ");
			sql.append(" ,b.OID_TENDER_ID tenderId ");
			sql.append(" from order_backamt_record b ");
			sql.append(" LEFT JOIN projects_info_des_online p ON b.OID_PLATFORM_PROJECTS_ID = p.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" WHERE b.TENDER_STATUS = 3 ");
			if (!StringUtils.isEmpty(reqMap.get("oidUserId"))) {
				sql.append(" AND b.OID_USER_ID =? ");
				params.add(reqMap.get("oidUserId"));
			}
			if (!StringUtils.isEmpty(reqMap.get("beginDate"))) {
				sql.append(" AND DATE_FORMAT(b.UPD_DATE, '%Y-%m-%d') >= ? ");
				params.add(reqMap.get("beginDate"));
			}
			if (!StringUtils.isEmpty(reqMap.get("endDate"))) {
				sql.append(" AND DATE_FORMAT(b.UPD_DATE, '%Y-%m-%d') <= ? ");
				params.add(reqMap.get("endDate"));
			}
			if (!StringUtils.isEmpty(reqMap.get("dateType"))) {
				if("1".equals(reqMap.get("dateType"))){
					sql.append(" AND b.INS_DATE between date_sub(now(),interval 1 month) and now()");
				}
				if("2".equals(reqMap.get("dateType"))){
					sql.append(" AND b.INS_DATE between date_sub(now(),interval 3 month) and now() ");
				}
				if("3".equals(reqMap.get("dateType"))){
					sql.append(" AND b.INS_DATE between date_sub(now(),interval 12 month) and now() ");
				}
			}
		}
		if(StringUtils.isEmpty(reqMap.get("projectType"))){
			sql.append("UNION ALL");
		}
		if("1".equals(reqMap.get("projectType"))||StringUtils.isEmpty(reqMap.get("projectType"))){//用户购买付款
			sql.append(" SELECT ");
			sql.append(" p.PROJECT_MAIN_TITLE projectMainTitle ");
			sql.append(" ,DATE_FORMAT(t.UPD_DATE,'%Y-%m-%d') date ");
			sql.append(" ,t.INS_DATE insDate ");
			sql.append(" ,'1' statusTp ");
			sql.append(" ,t.TENDER_AMOUNT amount ");
			sql.append(" ,t.OID_USER_ID oidUserId ");
			sql.append(" ,t.TENDER_SSN tenderSsn ");
			sql.append(" ,t.OID_TENDER_ID tenderId ");
			sql.append(" from user_tender t ");
			sql.append(" LEFT JOIN projects_info_des_online p ON t.OID_PLATFORM_PROJECTS_ID = p.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" WHERE t.TENDER_STATUS in(0,1,3) ");
			if (!StringUtils.isEmpty(reqMap.get("oidUserId"))) {
				sql.append(" AND t.OID_USER_ID = ? ");
				params.add(reqMap.get("oidUserId"));
			}
			if (!StringUtils.isEmpty(reqMap.get("beginDate"))) {
				sql.append(" AND DATE_FORMAT(t.UPD_DATE, '%Y-%m-%d') >= ? ");
				params.add(reqMap.get("beginDate"));
			}
			if (!StringUtils.isEmpty(reqMap.get("endDate"))) {
				sql.append(" AND DATE_FORMAT(t.UPD_DATE, '%Y-%m-%d') <= ? ");
				params.add(reqMap.get("endDate"));
			}
			if (!StringUtils.isEmpty(reqMap.get("dateType"))) {
				if("1".equals(reqMap.get("dateType"))){
					sql.append(" AND t.INS_DATE between date_sub(now(),interval 1 month) and now()");
				}
				if("2".equals(reqMap.get("dateType"))){
					sql.append(" AND t.INS_DATE between date_sub(now(),interval 3 month) and now() ");
				}
				if("3".equals(reqMap.get("dateType"))){
					sql.append(" AND t.INS_DATE between date_sub(now(),interval 12 month) and now() ");
				}
			}
		}
		if(StringUtils.isEmpty(reqMap.get("projectType"))){
			sql.append(" ) a ");
			sql.append(" ORDER BY a.insDate DESC");
		}else{
			sql.append(" ORDER BY insDate DESC");
		}
		System.out.println(sql);
		System.out.println(params);
		return queryForList(sql.toString(),(int)reqMap.get("curPage"),(int)reqMap.get("pageSize"),params.toArray());
	}

	//获取交易条数
	@Override
	public int getDealRecordCnt(Map reqMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isEmpty(reqMap.get("projectType"))){
			sql.append("SELECT");
			sql.append(" sum(statusTp)");
			sql.append(" FROM(");
		}
		if("2".equals(reqMap.get("projectType"))||StringUtils.isEmpty(reqMap.get("projectType"))){//退款
			sql.append(" SELECT ");
			sql.append(" count(1) statusTp ");
			sql.append(" from order_backamt_record b ");
			sql.append(" LEFT JOIN projects_info_des_online p ON b.OID_PLATFORM_PROJECTS_ID = p.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" WHERE b.TENDER_STATUS = 3 ");
			if (!StringUtils.isEmpty(reqMap.get("oidUserId"))) {
				sql.append(" AND b.OID_USER_ID =? ");
				params.add(reqMap.get("oidUserId"));
			}
			if (!StringUtils.isEmpty(reqMap.get("beginDate"))) {
				sql.append(" AND DATE_FORMAT(b.UPD_DATE, '%Y-%m-%d') >= ? ");
				params.add(reqMap.get("beginDate"));
			}
			if (!StringUtils.isEmpty(reqMap.get("endDate"))) {
				sql.append(" AND DATE_FORMAT(b.UPD_DATE, '%Y-%m-%d') <= ? ");
				params.add(reqMap.get("endDate"));
			}
			if (!StringUtils.isEmpty(reqMap.get("dateType"))) {
				if("1".equals(reqMap.get("dateType"))){
					sql.append(" AND b.INS_DATE between date_sub(now(),interval 1 month) and now()");
				}
				if("2".equals(reqMap.get("dateType"))){
					sql.append(" AND b.INS_DATE between date_sub(now(),interval 3 month) and now() ");
				}
				if("3".equals(reqMap.get("dateType"))){
					sql.append(" AND b.INS_DATE between date_sub(now(),interval 12 month) and now() ");
				}
			}
		}
		if(StringUtils.isEmpty(reqMap.get("projectType"))){
			sql.append("UNION ALL");
		}
		if("1".equals(reqMap.get("projectType"))||StringUtils.isEmpty(reqMap.get("projectType"))){//认购
			sql.append(" SELECT ");
			sql.append(" count(1) statusTp ");
			sql.append(" from user_tender t ");
			sql.append(" LEFT JOIN projects_info_des_online p ON t.OID_PLATFORM_PROJECTS_ID = p.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" WHERE t.TENDER_STATUS in(0,1,3) ");
			if (!StringUtils.isEmpty(reqMap.get("oidUserId"))) {
				sql.append(" AND t.OID_USER_ID = ? ");
				params.add(reqMap.get("oidUserId"));
			}
			if (!StringUtils.isEmpty(reqMap.get("beginDate"))) {
				sql.append(" AND DATE_FORMAT(t.UPD_DATE, '%Y-%m-%d') >= ? ");
				params.add(reqMap.get("beginDate"));
			}
			if (!StringUtils.isEmpty(reqMap.get("endDate"))) {
				sql.append(" AND DATE_FORMAT(t.UPD_DATE, '%Y-%m-%d') <= ? ");
				params.add(reqMap.get("endDate"));
			}
			if (!StringUtils.isEmpty(reqMap.get("dateType"))) {
				if("1".equals(reqMap.get("dateType"))){
					sql.append(" AND t.INS_DATE between date_sub(now(),interval 1 month) and now()");
				}
				if("2".equals(reqMap.get("dateType"))){
					sql.append(" AND t.INS_DATE between date_sub(now(),interval 3 month) and now() ");
				}
				if("3".equals(reqMap.get("dateType"))){
					sql.append(" AND t.INS_DATE between date_sub(now(),interval 12 month) and now() ");
				}
			}
		}
		if(StringUtils.isEmpty(reqMap.get("projectType"))){
			sql.append(" ) b ");
		}
		System.out.println(sql);
		System.out.println(params);
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
