package com.zhongchou.common.service;


import com.zhongchou.common.dto.AdminUserDto;
import com.zhongchou.common.dto.UserDto;

/**
 * 登录业务逻辑的接口。
 */
public interface ILoginService {

	/**
	 * 管理端用管理员用户登录的方法。
	 *
	 * @param loginId  登录账户ID
	 * @param password 登录账户密码
	 * @return 持有管理员用户的数据传输对象
	 */
	AdminUserDto adminAuth(String loginId, String password);

	/**
	 * 查询用户输入密码错误次数
	 *
	 * @param loginId  登录账户ID
	 * @param password 登录账户密码
	 * @return 持有普通用户的数据传输对象
	 */
	int selImgFlag(String mobile);
	/**
	 * 普通用户端用用户登录的方法。
	 *
	 * @param loginId  登录账户ID
	 * @param password 登录账户密码
	 * @return 持有普通用户的数据传输对象
	 */
	UserDto auth(String loginId, String password);
}
