package com.zhongchou.common.dao.impl;

import java.util.List;

import org.springframework.util.StringUtils;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.IHelpDao;
import com.zhongchou.common.dao.IIndexDao;
import com.zhongchou.common.dto.BannerDto;

/**
 * 获取帮助中心后台的实现类。
 */

public class HelpDaoImpl extends BaseDao implements IHelpDao {

	@Override
	public Row getSafetyMessage(String showId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  ");
		sql.append("CONTENTS ");
		sql.append(" from help_center ");
		sql.append(" WHERE DEL_FLG = '0' ");
		sql.append(" AND PAGE_ID = ? ");
		return query(sql.toString(), new Object[]{showId});
	}

	//获取当前可下载的最新app信息
	@Override
	public Row getDownloadApp() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  ");
		sql.append(" * ");
		sql.append(" from app_vertion_manage ");
		sql.append(" WHERE DEL_FLAG = '0' ");
		sql.append(" AND DEFAULT_FLAG = '0' ");
		return query(sql.toString(), new Object[]{});
	}
}

