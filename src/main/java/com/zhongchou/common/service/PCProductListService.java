package com.zhongchou.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.PcGoodProductListDto;
import com.zhongchou.common.dto.PcProductListDto;

public interface PCProductListService {


	List<PcProductListDto> getProjectList(int pageSize,int curPage);
/**
 * 分页条件查询
 * @param selMap
 * @return
 */
	 List<PcProductListDto> selPcProductList(Map selMap);

	/**
	 *分页多条件查询精选产品列表
	 */
	List<PcGoodProductListDto> selPcGoodProductList(Map selMap);
	/**
	 * 查询总记录数
	 * @param selMap
	 * @return
	 */
	 int countPcProduct(Map selMap);
	 /**
	  * 查询精选展示总记录数
	  * @param selMap
	  * @return
	  */
	 int countPcGoodProduct(Map selMap);
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
