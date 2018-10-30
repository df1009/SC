package com.zhongchou.common.service;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;




/**
 * 评论业务逻辑的接口。
 */
public interface INoticeService {
	/**
	 *获取首页滚动公告
	 */
	List<Row> getHomePageNotice();

	/**
	 *获取列表
	 */
	List<Row> getNoticeList(String type,int pageIndex,int pageSize);

	/**
	 *获取详情
	 */
	Map getNoticeDetail(String noticeId,String type,int pageIndex,int pageSize);

	/**
	 *获取详情用于上一页下一页
	 */
	Map getNoticeDetail(String noticeId,String type);
	/**
	 *获取APP详情
	 */
	Map getAppNoticeDetail(String noticeId);
	/**
	 *获取公告总数
	 */
	int getNoticeCount(String type);
}
