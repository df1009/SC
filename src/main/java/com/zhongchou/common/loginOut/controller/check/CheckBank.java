package com.zhongchou.common.loginOut.controller.check;

import java.util.HashMap;
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

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IBankService;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class CheckBank extends BaseController {
	Logger logger=Logger.getLogger(CheckBank.class);

	@Autowired
	private IBankService bankService;

	/**
	 * 姓名银行卡验证
	 * @param userName 用户真实姓名
	 * @param idCard 身份证号
	 * @param bankCode 银行卡号
	 * @param mobile  银行预留手机号
	 * @return
	 */
	@RequestMapping(value = "SCLogin/CheckBank.do", method ={ RequestMethod.POST,RequestMethod.GET})
	private ModelAndView checkPhoneInput(HttpServletRequest request,HttpServletResponse response,Model model,String userName,String idCard,String bankCode,String mobile) {
		logger.info("CheckBank.CheckBank  start");
		Map<String,Object> out = new HashMap<String,Object>();

		// 取得登陆账号
		String loginId = (String)this.getSessionObj("LOGIN_ID");

		UserDto user = getUser();

		// 如果不是伪登陆状态，那就从Session取
		if(StringUtils.isEmpty(loginId) && user != null){
			loginId = user.getOidUserId();
		}

		String regex = "^(0|[1-9][0-9]*)$";
		if(!StringUtils.isEmpty(loginId)){
			if (StringUtils.isEmpty(userName)){//姓名格式错误

				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "姓名格式错误");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(StringUtils.isEmpty(idCard)){//身份证号格式错误

				out.put(Constants.RET_CODE, "003");
				out.put(Constants.RET_MSG, "身份证号格式错误");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(StringUtils.isEmpty(bankCode)||!StringUtils.match(regex, bankCode)){//银行卡号格式错误

				out.put(Constants.RET_CODE, "004");
				out.put(Constants.RET_MSG, "银行卡号格式错误");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(StringUtils.isEmpty(mobile)||!StringUtils.match(regex, mobile)){//银行预留手机号格式错误

				out.put(Constants.RET_CODE, "005");
				out.put(Constants.RET_MSG, "银行预留手机号格式错误");
				JsonUtil.writeJson(response,out);
				return null;
			}else{

				Map<String,Object> parameter  = new HashMap<String,Object>();

				parameter.put("loginId", loginId);
				parameter.put("userName", userName);
				parameter.put("idTp", "1");
				parameter.put("idCard", idCard);
				parameter.put("bankCode", bankCode);
				parameter.put("mobile", mobile);

				// 调用中证接口取得原请求流水
				Map<String,Object> map = bankService.getBankSms(parameter);

				String code = (String) map.get("code");
				String BankReqSsn = (String) map.get("BankReqSsn");

				if("000".equals(code) && !StringUtils.isEmpty(BankReqSsn)){

					this.setSessionObj("bankUserName", userName);
					this.setSessionObj("idTp", "1");
					this.setSessionObj("bankIdCard", idCard);
					this.setSessionObj("bankCode", bankCode);
					this.setSessionObj("bankMobile", mobile);
					this.setSessionObj("bankReqSsn", BankReqSsn);
					out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
					out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
					JsonUtil.writeJson(response,out);
					return null;
				}else{
					//错误，异常
					if("092213".equals((String) map.get("rsp_code"))){

						out.put(Constants.RET_CODE, "003");
						out.put(Constants.RET_MSG, "身份证号格式错误");
						JsonUtil.writeJson(response,out);
						return null;
					}else if("091027".equals((String) map.get("rsp_code"))){

						out.put(Constants.RET_CODE, "004");
						out.put(Constants.RET_MSG, "银行卡号格式错误");
						JsonUtil.writeJson(response,out);
						return null;
					}else if("044001".equals((String) map.get("rsp_code"))){

						out.put(Constants.RET_CODE, "006");
						out.put(Constants.RET_MSG, "银行预留手机号码错误");
						JsonUtil.writeJson(response,out);
						return null;
					}else{
						out.put(Constants.RET_CODE, "120");
						out.put(Constants.RET_MSG, map);
						JsonUtil.writeJson(response,out);
						return null;
					}
				}
			}
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "用户未登录");
			JsonUtil.writeJson(response,out);
			return null;
		}
	}
}
