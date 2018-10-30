package com.zhongchou.common.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.NoticeDao;
import com.zhongchou.common.service.INoticeService;
import com.zhongchou.common.util.StringUtil;

/**
 * 登录业务逻辑的接口。
 */
@Service
public class NoticeServiceImpl implements INoticeService{
	Logger logger=Logger.getLogger(NoticeServiceImpl.class);
	@Autowired
	private NoticeDao noticeDao;
	/**
	 *获取首页滚动公告
	 */
	public List<Row> getHomePageNotice(){
		List homeNoticeList = new ArrayList();
		List<Row> NoticeList = noticeDao.getHomePageNotice();
		for (Row row : NoticeList) {
			if (!row.isEmpty()) {
				Map noticeMap = new HashMap();
				noticeMap.put("noticeId", ConvUtils.convToString(row.get("NOTICE_ID")));
				noticeMap.put("noticeTitle", ConvUtils.convToString(row.get("TITLE")));
				noticeMap.put("noticeMsg", StringUtil.editorFormat(ConvUtils.convToString(row.get("CONTENT"))));//公告内容
				homeNoticeList.add(noticeMap);
			}
		}
		return homeNoticeList;
	}

	/**
	 *获取公告列表
	 */
	public List<Row> getNoticeList(String type,int pageIndex,int pageSize){
		List noticeList = new ArrayList();
		List<Row> Notice = noticeDao.getNoticeList(type,pageIndex,pageSize);
		for (Row row : Notice) {
			String noticeTime =  DateUtils.convertDate2String((Date)row.get("PUBLISH_TIME"),"yyyy-MM-dd");
			Map noticeMap = new HashMap();
			noticeMap.put("noticeId", ConvUtils.convToString(row.get("NOTICE_ID")));
			noticeMap.put("noticeTitle", ConvUtils.convToString(row.get("TITLE")));//标题
			noticeMap.put("noticeTime",noticeTime);//发布时间
			noticeMap.put("timeYear",noticeTime.substring(0, 7));//发布时间
			noticeMap.put("timeMoon",noticeTime.substring(8, 10));//发布时间
			noticeMap.put("thumbnail", ConvUtils.convToString(row.get("THUMBNAIL")));//缩略图
			noticeMap.put("newsOriginate", ConvUtils.convToString(row.get("NEWS_ORIGINATE")));//新闻来源
			noticeMap.put("noticeMsg", StringUtil.editorFormat(ConvUtils.convToString(row.get("CONTENT"))));//公告内容
			noticeList.add(noticeMap);
		}
		return noticeList;
	}

	/**
	 *获取公告总数
	 */
	public int getNoticeCount(String type){
		return noticeDao.getNoticeCount(type);
	}
	/**
	 *获取公告详情
	 */
	public Map getNoticeDetail(String noticeId,String type){
		Map noticeMap = new HashMap();
		String upNoticeId ="";//上一条数据id
		String downNoticeId="";//下一条数据id
		String upNoticeTitle="";
		String downNoticeTitle=	"";
		List<Row> Notice = noticeDao.getNoticeList(type);
		for (int i = 0; i < Notice.size(); i++) {
			Row row = Notice.get(i);
			if(noticeId.equals(ConvUtils.convToString(row.get("NOTICE_ID")))){//与当前ID对比获取角标位置
				//获取前一条数ID
				if(i>=1){
				upNoticeId = ConvUtils.convToString(Notice.get(i-1).get("NOTICE_ID"));
				upNoticeTitle = ConvUtils.convToString(Notice.get(i-1).get("TITLE"));
					}
				//获取后一条数据ID
				if(0<=i && i<Notice.size()-1){
					downNoticeId = ConvUtils.convToString(Notice.get(i+1).get("NOTICE_ID"));
					downNoticeTitle = ConvUtils.convToString(Notice.get(i+1).get("TITLE"));
						}
				if(i>=Notice.size()-1){
					upNoticeId = ConvUtils.convToString(Notice.get(i-1).get("NOTICE_ID"));
					upNoticeTitle = ConvUtils.convToString(Notice.get(i-1).get("TITLE"));
				}
				//当为上一页的最后一条数据时
				/*if(i==Notice.size()-1){
					pageIndex = pageIndex+pageSize;
					List<Row> NoticeEndPage = noticeDao.getNoticeList(type,pageIndex,pageSize);
					if(NoticeEndPage.size()!=0){
						downNoticeId = ConvUtils.convToString(NoticeEndPage.get(0).get("NOTICE_ID"));
						downNoticeTitle = ConvUtils.convToString(NoticeEndPage.get(0).get("TITLE"));
					}
				}*/
				//当为下一页第一条数据时
				/*if(i==0 && pageIndex/pageSize>1){
					pageIndex = pageIndex-pageSize;
					List<Row> NoticeBeginPage = noticeDao.getNoticeList(type,pageIndex,pageSize);
					if(NoticeBeginPage.size()!=0){
						upNoticeId =  ConvUtils.convToString(NoticeBeginPage.get(pageSize-1).get("NOTICE_ID"));
						upNoticeTitle = ConvUtils.convToString(NoticeBeginPage.get(pageSize-1).get("TITLE"));
					}
				}*/
			}
		}

		Map NoticeMap = noticeDao.getNoticeDetail(noticeId);
		noticeMap.put("upNoticeId",upNoticeId);
		noticeMap.put("downNoticeId",downNoticeId);
		noticeMap.put("upNoticeTitle",upNoticeTitle);
		noticeMap.put("downNoticeTitle",downNoticeTitle);
		noticeMap.put("noticeTitle", ConvUtils.convToString(NoticeMap.get("TITLE")));//标题
		noticeMap.put("noticeContent", ConvUtils.convToString(NoticeMap.get("CONTENT")));//内容
		noticeMap.put("noticeTime", DateUtils.convertDate2String((Date)NoticeMap.get("PUBLISH_TIME"),"yyyy年MM月dd日"));//日期
		noticeMap.put("thumbnail", ConvUtils.convToString(NoticeMap.get("THUMBNAIL")));//缩略图
		noticeMap.put("newsOriginate", ConvUtils.convToString(NoticeMap.get("NEWS_ORIGINATE")));//新闻来源
		return noticeMap;
	}

	/**
	 * 获取app公告详情
	 * @param noticeId
	 * @return
	 */
	public Map getAppNoticeDetail(String noticeId){
		Map noticeMap = new HashMap();
		Map NoticeMap = noticeDao.getNoticeDetail(noticeId);
		noticeMap.put("noticeTitle", ConvUtils.convToString(NoticeMap.get("TITLE")));//标题
		noticeMap.put("noticeContent", ConvUtils.convToString(NoticeMap.get("CONTENT")));//内容
		noticeMap.put("noticeTime", DateUtils.convertDate2String((Date)NoticeMap.get("PUBLISH_TIME"),"yyyy年MM月dd日"));//日期
		return noticeMap;
	}

	@Override
	public Map getNoticeDetail(String noticeId, String type, int pageIndex,
			int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
}
