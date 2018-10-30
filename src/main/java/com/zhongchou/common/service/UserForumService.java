package com.zhongchou.common.service;

import java.util.List;

import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.dto.UserForum;



/**
 * 评论业务逻辑的接口。
 */
public interface UserForumService {

	/**
	 * 查询当前楼层数
	 *
	 * @param projectId  项目ID
	 * @param fatherFloor 父节点楼层
	 * @return int
	 */
	int insertForum(UserForum userForum);

	/**
	 * 查询当前楼层数
	 *
	 * @param projectId  项目ID
	 * @param fatherFloor 父节点楼层
	 * @return int
	 */
	int delForum(String id,String userID);

	/**
	 * 查询某页评论
	 *
	 */
	List<UserForum> selMainForum(UserForum userForum,UserDto user);

	/**
	 * 查询评论总数和页数
	 *
	 */
	int countForum();
}
