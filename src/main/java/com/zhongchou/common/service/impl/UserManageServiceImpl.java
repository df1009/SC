package com.zhongchou.common.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.Row;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dao.ICommonDao;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IUserManageService;


/**
 * 用户中心业务逻辑的实现类。
 */
@Service
public class UserManageServiceImpl implements IUserManageService {
	/**
	 * 用户的数据访问对象。
	 */
	@Autowired
	private IUserDao userDao;
	/**
	 * 共通的数据访问对象。
	 */
	@Autowired
	private ICommonDao commonDao;

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public boolean checkMobileExists(String mobile, String... userType) {
		if (userType != null && userType.length > 0) {
			if (Constants.USER_TYPE_ORG.equals(userType[0])) {
				return !userDao.getOrgByContactMobile(mobile).isEmpty();
			}
		}
		return !userDao.getUserByMobile(mobile).isEmpty();
	}
	@Transactional(propagation = Propagation.NEVER)
	public boolean checkFinanPhoneInput(String mobile) {
		return !userDao.checkFinanPhoneInput(mobile).isEmpty();
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public Row getUser(UserDto user) {
		return userDao.getUser(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updateUser(UserDto user) {
		return userDao.updUser(user) > 0;
	}

	@Override
	public BigInteger nextSeq(String sequenceName) {
		return commonDao.nextSeq(sequenceName);
	}

	@Override
	public List selIntroducerPreson(String uId) {
		List<Row> introducerPreson = userDao.selIntroducerPreson(uId);
		List introducerPresonList = new ArrayList();
		for (Row row : introducerPreson) {
			if (!row.isEmpty()) {
				Map presonMap = new HashMap();
				presonMap.put("mobile", ConvUtils.convToString(row.get("MOBILE")));
				presonMap.put("userName", ConvUtils.convToString(row.get("USER_NAME")));
				presonMap.put("status", "成功");
				String insDate = ConvUtils.convToString(row.get("INS_DATE")).substring(0, 10);
				presonMap.put("insDate", insDate);
				introducerPresonList.add(presonMap);
				}
			}
		return introducerPresonList;
	}
	/**
	 * userId 用户id
	 * return Map 邀请人信息
	 */
	public Map selIntroducer(String userId) {
		return userDao.selIntroducer(userId);
	}


	/**
	 * 设置我的邀请人信息
	 * userMap 用户id
	 * return Map 邀请人信息
	 */
	public int setIntroducer(UserDto user) {
		return userDao.insIntroducer(user);
	}
}
