package com.zhongchou.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zhongchou.common.dto.APPProductListDto;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.PcGoodProductListDto;
import com.zhongchou.common.dto.PcProductListDto;

public interface APPProductListService {


	List<APPProductListDto> getProjectList(int pageSize,int curPage);
	/**
	 * 分页条件查询
	 * @param selMap
	 * @return
	 */
	 List<APPProductListDto> selAppProductList(Map selMap);
	 /**
	 *分页多条件查询精选产品列表
	 */
	List<APPProductListDto> selAppGoodProductList(Map selMap);
	/**
	 * 查询总记录数
	 * @param selMap
	 * @return
	 */
	 int countAppProduct(Map selMap);
	 /**
	  * 查询精选展示总记录数
	  * @param selMap
	  * @return
	  */
	 int countAppGoodProduct(Map selMap);
	 /**
	  * 行业类别
	  * @return
	  */
	 Set seldustryType();

	 /**
	 * 查询产品列表banner图
	 */
	List<BannerDto> getBannerList();
}
