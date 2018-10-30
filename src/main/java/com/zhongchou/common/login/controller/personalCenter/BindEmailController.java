package com.zhongchou.common.login.controller.personalCenter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.config.ConfigKey;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.SendEmailDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IMyProjectService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.SendEmailUtil;
import com.zhongchou.common.util.ThreeDesUtil;

@Controller
public class BindEmailController extends BaseController {
	String url = Config.getString("registUrl");//壹盐双创网址
	@Autowired
	private IMyProjectService  myProjectService;
	private static final Logger LOGGER = LoggerFactory.getLogger(BindEmailController.class);
	private static final long ONEDAY = 24*60*60*1000;
	/**
	 * 发送邮件
	 */
	@RequestMapping(value="/SCLogin/sendUserEmail.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView sendEmailToUser(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> out = new HashMap<>();

		UserDto user = getUser();
		String loginId = user.getOidUserId();
		String userEmail = (String) request.getParameter("userEmail");

		/*String basePath = request.getScheme()+"://"+Config.getString("yiyan.domain.url")+request.getContextPath()+"/SCLogin/bindUserEmail.do?t=";*/
		//String basePath = request.getScheme()+"://"+"101.37.3.111:8882"+request.getContextPath()+"/SCLogin/bindUserEmail.do?t=";
		String basePath = url+"/?t=";

		SendEmailDto emailDto = new SendEmailDto();
		try {
			//发送邮件
			emailDto = SendEmailUtil.sendEmail(loginId, userEmail,basePath);
			//更新邮件记录表
			emailDto.setSendResultFlg("1");
			myProjectService.recordEmailLog(emailDto);
			LOGGER.info("-------------发送邮件成功------------------");
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response, out);
			return null;
		} catch (Exception e) {
			LOGGER.error("-------------发送邮件失败------------------");
			LOGGER.info(e.getMessage());
			emailDto.setLoginId(loginId);
			emailDto.setTo(userEmail);;
			//更新邮件记录表
			emailDto.setSendResultFlg("0");
			myProjectService.recordEmailLog(emailDto);
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "发送邮件失败");
			JsonUtil.writeJson(response, out);
			return null;
		}

	}
	/**
	 * 绑定用户邮箱
	 */
	@RequestMapping(value = "/loginOut/bindUserEmail.do", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView bindEmailToUser(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String,Object> out = new HashMap<>();
		// 秘钥
		try {
			String key = "yilicai";
			key = "7f61ff638daf5bffd12f4ce2";
			String pt = request.getParameter("t");//用户的加密信息
			pt=pt.replace(" ", "+");
			byte[] reqPassword = Base64.decodeBase64(pt);
			byte[] srcBytes = ThreeDesUtil.decryptMode(key.getBytes(), reqPassword);
			String param = new String(srcBytes);
			String[] split = param.split(":");
			String loginId = split[0];
			String userEmail = split[1];
			String time = split[2];
			long times = Long.parseLong(time);
			long nowTime = new Date().getTime()-times;
			if(nowTime>ONEDAY){
				out.put(Constants.RET_CODE, "004");
				out.put(Constants.RET_MSG, "绑定邮箱超过24小时");
				JsonUtil.writeJson(response, out);
				return null;
			}
			LOGGER.info("-------------调用绑定邮箱接口开始-----------------");
			Map<String,Object> emailMap = myProjectService.bindUserEmail(loginId, userEmail);
			LOGGER.info("-------------调用绑定邮箱接口结束-----------------");
			out.put("email", (String)emailMap.get("email"));
			switch ((String)emailMap.get("rsp_code")) {
			case "001":
				LOGGER.info("-------------已绑定过邮箱------------------");
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "已绑定过邮箱");
				break;
			case "002":
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				break;
			default:
				LOGGER.info("-------------绑定邮箱失败------------------");
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "绑定邮箱失败");
				break;
			}
			JsonUtil.writeJson(response, out);
			return null;
		} catch (Exception e) {
			LOGGER.error("-------------绑定邮箱异常------------------");
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "绑定邮箱异常");
			JsonUtil.writeJson(response, out);
			return null;
		}

	}
/**
 * 检查邮箱是否绑定过
 * @param request
 * @param response
 * @return
 */
	@RequestMapping(value="/SCLogin/checkEmailExist.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView checkEmailExist(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> out = new HashMap<>();
		UserDto user = getUser();
		String loginId = user.getOidUserId();
		Map<String,Object> emailMap = myProjectService.checkEmailExist(loginId);
		if("001".equals((String)emailMap.get("rsp_code"))){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "已绑定过邮箱");
		}
		if("002".equals((String)emailMap.get("rsp_code"))){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "未绑定过邮箱");
		}
		out.put(Constants.RET_CODE, "000");
		out.put(Constants.RET_MSG, "success");
		return null;

	}

}
