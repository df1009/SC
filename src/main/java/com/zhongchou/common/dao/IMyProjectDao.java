package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.SendEmailDto;

/**
 * 个人中心接口。
 */
public interface IMyProjectDao {

	/**
	 * 获取我的项目数据条数
	 */
	public int getMyProjectCnt(String oidUserId,String projectState);

	/**
	 * 获取我的项目数据
	 */
	List<Row> getMyProjectList(String projectState,String oidUserId, int pageSize,int curPage);
	/**
	 *获取App我的项目列表
	 * @param projectState
	 * @param oidUserId
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	List<Row> getMyAppProjectList(String oidUserId, int pageSize,int curPage);

	/**
	 * 获取行业名
	 */
	List<Row> getIndustry();

	/**
	 * 获取审核信息
	 */
	List<Row> getVerifyList(String projectCode,String oidUserId);


	/**
	 *插入项目信息Des表
	 */
	int insertProjectInformationDes(Map params);

	int updateProjectInformationDes(Map introductionMap);

	/**
	 *插入项目信息detail表
	 */
	int insertProjectInformationDet(Map params);

	/**
	 *插入项目进展信息
	 */
	int insertProjectProgress(List<Object> params);
	/**
	 *修改项目进展信息
	 */
	 int updProjectProgress(List<Object> params);

	/**
	 *提交审核
	 */
	int updateDes(String projectCode);

	/**
	 *提交审核
	 */
	int updateDet(String projectCode,String oidUserId);

	/**
	 *删除项目信息
	 */
	int deleteProjectInformation(String projectCode,String partyNum,String oidUserId,String id);

	/**
	 *删除项目进展
	 */
	int deleteProjectProgress(String projectCode,String id,String oidUserId);

	/**
	 *  获取用户信息
	 * @param string
	 * @return
	 */
	public Row getUserInfo(String loginId);
	/**
	 * 查询用户邮箱
	 * @param loginId
	 * @return
	 */
	public Row queryUserEmail(String loginId);
	/**
	 * 绑定用户邮箱
	 * @param loginId
	 * @param userEmail
	 * @return
	 */
	public int bindUserEmail(String loginId, String userEmail);
	/**
	 * 发送邮件记录
	 */
	public void recordEmailLog(SendEmailDto emailDto);

	/**
	 * 获取冻结状态
	 */
	List<Row> getCheckFrzz(String projectId);
	/**
	 * 添加用户头像
	 */
	public void updateHeadImage(String loginId, String newPath);

	//查询项目是不是本人发布的项目
	public Row selProjectAscription(String proId,String userId);

	//查询项目介绍信息
	public Row getprojectIntroduce(String proId,String userId);

	//修改项目信息
	int updateProjectDetal(Map proMap);

	/**
	 * 查询公共模块
	 */
	List<Row> selIntroductionTemplate();

	/**
	 * 查询单个公共模块
	 */
	Row selSingleIntroductionTemplate(String id);

}
