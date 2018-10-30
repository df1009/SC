package com.zhongchou.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.UserDto;



/**
 * 用户中心业务逻辑的接口。
 */
public interface IUserManageService {

	/**
	 * 检查用户手机号是否存在的方法。
	 *
	 * @param mobile   用户手机号
	 * @param userType 用户类型
	 * @return true存在，否则不存在
	 */
	boolean checkMobileExists(String mobile, String... userType);

	/**
	 * 获取用户。
	 *
	 * @param user
	 * @return 查询到的用户信息
	 */
	Row getUser(UserDto user);

	/**
	 * 更新用户。
	 *
	 * @param user
	 * @return 更新成否结果
	 */
	boolean updateUser(UserDto user);


	public BigInteger nextSeq(String sequenceName) ;

	/**
	 *查询用户推荐的人
	 */
	List selIntroducerPreson(String uId);
	/**
	 *查询用户的推荐人
	 */
	Map selIntroducer(String userId);
	/**
	 *设置用户的推荐人
	 */
	int setIntroducer(UserDto user);
	/**
	 * 检查融资申请是否已存在此手机号
	 * @param userManageService
	 * @param userPhone
	 * @return
	 */
	boolean checkFinanPhoneInput(String userPhone);
}