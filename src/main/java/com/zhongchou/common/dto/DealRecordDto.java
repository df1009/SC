package com.zhongchou.common.dto;

import com.zhongchou.common.model.SearchConditionBase;

public class DealRecordDto extends SearchConditionBase {

	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	private static final long serialVersionUID = 1L;
	/**交易时间 */
	private String transactionDate;
	/**交易类型 */
	private String dealType;
	/**产品简称 */
	private String projectName;
	/**银行卡名称 */
	private String idCardName;
	/**银行卡号 */
	private String idCardNum;
	/**交易返回码 */
	private String returnCode;
	/**返回码说明 */
	private String codeExplain;
	/**交易状态 */
	private String transactionStatus;
	/**到账时间 */
	private String arrivalDate;
	/**交易金额 */
	private String moneyNum;
	/**交易金额 */
	private String amount;
	/**融资产品ID */
	private String projectId;
	/**订单ID */
	private String tenderId;
	/**订单图片 */
	private String tenderImg;





	public String getTenderImg() {
		return tenderImg;
	}
	public void setTenderImg(String tenderImg) {
		this.tenderImg = tenderImg;
	}
	public String getTenderId() {
		return tenderId;
	}
	public void setTenderId(String tenderId) {
		this.tenderId = tenderId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getCodeExplain() {
		return codeExplain;
	}
	public void setCodeExplain(String codeExplain) {
		this.codeExplain = codeExplain;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getIdCardName() {
		return idCardName;
	}
	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}
	public String getIdCardNum() {
		return idCardNum;
	}
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public String getMoneyNum() {
		return moneyNum;
	}
	public void setMoneyNum(String moneyNum) {
		this.moneyNum = moneyNum;
	}


}
