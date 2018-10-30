package com.zhongchou.common.loginOut.controller.check;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;

@Controller
public class CheckPassword extends BaseController {

	String privateKey = Config.getString("RSAPrivate");

	/**
	 * 注册密码验证
	 * @param password 加密后密码
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/checkRegistPassword.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView userLogin(HttpServletRequest request,HttpServletResponse response,Model model,String password){
		Map<String,Object> out = new HashMap<String,Object>();
		String resortPwd = null;

		//参数验证
		if(StringUtils.isEmpty(password)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		try {
			resortPwd = RSAUtil.decryptByPrivate(privateKey,password);
		} catch (Exception e) {
			e.printStackTrace();
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "密码解密有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!StringUtils.isPasswordStrength(resortPwd)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		this.setSessionObj("password", resortPwd);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}
}
