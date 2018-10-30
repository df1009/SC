package com.zhongchou.common.service;


import java.util.List;
import java.util.Map;

import com.zhongchou.common.dto.MyProjectlDto;
import com.zhongchou.common.dto.SendEmailDto;

/**
 * 个人中心逻辑的接口。
 */
public interface IPCMyProjectService {


	/**
	 * 我的项目数据条数
	 */
	public int getMyProjectCnt(String oidUserId,String projectState);

	/**
	 * 我的项目数据获取
	 */
	List getMyProjectList(String projectState,String oidUserId, int pageSize,int curPage);
	/**
	 * 查询我的产品详细内容
	 * 用户的IOD  oidUserId
	 * 产品的code      projectCode
	 * @return   
	 */
	Map selProductDetail(Map selMap);
	/**
	 * 查询我的产品模块信息
	 * 用户的IOD  oidUserId
	 * 产品的code      projectCode
	 * @return   
	 */
	List selProductModularDetailList(Map selMap);
	/**
	 * 添加项目介绍信息
	 */
	int saveMyProjectInfo(Map selMap);
	
	/**
	 * 插入项目模块信息
	 * 
	 */
	Map insertProjectInformation(Map proMap);
	/**
	 * 查询我的项目的项目进展
	 */
	Map selMyProjectProgress(Map paramsMap);
	/**
	 * 我的项目草稿状态提交审核
	 */
	boolean submitMyProject(Map paramsMap);
	/**
	 * 删除项目信息
	 */
	boolean deleteProjectInformation(Map reqMap);

	/**
	 * 项目驳回原因
	 */
	List<Map> getVerifyList(String projectCode,String oidUserId);
	//查询用户是否有我的项目权限
	int getMyProject(String loginId);
	//设置模块序号
    public boolean setMyProjectModularSort(Map selMap);
}
