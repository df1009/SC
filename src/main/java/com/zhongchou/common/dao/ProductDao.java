package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;

public interface ProductDao{
	/**
	 *加载产品列表
	 */
	List<Row> selProductFirstList();

	/**
	 * 查询产品总数
	 *
	 */
	int countProduct(Map selMap);

	/**
	 *查询行业类别
	 */
	List<Row> selindustryType();

	/**
	 *分页多条件查询产品列表
	 */
	List<Row> selProductList(Map selMap);

	/**
	 *查询单个项目的项目介绍信息
	 */
	List<Row> selProductIntroduce(String productId,String type);

	/**
	 *查询单个项目详细信息
	 */
	Row selProductDetaile(String productId);

	/**
	 *查询单个项目简略信息
	 */
	Row selProductBrief(String productId);

	/**
	 *查询单个项目投资人
	 */
	List<Row> selProductInvestor(String productId,int sumNum,int pageSize);

	/**
	 *查询单个项目投资数量
	 */
	int countProductInvestor(String productId);

	/**
	 *查询否符投资人风险等级的项目
	 */
	List<Row> userRiskLvlAccordPro(String userId);

	/**
	 * 获取项目方法
	 *
	 * @return 项目数据
	 */
	List<Row> getProjectList();

	/**
	 * 查询用户邀请认的投资记录
	 *
	 */
	List<Row> selIntroducerInvest(String uId);
	/**
	 * 查询产品购买状态
	 *
	 */
	Row selPurchaseStatus(String proId);

	/**
	 * 购买时更新产品
	 *
	 */
	int updateProductToBuy(String quota,String proId);

	//查询产品购买状态
	Row selProjectToBuy(String proId);
	//我要融资
	int addFinancing(Map financingMap);
}
