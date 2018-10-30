package com.zhongchou.common.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.yanshang.util.MessageUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IPCMessageDao;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dao.IUserDetailDao;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.zhongzhengSao.QueryUserInfoSao;
import com.zhongchou.common.zhongzhengSao.ResetPayPwdSao;
import com.zhongchou.common.zhongzhengSao.SendSmsSao;
import com.zhongchou.common.zhongzhengSao.UpdatePayPwdSao;

/**
 * 登录业务逻辑的接口。
 */
@Service
public class UserServiceImpl extends BaseSaoServiceImpl implements IUserService{
	Logger logger=Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IPCMessageDao messageDao;
	/**
	 * 用户详细数据操作的接口。
	 */
	@Autowired
	private IUserDetailDao userDetailDao;


	@Override
	public UserDto getUser(UserDto user) {

		Row row = userDao.getUser(user);

		user.setOidUserId((String)row.getString("OID_USER_ID"));//用户OID
		user.setUserId((String)row.getString("USER_ID"));//用户ID
		user.setMobile((String)row.getString("MOBILE"));//手机号
		user.setEmail((String)row.getString("EMAIL"));//邮箱
		user.setLoginTimes((String)row.getString("LOGIN_TIMES"));//用户登录次数
		user.setUserPwd((String)row.getString("USER_PWD"));//用户登录密码
		user.setUserPwdStrength((String)row.getString("USER_PWD_STRENGTH"));//用户登录密码强度
		user.setUserLevel(null);//用户级别
		user.setLoginErrorTimes((String)row.getString("LOGIN_ERROR_TIMES"));//用户登录错误次数
		user.setLastLoginDate((Date)row.get("LAST_LOGIN_DATE"));//最后登录时间
		user.setUserType((String)row.getString("USER_TYPE"));//用户类型（"0":个人用户 1:机构用户）
		user.setInvestmentFlg((String)row.getString("INVESTMENT_FLG"));//投资用户标示 (0:无效 1:有效)
		user.setFinancingFlg((String)row.getString("FINANCING_FLG"));//融资用户标示(0:无效 1:有效)
		user.setEmailVerifyFlg((String)row.getString("EMAIL_VERIFY_FLG"));//邮箱验证标示（0：未验证，1：已验证）
		user.setDelFlg((String)row.getString("DEL_FLG"));//删除标识(0:有效 1:无效)
		user.setOidCustomerserviceId((String)row.getString("OID_CUSTOMER_SERVICE_ID"));//客服id
		user.setNickname(null);//用户昵称
		user.setUserIconFileId(null);//头像文件ID
		user.setUserRealName(null);//用户真实姓名
		user.setIdCard(null);//身份证号
		user.setIdCardFileId1(null);//身份证正面
		user.setIdCardFileId2(null);//身份证反面
		user.setIdCardFileId3(null);//手持身份证
		user.setIdCardErrorTimes(null);//身份证号错误次数
		user.setUserPoint(null);//用户积分
		user.setIdCardVerifyFlg(null);//身份证验证标示（0：未验证，1：已验证）
		user.setWealthManagerFlg(null);//理财经理人标识（0：否，1：是）
		user.setGuaranteeFlg(null);//担保类型(0:否 1:是)
		user.setUserComment(null);//用户说明
		user.setUserPayPwd((String)row.getString("USER_PAY_PWD"));//用户支付密码
		user.setIntroducerUserId(null);//推荐人用户OID
		user.setIntroducerUserMobile(null);//推荐人手机号
		user.setRefcode(null);//邀请码
		user.setOrgDetail(null);//机构详细Dto
		user.setProvince(null);//省
		user.setCity(null);//市
		user.setAddress(null);//收货地址
		user.setAutoFinanceTendeFlg((String)row.getString("AUTO_FINANCE_TENDER_FLG"));//自动投标授权Flg
		user.setAutoFinanceProdFlg((String)row.getString("AUTO_FINANCE_PROD_FLG"));//自动理财授权Flg
		user.setAutoShowOrHideFlg((String)row.getString("AUTO_SHOW_OR_HIDE_FLG"));//总资产等数据，显示还是不显示Flg
		user.setDiploma(null);//最高学历
		user.setUniversity(null);//毕业院校
		user.setCompany(null);//公司名称
		user.setIndustry(null);//公司行业
		user.setScale(null);//公司规模
		user.setPosition(null);//职位
		user.setIncome(null);//月收入
		user.setRecommendedByPhone(null);//推荐人(手机号)
		user.setSalt((String)row.getString("SALT"));
		user.setZdhkFlag(null);
		user.setRegisterSource((String)row.getString("REGISTER_SOURCE"));//注册来源 （0手机注册，空 电脑注册，1公众号注册）
		user.setType((String)row.getString("TYPE"));//属性 0：正常用户 1：测试用户
		user.setSsFlag((String)row.getString("SS_FLAG"));//双创接口是否注册（0未注册 1已注册）
		user.setTcFlag((String)row.getString("TC_FLAG"));//是否绑卡 0否1是
		user.setRaFlag((String)row.getString("RA_FLAG"));//是否进行风险评估 0否1是

		return user;
	}

	@Override
	public UserDto getUserInfo(String loginId) {

		Row row = userDao.getUserInfo(loginId);

		UserDto user = new UserDto();
		user.setOidUserId((String)row.getString("OID_USER_ID"));//用户OID
		user.setMobile((String)row.getString("MOBILE"));//手机号
		user.setEmail((String)row.getString("EMAIL"));//邮箱
		user.setInvestmentFlg((String)row.getString("INVESTMENT_FLG"));//是否认证合格投资人
		user.setUserPwd("******");//用户登录密码
		user.setUserIconFileId((String)row.getString("USER_ICON_FILE_ID"));//头像文件ID
		user.setUserPayPwd(StringUtils.isEmpty(row.getString("USER_PAY_PWD"))?"":"******");//用户支付密码
		user.setRiskLvl((String)row.getString("RISK_LEVEL"));//风险评估等级
		user.setUserRealName((String)row.getString("USER_NAME"));//真实姓名
		user.setIdCard((String)row.getString("ID_CARD"));//身份证号
		return user;
	}

	@Override
	public boolean updUserIconFileId(String loginId,String headPortrait) {

		if(userDao.updUserIconFileId(loginId,headPortrait) == 0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updNewUserPhone(String loginId,String newUserPhone,Map paramMap) {

		if(userDao.updUserPhone(loginId,newUserPhone) == 0){
			return false;
		}else{
			// user_modify_log表插入用户信息表更改记录表
			if (userDetailDao.addUserModifyLog(paramMap) == 0) {
				logger.error("插入用户更改记录表数据失败");
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				return false;
			}
		return true;
		}
	}

	@Override
	public Map updPayPassword(String loginId,String newPayPassword,String oldPayPassword) {
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("loginId", loginId);
		resMap.put("newPwd", newPayPassword);
		resMap.put("oldPwd", oldPayPassword);

		try {
			String seq = nextSeq();
			UpdatePayPwdSao rps = new UpdatePayPwdSao(seq);
			Map backMap = rps.setEncryptData(resMap);
			if("000000".equals((String)backMap.get("rsp_code"))){
				if(userDao.updPayPassword(loginId,newPayPassword) == 0){
					logger.info("用户："+loginId+"修改密码更新user表失败");
					return backMap;
				}else if(userDao.updPayPwdForUserIdVer(loginId,newPayPassword) == 0){
					logger.info("用户："+loginId+"修改密码更新USER_ID_VERIFICATION表失败");
					return backMap;
				}else{
					return backMap;
				}
			}else{
				return backMap;
			}

		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 重置支付密码获取短信
	 * @param user
	 * @return
	 */

	@Override
	public String getSmsCode(UserDto user) {
		Map<String,Object> map = new HashMap<>();
		map.put("loginId", user.getOidUserId());
		map.put("mobile", user.getMobile());
		map.put("smsType", "03");
		String seq = nextSeq();
		SendSmsSao smsSao = new SendSmsSao(seq);
		Map dataMap = smsSao.setEncryptData(map);
		if(dataMap!=null&&!StringUtils.isEmpty((String)dataMap.get("reqSsn"))){
			return (String)dataMap.get("reqSsn");
		}
		return null;
	}
	/**
	 * 重置支付密码
	 */

	@Override
	public Map<String,Object> resetPayPassword(Map<String, Object> paramMap) {
			String seq = nextSeq();
			ResetPayPwdSao pwdSao = new ResetPayPwdSao(seq);
			Map setEncryptData = pwdSao.setEncryptData(paramMap);
			if("000000".equals((String)setEncryptData.get("rsp_code"))){
				if(userDao.updPayPassword((String)paramMap.get("loginId"),(String)paramMap.get("newPwd")) == 0){
					 setEncryptData.put("rsp_code", "000011");
					 setEncryptData.put("rsp_desc", "更新user表支付密码失败");
					 return setEncryptData;
				}
			}
			return setEncryptData;

	}
	//查询用户是否有我的项目的权限
	@Override
	public int getMyProject(String loginId) {
		return userDao.getMyProject(loginId);
	}

	//设置用户敏感信息查看方式
	@Override
	public int setSensitiveInfo(String loginId,String status) {
		return userDao.setSensitiveInfo(loginId,status);
	}
	//设置用户敏感信息查看方式
	@Override
	public int setcertifiedInvestor(String loginId,String investor) {
		return userDao.setcertifiedInvestor(loginId, investor);
	}
	//查询用户在中证的实名信息
	@Override
	public Map selUserInfo(String loginId) {
		Map setData = new HashMap();
		setData.put("loginId", loginId);
		QueryUserInfoSao userSao = new QueryUserInfoSao(nextSeq());
		return userSao.setEncryptData(setData);
	}
	/**
	 * 添加用户信息更改记录
	 * @param user
	 * @return
	 */

	@Override
	public boolean addUserModifyLog(Map paramMap) {
		// user_modify_log表插入用户信息表更改记录表
		if (userDetailDao.addUserModifyLog(paramMap) == 0) {
			logger.error("插入用户更改记录表数据失败");
			return false;
		}
		//新增用户的风险测评消息
		Map msgMap = new HashMap();
		msgMap.put("msgTitle",MessageUtils.getMessage("MSG0005",paramMap.get("afterChange")));
		msgMap.put("styleTitle","");
		msgMap.put("msgContent","去看看最近有哪些好项目");
		msgMap.put("msgType","1");//系统消息
		msgMap.put("msgStuType","2");//2:风险测评消息
		msgMap.put("oidUserId",paramMap.get("loginId"));
		messageDao.insMessage(msgMap);
		return true;
	}
	/**
	 * 认证投资人
	 * @param realInvest
	 * @return
	 */
	@Override
	public int certifiedInvestor(Map<String, String> realInvest) {
		int count = userDao.certifiedInvestor(realInvest);
		if(count>0&&!"4".equals(realInvest.get("investment"))){//认证成功且有效
			//新增用户的认证投资人消息
			Map msgMap = new HashMap();
			msgMap.put("msgTitle",MessageUtils.getMessage("MSG0001"));
			msgMap.put("styleTitle","");
			msgMap.put("msgContent","恭喜！您已成功认证壹盐双创合格投资人。");
			msgMap.put("msgType","1");//系统消息
			msgMap.put("msgStuType","4");//4:认证合格投资人消息
			msgMap.put("oidUserId",realInvest.get("userId"));
			msgMap.put("oidUserId",realInvest.get("userId"));
			messageDao.insMessage(msgMap);
		}
		return count;
	}
	/**
	 * 获取用户已有状态信息
	 * @param userId
	 * @return
	 */
	@Override
	public Map<String, Map<String, String>> getUserState(String userId) {
		Row stateRow = userDao.getUserState(userId);
		Map<String, Map<String, String>> userMap = new HashMap<>();
		Map<String,String> signFlagMap = new HashMap<>();// 是否签约 0否1是
		Map<String,String> authFlagMap = new HashMap<>();//认证标示(0：未认证；1：已认证)
		Map<String,String> tcFlagMap = new HashMap<>();//是否绑卡 0否1是
		Map<String,String> raFlagMap = new HashMap<>();//是否进行风险评估 0否1是
		//用户状态（是否实名；认证（认证标识；及内容）；绑卡(包括绑卡信息)；风险评测（风险等级；投资行为分类：进取型））
		if(stateRow.isEmpty()){
			return userMap;
		}else{
			//认证（认证标识；及内容）
			authFlagMap.put("authFlag", stateRow.getString("AUTH_FLAG"));
			authFlagMap.put("certifiedInvestor", stateRow.getString("CERTIFIED_INVESTOR"));
			authFlagMap.put("userName", stateRow.getString("USER_NAME"));
			authFlagMap.put("idCard", stateRow.getString("ID_CARD"));
			//绑卡(包括绑卡信息)
			tcFlagMap.put("tcFlag", stateRow.getString("tc_flag"));
			tcFlagMap.put("userName", stateRow.getString("USER_NAME"));
			tcFlagMap.put("idCard", stateRow.getString("ID_CARD"));
			tcFlagMap.put("cardNo", StringUtil.hideCardNum(stateRow.getString("CARD_NO")));
			tcFlagMap.put("bankName", stateRow.getString("BANK_NM"));
			//是否进行风险评估
			raFlagMap.put("riskLevel", stateRow.getString("RISK_LEVEL"));
			raFlagMap.put("raFlag", stateRow.getString("ra_flag"));
			//是否签约
			signFlagMap.put("singFlag", stateRow.getString("SIGN_FLAG"));

			userMap.put("signFlagMap", signFlagMap);
			userMap.put("authFlagMap", authFlagMap);
			userMap.put("tcFlagMap", tcFlagMap);
			userMap.put("raFlagMap", raFlagMap);
			return userMap;
		}
	}

}
