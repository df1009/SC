package com.zhongchou.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.UserDto;


/**
 * 用户数据操作的接口。
 */
public interface IUserDao {
	/**
	 * 通过用户手机号获取用户数据的方法。
	 *
	 * @param mobile 用户手机号
	 * @return 用户数据
	 */
	Row getUserByMobile(String mobile);
	/**
	 * 通过联系人手机号获取机构用户数据的方法。
	 *
	 * @param mobile 联系人手机号
	 * @return 机构用户数据
	 */
	Row getOrgByContactMobile(String mobile);

	/**
	 * 根据所设定的条件检索用户邀请码的方法
	 * @param user
	 * @return 根据所设定的条件检索到的用户邀请码
	 */
	Row checkRefcodeExists(String invitationCode);

	/**
	 * 插入用户账户数据的方法。
	 *
	 * @param user 持有插入的用户账户的数据传输对象
	 * @return 插入件数
	 */
	int insUserAccount(UserDto user);

	/**
	 * 插入用户积分数据的方法。
	 *
	 * @param user 持有插入的用户的数据传输对象
	 * @return 插入件数
	 */
	int insUserPoint(UserDto user);

	/**
	 * 插入用户收货地址数据的方法。
	 *
	 * @param user 持有更新的用户的数据传输对象
	 * @return 更新件数
	 */
	int insUserAddress(UserDto user);

	/**
	 * 插入用户身份验证表数据的方法。
	 *
	 * @param user 持有更新的用户的数据传输对象
	 * @return 更新件数
	 */
	int insUserIdVerification(UserDto user);

	/**
	 * 插入用户数据的方法。
	 *
	 * @param user 持有插入的用户的数据传输对象
	 * @return 插入件数
	 */
	int insUser(UserDto user);

	/**
	 * 插入用户详细数据的方法。
	 *
	 * @param user 持有插入的用户详细的数据传输对象
	 * @return 插入件数
	 */
	int insUserDetail(UserDto user);

	/**
	 * 插入用户推荐人数据的方法。
	 *
	 * @param user 持有插入的用户推荐人的数据传输对象
	 * @return 插入件数
	 */
	int insIntroducer(UserDto user);

	/**
	 * 管理端用获取指定的用户的方法。
	 *
	 * @param mobile
	 * @return 指定用户列表
	 */
	Row getUser(UserDto user);

	/**
	 * 更新用户数据的方法。
	 *
	 * @param user 持有更新的用户的数据传输对象
	 * @return 更新件数
	 */

	int updUser(UserDto user);

	/**
	 * 获取用户个人中心信息
	 * @param oidUserId
	 * @return
	 */

	Row getUserPersonal(String oidUserId);

	/**
	 * 更新用户中证接口注册字段的方法。
	 *
	 * @return 更新件数
	 */
	int updRegistUser(Map<String, Object> parame);

	/**
	 * 更新用户绑卡字段的方法。
	 *
	 * @return 更新件数
	 */
	int updBankUser(Map<String, Object> parame);

	/**
	 * 更新用户风险评估字段的方法。
	 *
	 * @return 更新件数
	 */
	int updRiskUser(Map<String, Object> parame);
	/**
	 * 判断是否存在用户。
	 *
	 * @return 更新件数
	 */
	Row getExistUser(UserDto userDto);

	/**
	 * 更新user_detail表风险评分的方法。
	 *
	 * @return 更新件数
	 */
	int updRiskUserDetail(Map<String, Object> parameter);


	/**
	 * 查询用户信息
	 *
	 * @return
	 */
	Row getUserInfo(String loginId);

	/**
	 * 更新用户头像
	 *
	 * @return
	 */
	int updUserIconFileId(String loginId, String headPortrait);

	/**
	 * 更新用户手机
	 *
	 * @return
	 */
	int updUserPhone(String loginId, String userPhone);

	/**
	 * 更新支付密码
	 *
	 * @return
	 */
	int updPayPassword(String loginId, String payPassword);

	/**
	 * 更新用户同意风险提示
	 *
	 *
	 */
	boolean saveConfirmRiskWarning(String userId);

	/**
	 *查询用户推荐的人
	 */
	List<Row> selIntroducerPreson(String uId);
	/**
	 * 更新用户身份表的支付密码
	 * @param loginId
	 * @param newPayPassword
	 * @return
	 */
	int updPayPwdForUserIdVer(String loginId, String newPayPassword);

	/**
	 * 根据邀请码查询邀请人手机号
	 * @param loginId
	 * @param newPayPassword
	 * @return
	 */
	Row selInvitationPhone(String invitationCode);

	//查询用户是否有我的项目权限
	int getMyProject(String loginId);

	//设置用户敏感信息查看方式
	int setSensitiveInfo(String loginId,String status);
	//设置用户认证投资人
	int setcertifiedInvestor(String loginId,String investor);
	//查询用户银行卡当日限额
	Row selDayLimitAmt(String userId);

	/**
	 *查询用户的推荐人
	 */
	Row selIntroducer(String userId);
	Row checkFinanPhoneInput(String mobile);
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
	Row getUserState(String userId);
}
