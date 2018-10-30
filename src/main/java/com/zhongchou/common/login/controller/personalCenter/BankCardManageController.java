package com.zhongchou.common.login.controller.personalCenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.zhongchou.common.service.IMyProjectService;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.util.UserTokenUtil;

@Controller
public class BankCardManageController extends BaseController {
	String ZZbankCd = Config.getString("ZZbankCd");//中证金通支持的银行
	@Autowired
	private IMyProjectService myProjectService;
	@Autowired
	private IUserManageService userManageService;

	@Autowired
	private IBankService bankService;
	private static Logger logger = LoggerFactory.getLogger(BankCardManageController.class);
	/**
	 * 查询用户所有有效银行卡
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/SCLogin/queryBankCard.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView queryBankCard(HttpServletRequest request,HttpServletResponse response) {
		UserDto user = getUser();
		String loginId = user.getOidUserId();
		Map<String,Object> outParam = new HashMap<String,Object>();
		List<Map<String, String>> bankList = new ArrayList<Map<String, String>>();
		int haveCardNum = 0;
		if(!StringUtils.isEmpty(user.getVerifyFlag())){
			logger.info("-------查询用户所有有效银行卡开始----------loginId:"+loginId);
			List<Map<String, String>> bankCards = myProjectService.queryBankCard(loginId);
			logger.info("-------查询用户所有有效银行卡结束----------");
			haveCardNum = bankCards.size();
			if(!bankCards.isEmpty()){
				for (Map<String, String> map : bankCards) {
					Map<String,String> paramMap = new HashMap<>();
					//将银行卡的hash放到session在用户取消卡时用来获取要取消的银行卡号
					this.setSessionObj((String)map.get("bankCardHash"), (String)map.get("cardNo"));
					//隐藏银行卡号中间数字
					String bankCode = (String)map.get("cardNo");
					String cardNum = StringUtil.hideCardNum(bankCode);
					paramMap.put("bankCode", cardNum);
					paramMap.put("bankName", (String)map.get("bankName"));
					paramMap.put("bankCardHash", (String)map.get("bankCardHash"));
					paramMap.put("bankCd", (String)map.get("bankCd"));
					bankList.add(paramMap);
				}
			}
		}
		int signFlag = user.getSignFlag().equals("1")?1:0;
		outParam.put("bankList", bankList);
		outParam.put("signFlag", signFlag);
		outParam.put("haveCardNum",haveCardNum);
		outParam.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		outParam.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response, outParam);
		return null;

	}
	/**
	 * 绑定银行卡获取验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/SCLogin/bankCardManane.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView bankCardManane(HttpServletRequest request,
			HttpServletResponse response) {
		//获取入参
		String bankCode = (String) request.getParameter("bankCode");
		String mobile = (String) request.getParameter("bankMobile");
		UserDto user = getUser();
		String loginId = user.getOidUserId();
		//封装入参
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loginId", loginId);
		paramMap.put("cardNo", bankCode);
		paramMap.put("mobileNo", mobile);
		//用于输出的map
		Map<String, String> out = new HashMap<String, String>();
		// 校验银行卡号格式
		if (StringUtils.isEmpty(bankCode)) {
			out = outParam("002", "银行卡号不可为空");
			JsonUtil.writeJson(response, out);
			return null;
		}
		// 校验银行预留手机号格式
		if (StringUtils.isEmpty(mobile) || !StringUtils.isMobileNum(mobile)) {
			out = outParam("003", "银行预留手机号格式错误");
			JsonUtil.writeJson(response, out);
			return null;
		}

		Map bankFlag =bankService.selSupportBank(bankCode);
		if(bankFlag.isEmpty()){
			out = outParam("008", "不支持该银行");
			JsonUtil.writeJson(response, out);
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
				out = outParam("009", "不支持该银行");
				JsonUtil.writeJson(response, out);
				return null;
			}
		}
		logger.info("---------银行卡管理添加银行卡获取验证码开始---------loginId:"+loginId+"---bankCode:"+bankCode+"----mobile:"+mobile);
		Map<String, Object> map = myProjectService
				.checkBankCardAvalid(paramMap);
		logger.info("---------银行卡管理添加银行卡获取验证码结束---------------"
				+ (String) map.get("rsp_desc"));
		switch ((String) map.get("rsp_code")) {
		case "000000":
			out = outParam(Constants.RET_SUCCESS_CODE,
					Constants.RET_SUCCESS_MSG);
			//参数放入session银行卡提交时使用
			this.setSessionObj("BankReqSsn",
					(String) map.get("BankReqSsn"));
			this.setSessionObj("bankCode", bankCode);
			this.setSessionObj("mobile", mobile);
			this.setSessionObj("userNmCn",
					(String) map.get("userNmCn"));
			this.setSessionObj("idTp", (String) map.get("idTp"));
			this.setSessionObj("idNo", (String) map.get("idNo"));
			break;
		case "000001":
			out = outParam("004", "请求中证失败");
			break;
		case "000002":
			out = outParam("005", "获取中证数据失败");
			break;
		case "046001":
			out = outParam("007", "该银行卡已绑定过");
			break;
		default:
			out = outParam("006", "获取验证码失败");
			break;
		}
		JsonUtil.writeJson(response, out);
		return null;
	}
	/**
	 * 绑定银行卡提交
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/SCLogin/bankCardMananeSubmit.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView bankCardMananeSubmit(HttpServletRequest request,HttpServletResponse response){
		//入参封装map
		Map<String,Object> paramMap = new HashMap<String,Object>();
		//出参封装map
		Map<String, String> out = new HashMap<String,String>();
		UserDto user = getUser();
		String loginId = user.getOidUserId();
		String tcFlag = user.getTcFlag();//是否帮过卡1已绑  0未绑
		if("1".equals(tcFlag)){//已绑卡
			out = outParam("001","不能重复绑卡");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(StringUtils.isEmpty((String)this.getSessionObj("BankReqSsn"))){
			out = outParam("009","请先发送短信验证码");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//封装入参
		paramMap.put("userIp", getRemoteAddr());//登录Ip
		paramMap.put("loginId", loginId);//登录帐号
		paramMap.put("userNmCn", (String)this.getSessionObj("userNmCn"));//姓名
		paramMap.put("idTp", (String)this.getSessionObj("idTp"));//证件类型1：身份证2：护照3：军官证4：士兵证5：港澳通行证6：户口本7：其他
		paramMap.put("idNo", (String)this.getSessionObj("idNo"));//证件号码
		paramMap.put("cardNo", (String)this.getSessionObj("bankCode"));//银行卡
		paramMap.put("mobileNo", (String)this.getSessionObj("mobile"));//银行预留手机号
		paramMap.put("yzmSsn", (String)this.getSessionObj("BankReqSsn"));//验证码流水
		paramMap.put("yzmContent", (String)request.getParameter("verificationCode"));//验证码
		paramMap.put("oidAccountBindBankId", ConvUtils.convToString(userManageService.nextSeq("OID_ACCOUNT_BIND_BANK_ID")));//银行管理ID
		logger.info("---------调取中证接口并查询用户绑卡信息开始---------------");
		Map<String,Object> selTiedBankCard = bankService.queryTiedBankCard(paramMap);
		if(!"000000".equals(selTiedBankCard.get("rsp_code"))){
			logger.info(loginId+"查询用户在中证绑卡失败");
			out = outParam("011", "系统异常");
			JsonUtil.writeJson(response,out);
			return null;
		}
		logger.info("---------调取中证接口并查询用户绑卡信息结束---------------cardList："+((List)selTiedBankCard.get("cardList")).size());
		if(((List)selTiedBankCard.get("cardList")).size()>0){
			out = outParam("010", "已绑过卡，请不要重复绑定");
			JsonUtil.writeJson(response,out);
			return null;
		}
		logger.info("---------银行卡管理添加银行卡提交开始---------------");
		Map<String,Object> map = myProjectService.bankCardMananeSubmit(paramMap);
		logger.info("---------银行卡管理添加银行卡提交结束---------------");
		switch ((String)map.get("rsp_code")) {
		case "000000":
			user.setTcFlag("1");
			user.setVerifyFlag("1");
			setUser(user);
			out = outParam(Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
			String bankCode = (String)this.getSessionObj("bankCode");
			out.put("bankCode", StringUtil.hideCardNum(bankCode));
			//移除session中数据
			this.clearSessionObj("BankReqSsn");
			this.clearSessionObj("bankCode");
			this.clearSessionObj("mobile");
			this.clearSessionObj("userNmCn");
			this.clearSessionObj("idTp");
			this.clearSessionObj("idNo");
			break;
		case "000001":
			out = outParam("002","请求中证失败");
			break;
		case "000002":
			out = outParam("003","获取中证数据失败");
			break;
		case "000003":
			out = outParam("004","更新绑定银行卡表失败");
			break;
		case "000004":
			out = outParam("005","已达到绑卡上限");
			break;
		case "000005":
			out = outParam("006","此卡已绑定不可重复绑卡");
			break;
		case "000006":
			out = outParam("007","此卡为之前已绑定取消过的卡并此次更新卡状态失败");
			break;
		default:
			out = outParam("008",StringUtil.splitDesc(ConvUtils.convToString(map.get("rsp_desc"))));
			break;
		}
		out.put("isSign",StringUtil.isBankSign(ConvUtils.convToString(map.get("bankCd"))));
		JsonUtil.writeJson(response,out);
		return null;
	}

	private Map<String,String> outParam(String code, String msg) {
		Map<String, String> outParam = new HashMap<String,String>();
		outParam.put(Constants.RET_CODE, code);
		outParam.put(Constants.RET_MSG, msg);
		return outParam;
	}

/*	//查询用户是否删除过银行卡(已实名未绑卡)
	@RequestMapping("/SCLogin/selBankManage.do")
	public ModelAndView selBankManage(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		Map out = new HashMap();
		UserDto user = getUser();
		String bankflag = "0";
		if(!StringUtils.isEmpty(user.getIdCard())){
			bankflag = "1";//已实名
		}
		out.put("bankflag", bankflag);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}*/

	//银行卡管理，添加银行卡反填信息
	@RequestMapping("/SCLogin/selBankManageMsg.do")
	public ModelAndView selBankManageMsg(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		Map out = new HashMap();
		UserDto user = getUser();
		out.put("userName", user.getUserRealName());
		out.put("userIdCard", user.getIdCard());
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}
	/**
	 * 取消银行卡
	 * @param request
	 * @param response
	 * @return
	 * 暂不支持取消银行卡，取消转人工服务
	 */
	/*@RequestMapping(value="/SCLogin/bankCardCancel.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView bankCardCancel(HttpServletRequest request,HttpServletResponse response){
		//页面获取银行卡的hashCode
		String bankCardHash = (String)request.getParameter("bankCardHash");
		//通过hashCode从session 中获取银行卡号
		String bankCode = (String)request.getSession().getAttribute(bankCardHash);
		UserDto user = (UserDto) getSession().getAttribute(Constants.SESSION_USER_INFO);
		String loginId = user.getOidUserId();
		//根据银行卡号和用户id取消银行卡
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("bankCode", bankCode);
		paramMap.put("loginId", loginId);
		Map<String,String> out = new HashMap<String,String>();
		//校验银行卡
		if(StringUtils.isEmpty(bankCode)){
			out = outParam("001","银行卡号不可为空");
			JsonUtil.writeJson(response,out);
			return null;
		}
		logger.info("-------------取消银行卡开始-----------loginId:"+loginId+"---bankCode:"+bankCode);
		Map<String,Object> cancelMap = myProjectService.bankCardCancel(paramMap);
		logger.info("-------------取消银行卡结束-----------");
		if(Constants.RET_SUCCESS_CODE.equals((String)cancelMap.get(Constants.RET_CODE))){
			request.getSession().removeAttribute(bankCardHash);
		}
		cancelMap.remove("bankCode");
		JsonUtil.writeJson(response,cancelMap);
		return null;
	}*/
}
