package com.zhongchou.common.login.controller.product;

import java.util.HashMap;
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

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.service.IUserTenderService;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;

@Controller
public class UserFinancingController extends BaseController {
	@Autowired
	private ProductService productService;


	private static final Logger LOGGER = LoggerFactory.getLogger(UserFinancingController.class);
	/**
	 *我要融资
	 */
	@RequestMapping(value="/loginOut/addFinancing.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView selPresonMsgToApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> out = new HashMap<>();
		String name = request.getParameter("name");//用户名称
		String mobile = request.getParameter("mobile");//手机号
		String email = request.getParameter("email");//邮箱
		String companyName = request.getParameter("companyName");//公司名称
		String regisTime = request.getParameter("regisTime");//注册时间
		String regisAddress = request.getParameter("regisAddress");//注册地址
		String industry = request.getParameter("industry");//所属行业
		if(StringUtils.isEmpty(name)
				||!StringUtils.isMobileNum(mobile)
				||!StringUtils.isEmail(email)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response, out);
			return null;
		}
		if(!checkSmsCode(true)){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "请先验证短信动态码");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!StringUtils.isEmpty(regisTime)&&!StringUtils.isDate(regisTime)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "注册时间异常");
			JsonUtil.writeJson(response, out);
			return null;
		}
		Map financingMap = new HashMap();
		financingMap.put("name",name);
		financingMap.put("mobile",mobile);
		financingMap.put("email",email);
		financingMap.put("companyName",companyName);
		financingMap.put("regisTime",regisTime);
		financingMap.put("regisAddress",regisAddress);
		financingMap.put("industry",industry);
		if(productService.addFinancing(financingMap)==0){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "提交失败");
			JsonUtil.writeJson(response, out);
			return null;
		}
		this.clearSessionObj(Constants.LOGIN_OUT_VERIFY_CODE);//清除短信验证
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response, out);
		return null;

	}

	
}
