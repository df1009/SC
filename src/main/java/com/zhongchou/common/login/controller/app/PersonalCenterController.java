package com.zhongchou.common.login.controller.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IMyProjectService;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.service.IUserTenderService;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;

@Controller
public class PersonalCenterController extends BaseController {
	@Autowired
	private IUserTenderService userTenderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private IUserService userService;
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonalCenterController.class);
	/**
	 *个人中心查询用户投资总额和投资数量
	 */
	@RequestMapping(value="/SCLogin/selPersonalCenter.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView sendEmailToUser(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> out = new HashMap<>();
		UserDto user = getUser();
		out.put("allTenderAmount", "***");
		out.put("allTenderCount", "***");
		if("1".equals(user.getIsDisplay())){
			Map tenderMsg = userTenderService.selPresonMsgToApp(user.getOidUserId());
			out.put("allTenderAmount", tenderMsg.get("allTenderAmount"));
			out.put("allTenderCount", tenderMsg.get("allTenderCount"));
		}
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response, out);
		return null;
	}

	/**
	 *个人中心设置敏感信息是否可见
	 */
	@RequestMapping(value="/SCLogin/setSensitiveInfo.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView setSensitiveInfo(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> out = new HashMap<>();
		String flag = request.getParameter("flag");
		if(!"0".equals(flag)
				&&!"1".equals(flag)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response,out);
			return null;
		}
		UserDto user = getUser();
		if(userService.setSensitiveInfo(user.getOidUserId(),flag)>0){
			user.setIsDisplay(flag);
			setUser(user);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "系统异常");
		}
		JsonUtil.writeJson(response, out);
		return null;

	}

	/**
	 *查询用户用户认证投资人
	 */
	@RequestMapping(value="/SCLogin/selCertifiedInvestor.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView selCertifiedInvestor(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> out = new HashMap<>();
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		String proId = request.getParameter("proId");
		UserDto user = getUser();
		String certifiedInvestor = user.getCertifiedInvestor();
		if(StringUtils.isEmpty(certifiedInvestor)){//未完成认证投资人
			out.put(Constants.RET_CODE, "001");
			out.put("realName", user.getUserRealName());
			out.put("idCard", user.getIdCard());
			JsonUtil.writeJson(response, out);
			return null;
		}
		Map proMap = productService.selRiskLvlToBuy(proId);
		if(proMap.size() == 0){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "产品id有误");
			JsonUtil.writeJson(response, out);
			return null;
		}
		returnAmount(proMap,out,user.getOidUserId(),proId);
		int userLvl = Integer.parseInt(user.getRiskLvl());
		int proLvl = Integer.parseInt((String)proMap.get("RISK_LVL"));
		if(userLvl<proLvl){
			out.put("userLvl", userLvl);
			out.put("proLvl", proLvl);
			if(!isApp()){
				//推荐符合用户风险的产品
				List proList = productService.userRiskLvlAccordPro(user.getOidUserId());
				out.put("proList", proList);
			}
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "风险提示");
			JsonUtil.writeJson(response, out);
			return null;
		}
		JsonUtil.writeJson(response, out);
		return null;

	}

	/**
	 *用户设置认证投资人
	 */
	@RequestMapping(value="/SCLogin/setCertifiedInvestor.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView setCertifiedInvestor(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> out = new HashMap<>();
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		String certifiedInvestor = request.getParameter("investor");
		String proId = request.getParameter("proId");
		if(!certifiedInvestor.equals("1,2")
				&&!certifiedInvestor.equals("1,3")
				&&!certifiedInvestor.equals("1,2,3")){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "investor error");
			JsonUtil.writeJson(response, out);
			return null;
		}
		UserDto user = getUser();
		userService.setcertifiedInvestor(user.getOidUserId(),certifiedInvestor);
		user.setCertifiedInvestor(certifiedInvestor);
		this.setUser(user);
		Map proMap = productService.selRiskLvlToBuy(proId);
		if(proMap.size() == 0){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "产品id有误");
			JsonUtil.writeJson(response, out);
			return null;
		}
		returnAmount(proMap,out,user.getOidUserId(),proId);
		int userLvl = Integer.parseInt(user.getRiskLvl());
		int proLvl = Integer.parseInt((String)proMap.get("RISK_LVL"));
		if(userLvl<proLvl){
			out.put("userLvl", userLvl);
			out.put("proLvl", proLvl);
			if(!isApp()){
				//推荐符合用户风险的产品
				List proList = productService.userRiskLvlAccordPro(user.getOidUserId());
				out.put("proList", proList);
			}
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "风险提示");
			JsonUtil.writeJson(response, out);
			return null;
		}
		JsonUtil.writeJson(response, out);
		return null;
	}

	@SuppressWarnings("unchecked")
	public void returnAmount(Map proMap,Map out,String userId,String proId){
		//查询用户是否投资过该产品
		String proStatus = (String)proMap.get("PLATFORM_PROJECTS_ST");
		if(userTenderService.selTenderProAmount(userId, proId)>0){//购买过该产品
			if("1".equals(proStatus)){
				out.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(proMap.get("MIN_SUBS_AMT"))));
			}else if("2".equals(proStatus)){
				out.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(proMap.get("MIN_BIDS_AMT"))));
			}
		}else{//第一次购买
			out.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(proMap.get("FIRST_MIN_BUY"))));
		}
		Map quotaMap = userTenderService.selRemainingQuota(userId,proId);
		//用户银行卡当日的剩余
		int surplusAmt = (int)quotaMap.get("dayLimitAmt");
		//用户银行卡当单笔限额
		int singleLimit = ConvUtils.convToInt(quotaMap.get("singleLimit"));
		//最大可投金额
		int maxSubsAmt = Integer.parseInt(StringUtil.changeSalesQuota(ConvUtils.convToString(proMap.get("MAX_SUBS_AMT"))));
		//最小追投金额
		int addAmount = 1;
		if("1".equals(proStatus)){
			addAmount = Integer.parseInt(StringUtil.changeSalesQuota(ConvUtils.convToString(proMap.get("SUBS_ADD_AMT"))));
		}else if("2".equals(proStatus)){
			addAmount = Integer.parseInt(StringUtil.changeSalesQuota(ConvUtils.convToString(proMap.get("BIDS_ADD_AMT"))));
		}
		addAmount=addAmount==0?1:addAmount;
		//剩余可投金额
		//String salesQuota = ConvUtils.convToString(proMap.get("SURPLUS_SALES_QUOTA"));
		//int surplusSalesQuota = Integer.parseInt(StringUtil.changeSalesQuota(salesQuota));
		//int maxAmount = surplusAmt;
		/*if(!"-1".equals(salesQuota)){
			maxAmount = surplusAmt<maxSubsAmt?surplusAmt:surplusSalesQuota;
		}*/
		/*if(maxSubsAmt !=0){
			maxAmount = maxAmount<maxSubsAmt?maxAmount:maxSubsAmt;
		}*/
		out.put("maxAmount", String.valueOf(maxSubsAmt));//最大认购额度
		out.put("addAmount", String.valueOf(addAmount));//追加金额
		out.put("singleLimit", String.valueOf(singleLimit));//单笔限额
		out.put("surplusAmt", String.valueOf(surplusAmt));//单日剩余额度
		//out.put("addAmount", String.valueOf("100"));
	}
}
