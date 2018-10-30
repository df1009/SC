package com.zhongchou.common.dao;

import java.util.Map;

import com.yanshang.util.Row;

/**
 * 用户详细数据操作的接口。
 */
public interface IUserDetailDao {

	/**
	 * 查询用户详细数据的方法。
	 *
	 * @param oidUserId 用户id
	 * @return 用户详细
	 */
	Row getUserDetail(Map<String, Object> parame);

	/**
	 * 更新用户详细数据的方法。
	 *
	 * @param UserDetail 持有更新的用户的数据传输对象
	 * @return 更新件数
	 */
	int updUserDetail(Map<String, Object> parame);
	/**
	 * 记录用户的变更记录
	 *
	 * @param UserDetail 持有更新的用户的数据传输对象
	 * @return 更新件数
	 */
	int addUserModifyLog(Map<String, Object> parame);
}
