package com.zhongchou.common.loginOut.controller.retrievePwd;

import java.io.UnsupportedEncodingException;
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

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.MD5;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.ISmsService;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.VerificationUtil;

@Controller
public class UserRetrievePwd extends BaseController {
	String privateKey = Config.getString("RSAPrivate");
	Logger logger=Logger.getLogger(UserRetrievePwd.class);
	@Autowired
	private ISmsService smsService;

	@Autowired
	private IUserManageService userManageService;


	/**
	 * 重置密码第一步,手机号验证
	 * @param 	userPhone  手机号
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/useRetrievePwd.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView useRetrievePwd(HttpServletRequest request,HttpServletResponse response,Model model,String userPhone){
		logger.info("UserRetrievePwd.useRetrievePwd  start");
		Map<String,Object> out = new HashMap<String,Object>();

		if(StringUtils.isEmpty(userPhone)
				||!StringUtils.isMobileNum(userPhone)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "手机号格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		boolean existFlag =  VerificationUtil.checkPhoneInput(userManageService,userPhone);

		if (existFlag) {
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "手机号不存在");
			JsonUtil.writeJson(response,out);
			return null;
		}

		this.setSessionObj("RetrieveUserPhone", userPhone);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	/**
	 * 重置密码
	 * @param 	password  密码
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/useRetrievePwdSubmit.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView useRetrievePwdSubmit(HttpServletRequest request,HttpServletResponse response,Model model,String password){
		logger.info("UserRetrievePwd.useRetrievePwdSubmit  start");
		//获取用户手机号
		String userPhone = (String) this.getSessionObj("RetrieveUserPhone");

		Map<String,Object> out = new HashMap<String,Object>();
		String resortPwd = null;

		if(StringUtils.isEmpty(password)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "请输入密码");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(StringUtils.isEmpty(userPhone)){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "请先输入手机号");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!checkSmsCode(true)){
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "请先验证短信动态码");
			JsonUtil.writeJson(response,out);
			return null;
		}
		try {
			resortPwd = RSAUtil.decryptByPrivate(privateKey,password);
		} catch (UnsupportedEncodingException e) {
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "密码格式不正确");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(!StringUtils.isPasswordStrength(resortPwd)){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "密码格式不正确");
			JsonUtil.writeJson(response,out);
			return null;
		}
		this.clearSessionObj("RetrieveUserPhone");
		UserDto user = new UserDto();
		Row resultMap = null;
		user.setMobile(userPhone);
		resultMap = userManageService.getUser(user);

		if (resultMap.isEmpty()) {
			out.put(Constants.RET_CODE, "006");
			out.put(Constants.RET_MSG, "用户不存在");
		}
		user = new UserDto();
		user.setOidUserId(ConvUtils.convToString(resultMap.get("OID_USER_ID")));
		user.setUserPwd(MD5.encryptMD5(resultMap.getString("OID_USER_ID") + resortPwd + resultMap.getString("SALT")));
		if (StringUtils.isAsciiAlphaNumCharOnly(resortPwd)) {
			user.setUserPwdStrength("1");
		} else {
			user.setUserPwdStrength("2");
		}
		boolean result = userManageService.updateUser(user);
		if (result) {
			this.clearSessionObj(Constants.LOGIN_OUT_VERIFY_CODE);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			out.put(Constants.RET_CODE, "007");
			out.put(Constants.RET_MSG, "重置失败");
			JsonUtil.writeJson(response,out);
			return null;
		}

	}
}
