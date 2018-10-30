package com.zhongchou.common.loginOut.controller.regist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IBankService;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.service.IregistNextBindBankService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.zhongzheng.cipher.MD5;

@Controller
public class RegistNextBindBankController extends BaseController {
	String ZZbankCd = Config.getString("ZZbankCd");//中证金通支持的银行
	String privateKey = Config.getString("RSAPrivate");

	@Autowired
	private IBankService bankService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IregistNextBindBankService registNextBindBankService;

	@Autowired
	private IUserManageService userManageService;

	private static final String idTp = "1";//证件类型默认身份证。


	/**
	 * 壹理财老用户注册第二步注册返显信息
	 * @param
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/initRegistBank.do", method ={ RequestMethod.POST,RequestMethod.GET})
	protected ModelAndView initRegistBank(HttpServletRequest request,
			HttpServletResponse response,Model model) {
		Map<String,Object> out = new HashMap<String,Object>();
		// 取得登陆账号
		String loginId = null;
		UserDto user = getUser();
		// 如果不是伪登陆状态，那就从Session取
		if(user != null){
			loginId = user.getOidUserId();
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "新用户");
			JsonUtil.writeJson(response,out);
			return null;
		}
		Map userMap = bankService.selBankSmg(loginId);
		//返显实名信息
		userMap.put("userName", user.getUserRealName());
		userMap.put("idCard", user.getIdCard());
		userMap.put("cardNo", "");
		/*if(userMap.isEmpty()){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "没有信息");
			JsonUtil.writeJson(response,out);
			return null;
		}*/
		out.put("registMsg", userMap);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
}

	/**
	 * 用户注册第二步获取验证码（绑卡）
	 * @param
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/CheckBankCode.do", method ={ RequestMethod.POST,RequestMethod.GET})
	protected ModelAndView checkBank(HttpServletRequest request,HttpServletResponse response,Model model) {

		String loginId = null;

		UserDto user = getUser();
		// 如果不是伪登陆状态，那就从Session取
		loginId = user.getOidUserId();

		//校验登录账号
		if (StringUtils.isEmpty(loginId)) {
			outParam(response, "001", "登录账号不能为空");
			return null;
		}

		//用户真实姓名
		String userName = (String)request.getParameter("bankUserName");
		// 身份证号
		String idCard = (String)request.getParameter("bankIdCard");
		// 银行卡号
		String bankCode = (String) request.getParameter("bankCode");
		// 银行预留手机号
		String mobile = (String) request.getParameter("bankMobile");
		// 身份证号校验
		if (StringUtils.isEmpty(idCard) || !StringUtil.isIdCard(idCard)) {
			outParam(response, "002", "身份证号格式错误");
			return null;
		}
		// 校验用户真实姓名
		if (StringUtils.isEmpty(userName)) {
			outParam(response, "003", "用户真实姓名不可为空");
			return null;
		}
		// 校验银行卡号格式
		if (StringUtils.isEmpty(bankCode)) {
			outParam(response, "004", "银行卡号不可为空");
			return null;
		}
		// 校验银行预留手机号格式
		if (StringUtils.isEmpty(mobile) || !StringUtils.isMobileNum(mobile)) {
			outParam(response, "005", "银行预留手机号格式错误");
			return null;
		}
		Map bankFlag =bankService.selSupportBank(bankCode);
		if(bankFlag.isEmpty()){
			outParam(response, "006", "不支持该银行");
			return null;
		}else{
			boolean flag = true;
			String[] bankCdList = ZZbankCd.split(",");
			for (int i = 0; i < bankCdList.length; i++) {
		        if(bankCdList[i].equals(bankFlag.get("bankCd"))){
		        	flag=false;
		        	break;
		        }
		    }
			if(flag){//在所支持的银行
				outParam(response, "006", "不支持该银行");
				return null;
			}
		}
		Map<String, Object> codeMap = new HashMap<String, Object>();
		codeMap.put("loginId", loginId);
		codeMap.put("idTp", idTp);
		codeMap.put("userName", userName);
		codeMap.put("idCard", idCard);
		codeMap.put("bankCode", bankCode);
		codeMap.put("mobile", mobile);
		// 调用中证接口取得原请求流水
		logger.info("---------调用中证接口取得原请求流水开始---------------");
		Map<String, Object> map = registNextBindBankService.getBankSms(codeMap);
		logger.info("---------调用中证接口取得原请求流水结束--------------流水号："+(String)map.get("BankReqSsn")+";message:"+(String)map.get("rsp_desc"));
		if ("000000".equals(map.get("rsp_code"))) {
			this.setSessionObj("BankReqSsn",
					(String) map.get("BankReqSsn"));// 将流水号放入session
			this.setSessionObj("userName", userName);
			this.setSessionObj("idCard", idCard);
			this.setSessionObj("bankCode", bankCode);
			this.setSessionObj("mobile", mobile);
			outParam(response, Constants.RET_SUCCESS_CODE,
					Constants.RET_SUCCESS_MSG);
			return null;
		}else if("046001".equals(map.get("rsp_code"))){
			outParam(response, "011", "该银行卡已被绑定,请更换银行卡");
			return null;
		}else  {
			outParam(response, "010", StringUtil.splitDesc(ConvUtils.convToString(map.get("rsp_desc"))));
			return null;
		}
	}
	/**
	 * 用户注册第二步注册提交（绑卡）
	 * @param
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/registNextConfirm.do", method ={ RequestMethod.POST,RequestMethod.GET})
	protected ModelAndView registNextSubmit(HttpServletRequest request,
			HttpServletResponse response,Model model) {
		//获取登录用户ip
		String userIp = getRemoteAddr();
		UserDto user = getUser();
		// 如果不是伪登陆状态，那就从Session取
		String loginId = user.getOidUserId();
		// 用户真实姓名
		String userName = (String) this.getSessionObj("userName");
		// 身份证号
		String idCard = (String) this.getSessionObj("idCard");
		// 银行卡号
		String bankCode = (String) this.getSessionObj("bankCode");
		// 银行预留手机号
		String mobile = (String) this.getSessionObj("mobile");
		// 获取流水号
		String BankReqSsn = (String) this.getSessionObj("BankReqSsn");
		// 短信验证码
		String verificationCode = (String) request.getParameter("verificationCode");
		// 支付密码
		String payPassword = (String) request.getParameter("payPassword");

		// 验证码参数验证
		if (StringUtils.isEmpty(userName)) {
			outParam(response, "001", "请先发送短信验证码");
			return null;
		}
		// 验证码参数验证
		if (StringUtils.isEmpty(verificationCode)) {
			outParam(response, "002", "验证码不可为空");
			return null;
		}
		// 支付密码参数验证
		if (StringUtils.isEmpty(payPassword)) {
			outParam(response, "003", "支付密码不可为空");
			return null;
		}
		String resortPwd = null;
		try {
			resortPwd = RSAUtil.decryptByPrivate(privateKey, payPassword);
		} catch (Exception e) {
			e.printStackTrace();
			outParam(response, "004", "支付密码解密有误");
			return null;
		}
		String regex = "^\\d{6}$";
		if (!StringUtils.match(regex,resortPwd)){
			outParam(response, "005", "支付密码格式错误");
			return null;
		}

		// 封装请求参数
		Map<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("userIp", userIp);// 用户ip
		parameter.put("loginId", loginId);// 用户id
		parameter.put("userName", userName);// 真实姓名
		parameter.put("idTp", idTp);// 证件类型
		parameter.put("idCard", idCard);// 身份证号
		parameter.put("bankCode", bankCode);// 银行卡号
		parameter.put("mobile", mobile);// 银行预留手机号
		parameter.put("mobileNo", mobile);// 银行预留手机号
		parameter.put("BankReqSsn", BankReqSsn);// 流水号
		parameter.put("payPassword", resortPwd);// 支付密码
		parameter.put("verificationCode", verificationCode);// 短信验证码
		parameter.put("oidAccountBindBankId", ConvUtils.convToString(userManageService.nextSeq("OID_ACCOUNT_BIND_BANK_ID")));//银行管理ID
		parameter.put("tcFlag", "1");// 是否绑卡
		parameter.put("idCardVerifyFlg", "1");// 身份证验证标示用于更新user_detail表
		parameter.put("mainFlg", "1");// 注册绑卡位主卡  主卡标识
		logger.info("---------调取中证接口并查询用户绑卡信息开始---------------");
		Map<String,Object> selTiedBankCard = bankService.queryTiedBankCard(parameter);
		logger.info("---------调取中证接口并查询用户绑卡信息结束---------------tiedBankCard："+selTiedBankCard.size());
		if(((List)selTiedBankCard.get("cardList")).size()>0){
			outParam(response, "010", "已绑过卡，请不要重复绑定");
			return null;
		}
		// 调取中证接口并保存银行卡信息
		logger.info("---------调取中证接口并保存银行卡信息开始---------------");
		Map<String,Object> tiedBankCard = registNextBindBankService.getTiedBankCard(parameter);
		logger.info("---------调取中证接口并保存银行卡信息结束---------------tiedBankCard："+tiedBankCard);
		if ("000000".equals((String)tiedBankCard.get("rsp_code"))) {

			this.clearSessionObj("userName");
			this.clearSessionObj("idCard");
			this.clearSessionObj("bankCode");
			this.clearSessionObj("mobile");
			this.clearSessionObj("BankReqSsn");
			Map<String,Object> out = new HashMap<String,Object>();
			//是否需要网银签约 0：否  1：是
			String isSign = StringUtil.isBankSign(ConvUtils.convToString(tiedBankCard.get("bankCd")));
			if(user!=null){
				user.setTcFlag("1");
				user.setUserRealName(userName);
				user.setIdCard(idCard);
				user.setVerifyFlag("1");
				//不需要签约的银行直接显示已签约
				if("0".equals(isSign))
					user.setSignFlag("1");
				try {
					user.setUserPayPwd(MD5.encode(resortPwd));
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setUser(user);
			}
			out.put("payMoblie", mobile);
			out.put("userName", userName);
			out.put("bankCode", tiedBankCard.get("bankCd"));
			out.put("isSign",isSign);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else if("051002".equals(tiedBankCard.get("rsp_code"))){
			outParam(response, "007", "证件号已存在");
			return null;
		} else if("051011".equals(tiedBankCard.get("rsp_code"))){
			outParam(response, "008", "银行卡已被绑定");
			return null;
		}else if("049997".equals(tiedBankCard.get("rsp_code"))){
			outParam(response, "009", "此银行卡占不支持");
			return null;
		}
		else {
			outParam(response, "006", StringUtil.splitDesc(ConvUtils.convToString(tiedBankCard.get("rsp_desc"))));
			return null;
		}
	}


	private void outParam(HttpServletResponse response, String code,
			String message) {
		Map<String,Object> out = new HashMap<String,Object>();
		out.put(Constants.RET_CODE, code);
		out.put(Constants.RET_MSG, message);
		JsonUtil.writeJson(response,out);
	}
	/**
	 * 查询中证支持银行
	 * @param
	 */
	@RequestMapping("/SCLogin/selSupportBankList.do")
	public ModelAndView selBankList(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, Object> out = new HashMap<String, Object>();
		String[] bankCdList = ZZbankCd.split(",");
		Set retSet = new HashSet();
		List selSupportBankList =bankService.selSupportBankList();
		for (int i = 0; i < bankCdList.length; i++) {
			for (int j = 0; j < selSupportBankList.size(); j++) {
				if(bankCdList[i].equals(((Map)selSupportBankList.get(j)).get("bankId"))){
					retSet.add(bankCdList[i]);
					continue;
				}
			}
		}
		out.put("bankCdList",retSet);
//更改后--------------------------------
		List retList = new ArrayList<>();
		for (int i = 0; i < bankCdList.length; i++) {
			for (int j = 0; j < selSupportBankList.size(); j++) {
				Map bankList = (Map)selSupportBankList.get(j);
				if(bankCdList[i].equals(bankList.get("bankId"))){
					Map retMap = new HashMap();
					retMap.put("singleLimitAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(bankList.get("singleLimitAmt"))));//单笔限额
					retMap.put("dayLimitAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(bankList.get("dayLimitAmt"))));//单日限额
					retMap.put("bankNm", StringUtil.supportBank(ConvUtils.convToString(bankList.get("bankId"))));//银行名
					retMap.put("bankId", bankList.get("bankId"));//银行cd
					String isBankSign = StringUtil.isBankSign(ConvUtils.convToString(bankList.get("bankId")));
					retMap.put("isBankSign",isBankSign);//根据银行cd判断该行是否需要网银签约 0：否  1：是
					retList.add(retMap);
					continue;
				}
			}
		}
		out.put("bankCdList",retList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}
}
