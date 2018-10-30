package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;

/**
 * 个人中心-银行卡管理
 */
public interface IUserBindbankDao {

	/**
	 * 根据oid获取我的账户下的银行卡
	 * 用户的IOD  oidUserId
	 * @return
	 */
	public List<Row> getUserBindbanks(String oidUserId);

	/**
	 * 删除银行卡
	 * 参数  param
	 * @return
	 */
	public int delUserBindbanks(Map<String,Object> param);
}
