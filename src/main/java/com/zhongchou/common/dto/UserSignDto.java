package com.zhongchou.common.dto;

import com.zhongchou.common.model.SearchConditionBase;


public class UserSignDto extends SearchConditionBase {

	private static final long serialVersionUID = 1L;

	/** 用户OID */
	private String oidUserId;
	/** 最后签到时间  */
	private String lastSignDate;
	/** 连续签到天数  */
	private String continuitySignCnt;

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
	 * @return the lastSignDate
	 */
	public String getLastSignDate() {
		return lastSignDate;
	}
	/**
	 * @param lastSignDate the lastSignDate to set
	 */
	public void setLastSignDate(String lastSignDate) {
		this.lastSignDate = lastSignDate;
	}
	/**
	 * @return the continuitySignCnt
	 */
	public String getContinuitySignCnt() {
		return continuitySignCnt;
	}
	/**
	 * @param continuitySignCnt the continuitySignCnt to set
	 */
	public void setContinuitySignCnt(String continuitySignCnt) {
		this.continuitySignCnt = continuitySignCnt;
	}

}
