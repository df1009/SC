package com.zhongchou.common.sms;

import com.zhongchou.common.dto.SendSmsDto;

public interface SmsSender {
	boolean send(SendSmsDto sendSms);
}
