package com.zhongchou.common.dto;

public class BankCardDto {
	String bankCd;//银行代码
	String bankNm;//银行名称
	String mobileNo;//银行预留手机号
	String singleLimitAmt;//单笔限额
	String dayLimitAmt;//单日限额
	public String getBankCd() {
		return bankCd;
	}
	public void setBankCd(String bankCd) {
		this.bankCd = bankCd;
	}
	public String getBankNm() {
		return bankNm;
	}
	public void setBankNm(String bankNm) {
		this.bankNm = bankNm;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getSingleLimitAmt() {
		return singleLimitAmt;
	}
	public void setSingleLimitAmt(String singleLimitAmt) {
		this.singleLimitAmt = singleLimitAmt;
	}
	public String getDayLimitAmt() {
		return dayLimitAmt;
	}
	public void setDayLimitAmt(String dayLimitAmt) {
		this.dayLimitAmt = dayLimitAmt;
	}


}
