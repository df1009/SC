package com.zhongchou.common.loginOut.controller.check;

import java.io.UnsupportedEncodingException;
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
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IRegistService;
import com.zhongchou.common.service.ISmsService;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.VerificationUtil;


@Controller
public class CheckAppMobile extends BaseController{
    String privateKey = Config.getString("RSAPrivate");

    @Autowired
    private IUserManageService userManageService;
    @Autowired
    private ISmsService smsService;

    @Autowired
    private IRegistService registService;

    //1:手机号验证:loginOut/checkMobile.do
    //2:图片验证码取得:调用getImgVerifyCode.do
    //3:登录外获取短信验证码:loginOut/getVerifyCodeToImg.do

    /**
     * 7:用户注册提交
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "loginOut/registAppSubmit.do", method ={ RequestMethod.POST,RequestMethod.GET})
    protected ModelAndView registSubmit(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> out = new HashMap<String,Object>();
        //String userPhone = (String)this.getSessionObj("userPhone");
        String userPhone = request.getParameter("userPhone");
        String password = request.getParameter("password");//密码
        String phoneCode = request.getParameter("phoneCode");//短信验证码

        if(StringUtils.isEmpty(userPhone)){
            return outparamMethod(response,out,"001","手机号不可为空");
        }
        if(StringUtils.isEmpty(phoneCode)){
            return outparamMethod(response,out,"002","短信验证码不可为空");
        }

        //短信验证码1:验证码错误   2：验证码超时  3：验证码错误次数超限
        try {
            int retmsg = VerificationUtil.checkInput(smsService,userPhone,phoneCode,"1");
            if(retmsg==2){
                return outparamMethod(response,out,"008","验证码超时");
            }else if(retmsg==1){
                return outparamMethod(response,out,"009","验证码错误");
            }else if(retmsg==3){
                return outparamMethod(response,out,"010","验证码错误次数超限");
            }
        } catch (Exception e) {
            logger.error("校验码验证异常:"+e);
            return outparamMethod(response,out,"011","校验码验证异常");
        }

        //参数验证
        if(StringUtils.isEmpty(password)){
            return outparamMethod(response,out,"003","请输入密码");
        }
        String resortPwd = "";
        try {
            resortPwd = RSAUtil.decryptByPrivate(privateKey,password);
        } catch (Exception e) {
            logger.error("密码解密有误:"+e);
            return outparamMethod(response,out,"004","密码解密有误");
        }
        if(!StringUtils.isPasswordStrength(resortPwd)){
            return outparamMethod(response,out,"005","密码格式错误");
        }
        UserDto user = new UserDto();
        boolean existFlag =  VerificationUtil.checkPhoneInput(userManageService,userPhone);
        if (!existFlag) {
            return outparamMethod(response,out,"006","手机号已注册");
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

        user.setUpdUserId(user.getOidUserId());
        if (registService.registUser(user)) {
            this.clearSessionObj(Constants.LOGIN_OUT_VERIFY_CODE);//清除短信验证
            this.clearSessionObj("userPhone");
            this.clearSessionObj("password");
            this.clearSessionObj("txtIntroducerPhone");
            return outparamMethod(response,out,Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
        }else {
            return outparamMethod(response,out,"007","注册失败");
        }
    }

    /**
	 * 重置密码
	 * @param 	password  密码
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/useRetrieveAppPwdSubmit.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView useRetrievePwdSubmit(HttpServletRequest request,HttpServletResponse response,String confirmPassword,String password){
		logger.info("UserRetrievePwd.useRetrievePwdSubmit  start");
		//获取用户手机号
		String userPhone = request.getParameter("userPhone");

		Map<String,Object> out = new HashMap<String,Object>();
		String resortPwd = null;
		String confirmresortPwd = null;
		if(StringUtils.isEmpty(password)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "请输入密码");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(StringUtils.isEmpty(userPhone)){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "请先输入手机号");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!checkSmsCode(true)){
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "请先验证短信动态码");
			JsonUtil.writeJson(response,out);
			return null;
		}

		try {
			resortPwd = RSAUtil.decryptByPrivate(privateKey,password);
			confirmresortPwd = RSAUtil.decryptByPrivate(privateKey,confirmPassword);
		} catch (UnsupportedEncodingException e) {
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "密码格式不正确");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!resortPwd.equals(confirmresortPwd)){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "两次输入密码不一致");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!StringUtils.isPasswordStrength(resortPwd)){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "密码格式不正确");
			JsonUtil.writeJson(response,out);
			return null;
		}
		this.clearSessionObj("RetrieveUserPhone");
		UserDto user = new UserDto();
		Row resultMap = null;
		user.setMobile(userPhone);
		resultMap = userManageService.getUser(user);

		if (resultMap.isEmpty()) {
			out.put(Constants.RET_CODE, "006");
			out.put(Constants.RET_MSG, "用户不存在");
		}
		user = new UserDto();
		user.setOidUserId(ConvUtils.convToString(resultMap.get("OID_USER_ID")));
		user.setUserPwd(MD5.encryptMD5(resultMap.getString("OID_USER_ID") + resortPwd + resultMap.getString("SALT")));
		if (StringUtils.isAsciiAlphaNumCharOnly(resortPwd)) {
			user.setUserPwdStrength("1");
		} else {
			user.setUserPwdStrength("2");
		}
		boolean result = userManageService.updateUser(user);
		if (result) {
			this.clearSessionObj(Constants.LOGIN_OUT_VERIFY_CODE);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			out.put(Constants.RET_CODE, "007");
			out.put(Constants.RET_MSG, "重置失败");
			JsonUtil.writeJson(response,out);
			return null;
		}

	}

}
