package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;

/**
 * 主页读取数据操作的接口。
 */
public interface IIndexDao {

	/**
	 * 获取banner list方法
	 *
	 * @return banner数据
	 */
	List<Row> getBannerList(String type);


	/**
	 * 获取合作机构方法
	 *
	 * @return 合作机构数据
	 */
	List<Row> getCompanionList();

	/**
	 * 获取战略合作方法
	 *
	 * @return 战略合作数据
	 */
	List<Row> getCooperateList();

	/**
	 * 获取验证状态
	 *
	 * @return 验证状态数据
	 */
	Row getStateMessage(String userId);

	/**
	 * 获取验证状态
	 *
	 * @return 验证状态数据
	 */
	Row getRsStateMessage(String userId);

	//添加反馈内容
	int insFeedback(Map feedbackMap);
}
