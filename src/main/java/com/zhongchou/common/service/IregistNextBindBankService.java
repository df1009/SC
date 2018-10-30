package com.zhongchou.common.service;

import java.util.Map;

public interface IregistNextBindBankService {
	/**
	 * 银行签约短信39
	 */
	Map<String,Object> getBankSms(Map<String,Object> parameter);

	/**
	 * 开通理财账户(绑卡)7
	 */
	Map<String, Object> getTiedBankCard(Map<String,Object> parameter);
}
