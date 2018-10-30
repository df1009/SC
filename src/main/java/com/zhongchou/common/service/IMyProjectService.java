package com.zhongchou.common.service;


import java.util.List;
import java.util.Map;

import com.zhongchou.common.dto.MyAppProjectlDto;
import com.zhongchou.common.dto.MyProjectlDto;
import com.zhongchou.common.dto.SendEmailDto;

/**
 * 个人中心逻辑的接口。
 */
public interface IMyProjectService {


	/**
	 * 我的项目数据条数
	 */
	public int getMyProjectCnt(String oidUserId,String projectState);

	/**
	 * 我的项目数据获取
	 */
	List<MyProjectlDto> getMyProjectList(String projectState,String oidUserId, int pageSize,int curPage);

	/**
	 * 获取app我的项目列表
	 * @param oidUserId
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	List<MyAppProjectlDto> getMyAppProjectList(String oidUserId, int pageSize,int curPage);

	/**
	 * 获取行业信息
	 */
	List<String> getIndustry();

	/**
	 * 我的审核内容
	 */
	List<Map> getVerifyList(String projectCode,String oidUserId);

	/**
	 * 插入项目信息
	 *
	 */
	String insertProjectInformation(Map proMap);


	/**
	 * 插入项目进展信息
	 */
	int insertProjectProgress(List<Object> params);
	/**
	 * 修改项目进展信息
	 */
	int updProjectProgress(List<Object> params);

	/**
	 * 提交审核
	 */
	int submitProject(String projectCode,String oidUserId);

	/**
	 * 删除项目信息
	 */
	int deleteProjectInformation(String projectCode,String partyNum,String oidUserId,String id);

	/**
	 * 删除项目进展
	 */
	int deleteProjectProgress(String projectCode,String id,String oidUserId);


	/**
	 * 银行卡管理添加银行卡获取验证码
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> checkBankCardAvalid(Map<String, Object> paramMap);
	/**
	 * 银行卡管理添加银行卡提交
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> bankCardMananeSubmit(Map<String, Object> paramMap);
	/**
	 * 银行卡管理取消非主银行卡
	 * @param paramMap
	 * @return
	 */

	public Map<String, Object> bankCardCancel(Map<String, Object> paramMap);
	/**
	 * 银行卡管理查询用户所有银行卡
	 * @param loginId
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> queryBankCard(String loginId);
	/**
	 * 绑定用户邮箱
	 * @param loginId
	 * @param userEmail
	 * @return
	 */
	public Map<String, Object> bindUserEmail(String loginId, String userEmail);
	/**
	 * 查询用户是否绑定邮箱
	 * @param loginId
	 * @return
	 */
	public Map<String, Object> checkEmailExist(String loginId);

	/**
	 * 记录发送邮件的log
	 * @param emailDto
	 */
	public void recordEmailLog(SendEmailDto emailDto);
	/**
	 * 更新用户头像
	 * @param loginId
	 * @param newPath
	 */
	public void updateHeadImage(String loginId, String newPath);

	//查询项目是否是本人发布的项目
	public Map selProjectAscription(String proId,String userId);

	//添加项目介绍
	boolean insertProjectIntroduction(Map introductionMap);

	/**
	 * 查询公共模块
	 * @param loginId
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> selIntroductionTemplate();

	/**
	 * 查询单个公共模块
	 * @param loginId
	 * @param paramMap
	 * @return
	 */
	public Map selSingleIntroductionTemplate(String id);

}
