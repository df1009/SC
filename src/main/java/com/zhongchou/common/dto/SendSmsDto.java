package com.zhongchou.common.dto;

import java.io.Serializable;

import com.zhongchou.common.enums.SmsContentType;
import com.zhongchou.common.enums.SmsType;

public class SendSmsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 手机号码 */
	private String phone;
	/** 发送状态 */
	private String sendStatus;
	/**
	 * 短信发送方式 1：手机短信 2：手机语音
	 */
	private SmsType smsType;

	/**
	 * 短信内容类型 01：注册 02：线上充值成功 03:线下充值成功 04:提现申请 05:提现成功 06:提现失败 07:还款提醒 08:成功还款
	 * 09:逾期提醒 10:回款提醒 11:回款到账 12:VIP认证通过 13:发标初审通过 14:发标流标 15:发标复审通过
	 * 16:手机验证时发送验证码 17:安全中心更改密码 18:安全中心更改手机号码
	 * 19 绑定银行卡；20 交易密码；21提现密码 22mail修改 23手机认证
	 */
	private SmsContentType smsContentType;
	/** 短信内容用：用户名 */
	private String userId;
	/** 短信内容用：金额 */
	private String money;
	// TODO
	/** 短信内容用：借款标标题 */
	private String borrowTitle;
	/** 验证码 */
	private String verifyCode;
	/** 短信内容 */
	private String contents;
	/** 日期 年月日时分 */
	private String time;
	/** 签名  */
	private String signature;
	/** 分页开始位置  */
	private int pageIndex;
	/** 每页最大显示数  */
	private int pageSize;
	/** 查找开始时间  */
	private String dateFrom;
	/** 查找结束时间  */
	private String dateTo;
	/** 短信认证类型  */
	private String operateType;
	/** 短信认证ID  */
	private String smsVerifyCodeId;

	/** 发送ip  */
	private String sendIp;

	private String sendMode;


	//定时任务短信发送标识
	private String sendFlag;

	public String getSendFlag() {
		return sendFlag;
	}


	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}


	public String getSendMode() {
		return sendMode;
	}


	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}


	/**
	 * @return the sendIp
	 */
	public String getSendIp() {
		return sendIp;
	}


	/**
	 * @param sendIp the sendIp to set
	 */
	public void setSendIp(String sendIp) {
		this.sendIp = sendIp;
	}

	/**
	 * @return the smsVerifyCodeId
	 */
	public String getSmsVerifyCodeId() {
		return smsVerifyCodeId;
	}

	/**
	 * @param smsVerifyCodeId the smsVerifyCodeId to set
	 */
	public void setSmsVerifyCodeId(String smsVerifyCodeId) {
		this.smsVerifyCodeId = smsVerifyCodeId;
	}

	/**
	 * @return the operateType
	 */
	public String getOperateType() {
		return operateType;
	}

	/**
	 * @param operateType the operateType to set
	 */
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	/**
	 * 签名的取值。
	 *
	 * @return 签名
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * 签名的设定。
	 *
	 * @param signature
	 *            签名
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * 日期的取值。
	 *
	 * @return 日期
	 */
	public String getTime() {
		return time;
	}

	/**
	 * 日期的设定。
	 *
	 * @param time
	 *            日期
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * 短信内容的取值。
	 *
	 * @return 短信内容
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * 短信内容的设定。
	 *
	 * @param contents
	 *            短信内容
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * 验证码的取值。
	 *
	 * @return 验证码
	 */
	public String getVerifyCode() {
		return verifyCode;
	}

	/**
	 * 验证码的设定。
	 *
	 * @param verifyCode
	 *            验证码
	 */
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	/**
	 * 手机号码的取值。
	 *
	 * @return 手机号码
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 手机号码的设定。
	 *
	 * @param phone
	 *            手机号码
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 发送方式（1：手机短信，2：手机语音）的取值。
	 *
	 * @return 发送方式（1：手机短信，2：手机语音）
	 */
	public SmsType getSmsType() {
		return smsType;
	}

	/**
	 * 发送方式（1：手机短信，2：手机语音）的设定。
	 *
	 * @param sendType
	 *            发送方式（1：手机短信，2：手机语音）
	 */
	public void setSmsType(SmsType smsType) {
		this.smsType = smsType;
	}

	/**
	 * 发送类型的取值。
	 *
	 * @return 发送类型
	 */
	public SmsContentType getSmsContentType() {
		return smsContentType;
	}

	/**
	 * 发送类型的设定。
	 *
	 * @param sendType
	 *            发送类型
	 */
	public void setSmsContentType(SmsContentType smsContentType) {
		this.smsContentType = smsContentType;
	}

	/**
	 * 短信内容用：用户名的取值。
	 *
	 * @return 短信内容用：用户名
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 短信内容用：用户名的设定。
	 *
	 * @param userId
	 *            短信内容用：用户名
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 短信内容用：金额的取值。
	 *
	 * @return 短信内容用：金额
	 */
	public String getMoney() {
		return money;
	}

	/**
	 * 短信内容用：金额的设定。
	 *
	 * @param money
	 *            短信内容用：金额
	 */
	public void setMoney(String money) {
		this.money = money;
	}

	/**
	 * 短信内容用：借款标标题的取值。
	 *
	 * @return 短信内容用：借款标标题
	 */
	public String getBorrowTitle() {
		return borrowTitle;
	}

	/**
	 * 短信内容用：借款标标题的设定。
	 *
	 * @param borrowTitle
	 *            短信内容用：借款标标题
	 */
	public void setBorrowTitle(String borrowTitle) {
		this.borrowTitle = borrowTitle;
	}

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the dateFrom
	 */
	public String getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * @return the dateTo
	 */
	public String getDateTo() {
		return dateTo;
	}

	/**
	 * @param dateTo the dateTo to set
	 */
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	/**
	 * @return the sendStatus
	 */
	public String getSendStatus() {
		return sendStatus;
	}

	/**
	 * @param sendStatus the sendStatus to set
	 */
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

}
