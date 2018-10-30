package com.zhongchou.common.service.impl;

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
import com.zhongchou.common.dao.IHelpDao;
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
import com.zhongchou.common.service.IHelpCenterService;
import com.zhongchou.common.service.IIndexService;
import com.zhongchou.common.service.ILoginService;

/**
 * 帮助中心的实现类。
 */
@Service

public class HelpCenterServiceImpl implements IHelpCenterService {
	Logger logger=Logger.getLogger(LoginController.class);

	@Autowired
	private IHelpDao helpDao;

	//获取后台帮助中心
	@Override
	public String getSafetyInfo(String showId) {
		String returnMessage = "";
		Row sqlMessage = helpDao.getSafetyMessage(showId);
		returnMessage = (String)sqlMessage.get("CONTENTS");
		return returnMessage;

 }

	@Override
	public Map getDownloadApp() {
		Row downloadApp = helpDao.getDownloadApp();
		Map appMap = new HashMap();
		appMap.put("appVertion", downloadApp.get("APP_VERTION"));//app版本号
		appMap.put("downloadImg", downloadApp.get("APP_LOAD_IMG"));//app下载二维码
		appMap.put("downloadAdr", downloadApp.get("APP_ADDRESS"));//app更新路径
		appMap.put("downloadInstruction", downloadApp.get("INSTRUCTION"));//app更新说明
		return appMap;
	}
}