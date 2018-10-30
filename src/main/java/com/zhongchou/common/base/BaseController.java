package com.zhongchou.common.base;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.config.ConfigKey;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.UserTokenUtil;

@Controller
public class BaseController {

	String privateKey = Config.getString("RSAPrivate");

	//protected String pageBasePath = "00_new/";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private IUserService userService;

	/*@Resource
	protected IService commonService;
	@Resource
	protected IActiveService activeService;*/

	protected String getUserId() {
		return UserTokenUtil.getUserId(getRequest());
	}

	protected String getMobileId() {
		return getRequest().getParameter("IMEIID");
	}

	protected String getCookie(String cookieName) {
		Cookie[] cookie = getRequest().getCookies();
		for (int i = 0; i < cookie.length; i++) {
			Cookie cook = cookie[i];
			if(cook.getName().equalsIgnoreCase(cookieName)){ //获取键
				return cook.getValue();    //获取值
			}
		}
		return null;
	}
	protected HttpSession getSession() {
	    return getRequest().getSession();
	}

	protected HttpServletRequest getRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs.getRequest();
	}

	protected void setUser(UserDto user) {
		UserTokenUtil.setUser(user, getRequest());
	}

	protected UserDto getUser() {
		 return UserTokenUtil.getUser(getRequest());
	}

	protected void clearUserInfo() {
		UserTokenUtil.clearUserInfo(getRequest());
	}

	protected void setSessionObj(String key,Object obj) {
		UserTokenUtil.setSessionObj(key, obj, getRequest());
	}

	protected Object getSessionObj(String key) {
		return UserTokenUtil.getSessionObj(key, getRequest());
	}

	protected void clearSessionObj(String key) {
		UserTokenUtil.clearSessionObj(key, getRequest());
	}

	protected int getItemsPerPage() {
		return StringUtils.toInt(Config.getString(ConfigKey.ITEMS_PER_PAGE), 10);
	}

	protected String getRemoteAddr() {
		String ip = getRequest().getHeader("x-forwarded-for");
		if(StringUtils.isEmpty(ip)){
			ip = getRequest().getRemoteAddr();
		}
		/*if(ip != null && ip.length() != 0 && !"unKnown".equalsIgnoreCase(ip)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			return ip.split(",")[0];
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = getRequest().getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = getRequest().getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = getRequest().getRemoteAddr();
		}
		if (ip != null && ip.indexOf(":") >= 0)
		{ // 判断是否为IPV6地址
			ip = "127.0.0.1";
		}*/
		if(ip != null && ip.length() != 0 && !":".equalsIgnoreCase(ip)){
			String[] ips =  ip.split(":");
			return ips[ips.length-1];
		}else if(ip != null && ip.length() != 0 && !",".equalsIgnoreCase(ip)){
			String[] ips =  ip.split(",");
			return ips[ips.length-1];
		}
		if(ip != null && "::1".equals(ip)){
			ip = "127.0.0.1";
		}
		return ip;
	}

	protected String getEquipment(){
		//Enumeration   typestr = getRequest().getHeaderNames();
		String s1 = getRequest().getHeader("user-agent");
		if(s1.contains("Android")) {
			//System.out.println("Android移动客户端");
			return "android";
		} else if(s1.contains("iPhone")) {
			//System.out.println("iPhone移动客户端");
			return "iPhone";
		} else if(s1.contains("iPad")) {
			//System.out.println("iPad客户端");
			return "iPad";
		}  else {
			//System.out.println("其他客户端");
			return "other";
		}
	}
	protected boolean isApp(){
		return UserTokenUtil.isApp(getRequest());
	}

	protected int isAttestation(){
		UserDto user = getUser();
		String tipCard = user.getTcFlag();
		String riskAssessment = user.getRaFlag();
		String verifyFlag = user.getVerifyFlag();
		if(StringUtils.isEmpty(user.getIdCard())
				||StringUtils.isEmpty(verifyFlag)){//未实名
			return 1;
		}else if(!"1".equals(riskAssessment)){//未测评
			return 2;
		}else if(!"1".equals(tipCard)){//未绑卡
			return 3;
		}
		return 0;
	}

	//校验图形验证码
	protected boolean checkImgCode(String imgCode) {
		boolean flag = false;
		String verifyCode = null;
		if(!StringUtils.isEmpty(imgCode)&&imgCode.equalsIgnoreCase(String.valueOf(getSessionObj(Constants.VERIFY_CODE)))){
			flag = true;
		}
		return flag;
	}
	//校验图形验证码  imgFlag： true不清除
	protected boolean checkImgCode(String imgCode,boolean imgFlag) {
		boolean flag = false;
		if(!StringUtils.isEmpty(imgCode)&&imgCode.equalsIgnoreCase(String.valueOf(getSessionObj(Constants.VERIFY_CODE)))){
			flag = true;
			if(!imgFlag){
				clearSessionObj(Constants.VERIFY_CODE);
			}
		}
		return flag;
	}

	//登录外短信验证码是否校验  clean： true不清除
	protected boolean checkSmsCode(boolean clean) {
		boolean flag = false;
		if(Constants.LOGIN_OUT_VERIFY_CODE_SECCESS.equals(String.valueOf(getSessionObj(Constants.LOGIN_OUT_VERIFY_CODE)))){
			flag = true;
			if(!clean){
				clearSessionObj(Constants.LOGIN_OUT_VERIFY_CODE);
			}
		}
		return flag;
	}

	//登录外短信验证码是否校验
	protected boolean checkSmsCode() {
			boolean flag = false;
			if(Constants.LOGIN_OUT_VERIFY_CODE_SECCESS.equals(String.valueOf(getSessionObj(Constants.LOGIN_OUT_VERIFY_CODE)))){
				flag = true;
				clearSessionObj(Constants.LOGIN_OUT_VERIFY_CODE);
			}
			return flag;
		}
	//登录内短信验证码是否校验
	protected boolean checkLoginSmsCode() {
		boolean flag = false;
		if(Constants.LOGIN_VERIFY_CODE_SECCESS.equals(String.valueOf(getSessionObj(Constants.LOGIN_VERIFY_CODE)))){
			flag = true;
			clearSessionObj(Constants.LOGIN_VERIFY_CODE);
		}
		return flag;
	}
	//登录内短信验证码是否校验    smsFlag  true:不清除session
	protected boolean checkLoginSmsCode(boolean smsFlag) {
		boolean flag = false;
		if(Constants.LOGIN_VERIFY_CODE_SECCESS.equals(String.valueOf(getSessionObj(Constants.LOGIN_VERIFY_CODE)))){
			flag = true;
			if(!smsFlag){
				clearSessionObj(Constants.LOGIN_VERIFY_CODE);
			}

		}
		return flag;
	}
	//登录内短信验证码是否校验
	protected boolean checkLoginSmsCodeToBuy() {
		boolean flag = false;
		if(Constants.LOGIN_VERIFY_CODE_BUY_SECCESS.equals(String.valueOf(getSessionObj(Constants.LOGIN_VERIFY_CODE_BUY)))){
			flag = true;
		}
		return flag;
	}
	//图形验证码是否校验
	protected boolean checkPicCode() {
		boolean flag = false;
		if(Constants.VERIFIEDPICODESUCCESS.equals(String.valueOf(getSessionObj(Constants.VERIFIEDPICODE)))){
			flag = true;
			clearSessionObj(Constants.VERIFIEDPICODE);
		}
		return flag;
	}
	/**
	 * 输出方法
	 * @param response
	 * @param out
	 * @param code
	 * @param value
	 * @return
	 */
	protected ModelAndView outparamMethod(HttpServletResponse response,
										  Map<String, Object> out, String code, String value) {
		out.put(Constants.RET_CODE, code);
		out.put(Constants.RET_MSG, value);
		JsonUtil.writeJson(response,out);
		return null;
	}
}
