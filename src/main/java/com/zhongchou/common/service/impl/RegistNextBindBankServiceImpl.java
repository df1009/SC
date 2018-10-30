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
import com.zhongchou.common.dao.IUserAccountBindBankDao;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dao.IUserDetailDao;
import com.zhongchou.common.dao.IUserIdVerificationDao;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IregistNextBindBankService;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.zhongzheng.cipher.MD5;
import com.zhongchou.common.zhongzhengSao.BankSmsSao;
import com.zhongchou.common.zhongzhengSao.QueryBankBinSao;
import com.zhongchou.common.zhongzhengSao.TiedBankCardSao;
@Service
public class RegistNextBindBankServiceImpl extends BaseSaoServiceImpl implements
		IregistNextBindBankService {
	private static final Logger logger=Logger.getLogger(RegistNextBindBankServiceImpl.class);
	/**
	 * 用户数据操作的接口。
	 */
	@Autowired
	private IUserDao userDao;

	/**
	 * 用户详细数据操作的接口。
	 */
	@Autowired
	private IUserDetailDao userDetailDao;

	/**
	 * 用户身份验证数据操作的接口。
	 */
	@Autowired
	private IUserIdVerificationDao userIdVerificationDao;

	/**
	 * 用户银行卡数据操作的接口。
	 */
	@Autowired
	private IUserAccountBindBankDao userAccountBindBankDao;

	/**
	 * 姓名银行卡验证
	 * @param parameter 接口参数
	 * @return 流水号
	 */
	public Map<String,Object> getBankSms(Map<String,Object> parameter) {

		// 返回的参数
		Map<String,Object> retMap = new HashMap<String,Object>();
		// 带入的参数
		Map<String,Object> setMap = new HashMap<String,Object>();

		String seq = nextSeq();
		BankSmsSao bankSmsSao = new BankSmsSao(seq);

		setMap.put("loginId",(String)parameter.get("loginId") );//登录帐号
		setMap.put("userNmCn", (String)parameter.get("userName"));//姓名
		setMap.put("idTp", (String)parameter.get("idTp"));//证件类型1：身份证2：护照3：军官证4：士兵证5：港澳通行证6：户口本7：其他
		setMap.put("idNo", (String)parameter.get("idCard"));//证件号码
		setMap.put("cardNo", (String)parameter.get("bankCode"));//银行卡
		setMap.put("mobileNo", (String)parameter.get("mobile"));//银行预留手机号
		// 调用接口，返回报文体
		retMap = bankSmsSao.setEncryptData(setMap);

		return retMap;
	}

	/**
	 * 开通理财账户(绑卡)
	 * @param parameter 接口参数
	 * @return 成功失败
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String,Object> getTiedBankCard(Map<String,Object> parameter) {

		// 返回的参数
		Map<String,Object> retMap = new HashMap<String,Object>();
		// 带入的参数
		Map<String,Object> setMap = new HashMap<String,Object>();

		String seq = nextSeq();
		TiedBankCardSao tiedBankCardSao = new TiedBankCardSao(seq);

		String loginId = (String) parameter.get("loginId");
		String payPassword = null;
		try {
			payPassword = MD5.encode((String) parameter.get("payPassword"));
		} catch (Exception e) {
			return null;
		}

		setMap.put("userIp", (String) parameter.get("userIp"));//登录Ip
		setMap.put("loginId", loginId);//登录帐号
		setMap.put("userNmCn", (String) parameter.get("userName"));//姓名
		setMap.put("idTp", (String) parameter.get("idTp"));//证件类型1：身份证2：护照3：军官证4：士兵证5：港澳通行证6：户口本7：其他
		setMap.put("idNo", (String) parameter.get("idCard"));//证件号码
		setMap.put("cardNo", (String) parameter.get("bankCode"));//银行卡
		setMap.put("mobileNo", (String) parameter.get("mobile"));//银行预留手机号
		setMap.put("payPwd", payPassword);//支付密码
		setMap.put("yzmSsn", (String) parameter.get("BankReqSsn"));//验证码流水
		setMap.put("yzmContent", (String)parameter.get("verificationCode"));//验证码

		//调用接口，返回报文体
		retMap = tiedBankCardSao.setEncryptData(setMap);
		//获取银行卡代码
		String seqBin = nextSeq();
		QueryBankBinSao queryBankBinSao = new QueryBankBinSao(seqBin);
		Map bankCdMap = queryBankBinSao.setEncryptData(setMap);
		if ("000000".equals(retMap.get("rsp_code").toString())) {

			parameter.put("payChannel", retMap.get("payChannel"));// 支付渠道
			parameter.put("needBankSign", retMap.get("needBankSign"));// 是否需要银商签约
			parameter.put("payPassword", payPassword);// MD5 32位加密后的支付密码
			parameter.put("bankCd", (String) bankCdMap.get("bankCd"));
		
			retMap.put("bankCd", bankCdMap.get("bankCd"));
			UserDto userDto = new UserDto();
			userDto.setOidUserId(loginId);

			// 查询用户信息
			//Row user = userDao.getExistUser(userDto);
			String isSign = StringUtil.isBankSign(ConvUtils.convToString(bankCdMap.get("bankCd")));
			//不需要签约的银行直接显示已签约
			if("0".equals(isSign))
				userDto.setSignFlag("1");
			if (userDao.updUser(userDto) == 0) {
				logger.error("更新用户签约信息失败");
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				retMap.put("rsp_code", "000009");
				retMap.put("rsp_desc", "更新用户签约信息失败");
			}
			// User表更新用户绑卡和支付密码
			if (userDao.updBankUser(parameter) == 0) {
				logger.error("更新用户绑卡标识和支付密码失败");
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				retMap.put("rsp_code", "000004");
				retMap.put("rsp_desc", "更新用户绑卡标识和支付密码失败");
			}

			// USER_DETAIL表更新用户详细信息
			if (userDetailDao.updUserDetail(parameter) == 0) {
				logger.error("更新用户详细信息失败");
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				retMap.put("rsp_code", "000005");
				retMap.put("rsp_desc", "更新用户详细信息失败");
			}

			// USER_ID_VERIFICATION表更新用户身份验证信息
			if (userIdVerificationDao.updUserIdVerification(parameter) == 0) {
				if(userIdVerificationDao.insUserIdVerification(parameter) == 0){
					logger.error("更新用户身份验证信息失败");
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
					retMap.put("rsp_code", "000006");
					retMap.put("rsp_desc", "更新用户身份验证信息失败");
				}

			}

			// USER_ACCOUNT_BIND_BANK表插入用户银行卡管理表数据
			if (userAccountBindBankDao.updUserAccountBindBank(parameter) == 0) {
				if (userAccountBindBankDao.insUserAccountBindBank(parameter) == 0) {
					logger.error("插入用户银行卡管理表数据失败");
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
					retMap.put("rsp_code", "000007");
					retMap.put("rsp_desc", "插入用户银行卡管理表数据失败");
				}
			}
			parameter.put("afterChange", parameter.get("bankCode"));
			parameter.put("count", "用户实名绑卡");
			// user_modify_log表插入用户信息表更改记录表
			if (userDetailDao.addUserModifyLog(parameter) == 0) {
				logger.error("插入用户更改记录表数据失败");
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				retMap.put("rsp_code", "000008");
				retMap.put("rsp_desc", "ModifyLog error");
			}
		}
		return retMap;
	}



}
