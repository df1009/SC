package com.zhongchou.common.loginOut.controller.login;

import java.util.Date;
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
import com.yanshang.config.ConfigKey;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.MessageUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.ILoginService;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.VerificationUtil;

@Controller
public class LoginController extends BaseController {
	Logger logger=Logger.getLogger(LoginController.class);
	String privateKey = Config.getString("RSAPrivate");
	String publicKey = Config.getString("RSAPulice");
	@Autowired
	private ILoginService loginService;
	@Autowired
	private IUserService userService;


	/**
	 * 用户登录
	 * @param username  账号
	 * @param password  密码
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/userLogin.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView userLogin(HttpServletRequest request,HttpServletResponse response,Model model,String username,String password){
		logger.info("LoginController.userLogin  start");
		Map<String,Object> out = new HashMap<String,Object>();
		String resortPwd = null;
		if(StringUtils.isEmpty(username)
				||!StringUtils.isMobileNum(username)
				||StringUtils.isEmpty(password)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			try {
				resortPwd = RSAUtil.decryptByPrivate(privateKey,password);
			} catch (Exception e) {
				e.printStackTrace();
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "密码解密有误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			//查询用户是否需要显示图形验证码
			int imgFlag = loginService.selImgFlag(username);
			out.put("errorNum", imgFlag);
			if(imgFlag>=3){
				String imgCode = request.getParameter("imgCode");//验证码
				//图形验证码
				if(!checkImgCode(imgCode)){
					out.put(Constants.RET_CODE, "008");
					out.put(Constants.RET_MSG, "验证码错误");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}
			UserDto user = loginService.auth(username, resortPwd);
			// 登录状态判定
			if (user.getErrorCode() == 1) {//帐户或密码输入错误
				out.put(Constants.RET_CODE, "003");
				out.put(Constants.RET_MSG, MessageUtils.getMessage("ERR0003"));
				JsonUtil.writeJson(response,out);
				return null;
			} else if (user.getErrorCode() == 2) {//帐户已被锁定
				int n = StringUtils.toInt(Config.getString(ConfigKey.LOGIN_ERROR_TIME), 3);
				out.put(Constants.RET_CODE, "004");
				out.put(Constants.RET_MSG, MessageUtils.getMessage("ERR0004", n));
				JsonUtil.writeJson(response,out);
				return null;
			} else if (user.getErrorCode() == 3) {//输入的帐户不存在
				out.put(Constants.RET_CODE, "005");
				out.put(Constants.RET_MSG, MessageUtils.getMessage("ERR0005"));
				JsonUtil.writeJson(response,out);
				return null;
			} else if (user.getErrorCode() == 4) {//帐户或密码输入错误
				out.put(Constants.RET_CODE, "003");
				out.put(Constants.RET_MSG, MessageUtils.getMessage("ERR0003"));
				JsonUtil.writeJson(response,out);
				return null;
			} else if (user.getErrorCode() == 5) {//中证注册失败
				out.put(Constants.RET_CODE, "006");
				out.put(Constants.RET_MSG, "中证注册失败");
				JsonUtil.writeJson(response,out);
				return null;
			} else if (user.getErrorCode() == 6) {//密码错误显示验证码
				out.put(Constants.RET_CODE, "007");
				out.put(Constants.RET_MSG, "密码输入错误");
				JsonUtil.writeJson(response,out);
				return null;
			}else {//成功
				String tokenId = null;
				if(isApp()){
					String mobileId =  getMobileId();
					try {
						mobileId = RSAUtil.decryptByPrivate(privateKey,mobileId);
					} catch (Exception e) {
						e.printStackTrace();
						out.put(Constants.RET_CODE, "008");
						out.put(Constants.RET_MSG, "imei is error");
						JsonUtil.writeJson(response,out);
						return null;
					}
					//userId+IMEI+yyyyMMddHHmmss
					String str = user.getOidUserId()+"+"+mobileId+"+"+DateUtils.convertDate2String(new Date(), "yyyyMMddHHmmss");
					try {
						tokenId = RSAUtil.encryptionByPublic(publicKey,str);
					} catch (Exception e) {
						e.printStackTrace();
						out.put(Constants.RET_CODE, "008");
						out.put(Constants.RET_MSG, "tokenId is error");
						JsonUtil.writeJson(response,out);
						return null;
					}
				}
				//去中证校验实名兼容壹理财迁移已实名绑卡用户
				Map userMsgMap = userService.selUserInfo(user.getOidUserId());
				user.setVerifyFlag(StringUtils.isEmpty(ConvUtils.convToString(userMsgMap.get("userName")))?"":"1");
				setUser(user);
				if(StringUtils.isEmpty(DateUtils.convertDate2String(user.getLastLoginDate(),"yyyyMMdd"))){//用户第一次登录
					out.put("Risk", "1");
				}else{
					out.put("Risk", "0");
				}
				Map userMap = new HashMap();
				userMap.put("userName", user.getUserRealName());
				userMap.put("mobile", StringUtils.encryptMobile(user.getMobile()));
				logger.info("tokenId:"+tokenId);
				out.put("tokenId", tokenId);
				out.put("user", userMap);
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response,out);
				return null;
			}
		}
	}
}
