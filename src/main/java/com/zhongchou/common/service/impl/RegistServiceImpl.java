package com.zhongchou.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dto.SendSmsDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IRegistService;
import com.zhongchou.common.service.ISmsService;
import com.zhongchou.common.zhongzhengSao.RegistSao;


@Service
public class RegistServiceImpl extends BaseSaoServiceImpl implements IRegistService {
	Logger logger=Logger.getLogger(RegistServiceImpl.class);

	@Autowired
	private IUserDao userDao;

	@Autowired
	private ISmsService smsService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean sendSms(SendSmsDto sendSms) {
		try {
			// 向手机发短信
			return smsService.sendSms(sendSms);
		} catch (Exception ex) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return false;
	}


	@Override
	@Transactional(propagation = Propagation.NEVER)
	public boolean checkRefcodeExists(String invitationCode) {

		Row invitationCodeMap = userDao.checkRefcodeExists(invitationCode);
		return invitationCodeMap.isEmpty();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean registUser(UserDto user) {
		try {
			// 插入USER数据--TBL_USER
			if (!insUser(user)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 插入USER详情数据数据--TBL_USER_DETAIL
			if (!insUserDetail(user)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 插入推荐人数据--TBL_USER_INTRODUCER_M
			if (!StringUtils.isEmpty(user.getIntroducerUserMobile())){
				user.setIntroducerUserId(ConvUtils.convToString(userDao.getUserByMobile(user.getIntroducerUserMobile()).get("OID_USER_ID")));
				if(!StringUtils.isEmpty(user.getIntroducerUserId())){
					if (!insIntroducer(user)) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return false;
					}
				}else{
					logger.info("用户："+user.getOidUserId()+"邀请人手机号："+user.getIntroducerUserMobile()+"未找到");
				}
			}
			// USERS_ACCOUNT表插入用户数据
			if (userDao.insUserAccount(user) == 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// USERS_POINT_M表插入用户数据(积分表注册时不用插入数据)
			/*if (userDao.insUserPoint(user) == 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}*/
			// USERS_ADDRESS表插入用户数据
			if (userDao.insUserAddress(user) == 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// USER_ID_VERIFICATION表插入用户数据
			if (userDao.insUserIdVerification(user) == 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}

			// 返回的参数
			Map<String,Object> retMap = new HashMap<String,Object>();
			// 带入的参数
			Map<String,Object> setMap = new HashMap<String,Object>();

			String seq = nextSeq();
			RegistSao RegistSao = new RegistSao(seq);

			setMap.put("loginId", user.getOidUserId());

			// 调用调用接口，返回报文体
			retMap = RegistSao.setEncryptData(setMap);

			if("000000".equals(retMap.get("rsp_code").toString())){

				Map<String,Object> parame  = new HashMap<String,Object>();

				parame.put("ssFlag", "1");
				parame.put("loginId", user.getOidUserId());

				// USER表插入用户数据
				if (userDao.updRegistUser(parame) == 0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}else{
				logger.info("用户："+user.getOidUserId()+"注册中证失败");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.info("用户："+user.getOidUserId()+"注册失败");
		}
		return false;
	}

	private boolean insUser(UserDto user) {
//		Row customMap = userDao.getMinCustomerService();
//		user.setOidCustomerserviceId(ConvUtils.convToString(customMap.get("ID")));
		user.setOidCustomerserviceId("0");
		return userDao.insUser(user) > 0;
	}

	private boolean insUserDetail(UserDto user) {
		return userDao.insUserDetail(user) > 0;
	}

	private boolean insIntroducer(UserDto user) {
		return userDao.insIntroducer(user) > 0;
	}
	//邀请码查询邀请人的手机号
	@Override
	@Transactional(propagation = Propagation.NEVER)
	public String selInvitationPhone(String invitationCode) {
		Row InvitationPhone = userDao.selInvitationPhone(invitationCode);
		return ConvUtils.convToString(InvitationPhone.get("MOBILE"));
	}
}
