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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.yanshang.config.Config;
import com.yanshang.config.ConfigKey;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.MD5;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.ILoginDao;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dto.AdminUserDto;
import com.zhongchou.common.dto.OrgDetailDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.loginOut.controller.login.LoginController;
import com.zhongchou.common.service.ILoginService;
import com.zhongchou.common.zhongzhengSao.RegistSao;




/**
 * 登录业务逻辑的实现类。
 */
@Service
public class LoginServiceImpl extends BaseSaoServiceImpl implements ILoginService {
	Logger logger=Logger.getLogger(LoginController.class);
	/**
	 * 登录的数据访问对象。
	 */
	@Autowired
	private ILoginDao loginDao;
	@Autowired
	private IUserDao userDao;


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AdminUserDto adminAuth(String loginId, String password) {
		AdminUserDto adminUser = new AdminUserDto();
		Row rtn = loginDao.adminLogin(loginId);
		if (!rtn.isEmpty()) {
			if (ConvUtils.convToInt(rtn.get("LOGIN_ERROR_TIMES")) > (StringUtils.toInt(Config.getString(ConfigKey.MGT_LOGIN_ERROR_TIMES), 5)  - 1)) {
				adminUser.setErrorCode(2);
				return adminUser;
			} else {
				if (MD5.encryptMD5AndSalt(password).equals(rtn.get("ADMIN_USER_PWD"))) {
					loginDao.updAdminLastLoginDate(loginId);
					adminUser.setOidAdminUserId(ConvUtils.convToString(rtn.get("OID_ADMIN_USER_ID")));
					adminUser.setAdminUserId(ConvUtils.convToString(rtn.get("ADMIN_USER_ID")));
					adminUser.setAdminUserName(ConvUtils.convToString(rtn.get("ADMIN_USER_NAME")));
					adminUser.setSysFlg(ConvUtils.convToString(rtn.get("SYS_FLG")));
					adminUser.setOidGroupId(ConvUtils.convToString(rtn.get("GROUP_ID")));
					adminUser.setGroupName(ConvUtils.convToString(rtn.get("GROUP_NAME")));
					adminUser.setRoleId(ConvUtils.convToString(rtn.get("ROLE_ID")).split(","));

					List<Row> acl = loginDao.getACL(adminUser.getRoleId());
//
//					adminUser.setMenu(getMenu(acl));
//					adminUser.setUrl(getUrl(acl));
//					adminUser.setMenuNav(getMenuNav(acl));
					adminUser.setAcl(acl);
					setAdminUserACL(acl, adminUser);
					return adminUser;
				} else {
					loginDao.addAdminLoginErrorTimes(loginId);
					adminUser.setErrorCode(1);
					return adminUser;
				}
			}
		} else {
			adminUser.setErrorCode(3);
			return adminUser;
		}
	}

	/*
	 * 管理端获取具有访问权限的菜单列表的方法。
	 */
	private void setAdminUserACL(List<Row> acl, AdminUserDto adminUser) {
		// 菜单列表
		List<Row> mainMenu = new ArrayList<>();
		// 可访问的Url
		Map<String, String> url = new HashMap<>();
		// 菜单导航
		Map<String, String[]> menuNav = new HashMap<>();
		for (Row main : acl) {
			if (ConvUtils.convToInt(main.get("PARENT_MENU_ID")) == 0) {
				List<Row> subMenu = new ArrayList<>();
				for (Row sub : acl) {
					if (ConvUtils.convToInt(sub.get("PARENT_MENU_ID")) == ConvUtils.convToInt(main.get("MENU_ID"))) {
						subMenu.add(sub);
					}
				}
				main.put("SUB_MENU", subMenu);
				mainMenu.add(main);
			}
			if ("4".equals(ConvUtils.convToString(main.get("MENU_RANK")))) {
				url.put(ConvUtils.convToString(main.get("URL")), ConvUtils.convToString(main.get("PARENT_MENU_ID")));
			} else {
				url.put(ConvUtils.convToString(main.get("URL")), ConvUtils.convToString(main.get("MENU_ID")));
			}
			menuNav.put(ConvUtils.convToString(main.get("URL")), ConvUtils.convToString(main.get("MENU_NAV")).split(","));
		}
		adminUser.setMenu(mainMenu);
		adminUser.setUrl(url);
		adminUser.setMenuNav(menuNav);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
/*	@SystemLog(module="用户登录services",methods="用户登录services")*/
	public UserDto auth(String loginId, String password) {
		UserDto user = new UserDto();
		Row rtn = loginDao.login(loginId);
		if (!rtn.isEmpty()) {
			// 前台的帐户锁定已打开（可设定）
			// 并且登录次数大于5次（可设定）
			// 并且未到3小时的解锁时间（可设定），用户为锁定状态。
			if ("ON".equals(Config.getString(ConfigKey.LOGIN_ERROR_STATUS))
					&& ConvUtils.convToInt(rtn.get("LOGIN_ERROR_TIMES")) > (StringUtils.toInt(Config.getString(ConfigKey.LOGIN_ERROR_TIMES), 100)  - 1)
					&& !loginDao.isLoginErrorTime(loginId, StringUtils.toInt(Config.getString(ConfigKey.LOGIN_ERROR_TIME), 3))) {
					logger.info("用户："+user.getOidUserId()+"登录已锁定");
					user.setErrorCode(2);
					return user;
			} else {
				if (MD5.encryptMD5(rtn.getString("OID_USER_ID") + password + rtn.getString("SALT")).equals(rtn.get("USER_PWD"))) {
					// 登录成功
					user.setLastLoginDate((Date) rtn.get("LAST_LOGIN_DATE"));
					loginDao.updLastLoginDate(loginId);
					loginDao.addLoginTimes(loginId);
					user.setOidUserId(ConvUtils.convToString(rtn.get("OID_USER_ID")));//用户ID
					user.setUserId(ConvUtils.convToString(rtn.get("USER_ID")));
					user.setInvestmentFlg(ConvUtils.convToString(rtn.get("INVESTMENT_FLG")));
					user.setFinancingFlg(ConvUtils.convToString(rtn.get("FINANCING_FLG")));
					user.setUserType(ConvUtils.convToString(rtn.get("USER_TYPE")));
					user.setEmail(ConvUtils.convToString(rtn.get("EMAIL")));//邮箱
					user.setMobile(ConvUtils.convToString(rtn.get("MOBILE")));//手机号
					user.setUserPwd(ConvUtils.convToString(rtn.get("USER_PWD")));//登录密码
					user.setSalt(rtn.getString("SALT"));
					user.setUserPwdStrength(ConvUtils.convToString(rtn.get("USER_PWD_STRENGTH")));//用户登录密码强度
					user.setUserPayPwd(ConvUtils.convToString(rtn.get("USER_PAY_PWD")));//用户支付密码
					user.setLoginTimes(ConvUtils.convToString(rtn.get("LOGIN_TIMES")));//登录次数
					user.setLoginErrorTimes(ConvUtils.convToString(rtn.get("LOGIN_ERROR_TIMES")));//连续错误登录次数
					user.setLastLoginIp(ConvUtils.convToString(rtn.get("LAST_LOGIN_IP")));//最后登录IP
					user.setLastLoginDate((Date) rtn.get("LAST_LOGIN_DATE"));//最后登录时间
					user.setSsFlag(ConvUtils.convToString(rtn.get("SS_FLAG")));//双创接口是否注册（0未注册 1已注册）
					user.setTcFlag(ConvUtils.convToString(rtn.get("TC_FLAG")));//是否绑卡
					user.setRaFlag(ConvUtils.convToString(rtn.get("RA_FLAG")));//是否进行风险评估0否1是
					user.setIsDisplay(ConvUtils.convToString(rtn.get("SENSITIVEINFO_FLAG")));//用户敏感信息是否可见0否1是
					user.setCertifiedInvestor(ConvUtils.convToString(rtn.get("CERTIFIED_INVESTOR")));//用户认证投资人
					user.setSignFlag(ConvUtils.convToString(rtn.get("SIGN_FLAG")));//是否网银签约
					user.setAuthFlag(rtn.getString("AUTH_FLAG"));//认证标识（0：未认证；1：已认证;1.3.0版本)
					Row userinfo = loginDao.getUserInfo(user.getOidUserId(), user.getUserType());

					if ("0".equals(user.getUserType())) {
						user.setUserIconFileId(ConvUtils.convToString(userinfo.get("USER_ICON_FILE_ID")));//用户头像
						user.setUserRealName(ConvUtils.convToString(userinfo.get("USER_NAME")));;//用户真实姓名
						user.setIdCard(ConvUtils.convToString(userinfo.get("ID_CARD")));//身份证号
						user.setIdCardVerifyFlg(ConvUtils.convToString(userinfo.get("ID_CARD_VERIFY_FLG")));
						user.setWealthManagerFlg(ConvUtils.convToString(userinfo.get("WEALTH_MANAGER_FLG")));
						user.setRecommendedByPhone(ConvUtils.convToString(userinfo.get("RECOMMENDED_BY_PHONE")));//推荐人(手机号)
						user.setRiskLvl((ConvUtils.convToString(userinfo.get("RISK_LEVEL"))));//用户风险评级
						user.setRefcode((ConvUtils.convToString(userinfo.get("REFCODE"))));//用户邀请码
						user.setVerifyFlag(StringUtils.isEmpty(userinfo.getString("USER_NAME"))?"":"1");
					} else if ("1".equals(user.getUserType())) {
	//						user.setMobile(ConvUtils.convToString(userinfo.get("CONTACT_MOBILE")));
						OrgDetailDto orgDetail =  new OrgDetailDto();
						orgDetail.setOrgIconFileId(ConvUtils.convToString(userinfo.get("USER_ICON_FILE_ID")));
						orgDetail.setOrgName(ConvUtils.convToString(userinfo.get("ORG_NAME")));
						orgDetail.setProvince(ConvUtils.convToString(userinfo.get("PROVINCE")));
						orgDetail.setCity(ConvUtils.convToString(userinfo.get("CITY")));
						orgDetail.setAddress(ConvUtils.convToString(userinfo.get("ADDRESS")));
						orgDetail.setOrgType(ConvUtils.convToString(userinfo.get("ORG_TYPE")));
						orgDetail.setBanklicenseCode(ConvUtils.convToString(userinfo.get("BANKLICENSE_CODE")));
						orgDetail.setOrgNo(ConvUtils.convToString(userinfo.get("ORG_NO")));
						orgDetail.setBusinessCode(ConvUtils.convToString(userinfo.get("BUSINESS_CODE")));
						orgDetail.setTaxNo(ConvUtils.convToString(userinfo.get("TAX_NO")));
						orgDetail.setLegalName(ConvUtils.convToString(userinfo.get("LEGAL_NAME")));
						orgDetail.setLegalIdCard(ConvUtils.convToString(userinfo.get("LEGAL_ID_CARD")));
						orgDetail.setContactName(ConvUtils.convToString(userinfo.get("CONTACT_NAME")));
						orgDetail.setContactEmail(ConvUtils.convToString(userinfo.get("CONTACT_EMAIL")));
						orgDetail.setContactTel(ConvUtils.convToString(userinfo.get("CONTACT_TEL")));
						orgDetail.setContactMobile(ConvUtils.convToString(userinfo.get("CONTACT_MOBILE")));
						orgDetail.setContactMobileVerifyFlg(ConvUtils.convToString(userinfo.get("CONTACT_MOBILE_VERIFY_FLG")));
						user.setOrgDetail(orgDetail);
					}

					if(!"1".equals(user.getSsFlag())){//未注册中证
						Map setMap = new HashMap();
						RegistSao RegistSao = new RegistSao(nextSeq());
						setMap.put("loginId", user.getOidUserId());
						// 调用调用接口，返回报文体
						Map retMap = RegistSao.setEncryptData(setMap);
						if("000000".equals(retMap.get("rsp_code").toString())){
							Map<String,Object> parame  = new HashMap<String,Object>();
							parame.put("ssFlag", "1");
							parame.put("loginId", user.getOidUserId());
							// USER表插入用户数据
							if (userDao.updRegistUser(parame) == 0) {
								TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							}
						}else{
							user.setErrorCode(5);
						}

					}

					user.setVipFlg(false);
					return user;
				} else {
					// 前台的帐户锁定打开时
					if ("ON".equals(Config.getString(ConfigKey.LOGIN_ERROR_STATUS))) {
						// 密码错误时，错误登录次数+1
						loginDao.addLoginErrorTimes(loginId);
						// 错误登录次数重新取得
						Row rtnErrorTimes = loginDao.login(loginId);
						// 错误登录次数达到上限时
						if (ConvUtils.convToInt(rtnErrorTimes.get("LOGIN_ERROR_TIMES")) >= StringUtils.toInt(Config.getString(ConfigKey.LOGIN_ERROR_TIMES), 100)) {
							user.setErrorCode(2);
						//判断登陆失败后是否需要验证码
						} else if(ConvUtils.convToInt(rtnErrorTimes.get("LOGIN_ERROR_TIMES")) >= 3 && 3 <= StringUtils.toInt(Config.getString(ConfigKey.LOGIN_ERROR_TIME), 3)){
							// 密码输入错误,开始需要输入验证码
							user.setLoginErrorTimes(ConvUtils.convToString(rtn.get("LOGIN_ERROR_TIMES")));
							user.setErrorCode(6);
						}else{// 错误登录次数未达到上限时
							user.setLoginErrorTimes(ConvUtils.convToString(rtnErrorTimes.get("LOGIN_ERROR_TIMES")));
							user.setErrorCode(4);
						}
					// 前台的帐户锁定关闭时
					} else {
						user.setErrorCode(1);
					}
					return user;
				}
			}
		} else {
			user.setErrorCode(3);
			return user;
		}
	}
	//查询用户输入密码错误次数
	@Override
	public int selImgFlag(String mobile) {
		Row errorTimes = loginDao.selImgFlag(mobile);
		return Integer.parseInt(ConvUtils.convToString(errorTimes.getInt("LOGIN_ERROR_TIMES")));

	}

}
