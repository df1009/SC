package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.UserTender;

/**
 * 用户中心-订单管理
 */
public interface IPCUserTenderDao {

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的数量()
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * @return list的总数   listCnt
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
	List<Row> getUserTenders(Map tenderMap);
	/**
	 *查询订单项目详情
	 */
	Row getOrderProductInfo(Map tenderMap);
	//获取退款信息
	Row getReturnAmt(String tenderId,String oidUserId);
	//删除关闭的订单
	int delCloseOrder(String tenderId,String oidUserId);
	//取消的订单
	int updCloseOrder(String tenderId,String oidUserId);
}
