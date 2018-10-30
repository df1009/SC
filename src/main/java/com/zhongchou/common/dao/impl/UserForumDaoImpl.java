package com.zhongchou.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.UserForumDao;
import com.zhongchou.common.dto.UserForum;


/**
 * 登录业务数据操作的接口。
 */
public class UserForumDaoImpl extends BaseDao implements UserForumDao{

	@Override
	public int selFloorCount(String projectId, String fatherFloor) {
		StringBuilder sql = new StringBuilder();
		if("0".equals(fatherFloor)){//主评论
			sql.append("SELECT COUNT(1) FROM FORUM_DETAIL ");
			sql.append(" WHERE UP_LEVEL=? ");
			sql.append(" AND OID_FINANCE_PROJECTS_ID=? ");
		}else{//子评论
			sql.append("SELECT COUNT(1) FROM FORUM_DETAIL ");
			sql.append(" WHERE UP_LEVEL=( ");
			sql.append(" SELECT LEVEL FROM FORUM_DETAIL ");
			sql.append(" WHERE FORUM_ID =? )");
			sql.append(" AND OID_FINANCE_PROJECTS_ID=? ");
		}
		return getCount(sql.toString(), new Object[]{fatherFloor,projectId});
	}
	@Override
	public int insertForum(UserForum userForum) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(" FORUM_DETAIL ");
		sql.append(" (OID_FINANCE_PROJECTS_ID, OID_USER_ID, NICKNAME, ANSWER_NICKNAME,ANSWER_OID_USER_ID,");
		sql.append(" USER_ICON_FILE_ID,FORUM_NEWS, LEVEL, UP_LEVEL, INS_DATE,INS_IP,DELETE_FLAG,SHOW_FLAG) ");
		sql.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, '0',?) ");
		Object[] params = new Object[]{
				userForum.getOidFinanceProjectsId(),
				userForum.getOidUserId(),
				userForum.getNickName(),
				userForum.getAnswerNickname(),
				userForum.getAnswerOidUserId(),
				userForum.getUserIconFileId(),
				userForum.getForumNews(),
				userForum.getLevel(),
				userForum.getUpLevel(),
				userForum.getInsIp(),
				userForum.getShowFlag()
				};
		return update(sql.toString(), params);
	}

	@Override
	public int delForum(String id, String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(" FORUM_DETAIL ");
		sql.append(" SET DELETE_FLAG = 1 ");
		sql.append(" WHERE ");
		sql.append(" FORUM_ID = ? ");
		sql.append(" AND OID_USER_ID = ? ");
		return update(sql.toString(), new Object[]{id, userId});
	}

	@Override
	public List<Row> selMainForum(UserForum userForum) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM FORUM_DETAIL ");
		sql.append(" WHERE UP_LEVEL=0 ");
		sql.append(" AND DELETE_FLAG=0 ");
		sql.append(" ORDER BY FORUM_ID DESC");
		return queryForList(sql.toString(), userForum.getPageIndex(), userForum.getPageSize(), params.toArray());
	}

	@Override
	public List<Row> selSonComment(String level) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM FORUM_DETAIL ");
		sql.append(" WHERE UP_LEVEL=? ");
		sql.append(" AND DELETE_FLAG=0 ");
		return queryForList(sql.toString(), new Object[]{level});
	}
	@Override
	public int countForum() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(1) FROM FORUM_DETAIL ");
		sql.append(" WHERE UP_LEVEL=0 ");
		sql.append(" AND DELETE_FLAG = 0 ");
		return getCount(sql.toString(), new Object[]{});
	}
	@Override
	public List<Row> selFloorNumber(Map strMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("SELECT * FROM FORUM_DETAIL ");
		sql.append(" WHERE FORUM_ID = ? ");
		params.add(strMap.get("id"));
		if(strMap.get("upLevel")!=null){
			sql.append(" AND UP_LEVEL = ? ");
			params.add(strMap.get("upLevel"));
		}
		sql.append(" AND DELETE_FLAG=0 ");
		return queryForList(sql.toString(), params.toArray());
	}
}
