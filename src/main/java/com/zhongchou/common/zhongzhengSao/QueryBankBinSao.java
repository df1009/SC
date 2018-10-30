package com.zhongchou.common.zhongzhengSao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.SCMap;
import com.zhongchou.common.util.Log4jUtil;
import com.zhongchou.common.util.ZhongzhengUtil;
import com.zhongchou.common.zhongzheng.util.TimeoutException;
//银行卡bin查询
public class QueryBankBinSao extends BaseSao {
	Logger logger=Logger.getLogger(QueryBankBinSao.class);
	String functionId = "G0000036";

	public QueryBankBinSao(String req_ssn) {
		super.req_ssn = req_date+req_ssn;
	}


	public Map receivEncryptData(String content){
		Map retMap = new HashMap();
		List productList = null;
		//解密后的接口数据
		String encryptData = null;
		try {
			encryptData = ZhongzhengUtil.receivEncryptData(content);
			logger.info("QueryBankBinSao receivEncryptData response ("+functionId+") :\r\n"+Log4jUtil.output(encryptData));
			JSONObject obj = JSONObject.fromObject(encryptData);
			JSONObject head= obj.getJSONObject("head");
			JSONObject body= obj.getJSONObject("body");
			String rspCode = head.get("rsp_code").toString();
			String rspDesc = head.get("rsp_desc").toString();
			if("000000".equals(rspCode)){//成功
				retMap.put("cardBin", body.get("card_bin").toString());
				retMap.put("bankCd", body.get("bank_cd").toString());
				retMap.put("cardTp", body.get("card_tp").toString());
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
		Map retMap = new HashMap();
		logger.info("QueryBankBinSao setEncryptData start ");
		SCMap scBody = new SCMap();
		SCMap scHeader = new SCMap();
		SCMap scMap = new SCMap(scHeader,scBody);
		scHeader.setValue("req_ssn", req_ssn);
		scHeader.setValue("req_date", req_date);
		scHeader.setValue("version", version);
		scHeader.setValue("ins_cd", ins_cd);
		scHeader.setValue("channel_no", channel_no);
		scHeader.setValue("function", functionId);

		scBody.setValue("card_no", ConvUtils.convToString(setData.get("cardNo")));//银行卡号
		logger.info("QueryBankBinSao setEncryptData encryptDatarequest("+functionId+") :\r\n"+Log4jUtil.output(scMap.getMap().toString()));
		String responseContent = null;
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
