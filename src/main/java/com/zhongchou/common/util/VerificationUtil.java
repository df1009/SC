package com.zhongchou.common.util;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.yanshang.config.Config;
import com.yanshang.config.ConfigKey;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.SendSmsDto;
import com.zhongchou.common.service.ISmsService;
import com.zhongchou.common.service.IUserManageService;

public class VerificationUtil {

	//验证码随机数
	public static String generateVerifyCode(int verifySize)
	  {
	    String sources = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
	    int codesLen = sources.length();
	    Random rand = new Random();
	    StringBuilder verifyCode = new StringBuilder(verifySize);
	    for (int i = 0; i < verifySize; i++) {
	      verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
	    }
	    return verifyCode.toString();
	  }
	//验证码随机数
	public static String generatingRandomNum(String sources,int verifySize)
	  {
	    int codesLen = sources.length();
	    Random rand = new Random();
	    StringBuilder verifyCode = new StringBuilder(verifySize);
	    for (int i = 0; i < verifySize; i++) {
	      verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
	    }
	    return verifyCode.toString();
	  }
	/**
	 * 手机号验证是否存在
	 * @param userPhone 手机号
	 * @return true 不存在   false 存在
	 */
	public static boolean checkPhoneInput(IUserManageService userManageService,String userPhone) {
		if (!StringUtils.isMobileNum(userPhone)||StringUtils.isEmpty(userPhone)) {
			return true;
		} else{
			boolean existFlag =  userManageService.checkMobileExists(userPhone);
			if (existFlag) {
				return false;
			} else {
				return true;
			}
		}
	}
	/**
	 * 验证手机验证码
	 * @return 无
	 */
	public static int checkInput(ISmsService smsService,String userPhone,String phoneCode,String sendType) {
		boolean flag = false;
		SendSmsDto sendSms = new SendSmsDto();
		sendSms.setPhone(userPhone);
		sendSms.setVerifyCode(phoneCode);
		int limit = 10;
		if (!StringUtils.isEmpty(Config.getString(ConfigKey.SMS_VERIFY_CODE_LIMIT))) {
			limit = Integer.parseInt(Config.getString(ConfigKey.SMS_VERIFY_CODE_LIMIT));
		}
		sendSms.setOperateType(sendType);
		int retmsg = smsService.checkVerifyCode(sendSms, limit);
		if (retmsg == 0) {
			//设置短信验证码为失效
			VerifyCodeInvalid(smsService,sendSms);
		}
		return retmsg;
	}

	/**
	 * 设置手机验证码失效
	 * @return 无
	 */
	public static void VerifyCodeInvalid(ISmsService smsService,SendSmsDto sendSms) {
		smsService.updateVerifyCodeInvalid(sendSms);
	}
	/**
	 * 检查融资申请是否已存在此手机号
	 * @param userManageService
	 * @param userPhone
	 * @return
	 */
	public static boolean checkFinanPhoneInput(
			IUserManageService userManageService, String userPhone) {
		if (!StringUtils.isMobileNum(userPhone)||StringUtils.isEmpty(userPhone)) {
			return true;
		} else{
			boolean existFlag =  userManageService.checkFinanPhoneInput(userPhone);
			if (existFlag) {
				return false;
			} else {
				return true;
			}
		}
	}

}
