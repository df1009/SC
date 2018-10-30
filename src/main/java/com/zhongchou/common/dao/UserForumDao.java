package com.zhongchou.common.dao;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.UserForum;

/**
 * 评论业务数据操作的接口。
 */
public interface UserForumDao {
	/**
	 * 根据上级id查询当前楼层数
	 *
	 * @param projectId  项目ID
	 * @param fatherFloor 父节点楼层
	 * @return int
	 */
	int selFloorCount(String projectId, String fatherFloor);


	/**
	 * 插入评论
	 *
	 */
	int insertForum(UserForum userForum);

	/**
	 * 删除评论
	 *
	 * @param id  项目ID
	 * @param userId 用户id
	 * @return int
	 */
	int delForum(String id, String userId);

	/**
	 * 查询主评论
	 *
	 */
	List<Row> selMainForum(UserForum userForum);
	/**
	 * 查询当前评论的子评论
	 *
	 */
	List<Row> selSonComment(String level);

	/**
	 * 查询当前评论总数
	 *
	 */
	int countForum();

	/**
	 * 根据id查询当前评论所在楼层
	 *
	 */
	List<Row> selFloorNumber(Map strMap);

	}
