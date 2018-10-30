package com.zhongchou.common.service;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.InvestorDto;
import com.zhongchou.common.dto.OrderInfoDto;
import com.zhongchou.common.dto.ProgressDto;
import com.zhongchou.common.dto.ProjectInfoDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.dto.UserForum;
import com.zhongchou.common.dto.UserTenderDto;

/**
 * 用户中心-订单管理
 */
public interface IPCUserTenderService {

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的数量()
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * @return   list的总数   listCnt
	 */
	 int getUserTenderCnt(Map tenderMap);

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的list数组数据
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * 每页面的条数  pageSize
	 * 当前页数          curPage
	 * @return
	 */
	 List<UserTenderDto> getUserTenders(Map tenderMap);

	//获取订单详情
	 Map<String,Object> getOrderdetailInfo(Map tenderMap);
	//删除关闭的订单
	 boolean delCloseOrder(String tenderId,String oidUserId);
	//取消的订单
	public boolean updCloseOrder(String tenderId,String oidUserId);
}
