package com.zhongchou.common.service;

import java.util.Map;

import com.zhongchou.common.dto.UserDto;

/**
 * 用户业务逻辑的接口。
 */
public interface IUserService {

	/**
	 * 取用户信息
	 *
	 */
	UserDto getUser(UserDto user);

	/**
	 * 取用户基本信息
	 *
	 */
	UserDto getUserInfo(String loginId);

	/**
	 * 更新用户头像
	 *
	 */
	boolean updUserIconFileId(String loginId,String headPortrait);

	/**
	 * 更新用户手机号
	 *
	 */
	boolean updNewUserPhone(String loginId,String newUserPhone,Map paramMap);

	/**
	 * 更新支付密码
	 * @param userPayPwd
	 *
	 */
	Map updPayPassword(String loginId,String payPassword, String userPayPwd);
	/**
	 * 重置支付密码获取短信
	 * @param user
	 * @return
	 */
	String getSmsCode(UserDto user);
	/**
	 * 重置支付密码
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> resetPayPassword(Map<String, Object> paramMap);

	//查询用户是否有我的项目权限
	int getMyProject(String loginId);
	//设置用户敏感信息查看方式
	int setSensitiveInfo(String loginId,String status);
	//设置用户敏感信息查看方式
	int setcertifiedInvestor(String loginId,String investor);
	//查询用户在中证的实名信息
	Map selUserInfo(String loginId);
	//添加用户信息更改记录
	boolean addUserModifyLog(Map paramMap);
	/**
	 * 认证投资人
	 * @param realInvest
	 * @return
	 */
	int certifiedInvestor(Map<String, String> realInvest);
	/**
	 * 获取用户已有状态信息
	 * @param userId
	 * @return
	 */
	Map<String, Map<String, String>> getUserState(String userId);
}
