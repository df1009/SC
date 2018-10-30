package com.zhongchou.common.loginOut.controller.regist;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.MD5;
import com.yanshang.util.StringUtils;
import com.yanshang.util.VerifyCodeUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IRegistService;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.VerificationUtil;

@Controller
public class RegistFirstMobileController extends BaseController {
	String privateKey = Config.getString("RSAPrivate");

	@Autowired
	private IUserManageService userManageService;

	@Autowired
	private IRegistService registService;

	/**
	 * 用户注册第一步提交（手机）
	 * @param
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/registFirstSubmit.do", method ={ RequestMethod.POST,RequestMethod.GET})
	protected ModelAndView registFirstSubmit(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,Object> out = new HashMap<String,Object>();
		String userPhone = (String)this.getSessionObj("userPhone");
		String password = request.getParameter("password");//密码
		//String txtIntroducerPhone = (String)this.getSessionObj("txtIntroducerPhone");
		String txtIntroducerPhone = "";
		String registerCode = request.getParameter("registerCode");//邀请码
		if(StringUtils.isEmpty(userPhone)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "跨步");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!checkSmsCode(true)){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "请先验证短信动态码");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//参数验证
		if(StringUtils.isEmpty(password)){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "请输入密码");
			JsonUtil.writeJson(response,out);
			return null;
		}
		String resortPwd = "";
		try {
			resortPwd = RSAUtil.decryptByPrivate(privateKey,password);
		} catch (Exception e) {
			e.printStackTrace();
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "密码解密有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!StringUtils.isPasswordStrength(resortPwd)){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		UserDto user = new UserDto();
		boolean existFlag =  VerificationUtil.checkPhoneInput(userManageService,userPhone);
		if (!existFlag) {
			out.put(Constants.RET_CODE, "007");
			out.put(Constants.RET_MSG, "手机号已注册，请不要重复提交");
			JsonUtil.writeJson(response,out);
			return null;
		}
		// 用户OID(UUID自动生成)
		user.setOidUserId(ConvUtils.convToString(userManageService.nextSeq("OID_USER_ID")));

		// 用户登录密码
		user.setSalt(StringUtils.genRandomNum(8));
		user.setUserPwd(MD5.encryptMD5(user.getOidUserId() + resortPwd + user.getSalt()));
		// 用户登录密码强度
		if (StringUtils.isAsciiAlphaNumCharOnly(password)) {
			user.setUserPwdStrength("1");
		} else {
			user.setUserPwdStrength("2");
		}
		String registCodePhone = "";
		if(!StringUtils.isEmpty(registerCode)){
			registCodePhone = registService.selInvitationPhone(registerCode);
		}
		//邀请码查询邀请人手机号

		//邀请码
		String invitationCode = VerifyCodeUtils.generateVerifyCode(4);

		do{
			invitationCode = VerifyCodeUtils.generateVerifyCode(4);
		}while(!registService.checkRefcodeExists(invitationCode));

		// 邀请码
		user.setRefcode(invitationCode);
		// 手机号
		user.setMobile(userPhone);
		// 邮件
		user.setEmail("");
		// 用户类型（"0":个人用户; 1:机构用户;）
		user.setUserType("0");
		// 投资用户标示(0:无效; 1:有效)
		user.setInvestmentFlg("1");
		// 融资用户标示(0:无效; 1:有效)
		user.setFinancingFlg("0");

		if(StringUtils.isEmpty(registCodePhone)){
			if(StringUtils.isEmpty(txtIntroducerPhone)){
				// 邀请人
				user.setIntroducerUserMobile("");
			}else{
				user.setIntroducerUserMobile(txtIntroducerPhone);
			}
		}else{//通过邀请链接注册
			user.setIntroducerUserMobile(registCodePhone);
		}
		user.setUpdUserId(user.getOidUserId());
		logger.info(user.getOidUserId()+":的邀请人："+user.getIntroducerUserMobile());
		if (registService.registUser(user)) {
			this.clearSessionObj(Constants.LOGIN_OUT_VERIFY_CODE);//清除短信验证
			this.clearSessionObj("userPhone");
			this.clearSessionObj("password");
			this.clearSessionObj("txtIntroducerPhone");
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else {
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "注册失败");
			JsonUtil.writeJson(response,out);
			return null;
		}
	}
}
