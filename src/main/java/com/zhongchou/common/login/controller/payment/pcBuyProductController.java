package com.zhongchou.common.login.controller.payment;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.dto.UserTender;
import com.zhongchou.common.service.IBuyProductService;
import com.zhongchou.common.service.PcProductService;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.zhongzheng.cipher.MD5;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class pcBuyProductController extends BaseController {
	Logger logger=Logger.getLogger(pcBuyProductController.class);
	@Autowired
	private PcProductService productService;
	@Autowired
	private IBuyProductService buyProductService;
	@Autowired
	private IUserDao userDao;

	private static final String PRIVATEKEY = Config.getString("RSAPrivate");


	/**
	 * 判断时间是否过期
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "SCLogin/overTimeJudge.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView overTimeJudge(HttpServletRequest request,HttpServletResponse response){
		Map out = new HashMap();
		String coolingPeriodTime = "150000";//HHmmss
		if (ConvUtils.convToInt(Config.getString("coolingPeriod")) > 0) {
			coolingPeriodTime = ConvUtils.convToString(Config.getString("coolingPeriod"));
		}
		String endTransactionDate = DateUtils.convertDate2String(new Date(),"yyyyMMdd")+coolingPeriodTime;
		String startTransactionDate = DateUtils.convertDate2String(new Date(),"yyyyMMdd")+"090000";
		Date buyStartDate = DateUtils.convertString2Date(startTransactionDate,"yyyyMMddHHmmss");
		Date buyEndDate = DateUtils.convertString2Date(endTransactionDate,"yyyyMMddHHmmss");
		Date nowDate = new Date();
		if(nowDate.after(buyEndDate)
				||nowDate.before(buyStartDate)){
			return outparamMethod(response, out,"001","当前不在购买时间");
		}else{
			return outparamMethod(response, out,Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
		}
	}

	/**
	 * 初始订单信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "SCLogin/initOrderInfo.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView initOrderInfo(HttpServletRequest request,HttpServletResponse response){
		UserDto user = getUser();
		logger.info("pcBuyProductController.initOrderInfo         start"+user.getOidUserId());
		Map out = new HashMap();
		String productId = request.getParameter("productId");//产品id
		String loginId = user.getOidUserId();
		if(StringUtils.isEmpty(productId)){
			return outparamMethod(response, out,"002","产品id不可为空");
		}
		try {
			//查询产品认购金额信息
			Map tendermsg = productService.selprojectAmount(productId,loginId);
			String minAmount =  ConvUtils.convToString(tendermsg.get("minAmount"));//最低认购金额
			String addAmount =  ConvUtils.convToString(tendermsg.get("addAmount"));//追加金额
			String maxAmount =  ConvUtils.convToString(tendermsg.get("maxAmount"));//追加金额
			String leadName =  ConvUtils.convToString(tendermsg.get("userName"));//领投人信息
			String productprospectus =  ConvUtils.convToString(tendermsg.get("productprospectus"));//募集说明书
			String investmentconfirmation = ConvUtils.convToString(tendermsg.get("investmentconfirmation"));//投资确认书
			Row proMap = productService.selRiskLvlToBuy(productId);//项目产品信息
			if(!StringUtils.isEmpty(user.getRiskLvl())){
				out.put("userLvl",user.getRiskLvl());
			}else{
				out.put("userLvl","");
			}
			if(!StringUtils.isEmpty(ConvUtils.convToString(proMap.get("RISK_LVL_DES")))){
				out.put("proLvl",ConvUtils.convToString(proMap.get("RISK_LVL_DES")));
			}else{
				out.put("proLvl","");
			}
			String projectTitle = ConvUtils.convToString(proMap.get("PROJECT_MAIN_TITLE"));
			String summarytext = ConvUtils.convToString(proMap.get("SUMMARYTEXT"));
			String projectImg = ConvUtils.convToString(proMap.get("APP_LIST_IMG"));
			String userName = user.getUserRealName();//用户姓名
			String idCard = user.getIdCard();//用户证件号
			idCard = StringUtil.changeIdCard(idCard);

			out.put("minAmount",minAmount);
			out.put("productprospectus",productprospectus);
			out.put("addAmount",addAmount);
			out.put("leadName",leadName);
			out.put("maxAmount",maxAmount);
			out.put("projectImg",projectImg);
			out.put("projectTitle",projectTitle);
			out.put("summarytext",summarytext);
			out.put("userName",userName);
			out.put("idCard",idCard);
			out.put("investmentconfirmation", investmentconfirmation);
			return outparamMethod(response, out,Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
		} catch (Exception e) {
			return outparamMethod(response, out,"003","初始订单信息异常");
		}
	}
	/**
	 * 用户创建订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "SCLogin/createOrder.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView createOrder(HttpServletRequest request,HttpServletResponse response){
		UserDto user = getUser();
		logger.info("buyProductController.createOrder         start"+user.getOidUserId());
		Map out = new HashMap();
		if(!"1".equals(user.getTcFlag())){//未绑卡
			out.put("tipCardFlag", "0");
		}else{
			out.put("tipCardFlag", "1");
		}
		String coolingPeriodTime = "150000";//HHmmss
		if (ConvUtils.convToInt(Config.getString("coolingPeriod")) > 0) {
			coolingPeriodTime = ConvUtils.convToString(Config.getString("coolingPeriod"));
		}
		String endTransactionDate = DateUtils.convertDate2String(new Date(),"yyyyMMdd")+coolingPeriodTime;
		String startTransactionDate = DateUtils.convertDate2String(new Date(),"yyyyMMdd")+"090000";
		Date buyStartDate = DateUtils.convertString2Date(startTransactionDate,"yyyyMMddHHmmss");
		Date buyEndDate = DateUtils.convertString2Date(endTransactionDate,"yyyyMMddHHmmss");
		Date nowDate = new Date();
		if(nowDate.after(buyEndDate)
				||nowDate.before(buyStartDate)){
			return outparamMethod(response, out,"001","当前不在购买时间");
		}

		String productId = request.getParameter("productId");//产品id
		String amount = request.getParameter("amount");//购买金额
		String loginId = user.getOidUserId();
		try {
			Double.parseDouble(amount);
		} catch (Exception e) {
			return outparamMethod(response, out,"002","购买金额有误");
		}
		if(!StringUtil.isAllNumber(amount)){
			return outparamMethod(response, out,"003","投资金额最小单位为元");
		}
		//查询产品认购金额信息
		Map tendermsg = productService.selprojectAmount(productId,user.getOidUserId());
		String minAmount =  ConvUtils.convToString(tendermsg.get("minAmount"));//最低认购金额
		String addAmount =  ConvUtils.convToString(tendermsg.get("addAmount"));//追加金额
		String maxAmount =  ConvUtils.convToString(tendermsg.get("maxAmount"));//追加金额
		Double minusAmount = Double.parseDouble(amount)-Double.parseDouble(minAmount);//用户投资金额减最低投资金额
		Double maxAmt = Double.parseDouble(amount)-Double.parseDouble(maxAmount);//用户投资金额减最低投资金额
		if(minusAmount<0){
			return outparamMethod(response, out,"004","投资金额小于最低投资金额");
		}
		if(maxAmt>0){
			return outparamMethod(response, out,"007","投资金额大于最高投资金额");
		}

		if(Double.parseDouble(addAmount)!=0&&minusAmount%Double.parseDouble(addAmount)!=0){
			return outparamMethod(response, out,"005","产品追加金额有误");
		}

		Map<String,Object> orderMap = new HashMap();
		try {
			UserTender userTender = new UserTender();
			userTender.setOidUserId(loginId);
			userTender.setOidPlatformProductsId(productId);
			userTender.setTenderAmount(Double.parseDouble(amount)*100);//金额（分）
			userTender.setTenderStatus("4");
			userTender.setTenderType("2");//投资类型 01:固收项目 02：股权
			userTender.setAutoStatus("1");//自动状态 0自动 1手动
			orderMap = buyProductService.createOrder(userTender);
			out.put("orderMap",orderMap);
			out.put("oidTenderId", orderMap.get("oidTenderId"));
			return outparamMethod(response, out,Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
		} catch (Exception e) {
			return outparamMethod(response, out,"006","创建订单异常");
		}
	}

	/**
	 * 查询用户订单待支付信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "SCLogin/waitOrderInfo.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView waitOrderInfo(HttpServletRequest request,HttpServletResponse response){
		Map out = new HashMap();
		UserDto user = getUser();
		logger.info("buyProductController.waitOrderInfo         start"+user.getOidUserId());
		String userId = user.getOidUserId();
		String tenderId = request.getParameter("tenderId");
		if(StringUtils.isEmpty(tenderId)){
			return outparamMethod(response, out,"001","订单号不可为空");
		}
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("userId",userId);
		paramMap.put("tenderId",tenderId);
		try {
			Map<String,Object> waitOrderInfo = buyProductService.getWaitOrderInfo(paramMap);
			Map<String,Object> displayOrderMap = (Map<String, Object>) waitOrderInfo.get("displayOrderMap");
			String bankCd = ConvUtils.convToString(displayOrderMap.get("bankCd"));
			if("0".equals(StringUtil.isBankSign(bankCd))||("1".equals(StringUtil.isBankSign(bankCd))&&"1".equals(user.getSignFlag()))){
				displayOrderMap.put("displayOrNot","1");//显示 单笔限额 今日剩余限额
			}else{
				displayOrderMap.put("displayOrNot","0");//不显示 单笔限额 今日剩余限额
			}
			out.put("waitOrderInfo",waitOrderInfo.get("displayOrderMap"));
			setSessionObj("sessionMap",waitOrderInfo.get("sessionMap"));
			return outparamMethod(response, out,Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
		} catch (Exception e) {
			logger.error(e);
			return outparamMethod(response, out,"002","查询用户订单待支付信息异常");
		}
	}

	//未绑卡先调绑卡接口1:绑卡验证码接口：bankCardManane.do ； 2：绑卡提交接口：bankCardMananeSubmit.do

	//支付获取中证短信验证码 sendShortMessage.do

	/**
	 * 确认支付
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "SCLogin/payOrderConfirm.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView payOrderConfirm(HttpServletRequest request,HttpServletResponse response){
		UserDto user = getUser();
		logger.info("pcbuyProductController.payOrderConfirm         start"+user.getOidUserId());
		Map out = new HashMap();

		String productId = request.getParameter("productId");//产品id
		String tenderId = request.getParameter("tenderId");//订单id
		String PaymentPwd = request.getParameter("paymentPwd");//支付密码
		String bankNoIndex = request.getParameter("bankNoIndex");//银行卡index
		//String verificationCode = request.getParameter("verificationCode");//验证码
		String amount = request.getParameter("amount");//购买金额
		Map<String,Object> userBankInfo = (Map<String, Object>) getSessionObj("sessionMap");//用户银行卡明文
		String shortMessageReqSsn = (String)getSessionObj("shortMessageReqSsn");//验证码流水号
		String loginId = user.getOidUserId();//用户ID
		if(!checkLoginSmsCodeToBuy()){
			return outparamMethod(response, out,"010","请先发送短信验证码");
		}
		String bankNo = "";
		String bankIndex = "";
		String bankName = "";
		String insDate = "";
		if(userBankInfo!=null&&!userBankInfo.isEmpty()){
			bankIndex = ConvUtils.convToString(userBankInfo.get("bankNoIndex"));
			if(bankIndex.equals(bankNoIndex)){
				bankNo = ConvUtils.convToString(userBankInfo.get("bankNo"));
				bankName = ConvUtils.convToString(userBankInfo.get("cardNm"));
			}
			insDate = ConvUtils.convToString(userBankInfo.get("insDate"));
		}
		if(!StringUtils.isEmpty(insDate)){
			long instime = DateUtils.convertString2Date(insDate, "yyyy-MM-dd HH:mm:ss").getTime();
			long nowtime = new Date().getTime();
			if(nowtime-instime>30*60*1000){
				return outparamMethod(response, out,"012","付款时间已过");
			}
		}
		if(StringUtils.isEmpty(bankNo)){
			return outparamMethod(response, out,"001","银行卡信息有误");
		}
		if(StringUtils.isEmpty(productId)){
			return outparamMethod(response, out,"002","产品ID不可为空");
		}
		/*if(StringUtils.isEmpty(verificationCode)){
			return outparamMethod(response, out,"003","验证码不可为空");
		}*/
		if(StringUtils.isEmpty(amount)){
			return outparamMethod(response, out,"004","购买金额不可为空");
		}
		if(StringUtils.isEmpty(tenderId)){
			return outparamMethod(response, out,"005","订单ID不可为空");
		}
		amount = amount.replace(",", "");
		try {
			Double.parseDouble(amount);
		} catch (Exception e) {
			return outparamMethod(response, out,"006","购买金额有误");
		}

		String resortPwd = "";
		try {
			resortPwd = RSAUtil.decryptByPrivate(PRIVATEKEY,PaymentPwd);
		} catch (Exception e) {
			logger.error("密码解密有误:"+e);
			return outparamMethod(response, out,"007","密码解密有误");
		}
		if(StringUtils.isEmpty(resortPwd)||StringUtils.isPasswordStrength(resortPwd)){
			return outparamMethod(response, out,"008","支付密码有误");
		}
		Map retMap = new HashMap();
		try {
			Map buyMap = new HashMap();
			buyMap.put("loginId", loginId);//用户id
			buyMap.put("productCd", productId);//产品id
			buyMap.put("tenderId", tenderId);//订单id
			buyMap.put("txnAmt", Double.parseDouble(amount)*100);//金额（分）
			buyMap.put("payTp", "1");//银行卡支付
			buyMap.put("authTp", "1");//验证方式 支付密码
			buyMap.put("cardNo", bankNo);//银行卡号
			buyMap.put("payPwd", MD5.encode(resortPwd));//支付密码
			/*buyMap.put("yzmSsn", shortMessageReqSsn);//验证码流水
			buyMap.put("yzmContent", verificationCode);//验证码
*/			buyMap.put("userIp", getRemoteAddr());//ip

			retMap = buyProductService.payOrderConfirm(buyMap);

		} catch (Exception e) {
			logger.error("-----认购异常:------\r\n"+e);
			return outparamMethod(response, out,"009","认购异常");
		}
		if("000000".equals(retMap.get("rsp_code"))){
			if(!"1".equals(user.getSignFlag())){//第一次购买未签约
				UserDto userDto = new UserDto();
				userDto.setOidUserId(user.getOidUserId());
				userDto.setSignFlag("1");
				if(userDao.updUser(userDto)>0){
					user.setSignFlag("1");
					setUser(user);
				}
			}
			clearSessionObj("sessionMap");
			clearSessionObj("shortMessageReqSsn");
			this.clearSessionObj(Constants.LOGIN_VERIFY_CODE_BUY);
			out.put("tenderAmt",ConvUtils.convToString(retMap.get("tenderAmt")));
			return outparamMethod(response, out,Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
		}else{
			return outparamMethod(response, out,"011",StringUtil.splitDesc(ConvUtils.convToString(retMap.get("rsp_desc"))));
		}
	}

}
