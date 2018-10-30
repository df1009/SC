package com.zhongchou.common.loginOut.controller.check;

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

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.util.VerificationUtil;

@Controller
public class CheckMobile extends BaseController {
	Logger logger=Logger.getLogger(CheckMobile.class);

	@Autowired
	private IUserManageService userManageService;

	/**
	 * 手机号验证
	 * @param userPhone 手机号
	 * @return 无
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "loginOut/checkMobile.do", method ={ RequestMethod.POST,RequestMethod.GET})
	private ModelAndView checkPhoneInput(HttpServletRequest request,HttpServletResponse response,Model model,String userPhone) throws UnsupportedEncodingException {
		Map<String,Object> out = new HashMap<String,Object>();
		logger.info("CheckMobile.checkMobile  start");
		Integer countNum = 1;
		if(isApp()){

			if(null!=getSessionObj("numberStr")&&""!=getSessionObj("numberStr")){
				countNum = (int)getSessionObj("numberStr")+1;
			}
			out.put("countNum",countNum);
			setSessionObj("numberStr", countNum);
		}
		if (StringUtils.isEmpty(userPhone)||!StringUtils.isMobileNum(userPhone)) {
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		} else{
			boolean existFlag =  VerificationUtil.checkPhoneInput(userManageService,userPhone);
			if (!existFlag) {
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "手机号已存在");
				JsonUtil.writeJson(response,out);
				return null;
			} else {
				this.setSessionObj("userPhone", userPhone);
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response,out);
				return null;
			}
		}
	}

	/**
	 * 推荐人手机号验证
	 * @param 错误信息 labObj
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/checkIntroducerPhone.do", method ={ RequestMethod.POST,RequestMethod.GET})
	private ModelAndView checkIntroducerPhoneInput(HttpServletRequest request,HttpServletResponse response,Model model,String txtIntroducerPhone) {
		logger.info("CheckMobile.checkIntroducerPhone  start");
		Map<String,Object> out = new HashMap<String,Object>();

		if (!StringUtil.isMobileNum(txtIntroducerPhone)) {
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		} else {
			boolean existFlag = VerificationUtil.checkPhoneInput(userManageService,txtIntroducerPhone);
			if (existFlag) {
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "推荐人不存在");
				JsonUtil.writeJson(response,out);
				return null;
			} else {
				this.setSessionObj("txtIntroducerPhone", txtIntroducerPhone);
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response,out);
				return null;
			}
		}
	}
}
