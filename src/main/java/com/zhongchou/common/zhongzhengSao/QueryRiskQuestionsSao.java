package com.zhongchou.common.zhongzhengSao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhongchou.common.base.SCMap;
import com.zhongchou.common.util.Log4jUtil;
import com.zhongchou.common.util.ZhongzhengUtil;
import com.zhongchou.common.zhongzheng.util.TimeoutException;
//风险题库查询
public class QueryRiskQuestionsSao extends BaseSao {
	Logger logger=Logger.getLogger(QueryRiskQuestionsSao.class);
	String functionId = "G0000017";

	public QueryRiskQuestionsSao(String req_ssn) {
		super.req_ssn = req_date+req_ssn;
	}


	public Map receivEncryptData(String content){
		Map retMap = new HashMap();//返回值

		List subjectList = new LinkedList();//题目
		//解密后的接口数据
		String encryptData = null;
		try {
			encryptData = ZhongzhengUtil.receivEncryptData(content);
			logger.info("QueryRiskQuestionsSao receivEncryptData response ("+functionId+") :\r\n"+Log4jUtil.output(encryptData));
			JSONObject obj = JSONObject.fromObject(encryptData);
			JSONObject head= obj.getJSONObject("head");
			JSONObject body= obj.getJSONObject("body");
			String rspCode = head.get("rsp_code").toString();
			String rspDesc = head.get("rsp_desc").toString();
			if("000000".equals(rspCode)){//成功
				JSONArray records = body.getJSONArray("records");//
				String totalResults = body.get("total_results").toString();//总记录数
				String testqId = body.get("testq_id").toString();//题库编号
				for (int i = 0; i < records.size(); i++) {
					Map subjectMap = new HashMap();//题目和答案
					List answerList = new ArrayList();//答案
					//获取题目
					JSONObject jsonItem = records.getJSONObject(i);
					subjectMap.put("questionId", jsonItem.get("question_id").toString());//题目编号
					subjectMap.put("questionTp", jsonItem.get("question_tp").toString());//题目类型0 单选1 多选
					subjectMap.put("question", jsonItem.get("question").toString());//题目内容
					subjectMap.put("answerChooses", answerList);//题目答案
					//答案
					JSONArray answerChooses = records.getJSONObject(i).getJSONArray("answer_choose");
					for (int j = 0; j < answerChooses.size(); j++) {
						JSONObject answerItem = answerChooses.getJSONObject(j);
						Map answerMap = new HashMap();//答案
						answerMap.put("answer", answerItem.get("answer").toString());//选项
						answerMap.put("content", answerItem.get("content").toString());//答案
						answerMap.put("score", answerItem.get("score").toString());//分值
						answerList.add(answerMap);
					}
					subjectList.add(subjectMap);
					}

				retMap.put("totalResults", totalResults);
				retMap.put("testqId", testqId);
				retMap.put("subjectList", subjectList);
				retMap.put("rsp_code", rspCode);
				retMap.put("rsp_desc", rspDesc);
			}else{//失败
				retMap.put("rsp_code", rspCode);
				retMap.put("rsp_desc", rspDesc);
			}
		} catch (Exception e) {
			retMap.put("rsp_code", "000002");
			retMap.put("rsp_desc", "获取中证数据失败");
			e.printStackTrace();
			return retMap;
		}
		return retMap;
	}

	public Map setEncryptData(Map setData){
		logger.info("QueryRiskQuestionsSao setEncryptData start ");
		SCMap scBody = new SCMap();
		SCMap scHeader = new SCMap();
		SCMap scMap = new SCMap(scHeader,scBody);
		scHeader.setValue("req_ssn", req_ssn);
		scHeader.setValue("req_date", req_date);
		scHeader.setValue("version", version);
		scHeader.setValue("ins_cd", ins_cd);
		scHeader.setValue("channel_no", channel_no);
		scHeader.setValue("function", functionId);

		logger.info("QueryRiskQuestionsSao setEncryptData encryptDatarequest("+functionId+") :\r\n"+Log4jUtil.output(scMap.getMap().toString()));
		String responseContent = null;
		Map retMap = new HashMap();
		try {
			responseContent = ZhongzhengUtil.encryption3Des(scMap.getMap());
		} catch (TimeoutException e) {
			retMap.put("rsp_code", "000010");
			retMap.put("rsp_desc", "请求中证超时");
			e.printStackTrace();
			return retMap;
		}catch (Exception e) {

			retMap.put("rsp_code", "000001");
			retMap.put("rsp_desc", "请求中证失败");
			e.printStackTrace();
			return retMap;
		}//请求中证获取数据
		return receivEncryptData(responseContent);
	}
}
