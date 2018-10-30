package com.zhongchou.common.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.ICommonDao;

/**
 * 共通数据操作的实现类。
 */
public class CommonDaoImpl extends BaseDao implements ICommonDao {

	@Override
	public BigInteger nextSeq(String sequenceName) {
		return nextSequence(sequenceName);
	}
}
