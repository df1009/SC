package com.zhongchou.common.service;

import com.zhongchou.common.dto.SendSmsDto;
import com.zhongchou.common.dto.UserDto;



public interface IRegistService{

//	User userAuth(String loginId, String password);

    boolean registUser(UserDto user);

    boolean sendSms(SendSmsDto sendSms);

	boolean checkRefcodeExists(String invitationCode);

	String selInvitationPhone(String invitationCode);
}
