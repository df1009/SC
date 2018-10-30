package com.zhongchou.common.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanshang.config.Config;
import com.yanshang.config.ConfigKey;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.MD5;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IIndexDao;
import com.zhongchou.common.dao.ILoginDao;
import com.zhongchou.common.dto.AdminUserDto;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.CompanionDto;
import com.zhongchou.common.dto.CooperateDto;
import com.zhongchou.common.dto.OrgDetailDto;
import com.zhongchou.common.dto.ProjectDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.dto.UserForum;
import com.zhongchou.common.loginOut.controller.login.LoginController;
import com.zhongchou.common.service.IIndexService;
import com.zhongchou.common.service.ILoginService;




/**
 * 主页逻辑的实现类。
 */
@Service
public class IndexServiceImpl implements IIndexService {
	Logger logger=Logger.getLogger(LoginController.class);

	@Autowired
	private IIndexDao indexDao;

	@Override
	public List<BannerDto> getBannerList(String type) {
		List<BannerDto> returnMessage = new ArrayList<BannerDto>();
		List<Row> message = indexDao.getBannerList(type);//1:首页2：双创项目3：登录页
		for (Row row : message) {
			if (!row.isEmpty()) {
				BannerDto bannerDto = new BannerDto();
				bannerDto.setBannerUrl((String)row.get("IMG_PATH"));
				bannerDto.setBannerLink((String)row.get("LINK_URL"));
				bannerDto.setBannerAppUrl((String)row.get("APP_IMG_PATH"));
				bannerDto.setBannerAppLink((String)row.get("APP_LINK_URL"));
				returnMessage.add(bannerDto);
			}
		}
		return returnMessage;
	}

	@Override
	public List<CompanionDto> getCompanionList() {
		List<CompanionDto> returnMessage = new ArrayList<CompanionDto>();
		List<Row> message = indexDao.getCompanionList();
		for (Row row : message) {
			if (!row.isEmpty()) {
				CompanionDto companionDto = new CompanionDto();
				companionDto.setCompanionName((String)row.get("NAME"));
				companionDto.setCompanionLink((String)row.get("WEBSITE"));
				companionDto.setImgUrl((String)row.get("LINK_FILE_PATH"));
				returnMessage.add(companionDto);
			}
		}
		return returnMessage;
	}

	@Override
	public List<CooperateDto> getCooperateList() {
		List<CooperateDto> returnMessage = new ArrayList<CooperateDto>();
		List<Row> message = indexDao.getCooperateList();
		for (Row row : message) {
			if (!row.isEmpty()) {
				CooperateDto cooperateDto = new CooperateDto();
				cooperateDto.setCooperateName((String)row.get("NAME"));
				cooperateDto.setCooperateLink((String)row.get("WEBSITE"));
				cooperateDto.setImgUrl((String)row.get("LINK_FILE_PATH"));
				returnMessage.add(cooperateDto);
			}
		}
		return returnMessage;
	}

	@Override
	public int checkState(String userId) {
		Row message = indexDao.getStateMessage(userId);
		int returnMessage = 0;
		if (!message.isEmpty()) {
			returnMessage = Integer.parseInt(message.get("TC_FLAG").toString());
		}
		return returnMessage;
	}

	@Override
	public int checkRsState(String userId) {
		Row message = indexDao.getRsStateMessage(userId);
		int returnMessage = 0;
		if (!message.isEmpty()) {
			returnMessage = Integer.parseInt(message.get("RA_FLAG").toString());
		}
		return returnMessage;
	}
	//添加反馈
	@Override
	public int addFeedback(Map insMap) {
		return indexDao.insFeedback(insMap);
	}
}
