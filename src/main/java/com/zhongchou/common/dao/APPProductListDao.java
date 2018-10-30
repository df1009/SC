package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;

public interface APPProductListDao{
/**
 * 首页列表展示
 * pageSize：页面条数
 * curPage：当前页数
 */
	List<Row> selProductList(int pageSize,int curPage);

	/**
	 * 查询产品总数
	 *
	 */
	int countAppProduct(Map selMap);

	/**
	 * 查询精选展示产品总数
	 *
	 */
	int countAppGoodProduct(Map selMap);
	/**
	 *分页多条件查询产品列表
	 */
	List<Row> selAppProductList(Map selMap);
	/**
	 *分页多条件查询精选产品列表
	 */
	List<Row> selAppGoodProductList(Map selMap);
	/**
	 *查询行业类别
	 */
	List<Row> selindustryType();
}
