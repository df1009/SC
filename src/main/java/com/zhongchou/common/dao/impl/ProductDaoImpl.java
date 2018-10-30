package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.ProductDao;

public class ProductDaoImpl extends BaseDao implements ProductDao{

	//加载产品列表
	@Override
	public List<Row> selProductFirstList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(" d.PLATFORM_PROJECTS_ST ");
		sql.append(" ,d.RISK_LVL ");
		sql.append(" ,d.PLATFORM_PROJECTS_SHORT_NM ");
		sql.append(" ,d.MIN_SUBS_AMT ");
		sql.append(" ,d.MIN_BIDS_AMT ");
		sql.append(" ,d.SALES_QUOTA ");
		sql.append(" ,d.SURPLUS_SALES_QUOTA ");
		sql.append(" ,d.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" ,d.SALES_QUOTA ");
		sql.append(" ,d.FIRST_MIN_BUY ");
		sql.append(" ,i.PROJECT_MAIN_TITLE ");
		sql.append(" FROM PROJECTS_INFO d");
		sql.append(" INNER JOIN PROJECTS_INFO_DES i ");
		sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" WHERE i.LAST_STATE = 1");
		sql.append(" AND i.STATUS <> 4");
		sql.append(" ORDER BY ISS_BGN_DT DESC ");
		sql.append(" LIMIT 0 ,10 ");
		return queryForList(sql.toString(),new Object[]{ });
	}
	//查询产品总数
	@Override
	public int countProduct(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		if(selMap ==null){
			sql.append("SELECT count(1) FROM projects_info d");
			sql.append(" INNER JOIN PROJECTS_INFO_DES i ");
			sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" WHERE i.LAST_STATE = 1");
			sql.append(" AND i.STATUS <> 4");
		}else{
			sql.append("SELECT count(1) FROM projects_info d");
			sql.append(" INNER JOIN PROJECTS_INFO_DES i ");
			sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
			if(selMap.get("industryType") != null){
				sql.append(" INNER JOIN INDUSTRY_TYPE t");
				sql.append(" ON i.INDUSTRY=t.INDUSTRY_NM");
				sql.append(" WHERE i.INDUSTRY IN (");
				for (String param : ((String)selMap.get("industryType")).split(",")) {
					sql.append("?,");
					params.add(param);
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(") ");
				sql.append(" AND t.STATUS = '1'");
				sql.append(" AND i.LAST_STATE = 1");
				sql.append(" AND i.STATUS <> 4");

			}else{
				sql.append(" WHERE i.STATUS <> 4");
				sql.append(" AND i.LAST_STATE = 1");
			}
			if(selMap.get("riskLvl") != null){
				sql.append(" AND d.RISK_LVL IN (");
				for (String param : ((String)selMap.get("riskLvl")).split(",")) {
					sql.append("?,");
					params.add(param);
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(") ");
			}
			if(selMap.get("platformProjectsSt") != null){
				sql.append(" AND d.PLATFORM_PROJECTS_ST IN (");
				for (String param : ((String)selMap.get("platformProjectsSt")).split(",")) {
					sql.append("?,");
					params.add(param);
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(") ");
			}
		}
		return getCount(sql.toString(), params.toArray());
	}

	//查询行业类别
	@Override
	public List<Row> selindustryType() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT INDUSTRY_ID,INDUSTRY_NM FROM INDUSTRY_TYPE ");
		sql.append(" WHERE STATUS = 1");
		return queryForList(sql.toString(),new Object[]{ });
	}

	//分页多条件查询产品列表
	@Override
	public List<Row> selProductList(Map selMap) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT d.OID_PLATFORM_PROJECTS_ID,d.PLATFORM_PROJECTS_SHORT_NM,d.MIN_SUBS_AMT,d.MIN_BIDS_AMT,d.RISK_LVL,d.SALES_QUOTA,d.PLATFORM_PROJECTS_ST,d.SURPLUS_SALES_QUOTA,d.FIRST_MIN_BUY,i.PROJECT_MAIN_TITLE ");
		sql.append(" FROM PROJECTS_INFO d");
		sql.append(" INNER JOIN PROJECTS_INFO_DES i ");
		sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
		if(selMap.get("industryType") != null){

			sql.append(" INNER JOIN INDUSTRY_TYPE t");
			sql.append(" ON i.INDUSTRY=t.INDUSTRY_NM");

			sql.append(" WHERE i.INDUSTRY IN (");
			for (String param : ((String)selMap.get("industryType")).split(",")) {
				sql.append("?,");
				params.add(param);
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") ");

			sql.append(" AND t.STATUS = '1'");
			sql.append(" AND i.LAST_STATE = 1");
			sql.append(" AND i.STATUS <> 4");
		}else{
			sql.append(" WHERE i.STATUS <> 4");
			sql.append(" AND i.LAST_STATE = 1");
		}
		if(selMap.get("riskLvl") != null){
			sql.append(" AND d.RISK_LVL IN (");
			for (String param : ((String)selMap.get("riskLvl")).split(",")) {
				sql.append("?,");
				params.add(param);
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") ");
		}
		if(selMap.get("platformProjectsSt") != null){
			sql.append(" AND d.PLATFORM_PROJECTS_ST IN (");
			for (String param : ((String)selMap.get("platformProjectsSt")).split(",")) {
				sql.append("?,");
				params.add(param);
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") ");
		}
		if(selMap.get("sortType") != null
				&&"0".equals(selMap.get("sortType").toString())){
			sql.append(" ORDER BY ISS_BGN_DT ASC");
		}else{
			sql.append(" ORDER BY ISS_BGN_DT DESC");
		}

		if(selMap.get("pageIndex") != null){
			return queryForList(sql.toString(),Integer.parseInt(selMap.get("pageIndex").toString()),Integer.parseInt(selMap.get("pageSize").toString()),  params.toArray());
		}else{
			return queryForList(sql.toString(),  params.toArray());
		}
	}

	//查询单个项目详细信息
	@Override
	public Row selProductDetaile(String productId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT d.*,i.*,ud.USER_NAME,ud.USER_ICON_FILE_ID from PROJECTS_INFO_DES d ");
		sql.append(" INNER JOIN PROJECTS_INFO i");
		sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID");
		sql.append(" INNER JOIN USER_DETAIL ud");
		sql.append(" ON d.OID_USER_ID=ud.OID_USER_ID");
		sql.append(" WHERE d.OID_PLATFORM_PROJECTS_ID = ?");
		sql.append(" AND d.LAST_STATE = 1");
		sql.append(" AND d.STATUS <> 4");

		return singleQuery(sql.toString(),new Object[]{ productId});
	}

	//查询单个项目简略信息
	@Override
	public Row selProductBrief(String productId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT d.OID_PLATFORM_PROJECTS_ID,d.IMG_PATH,d.SMALL_IMG_PATH,i.ISS_BGN_DT,ud.USER_NAME,i.PLATFORM_PROJECTS_SHORT_NM from PROJECTS_INFO_DES d  ");
		sql.append(" INNER JOIN PROJECTS_INFO i");
		sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID");
		sql.append(" INNER JOIN USER_DETAIL ud");
		sql.append(" ON d.OID_USER_ID=ud.OID_USER_ID");
		sql.append(" WHERE d.OID_PLATFORM_PROJECTS_ID = ?");
		sql.append(" AND d.STATUS = 3");
		return singleQuery(sql.toString(),new Object[]{ productId});
	}

	//查询单个项目的项目介绍信息
	@Override
	public List<Row> selProductIntroduce(String productId,String type) {
		StringBuilder sql = new StringBuilder();
		if("1".equals(type)){//项目信息
			sql.append("SELECT TITLE,SUMMARYTEXT,INS_DATE,IMG_PATH FROM PROJECTS_INFO_DETAL");
			sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
			sql.append(" AND STATUS = 3");
			sql.append(" AND TYPE = ? ");
			sql.append(" ORDER BY CAST(PARTY_NUM AS SIGNED),INS_DATE");
		}else if("2".equals(type)){//项目进展
			sql.append("SELECT TITLE,SUMMARYTEXT,INS_DATE,IMG_PATH,PROGRESS_DATE FROM PROJECTS_INFO_DETAL");
			sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
			sql.append(" AND STATUS = 3");
			sql.append(" AND TYPE = ? ");
			sql.append(" ORDER BY PROGRESS_DATE DESC,INS_DATE DESC");
		}

		return queryForList(sql.toString(),  new Object[]{ productId,type});
	}


	//查询单个项目投资人
	@Override
	public List<Row> selProductInvestor(String productId,int sumNum,int pageSize) {
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
		return queryForList(sql.toString(),sumNum,pageSize,new Object[]{ productId});
	}

	//查询单个项目投资数量
	@Override
	public int countProductInvestor(String productId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(1) from USER_TENDER ut");
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
		return getCount(sql.toString(),new Object[]{ productId});
	}

	//查询否符投资人风险等级的项目
	@Override
	public List<Row> userRiskLvlAccordPro(String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * from PROJECTS_INFO p ");
		sql.append(" INNER JOIN projects_info_des de");
		sql.append(" ON de.OID_PLATFORM_PROJECTS_ID = p.OID_PLATFORM_PROJECTS_ID");
		sql.append(" WHERE ");
		sql.append(" p.PLATFORM_PROJECTS_ST in (1,2)");
		sql.append(" AND de.LAST_STATE = 1");
		sql.append(" AND de.STATUS <> 4");
		sql.append(" AND p.RISK_LVL<=");
		sql.append(" (SELECT RISK_LEVEL from USER_DETAIL");
		sql.append(" WHERE OID_USER_ID=?)");
		sql.append(" ORDER BY ISS_BGN_DT DESC");
		sql.append(" LIMIT 0,3");
		return queryForList(sql.toString(),new Object[]{ userId});
	}

	public List<Row> getProjectList() {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT   ");
		sql.append(" t1.OID_PLATFORM_PROJECTS_ID, ");
		sql.append(" t1.PLATFORM_PROJECTS_FULL_NM, ");
		sql.append(" t1.PLATFORM_PROJECTS_SHORT_NM, ");
		sql.append(" t1.RISK_LVL, ");
		sql.append(" t1.MIN_SUBS_AMT, ");
		sql.append(" t1.MIN_BIDS_AMT, ");
		sql.append(" t1.SALES_QUOTA, ");
		sql.append(" t1.PLATFORM_PROJECTS_ST, ");
		sql.append(" t1.FIRST_MIN_BUY, ");
		sql.append(" t1.SURPLUS_SALES_QUOTA, ");
		sql.append(" i.PROJECT_MAIN_TITLE ");
		sql.append(" FROM PROJECTS_INFO t1 ");
		sql.append(" INNER JOIN PROJECTS_INFO_DES i ");
		sql.append(" ON t1.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" WHERE i.LAST_STATE = 1");
		sql.append(" AND i.STATUS <> 4");
		sql.append(" ORDER BY ISS_BGN_DT DESC ");
		sql.append(" LIMIT 0,6 ");
		return queryForList(sql.toString(),new Object[]{});
	}

	//查询行业类别
	@Override
	public List<Row> selIntroducerInvest(String uId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u.OID_USER_ID,u.MOBILE,d.USER_NAME,DATE_FORMAT(m.INS_DATE,'%Y-%m-%d %H:%i:%S')INS_DATE,i.PLATFORM_PROJECTS_SHORT_NM,i.PLATFORM_PROJECTS_ST,m.TENDER_AMOUNT from USER_TENDER m ");
		sql.append(" INNER JOIN USER u");
		sql.append(" ON m.OID_USER_ID=u.OID_USER_ID");
		sql.append(" INNER JOIN USER_DETAIL d");
		sql.append(" ON m.OID_USER_ID=d.OID_USER_ID");
		sql.append(" INNER JOIN PROJECTS_INFO i");
		sql.append(" ON i.OID_PLATFORM_PROJECTS_ID=m.OID_PLATFORM_PROJECTS_ID");
		sql.append(" where m.OID_USER_ID IN");
		sql.append(" (");
		sql.append(" SELECT u.OID_USER_ID from USER_INTRODUCER_M m");
		sql.append(" INNER JOIN USER u");
		sql.append(" ON m.OID_USER_ID=u.OID_USER_ID");
		sql.append(" INNER JOIN USER_DETAIL d");
		sql.append(" ON m.OID_USER_ID=d.OID_USER_ID");
		sql.append(" where INTRODUCER_OID_USER_ID = ?)");
		sql.append(" AND m.TENDER_STATUS = 3");
		sql.append(" ORDER BY m.INS_DATE DESC");
		return queryForList(sql.toString(),new Object[]{uId });
	}
	//查询产品购买状态
	@Override
	public Row selPurchaseStatus(String proId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT   ");
		sql.append(" t1.PLATFORM_PROJECTS_FULL_NM, ");
		sql.append(" t1.RISK_LVL, ");
		sql.append(" t1.MIN_SUBS_AMT, ");
		sql.append(" t1.SALES_QUOTA, ");
		sql.append(" t1.SURPLUS_SALES_QUOTA ");
		sql.append(" FROM PROJECTS_INFO t1 ");
		return singleQuery(sql.toString(),new Object[]{proId});
	}
	@Override
	public int updateProductToBuy(String quota,String proId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE projects_info p set p.SURPLUS_SALES_QUOTA = ? ");
		sql.append(" where p.OID_PLATFORM_PROJECTS_ID = ?; ");
		return update(sql.toString(), new Object[]{quota,proId });
	}
	//查询产品购买状态
	@Override
	public Row selProjectToBuy(String proId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT i.RISK_LVL,i.FIRST_MIN_BUY,i.MIN_SUBS_AMT,i.MIN_BIDS_AMT,i.PLATFORM_PROJECTS_ST,i.MAX_SUBS_AMT,i.SURPLUS_SALES_QUOTA,i.SUBS_ADD_AMT,i.BIDS_ADD_AMT from PROJECTS_INFO_DES d ");
		sql.append(" INNER JOIN PROJECTS_INFO i ");
		sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" AND d.LAST_STATE = 1 ");
		sql.append(" AND d.STATUS <> 4 ");
		sql.append(" AND i.OID_PLATFORM_PROJECTS_ID = ? ");
		return singleQuery(sql.toString(),new Object[]{proId});
	}
	//我要融资
	@Override
	public int addFinancing(Map financingMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		params.add(financingMap.get("name"));
		params.add(financingMap.get("mobile"));
		params.add(financingMap.get("email"));
		sql.append("INSERT INTO ");
		sql.append(" finacing_manage ");
		sql.append(" (NAME, MOBILE, EMAIL ");
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("companyName")))){
			sql.append(", COMPANY_NAME");
			params.add(financingMap.get("companyName"));
		}
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("regisTime")))){
			sql.append(", REGIS_TIME");
			params.add(financingMap.get("regisTime"));
		}
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("regisAddress")))){
			sql.append(", REGIS_ADDRESS");
			params.add(financingMap.get("regisAddress"));
		}
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("industry")))){
			sql.append(", INDUSTRY");
			params.add(financingMap.get("industry"));
		}
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("type")))){
			sql.append(", type");
			params.add(financingMap.get("type"));
		}
		sql.append(" )VALUES ");
		sql.append(" (?,?,? ");
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("companyName")))){
			sql.append(",?");
		}
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("regisTime")))){
			sql.append(",?");
		}
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("regisAddress")))){
			sql.append(",?");
		}
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("industry")))){
			sql.append(",?");
		}
		if(!StringUtils.isEmpty(ConvUtils.convToString(financingMap.get("type")))){
			sql.append(",?");
		}
		sql.append(")");
		return update(sql.toString(), params.toArray());
	}
	
}
