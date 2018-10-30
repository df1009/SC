package com.zhongchou.common.dto;

import java.util.Date;

import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.model.SearchConditionBase;


/**
 * 用户信息dto类
 */
public class UserDto extends SearchConditionBase {

	public String getVerifyFlag() {
		return verifyFlag;
	}
	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}
	private static final long serialVersionUID = 1L;

	/** 用户OID */
	private String oidUserId;
	/** 用户ID */
	private String userId;
	/** 手机号 */
	private String mobile = "";
	/** 邮箱 */
	private String email;
	/** 用户登录次数 */
	private String loginTimes;
	/** 用户登录密码 */
	private String userPwd;
	/** 用户登录密码强度*/
	private String userPwdStrength;
	/** 用户级别 */
	private String userLevel;
	/** 用户登录错误次数 */
	private String loginErrorTimes;
	/** 最后登录时间 */
	private Date lastLoginDate;
	/** 用户类型（"0":个人用户; 1:机构用户;） */
	private String userType;
	/** 投资用户标示 (0:无效; 1:有效)*/
	private String investmentFlg;
	/** 融资用户标示(0:无效; 1:有效) */
	private String financingFlg;
	/** 邮箱验证标示（0：未验证，1：已验证） */
	private String emailVerifyFlg;
	/** 删除标识(0:有效; 1:无效) */
	private String delFlg = "0";
	/** 客服id */
	private String oidCustomerserviceId = "";
	/** 用户昵称 */
	private String nickname = "";
	/** 头像文件ID */
	private String userIconFileId = "";
	/** 用户真实姓名 */
	private String userRealName = "";
	/** 身份证号 */
	private String idCard = "";
	/** 身份证正面 */
	private String idCardFileId1 = "";
	/** 身份证反面 */
	private String idCardFileId2 = "";
	/** 手持身份证 */
	private String idCardFileId3 = "";
	/** 身份证号错误次数 */
	private String idCardErrorTimes;
	/** 用户积分 */
	private String userPoint = "";
	/** 身份证验证标示（0：未验证，1：已验证） */
	private String idCardVerifyFlg  = "";
	/** 理财经理人标识（0：否，1：是） */
	private String wealthManagerFlg = "";
	/** VIP标识（false：否，true：是） */
	private boolean isVipFlg;
	/** 担保类型(0:否; 1:是) */
	private String guaranteeFlg = "";
	/** 用户说明 */
	private String userComment = "";
	/** 用户支付密码 */
	private String userPayPwd = "";
	/** 推荐人用户OID */
	private String introducerUserId = "";
	/** 推荐人手机号 */
	private String introducerUserMobile = "";
	/** 邀请码 */
	private String refcode = "";
	/** 错误代码 */
	private int errorCode;
	/** 机构详细Dto */
	private OrgDetailDto orgDetail;
	/** 省 */
	private String province;
	/** 市 */
	private String city;
	/** 收货地址 */
	private String address;
	/** 自动投标授权Flg */
	private String autoFinanceTendeFlg;
	/** 自动理财授权Flg */
	private String autoFinanceProdFlg;
	/** 总资产等数据，显示还是不显示Flg */
	private String autoShowOrHideFlg;

	/** 最高学历 */
	private String diploma;
	/** 毕业院校 */
	private String university;
	/** 公司名称 */
	private String company;
	/** 公司行业 */
	private String industry;
	/** 公司规模 */
	private String scale;
	/** 职位 */
	private String position;
	/** 月收入 */
	private String income;
	/** 推荐人(手机号) */
	private String recommendedByPhone;

	private String salt;

	private String zdhkFlag = "";

	//注册来源 （0手机注册，空 电脑注册，1公众号注册）
	private String registerSource = "";
	/** 属性 0：正常用户 1：测试用户*/
	private String type = "";

	/** 中证接口是否注册（0未注册 1已注册） */
	private String ssFlag;

	/** 是否绑卡 0否1是 */
	private String tcFlag ;

	/** 是否进行风险评估 0否1是  */
	private String raFlag ;

	/** 风险等级  */
	private String riskLvl ;

	/** 最后登录IP  */
	private String lastLoginIp ;

	/** 投资总额显示标识  */
	private String isDisplay ;

	/** 认证投资人信息  */
	private String certifiedInvestor ;

	/** 是否签约 0否1是  */
	private String signFlag ;

	/** 是否在中证实名 0否1是  */
	private String verifyFlag ;

	/**认证投资人认证标示*/
	private String authFlag;

	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	/**
	 * 取得diploma的方法。
	 * @return the diploma
	 */
	public String getDiploma() {
		return diploma;
	}
	/**
	 * 设置diploma的方法。
	 * @param diploma the diploma to set
	 */
	public void setDiploma(String diploma) {
		this.diploma = diploma;
	}
	/**
	 * 取得university的方法。
	 * @return the university
	 */
	public String getUniversity() {
		return university;
	}
	/**
	 * 设置university的方法。
	 * @param university the university to set
	 */
	public void setUniversity(String university) {
		this.university = university;
	}
	/**
	 * 取得company的方法。
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * 设置company的方法。
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 取得industry的方法。
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}
	/**
	 * 设置industry的方法。
	 * @param industry the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	/**
	 * 取得scale的方法。
	 * @return the scale
	 */
	public String getScale() {
		return scale;
	}
	/**
	 * 设置scale的方法。
	 * @param scale the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}
	/**
	 * 取得position的方法。
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * 设置position的方法。
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * 取得income的方法。
	 * @return the income
	 */
	public String getIncome() {
		return income;
	}
	/**
	 * 设置income的方法。
	 * @param income the income to set
	 */
	public void setIncome(String income) {
		this.income = income;
	}
	/**
	 * 取得recommendedByPhone的方法。
	 * @return the recommendedByPhone
	 */
	public String getRecommendedByPhone() {
		return recommendedByPhone;
	}
	/**
	 * 设置recommendedByPhone的方法。
	 * @param recommendedByPhone the recommendedByPhone to set
	 */
	public void setRecommendedByPhone(String recommendedByPhone) {
		this.recommendedByPhone = recommendedByPhone;
	}
	/**
	 * 取得客服id详细Dto
	 * @return oidCustomerserviceId
	 */
	public String getOidCustomerserviceId() {
		return oidCustomerserviceId;
	}
	/**
	 * 设置客服id
	 * @param oidCustomerserviceId 要设置的 oidCustomerserviceId
	 */
	public void setOidCustomerserviceId(String oidCustomerserviceId) {
		this.oidCustomerserviceId = oidCustomerserviceId;
	}
	/**
	 * 取得机构详细Dto
	 * @return orgDetail
	 */
	public OrgDetailDto getOrgDetail() {
		return orgDetail;
	}
	/**
	 * 设置机构详细Dto
	 * @param oidUserId 要设置的 orgDetail
	 */
	public void setOrgDetail(OrgDetailDto orgDetail) {
		this.orgDetail = orgDetail;
	}
	/**
	 * 取得用户OID
	 * @return oidUserId
	 */
	public String getOidUserId() {
		return oidUserId;
	}
	/**
	 * 设置用户OID
	 * @param oidUserId 要设置的 oidUserId
	 */
	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}
	/**
	 * 取得用户ID
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置用户ID
	 * @param userId 要设置的 userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 取得手机号
	 * @return mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置手机号
	 * @param mobile 要设置的 mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 取得邮箱
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置邮箱
	 * @param email 要设置的 email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 取得用户登录次数
	 * @return loginTimes
	 */
	public String getLoginTimes() {
		return loginTimes;
	}
	/**
	 * 设置用户登录次数
	 * @param loginTimes 要设置的 loginTimes
	 */
	public void setLoginTimes(String loginTimes) {
		this.loginTimes = loginTimes;
	}
	/**
	 * 取得用户登录次数
	 * @return userPwd
	 */
	public String getUserPwd() {
		return userPwd;
	}
	/**
	 * 设置用户登录次数
	 * @param userPwd 要设置的 userPwd
	 */
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	/**
	 * 取得用户登录密码强度
	 * @return userPwdStrength
	 */
	public String getUserPwdStrength() {
		return userPwdStrength;
	}
	/**
	 * 设置用户登录密码强度
	 * @param userPwdStrength 要设置的 userPwdStrength
	 */
	public void setUserPwdStrength(String userPwdStrength) {
		this.userPwdStrength = userPwdStrength;
	}
	/**
	 * 取得用户级别
	 * @return userLevel
	 */
	public String getUserLevel() {
		return userLevel;
	}
	/**
	 * 设置用户级别
	 * @param userLevel 要设置的 userLevel
	 */
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	/**
	 * 取得用户登录错误次数
	 * @return loginErrorTimes
	 */
	public String getLoginErrorTimes() {
		return loginErrorTimes;
	}
	/**
	 * 设置用户登录错误次数
	 * @param loginErrorTimes 要设置的 loginErrorTimes
	 */
	public void setLoginErrorTimes(String loginErrorTimes) {
		this.loginErrorTimes = loginErrorTimes;
	}
	/**
	 * 取得最后登录时间
	 * @return lastLoginDate
	 */
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	/**
	 * 设置最后登录时间
	 * @param lastLoginDate 要设置的 lastLoginDate
	 */
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	/**
	 * 取得头像文件ID
	 * @return userIconFileId
	 */
	public String getUserIconFileId() {
		return userIconFileId;
	}
	/**
	 * 设置头像文件ID
	 * @param userIconFileId 要设置的 userIconFileId
	 */
	public void setUserIconFileId(String userIconFileId) {
		this.userIconFileId = userIconFileId;
	}
	/**
	 * 取得用户类型
	 * @return userType
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * 设置用户类型
	 * @param userType 要设置的 userType
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	/**
	 * 取得投资用户标示
	 * @return investmentFlg
	 */
	public String getInvestmentFlg() {
		return investmentFlg;
	}
	/**
	 * 设置投资用户标示
	 * @param investmentFlg 要设置的 investmentFlg
	 */
	public void setInvestmentFlg(String investmentFlg) {
		this.investmentFlg = investmentFlg;
	}
	/**
	 * 取得融资用户标示
	 * @return financingFlg
	 */
	public String getFinancingFlg() {
		return financingFlg;
	}
	/**
	 * 设置融资用户标示
	 * @param financingFlg 要设置的 financingFlg
	 */
	public void setFinancingFlg(String financingFlg) {
		this.financingFlg = financingFlg;
	}
	/**
	 * 取得邮箱验证标示
	 * @return emailVerifyFlg
	 */
	public String getEmailVerifyFlg() {
		return emailVerifyFlg;
	}
	/**
	 * 设置邮箱验证标示
	 * @param emailVerifyFlg 要设置的 emailVerifyFlg
	 */
	public void setEmailVerifyFlg(String emailVerifyFlg) {
		this.emailVerifyFlg = emailVerifyFlg;
	}
	/**
	 * 取得删除标识
	 * @return delFlg
	 */
	public String getDelFlg() {
		return delFlg;
	}
	/**
	 * 设置删除标识
	 * @param delFlg 要设置的 delFlg
	 */
	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	/**
	 * 取得用户昵称
	 * @return nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * 设置用户昵称
	 * @param nickname 要设置的 nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * 取得用户真实姓名
	 * @return userRealName
	 */
	public String getUserRealName() {
		return userRealName;
	}
	/**
	 * 设置用户真实姓名
	 * @param userRealName 要设置的 userRealName
	 */
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
	/**
	 * 取得身份证号
	 * @return idCard
	 */
	public String getIdCard() {
		return idCard;
	}
	/**
	 * 设置身份证号
	 * @param idCard 要设置的 idCard
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	/**
	 * 取得身份证正面图片
	 * @return the idCardFileId1
	 */
	public String getIdCardFileId1() {
		return idCardFileId1;
	}
	/**
	 * 设置身份证正面图片
	 * @param idCardFileId1 the idCardFileId1 to set
	 */
	public void setIdCardFileId1(String idCardFileId1) {
		this.idCardFileId1 = idCardFileId1;
	}
	/**
	 * 取得身份证反面图片
	 * @return the idCardFileId2
	 */
	public String getIdCardFileId2() {
		return idCardFileId2;
	}
	/**
	 * 设置身份证反面图片
	 * @param idCardFileId2 the idCardFileId2 to set
	 */
	public void setIdCardFileId2(String idCardFileId2) {
		this.idCardFileId2 = idCardFileId2;
	}
	/**
	 * 取得手持身份证图片
	 * @return the idCardFileId3
	 */
	public String getIdCardFileId3() {
		return idCardFileId3;
	}
	/**
	 * 设置手持身份证图片
	 * @param idCardFileId3 the idCardFileId3 to set
	 */
	public void setIdCardFileId3(String idCardFileId3) {
		this.idCardFileId3 = idCardFileId3;
	}
	/**
	 * 取得身份证号认证错误次数
	 * @return idCard
	 */
	public String getIdCardErrorTimes() {
		return idCardErrorTimes;
	}
	/**
	 * 设置身份证号认证错误次数
	 * @param idCardErrorTimes
	 */
	public void setIdCardErrorTimes(String idCardErrorTimes) {
		this.idCardErrorTimes = idCardErrorTimes;
	}
	/**
	 * 取得用户积分
	 * @return userPoint
	 */
	public String getUserPoint() {
		return userPoint;
	}
	/**
	 * 设置用户积分
	 * @param userPoint 要设置的 userPoint
	 */
	public void setUserPoint(String userPoint) {
		this.userPoint = userPoint;
	}
	/**
	 * 取得身份证验证标示
	 * @return idCardVerifyFlg
	 */
	public String getIdCardVerifyFlg() {
		return idCardVerifyFlg;
	}
	/**
	 * 设置身份证验证标示
	 * @param idCardVerifyFlg 要设置的 idCardVerifyFlg
	 */
	public void setIdCardVerifyFlg(String idCardVerifyFlg) {
		this.idCardVerifyFlg = idCardVerifyFlg;
	}
	/**
	 * 取得理财经理人标识
	 * @return wealthManagerFlg
	 */
	public String getWealthManagerFlg() {
		return wealthManagerFlg;
	}
	/**
	 * 设置理财经理人标识
	 * @param wealthManagerFlg 要设置的 wealthManagerFlg
	 */
	public void setWealthManagerFlg(String wealthManagerFlg) {
		this.wealthManagerFlg = wealthManagerFlg;
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public boolean isVipFlg() {
		return isVipFlg;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param isVipFlg
	 */
	public void setVipFlg(boolean isVipFlg) {
		this.isVipFlg = isVipFlg;
	}
	/**
	 * 取得担保类型
	 * @return guaranteeFlg
	 */
	public String getGuaranteeFlg() {
		return guaranteeFlg;
	}
	/**
	 * 设置担保类型
	 * @param guaranteeFlg 要设置的 guaranteeFlg
	 */
	public void setGuaranteeFlg(String guaranteeFlg) {
		this.guaranteeFlg = guaranteeFlg;
	}
	/**
	 * 取得用户说明
	 * @return userComment
	 */
	public String getUserComment() {
		return userComment;
	}
	/**
	 * 设置用户说明
	 * @param userComment 要设置的 userComment
	 */
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	/**
	 * 取得用户支付密码
	 * @return userPayPwd
	 */
	public String getUserPayPwd() {
		return userPayPwd;
	}
	/**
	 * 设置用户支付密码
	 * @param userPayPwd 要设置的 userPayPwd
	 */
	public void setUserPayPwd(String userPayPwd) {
		this.userPayPwd = userPayPwd;
	}
	/**
	 * 取得推荐人用户OID
	 * @return introducerUserId
	 */
	public String getIntroducerUserId() {
		return introducerUserId;
	}
	/**
	 * 设置推荐人用户OID
	 * @param introducerUserId 要设置的 introducerUserId
	 */
	public void setIntroducerUserId(String introducerUserId) {
		this.introducerUserId = introducerUserId;
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public String getIntroducerUserMobile() {
		return introducerUserMobile;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param introducerUserMobile
	 */
	public void setIntroducerUserMobile(String introducerUserMobile) {
		this.introducerUserMobile = introducerUserMobile;
	}
	/**
	 * 取得错误代码
	 * @return errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * 设置错误代码
	 * @param errorCode 要设置的 errorCode
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getUserNameView() {
		if(Constants.USER_TYPE_PERSON.equals(getUserType())) {
			return getMobile();
		} else {
			return getEmail();
		}
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param province
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public String getAutoFinanceTendeFlg() {
		return autoFinanceTendeFlg;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param autoFinanceTendeFlg
	 */
	public void setAutoFinanceTendeFlg(String autoFinanceTendeFlg) {
		this.autoFinanceTendeFlg = autoFinanceTendeFlg;
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public String getAutoFinanceProdFlg() {
		return autoFinanceProdFlg;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param autoFinanceProdFlg
	 */
	public void setAutoFinanceProdFlg(String autoFinanceProdFlg) {
		this.autoFinanceProdFlg = autoFinanceProdFlg;
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public String getRefcode() {
		return refcode;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param refcode
	 */
	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}
	public String getZdhkFlag() {
		return zdhkFlag;
	}
	public void setZdhkFlag(String zdhkFlag) {
		this.zdhkFlag = zdhkFlag;
	}
	public String getRegisterSource() {
		return registerSource;
	}
	public void setRegisterSource(String registerSource) {
		this.registerSource = registerSource;
	}
	/**
	 * 获取type
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置type
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	public String getAutoShowOrHideFlg() {
		return autoShowOrHideFlg;
	}
	public void setAutoShowOrHideFlg(String autoShowOrHideFlg) {
		this.autoShowOrHideFlg = autoShowOrHideFlg;
	}
	public String getSsFlag() {
		return ssFlag;
	}
	public void setSsFlag(String ssFlag) {
		this.ssFlag = ssFlag;
	}
	public String getTcFlag() {
		return tcFlag;
	}
	public void setTcFlag(String tcFlag) {
		this.tcFlag = tcFlag;
	}
	public String getRaFlag() {
		return raFlag;
	}
	public void setRaFlag(String raFlag) {
		this.raFlag = raFlag;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public String getRiskLvl() {
		return riskLvl;
	}
	public void setRiskLvl(String riskLvl) {
		this.riskLvl = riskLvl;
	}
	public String getSignFlag() {
		return signFlag;
	}
	public void setSignFlag(String signFlag) {
		this.signFlag = signFlag;
	}
	public String getCertifiedInvestor() {
		return certifiedInvestor;
	}
	public void setCertifiedInvestor(String certifiedInvestor) {
		this.certifiedInvestor = certifiedInvestor;
	}
	public String getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}
	public String getAuthFlag() {
		return authFlag;
	}
	public void setAuthFlag(String authFlag) {
		this.authFlag = authFlag;
	}


}
