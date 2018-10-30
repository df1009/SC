package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IPCUserTenderDao;

/**
 * 用户中心-订单管理
 */
public class PCUserTenderDaoImpl extends BaseDao implements IPCUserTenderDao {

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的数量()
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * @return
	 */
	@Override
	public int getUserTenderCnt(Map tenderMap){

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(T.OID_TENDER_ID) AS cnt ");
		sql.append(" FROM ");
		sql.append(" 	user_tender T ");
		sql.append(" INNER JOIN ");
		sql.append(" 	PROJECTS_INFO T1 ");
		sql.append(" ON T.OID_PLATFORM_PROJECTS_ID = T1.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty((String)tenderMap.get("oidUserId"))) {
			params.add(tenderMap.get("oidUserId"));
			sql.append(" 	AND T.OID_USER_ID = ? ");
		}
		if (!StringUtils.isEmpty((String)tenderMap.get("projectName"))) {
			params.add("%" + tenderMap.get("projectName") + "%");
			sql.append(" 	AND T1.PLATFORM_PROJECTS_SHORT_NM LIKE  ? ");
		}
		if (!StringUtils.isEmpty((String)tenderMap.get("projectState"))) {
			params.add(tenderMap.get("projectState"));
			sql.append(" 	AND T.TENDER_STATUS = ? ");
		}
		if (!StringUtils.isEmpty((String)tenderMap.get("orderId"))) {
			params.add(tenderMap.get("orderId"));
			sql.append(" 	AND T.OID_TENDER_ID = ? ");
		}
  		return getCount(sql.toString(), params.toArray());

	}

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的list数组数据
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * 每页面的条数  pageSize
	 * 当前页数          curPage
	 * @return
	 */
	@Override
	public List<Row> getUserTenders(Map tenderMap){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" 	 T.OID_TENDER_ID AS oidTenderID ");
		sql.append(" 	,T.TENDER_SSN tenderSsn");
		sql.append(" 	,DATE_FORMAT(T.PAY_DATE,'%m/%d %H:%i') AS payDate ");
		sql.append(" 	,DATE_FORMAT(T.CONFIRM_SUCCESS_DATE,'%m/%d %H:%i') AS confirmSuccessDate ");
		sql.append(" 	,DATE_FORMAT(T.CONFIRM_SUCCESS_DATE,'%Y年%m月%d日') AS confirmSuccessDate1 ");
		sql.append(" 	,DATE_FORMAT(T.CONFIRM_SUCCESS_DATE,'%Y%m%d%H%i%S') AS confirmSuccessDate2 ");
		sql.append(" 	,T.TENDER_AMOUNT AS tenderAmount ");
		sql.append(" 	,T.TENDER_STATUS tenderStatus ");
		sql.append(" 	,DATE_FORMAT(T.INS_DATE,'%Y-%m-%d %H:%i:%S')  AS insDate ");
		sql.append(" 	,DATE_FORMAT(T.INS_DATE,'%m/%d %H:%i') AS insDateDetal ");
		sql.append(" 	,T1.PLATFORM_PROJECTS_ST productStatus ");
		sql.append(" 	,T2.ISS_BGN_DT_DES issEndDate ");
		sql.append(" 	,T1.OID_PLATFORM_PROJECTS_ID projectCode ");
		sql.append(" 	,date_add(T.INS_DATE, interval 30 MINUTE) overdueDate");
		sql.append(" 	,DATE_FORMAT(date_add(T.INS_DATE, interval 30 MINUTE),'%m/%d %H:%i') overdueDate1");
		sql.append(" 	,T2.PROJECT_MAIN_TITLE AS productName ");
		sql.append(" 	,T2.APP_LIST_IMG smallImg ");
		sql.append(" 	,b.CARD_NO ");
		sql.append(" 	,b.BANK ");
		sql.append(" FROM ");
		sql.append(" 	user_tender T ");
		sql.append(" LEFT JOIN PROJECTS_INFO T1 ON T.OID_PLATFORM_PROJECTS_ID = T1.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" LEFT JOIN projects_info_des_online T2 ON T.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" LEFT JOIN user_account_bind_bank b ON b.OID_ACCOUNT_BIND_BANK_ID = T.OID_ACCOUNT_BIND_BANK_ID ");
		sql.append(" WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty((String)tenderMap.get("oidUserId"))) {
			params.add(tenderMap.get("oidUserId"));
			sql.append(" 	AND T.OID_USER_ID = ? ");
		}
		if (!StringUtils.isEmpty((String)tenderMap.get("projectName"))) {
			params.add("%" + tenderMap.get("projectName") + "%");
			sql.append(" 	AND T1.PLATFORM_PROJECTS_SHORT_NM LIKE  ? ");
		}
		if (!StringUtils.isEmpty((String)tenderMap.get("projectState"))&&!"0".equals(tenderMap.get("projectState"))) {
			params.add(tenderMap.get("projectState"));
			sql.append(" 	AND T.TENDER_STATUS = ? ");
		}
		if (!StringUtils.isEmpty((String)tenderMap.get("orderId"))) {
			params.add(tenderMap.get("orderId"));
			sql.append(" 	AND T.OID_TENDER_ID = ? ");
		}
		if (!StringUtils.isEmpty((String)tenderMap.get("projectId"))) {
			params.add(tenderMap.get("orderId"));
			sql.append(" 	AND T1.OID_PLATFORM_PROJECTS_ID = ? ");
		}
		if (!StringUtils.isEmpty((String)tenderMap.get("tenderSsn"))) {
			params.add(tenderMap.get("tenderSsn"));
			sql.append(" 	AND T.TENDER_SSN = ? ");
		}
		if("0".equals(tenderMap.get("projectState"))){//退款订单
			sql.append(" 	AND EXISTS ( ");
			sql.append(" 	SELECT 1 FROM ORDER_BACKAMT_RECORD B WHERE B.OID_TENDER_ID = T.OID_TENDER_ID ");
			sql.append(" 	) ");
		}
		if("1".equals(tenderMap.get("projectState"))){//支付成功
			sql.append(" 	AND NOT EXISTS ( ");
			sql.append(" 	SELECT 1 FROM ORDER_BACKAMT_RECORD B WHERE B.OID_TENDER_ID = T.OID_TENDER_ID ");
			sql.append(" 	) ");
		}
		sql.append(" ORDER BY T.INS_DATE DESC");
		return queryForList(sql.toString(),(int)tenderMap.get("PageCount"),(int)tenderMap.get("pageSize"), params.toArray());
	}

	
	//查询项目基本信息
	@Override
	public Row getOrderProductInfo(Map tenderMap) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT T.OID_TENDER_ID "
						+ " ,T.TENDER_STATUS "
						+ " ,T.TENDER_SSN "
						+ " ,T.INS_DATE "
						+ " ,T1.PLATFORM_PROJECTS_ST "
						+ ",DATE_FORMAT(T.INS_DATE,'%Y%m%d%H%i%s')  AS insDate "
						+ " ,T.TENDER_AMOUNT ");
		sql.append(" FROM USER_TENDER T");
		sql.append(" INNER JOIN ");
		sql.append(" 	PROJECTS_INFO T1 ");
		sql.append(" ON T.OID_PLATFORM_PROJECTS_ID = T1.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" WHERE T.OID_TENDER_ID=? ");
		sql.append(" AND T.OID_USER_ID=? ");
		return query(sql.toString(),new Object[]{ tenderMap.get("productId"),tenderMap.get("loginId")});
	}

	
	//获取退款信息
	@Override
	public Row getReturnAmt(String tenderId,String oidUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" BACKAMT_TYPE ");
		sql.append(" ,DATE_FORMAT(INS_DATE,'%m/%d %H:%i') AS INS_DATE ");
		sql.append(" ,DATE_FORMAT(UPD_DATE,'%m/%d %H:%i') AS UPD_DATE ");
		sql.append(" ,TENDER_STATUS ");
		sql.append(" FROM ORDER_BACKAMT_RECORD  ");
		sql.append(" WHERE ");
		sql.append(" OID_TENDER_ID= ? ");
		sql.append(" AND OID_USER_ID =? ");
		sql.append(" ORDER BY INS_DATE DESC ");
		return query(sql.toString(),new Object[]{tenderId,oidUserId});

	}
	
	//删除关闭的订单
	@Override
	public int delCloseOrder(String tenderId,String oidUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(" user_tender ");
		sql.append(" WHERE ");
		sql.append(" OID_TENDER_ID = ?  ");
		sql.append(" AND OID_USER_ID = ?  ");
		sql.append(" AND TENDER_STATUS in (4,5)  ");
		return update(sql.toString(), new Object[]{tenderId,oidUserId});

	}
	
	//取消的订单
	@Override
	public int updCloseOrder(String tenderId,String oidUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE user_tender SET TENDER_STATUS = '5'  ");
		sql.append(" WHERE ");
		sql.append(" OID_TENDER_ID = ?  ");
		sql.append(" AND OID_USER_ID = ?  ");
		sql.append(" AND TENDER_STATUS =4  ");
		return update(sql.toString(), new Object[]{tenderId,oidUserId});

	}
}
