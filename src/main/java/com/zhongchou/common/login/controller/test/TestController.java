package com.zhongchou.common.login.controller.test;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.yanshang.config.Config;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.INoticeService;
import com.zhongchou.common.service.IRedisService;
import com.zhongchou.common.service.ISmsService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RedisUtil;
import com.zhongchou.common.util.SerializeUtil;

@Controller
public class TestController extends BaseController {
	Logger logger=Logger.getLogger(TestController.class);
	@Autowired
	private INoticeService noticeService;
	@Autowired
	private ISmsService smsService;
	/*
	*//**
	 * 获取首页滚动公告
	 *
	 * @return 无
	 *//*
	@RequestMapping(value = "loginOut/setRedis.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getHomePageNotice(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		List homeNoticeList = noticeService.getHomePageNotice();


		out.put("homeNoticeList", homeNoticeList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		//redisService.set("out", SerializeUtil.serializeString(out));
		UserDto u = new UserDto();
		u.setAddress("的發送到反倒是");
		String json = SerializeUtil.serializeString(u);
		RedisUtil.set("out123", json);
		JsonUtil.writeJson(response,out);
		return null;
	}

	@RequestMapping(value = "loginOut/getRedis.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getRedis(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();

		//out = (Map) SerializeUtil.unserializeString();
		Gson gson = new Gson();
		UserDto u = (UserDto)SerializeUtil.unserializeString(RedisUtil.get("out123"));
		out.put("u", u);
		JsonUtil.writeJson(response,out);
		return null;
	}WEIXINURL
*/
	@RequestMapping(value = "loginOut/getSmsCode.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getSmsCode(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		String weixinurl = Config.getString("WEIXINURL");//微信域名
		if(!"m2.yiyanstart.com".equals(weixinurl)){//测试和开发环境
			String phone = request.getParameter("phone");
			out = smsService.getUserVerifyCode(phone);
			JsonUtil.writeJson(response,out);
			return null;
		}
		return null;
	}
}
