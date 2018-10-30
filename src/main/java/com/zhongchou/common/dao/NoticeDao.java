package com.zhongchou.common.dao;

import java.util.List;

import com.yanshang.util.Row;

/**
 * 评论业务数据操作的接口。
 */
public interface NoticeDao {
	/**
	 *获取首页滚动公告
	 */
	List<Row> getHomePageNotice();

	/**
	 *获取公告列表
	 */
	List<Row> getNoticeList(String type,int pageIndex,int pageSize);

	/**
	 *获取公告列表用于下一条下一条数据
	 */
	List<Row> getNoticeList(String type);

	/**
	 *获取公告详情
	 */
	Row getNoticeDetail(String noticeId);
	/**
	 *获取公告总数
	 */
	int getNoticeCount(String type);
}
