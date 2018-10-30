package com.zhongchou.common.service;

import java.util.List;
import java.util.Map;

import com.zhongchou.common.dto.UserTender;

public interface IBuyProductService {
	/**
	 * 记录用户同意风险提示
	 */
	boolean saveConfirmRiskWarning(String userId);

	/**
	 * 购买
	 */
	Map buyProduct(Map buyMap);

	/**
	 * 用户创建订单
	 * @param userTender
	 * @return
	 */
    Map<String,Object> createOrder(UserTender userTender);

	/**
	 * 查询用户订单待支付信息
	 * @param paramMap
	 * @return
	 */
	Map<String,Object> getWaitOrderInfo(Map<String, String> paramMap);

	/**
	 * 确认支付
	 * @param buyMap
	 * @return
	 */
	Map payOrderConfirm(Map buyMap);

}
