package com.zhongchou.common.service;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;

/**
 * 1.3.0 产品service
 *
 * @author gaoming
 * @create 2017-09-05-17:35
 **/
public interface PcProductService {

    /**
     * /产品信息
     * @param productId
     * @return
     */
    Map getProductDetail(String productId);

    /**
     * 项目介绍
     * @param productId
     * @return
     */
    List<Map<String, String>> getProductIntroduce(String productId);

    /**
     * 融资信息
     * @param productId
     * @return
     */
    Map getProductTradingInfo(String productId);

    /**
     * 项目进展
     * @param productId
     * @return
     */
    List<Map<String, Object>> getProductProgress(String productId);

    /**
     * 展示其它项目信息列表
     * @param productId
     * @return
     */
    List getOtherProjectList(String productId);
    List selProductInvestor(String productId,int sumNum,int pageSize);
    //查询项目投资人数量
    int countProductInvestor(String productId);

    //查询产品投资金额信息
    Map selprojectAmount(String productId,String userId);
    //查询项目风险等级及app认购页数据
    Row selRiskLvlToBuy(String productId);
}
