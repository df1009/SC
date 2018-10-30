package com.zhongchou.common.zhongzhengSao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yanshang.util.ConvUtils;
import com.zhongchou.common.base.SCMap;
import com.zhongchou.common.util.Log4jUtil;
import com.zhongchou.common.util.ZhongzhengUtil;
import com.zhongchou.common.zhongzheng.util.TimeoutException;
//用户绑卡查询
public class QueryTiedBankCardSao extends BaseSao {
	Logger logger=Logger.getLogger(QueryTiedBankCardSao.class);
	String functionId = "G0000026";

	public QueryTiedBankCardSao(String req_ssn) {
		super.req_ssn = req_date+req_ssn;
	}


	public Map receivEncryptData(String content){
		Map retMap = new HashMap();//返回值
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
				List cardList = new ArrayList();//卡集合
				JSONArray records = body.getJSONArray("records");//
				for (int i = 0; i < records.size(); i++) {
					Map cardMap = new HashMap();
					JSONObject jsonItem = records.getJSONObject(i);
					cardMap.put("userNmCn", jsonItem.get("user_nm_cn").toString());//姓名
					cardMap.put("idTp", jsonItem.get("id_tp").toString());//证件类型
					cardMap.put("idNo", jsonItem.get("id_no").toString());//证件号码
					cardMap.put("bankCd", jsonItem.get("bank_cd").toString());//行别
					cardMap.put("cardNo", jsonItem.get("card_no").toString());//银行卡
					cardMap.put("mobileNo", jsonItem.get("mobile_no").toString());//预留手机号
					cardMap.put("isDefault", jsonItem.get("is_default").toString());//是否是默认银行卡 0否1是
					cardMap.put("cardTp", jsonItem.get("card_tp").toString());//银行卡类型1借记卡 2贷记卡
					cardList.add(cardMap);
				}
				retMap.put("cardList", cardList);
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

		scBody.setValue("login_id", ConvUtils.convToString(setData.get("loginId")));//登录帐号
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
