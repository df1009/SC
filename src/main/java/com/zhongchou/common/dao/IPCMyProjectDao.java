package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.SendEmailDto;

/**
 * 个人中心接口。
 */
public interface IPCMyProjectDao {

	/**
	 * 获取我的项目数据条数
	 */
	public int getMyProjectCnt(String oidUserId,String projectState);

	/**
	 * 获取我的项目数据
	 */
	List<Row> getMyProjectList(String projectState,String oidUserId, int pageSize,int curPage);

	/**
	 * 获取我的项目详细信息
	 */
	Row getMyProjectSupDetail(Map selMap);
	/**
	 * 获取我的项目详细信息
	 */
	Row getMyProjectOnlineDetail(Map selMap);
	/**
	 * 获取我的项目模块信息
	 */
	List<Row> selProductModularDetailList(Map selMap);
	
	/**
	 * 添加项目介绍信息
	 */
	int saveMyProjectInfo(Map selMap);
	/**
	 * 添加项目介绍信息
	 */
	int saveInsMyProjectInfo(Map selMap);
	
	/**
	 * 保存模块和进展时更改项目状态为草稿
	 */
	int updateDesSupSta(Map selMap);
	
	/**
	 * 修改草稿,审核中,审核通过的项目模块 查询sup中是否存在
	 * 
	 */
	Row selMyProjectModularToId(Map selMap);
	/**
	 * 修改项目模块信息时复制online表
	 */
	int updSaveMyProjectModularCopy(Map selMap);
	/**
	 * 修改项目模块信息
	 */
	int updSaveMyProjectModular(Map selMap);
	/**
	 * 新增项目模块信息(保存草稿或提交审核)
	 */
	int AddSaveMyProjectModular(Map selMap);
	
	/**
	 * 获取我的项目项目审核通过的进展信息
	 */
	List<Row> selProductProgressOnlineList(Map selMap);
	
	/**
	 * 获取我的项目项目草稿，审核中，驳回的的进展信息
	 */
	List<Row> selProductProgressSupList(Map selMap);
	 
	/**
	 * 项目介绍信息提交审核
	 */
	int submitProjectExaminerDes(Map selMap);
	
	/**
	 * 项目模块信息和项目进展提交审核
	 */
	int submitProjectExaminerDetail(Map selMap);
	//删除项目模块
	public int deleteProjectInformationSup(Map reqMap);
	public int deleteProjectInformationOnline(Map reqMap);
	/**
	 * 获取审核驳回原因
	 */
	List<Row> getVerifyList(String projectCode,String oidUserId);
	//查询用户是否有我的项目权限
	int getMyProject(String loginId);
	//查询产品的在detail中的数据
    Row selProjectToDetail(String proId);
    //查询产品的在detail中的数据MyProjectModularSort
    Row selMyProjectModularSort(String projectCode,String oidUserId);
    //设置模块序号
    int setMyProjectModularSortSup(Map reqMap);
    int setMyProjectModularSortOnline(Map reqMap);
}
