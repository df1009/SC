package com.zhongchou.common.dao;

import java.util.Map;

import com.yanshang.util.Row;


/**
 * 用户身份验证数据操作的接口。
 */
public interface IUserIdVerificationDao {

	/**
	 * 查询用户详细数据的方法。
	 *
	 * @param oidUserId 用户id
	 * @return 用户身份验证数据
	 */
	Row getUserIdVerification(Map<String, Object> parame);

	/**
	 * 更新用户身份验证数据的方法。
	 *
	 * @param userIdVerification 持有更新的用户的数据传输对象
	 * @return 更新件数
	 */

	int updUserIdVerification(Map<String, Object> parame);

	/**
	 * 新增用户身份验证数据的方法。
	 *
	 * @param userIdVerification 持有更新的用户的数据传输对象
	 * @return 更新件数
	 */

	int insUserIdVerification(Map<String, Object> parame);
}
