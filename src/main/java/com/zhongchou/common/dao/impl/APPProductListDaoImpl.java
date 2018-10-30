package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.APPProductListDao;

public class APPProductListDaoImpl extends BaseDao implements APPProductListDao{

	@Override
	public List<Row> selProductList(int pageSize, int curPage) {
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
		sql.append(" INNER JOIN PROJECTS_INFO_DES_ONLINE i ");
		sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" AND i.STATUS <> 4");
		sql.append(" ORDER BY ISS_BGN_DT DESC ");
		sql.append(" LIMIT 0 ,10 ");
		return queryForList(sql.toString(),new Object[]{ });
	}

//查询总条数
	@Override
	public int countAppProduct(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		if(selMap ==null){
			sql.append("SELECT count(1) FROM projects_info d");
			sql.append(" INNER JOIN PROJECTS_INFO_DES_ONLINE i ");
			sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
			//sql.append(" WHERE i.LAST_STATE = 1");
			sql.append(" WHERE i.ONLINE_FLAG = 0 ");
			sql.append(" AND i.STATUS <> 4");
			sql.append(" AND i.SHOW_PROJECT_FLAG != 1 ");
		}else{
			sql.append("SELECT count(1) FROM projects_info d");
			sql.append(" INNER JOIN PROJECTS_INFO_DES_ONLINE i ");
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
				//sql.append(" AND i.LAST_STATE = 1");
				sql.append(" AND i.ONLINE_FLAG = 0");
				sql.append(" AND i.STATUS <> 4");
				sql.append(" AND i.SHOW_PROJECT_FLAG != 1 ");
			}else{
				sql.append(" WHERE i.STATUS <> 4");
				sql.append(" AND i.ONLINE_FLAG = 0 ");
				sql.append(" AND i.SHOW_PROJECT_FLAG != 1 ");
			}
			if(selMap.get("platformProjectsSt") != null){
				sql.append(" AND d.PLATFORM_PROJECTS_ST IN (");
				for (String param : ((String)selMap.get("platformProjectsSt")).split(",")) {
					sql.append("?,");
					params.add(param);
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(") ");
				sql.append(" AND i.SHOW_PROJECT_FLAG != 1 ");
			}
		}
		return getCount(sql.toString(), params.toArray());
	}
	//查询精选总条数
			@Override
			public int countAppGoodProduct(Map selMap) {
				StringBuilder sql = new StringBuilder();
				List<Object> params = new ArrayList<>();
				if(selMap ==null){
					sql.append("SELECT count(1) FROM projects_info d");
					sql.append(" INNER JOIN PROJECTS_INFO_DES_ONLINE i ");
					sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
					//sql.append(" WHERE i.LAST_STATE = 1");
					sql.append(" AND i.ONLINE_FLAG = 0");
					sql.append(" AND i.STATUS <> 4");
					sql.append(" AND i.SHOW_PROJECT_FLAG = 1 ");
				}else{
					sql.append("SELECT count(1) FROM projects_info d");
					sql.append(" INNER JOIN PROJECTS_INFO_DES_ONLINE i ");
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
						//sql.append(" AND i.LAST_STATE = 1");
						sql.append(" AND i.ONLINE_FLAG = 0");
						sql.append(" AND i.STATUS <> 4");
						sql.append(" AND i.SHOW_PROJECT_FLAG = 1");

					}else{
						sql.append(" WHERE i.STATUS <> 4");
						sql.append(" AND i.ONLINE_FLAG = 0 ");
						sql.append(" AND i.SHOW_PROJECT_FLAG = 1");
					}
					if(selMap.get("platformProjectsSt") != null){
						sql.append(" AND i.SHOW_PROJECT_FLAG = 1");
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
//分页条件查询
	@Override
	public List<Row> selAppProductList(Map selMap) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT d.OID_PLATFORM_PROJECTS_ID,i.PROJECT_MAIN_TITLE,d.SALES_QUOTA,i.MIN_SUBS_AMT_DES,d.SURPLUS_SALES_QUOTA,d.PLATFORM_PROJECTS_ST,i.SUMMARYTEXT");
		sql.append(",i.PROJECT_COMPANY_NAME,i.PROJECT_ADDS,i.PROJECT_CHARACTER,i.APP_LIST_IMG,i.ISS_END_DT_DES,i.SHOW_PROJECT_FLAG");
		sql.append(",(SELECT count(1) FROM user_tender u where u.OID_PLATFORM_PROJECTS_ID=d.OID_PLATFORM_PROJECTS_ID and u.TENDER_STATUS='3') payPersonNum ");
		sql.append(" FROM PROJECTS_INFO d");
		sql.append(" INNER JOIN PROJECTS_INFO_DES_ONLINE i ");
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
			//sql.append(" AND i.LAST_STATE = 1");
			sql.append(" AND i.ONLINE_FLAG = 0");
			sql.append(" AND i.STATUS <> 4");
			sql.append(" AND i.SHOW_PROJECT_FLAG != 1 ");
		}else{
			sql.append(" WHERE i.STATUS <> 4");
			//sql.append(" AND i.LAST_STATE = 1");
			sql.append(" AND i.ONLINE_FLAG = 0");
			sql.append(" AND i.SHOW_PROJECT_FLAG != 1 ");
		}
		if(selMap.get("platformProjectsSt") != null){
			sql.append(" AND d.PLATFORM_PROJECTS_ST IN (");
			for (String param : ((String)selMap.get("platformProjectsSt")).split(",")) {
				sql.append("?,");
				params.add(param);
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") ");
			sql.append(" AND i.SHOW_PROJECT_FLAG != 1 ");
		}
		sql.append(" ORDER BY d.PLATFORM_PROJECTS_ST ASC,i.INS_DATE DESC");
		/*if(selMap.get("sortType") != null
				&&"0".equals(selMap.get("sortType").toString())){
		}else{
			sql.append(" ORDER BY ISS_BGN_DT DESC");
		}*/

		if(selMap.get("pageIndex") != null){
			return queryForList(sql.toString(),Integer.parseInt(selMap.get("pageIndex").toString()),Integer.parseInt(selMap.get("pageSize").toString()),  params.toArray());
		}else{
			return queryForList(sql.toString(),  params.toArray());
		}
	}
	//分页条件精选产品查询
			@Override
			public List<Row> selAppGoodProductList(Map selMap) {
				List<Object> params = new ArrayList<>();
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT d.OID_PLATFORM_PROJECTS_ID,d.PLATFORM_PROJECTS_SHORT_NM,d.SALES_QUOTA,i.MIN_SUBS_AMT_DES,d.SURPLUS_SALES_QUOTA,d.PLATFORM_PROJECTS_ST,i.SUMMARYTEXT");
				sql.append(",i.PROJECT_COMPANY_NAME,i.PROJECT_ADDS,i.PROJECT_CHARACTER,i.APP_LIST_IMG,i.ISS_END_DT_DES,i.SHOW_PROJECT_FLAG");
				sql.append(",(SELECT count(1) FROM user_tender u where u.OID_PLATFORM_PROJECTS_ID=d.OID_PLATFORM_PROJECTS_ID and u.TENDER_STATUS='3') payPersonNum ");
				sql.append(" FROM PROJECTS_INFO d");
				sql.append(" INNER JOIN PROJECTS_INFO_DES_ONLINE i ");
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
					sql.append(" AND i.SHOW_PROJECT_FLAG = 1 ");
					sql.append(" AND i.ONLINE_FLAG = 0");
					sql.append(" AND i.STATUS <> 4");
				}else{
					sql.append(" WHERE i.STATUS <> 4");
					//sql.append(" AND i.LAST_STATE = 1");
					sql.append(" AND i.ONLINE_FLAG = 0");
					sql.append(" AND i.SHOW_PROJECT_FLAG = 1 ");
				}
				 if(selMap.get("platformProjectsSt") != null){
					sql.append(" AND d.PLATFORM_PROJECTS_ST IN (");
					for (String param : ((String)selMap.get("platformProjectsSt")).split(",")) {
						sql.append("?,");
						params.add(param);
					}
					sql.deleteCharAt(sql.length()-1);
					sql.append(") ");
					sql.append(" AND i.SHOW_PROJECT_FLAG = 1 ");
				}
				sql.append(" ORDER BY d.PLATFORM_PROJECTS_ST ASC,i.INS_DATE DESC");
				/*if(selMap.get("sortType") != null
						&&"0".equals(selMap.get("sortType").toString())){
				}else{
					sql.append(" ORDER BY ISS_BGN_DT DESC");
				}*/

				if(selMap.get("pageIndex") != null){
					return queryForList(sql.toString(),Integer.parseInt(selMap.get("pageIndex").toString()),Integer.parseInt(selMap.get("pageSize").toString()),  params.toArray());
				}else{
					return queryForList(sql.toString(),  params.toArray());
				}
			}
	//查询行业类别
			@Override
			public List<Row> selindustryType() {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT INDUSTRY_ID,INDUSTRY_NM FROM INDUSTRY_TYPE ");
				sql.append(" WHERE STATUS = 1");
				return queryForList(sql.toString(),new Object[]{ });
			}

}
