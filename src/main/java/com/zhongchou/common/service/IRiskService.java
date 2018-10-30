package com.zhongchou.common.service;

import java.util.Map;

public interface IRiskService {
	/**
	 * 取得题库数据
	 */
	Map<String, Object> getQuestions();

	/**
	 * 风险测评数据提交
	 */
	Map<String, Object> riskSubmit(Map<String,String> params);

	Map<String, Object> newRiskSubmit(Map<String, String> params);

}
