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
//撤单
public class RedeemProductSao extends BaseSao {
	Logger logger=Logger.getLogger(RedeemProductSao.class);
	String functionId = "G0000023";

	public RedeemProductSao(String req_ssn) {
		super.req_ssn = req_date+req_ssn;
	}


	public Map receivEncryptData(String content){
		Map retMap = new HashMap();
		List productList = null;
		//解密后的接口数据
		String encryptData = null;
		try {
			encryptData = ZhongzhengUtil.receivEncryptData(content);
			logger.info("RedeemProductSao receivEncryptData response ("+functionId+") :\r\n"+Log4jUtil.output(encryptData));
			JSONObject obj = JSONObject.fromObject(encryptData);
			JSONObject head= obj.getJSONObject("head");
			JSONObject body= obj.getJSONObject("body");
			String rspCode = head.get("rsp_code").toString();
			String rspDesc = head.get("rsp_desc").toString();
			retMap.put("req_ssn", head.get("req_ssn"));
			retMap.put("rsp_code", rspCode);
			retMap.put("rsp_desc", rspDesc);
		} catch (Exception e) {
			retMap.put("rsp_code", "000002");
			retMap.put("rsp_desc", "获取中证数据失败");
			e.printStackTrace();
			return retMap;
		}
		return retMap;
	}

	public Map setEncryptData(Map setData){
		logger.info("RedeemProductSao setEncryptData start ");
		Map retMap = new HashMap();
		SCMap scBody = new SCMap();
		SCMap scHeader = new SCMap();
		SCMap scMap = new SCMap(scHeader,scBody);
		scHeader.setValue("req_ssn", req_ssn);
		scHeader.setValue("req_date", req_date);
		scHeader.setValue("version", version);
		scHeader.setValue("ins_cd", ins_cd);
		scHeader.setValue("channel_no", channel_no);
		scHeader.setValue("function", functionId);
		String userIp = (String)setData.get("userIp");
		if(StringUtils.isEmpty(userIp)){
			retMap.put("rsp_code", "000003");
			retMap.put("rsp_desc", "userIp为空");
		}
		scHeader.setValue("user_ip", ConvUtils.convToString(setData.get("userIp")));//操作ip

		scBody.setValue("login_id", ConvUtils.convToString(setData.get("loginId")));//登录帐号
		scBody.setValue("req_ssn", ConvUtils.convToString(setData.get("reqSsn")));//原交易请求流水
		scBody.setValue("req_date", ConvUtils.convToString(setData.get("reqDate")));//原交易请求日期
		scBody.setValue("auth_tp", ConvUtils.convToString(setData.get("authTp")));//安全认证方式1 支付密码2 短信12 支付密码和短信同时验证码
		scBody.setValue("pay_pwd", ConvUtils.convToString(setData.get("payPwd")));//支付密码
		scBody.setValue("yzm_ssn", ConvUtils.convToString(setData.get("yzmSsn")));//验证码流水
		scBody.setValue("yzm_content", ConvUtils.convToString(setData.get("yzmContent")));//验证码
		scBody.setValue("product_cd", ConvUtils.convToString(setData.get("productCd")));//产品id

		logger.info("RedeemProductSao setEncryptData encryptDatarequest("+functionId+") :\r\n"+Log4jUtil.output(scMap.getMap().toString()));
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
