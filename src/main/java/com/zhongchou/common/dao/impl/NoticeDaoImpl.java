package com.zhongchou.common.dao.impl;

import java.util.List;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.NoticeDao;


/**
 * 登录业务数据操作的接口。
 */
public class NoticeDaoImpl extends BaseDao implements NoticeDao{
	/**
	 *获取首页滚动公告
	 */
	@Override
	public List<Row> getHomePageNotice() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM notice ");
		sql.append(" WHERE NOW() BETWEEN SHOW_TIME_START AND SHOW_TIME_END ");
		sql.append(" AND PUBLISH_TIME <= NOW() ");
		sql.append(" AND DEL_FLG = '0' ");
		sql.append(" AND VALID_FLG = '1' ");
		return queryForList(sql.toString(), new Object[]{ });
	}

	/**
	 *获取公告列表
	 *pageIndex 查询起始位置
	 *pageSize  查询数量
	 */
	@Override
	public List<Row> getNoticeList(String type,int pageIndex,int pageSize) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM NOTICE ");
		sql.append(" WHERE DEL_FLG = 0 ");
		sql.append(" AND VALID_FLG = 1 ");
		sql.append(" AND PUBLISH_TIME <= NOW() ");
		sql.append(" AND NEWS_TYPE = ? ");
		sql.append(" ORDER BY PUBLISH_TIME DESC ");
		return queryForList(sql.toString(),pageIndex,pageSize, new Object[]{type });
	}

	@Override
	public List<Row> getNoticeList(String type) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM NOTICE ");
		sql.append(" WHERE DEL_FLG = 0 ");
		sql.append(" AND VALID_FLG = 1 ");
		sql.append(" AND PUBLISH_TIME <= NOW() ");
		sql.append(" AND NEWS_TYPE = ? ");
		sql.append(" ORDER BY PUBLISH_TIME DESC ");
		return queryForList(sql.toString(),new Object[]{type });
	}



	/**
	 *获取公告详情
	 */
	@Override
	public Row getNoticeDetail(String noticeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM notice ");
		sql.append(" WHERE NOTICE_ID = ? ");
		sql.append(" AND DEL_FLG = 0 ");
		sql.append(" AND VALID_FLG = 1 ");
		return singleQuery(sql.toString(), new Object[]{noticeId });
	}

	/**
	 *获取公告总数
	 */
	@Override
	public int getNoticeCount(String type) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(1) FROM notice ");
		sql.append(" WHERE DEL_FLG = 0 ");
		sql.append(" AND VALID_FLG = 1 ");
		sql.append(" AND PUBLISH_TIME <= NOW() ");
		sql.append(" AND NEWS_TYPE = ? ");
		return getCount(sql.toString(), new Object[]{ type});
	}
}
