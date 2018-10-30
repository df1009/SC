package com.zhongchou.common.dto;

import java.util.Date;

import com.zhongchou.common.model.SearchConditionBase;

public class UserTender extends SearchConditionBase {

	private static final long serialVersionUID = 394846677714971390L;

	/** 投资ID */
	private String oidTenderId;
	/** 融资产品ID */
	private String oidPlatformProductsId;
	/** 投资用户ID */
	private String oidUserId;
	/**订单流水号 */
	private String tenderSsn;
	/** 投资金额 */
	private double tenderAmount;
	/** 投资类型 */
	private String tenderType;
	/** 投资状态 */
	private String tenderStatus;
	/** 自动状态 0自动 1手动 */
	private String autoStatus;
	/** 托管请求流水号 */
	private String requestNo;
	/** 作成日 */
	private Date insDate;
	/** 更新日 */
	private Date updDate;
	/** 所投资的项目最近一次更新时间 */
	private Date recentlyUpdDate;
	/** 所投资的项目经过当前投资,是否满标 */
	private boolean isFullAfterTender;
	/** 回款来源 */
	private String tenderfrom;
	/** 投资合同号 */
	private String contractNo;


	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}
	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
	public String getTenderfrom() {
		return tenderfrom;
	}
	/**
	 * 设置set的方法。
	 *
	 * @param tenderfrom
	 */
	public void setTenderfrom(String tenderfrom) {
		this.tenderfrom = tenderfrom;
	}
	public String getOidTenderId() {
		return oidTenderId;
	}
	public void setOidTenderId(String oidTenderId) {
		this.oidTenderId = oidTenderId;
	}
	public String getOidPlatformProductsId() {
		return oidPlatformProductsId;
	}
	public void setOidPlatformProductsId(String oidPlatformProductsId) {
		this.oidPlatformProductsId = oidPlatformProductsId;
	}
	public String getOidUserId() {
		return oidUserId;
	}
	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}
	public double getTenderAmount() {
		return tenderAmount;
	}
	public void setTenderAmount(double tenderAmount) {
		this.tenderAmount = tenderAmount;
	}
	public String getTenderStatus() {
		return tenderStatus;
	}
	public void setTenderStatus(String tenderStatus) {
		this.tenderStatus = tenderStatus;
	}
	public String getAutoStatus() {
		return autoStatus;
	}
	public void setAutoStatus(String autoStatus) {
		this.autoStatus = autoStatus;
	}
	public Date getInsDate() {
		return insDate;
	}
	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}
	public Date getUpdDate() {
		return updDate;
	}
	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public Date getRecentlyUpdDate() {
		return recentlyUpdDate;
	}
	public void setRecentlyUpdDate(Date recentlyUpdDate) {
		this.recentlyUpdDate = recentlyUpdDate;
	}
	public boolean isFullAfterTender() {
		return isFullAfterTender;
	}
	public void setFullAfterTender(boolean isFullAfterTender) {
		this.isFullAfterTender = isFullAfterTender;
	}
	public String getTenderType() {
		return tenderType;
	}
	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
	public String getTenderSsn() {
		return tenderSsn;
	}
	public void setTenderSsn(String tenderSsn) {
		this.tenderSsn = tenderSsn;
	}
}
