package com.zhongchou.common.dto;

import java.io.Serializable;

/**
 * 机构用户详细Dto类
 *
 */
public class OrgDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 头像文件ID */
	private String orgIconFileId = "";
	/** 机构名称 */
	private String orgName = "";
	/** 省 */
	private String province = "";
	/** 市 */
	private String city = "";
	/** 地址 */
	private String address = "";
	/** 机构类型 */
	private String orgType = "";
	/** 开户银行许可证号 */
	private String banklicenseCode = "";
	/** 组织机构代码 */
	private String orgNo = "";
	/** 营业执照编号 */
	private String businessCode = "";
	/** 税务登记号 */
	private String taxNo = "";
	/** 法人姓名 */
	private String legalName = "";
	/** 法人身份证号 */
	private String legalIdCard = "";
	/** 联系人姓名 */
	private String contactName = "";
	/** 联系人电话 */
	private String contactTel = "";
	/** 联系人手机号 */
	private String contactMobile = "";
	/** 联系人邮箱 */
	private String contactEmail = "";
	/** 联系人手机验证标示（0：未验证，1：已验证） */
	private String contactMobileVerifyFlg = "0";
	/** 作成时间 */
	private String insDate = null;
	/** 支付认证区分 */
	private String paymentVerifyFlg = null;
	/** 机构信息审核Flg */
	private String orgVerifyFlg = null;


	/**
	 * 取得头像文件ID
	 * @return orgIconFileId
	 */
	public String getOrgIconFileId() {
		return orgIconFileId;
	}
	/**
	 * 设置头像文件ID
	 * @param orgIconFileId 要设置的 orgIconFileId
	 */
	public void setOrgIconFileId(String orgIconFileId) {
		this.orgIconFileId = orgIconFileId;
	}
	/**
	 * 取得机构名称
	 * @return orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * 设置机构名称
	 * @param orgName 要设置的 orgName
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * 取得省
	 * @return province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置省
	 * @param province 要设置的 province
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 取得市
	 * @return city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 设置市
	 * @param city 要设置的 city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 取得地址
	 * @return address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置地址
	 * @param address 要设置的 address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 取得开户银行许可证号
	 * @return banklicenseCode
	 */
	public String getBanklicenseCode() {
		return banklicenseCode;
	}
	/**
	 * 设置开户银行许可证号
	 * @param banklicenseCode 要设置的 banklicenseCode
	 */
	public void setBanklicenseCode(String banklicenseCode) {
		this.banklicenseCode = banklicenseCode;
	}
	/**
	 * 取得机构类型
	 * @return orgType
	 */
	public String getOrgType() {
		return orgType;
	}
	/**
	 * 设置机构类型
	 * @param orgType 要设置的 orgType
	 */
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	/**
	 * 取得组织机构代码
	 * @return orgNo
	 */
	public String getOrgNo() {
		return orgNo;
	}
	/**
	 * 设置组织机构代码
	 * @param orgNo 要设置的 orgNo
	 */
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}
	/**
	 * 取得营业执照编号
	 * @return businessCode
	 */
	public String getBusinessCode() {
		return businessCode;
	}
	/**
	 * 设置营业执照编号
	 * @param businessCode 要设置的 businessCode
	 */
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	/**
	 * 取得税务登记号
	 * @return taxNo
	 */
	public String getTaxNo() {
		return taxNo;
	}
	/**
	 * 设置税务登记号
	 * @param taxNo 要设置的 taxNo
	 */
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	/**
	 * 取得法人姓名
	 * @return legalName
	 */
	public String getLegalName() {
		return legalName;
	}
	/**
	 * 设置法人姓名
	 * @param legalName 要设置的 legalName
	 */
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	/**
	 * 取得法人身份证号
	 * @return legalIdCard
	 */
	public String getLegalIdCard() {
		return legalIdCard;
	}
	/**
	 * 设置法人身份证号
	 * @param legalIdCard 要设置的 legalIdCard
	 */
	public void setLegalIdCard(String legalIdCard) {
		this.legalIdCard = legalIdCard;
	}
	/**
	 * 取得联系人姓名
	 * @return contactName
	 */
	public String getContactName() {
		return contactName;
	}
	/**
	 * 设置联系人姓名
	 * @param contactName 要设置的 contactName
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	/**
	 * 取得联系人电话
	 * @return contactTel
	 */
	public String getContactTel() {
		return contactTel;
	}
	/**
	 * 设置联系人电话
	 * @param contactTel 要设置的 contactTel
	 */
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	/**
	 * 取得联系人手机号
	 * @return contactMobile
	 */
	public String getContactMobile() {
		return contactMobile;
	}
	/**
	 * 设置联系人手机号
	 * @param contactMobile 要设置的 contactMobile
	 */
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	/**
	 * 取得联系人邮箱
	 * @return contactEnail
	 */
	public String getContactEmail() {
		return contactEmail;
	}
	/**
	 * 设置联系人邮箱
	 * @param contactEnail 要设置的 contactEnail
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	/**
	 * 取得联系人手机验证标示
	 * @return contactMobileVerifyFlg
	 */
	public String getContactMobileVerifyFlg() {
		return contactMobileVerifyFlg;
	}
	/**
	 * 设置联系人手机验证标示
	 * @param contactMobileVerifyFlg 要设置的 contactMobileVerifyFlg
	 */
	public void setContactMobileVerifyFlg(String contactMobileVerifyFlg) {
		this.contactMobileVerifyFlg = contactMobileVerifyFlg;
	}
	/**
	 * 设定作成时间
	 * @return insDate
	 */
	public String getInsDate() {
		return insDate;
	}
	/**
	 * 取得作成时间
	 * @param insDate 要设置的 insDate
	 */
	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}
	/**
	 * 取得支付认证区分
	 * @return paymentVerifyFlg
	 */
	public String getPaymentVerifyFlg() {
		return paymentVerifyFlg;
	}
	/**
	 * 设置支付认证区分
	 * @param paymentVerifyFlg 要设置的 paymentVerifyFlg
	 */
	public void setPaymentVerifyFlg(String paymentVerifyFlg) {
		this.paymentVerifyFlg = paymentVerifyFlg;
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public String getOrgVerifyFlg() {
		return orgVerifyFlg;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param orgVerifyFlg
	 */
	public void setOrgVerifyFlg(String orgVerifyFlg) {
		this.orgVerifyFlg = orgVerifyFlg;
	}
}
