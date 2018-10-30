package com.zhongchou.common.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.MessageUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IPCMessageDao;
import com.zhongchou.common.dao.IUserAccountBindBankDao;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dao.IUserTenderDao;
import com.zhongchou.common.dao.PcProductDao;
import com.zhongchou.common.dao.ProductDao;
import com.zhongchou.common.dto.ProductDetailDto;
import com.zhongchou.common.dto.UserTender;
import com.zhongchou.common.service.IBuyProductService;
import com.zhongchou.common.util.CommonUtils;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.zhongzhengSao.ApplyProductSao;
import com.zhongchou.common.zhongzhengSao.ProductDetailSao;
import com.zhongchou.common.zhongzhengSao.QuerySubscribeRedemptionSao;

@Service
public class BuyProductServiceImpl extends BaseSaoServiceImpl implements IBuyProductService {
	Logger logger=Logger.getLogger(BuyProductServiceImpl.class);
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IUserTenderDao userTenderDao;
	@Autowired
	private IUserAccountBindBankDao userAccountBindBankDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private PcProductDao PcproductDao;
	@Autowired
	private IPCMessageDao messageDao;
	@Override
	public boolean saveConfirmRiskWarning(String userId) {
		return userDao.saveConfirmRiskWarning(userId);
	}

	@Override
	public Map buyProduct(Map buyMap) {
		logger.info("BuyProductServiceImpl.buyProduct  start"+buyMap.get("loginId"));
		ApplyProductSao buyProduct = new ApplyProductSao(nextSeq());
		Map retMap = buyProduct.setEncryptData(buyMap);
		UserTender userTender = new UserTender();
		String oidTenderId = CommonUtils.generateId(DateUtils.getDateTime());
		do{
			oidTenderId = CommonUtils.generateId(DateUtils.getDateTime());
		}while(userTenderDao.checkTenderIdExists(oidTenderId));
		userTender.setOidTenderId(oidTenderId);
		userTender.setOidPlatformProductsId((String)buyMap.get("productCd"));
		userTender.setOidUserId((String)buyMap.get("loginId"));
		userTender.setTenderAmount((Double)buyMap.get("txnAmt"));
		userTender.setAutoStatus((String)buyMap.get("autoStatus"));
		userTender.setRequestNo((String)buyMap.get("requestNo"));
		userTender.setTenderSsn((String)retMap.get("req_ssn"));
		//根据银行卡号查询管理表id
		Row bankRow = userAccountBindBankDao.selOidAccountBindBankId(userTender.getOidUserId(),(String)buyMap.get("cardNo"));
		String bankId = (String)bankRow.get("OID_ACCOUNT_BIND_BANK_ID");
		if("000000".equals(retMap.get("rsp_code"))){//购买成功，增加购买记录
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功");
			userTender.setTenderStatus("1");
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功记录tender表开始");
			userTenderDao.saveUserTender(userTender,bankId);
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功记录tender表结束");
			//更新中证产品数据
			ProductDetailSao proSao = new ProductDetailSao(nextSeq());
			Map reqMap = new HashMap();
			reqMap.put("productCd", buyMap.get("productCd"));
			reqMap.put("pageNo", "1");
			reqMap.put("pageSize", "5");
			List retList = (List)proSao.setEncryptData(reqMap).get("productList");
			ProductDetailDto proDetilDto = new ProductDetailDto();
			if(retList != null && retList.size()>0){
				proDetilDto = (ProductDetailDto)retList.get(0);
			}
			String surplusQuota = proDetilDto.getSurplus_sales_quota();//当前项目剩余投资额
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功更新产品生表开始");
			if(!"-1".equals(surplusQuota)){//不限
				productDao.updateProductToBuy(surplusQuota,(String)buyMap.get("productCd"));
			}
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功更新产品生表结束，当前产品剩余购买额度为："+surplusQuota+"分");
		}else if("000010".equals(retMap.get("rsp_code"))
				||"000001".equals(retMap.get("rsp_code"))
				||"000002".equals(retMap.get("rsp_code"))){//超时
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"超时");
			userTender.setTenderStatus("2");
			userTenderDao.saveUserTender(userTender,bankId);
		}else{
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"失败  流水号："+retMap.get("req_ssn"));
			userTender.setTenderStatus("0");
			logger.info("购买失败开始查询交易是否扣款");
			Map setMap = new HashMap();
			QuerySubscribeRedemptionSao querySao = new QuerySubscribeRedemptionSao(nextSeq());
			setMap.put("loginId", buyMap.get("loginId"));//登录帐号
			setMap.put("productCd", buyMap.get("productCd"));//产品代码
			setMap.put("startDt",  DateUtils.convertDate2String(new Date(), "yyyyMMdd"));//起始交易请求日期  yyyyMMdd
			setMap.put("endDt",  DateUtils.convertDate2String(new Date(), "yyyyMMdd"));//截止交易请求日期 yyyyMMdd
			setMap.put("reqSsn", retMap.get("req_ssn"));//原交易请求流水
			setMap.put("pageNo", "1");//页码
			setMap.put("pageSize", "1");//每页获取条数
			setMap.put("userIp", buyMap.get("userIp"));//ip
			Map QueryMap = querySao.setEncryptData(setMap);
			if("000000".equals(QueryMap.get("rsp_code"))){
				if(QueryMap.get("transactionQueryList")!=null){
					List QueryList = (List)QueryMap.get("transactionQueryList");
					if(QueryList.size()>0){
						String paySt = ConvUtils.convToString(((Map)QueryList.get(0)).get("paySt"));
						if("3".equals(paySt)){//购买失败支付成功
							logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"失败  扣款成功");
							userTenderDao.saveUserTender(userTender,bankId);//插入购买失败记录
							//插入退款记录
							Map tenderMap = new HashMap();
							tenderMap.put("tenderId", userTender.getOidTenderId());
							tenderMap.put("proId", userTender.getOidPlatformProductsId());
							tenderMap.put("userId", userTender.getOidUserId());
							tenderMap.put("redeemSsn", userTender.getTenderSsn());
							tenderMap.put("amount", userTender.getTenderAmount());
							tenderMap.put("type", "1");
							userTenderDao.insRefund(tenderMap);
						}
					}
				}
			}

		}
		return retMap;
	}

	/**
	 * 用户创建订单
	 * @param userTender
	 * @return
	 */
    @Override
    public Map<String, Object> createOrder(UserTender userTender) {
		String oidTenderId = CommonUtils.generateId(DateUtils.getDateTime());//产生订单号
		userTender.setOidTenderId(oidTenderId);
		Map<String,Object> orderMap = new HashMap<>();
		if(userTenderDao.saveUserTender(userTender,"")>0){
			orderMap.put("oidTenderId",oidTenderId);
		}
        return orderMap;
    }
	/**
	 * 查询用户订单待支付信息
	 * @param paramMap
	 * @return
	 */
	@Override
	public Map<String, Object> getWaitOrderInfo(Map<String, String> paramMap) {
		//订单信息
		Row waitTenderInfo = userTenderDao.getWaitTenderInfo(paramMap);

		Map<String,Object> displayOrderMap = new HashMap<>();
		//剩余支付时间
		if(!StringUtils.isEmpty(waitTenderInfo.getString("insDate"))){
			String endBusinessTime = "150000";//HHmmss
			if (ConvUtils.convToInt(Config.getString("coolingPeriod")) > 0) {
				endBusinessTime = ConvUtils.convToString(Config.getString("coolingPeriod"));
			}
			DateFormat day = new SimpleDateFormat("yyyyMMdd");
			String format = day.format(new Date())+endBusinessTime;
			//订单30分钟剩余时间
			double orderTime = (waitTenderInfo.getDate("overdueDate").getTime()-new Date().getTime())/1000;
			//系统最晚购买剩余时间
			double systemTime = (DateUtils.convertString2Date(format,"yyyyMMddHHmmss").getTime()-new Date().getTime())/1000;
			if(orderTime>systemTime){
				orderTime = systemTime;
			}
			if(orderTime<0){
				orderTime=0;
			}
			displayOrderMap.put("remainTime",orderTime);
			displayOrderMap.put("remainTimeApp",String.valueOf((int)orderTime/60));
		}

		//支持的银行卡及限额
		List<Row> bankRow  = userAccountBindBankDao.selSupportBankList();
		List bankList = new ArrayList();
		for (Row row : bankRow) {
			if (!row.isEmpty()) {
				Map bankMap = new HashMap();
				bankMap.put("bankId", ConvUtils.convToString(row.get("BANK_CD")));
				String isBankSign = StringUtil.isBankSign(ConvUtils.convToString(row.get("BANK_CD")));
				bankMap.put("isBankSign",isBankSign);//根据银行cd判断该行是否需要网银签约 0：否  1：是
				bankMap.put("bankNm", ConvUtils.convToString(row.get("BANK_NM")));
				String singleLimitAmt = StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SINGLE_LIMIT_AMT")));
				String dayLimitAmt = StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("DAY_LIMIT_AMT")));
				bankMap.put("singleLimitAmt", Double.valueOf(singleLimitAmt)/10000);//万元
				bankMap.put("dayLimitAmt", Double.valueOf(dayLimitAmt)/10000);//万元
				bankList.add(bankMap);
			}
		}
		displayOrderMap.put("bankList",bankList);

		//用户当日投资金额
		int tenderAmt = userTenderDao.countUserTenderAmountToday(paramMap.get("userId"));
		int dayLimitAmt = Integer.parseInt(StringUtil.changeSalesQuota(waitTenderInfo.getString("dayLimitAmt")));
		logger.info("dayLimitAmt:"+dayLimitAmt);
		String dayLimitAmta = dayLimitAmt-tenderAmt+"";
		displayOrderMap.put("dayLimitAmt", dayLimitAmta);

		String bankNo = waitTenderInfo.getString("cardNo");
		if(!StringUtils.isEmpty(bankNo)){
			Map<String,Object> sessionMap = new HashMap<>();
			displayOrderMap.put("cardNoLast", bankNo.substring(bankNo.length()-4,bankNo.length()));//银行卡后四位数
			String bankNoIndex = bankNo.hashCode()+"";
			displayOrderMap.put("bankNoIndex", bankNoIndex);//银行卡hashcode
			sessionMap.put("bankNoIndex",bankNoIndex);
			sessionMap.put("bankNo",bankNo);
			sessionMap.put("cardNm",waitTenderInfo.getString("bankName"));
			sessionMap.put("bankCd",waitTenderInfo.getString("bankCd"));
			sessionMap.put("insDate",DateUtils.convertDate2String(waitTenderInfo.getDate("insDate"),"yyyy-MM-dd HH:mm:ss"));

			waitTenderInfo.put("sessionMap",sessionMap);

		}
		displayOrderMap.put("tenderId",waitTenderInfo.getString("tenderId"));
		displayOrderMap.put("projectName",waitTenderInfo.getString("projectName"));
		displayOrderMap.put("userName",waitTenderInfo.getString("userName"));
		displayOrderMap.put("tenderAmount",waitTenderInfo.getString("tenderAmount"));
		displayOrderMap.put("mobile",waitTenderInfo.getString("mobile"));
		displayOrderMap.put("singleLimitAmt",StringUtil.changeSalesQuota(waitTenderInfo.getString("singleLimitAmt")));
		displayOrderMap.put("idCard",waitTenderInfo.getString("idCard"));
		displayOrderMap.put("bankCd",waitTenderInfo.getString("bankCd"));
		displayOrderMap.put("appSmallImg",waitTenderInfo.getString("APP_LIST_IMG"));
		//APP_LIST_IMG

		waitTenderInfo.put("displayOrderMap",displayOrderMap);

		return waitTenderInfo;
	}
	/**
	 * 确认支付
	 * @param buyMap
	 * @return
	 */
	@Override
	public Map payOrderConfirm(Map buyMap) {
		logger.info("BuyProductServiceImpl.payOrderConfirm  start"+buyMap.get("loginId"));
		String ssn = nextSeq();
		ApplyProductSao buyProduct = new ApplyProductSao(ssn);
		Map retMap = buyProduct.setEncryptData(buyMap);
		UserTender userTender = new UserTender();
		String oidTenderId = ConvUtils.convToString(buyMap.get("tenderId"));
		userTender.setOidTenderId(oidTenderId);
		userTender.setOidPlatformProductsId((String)buyMap.get("productCd"));
		userTender.setOidUserId((String)buyMap.get("loginId"));
		userTender.setTenderAmount((Double)buyMap.get("txnAmt"));
		userTender.setTenderSsn(DateUtils.convertDate2String(new Date(),"yyyyMMdd")+ssn);

		//根据银行卡号查询管理表id
		Row bankRow = userAccountBindBankDao.selOidAccountBindBankId(userTender.getOidUserId(),(String)buyMap.get("cardNo"));
		String bankId = (String)bankRow.get("OID_ACCOUNT_BIND_BANK_ID");
		if("000000".equals(retMap.get("rsp_code"))){//购买成功，增加购买记录
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功");
			userTender.setTenderStatus("1");
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功记录tender表开始");
			userTenderDao.updUserTender(userTender,bankId);//购买成功更新tender表
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功记录tender表结束");
			//更新中证产品数据
			ProductDetailSao proSao = new ProductDetailSao(nextSeq());
			Map reqMap = new HashMap();
			reqMap.put("productCd", buyMap.get("productCd"));
			reqMap.put("pageNo", "1");
			reqMap.put("pageSize", "5");
			List retList = (List)proSao.setEncryptData(reqMap).get("productList");
			ProductDetailDto proDetilDto = new ProductDetailDto();
			if(retList != null && retList.size()>0){
				proDetilDto = (ProductDetailDto)retList.get(0);
			}
			String surplusQuota = proDetilDto.getSurplus_sales_quota();//当前项目剩余投资额
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功更新产品生表开始");
			if(!"-1".equals(surplusQuota)){//不限
				productDao.updateProductToBuy(surplusQuota,(String)buyMap.get("productCd"));
			}
			retMap.put("tenderAmt",ConvUtils.convToString(buyMap.get("txnAmt")));
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功更新产品生表结束，当前产品剩余购买额度为："+surplusQuota+"分");
		}else if("000010".equals(retMap.get("rsp_code"))
				||"000001".equals(retMap.get("rsp_code"))
				||"000002".equals(retMap.get("rsp_code"))){//超时
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"超时");
			userTender.setTenderStatus("2");
			userTenderDao.updUserTender(userTender,bankId);
		}else{
			logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"失败  流水号："+retMap.get("req_ssn"));
			userTender.setTenderStatus("0");
			logger.info("购买失败开始查询交易是否扣款");
			Map setMap = new HashMap();
			QuerySubscribeRedemptionSao querySao = new QuerySubscribeRedemptionSao(nextSeq());
			setMap.put("loginId", buyMap.get("loginId"));//登录帐号
			setMap.put("productCd", buyMap.get("productCd"));//产品代码
			setMap.put("startDt",  DateUtils.convertDate2String(new Date(), "yyyyMMdd"));//起始交易请求日期  yyyyMMdd
			setMap.put("endDt",  DateUtils.convertDate2String(new Date(), "yyyyMMdd"));//截止交易请求日期 yyyyMMdd
			setMap.put("reqSsn", retMap.get("req_ssn"));//原交易请求流水
			setMap.put("pageNo", "1");//页码
			setMap.put("pageSize", "1");//每页获取条数
			setMap.put("userIp", buyMap.get("userIp"));//ip
			Map QueryMap = querySao.setEncryptData(setMap);
			if("000000".equals(QueryMap.get("rsp_code"))){
				if(QueryMap.get("transactionQueryList")!=null){
					List QueryList = (List)QueryMap.get("transactionQueryList");
					if(QueryList.size()>0){
						String paySt = ConvUtils.convToString(((Map)QueryList.get(0)).get("paySt"));
						logger.info("当前用户的扣款状态paySt为:"+paySt);
						if("3".equals(paySt)){//购买失败支付成功
							logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"失败  扣款成功");
							userTenderDao.updUserTender(userTender,bankId);//插入购买失败记录
							//插入退款记录
							Map tenderMap = new HashMap();
							tenderMap.put("tenderId", userTender.getOidTenderId());
							tenderMap.put("proId", userTender.getOidPlatformProductsId());
							tenderMap.put("userId", userTender.getOidUserId());
							tenderMap.put("redeemSsn", userTender.getTenderSsn());
							tenderMap.put("amount", userTender.getTenderAmount());
							tenderMap.put("type", "1");
							userTenderDao.insRefund(tenderMap);
							//新增用户的退款消息
							Map msgMap = new HashMap();
							msgMap.put("msgTitle",MessageUtils.getMessage("MSG0003"));
							msgMap.put("styleTitle",PcproductDao.selProductName((String)buyMap.get("projectName"))+";"+buyMap.get("projectCode"));
							msgMap.put("msgContent","去看看");
							msgMap.put("msgType","2");//项目消息
							msgMap.put("msgStuType","1");//1:项目状态消息
							msgMap.put("oidUserId",buyMap.get("loginId"));
							msgMap.put("oidUserId",buyMap.get("loginId"));
							messageDao.insMessage(msgMap);
						}else if("1".equals(paySt)){//支付未确认
							logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"失败  支付未确认");
							userTender.setTenderStatus("2");
							userTenderDao.updUserTender(userTender,bankId);//插入购买超时记录
						}
					}
				}
			}

		}
		return retMap;
	}
	/*@Override
	public Map payOrderConfirm(Map buyMap) {
		logger.info("BuyProductServiceImpl.payOrderConfirm  start"+buyMap.get("loginId"));
		Map retMap = new HashMap<>();
		UserTender userTender = new UserTender();
		String oidTenderId = ConvUtils.convToString(buyMap.get("tenderId"));
		userTender.setOidTenderId(oidTenderId);
		userTender.setOidPlatformProductsId((String)buyMap.get("productCd"));
		userTender.setOidUserId((String)buyMap.get("loginId"));
		userTender.setTenderAmount((Double)buyMap.get("txnAmt"));
		userTender.setTenderSsn(nextSeq());

		//根据银行卡号查询管理表id
		Row bankRow = userAccountBindBankDao.selOidAccountBindBankId(userTender.getOidUserId(),(String)buyMap.get("cardNo"));
		String bankId = (String)bankRow.get("OID_ACCOUNT_BIND_BANK_ID");
		logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功");
		userTender.setTenderStatus("3");
		logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功记录tender表开始");
		userTenderDao.updUserTender(userTender,bankId);//购买成功更新tender表
		logger.info("用户:"+buyMap.get("loginId")+"购买产品："+buyMap.get("productCd")+"成功记录tender表结束");
		retMap.put("tenderAmt",ConvUtils.convToString(buyMap.get("txnAmt")));
		retMap.put("rsp_code","000000");

		return retMap;
	}*/


}
