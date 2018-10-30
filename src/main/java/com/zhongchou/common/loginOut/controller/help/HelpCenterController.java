package com.zhongchou.common.loginOut.controller.help;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IHelpCenterService;
import com.zhongchou.common.service.IIndexService;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class HelpCenterController extends BaseController {
	Logger logger=Logger.getLogger(HelpCenterController.class);
	@Autowired
	private IHelpCenterService helpCenterService;

	@RequestMapping("/helpCenter.do")
	public ModelAndView help(HttpServletRequest request,HttpServletResponse response,Model model,String showId){
		logger.info("HelpCenterController.helpCenter  start");
		 ModelAndView modelAndView = new ModelAndView();
		 Map out = new HashMap();
		 if(StringUtils.isEmpty(showId)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "入参为空");
			JsonUtil.writeJson(response,out);
			return null;
		 }
		 String pageInfo = helpCenterService.getSafetyInfo(showId);
		 if(null == pageInfo){
		 out.put(Constants.RET_CODE, "002");
		 out.put(Constants.RET_MSG,"页面获取失败");
		 JsonUtil.writeJson(response,out);
		 return null;
		 }
		 out.put("pageInfo",pageInfo);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
		 return null;
	}

}
