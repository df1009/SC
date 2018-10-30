package com.zhongchou.common.dto;

import com.zhongchou.common.model.SearchConditionBase;


public class UserTenderDto extends SearchConditionBase {

	/** 订单的ID */
	private String oidTenderID;
	/** 订单的编号 */
	private String tenderSsn;

	/** 项目名称 */
	private String productName;

	/** 投资金额数 */
	private String amountStr;

	/** 订单生成的时间 */
	private String insDate;

	/** 项目的状态 */
	private String productStatus;

	/** 项目的状态名称 */
	private String productStatusName;

	/** 订单的状态 */
	private String tenderStatus;


	/** 订单的状态名称 */
	private String tenderStatusName;

	/** 是否可以撤回 */
	private String isWithdraw;

	/** 融资项目id */
	private String projectCode;
	//订单图片
	private String smallImg;

	//购买成功后是否可撤单（0不可撤单，1可撤单）
	private String isRevoke;



	public String getIsRevoke() {
		return isRevoke;
	}

	public void setIsRevoke(String isRevoke) {
		this.isRevoke = isRevoke;
	}

	public String getSmallImg() {
		return smallImg;
	}

	public void setSmallImg(String smallImg) {
		this.smallImg = smallImg;
	}

	public String getTenderSsn() {
		return tenderSsn;
	}

	public void setTenderSsn(String tenderSsn) {
		this.tenderSsn = tenderSsn;
	}

	public String getTenderStatusName() {
		return tenderStatusName;
	}

	public void setTenderStatusName(String tenderStatusName) {
		this.tenderStatusName = tenderStatusName;
	}

	public String getProductStatusName() {
		return productStatusName;
	}

	public void setProductStatusName(String productStatusName) {
		this.productStatusName = productStatusName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getIsWithdraw() {
		return isWithdraw;
	}

	public void setIsWithdraw(String isWithdraw) {
		this.isWithdraw = isWithdraw;
	}

	public String getOidTenderID() {
		return oidTenderID;
	}

	public void setOidTenderID(String oidTenderID) {
		this.oidTenderID = oidTenderID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAmountStr() {
		return amountStr;
	}

	public void setAmountStr(String amountStr) {
		this.amountStr = amountStr;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getTenderStatus() {
		return tenderStatus;
	}

	public void setTenderStatus(String tenderStatus) {
		this.tenderStatus = tenderStatus;
	}


}
