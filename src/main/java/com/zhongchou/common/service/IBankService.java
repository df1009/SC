package com.zhongchou.common.service;

import java.util.List;
import java.util.Map;

public interface IBankService {
	/**
	 * 银行签约短信39
	 */
	Map<String,Object> getBankSms(Map<String,Object> parameter);

	/**
	 * 查询用户绑卡信息
	 */
	Map selBankCardList(String userId);

	/**
	 * 查询银行卡代扣限额
	 */
	List selBankQuota(String bankCd);
	/**
	 * 查询银行卡中证是否支持
	 */
	Map selSupportBank(String bankId);

	//壹理财老用户注册第二部返显用户绑卡信息
	Map selBankSmg(String userId);

	/**
	 * 查询用户在中证的绑卡信息
	 */
	Map<String,Object> queryTiedBankCard(Map<String,Object> parameter);
	////查询中证支持的银行
	List selSupportBankList();
	//查询用户银行卡绑定的手机号
	Map selBankMap(String oidUserId);
}
