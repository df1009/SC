package com.zhongchou.common.dto;

import java.util.Date;
import java.util.List;

import com.zhongchou.common.model.SearchConditionBase;


public class UserForum extends SearchConditionBase {

	/** 评论ID */
	private String forumId;
	/** 融资项目ID */
	private String oidFinanceProjectsId;
	/** 用户OID */
	private String oidUserId;
	/** 用户昵称  */
	private String nickName;
	/** 回复用户昵称  */
	private String answerNickname;
	/** 回复用户昵称  */
	private String answerOidUserId;
	/** 用户头像id  */
	private String userIconFileId;
	/** 评论内容  */
	private String forumNews;
	/** 楼层编号  */
	private String level;
	/** 上级楼层编号 */
	private String upLevel;
	/** 生成日期 */
	private Date insDate;
	/** 生成者IP */
	private String insIp;
	/** 删除标识 0:有效  1：删除 */
	private String deleteFlag;
	//显示标识0：显示；1:隐藏
	private String showFlag;
	/** 子评论集合*/
	List<UserForum> childForumList;


	public String getForumId() {
		return forumId;
	}
	public void setForumId(String forumId) {
		this.forumId = forumId;
	}
	public String getOidFinanceProjectsId() {
		return oidFinanceProjectsId;
	}
	public void setOidFinanceProjectsId(String oidFinanceProjectsId) {
		this.oidFinanceProjectsId = oidFinanceProjectsId;
	}
	public String getOidUserId() {
		return oidUserId;
	}
	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAnswerNickname() {
		return answerNickname;
	}
	public void setAnswerNickname(String answerNickname) {
		this.answerNickname = answerNickname;
	}

	public String getAnswerOidUserId() {
		return answerOidUserId;
	}
	public void setAnswerOidUserId(String answerOidUserId) {
		this.answerOidUserId = answerOidUserId;
	}
	public String getForumNews() {
		return forumNews;
	}
	public void setForumNews(String forumNews) {
		this.forumNews = forumNews;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getUpLevel() {
		return upLevel;
	}
	public void setUpLevel(String upLevel) {
		this.upLevel = upLevel;
	}

	public Date getInsDate() {
		return insDate;
	}
	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}
	public String getInsIp() {
		return insIp;
	}
	public void setInsIp(String insIp) {
		this.insIp = insIp;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getUserIconFileId() {
		return userIconFileId;
	}
	public void setUserIconFileId(String userIconFileId) {
		this.userIconFileId = userIconFileId;
	}

	public String getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}
	public List<UserForum> getChildForumList() {
		return childForumList;
	}
	public void setChildForumList(List<UserForum> childForumList) {
		this.childForumList = childForumList;
	}

}
