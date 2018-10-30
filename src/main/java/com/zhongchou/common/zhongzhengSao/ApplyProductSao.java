package com.zhongchou.common.zhongzhengSao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.SCMap;
import com.zhongchou.common.util.Log4jUtil;
import com.zhongchou.common.util.ZhongzhengUtil;
import com.zhongchou.common.zhongzheng.util.TimeoutException;
//认购申购产品
public class ApplyProductSao extends BaseSao {
	Logger logger=Logger.getLogger(ApplyProductSao.class);
	String functionId = "G0000020";

	public ApplyProductSao(String req_ssn) {
		super.req_ssn = req_date+req_ssn;
	}

	public Map receivEncryptData(String content){
		Map retMap = new HashMap();
		List productList = null;
		//解密后的接口数据
		String encryptData = null;
		try {
			encryptData = ZhongzhengUtil.receivEncryptData(content);
			logger.info("ApplyProductSao receivEncryptData response ("+functionId+") :\r\n"+Log4jUtil.output(encryptData));
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
		logger.info("ApplyProductSao setEncryptData start ");
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
		scHeader.setValue("user_ip", setData.get("userIp").toString());//操作ip

		scBody.setValue("login_id", ConvUtils.convToString(setData.get("loginId")));//登录帐号
		scBody.setValue("product_cd", ConvUtils.convToString(setData.get("productCd")));//产品代码
		scBody.setValue("card_no", ConvUtils.convToString(setData.get("cardNo")));//银行卡号
		scBody.setValue("txn_amt", ConvUtils.convToString(setData.get("txnAmt")));//交易金额  单位：分
		scBody.setValue("pay_tp", ConvUtils.convToString(setData.get("payTp")));//支付方式  0 理财余额支付1 银行卡支付
		scBody.setValue("auth_tp", ConvUtils.convToString(setData.get("authTp")));//安全认证方式1 支付密码2 短信12 支付密码和短信同时验证码
		scBody.setValue("pay_pwd", ConvUtils.convToString(setData.get("payPwd")));//支付密码
		scBody.setValue("yzm_ssn", ConvUtils.convToString(setData.get("yzmSsn")));//验证码流水
		scBody.setValue("yzm_content", ConvUtils.convToString(setData.get("yzmContent")));//验证码

		logger.info("ApplyProductSao setEncryptData encryptDatarequest("+functionId+") :\r\n"+Log4jUtil.output(scMap.getMap().toString()));
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
