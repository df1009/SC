package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.UserTender;

/**
 * 用户中心-订单管理
 */
public interface IUserTenderDao {

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的数量()
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * @return list的总数   listCnt
	 */
	public int getUserTenderCnt(Map tenderMap);

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
	public List<Row> getUserTenders(Map tenderMap);


	/**
	 * 获取撤单状态
	 */
	 Row getReturnState(String tenderId,String oidUserId);


	 /**
	 * 获取撤单状态
	 */
	 Row getReturnAllState(String tenderId,String oidUserId);
	/**
	 * 我的中心  获取订单数据
	 */
	 Row getOrderInfo(String orderNum);
	/**
	 * 我的中心  获取跟投人list
	 */
	 List<Row> getInvestorList(String projectsId);

	/**
	 * 查询单个项目详细信息
	 */
	 Row selProductDetaile(String productId);

	 /**
	 *查询单个项目的项目介绍信息
	 */
	List<Row> selProductIntroduce(String productId,String type);

	/**
	 *查询单个项目投资人
	 */
	List<Row> selProductInvestor(String productId);
	 //增加用户的购买记录
	 int saveUserTender(UserTender userTender,String bankId);
	 /**
	 * 验证投标表主键在投标表里是否存在的方法。
	 * @param oidTenderId
	 * @return
	 */
	 boolean checkTenderIdExists(String oidTenderId);
	/**
	 *查询项目投资基本信息
	 */
	Row getProductinfo(String productId,String loginId);

	/**
	 * 查询项目信息
	 */
	Row getUserTender(String tenderId,String userId);

	/**
	 * 插入记录表
	 */
	int insOrderCancellationRecord(Map<String,Object> param);
	//查询撤单信息
	Row getOrderCancellationRecord(String reqSsn,String userId);
	//已有撤单信息，更新时间
	int updateOrderCancellationRecord(Map<String,Object> param);
	//查询用户是否投资过该产品
	int selUserTenderPro(String productId,String loginId);
	/**
	 * 插入退款记录
	 */
	int insRefund(Map<String,Object> param);
	/**
	 * 查询用户投资总金额
	 */
	int selAllTenderAmount(String userId);
	/**
	 * 查询用户投资项目总数
	 */
	int selTenderCount(String userId);

	/**
	 * 模糊查询用户投资信息
	 */
	List<Row> likeTender(String userId,String proName);
	/**
	 * 查询用户投资过产品
	 */
	public int countUserTenderAmount(String userId, String proName);
	/**
	 * 查询用户当日投资金额
	 */
	public int countUserTenderAmountToday(String userId);
	/**
	 * 订单信息
	 * @param paramMap
	 * @return
	 */
	Row getWaitTenderInfo(Map<String, String> paramMap);

	/**
	 * 购买成功更新tender表
	 * @param userTender
	 * @param bankId
	 */
	void updUserTender(UserTender userTender, String bankId);
}
