package com.zhongchou.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.ProductDetailDto;
import com.zhongchou.common.dto.ProjectDto;

//产品的业务逻辑
public interface ProductService {


	/**
	 * 获取项目方法
	 *
	 * @return 精选项目的list
	 */
	List<ProjectDto> getProjectList();

	List<ProductDetailDto> selProductInitList();

	List<ProductDetailDto> selProductList(Map selMap);

	int countProduct(Map selMap);

	List<Set> seldustryType();

	List<Map> selAdjacentProductList(Map selMap);

	Map selProductDetailList(String productId,String type);

	List selProductInvestor(String productId,int sumNum,int pageSize);

	List<Map> userRiskLvlAccordPro(String userId);
	//查询邀请人投资记录
	List<Map> selIntroducerInvest(String userId);
	//查询项目投资人数量
	int countProductInvestor(String productId);
	//查询产品列表banner图
	List<BannerDto> getBannerList();
	//查询产品投资金额信息
	Map selprojectAmount(String productId,String userId);
	//查询项目风险等级及app认购页数据
	Map selRiskLvlToBuy(String productId);
	//我要融资
	int addFinancing(Map financingMap);
}
