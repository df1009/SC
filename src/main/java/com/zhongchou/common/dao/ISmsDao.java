package com.zhongchou.common.dao;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.SendSmsDto;

public interface ISmsDao {

	Row getLastVerifyCode(SendSmsDto sendSms);

	int updateVerifyCodeInvalid(SendSmsDto sendSms);

	int getSmsHistoryCntByChk(SendSmsDto sendSms);

	int saveNightSms(SendSmsDto sendSms);

	int saveHistory(SendSmsDto sendSms, boolean isSendSucc);

	int saveVerifyCode(SendSmsDto sendSms);

	int updateVerifyCode(SendSmsDto sendSms);

	int updateErrorSms(SendSmsDto sendSms,String vId);

	int updateErrorVerifyCodeInvalid(SendSmsDto sendSms,String vId);

	int selSendSmsTenMinute(String phone);

	int saveSms(SendSmsDto sendSms);

	Row getUserVerifyCode(String phone);
}
