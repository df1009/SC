package com.zhongchou.common.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IUserTenderDao;
import com.zhongchou.common.dto.UserTender;

/**
 * 用户中心-订单管理
 */
public class UserTenderDaoImpl extends BaseDao implements IUserTenderDao {

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
		sql.append(" WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(tenderMap.get("oidUserId"))) {
			params.add(tenderMap.get("oidUserId"));
			sql.append(" 	AND T.OID_USER_ID = ? ");
		}
		if (!StringUtils.isEmpty(tenderMap.get("projectName"))) {
			params.add("%" + tenderMap.get("projectName") + "%");
			sql.append(" 	AND T1.PLATFORM_PROJECTS_SHORT_NM LIKE  ? ");
		}
		if (!StringUtils.isEmpty(tenderMap.get("projectState"))) {
			params.add(tenderMap.get("projectState"));
			sql.append(" 	AND T1.PLATFORM_PROJECTS_ST = ? ");
		}
		if (!StringUtils.isEmpty(tenderMap.get("beginDate"))) {
			sql.append(" AND DATE_FORMAT(T.INS_DATE, '%Y-%m-%d') >= ? ");
			params.add(tenderMap.get("beginDate"));
		}
		if (!StringUtils.isEmpty(tenderMap.get("endDate"))) {
			sql.append(" AND DATE_FORMAT(T.INS_DATE, '%Y-%m-%d') <= ? ");
			params.add(tenderMap.get("endDate"));
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
		sql.append(" 	,T3.APP_LIST_IMG AS smallImg ");
		sql.append(" 	,T.TENDER_SSN ");
		sql.append(" 	,T1.PLATFORM_PROJECTS_SHORT_NM AS productName ");
		sql.append(" 	,T.TENDER_AMOUNT AS amountStr ");
		sql.append(" 	,DATE_FORMAT(T.INS_DATE,'%Y%m%d%H%i%s')  AS insDate ");
		sql.append(" 	,T.INS_DATE AS insDateDetal ");
		sql.append(" 	,T1.PLATFORM_PROJECTS_ST productStatus ");
		sql.append(" 	,T.TENDER_STATUS tenderStatus ");
		sql.append(" 	,T1.OID_PLATFORM_PROJECTS_ID projectCode ");
		sql.append(" FROM ");
		sql.append(" 	user_tender T ");
		sql.append(" INNER JOIN ");
		sql.append(" 	PROJECTS_INFO T1 ");
		sql.append(" ON T.OID_PLATFORM_PROJECTS_ID = T1.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" LEFT JOIN PROJECTS_INFO_DES_ONLINE T3 ON T3.OID_PLATFORM_PROJECTS_ID = T1.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isEmpty(tenderMap.get("oidUserId"))) {
			params.add(tenderMap.get("oidUserId"));
			sql.append(" 	AND T.OID_USER_ID = ? ");
		}
		if (!StringUtils.isEmpty(tenderMap.get("projectName"))) {
			params.add("%" + tenderMap.get("projectName") + "%");
			sql.append(" 	AND T1.PLATFORM_PROJECTS_SHORT_NM LIKE  ? ");
		}
		if (!StringUtils.isEmpty(tenderMap.get("projectState"))) {
			params.add(tenderMap.get("projectState"));
			sql.append(" 	AND T1.PLATFORM_PROJECTS_ST = ? ");
		}
		if (!StringUtils.isEmpty(tenderMap.get("beginDate"))) {
			sql.append(" AND DATE_FORMAT(T.INS_DATE, '%Y-%m-%d') >= ? ");
			params.add(tenderMap.get("beginDate"));
		}
		if (!StringUtils.isEmpty(tenderMap.get("endDate"))) {
			sql.append(" AND DATE_FORMAT(T.INS_DATE, '%Y-%m-%d') <= ? ");
			params.add(tenderMap.get("endDate"));
		}
		sql.append(" ORDER BY T.INS_DATE DESC");
		return queryForList(sql.toString(),(int)tenderMap.get("PageCount"),(int)tenderMap.get("pageSize"), params.toArray());
	}

	//获取撤单信息
	public Row getReturnState(String tenderId,String oidUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" TENDER_STATUS ");
		sql.append(" FROM ORDER_CANCELLATION_RECORD  ");
		sql.append(" WHERE ");
		sql.append(" OID_TENDER_ID= ? ");
		sql.append(" AND TENDER_STATUS IN (1,3) ");
		sql.append(" AND OID_USER_ID =? ");
		sql.append(" ORDER BY INS_DATE DESC ");
		return query(sql.toString(),new Object[]{tenderId,oidUserId});

	}

	//获取撤单信息
	public Row getReturnAllState(String tenderId,String oidUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" TENDER_STATUS ");
		sql.append(" FROM ORDER_CANCELLATION_RECORD  ");
		sql.append(" WHERE ");
		sql.append(" OID_TENDER_ID= ? ");
		sql.append(" AND OID_USER_ID =? ");
		sql.append(" ORDER BY INS_DATE DESC ");
		return query(sql.toString(),new Object[]{tenderId,oidUserId});

	}
	//订单信息
	public Row getOrderInfo(String orderNum) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" T1.ORDER_STATUS, ");
		sql.append(" T1.OID_TENDER_ID, ");
		sql.append(" T1.TRANSACTION_NUM, ");
		sql.append(" T1.INS_DATE, ");
		sql.append(" T1.PAY_TIME, ");
		sql.append(" T1.ACTUAL_PAYMENT ");
		sql.append(" FROM ORDER_DETAILS T1 ");
		sql.append(" WHERE ");
		sql.append("  OID_TENDER_ID= ? ");
		return query(sql.toString(),new Object[]{orderNum});

	}
	//跟投人信息list
	public List<Row> getInvestorList(String projectsId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" T2.USER_ICON_FILE_ID, ");
		sql.append(" T2.NICKNAME, ");
		sql.append(" DATE_FORMAT(T1.INS_DATE,'%Y-%m-%d') AS INS_DATE, ");
		sql.append(" T1.TENDER_AMOUNT ");
		sql.append(" FROM USER_TENDER T1 ");
		sql.append(" LEFT JOIN ");
		sql.append(" USER_DETAIL T2 ");
		sql.append(" ON T1.OID_USER_ID = T2.OID_USER_ID ");
		sql.append(" WHERE T1.OID_PLATFORM_PROJECTS_ID = ? ");
		return queryForList(sql.toString(),new Object[]{projectsId});
	}
	//购买时增加认购信息
	@Override
	public int saveUserTender(UserTender userTender,String BankId) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" USER_TENDER ");
		sql.append(" (OID_TENDER_ID, OID_PLATFORM_PROJECTS_ID, OID_USER_ID, TENDER_TYPE,TENDER_AMOUNT,TENDER_STATUS,AUTO_STATUS, REQUEST_NO, INS_DATE, UPD_DATE, TENDER_SSN,OID_ACCOUNT_BIND_BANK_ID) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?,now(),now(),?,?) ");
		Object[] params = new Object[]{
				userTender.getOidTenderId(),
				userTender.getOidPlatformProductsId(),
				userTender.getOidUserId(),
				"02",
				userTender.getTenderAmount(),
				userTender.getTenderStatus(),
				userTender.getAutoStatus(),
				userTender.getRequestNo(),
				userTender.getTenderSsn(),
				BankId
				};
		return update(sql.toString(), params);
	}

	public boolean checkTenderIdExists(String oidTenderId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  ");
		sql.append("    OID_TENDER_ID   ");
		sql.append(" FROM USER_TENDER ");
		sql.append("   WHERE OID_TENDER_ID = ? ");
		List<Object> params = new ArrayList<>();
		params.add(oidTenderId);
		return queryForList(sql.toString(), params.toArray()).size() > 0;
	}
	//查询单个项目详细信息
	@Override
	public Row selProductDetaile(String productId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * from PROJECTS_INFO_DES d,PROJECTS_INFO i");
		sql.append(" WHERE d.OID_PLATFORM_PROJECTS_ID=i.OID_PLATFORM_PROJECTS_ID");
		sql.append(" AND i.OID_PLATFORM_PROJECTS_ID = ?");
		return singleQuery(sql.toString(),new Object[]{ productId});
	}

	//查询单个项目的项目介绍信息
	@Override
	public List<Row> selProductIntroduce(String productId,String type) {
		StringBuilder sql = new StringBuilder();
		if("1".equals(type)){//项目信息
			sql.append("SELECT PROJECTS_INFO_ID,PARTY_NUM,TITLE,SUMMARYTEXT,PROGRESS_DATE,IMG_PATH,STATUS FROM PROJECTS_INFO_DETAL");
			sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
			sql.append(" AND TYPE = ? ");
			sql.append(" ORDER BY CAST(PARTY_NUM AS SIGNED),INS_DATE");
		}else if("2".equals(type)){//项目进展
			sql.append("SELECT PROJECTS_INFO_ID,PARTY_NUM,TITLE,SUMMARYTEXT,PROGRESS_DATE,IMG_PATH,STATUS FROM PROJECTS_INFO_DETAL");
			sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
			sql.append(" AND TYPE = ? ");
			sql.append(" ORDER BY PROGRESS_DATE DESC,INS_DATE DESC");
		}

		return queryForList(sql.toString(),  new Object[]{ productId,type});
	}

	//查询单个项目投资人
	@Override
	public List<Row> selProductInvestor(String productId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u.OID_USER_ID oid,UT.TENDER_AMOUNT amount,UT.INS_DATE insDate,ud.NICKNAME nickName,ud.USER_ICON_FILE_ID iconfile,ud.USER_NAME userName from USER_TENDER ut");
		sql.append(" INNER JOIN USER u");
		sql.append(" on ut.OID_USER_ID = u.OID_USER_ID");
		sql.append(" INNER JOIN PROJECTS_INFO p");
		sql.append(" on p.OID_PLATFORM_PROJECTS_ID=ut.OID_PLATFORM_PROJECTS_ID");
		sql.append(" INNER JOIN USER_DETAIL ud");
		sql.append(" on ud.OID_USER_ID = u.OID_USER_ID");
		sql.append(" WHERE p.OID_PLATFORM_PROJECTS_ID=?");
		sql.append(" AND ut.TENDER_STATUS =3");
		sql.append(" AND not exists(select 1 from ORDER_CANCELLATION_RECORD oc where ut.TENDER_SSN = oc.REQ_SSN)");
		sql.append(" ORDER BY ut.INS_DATE DESC");
		return queryForList(sql.toString(),new Object[]{ productId});
	}

	//查询项目基本信息
	@Override
	public Row getProductinfo(String productId,String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT T.OID_TENDER_ID "
						+ " ,T.TENDER_STATUS "
						+ " ,T.TENDER_SSN "
						+ " ,T.INS_DATE "
						+ " ,T1.PLATFORM_PROJECTS_ST "
						+ " ,T1.PLATFORM_PROJECTS_SHORT_NM "
						+ ",DATE_FORMAT(T.INS_DATE,'%Y%m%d%H%i%s')  AS insDate "
						+ " ,T.TENDER_AMOUNT ");
		sql.append(" FROM USER_TENDER T");
		sql.append(" INNER JOIN ");
		sql.append(" 	PROJECTS_INFO T1 ");
		sql.append(" ON T.OID_PLATFORM_PROJECTS_ID = T1.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" WHERE T.OID_TENDER_ID=? ");
		sql.append(" AND T.OID_USER_ID=? ");
		return query(sql.toString(),new Object[]{ productId,loginId});
	}
	//查询项目信息
	@Override
	public Row getUserTender(String tenderId,String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT OID_TENDER_ID "
				+ " ,OID_PLATFORM_PROJECTS_ID "
				+ " ,OID_USER_ID "
				+ " ,TENDER_TYPE "
				+ " ,TENDER_AMOUNT "
				+ " ,TENDER_STATUS "
				+ " ,AUTO_STATUS "
				+ " ,REQUEST_NO "
				+ " ,CONTRACT_NO "
				+ " ,INS_DATE "
				+ " ,UPD_DATE "
				+ " ,TENDER_SSN ");
		sql.append(" FROM USER_TENDER ");
		sql.append(" WHERE OID_TENDER_ID=? ");
		sql.append(" AND OID_USER_ID=? ");
		return query(sql.toString(),new Object[]{ tenderId,userId});
	}

	//插入撤单记录
	@Override
	public int insOrderCancellationRecord(Map<String,Object> param) {

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" ORDER_CANCELLATION_RECORD ");
		sql.append(" (OID_TENDER_ID,OID_PLATFORM_PROJECTS_ID,OID_USER_ID,TENDER_TYPE,TENDER_AMOUNT,TENDER_STATUS,AUTO_STATUS,REQUEST_NO,CONTRACT_NO,INS_DATE,UPD_DATE,TENDER_SSN,REQ_SSN,REQ_TENDER_ID) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?,?,?,NOW(),NOW(),?,?,?) ");

		Object[] params = new Object[]{
				(String)param.get("tenderId"),
				(String)param.get("proId"),
				(String)param.get("userId"),
				(String)param.get("tenderTy"),
				(BigDecimal)param.get("amount"),
				(String)param.get("status"),
				(String)param.get("autoStatus"),
				(String)param.get("reqNo"),
				(String)param.get("contractNo"),
				(String)param.get("redeemSsn"),
				(String)param.get("tenderSsn"),
				(String)param.get("tenderId")
				};
		return update(sql.toString(), params);
	}

	//查询撤单信息
	@Override
	public Row getOrderCancellationRecord(String reqSsn,String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT 1 ");
		sql.append(" FROM ORDER_CANCELLATION_RECORD ");
		sql.append(" WHERE REQ_SSN=? ");
		sql.append(" AND OID_USER_ID=? ");
		return query(sql.toString(),new Object[]{ reqSsn,userId});
	}

	//原撤单以撤但完成修改撤单状态
	@Override
	public int updateOrderCancellationRecord(Map<String,Object> param) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE ORDER_CANCELLATION_RECORD");
		sql.append(" SET TENDER_STATUS = 3 ");
		sql.append(" WHERE REQ_SSN=? ");
		sql.append(" AND OID_USER_ID=? ");
		Object[] params = new Object[]{
				(String)param.get("tenderSsn"),
				(String)param.get("loginId")
				};
		return update(sql.toString(),params);
	}

	@Override
	public int selUserTenderPro(String productId, String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(1) from user_tender");
		sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
		sql.append(" AND OID_USER_ID = ?");
		sql.append(" AND TENDER_STATUS IN (1,3)");
		return getCount(sql.toString(), new Object[]{ productId,loginId});
	}

	//插入退款记录
	@Override
	public int insRefund(Map<String,Object> param) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" order_backamt_record ");
		sql.append(" (OID_TENDER_ID,OID_PLATFORM_PROJECTS_ID,OID_USER_ID,TENDER_STATUS,TENDER_SSN,BACK_AMT,BACKAMT_TYPE) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,?) ");

		Object[] params = new Object[]{
				param.get("tenderId"),
				param.get("proId"),
				param.get("userId"),
				"1",
				param.get("redeemSsn"),
				param.get("amount"),
				param.get("type")
				};
		return update(sql.toString(), params);
	}

	//查询用户投资总额
	@Override
	public int selAllTenderAmount(String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(sum(TENDER_AMOUNT)/100,0) from user_tender t ");
		sql.append(" WHERE  NOT EXISTS( ");
		sql.append(" SELECT 1 from order_cancellation_record o  ");
		sql.append(" WHERE t.TENDER_SSN = o.REQ_SSN ");
		sql.append(" AND OID_USER_ID = ? ");
		sql.append(" AND TENDER_STATUS IN(1,3)) ");
		sql.append(" AND OID_USER_ID = ? ");
		return getCount(sql.toString(), new Object[]{ userId,userId});
	}

	//查询用户投资项目的数量
	@Override
	public int selTenderCount(String userId) {
		StringBuilder sql = new StringBuilder();
		//sql.append("SELECT count(DISTINCT OID_PLATFORM_PROJECTS_ID) from user_tender");
		sql.append("SELECT count(TENDER_SSN) from user_tender t ");
		sql.append(" WHERE t.OID_USER_ID = ? ");
		sql.append(" and TENDER_STATUS in (1,3) ");
		sql.append(" and not EXISTS ");
		sql.append(" (SELECT REQ_SSN from order_cancellation_record o ");
		sql.append(" WHERE t.TENDER_SSN = o.REQ_SSN ");
		sql.append(" and o.OID_USER_ID = ? ");
		sql.append(" and TENDER_STATUS = 3) ");
		return getCount(sql.toString(), new Object[]{ userId,userId});
	}
	/**
	 * 模糊查询用户投资信息
	 */
	@Override
	public List<Row> likeTender(String userId, String proName) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT p.PROJECT_MAIN_TITLE,p.OID_PLATFORM_PROJECTS_ID,ut.* FROM projects_info_des_online p ");
		sql.append(" INNER JOIN user_tender ut ");
		sql.append(" ON p.OID_PLATFORM_PROJECTS_ID = ut.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" WHERE ut.OID_USER_ID = ? ");
		sql.append(" AND p.PROJECT_MAIN_TITLE LIKE ? ");
		sql.append(" ORDER BY ut.INS_DATE DESC ");
		params.add(userId);
		params.add("%" + proName + "%");
		return queryForList(sql.toString(), params.toArray());
	}
	/**
	 * 查询用户投资过产品
	 */
	@Override
	public int countUserTenderAmount(String userId, String proId) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(1) from user_tender ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" AND OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND TENDER_STATUS in(1,3)");
		return getCount(sql.toString(), new Object[]{ userId,proId});
	}
	/**
	 * 查询用户当日投资金额
	 */
	@Override
	public int countUserTenderAmountToday(String userId) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(sum(TENDER_AMOUNT)/100,0) from user_tender ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" AND TENDER_STATUS in(1,3) ");
		sql.append(" AND to_days(INS_DATE) = to_days(now()) ");
		return getCount(sql.toString(), new Object[]{ userId});
	}
	/**
	 * 订单信息
	 * @param paramMap
	 * @return
	 */
	@Override
	public Row getWaitTenderInfo(Map<String, String> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.OID_TENDER_ID tenderId,po.APP_LIST_IMG,");
		sql.append("p.PLATFORM_PROJECTS_SHORT_NM projectName,");
		sql.append("(SELECT d.USER_NAME FROM user_detail d where d.OID_USER_ID = t.OID_USER_ID) userName,");
		sql.append("format(t.TENDER_AMOUNT/100,2) tenderAmount,");
		sql.append("b.MOBILE mobile,");
		sql.append("b.CARD_NO cardNo,");
		sql.append("date_add(t.INS_DATE, interval 30 MINUTE) overdueDate,");
		sql.append("t.INS_DATE insDate,");
		sql.append("b.BANK bankCd,");
		sql.append("(SELECT  q.BANK_NM FROM bank_withhold_quota q where q.BANK_CD = b.BANK) bankName,");
		sql.append("(SELECT  q.SINGLE_LIMIT_AMT FROM bank_withhold_quota q where q.BANK_CD = b.BANK) singleLimitAmt,");
		sql.append("(SELECT  q.DAY_LIMIT_AMT FROM bank_withhold_quota q where q.BANK_CD = b.BANK) dayLimitAmt, ");
		sql.append("(SELECT  d.ID_CARD FROM user_detail d where d.OID_USER_ID = t.OID_USER_ID) idCard ");

		sql.append(" FROM user_tender t INNER JOIN projects_info p on p.OID_PLATFORM_PROJECTS_ID = t.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" LEFT JOIN user_account_bind_bank b on b.OID_USER_ID = t.OID_USER_ID and b.DEL_FLG = '0' ");
		sql.append(" LEFT JOIN PROJECTS_INFO_DES_ONLINE po ON p.OID_PLATFORM_PROJECTS_ID = po.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" where t.OID_TENDER_ID=?");
		sql.append(" and t.OID_USER_ID = ?");
		sql.append(" and t.TENDER_STATUS='4'");
		return query(sql.toString(),new Object[]{ paramMap.get("tenderId"),paramMap.get("userId")});
	}
	/**
	 * 购买成功更新tender表
	 * @param userTender
	 * @param bankId
	 */
	@Override
	public void updUserTender(UserTender userTender, String bankId) {
		StringBuilder sql = new StringBuilder();
		List<Object> paramList = new ArrayList<>();
		sql.append("UPDATE user_tender ");
		sql.append(" SET  TENDER_AMOUNT=?,");
		sql.append(" TENDER_STATUS=?,");
		sql.append(" TENDER_SSN=?,");
		sql.append(" UPD_DATE= now(),");
		sql.append(" PAY_DATE= now(),");
		sql.append(" OID_ACCOUNT_BIND_BANK_ID=? ");
		sql.append(" WHERE OID_TENDER_ID=?");
		sql.append(" and OID_PLATFORM_PROJECTS_ID=?");
		sql.append(" and OID_USER_ID=?");
		paramList.add(userTender.getTenderAmount());
		paramList.add(userTender.getTenderStatus());
		paramList.add(userTender.getTenderSsn());
		paramList.add(bankId);
		paramList.add(userTender.getOidTenderId());
		paramList.add(userTender.getOidPlatformProductsId());
		paramList.add(userTender.getOidUserId());
		update(sql.toString(),paramList.toArray());
	}
}
