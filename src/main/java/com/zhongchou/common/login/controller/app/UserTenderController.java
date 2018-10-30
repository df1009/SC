package com.zhongchou.common.login.controller.app;

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

import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.service.IUserTenderService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;

@Controller
public class UserTenderController extends BaseController {
	@Autowired
	private IUserTenderService userTenderService;

	@Autowired
	private IUserManageService userManageService;

	@Autowired
	private IUserService userService;


	private static final Logger LOGGER = LoggerFactory.getLogger(UserTenderController.class);
	/**
	 *个人中心查询用户投资总额和投资数量
	 */
	@RequestMapping(value="/SCLogin/selPresonMsgToApp.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView selPresonMsgToApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> out = new HashMap<>();
		UserDto user = getUser();
		out.put("allTenderAmount", "***");
		out.put("allTenderCount", "***");
		out.put("tenderDisplay", "0");
		if(isApp()){
			if("1".equals(user.getIsDisplay())){
				Map tenderMsg = userTenderService.selPresonMsgToApp(user.getOidUserId());
				out.put("allTenderAmount", tenderMsg.get("allTenderAmount"));
				out.put("allTenderCount", tenderMsg.get("allTenderCount"));
				out.put("tenderDisplay", "1");
			}
		}else{
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
	 *设置手机个人中心敏感信息是否可见
	 */
	@RequestMapping(value="/SCLogin/setPresonSensitiveInfor.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView setPresonSensitiveInfor(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> out = new HashMap<>();
		String setEye = request.getParameter("setEye");//0:不可见  1：可见
		String regex = "^[01]$";
		if(!StringUtil.match(regex, setEye)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response,out);
			return null;
		}
		UserDto userDto = getUser();
		UserDto user = new UserDto();
		userDto.setIsDisplay(setEye);
		user.setIsDisplay(setEye);
		user.setOidUserId(userDto.getOidUserId());
		if(userService.setSensitiveInfo(user.getOidUserId(),setEye)>0){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "系统异常");
		}
		if("0".equals(setEye)){
			out.put("allTenderAmount", "***");
			out.put("allTenderCount", "***");
		}else{
			Map tenderMsg = userTenderService.selPresonMsgToApp(userDto.getOidUserId());
			out.put("allTenderAmount", tenderMsg.get("allTenderAmount"));
			out.put("allTenderCount", tenderMsg.get("allTenderCount"));
		}
		setUser(userDto);
		out.put("tenderDisplay", user.getIsDisplay());
		JsonUtil.writeJson(response, out);
		return null;

	}
}
