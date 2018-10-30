package com.zhongchou.common.service;

import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.SendSmsDto;

public interface ISmsService {

	int checkVerifyCode(SendSmsDto sendSms, int limit);

	boolean sendSms(SendSmsDto sendSms);

	boolean saveHistory(SendSmsDto sendSms, boolean isSendSucc);

	boolean saveNightSms(SendSmsDto sendSms);

	boolean saveVerifyCode(SendSmsDto sendSms);

	Row getValidVerifyCode(SendSmsDto sendSms);

	boolean updateVerifyCodeInvalid(SendSmsDto sendSms);

	int selSendSmsTenMinute(String phone);

	boolean saveSystemSms(SendSmsDto sendSms);

	Map<String,Object> sendShortMessage(String mobileNum,String loginId,String sendType);

	Map getUserVerifyCode(String phone);
}
