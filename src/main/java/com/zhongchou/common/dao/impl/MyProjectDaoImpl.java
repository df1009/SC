package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IMyProjectDao;
import com.zhongchou.common.dto.SendEmailDto;

/**
 * 个人中心实现类。
 */

public class MyProjectDaoImpl extends BaseDao implements IMyProjectDao {

	@Override
	public int getMyProjectCnt(String oidUserId,String projectState){
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append(" SELECT ");
		sql.append(" count(1) ");
		sql.append(" FROM PROJECTS_INFO_DES T2 ");
		sql.append(" LEFT JOIN ");
		sql.append("  PROJECTS_INFO T1");
		sql.append(" ON T1.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
		if("1".equals(projectState)){//未发布
			sql.append(" WHERE T2.OID_USER_ID = ? ");
			sql.append("	AND T2.LAST_STATE = 0 ");
			sql.append("	AND T2.STATUS not in (1,5)");
			params.add(oidUserId);
		}else if("2".equals(projectState)){//审核中
			sql.append(" WHERE T2.OID_USER_ID = ? ");
			sql.append(" AND T2.STATUS IN (1,5) ");
			sql.append("  OR (");
			sql.append(" SELECT count(1) FROM projects_info_detal T3 ");
			sql.append(" WHERE T3.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID");
			sql.append(" AND T3.INS_USER_ID = ?");
			sql.append(" AND T3.STATUS IN (1,5)");
			sql.append(" AND T2.STATUS <> 4");
			sql.append(" )>0 ");
			params.add(oidUserId);
			params.add(oidUserId);
		}else if("3".equals(projectState)){//已发布
			sql.append(" WHERE T2.OID_USER_ID = ? ");
			sql.append(" AND T2.STATUS in (2,3,4)");
			sql.append(" AND T2.LAST_STATE = 1");
			sql.append("  AND(");
			sql.append(" SELECT count(1) FROM projects_info_detal T3 ");
			sql.append(" WHERE T3.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID");
			sql.append(" AND T3.INS_USER_ID = ?");
			sql.append(" AND T3.STATUS in (1,5)");
			sql.append(" )=0 ");
			params.add(oidUserId);
			params.add(oidUserId);
		}
  		return getCount(sql.toString(), params.toArray());

	}


	//获取我的项目方法
	@Override
	public List<Row> getMyProjectList(String projectState,String oidUserId, int pageSize,int curPage) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		if("2".equals(projectState)){
			sql.append(" SELECT ");
			sql.append(" PLATFORM_PROJECTS_SHORT_NM, ");
			sql.append(" ENTERPRISE_LEGAL_PERSON_NAME, ");
			sql.append(" OID_PLATFORM_PROJECTS_ID, ");
			sql.append(" ISS_BGN_DT, ");
			sql.append(" ISS_END_DT, ");
			sql.append(" INS_DATE, ");
			sql.append(" STATUS ");
			sql.append(" FROM( ");
			sql.append(" SELECT ");
			sql.append(" T1.PLATFORM_PROJECTS_SHORT_NM, ");
			sql.append(" T1.ENTERPRISE_LEGAL_PERSON_NAME, ");
			sql.append(" T1.OID_PLATFORM_PROJECTS_ID, ");
			sql.append(" T1.ISS_BGN_DT, ");
			sql.append(" T1.ISS_END_DT, ");
			sql.append(" T2.INS_DATE,");
			sql.append(" '1' STATUS");
			sql.append(" FROM PROJECTS_INFO_DES T2 ");
			sql.append(" LEFT JOIN ");
			sql.append("  PROJECTS_INFO T1");
			sql.append(" ON T1.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" WHERE T2.OID_USER_ID = ? ");
			sql.append(" AND T2.STATUS = 1");
			sql.append("  OR (");
			sql.append(" SELECT count(1) FROM projects_info_detal T3 ");
			sql.append(" WHERE T3.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID");
			sql.append(" AND T3.INS_USER_ID = ?");
			sql.append(" AND T3.STATUS = 1");
			sql.append(" AND T2.STATUS <> 4");
			sql.append(" )>0 ");
			sql.append("  AND (");
			sql.append(" SELECT count(1) FROM projects_info_detal T4 ");
			sql.append(" WHERE T4.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID");
			sql.append(" AND T4.INS_USER_ID = ?");
			sql.append(" AND T4.STATUS = 5");
			sql.append(" AND T2.STATUS <> 4");
			sql.append(" )=0 ");
			sql.append(" UNION");
			sql.append(" SELECT ");
			sql.append("  T1.PLATFORM_PROJECTS_SHORT_NM, ");
			sql.append(" T1.ENTERPRISE_LEGAL_PERSON_NAME, ");
			sql.append(" T1.OID_PLATFORM_PROJECTS_ID, ");
			sql.append(" T1.ISS_BGN_DT, ");
			sql.append(" T1.ISS_END_DT, ");
			sql.append(" T2.INS_DATE,");
			sql.append(" '5' STATUS");
			sql.append(" FROM PROJECTS_INFO_DES T2 ");
			sql.append(" INNER JOIN ");
			sql.append(" PROJECTS_INFO T1");
			sql.append(" ON T1.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
			sql.append(" WHERE T2.OID_USER_ID = ? ");
			sql.append(" AND T2.STATUS = 5");
			sql.append(" OR (");
			sql.append(" SELECT count(1) FROM projects_info_detal T3 ");
			sql.append(" WHERE T3.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID");
			sql.append(" AND T3.INS_USER_ID = ?");
			sql.append(" AND T3.STATUS =5");
			sql.append(" AND T2.STATUS <> 4");
			sql.append(" )>0 ");
			params.add(oidUserId);
			params.add(oidUserId);
			params.add(oidUserId);
			params.add(oidUserId);
			params.add(oidUserId);
			sql.append(" ) AS a ");
			sql.append(" ORDER BY INS_DATE DESC");
		}else{
			sql.append(" SELECT ");
			sql.append(" T1.PLATFORM_PROJECTS_SHORT_NM, ");
			sql.append(" T1.ENTERPRISE_LEGAL_PERSON_NAME, ");
			sql.append(" T1.OID_PLATFORM_PROJECTS_ID, ");
			sql.append(" T1.ISS_BGN_DT, ");
			sql.append(" T1.ISS_END_DT, ");
			sql.append(" T2.INS_DATE,");
			sql.append(" T2.STATUS ");
			sql.append(" FROM PROJECTS_INFO_DES T2 ");
			sql.append(" LEFT JOIN ");
			sql.append("  PROJECTS_INFO T1");
			sql.append(" ON T1.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
			if("1".equals(projectState)){//未发布
				sql.append(" WHERE T2.OID_USER_ID = ? ");
				sql.append("	AND T2.LAST_STATE = 0 ");
				sql.append("	AND T2.STATUS not in (1,5)");
				params.add(oidUserId);
			}else if("3".equals(projectState)){//已发布
				sql.append(" WHERE T2.OID_USER_ID = ? ");
				sql.append(" AND T2.STATUS in (2,3,4)");
				sql.append(" AND T2.LAST_STATE = 1");
				sql.append("  AND(");
				sql.append(" SELECT count(1) FROM projects_info_detal T3 ");
				sql.append(" WHERE T3.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID");
				sql.append(" AND T3.INS_USER_ID = ?");
				sql.append(" AND T3.STATUS IN (1,5)");
				sql.append(" )=0 ");
				params.add(oidUserId);
				params.add(oidUserId);
			}
			sql.append(" ORDER BY T2.INS_DATE DESC");
		}

		sql.append(" LIMIT ?, ? ");
		int beginPotion = pageSize * (curPage-1);
		params.add(beginPotion);
		params.add(pageSize);
		return queryForList(sql.toString(),params.toArray());
	}

	//获取app我的项目方法
		@Override
		public List<Row> getMyAppProjectList(String oidUserId, int pageSize,int curPage) {
			StringBuilder sql = new StringBuilder();
			List<Object> params = new ArrayList<>();
				sql.append(" SELECT ");
				sql.append("T1.SALES_QUOTA,T1.SURPLUS_SALES_QUOTA,");
				sql.append("T1.PLATFORM_PROJECTS_ST,");
				sql.append(" T1.PLATFORM_PROJECTS_SHORT_NM, ");
				sql.append(" T1.ENTERPRISE_LEGAL_PERSON_NAME, ");
				sql.append(" T1.OID_PLATFORM_PROJECTS_ID, ");
				sql.append(" T1.ISS_BGN_DT, ");
				sql.append(" T1.ISS_END_DT, ");
				sql.append(" T2.APP_LIST_IMG,");
				sql.append(" T2.SHOW_PROJECT_FLAG,");
				sql.append(" T2.INS_DATE,");
				sql.append(" T2.STATUS ");
				sql.append(" FROM projects_info_des_online T2 ");
				sql.append(" LEFT JOIN ");
				sql.append("  PROJECTS_INFO T1");
				sql.append(" ON T1.OID_PLATFORM_PROJECTS_ID = T2.OID_PLATFORM_PROJECTS_ID ");
				sql.append(" WHERE T2.OID_USER_ID = ? ");
				sql.append(" ORDER BY T2.INS_DATE DESC");
			sql.append(" LIMIT ?, ? ");
			int beginPotion = pageSize * (curPage-1);
			params.add(oidUserId);
			params.add(beginPotion);
			params.add(pageSize);
			return queryForList(sql.toString(),params.toArray());
		}

	//获取全部行业
	@Override
	public List<Row> getIndustry() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * ");
		sql.append(" FROM INDUSTRY_TYPE ");
		sql.append(" WHERE STATUS ='1'");
		return queryForList(sql.toString(),new Object[]{});
	}


	//获取我的项目信息及项目进展详情方法
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

	//查入项目信息
		@Override
		public int insertProjectInformationDes(Map introductionMap) {
			List<Object> params = new ArrayList<>();
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ");
			sql.append(" PROJECTS_INFO_DES ");
			sql.append(" (OID_PLATFORM_PROJECTS_ID, ");
			sql.append(" INDUSTRY, ");
			sql.append(" OID_USER_ID, ");
			sql.append(" IMG_PATH, ");
			sql.append(" LINK_URL, ");
			sql.append(" SUMMARYTEXT, ");
			sql.append(" TITLE, ");
			sql.append(" INS_DATE, ");
			sql.append(" LAST_STATE, ");
			sql.append(" STATUS) ");
			sql.append(" VALUES ");
			sql.append(" (?,?,?,?,?,'',now(),'0',?) ");
			params.add(introductionMap.get("projectCode"));
			params.add(introductionMap.get("industry"));
			params.add(introductionMap.get("oidUserId"));
			params.add(introductionMap.get("imgUrl"));
			params.add(introductionMap.get("videoUrl"));
			params.add(introductionMap.get("introduction"));
			params.add(introductionMap.get("status"));
			return update(sql.toString(), params.toArray());
		}

		//更新项目信息
		@Override
		public int updateProjectInformationDes(Map introductionMap) {

			List<Object> params = new ArrayList<>();
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ");
			sql.append(" PROJECTS_INFO_DES SET");
			//sql.append(" PROJECT_MAIN_TITLE = ?, ");
			sql.append(" INDUSTRY = ?, ");
			sql.append(" IMG_PATH = ?, ");
			//sql.append(" APP_IMG = ?, ");
			sql.append(" LINK_URL = ?, ");
			sql.append(" STATUS = ?, ");
			sql.append(" SUMMARYTEXT = ?, ");
			sql.append(" UPD_DATE = now() ");
			sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
			//params.add(introductionMap.get("mainTitle"));
			params.add(introductionMap.get("industry"));
			params.add(introductionMap.get("imgUrl"));
			//params.add(introductionMap.get("appImgUrl"));
			params.add(introductionMap.get("videoUrl"));
			params.add(introductionMap.get("status"));
			params.add(introductionMap.get("introduction"));
			params.add(introductionMap.get("projectCode"));
			return update(sql.toString(), params.toArray());
		}

	//查入项目信息
	@Override
	public int insertProjectInformationDet(Map proMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" PROJECTS_INFO_DETAL ");
		sql.append(" (OID_PLATFORM_PROJECTS_ID, ");
		sql.append(" PARTY_NUM, ");
		sql.append(" TITLE, ");
		sql.append(" SUMMARYTEXT, ");
		sql.append(" INS_USER_ID, ");
		sql.append(" STATUS, ");
		sql.append(" TYPE) ");
		sql.append(" VALUES ");
		sql.append(" (?,?,?,?,?,?,'1') ");

		Object[] params = new Object[]{
				proMap.get("projectCode"),
				proMap.get("partyNum"),
				proMap.get("projectTitle"),
				proMap.get("projectContent"),
				proMap.get("oidUserId"),
				proMap.get("status")
				};
		return update(sql.toString(), params);
	}

	//查入项目进展信息
	@Override
	public int insertProjectProgress(List<Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" PROJECTS_INFO_DETAL ");
		sql.append(" (OID_PLATFORM_PROJECTS_ID, ");
		sql.append(" PARTY_NUM, ");
		sql.append(" STATUS, ");
		sql.append(" TITLE, ");
		sql.append(" SUMMARYTEXT, ");
		sql.append(" PROGRESS_DATE, ");
		sql.append(" INS_USER_ID, ");
		sql.append(" IMG_PATH, ");
		sql.append(" TYPE) ");
		sql.append(" VALUES ");
		sql.append(" (?,'','1','',?,?,?,?,'2') ");
		return update(sql.toString(), params.toArray());
	}

	//修改项目进展信息
	@Override
	public int updProjectProgress(List<Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" PROJECTS_INFO_DETAL ");
		sql.append(" SET STATUS = '1'");
		sql.append(" ,SUMMARYTEXT=? ");
		sql.append(" ,PROGRESS_DATE=? ");
		sql.append(" ,IMG_PATH=? ");
		sql.append(" WHERE PROJECTS_INFO_ID=? ");
		sql.append(" AND STATUS = '5' ");
		return update(sql.toString(), params.toArray());
	}

	@Override
	public Row getUserInfo(String loginId) {
		StringBuilder sql = new StringBuilder();
		Map<String,String> user = new HashMap<String,String>();
		sql.append("SELECT USER_NAME, ");
		sql.append(" ID_CARD_TYPE, ");
		sql.append(" ID_CARD_NO ");
		sql.append(" FROM USER_ID_VERIFICATION ");
		sql.append(" WHERE OID_USER_ID = ?");
		return singleQuery(sql.toString(), new Object[]{loginId});
	}




	@Override
	public int deleteProjectInformation(String projectCode,String partyNum,String oidUserId,String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(" PROJECTS_INFO_DETAL ");
		sql.append(" WHERE ");
		sql.append(" OID_PLATFORM_PROJECTS_ID = ?  ");
		sql.append(" AND PARTY_NUM = ?  ");
		sql.append(" AND INS_USER_ID = ?  ");
		sql.append(" AND PROJECTS_INFO_ID = ?  ");
		return update(sql.toString(), new Object[]{projectCode,partyNum,oidUserId,id});
	}



	@Override
	public int updateDes(String projectCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE ");
		sql.append(" PROJECTS_INFO_DES ");
		sql.append(" SET ");
		sql.append(" STATUS = '2' ");
		sql.append(" WHERE ");
		sql.append(" OID_PLATFORM_PROJECTS_ID = ? ");
		return update(sql.toString(), new Object[]{projectCode});
	}

	@Override
	public int updateDet(String projectCode,String oidUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE ");
		sql.append(" PROJECTS_INFO_DETAL ");
		sql.append(" SET ");
		sql.append(" STATUS = '1' ");
		sql.append(" WHERE ");
		sql.append(" OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND STATUS = '2' ");
		sql.append(" AND INS_USER_ID = ? ");
		sql.append(" AND TYPE = '1' ");
		return update(sql.toString(), new Object[]{projectCode,oidUserId});
	}

	/**
	 * 查询用户邮箱
	 */
	@Override
	public Row queryUserEmail(String loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT EMAIL FROM USER WHERE OID_USER_ID=?");
		return query(sql.toString(), loginId);
	}

	//获取审核信息
	  @Override
		public List<Row> getCheckFrzz(String projectID) {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT ");
			sql.append(" * ");
			sql.append(" FROM PROJECTS_INFO_VERIFY  ");
			sql.append(" WHERE ");
			sql.append(" OID_PLATFORM_PROJECTS_ID= ? ");
			return queryForList(sql.toString(),new Object[]{projectID});
	  }

	/**
	 * 绑定用户邮箱
	 */
	@Override
	public int bindUserEmail(String loginId, String userEmail) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE USER SET EMAIL = ? WHERE OID_USER_ID=?");
		return update(sql.toString(), new Object[]{userEmail,loginId});
	}
	/**
	 * 发送邮件记录
	 */
	@Override
	public void recordEmailLog(SendEmailDto emailDto) {
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO ")
		   .append(" SEND_MAIL_LOG ")
		   .append(" (OID_USER_ID ")
		   .append(" ,FROM_ADDRESS ")
		   .append(" ,SEND_RESULT_FLG ")
		   .append(" ,MAIL_TYPE ")
		   .append(" ,TO_ADDRESS ")
		   .append(" ,SUBJECT ")
		   .append(" ,BODY) ")
		   .append(" VALUES ")
		   .append(" (?,?,?,?,?,?,?) ");
		List<Object> list = new ArrayList<>();
		list.add(emailDto.getLoginId());
		list.add(emailDto.getFrom());
		list.add(emailDto.getSendResultFlg());
		list.add("01");
		list.add(emailDto.getTo());
		list.add(emailDto.getSubject());
		list.add(emailDto.getMessageText());
		update(sql.toString(), list.toArray());
	}
	/**
	 * 添加用户头像
	 */


	@Override
	public void updateHeadImage(String loginId, String newPath) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE user_detail SET USER_ICON_FILE_ID = ? WHERE OID_USER_ID=?");
		update(sql.toString(), new Object[]{newPath,loginId});
	}

	//查询项目是不是本人发布的项目
	@Override
	public Row selProjectAscription(String proId,String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT 1 from projects_info_des ");
		sql.append(" where OID_USER_ID = ? ");
		sql.append(" and OID_PLATFORM_PROJECTS_ID =?");
		return singleQuery(sql.toString(),new Object[]{userId,proId});
	}
	//查询项目介绍信息
	@Override
	public Row getprojectIntroduce(String proId,String userId) {
		StringBuilder sql = new StringBuilder();
		Map<String,String> user = new HashMap<String,String>();
		sql.append(" SELECT INDUSTRY ,IMG_PATH,LINK_URL,SUMMARYTEXT,LAST_STATE  from projects_info_des ");
		sql.append(" where OID_USER_ID = ? ");
		sql.append(" and OID_PLATFORM_PROJECTS_ID =?");
		return singleQuery(sql.toString(),new Object[]{userId,proId});
	}

	//修改项目 信息
	@Override
	public int updateProjectDetal(Map proMap) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE ");
		sql.append(" PROJECTS_INFO_DETAL ");
		sql.append(" SET ");
		sql.append(" PARTY_NUM =?");
		sql.append(" ,STATUS = ?");
		sql.append(" ,TITLE = ?");
		sql.append(" ,SUMMARYTEXT = ?");
		sql.append(" ,UPD_DATE =NOW()");
		sql.append(" ,UPD_USER_ID =?");
		sql.append(" ,TYPE=1");
		sql.append(" WHERE ");
		sql.append(" OID_PLATFORM_PROJECTS_ID = ? ");
		sql.append(" AND PROJECTS_INFO_ID = ? ");
		sql.append(" AND INS_USER_ID = ? ");

		List<Object> list = new ArrayList<>();
		list.add(proMap.get("partyNum"));
		list.add(proMap.get("status"));
		list.add(proMap.get("projectTitle"));
		list.add(proMap.get("projectContent"));
		list.add(proMap.get("oidUserId"));
		list.add(proMap.get("projectCode"));
		list.add(proMap.get("id"));
		list.add(proMap.get("oidUserId"));
		return update(sql.toString(), list.toArray());
	}


	@Override
	public int deleteProjectProgress(String projectCode, String id,
			String oidUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(" PROJECTS_INFO_DETAL ");
		sql.append(" WHERE ");
		sql.append(" OID_PLATFORM_PROJECTS_ID = ?  ");
		sql.append(" AND PROJECTS_INFO_ID = ?  ");
		sql.append(" AND INS_USER_ID = ?  ");
		sql.append(" AND STATUS = '5'  ");
		return update(sql.toString(), new Object[]{projectCode,id,oidUserId});
	}


	@Override
	public List<Row> selIntroductionTemplate() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" * ");
		sql.append(" FROM projects_info_detal  ");
		sql.append(" WHERE TYPE = 0");
		sql.append(" AND STATUS = 3");
		sql.append(" ORDER BY CAST(PARTY_NUM AS SIGNED)  ");
		return queryForList(sql.toString(),new Object[]{});
	}


	@Override
	public Row selSingleIntroductionTemplate(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" * ");
		sql.append(" FROM projects_info_detal  ");
		sql.append(" WHERE TYPE = 0");
		sql.append(" AND STATUS = 3");
		sql.append(" AND PROJECTS_INFO_ID = ?");
		return singleQuery(sql.toString(),new Object[]{id});
	}
}
