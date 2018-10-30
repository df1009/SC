package com.zhongchou.common.dao;

import java.util.List;

import com.yanshang.util.Row;

/**
 * 登录业务数据操作的接口。
 */
public interface ILoginDao {

	/**
	 * 通过登录账户ID获取管理员账户数据的方法。
	 *
	 * @param loginId 登录账户ID
	 * @return 管理员账户数据
	 */
	Row adminLogin(String loginId);

	/**
	 * 通过登录账户ID获取用户账户数据的方法。
	 *
	 * @param loginId 登录账户ID
	 * @return 用户账户数据
	 */
	Row login(String loginId);

	/**
	 * 加算管理员账户登录错误次数的方法。
	 *
	 * @param loginId 登录账户ID
	 * @return 登录错误次数
	 */
	int addAdminLoginErrorTimes(String loginId);

	/**
	 * 加算用户账户登录次数的方法。
	 *
	 * @param loginId 登录账户ID
	 * @return 登录次数
	 */
	int addLoginTimes(String loginId);

	/**
	 * 加算用户账户登录错误次数的方法。
	 *
	 * @param loginId 登录账户ID
	 * @return 登录错误次数
	 */
	int addLoginErrorTimes(String loginId);

	/**
	 * 判断锁定帐户的解锁时间是否到期
	 *
	 * @param loginId 登录账户ID
	 * @param time 设定经过的解锁时间
	 * @return true：到期；false：未到期
	 */
	boolean isLoginErrorTime(String loginId, int time);

	/**
	 * 更新管理员账户最后登录时间的方法
	 *
	 * @param loginId 登录账户ID
	 * @return 更新件数
	 */
	int updAdminLastLoginDate(String loginId);

	/**
	 * 更新用户账户最后登录时间的方法
	 *
	 * @param loginId 登录账户ID
	 * @return 更新件数
	 */
	int updLastLoginDate(String loginId);

	/**
	 * 通过角色UUID获取访问权限数据列表的方法。
	 *
	 * @param roleId 角色UUID
	 * @return 访问权限数据列表
	 */
	List<Row> getACL(String[] roleId);

	/**
	 * 通过用户UUID获取用户账户数据的方法。
	 *
	 * @param oidUserId 用户UUID
	 * @param userType  用户类型
	 * @return 用户账户数据
	 */
	Row getUserInfo(String oidUserId, String userType);

	//查询用户输入密码错误次数
	Row selImgFlag(String mobile);
}
