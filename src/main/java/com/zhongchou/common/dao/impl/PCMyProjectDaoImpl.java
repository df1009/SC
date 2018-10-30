package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IPCMyProjectDao;

/**
 * 个人中心实现类。
 */

public class PCMyProjectDaoImpl extends BaseDao implements IPCMyProjectDao {

	/**
	 * 查询我的项目
	 * 用户的IOD  oidUserId
	 * 项目状态      projectState
	 * 0:待编辑 1：待审核  2：驳回  3 已发布
	 * @return
	 */
	@Override
	
	public int getMyProjectCnt(String oidUserId,String projectState){StringBuilder sql = new StringBuilder();
	List<Object> params = new ArrayList<>();
	if("3".equals(projectState)){//已发布
		sql.append(" SELECT ");
		sql.append(" count(1) ");
		sql.append(" FROM ");
		sql.append(" PROJECTS_INFO_DES_ONLINE o ");
		sql.append(" INNER JOIN PROJECTS_INFO i ON o.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" LEFT JOIN projects_info_des_sup s ON i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
		sql.append(" AND s.DRAFT_FLAG=0 ");
		sql.append(" LEFT JOIN projects_info_detail_sup d ON i.OID_PLATFORM_PROJECTS_ID = d.OID_PLATFORM_PROJECTS_ID  ");
		sql.append(" AND d.DRAFT_FLAG=0 ");
		sql.append(" WHERE o.OID_USER_ID = ? ");
		sql.append(" AND s.OID_PLATFORM_PROJECTS_ID IS NULL  ");
		sql.append(" AND d.OID_PLATFORM_PROJECTS_ID IS NULL  ");
		params.add(oidUserId);
	}else if("0".equals(projectState)){//待编辑
		sql.append(" SELECT  ");
		sql.append(" count(1) ");
		sql.append(" from projects_info i ");
		sql.append(" INNER JOIN projects_info_des_sup s ON i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
		sql.append(" WHERE EXISTS( ");
		sql.append(" SELECT * FROM projects_info_des_sup s  ");
		sql.append(" WHERE i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
		sql.append(" AND s.STATUS = 0  ");
		sql.append(" AND s.LAST_STATE = 0  ");
		sql.append(" AND s.OID_USER_ID = ? ");
		sql.append(" ) ");
		//排除初审状态直接编辑模块和进展提交审核状态的数据
		sql.append(" and not EXISTS( ");
		sql.append(" SELECT * FROM projects_info_detail_sup s  ");
		sql.append(" WHERE i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" AND s.STATUS = 1   ");
		sql.append(" AND s.OID_USER_ID = ?  ");
		sql.append(" ) ");
		params.add(oidUserId);
		params.add(oidUserId);
	}else{// 1：待审核  2：驳回 
		sql.append(" SELECT  ");
		sql.append(" count(1) ");
		sql.append(" from projects_info i ");
		sql.append(" LEFT JOIN projects_info_des_sup s ON i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
		sql.append(" WHERE EXISTS( ");
		sql.append(" SELECT * FROM projects_info_des_sup s  ");
		sql.append(" WHERE i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
		sql.append(" AND s.STATUS = ?  ");
		sql.append(" AND s.DRAFT_FLAG = 0  ");
		sql.append(" AND s.OID_USER_ID = ? ");
		sql.append(" ) ");
		sql.append(" OR EXISTS( ");
		sql.append(" SELECT * FROM projects_info_detail_sup s  ");
		sql.append(" WHERE i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
		sql.append(" AND s.STATUS = ?  ");
		sql.append(" AND s.DRAFT_FLAG = 0  ");
		sql.append(" AND s.OID_USER_ID = ? ");
		sql.append(" ) ");
		params.add(projectState);
		params.add(oidUserId);
		params.add(projectState);
		params.add(oidUserId);
	}
	if(StringUtils.isEmpty(projectState)){//所有
		sql.setLength(0);
		params.removeAll(params);
		sql.append(" SELECT ");
		sql.append(" sum(id) ");
		sql.append("  ");
		sql.append(" FROM ");
		sql.append(" ( ");
		sql.append(" select  ");
		sql.append(" count( proo.OID_PLATFORM_PROJECTS_ID) id ");
		 sql.append("  FROM projects_info_des_online proo   ");
		 sql.append(" LEFT JOIN projects_info_des_sup pros ON proo.OID_PLATFORM_PROJECTS_ID=pros.OID_PLATFORM_PROJECTS_ID  ");
		 sql.append(" LEFT JOIN projects_info_detail_sup prod ON proo.OID_PLATFORM_PROJECTS_ID=prod.OID_PLATFORM_PROJECTS_ID   ");
		 sql.append(" INNER JOIN projects_info proi ON proo.OID_PLATFORM_PROJECTS_ID = proi.OID_PLATFORM_PROJECTS_ID  ");
		 sql.append("  where pros.OID_PLATFORM_PROJECTS_ID IS NULL    ");
		 sql.append(" AND proo.OID_USER_ID = ?  ");
		sql.append(" UNION  ");
		sql.append(" SELECT  ");
		sql.append(" count(proi.OID_PLATFORM_PROJECTS_ID) id ");
		 sql.append("  from projects_info_des_sup pros  ");
		 sql.append(" INNER JOIN projects_info proi ON pros.OID_PLATFORM_PROJECTS_ID = proi.OID_PLATFORM_PROJECTS_ID  ");
		 sql.append(" WHERE pros.OID_USER_ID = ?  ");
		sql.append(" ) a ");
		params.add(oidUserId);
		params.add(oidUserId);
	}
	return getCount(sql.toString(),params.toArray());
	}


	/**
	 * 查询我的项目查询符合条件产品
	 * 用户的IOD  oidUserId
	 * 项目状态      projectState
	 * 空：全部 0:待编辑 1：待审核  2：驳回  3 已发布
	 * @return
	 */
	@Override
	public List<Row> getMyProjectList(String projectState,String oidUserId, int pageSize,int curPage) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		if("3".equals(projectState)){//已发布
			sql.append(" SELECT ");
			sql.append(" i.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" ,i.PLATFORM_PROJECTS_FULL_NM ");
			sql.append(" ,i.PLATFORM_PROJECTS_ST ");
			sql.append(" ,i.SALES_QUOTA ");
			sql.append(" ,i.SURPLUS_SALES_QUOTA ");
			sql.append(" ,i.SHOW_PROJECT_FLAG ");
			sql.append(" ,i.INS_DATE ");
			sql.append(" ,o.ONLINE_FLAG ");
			sql.append(" ,o.STATUS ");
			sql.append(" ,o.LAST_STATE ");
			sql.append(" ,o.APP_LIST_IMG ");
			sql.append(" ,o.PROJECT_MAIN_TITLE ");
			sql.append(" FROM ");
			sql.append(" PROJECTS_INFO_DES_ONLINE o ");
			sql.append(" INNER JOIN PROJECTS_INFO i ON o.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" LEFT JOIN projects_info_des_sup s ON i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
			sql.append(" AND s.DRAFT_FLAG=0 ");
			sql.append(" LEFT JOIN projects_info_detail_sup d ON i.OID_PLATFORM_PROJECTS_ID = d.OID_PLATFORM_PROJECTS_ID  ");
			sql.append(" AND d.DRAFT_FLAG=0 ");
			sql.append(" WHERE o.OID_USER_ID = ? ");
			sql.append(" AND s.OID_PLATFORM_PROJECTS_ID IS NULL  ");
			sql.append(" AND d.OID_PLATFORM_PROJECTS_ID IS NULL  ");
			sql.append(" ORDER BY o.INS_DATE DESC");
			params.add(oidUserId);
		}else if("0".equals(projectState)){//待编辑
			sql.append(" SELECT  ");
			sql.append(" i.OID_PLATFORM_PROJECTS_ID, ");
			sql.append(" i.PLATFORM_PROJECTS_FULL_NM, ");
			sql.append(" i.PLATFORM_PROJECTS_ST, ");
			sql.append(" i.SALES_QUOTA, ");
			sql.append(" i.SURPLUS_SALES_QUOTA, ");
			sql.append(" i.INS_DATE, ");
			sql.append(" i.SHOW_PROJECT_FLAG, ");
			sql.append(" s.ONLINE_FLAG, ");
			sql.append(" s.STATUS, ");
			sql.append(" s.LAST_STATE ");
			sql.append(" ,s.APP_LIST_IMG ");
			sql.append(" ,s.PROJECT_MAIN_TITLE ");
			sql.append(" from projects_info i ");
			sql.append(" INNER JOIN projects_info_des_sup s ON i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
			sql.append(" WHERE EXISTS( ");
			sql.append(" SELECT * FROM projects_info_des_sup s  ");
			sql.append(" WHERE i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
			sql.append(" AND s.STATUS = 0  ");
			sql.append(" AND s.LAST_STATE = 0  ");
			sql.append(" AND s.OID_USER_ID = ? ");
			sql.append(" ) ");
			//排除初审状态直接编辑模块和进展提交审核状态的数据
			sql.append(" and not EXISTS( ");
			sql.append(" SELECT * FROM projects_info_detail_sup s  ");
			sql.append(" WHERE i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" AND s.STATUS = 1   ");
			sql.append(" AND s.OID_USER_ID = ?  ");
			sql.append(" ) ");
			params.add(oidUserId);
			params.add(oidUserId);
		}else{// 1：待审核  2：驳回 
			sql.append(" SELECT  ");
			sql.append(" i.OID_PLATFORM_PROJECTS_ID, ");
			sql.append(" i.PLATFORM_PROJECTS_FULL_NM, ");
			sql.append(" i.PLATFORM_PROJECTS_ST, ");
			sql.append(" i.SALES_QUOTA, ");
			sql.append(" i.SURPLUS_SALES_QUOTA, ");
			sql.append(" i.INS_DATE, ");
			sql.append(" i.SHOW_PROJECT_FLAG, ");
			sql.append(" IFNULL(s.ONLINE_FLAG,(SELECT po.ONLINE_FLAG from projects_info_des_online po WHERE i.OID_PLATFORM_PROJECTS_ID = po.OID_PLATFORM_PROJECTS_ID)) ONLINE_FLAG, ");
			sql.append(" s.STATUS, ");
			sql.append(" s.LAST_STATE ");
			sql.append(" ,IFNULL(s.APP_LIST_IMG,(SELECT po.APP_LIST_IMG from projects_info_des_online po WHERE i.OID_PLATFORM_PROJECTS_ID = po.OID_PLATFORM_PROJECTS_ID)) APP_LIST_IMG ");
			sql.append(" ,s.PROJECT_MAIN_TITLE ");
			sql.append(" from projects_info i ");
			sql.append(" LEFT JOIN projects_info_des_sup s ON i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
			sql.append(" WHERE EXISTS( ");
			sql.append(" SELECT * FROM projects_info_des_sup s  ");
			sql.append(" WHERE i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
			sql.append(" AND s.STATUS = ?  ");
			sql.append(" AND s.DRAFT_FLAG = 0  ");
			sql.append(" AND s.OID_USER_ID = ? ");
			sql.append(" ) ");
			sql.append(" OR EXISTS( ");
			sql.append(" SELECT * FROM projects_info_detail_sup s  ");
			sql.append(" WHERE i.OID_PLATFORM_PROJECTS_ID = s.OID_PLATFORM_PROJECTS_ID  ");
			sql.append(" AND s.STATUS = ?  ");
			sql.append(" AND s.DRAFT_FLAG = 0  ");
			sql.append(" AND s.OID_USER_ID = ? ");
			sql.append(" ) ");
			params.add(projectState);
			params.add(oidUserId);
			params.add(projectState);
			params.add(oidUserId);
			sql.append(" ORDER BY s.INS_DATE DESC");
		}
		if(StringUtils.isEmpty(projectState)){//所有
			sql.setLength(0);
			params.removeAll(params);
			getAllMyProject(sql);
			params.add(oidUserId);
			params.add(oidUserId);
		}
		sql.append(" LIMIT ?, ? ");
		int beginPotion = pageSize * (curPage-1);
		params.add(beginPotion);
		params.add(pageSize);
		return queryForList(sql.toString(),params.toArray());
	}

	/**
	 * 查询所有我的项目信息
	 */
	public void getAllMyProject(StringBuilder sql) {
		 sql.append(" SELECT  ");
		 sql.append(" desid,  ");
		 sql.append(" detailid,  ");
		 sql.append(" PROJECT_MAIN_TITLE,  ");
		 sql.append(" OID_PLATFORM_PROJECTS_ID,  ");
		 sql.append(" PLATFORM_PROJECTS_FULL_NM,  ");
		 sql.append(" PLATFORM_PROJECTS_ST,  ");
		 sql.append(" SALES_QUOTA,  ");
		 sql.append(" SURPLUS_SALES_QUOTA,  ");
		 sql.append(" INS_DATE,  ");
		 sql.append(" SHOW_PROJECT_FLAG,  ");
		 sql.append(" ONLINE_FLAG,  ");
		 sql.append(" STATUS,  ");
		 sql.append(" LAST_STATE,  ");
		 sql.append(" DRAFT_FLAG,  ");
		 sql.append(" APP_LIST_IMG  ");
		 sql.append(" FROM  ");
		 sql.append(" (  "); 
		 sql.append(" select   ");
		 sql.append("  pros.OID_PLATFORM_PROJECTS_ID  desid, ");
		 sql.append(" prod.OID_PLATFORM_PROJECTS_ID detailid, ");
		 sql.append(" proi.OID_PLATFORM_PROJECTS_ID  ");
		 sql.append(" ,proi.PLATFORM_PROJECTS_FULL_NM  ");
		 sql.append(" ,proi.PLATFORM_PROJECTS_ST  ");
		 sql.append(" ,proi.SALES_QUOTA  ");
		 sql.append(" ,proi.SURPLUS_SALES_QUOTA  ");
		 sql.append(" ,proi.INS_DATE  ");
		 sql.append(" ,proi.SHOW_PROJECT_FLAG ");
		 sql.append(" ,proo.ONLINE_FLAG  ");
		 sql.append(" ,proo.STATUS  ");
		 sql.append(" ,proo.PROJECT_MAIN_TITLE  ");
		 sql.append(" ,proo.LAST_STATE  ");
		 sql.append(" ,proo.DRAFT_FLAG  ");
		 sql.append(" ,proo.APP_LIST_IMG  ");
		 sql.append("  FROM projects_info_des_online proo   ");
		 sql.append(" LEFT JOIN projects_info_des_sup pros ON proo.OID_PLATFORM_PROJECTS_ID=pros.OID_PLATFORM_PROJECTS_ID  ");
		 sql.append(" LEFT JOIN projects_info_detail_sup prod ON proo.OID_PLATFORM_PROJECTS_ID=prod.OID_PLATFORM_PROJECTS_ID   ");
		 sql.append(" INNER JOIN projects_info proi ON proo.OID_PLATFORM_PROJECTS_ID = proi.OID_PLATFORM_PROJECTS_ID  ");
		 sql.append("  where pros.OID_PLATFORM_PROJECTS_ID IS NULL    ");
		 sql.append(" AND proo.OID_USER_ID = ?  ");
		 sql.append(" UNION   ");
		 sql.append(" SELECT   ");
		 sql.append(" ''  desid, ");
		 sql.append(" ''  detailid, ");
		 sql.append(" proi.OID_PLATFORM_PROJECTS_ID  ");
		 sql.append(" ,proi.PLATFORM_PROJECTS_FULL_NM  ");
		 sql.append(" ,proi.PLATFORM_PROJECTS_ST  ");
		 sql.append(" ,proi.SALES_QUOTA  ");
		 sql.append(" ,proi.SURPLUS_SALES_QUOTA  ");
		 sql.append(" ,proi.INS_DATE  ");
		 sql.append(" ,proi.SHOW_PROJECT_FLAG ");
		 sql.append(" ,pros.ONLINE_FLAG  ");
		 sql.append(" ,pros.STATUS  ");
		 sql.append(" ,pros.PROJECT_MAIN_TITLE  ");
		 sql.append(" ,pros.LAST_STATE  ");
		 sql.append(" ,pros.DRAFT_FLAG  ");
		 sql.append(" ,pros.APP_LIST_IMG  ");
		 sql.append("  from projects_info_des_sup pros  ");
		 sql.append(" INNER JOIN projects_info proi ON pros.OID_PLATFORM_PROJECTS_ID = proi.OID_PLATFORM_PROJECTS_ID  ");
		 sql.append(" WHERE pros.OID_USER_ID = ?  ");
		 sql.append(" ) a  ");
		sql.append(" ORDER BY a.INS_DATE desc ");
	}

	/**
	 * 获取我的项目详细信息
	 */
	@Override
	public Row getMyProjectSupDetail(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" SELECT pros.*  ");
		sql.append(" ,proi.PLATFORM_PROJECTS_FULL_NM ");
		sql.append(" ,proi.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" ,proi.SALES_QUOTA ");
		sql.append(" FROM projects_info proi ");
		sql.append(" INNER JOIN projects_info_des_sup pros ON proi.OID_PLATFORM_PROJECTS_ID = pros.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" WHERE pros.OID_USER_ID = ? ");
		sql.append(" AND proi.OID_PLATFORM_PROJECTS_ID = ? ");
		params.add((String)selMap.get("oidUserId"));
		params.add((String)selMap.get("projectId"));
		return query(sql.toString(),params.toArray());
	}

	/**
	 * 获取我的项目详细信息
	 */
	@Override
	public Row getMyProjectOnlineDetail(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" SELECT proo.* ");
		sql.append(" ,proi.PLATFORM_PROJECTS_FULL_NM ");
		sql.append(" ,proi.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" ,proi.SALES_QUOTA ");
		sql.append(" FROM projects_info proi ");
		sql.append(" INNER JOIN projects_info_des_online proo ON proi.OID_PLATFORM_PROJECTS_ID = proo.OID_PLATFORM_PROJECTS_ID ");
		sql.append(" WHERE proo.OID_USER_ID = ? ");
		sql.append(" AND proi.OID_PLATFORM_PROJECTS_ID = ? ");
		params.add((String)selMap.get("oidUserId"));
		params.add((String)selMap.get("projectId"));
		return query(sql.toString(),params.toArray());
	}
	/**
	 * 获取我的项目模块信息
	 */
	@Override
	public List<Row> selProductModularDetailList(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" SELECT a.* FROM( ");
		sql.append(" SELECT proo.* FROM projects_info_detail_online proo ");
		sql.append(" LEFT JOIN projects_info_detail_sup pros ON proo.OID_PLATFORM_PROJECTS_ID=pros.OID_PLATFORM_PROJECTS_ID AND proo.PARTY_NUM=pros.PARTY_NUM ");
		sql.append(" INNER JOIN projects_info proi ON proo.OID_PLATFORM_PROJECTS_ID = proi.OID_PLATFORM_PROJECTS_ID  ");
		sql.append(" where pros.OID_PLATFORM_PROJECTS_ID IS NULL ");
		sql.append(" AND proo.OID_USER_ID = ? ");
		sql.append(" AND proo.TYPE = 1 ");
		sql.append(" UNION ALL ");
		sql.append(" SELECT * FROM projects_info_detail_sup WHERE OID_USER_ID = ? ");
		sql.append(" AND TYPE = 1 ");
		sql.append(" )a ");
		sql.append(" WHERE a.OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" ORDER BY a.PARTY_NUM ");
		params.add((String)selMap.get("oidUserId"));
		params.add((String)selMap.get("oidUserId"));
		params.add((String)selMap.get("projectId"));
		return queryForList(sql.toString(),params.toArray());
	}

	/**
	 * 添加项目介绍信息(保存草稿)
	 */
	@Override
	public int saveMyProjectInfo(Map introductionMap) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" PROJECTS_INFO_DES_SUP SET");
		sql.append(" INDUSTRY = ?, ");
		sql.append(" PROJECT_MAIN_TITLE = ?, ");
		sql.append(" SUMMARYTEXT = ?, ");
		sql.append(" LINK_URL = ?, ");
		sql.append(" IMG_PATH = ?, ");
		sql.append(" APP_LIST_IMG = ?, ");
		sql.append(" SELL_SHARE = ?, ");
		sql.append(" PROJECT_ADDS = ?, ");
		sql.append(" PROJECT_COMPANY_NAME = ?, ");
		sql.append(" SHAREHOLDERS_NUM = ?, ");
		sql.append(" COMPANY_TYPE = ?, ");
		sql.append(" CAN_SHAREHOLDERS_NUM = ?, ");
		sql.append(" COMPANY_REGIST_ADDS = ?, ");
		sql.append(" COMPANY_HOLD_TIME = ?, ");
		sql.append(" MIN_TARGET_AMT = ?, ");
		sql.append(" MAX_TARGET_AMT = ?, ");
		sql.append(" MIN_SELLING_SHARE = ?, ");
		sql.append(" MAX_SELLING_SHARE = ?, ");
		sql.append(" GUIDE_COMPANY = ?, ");
		sql.append(" DELIVERY_TIME = ?, ");
		sql.append(" PROJECT_CHARACTER = ?, ");
		sql.append(" DRAFT_FLAG = 1, ");
		sql.append(" SIGNOUT_TIME = ?, ");
		sql.append(" SC_PRODUCTPROSPECTUS = ?, ");
		sql.append(" RISK_LVL_DES = ?, ");
		sql.append(" MIN_SUBS_AMT_DES = ?, ");
		sql.append(" SUBS_ADD_AMT_DES = ?, ");
		sql.append(" MAX_SUBS_AMT_DES = ?, ");
		sql.append(" ISS_BGN_DT_DES = ?, ");
		sql.append(" ISS_END_DT_DES = ?, ");
		sql.append(" ENTERPRISE_ORG_CODE = ?, ");
		sql.append(" ENTERPRISE_LEGAL_PERSON_NAME = ?, ");
		sql.append(" MAJOR_BUSINESS = ?, ");
		sql.append(" FINANCING_PURPOSE = ?, ");
		sql.append(" FINANCING_MODE = ?, ");
		sql.append(" FINANCING_COST = ?, ");
		sql.append(" EXIT_MODE = ?, ");
		sql.append(" ANNOTATION = ?, ");
		sql.append(" STATUS = 0 ");
		sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
		sql.append(" AND OID_USER_ID = ?");
		//params.add(introductionMap.get("mainTitle"));
		params.add(introductionMap.get("industry"));
		params.add(introductionMap.get("title"));
		params.add(introductionMap.get("summarytext"));
		params.add(introductionMap.get("linkUrl"));
		params.add(introductionMap.get("imgPath"));
		params.add(introductionMap.get("appListImg"));
		params.add(introductionMap.get("sellShare"));
		params.add(introductionMap.get("projectAdds"));
		params.add(introductionMap.get("comPanyName"));
		params.add(introductionMap.get("shareholdersNum"));
		params.add(introductionMap.get("companyType"));
		params.add(introductionMap.get("canShareholdersNum"));
		params.add(introductionMap.get("companyRegistAdds"));
		params.add(introductionMap.get("companyHoldTime"));
		params.add(introductionMap.get("minTargetAmt"));
		params.add(introductionMap.get("maxTargetAmt"));
		params.add(introductionMap.get("minSellingShare"));
		params.add(introductionMap.get("maxSellingShare"));
		params.add(introductionMap.get("guideCompany"));
		params.add(introductionMap.get("deliveryTime"));
		params.add(introductionMap.get("projectCharacter"));
		params.add(introductionMap.get("signoutTime"));
		params.add(introductionMap.get("productprospectus"));
		params.add(introductionMap.get("riskLvl"));
		params.add(introductionMap.get("minSubsAmt"));
		params.add(introductionMap.get("subsAddAmt"));
		params.add(introductionMap.get("maxSubsSmt"));
		params.add(introductionMap.get("issBgnDt"));
		params.add(introductionMap.get("issEndDt"));
		params.add(introductionMap.get("orgCode"));
		params.add(introductionMap.get("legalPerson"));
		params.add(introductionMap.get("majorBusiness"));
		params.add(introductionMap.get("financingPurpose"));
		params.add(introductionMap.get("financingMode"));
		params.add(introductionMap.get("financingCost"));
		params.add(introductionMap.get("exitMode"));
		params.add(introductionMap.get("annotation"));
		params.add(introductionMap.get("projectId"));
		params.add(introductionMap.get("oidUserId"));
		return update(sql.toString(), params.toArray());
	}
	
	/**
	 * 添加项目介绍信息
	 */
	@Override
	public int saveInsMyProjectInfo(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("INSERT into projects_info_des_sup  ");
		sql.append(" SELECT * from projects_info_des_online o ");
		sql.append(" WHERE o.OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND o.OID_USER_ID = ? ");
		params.add(selMap.get("projectId"));
		params.add(selMap.get("oidUserId"));
		return update(sql.toString(),params.toArray());
	}
	
	/**
	 * 保存模块和进展时更改项目状态为草稿
	 */
	@Override
	public int updateDesSupSta(Map selMap) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE ");
		sql.append(" projects_info_des_sup ");
		sql.append(" SET ");
		sql.append(" STATUS = 0");
		sql.append(" ,DRAFT_FLAG = 1");
		sql.append(" WHERE ");
		sql.append(" OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND OID_USER_ID = ? ");
		List<Object> list = new ArrayList<>();
		list.add(selMap.get("projectCode"));
		list.add(selMap.get("oidUserId"));
		return update(sql.toString(), list.toArray());
	}
	
	/**
	 * 修改草稿,审核中,驳回,审核通过的项目 查询sup中是否存在
	 * 
	 */
	@Override
	public Row selMyProjectModularToId(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" SELECT * FROM projects_info_detail_sup  ");
		sql.append(" WHERE PROJECTS_INFO_ID = ? ");
		sql.append(" AND OID_USER_ID=? ");
		sql.append(" AND TYPE=? ");
		params.add(selMap.get("projectId"));
		params.add(selMap.get("oidUserId"));
		params.add(selMap.get("type"));
		return query(sql.toString(),params.toArray());
	}
	/**
	 * 修改项目模块信息时复制online表(保存草稿)
	 * 保证主键PROJECTS_INFO_ID在两张表中一致
	 */
	@Override
	public int updSaveMyProjectModularCopy(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("INSERT into projects_info_detail_sup  ");
		sql.append(" SELECT * from projects_info_detail_online o ");
		sql.append(" WHERE o.OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND o.OID_USER_ID = ? ");
		sql.append(" AND o.PROJECTS_INFO_ID = ? ");
		sql.append(" AND o.TYPE = 1");//只有项目模块才能更改通过审核的
		params.add(selMap.get("projectCode"));
		params.add(selMap.get("oidUserId"));
		params.add(selMap.get("projectId"));
		return update(sql.toString(),params.toArray());
	}

	/**
	 * 修改项目模块和进展信息第二步(修改与online不同点)(保存草稿)
	 * 
	 */
	@Override
	public int updSaveMyProjectModular(Map proMap) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE ");
		sql.append(" projects_info_detail_sup ");
		sql.append(" SET ");
		sql.append(" PARTY_NUM =?");
		sql.append(" ,STATUS = 0");
		sql.append(" ,TITLE = ?");
		sql.append(" ,SUMMARYTEXT = ?");
		sql.append(" ,UPD_DATE =NOW()");
		sql.append(" ,UPD_USER_ID =?");
		sql.append(" ,DRAFT_FLAG=1");
		sql.append(" ,PROGRESS_DATE=?");
		sql.append(" ,IMG_PATH=?");
		sql.append(" WHERE ");
		sql.append(" OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND PROJECTS_INFO_ID = ? ");
		sql.append(" AND OID_USER_ID = ? ");
		sql.append(" AND TYPE = ?");
		List<Object> list = new ArrayList<>();
		list.add(proMap.get("partyNum"));
		list.add(proMap.get("projectTitle"));
		list.add(proMap.get("projectContent"));
		list.add(proMap.get("oidUserId"));
		list.add(proMap.get("progressDate"));
		list.add(proMap.get("imgPath"));
		list.add(proMap.get("projectCode"));
		list.add(proMap.get("projectId"));
		list.add(proMap.get("oidUserId"));
		list.add(proMap.get("type"));
		return update(sql.toString(), list.toArray());
	}
	/**
	 * 新增项目模块信息(保存草稿)
	 */
	@Override
	public int AddSaveMyProjectModular(Map proMap){
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO projects_info_detail_sup  ");
		sql.append(" ( ");
		sql.append(" OID_PLATFORM_PROJECTS_ID, ");
		sql.append(" PARTY_NUM, ");
		sql.append(" TITLE, ");
		sql.append(" SUMMARYTEXT, ");
		sql.append(" INS_USER_ID, ");
		sql.append(" STATUS, ");
		sql.append(" OID_USER_ID, ");
		sql.append(" DRAFT_FLAG, ");
		sql.append(" IMG_PATH, ");
		sql.append(" PROGRESS_DATE, ");
		sql.append(" TYPE) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,0,?,'1',?,?,?) ");

		Object[] params = new Object[]{
				proMap.get("projectCode"),
				proMap.get("partyNum"),
				proMap.get("projectTitle"),
				proMap.get("projectContent"),
				proMap.get("oidUserId"),
				proMap.get("oidUserId"),
				proMap.get("imgPath"),
				proMap.get("progressDate"),
				proMap.get("type")
				};
		return update(sql.toString(), params);
	}

	/**
	 * 获取我的项目项目审核通过的进展信息
	 */
	@Override
	public List<Row> selProductProgressOnlineList(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" SELECT PROJECTS_INFO_ID id ,PROGRESS_DATE progressDate,SUMMARYTEXT projectContent,IMG_PATH imgPath from projects_info_detail_online ");
		sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND OID_USER_ID = ? ");
		sql.append(" AND TYPE = 2 ");
		params.add((String)selMap.get("projectId"));
		params.add((String)selMap.get("oidUserId"));
		return queryForList(sql.toString(),params.toArray());
	}
	
	/**
	 * 获取我的项目项目草稿，审核中，驳回的的进展信息
	 */
	@Override
	public List<Row> selProductProgressSupList(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" SELECT PROJECTS_INFO_ID id ,DATE_FORMAT(PROGRESS_DATE,'%Y-%m-%d') progressDate,SUMMARYTEXT projectContent,IMG_PATH imgPath from projects_info_detail_sup ");
		sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND OID_USER_ID = ? ");
		sql.append(" AND TYPE = 2 ");
		sql.append(" ORDER BY PROGRESS_DATE DESC ");
		params.add((String)selMap.get("projectId"));
		params.add((String)selMap.get("oidUserId"));
		return queryForList(sql.toString(),params.toArray());
	}
	
	/**
	 * 项目介绍信息提交审核
	 */
	@Override
	public int submitProjectExaminerDes(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" UPDATE projects_info_des_sup SET STATUS = 1,DRAFT_FLAG=0 ");
		sql.append(" WHERE OID_USER_ID = ? ");
	/*	sql.append(" AND DRAFT_FLAG = 1 ");
		sql.append(" AND STATUS = 0 ");*/
		params.add((String)selMap.get("oidUserId"));
		return update(sql.toString(), params.toArray());
	}
	/**
	 * 项目模块信息和项目进展提交审核
	 */
	@Override
	public int submitProjectExaminerDetail(Map selMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" UPDATE projects_info_detail_sup SET STATUS = 1,DRAFT_FLAG=0 ");
		sql.append(" WHERE OID_USER_ID = ? ");
		/*sql.append(" AND DRAFT_FLAG = 1 ");
		sql.append(" AND STATUS = 0 ");*/
		params.add((String)selMap.get("oidUserId"));
		return update(sql.toString(), params.toArray());
	}
	//删除项目模块
	@Override
	public int deleteProjectInformationSup(Map reqMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("DELETE FROM ");
		sql.append(" projects_info_detail_sup ");
		sql.append(" WHERE ");
		sql.append(" OID_PLATFORM_PROJECTS_ID = ?  ");
		sql.append(" AND INS_USER_ID = ?  ");
		sql.append(" AND PROJECTS_INFO_ID = ?  ");
		params.add((String)reqMap.get("projectCode"));
		params.add((String)reqMap.get("oidUserId"));
		params.add((String)reqMap.get("projectId"));
		if("1".equals(reqMap.get("projectType"))){//模块
			sql.append(" AND TYPE = 1  ");
			sql.append(" AND PARTY_NUM = ?  ");
			params.add((String)reqMap.get("partyNum"));
		}else{//进展
			sql.append(" AND TYPE = 2  ");
		}
		return update(sql.toString(), params.toArray());
	}
	//删除项目模块
	@Override
	public int deleteProjectInformationOnline(Map reqMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(" projects_info_detail_online ");
		sql.append(" WHERE ");
		sql.append(" OID_PLATFORM_PROJECTS_ID = ?  ");
		sql.append(" AND PARTY_NUM = ?  ");
		sql.append(" AND INS_USER_ID = ?  ");
		sql.append(" AND PROJECTS_INFO_ID = ?  ");
		sql.append(" AND TYPE = 1  ");//审核通过的进展不能删除
		Object[] params = new Object[]{
				reqMap.get("projectCode"),
				reqMap.get("partyNum"),
				reqMap.get("oidUserId"),
				reqMap.get("projectId")
				};
		return update(sql.toString(), params);
	}
	//获取我的项目驳回原因
	@Override
	public List<Row> getVerifyList(String projectCode,String oidUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" *, ");
		sql.append(" DATE_FORMAT(T1.INS_DATE,'%Y-%m-%d') AS INS_DATE ");
		sql.append(" FROM PROJECTS_INFO_VERIFY T1 ");
		sql.append(" WHERE T1.OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND T1.OWNER_USER_ID=? ");
		sql.append(" ORDER BY T1.INS_DATE DESC ");
	return queryForList(sql.toString(),new Object[]{projectCode,oidUserId});
	}
	//查询用户是否有我的项目权限
	@Override
	public int getMyProject(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(a) from ");
		sql.append(" (select COUNT(*) a from projects_info_des_online WHERE OID_USER_ID = ?    ");
		sql.append(" union all ");
		sql.append(" select COUNT(*) a from projects_info_des_sup WHERE OID_USER_ID = ? ) c ");
		List<Object> params = new ArrayList<>();
		params.add(loginId);
		params.add(loginId);
		return getCount(sql.toString(), params.toArray());
	}
	 //查询产品的在detail中的数据
    @Override
    public Row selProjectToDetail(String proId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * from projects_info_detail_sup ");
        sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ? ");
        return query(sql.toString(),new Object[]{proId});
    }
   //查询产品的在detail中的数据MyProjectModularSort
    @Override
    public Row selMyProjectModularSort(String projectCode,String oidUserId) {
        StringBuilder sql = new StringBuilder();
        /*sql.append(" SELECT * from projects_info_detail_sup ");
        sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ? ");*/
        return singleQuery(sql.toString(),new Object[]{projectCode,oidUserId});
    }
   //设置模块序号
    @Override
    public int setMyProjectModularSortSup(Map selMap) {
    	StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" UPDATE projects_info_detail_sup SET PARTY_NUM = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" AND OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND PROJECTS_INFO_ID = ? ");
		sql.append(" AND TYPE = 1 ");
		params.add((String)selMap.get("partyNum"));
		params.add((String)selMap.get("oidUserId"));
		params.add((String)selMap.get("projectCode"));
		params.add((String)selMap.get("projectId"));
		return update(sql.toString(), params.toArray());
    }
  //设置模块序号
    @Override
    public int setMyProjectModularSortOnline(Map selMap) {
    	StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" UPDATE projects_info_detail_online SET PARTY_NUM = ? ");
		sql.append(" WHERE OID_USER_ID = ? ");
		sql.append(" AND OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND PROJECTS_INFO_ID = ? ");
		sql.append(" AND TYPE = 1 ");
		params.add((String)selMap.get("partyNum"));
		params.add((String)selMap.get("oidUserId"));
		params.add((String)selMap.get("projectCode"));
		params.add((String)selMap.get("projectId"));
		return update(sql.toString(), params.toArray());
    }
}
