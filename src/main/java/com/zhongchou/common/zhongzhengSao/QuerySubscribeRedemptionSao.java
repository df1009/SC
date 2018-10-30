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
	//认申赎交易查询
	public class QuerySubscribeRedemptionSao extends BaseSao {
		Logger logger=Logger.getLogger(QuerySubscribeRedemptionSao.class);
		String functionId = "G0000022";

	public QuerySubscribeRedemptionSao(String req_ssn) {
		super.req_ssn = req_date+req_ssn;
	}


	public Map receivEncryptData(String content){
		Map retMap = new HashMap();
		List transactionQueryList = null;
		//解密后的接口数据
		String encryptData = null;
		try {
			encryptData = ZhongzhengUtil.receivEncryptData(content);
			logger.info("QuerySubscribeRedemptionSao receivEncryptData response ("+functionId+") :\r\n"+Log4jUtil.output(encryptData));
			JSONObject obj = JSONObject.fromObject(encryptData);
			JSONObject head= obj.getJSONObject("head");
			JSONObject body= obj.getJSONObject("body");
			String rspCode = head.get("rsp_code").toString();
			String rspDesc = head.get("rsp_desc").toString();
			if("000000".equals(rspCode)){//成功
				transactionQueryList = new ArrayList();
				JSONArray records = body.getJSONArray("records");
				String totalResults = body.get("total_results").toString();//总记录数
				String loginId = body.get("login_id").toString();//登录帐号
				for (int i = 0; i < records.size(); i++) {
					Map transactionQueryMap = new HashMap();
					//获取每一个json对象
					JSONObject jsonItem = records.getJSONObject(i);
					transactionQueryMap.put("reqSsn", ConvUtils.convToString(jsonItem.get("req_ssn"))) ;//原交易请求流水
					transactionQueryMap.put("reqDate", ConvUtils.convToString(jsonItem.get("req_date"))) ;//原交易请求日期
					transactionQueryMap.put("txnDate", ConvUtils.convToString(jsonItem.get("txn_date"))) ;//记账日期 yyyyMMdd
					transactionQueryMap.put("txnTime", ConvUtils.convToString(jsonItem.get("txn_time"))) ;//记账时间 HHMMSS
					transactionQueryMap.put("productCd", ConvUtils.convToString(jsonItem.get("product_cd"))) ;//产品代码
					transactionQueryMap.put("productInstTp", ConvUtils.convToString(jsonItem.get("product_inst_tp"))) ;//产品大类1  基金产品类2  私募基金类3  私募股权类4  信托产品类5  资管产品类6  银行产品类7  保险产品类8  债务融资工具类9 衍生品类10 内部产品11 资产支持证券类12 收益凭证类13 服务产品99 其他类型
					transactionQueryMap.put("productShortNm", ConvUtils.convToString(jsonItem.get("product_short_nm"))) ;//产品简称
					transactionQueryMap.put("txnTp", ConvUtils.convToString(jsonItem.get("txn_tp"))) ;//交易类型020 认购022 申购024 赎回052 撤单149 募集失败退款（TA下发的交易）150 产品清盘退款（TA下发的交易）151 产品终止退款（TA下发的交易）142 强制赎回143 分红（TA下发的交易）
					transactionQueryMap.put("txnAmt", ConvUtils.convToString(jsonItem.get("txn_amt"))) ;//产品简称
					transactionQueryMap.put("txnShare", ConvUtils.convToString(jsonItem.get("txn_share"))) ;//申请数量   赎回时必填；浮点型
					transactionQueryMap.put("txnSt", ConvUtils.convToString(jsonItem.get("txn_st"))) ;//订单状态 0：受理中1：受理失败2：受理成功
					transactionQueryMap.put("confirmSt", ConvUtils.convToString(jsonItem.get("confirm_st"))) ;//确认状态0：待确认1：确认失败2：确认成功3：部分确认成功
					transactionQueryMap.put("paySt", ConvUtils.convToString(jsonItem.get("pay_st"))) ;//支付状态0：待支付（申购待支付，赎回撤单待付款）1  支付中（申购支付中，赎回撤单付款中）2：支付失败（申购支付失败，赎回撤单付款失败）3：支付成功（申购支付成功，赎回撤单付款成功）
					transactionQueryMap.put("canCancel", ConvUtils.convToString(jsonItem.get("can_cancel"))) ;//可撤单标志 0：不可撤单1：可撤单
					transactionQueryMap.put("backamtSt", ConvUtils.convToString(jsonItem.get("backamt_st"))) ;//退款状态0 : 正常1 : 已退款
					transactionQueryMap.put("backAmt", ConvUtils.convToString(jsonItem.get("back_amt"))) ;//退款金额
					transactionQueryMap.put("confirmAmt", ConvUtils.convToString(jsonItem.get("confirm_amt"))) ;//确定金额
					transactionQueryMap.put("confirmShare", ConvUtils.convToString(jsonItem.get("confirm_share"))) ;//确定数量
					transactionQueryMap.put("payWay", ConvUtils.convToString(jsonItem.get("pay_way"))) ;//支付方式0 : 理财余额支付1 : 银行卡支付
					transactionQueryMap.put("sec_date", ConvUtils.convToString(jsonItem.get("sec_date"))) ;//证券公司交易日期  yyyyMMdd
					transactionQueryList.add(transactionQueryMap);
				}
				retMap.put("totalResults", totalResults);
				retMap.put("transactionQueryList", transactionQueryList);
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
		logger.info("QuerySubscribeRedemptionSao setEncryptData start ");
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
		scBody.setValue("product_inst_tp", ConvUtils.convToString(setData.get("productInstTp")));//产品大类
		scBody.setValue("txn_tp", ConvUtils.convToString(setData.get("txnTp")));//交易类型020 认购022 申购024 赎回052 撤单149 募集失败退款（TA下发的交易）150 产品清盘退款（TA下发的交易）151 产品终止退款（TA下发的交易）142 强制赎回143 分红（TA下发的交易）
		scBody.setValue("can_cancel", ConvUtils.convToString(setData.get("canCancel")));//可撤单标志0：不可撤单1：可撤单
		scBody.setValue("start_dt", ConvUtils.convToString(setData.get("startDt")));//起始交易请求日期  yyyyMMdd
		scBody.setValue("end_dt", ConvUtils.convToString(setData.get("endDt")));//截止交易请求日期 yyyyMMdd
		scBody.setValue("req_ssn", ConvUtils.convToString(setData.get("reqSsn")));//原交易请求流水 如果有值，开始截至日期必须有值且相同
		scBody.setValue("page_no", ConvUtils.convToString(setData.get("pageNo")));//页码
		scBody.setValue("page_size", ConvUtils.convToString(setData.get("pageSize")));//每页获取条数  每页获取条数。最大值1000
		scBody.setValue("order_by", ConvUtils.convToString(setData.get("orderBy")));//查询结果时间排序  1-升序，2-降序 不填默认降序

		logger.info("QuerySubscribeRedemptionSao setEncryptData encryptDatarequest("+functionId+") :\r\n"+Log4jUtil.output(scMap.getMap().toString()));
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
