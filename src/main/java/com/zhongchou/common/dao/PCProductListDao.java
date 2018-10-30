package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;

public interface PCProductListDao{
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
	int countPcProduct(Map selMap);

	/**
	 * 查询精选展示产品总数
	 *
	 */
	int countPcGoodProduct(Map selMap);
	/**
	 *分页多条件查询产品列表
	 */
	List<Row> selPcProductList(Map selMap);
	/**
	 *分页多条件查询精选产品列表
	 */
	List<Row> selPcGoodProductList(Map selMap);
	/**
	 *查询行业类别
	 */
	List<Row> selindustryType();
	/**
	 * 获取banner list方法
	 *
	 * @return banner数据
	 */
	List<Row> getBannerList(String type);

}
