package com.zhongchou.common.dao;

import com.yanshang.util.Row;

import java.util.List;

/**
 * @author gaoming
 * @create 2017-09-05-17:42
 **/
public interface PcProductDao {
    /**
     *查询单个项目详细信息
     */
    Row selProductDetaile(String productId);

    /**
     *查询单个项目的项目介绍信息
     */
    List<Row> selProductIntroduce(String productId, String type);

    /**
     * 展示其它项目信息列表
     * @param productId
     * @return
     */
    List<Row> getOtherProjectList(String productId);
    /**
     *查询单个项目投资人
     */
    List<Row> selProductInvestor(String productId,int sumNum,int pageSize);
    /**
     *查询单个项目投资数量
     */
    int countProductInvestor(String productId);
    //查询用户是否投资过该产品
    int selUserTenderPro(String productId,String loginId);
    //查询产品购买状态
    Row selProjectToBuy(String proId);
    //根据项目code查询项目名
    Row selProductName(String productId);
}
