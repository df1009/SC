package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IIndexDao;

/**
 * 登录业务数据操作的实现类。
 */

public class IndexDaoImpl extends BaseDao implements IIndexDao {

	//获取banner list方法
	@Override
	public List<Row> getBannerList(String type) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM advertize ");
		sql.append(" WHERE ");
		sql.append(" ADVERTIZE_SHOW_PAGE_ID = '1' ");
		sql.append(" AND VALID_FLG = '1' ");
		sql.append(" AND DEL_FLG = '0' ");
		sql.append(" AND SHOW_TYPE = ? ");
		sql.append(" AND NOW() BETWEEN ON_LINE_DATE AND OFF_LINE_DATE ");
		return queryForList(sql.toString(),new Object[]{type});
	}

	//获取是否绑卡
	@Override
	public Row getStateMessage(String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TC_FLAG from user where OID_USER_ID = ? ");
		return query(sql.toString(), new Object[]{userId});
		}

	//是否进行风险评估标识
	@Override
		public Row getRsStateMessage(String userId) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT RA_FLAG from user where OID_USER_ID = ? ");
			return query(sql.toString(), new Object[]{userId});
			}

	//获取合作机构方法
	@Override
	public List<Row> getCompanionList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM cooperate_company ");
		sql.append(" WHERE ");
		sql.append(" DEL_FLG = '0' ");
		sql.append(" AND COOMPANY_TYPE = '1' ");
		return queryForList(sql.toString(),new Object[]{});
	}

	//战略合作方法
	@Override
	public List<Row> getCooperateList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM cooperate_company ");
		sql.append(" WHERE ");
		sql.append(" DEL_FLG = '0' ");
		sql.append(" AND COOMPANY_TYPE = '2' ");
		return queryForList(sql.toString(),new Object[]{});
	}

	//添加反馈内容
	@Override
	public int insFeedback(Map feedbackMap) {
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO ")
		   .append(" feedback ")
		   .append(" (CONTENT ")
		   .append(" ,CONTACT ")
		   .append(" ,INS_USER_ID ")
		   .append(" ,UPD_USER_ID) ")
		   .append(" VALUES ")
		   .append(" (?,?,?,?) ");
		List<Object> list = new ArrayList<>();
		list.add(feedbackMap.get("content"));
		list.add(feedbackMap.get("contact"));
		list.add(feedbackMap.get("userId"));
		list.add(feedbackMap.get("userId"));
		return update(sql.toString(), list.toArray());
	}
}
