package com.zhongchou.common.dao;

import java.math.BigInteger;
import java.util.List;

import com.yanshang.util.Row;

/**
 * 共通数据操作的接口。
 */
public interface ICommonDao {

	/**
	 * 获取序列值的方法。
	 *
	 * @param sequenceName 序列名
	 * @return 序列值
	 */
	BigInteger nextSeq(String sequenceName);

}
