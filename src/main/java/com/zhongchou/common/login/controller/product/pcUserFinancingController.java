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
public class pcUserFinancingController extends BaseController {
	@Autowired
	private ProductService productService;


	private static final Logger LOGGER = LoggerFactory.getLogger(pcUserFinancingController.class);
	/**
	 *我要融资
	 */
	@RequestMapping(value="/loginOut/pcAddFinancing.do",method ={ RequestMethod.POST,RequestMethod.GET})
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
		String type = request.getParameter("type");//引入渠道0：双创  1：其他
		if(StringUtils.isEmpty(name)
				||!StringUtils.isMobileNum(mobile)){
			return outparamMethod(response, out, "001", "姓名不可为空");
		}
		if(type.length()>1
				){
			return outparamMethod(response, out, "006", "系统异常");
		}
		if(StringUtils.isEmpty(mobile)
				||!StringUtils.isMobileNum(mobile)){
			return outparamMethod(response, out, "004", "手机号格式错误");
		}
		if(!checkSmsCode(true)){
			return outparamMethod(response, out, "003", "请先验证短信动态码");
		}
		if(!StringUtils.isEmpty(regisTime)&&!StringUtils.isDate(regisTime)){
			return outparamMethod(response, out, "005", "注册时间异常");
		}
		Map financingMap = new HashMap();
		financingMap.put("name",name);
		financingMap.put("mobile",mobile);
		financingMap.put("email",email);
		financingMap.put("companyName",companyName);
		financingMap.put("regisTime",regisTime);
		financingMap.put("regisAddress",regisAddress);
		financingMap.put("industry",industry);
		financingMap.put("type",type);
		if(productService.addFinancing(financingMap)==0){
			return outparamMethod(response, out, "002", "提交失败");
		}
		this.clearSessionObj(Constants.LOGIN_OUT_VERIFY_CODE);//清除短信验证
		return outparamMethod(response, out, Constants.RET_SUCCESS_CODE, Constants.RET_SUCCESS_MSG);

	}


}
