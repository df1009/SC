package com.zhongchou.common.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanshang.util.Row;
import com.zhongchou.common.dao.UserForumDao;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.dto.UserForum;
import com.zhongchou.common.service.UserForumService;

/**
 * 登录业务逻辑的接口。
 */
@Service
public class UserForumServiceImpl implements UserForumService{
	Logger logger=Logger.getLogger(UserForumServiceImpl.class);
	@Autowired
	private UserForumDao userForumDao;

	@Override
	public int insertForum(UserForum userForum) {
		logger.info("UserForumServiceImpl.insertForum  start");
		String level = String.valueOf(userForumDao.selFloorCount(userForum.getOidFinanceProjectsId(), userForum.getUpLevel())+1);
		userForum.setLevel(level);//当前楼层
		logger.info("oidUserId:"+userForum.getOidUserId()+"  level:"+level);
		List<Row> mainForum = null;
		//有上级评论
		if(!"0".equals(userForum.getUpLevel())){
			Map strMap = new HashMap();
			strMap.put("upLevel", "0");
			strMap.put("id", userForum.getUpLevel());
			mainForum = userForumDao.selFloorNumber(strMap);
		}
		if(mainForum!=null){
			if(mainForum.size()!=1){
				logger.info("oidUserId:"+userForum.getOidUserId()+"  mainForum.size:"+mainForum.size());
				return 0;
			}else{
				for (Row row : mainForum) {
					userForum.setUpLevel((String)row.get("LEVEL"));//上级评论楼层
				}
			}
		}
		logger.info("UserForumServiceImpl.insertForum  end");
		return userForumDao.insertForum(userForum);
	}

	@Override
	public int delForum(String id, String userId) {
		// TODO Auto-generated method stub
		return userForumDao.delForum(id,userId);
	}

	@Override
	public List<UserForum> selMainForum(UserForum userForum,UserDto user) {
		logger.info("UserForumServiceImpl.selMainForum  start");
		//查询项目发起人id
		String prodectUserId = "20625";
		logger.info("prodectId:"+userForum.getOidFinanceProjectsId()+"   prodectUserId:"+prodectUserId);
		// TODO Auto-generated method stub
		List<UserForum> returnForumList = new ArrayList<>();
		List<UserForum> sonForumList = null;
		//查询一级评论
		List<Row> mainForum = userForumDao.selMainForum(userForum);
		for (Row row : mainForum) {
			//初始化子评论list
			sonForumList = new ArrayList<>();
			if (!row.isEmpty()) {
				String answerName = (String)row.get("ANSWER_NICKNAME");
				String answerId = (String)row.get("ANSWER_OID_USER_ID");
				UserForum forum = new UserForum();
				forum.setForumId(String.valueOf(row.get("FORUM_ID")));
				forum.setOidFinanceProjectsId((String)row.get("OID_FINANCE_PROJECTS_ID"));
				forum.setOidUserId((String)row.get("OID_USER_ID"));
				forum.setNickName((String)row.get("NICKNAME"));
				forum.setUserIconFileId((String)row.get("USER_ICON_FILE_ID")==null?"1":(String)row.get("USER_ICON_FILE_ID"));
				forum.setAnswerNickname(answerName);
				forum.setAnswerOidUserId(answerId);
				String showFlog = String.valueOf(row.get("SHOW_FLAG"));
				String content= (String)row.get("FORUM_NEWS");
				//未登录
				if(user==null){
					if("1".equals(showFlog)){
						content="*系统提示：内容涉及个人隐私资料，仅发起人可见*";
					}
				}else{
					//登录
					if("1".equals(showFlog)){
						if(!(user.getOidUserId().equals(prodectUserId)
								||user.getOidUserId().equals(answerId))){
							content="*系统提示：内容涉及个人隐私资料，仅发起人可见*";
						}
					}
				}
				forum.setForumNews(content);
				forum.setLevel((String)row.get("LEVEL"));
				forum.setUpLevel((String)row.get("UP_LEVEL"));
				forum.setInsDate((Date)row.get("INS_DATE"));
				//增加子评论
				forum.setChildForumList(sonForumList);
				returnForumList.add(forum);
				//当前一级评论的子评论
				List<Row> selSonCommentList =userForumDao.selSonComment(forum.getLevel());
				for (Row sonRow : selSonCommentList) {
					if (!sonRow.isEmpty()) {
						String sonAnswerName = (String)sonRow.get("ANSWER_NICKNAME");
						String sonAnswerId = (String)sonRow.get("ANSWER_OID_USER_ID");
						UserForum sonForum = new UserForum();
						sonForum.setForumId(String.valueOf(sonRow.get("FORUM_ID")));
						sonForum.setOidFinanceProjectsId((String)sonRow.get("OID_FINANCE_PROJECTS_ID"));
						sonForum.setOidUserId((String)sonRow.get("OID_USER_ID"));
						sonForum.setNickName((String)sonRow.get("NICKNAME"));
						sonForum.setUserIconFileId((String)sonRow.get("USER_ICON_FILE_ID")==null?"1":(String)row.get("USER_ICON_FILE_ID"));
						sonForum.setAnswerNickname(sonAnswerName);
						sonForum.setAnswerOidUserId(sonAnswerId);
						String sonShowFlog = String.valueOf(sonRow.get("SHOW_FLAG"));
						String sonContent = (String)sonRow.get("FORUM_NEWS");
						//未登录
						if(user==null){
							if("1".equals(sonShowFlog)){
								sonContent="*系统提示：内容涉及个人隐私资料，仅发起人可见*";
							}
						}else{
							//登录
							if("1".equals(sonShowFlog)){
								if(!(user.getOidUserId().equals(prodectUserId)
										||user.getOidUserId().equals(answerId))){
									sonContent="*系统提示：内容涉及个人隐私资料，仅发起人可见*";
								}
							}
						}
						sonForum.setForumNews(sonContent);
						sonForum.setLevel((String)sonRow.get("LEVEL"));
						sonForum.setUpLevel((String)sonRow.get("UP_LEVEL"));
						sonForum.setInsDate((Date)sonRow.get("INS_DATE"));
						sonForumList.add(sonForum);
					}
				}

			}

		}
		logger.info("UserForumServiceImpl.insertForum  end");
		return returnForumList;
	}

	@Override
	public int countForum() {
		Map countMap = new HashMap();
		return userForumDao.countForum();
	}

/*	public void setComment(){

	}*/
}
