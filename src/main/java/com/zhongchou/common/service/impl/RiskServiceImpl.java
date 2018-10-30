package com.zhongchou.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IRiskService;
import com.zhongchou.common.zhongzhengSao.QueryRiskQuestionsSao;
import com.zhongchou.common.zhongzhengSao.SubmitRiskQuestionsSao;

@Service
@SuppressWarnings("unchecked")
public class RiskServiceImpl extends BaseSaoServiceImpl implements IRiskService {
	Logger logger=Logger.getLogger(RiskServiceImpl.class);

	/**
	 * 用户数据操作的接口。
	 */
	@Autowired
	private IUserDao userDao;

	public Map<String, Object> getQuestions() {

		// 返回的参数
		Map<String,Object> retMap = new HashMap<String,Object>();
		// 带入的参数
		Map<String,Object> setMap = new HashMap<String,Object>();

		String seq = nextSeq();
		QueryRiskQuestionsSao QueryRiskQuestionsSao = new QueryRiskQuestionsSao(seq);

		//调用接口，返回报文体
		logger.info("---------调用中证接口查询评测题库开始-------------");
		retMap = QueryRiskQuestionsSao.setEncryptData(setMap);
		logger.info("---------调用中证接口查询评测题库结束-------------"+(String)retMap.get("rsp_desc"));
		return retMap;
	}

	public Map<String, Object> riskSubmit(Map<String,String> params) {

		// 返回的参数
		Map<String,Object> retMap = new HashMap<String,Object>();
		//对用户选取的答案做格式出来
		String answer = params.get("answer");
		String[] sp = answer.split(";");
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<sp.length;i++){
			String rf = sp[i].replaceFirst(",", ":");
			if(i==sp.length-1){
				buffer.append(rf);
			}else{
				buffer.append(rf).append(";");
			}
		}
		answer = buffer.toString();
		params.put("answer", answer);


		//调用接口，返回报文体
		String seq = nextSeq();
		SubmitRiskQuestionsSao submitRiskQuestionsSao = new SubmitRiskQuestionsSao(seq);
		logger.info("---------调用中证接口提交评测结果开始-------------");
		retMap = submitRiskQuestionsSao.setEncryptData(params);
		logger.info("---------调用中证接口提交评测结果结束-------------"+(String)retMap.get("rsp_desc"));
		if("000000".equals(retMap.get("rsp_code").toString())){

			UserDto userDto = new UserDto();

			userDto.setOidUserId(params.get("loginId"));

			// 查询用户信息
			Row row = userDao.getExistUser(userDto);
			if(!row.isEmpty()){

				// 带入的参数
				Map<String,Object> parameter = new HashMap<String,Object>();
				parameter.put("raFlag", "1");
				parameter.put("loginId", params.get("loginId"));
				parameter.put("riskLevel", retMap.get("riskLevel"));
				parameter.put("investType", retMap.get("riskLevelDesc"));//风险描述

				// 更新user表的已经风险提测
				try {
					userDao.updRiskUser(parameter);
				} catch (Exception e) {
					logger.error("-----------风险评测更新user表异常----------");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					e.printStackTrace();
				}
				//更新userDetail表的风险评分
				try {
					userDao.updRiskUserDetail(parameter);
				} catch (Exception e) {
					logger.error("-----------风险评测更新user_detail表异常----------");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					e.printStackTrace();
				}
			}
		}
		return retMap;
	}

	@Override
	public Map<String, Object> newRiskSubmit(Map<String, String> params) {
		// 返回的参数
		Map<String,Object> retMap = new HashMap<String,Object>();
		//调用接口，返回报文体
		String seq = nextSeq();
		SubmitRiskQuestionsSao submitRiskQuestionsSao = new SubmitRiskQuestionsSao(seq);
		logger.info("---------调用中证接口提交评测结果开始-------------");
		retMap = submitRiskQuestionsSao.setEncryptData(params);
		logger.info("---------调用中证接口提交评测结果结束-------------"+(String)retMap.get("rsp_desc"));
		if("000000".equals(retMap.get("rsp_code").toString())){

			UserDto userDto = new UserDto();

			userDto.setOidUserId(params.get("loginId"));

			// 查询用户信息
			Row row = userDao.getExistUser(userDto);
			if(!row.isEmpty()){

				// 带入的参数
				Map<String,Object> parameter = new HashMap<String,Object>();
				parameter.put("raFlag", "1");
				parameter.put("loginId", params.get("loginId"));
				parameter.put("riskLevel", retMap.get("riskLevel"));
				parameter.put("investType", retMap.get("riskLevelDesc"));//风险描述

				// 更新user表的已经风险提测
				try {
					userDao.updRiskUser(parameter);
				} catch (Exception e) {
					logger.error("-----------风险评测更新user表异常----------");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					e.printStackTrace();
				}
				//更新userDetail表的风险评分
				try {
					userDao.updRiskUserDetail(parameter);
				} catch (Exception e) {
					logger.error("-----------风险评测更新user_detail表异常----------");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					e.printStackTrace();
				}
			}
		}
		return retMap;
	}


}
