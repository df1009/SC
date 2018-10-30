package com.zhongchou.common.login.controller.payment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.login.controller.personalCenter.MyProjectController;
import com.zhongchou.common.service.IBankService;
import com.zhongchou.common.service.IBuyProductService;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.zhongzheng.cipher.MD5;

@Controller
public class buyProductController extends BaseController {
	Logger logger=Logger.getLogger(buyProductController.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private IBankService bankService;
	@Autowired
	private IBuyProductService buyProductService;
	@Autowired
	private IUserDao userDao;

	String privateKey = Config.getString("RSAPrivate");
	//初始购买信息
	@RequestMapping(value = "SCLogin/initBuy.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView initProductList(HttpServletRequest request,HttpServletResponse response,Model model){
		UserDto user = getUser();
		logger.info("buyProductController.initProductList         start"+user.getOidUserId());
		Map out = new HashMap();
		String tenderAmount = request.getParameter("tenderAmount");//投资金额
		String productId = request.getParameter("productId");//产品id
		if(StringUtils.isEmpty(productId)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!StringUtil.isAllNumber(tenderAmount)){
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "投资金额最小单位为元");
			JsonUtil.writeJson(response,out);
			return null;
		}

		//查询产品认购金额信息
		Map tendermsg = productService.selprojectAmount(productId,user.getOidUserId());
		String minAmount =  ConvUtils.convToString(tendermsg.get("minAmount"));//最低认购金额
		String addAmount =  ConvUtils.convToString(tendermsg.get("addAmount"));//追加金额
		Double minusAmount = Double.parseDouble(tenderAmount)-Double.parseDouble(minAmount);//用户投资金额减最低投资金额
		if(minusAmount<0){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "投资金额小于最低投资金额");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(minusAmount%Double.parseDouble(addAmount)!=0){
			out.put(Constants.RET_CODE, "006");
			out.put(Constants.RET_MSG, "产品追加金额有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
			logger.info("查询用户:"+user.getOidUserId()+"绑卡信息   开始");
			//用户绑定的银行卡信息
			Map bankListMap = bankService.selBankCardList(user.getOidUserId());
			List bankList = (List)bankListMap.get("bankList");
			List userBank = (List)bankListMap.get("userBank");
			setSessionObj("uerBankList", userBank);//用户银行卡明文
			//查询改用户主卡代扣限额
			String bankId = "";
			for (int i = 0; i < bankList.size(); i++) {
				Map bankMap = (Map)bankList.get(i);
				if("1".equals(bankMap.get("isDefault"))){
					bankId = (String)bankMap.get("bankCd");
					logger.info("用户:"+user.getOidUserId()+"主卡信息(行别:"+bankMap.get("bankCd")
							+" 卡号："+bankMap.get("cardNoMask")
							+" 卡名："+bankMap.get("cardNm")
							+")");
				}
			}
			List BankQuotaList = bankService.selBankQuota(bankId);
			out.put("BankQuotaList",BankQuotaList);
			out.put("bankList",bankList);
			out.put("productcontracts",tendermsg.get("productcontracts"));
			out.put("productprospectus",tendermsg.get("productprospectus"));
			out.put("investmentconfirmation",tendermsg.get("investmentconfirmation"));
			out.put("productsignbook",tendermsg.get("productsignbook"));

			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
	}

	//查询银行卡列表
	@RequestMapping(value = "SCLogin/selBankList.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView selBankList(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		UserDto user = getUser();
		Map bankListMap = bankService.selBankCardList(user.getOidUserId());
		List bankList = (List)bankListMap.get("bankList");
		List userBank = (List)bankListMap.get("userBank");
		setSessionObj("uerBankList", userBank);//用户银行卡明文
		//查询改用户主卡代扣限额
		String bankId = "";
		for (int i = 0; i < bankList.size(); i++) {
			Map bankMap = (Map)bankList.get(i);
			if("1".equals(bankMap.get("isDefault"))){
				bankId = (String)bankMap.get("bankCd");
			}
		}
		List BankQuotaList = bankService.selBankQuota(bankId);
		out.put("BankQuotaList",BankQuotaList);
		out.put("bankList",bankList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	//查询银行代扣限额
	@RequestMapping(value = "SCLogin/selBankQuota.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView selBankQuota(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		String bankId = request.getParameter("bankId");//银行代码
		List BankList = bankService.selBankQuota(bankId);
		out.put("BankQuotaList",BankList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	//购买
	@RequestMapping(value = "SCLogin/buyProduct.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView buyProduct(HttpServletRequest request,HttpServletResponse response,Model model){
		UserDto user = getUser();
		logger.info("buyProductController.buyProduct         start"+user.getOidUserId());
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
			out.put(Constants.RET_CODE,"008");
			out.put(Constants.RET_MSG, "当前时间不在购买时间");
			JsonUtil.writeJson(response,out);
			return null;
		}


		String productId = request.getParameter("productId");//产品id
		String PaymentPwd = request.getParameter("paymentPwd");//支付密码
		String bankNoIndex = request.getParameter("bankNoIndex");//银行卡index
		String verificationCode = request.getParameter("verificationCode");//验证码
		String amount = request.getParameter("amount");//购买金额
		int buyAmount=0;
		String loginId = user.getOidUserId();
		List userBankList = (List)getSessionObj("uerBankList");//用户银行卡明文
		String shortMessageReqSsn = (String)getSessionObj("shortMessageReqSsn");//验证码流水号
		String bankNo = "";
		String bankIndex = "";
		String bankName = "";
		String bankCd = "";
		logger.info("用户："+user.getOidUserId()+"有效银行卡个数"+userBankList.size());
		for (int i = 0; i < userBankList.size(); i++) {
			bankIndex = ConvUtils.convToString(((Map)userBankList.get(i)).get("bankNoIndex"));
			if(bankIndex.equals(bankNoIndex)){
				bankNo = ConvUtils.convToString(((Map)userBankList.get(i)).get("bankNo"));
				bankName = ConvUtils.convToString(((Map)userBankList.get(i)).get("cardNm"));
				bankCd = ConvUtils.convToString(((Map)userBankList.get(i)).get("bankCd"));
			}
		}
		if(StringUtils.isEmpty(bankNo)
				||StringUtils.isEmpty(productId)
				||StringUtils.isPasswordStrength(PaymentPwd)
				||StringUtils.isEmpty(verificationCode)
				||StringUtils.isEmpty(amount)){
			out.put(Constants.RET_CODE,"001");
			out.put(Constants.RET_MSG, "格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		try {
			Double.parseDouble(amount);
		} catch (Exception e) {
			out.put(Constants.RET_CODE,"002");
			out.put(Constants.RET_MSG, "购买金额有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!StringUtil.isAllNumber(amount)){
			out.put(Constants.RET_CODE, "008");
			out.put(Constants.RET_MSG, "投资金额最小单位为元");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//查询产品认购金额信息
		Map tendermsg = productService.selprojectAmount(productId,user.getOidUserId());
		String minAmount =  ConvUtils.convToString(tendermsg.get("minAmount"));//最低认购金额
		String addAmount =  ConvUtils.convToString(tendermsg.get("addAmount"));//追加金额
		Double minusAmount = Double.parseDouble(amount)-Double.parseDouble(minAmount);//用户投资金额减最低投资金额
		if(minusAmount<0){
			out.put(Constants.RET_CODE, "006");
			out.put(Constants.RET_MSG, "投资金额小于最低投资金额");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(minusAmount%Double.parseDouble(addAmount)!=0){
			out.put(Constants.RET_CODE, "007");
			out.put(Constants.RET_MSG, "产品追加金额有误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		String resortPwd = "";
		try {
			resortPwd = RSAUtil.decryptByPrivate(privateKey,PaymentPwd);
		} catch (Exception e) {
			e.printStackTrace();
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "密码解密有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		Map retMap = new HashMap();
		try {
		Map buyMap = new HashMap();
		buyMap.put("loginId", loginId);//用户id
		buyMap.put("productCd", productId);//产品id
		buyMap.put("txnAmt", Double.parseDouble(amount)*100);//金额（分）
		buyMap.put("payTp", "1");//银行卡支付
		buyMap.put("authTp", "12");//验证方式  支付密码和短信同时验证码
		buyMap.put("cardNo", bankNo);//银行卡号
		buyMap.put("payPwd", MD5.encode(resortPwd));//支付密码
		buyMap.put("yzmSsn", shortMessageReqSsn);//验证码流水
		buyMap.put("yzmContent", verificationCode);//验证码
		buyMap.put("userIp", getItemsPerPage());//ip地址
		buyMap.put("tenderType", "2");//投资类型 01:固收项目 02：股权
		buyMap.put("autoStatus", "1");//自动状态 0自动 1手动
		buyMap.put("requestNo", "");//托管流水号
		buyMap.put("userIp", getRemoteAddr());//ip

		retMap = buyProductService.buyProduct(buyMap);
		} catch (Exception e) {
			out.put(Constants.RET_CODE,"004");
			out.put(Constants.RET_MSG, "认购异常");
			JsonUtil.writeJson(response,out);
			e.printStackTrace();
			return null;
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
			clearSessionObj("uerBankList");
			clearSessionObj("shortMessageReqSsn");
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else if("092180".equals(retMap.get("rsp_code"))){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "请先发送短信验证码");
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			out.put("bankName", bankName);
			out.put("bankCd", bankCd);
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, StringUtil.splitDesc(ConvUtils.convToString(retMap.get("rsp_desc"))));
			JsonUtil.writeJson(response,out);
			return null;
		}
	}

}
