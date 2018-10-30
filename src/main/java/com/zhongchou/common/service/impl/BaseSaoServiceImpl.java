package com.zhongchou.common.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongchou.common.dao.ICommonDao;
import com.zhongchou.common.loginOut.controller.login.LoginController;
import com.zhongchou.common.service.BaseSaoService;




/**
 * 登录业务逻辑的实现类。
 */
@Service
public class BaseSaoServiceImpl implements BaseSaoService {
	Logger logger=Logger.getLogger(LoginController.class);

	@Autowired
	private ICommonDao commonDao;

	@Override
	public String nextSeq() {
		String seq = "";
		synchronized(this){
			seq = commonDao.nextSeq("REQ_ZHONGZHENG").toString();
		}
		return seq;
	}





}
