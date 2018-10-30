package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;


/**
 * 用户银行卡数据操作的接口。
 */
public interface IUserAccountBindBankDao {

	/**
	 * 查询用户详细数据的方法。
	 *
	 * @param parame 持有更新的用户的数据传输对象
	 * @return 用户银行卡数据
	 */
	Row getUserAccountBindBank(Map<String, Object> parame);

	/**
	 * 插入用户银行卡数据的方法。
	 *
	 * @param parame 持有更新的用户的数据传输对象
	 * @return 插入件数
	 */

	int insUserAccountBindBank(Map<String, Object> parame);

	/**
	 * 更新用户银行卡数据的方法。
	 *
	 * @param parame 持有更新的用户的数据传输对象
	 * @return 更新件数
	 */

	int updUserAccountBindBank(Map<String, Object> parame);

	/**
	 * 查询是否为主卡
	 * @param paramMap
	 * @return
	 */
	Row queryIsMasterCard(Map<String, Object> paramMap);
	/**
	 * 逻辑取消银行卡
	 * @param paramMap
	 * @return
	 */
	int cancelBackCard(Map<String, Object> paramMap);


	/**
	 * 查询银行限额
	 *
	 */
	List<Row> selBankWithholdQuota(String bankCd);
	/**
	 * 查询用户所有有效银行卡
	 *
	 */
	List<Row> queryBankCard(String loginId);
	/**
	 * 重新绑定之前的银行卡更新状态
	 * @param paramMap
	 * @return
	 */
	int updateBankCard(Map<String, Object> paramMap);
	/**
	 * 根据用户卡号查询id
	 * @param paramMap
	 * @return
	 */
	Row selOidAccountBindBankId(String loginId,String bankNo);

	//壹理财老用户注册第二部返显信息
	Row selBankSmg(String loginId);
	/**
	 * 查询中证支持的银行列表
	 * @param paramMap
	 */
	List<Row> selSupportBankList();
	/**
	 * 查询用户绑卡信息
	 * @param paramMap
	 */
	Row queryBankMobile(String loginId);
}
