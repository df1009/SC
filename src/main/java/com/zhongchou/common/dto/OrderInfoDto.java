package com.zhongchou.common.dto;

public class OrderInfoDto {

	private static final long serialVersionUID = 1L;
	/**订单状态 */
	private String orderStatus;
	/** 订单编号*/
	private String orderNum;
	/** 交易编号*/
	private String transactionNum;
	/**创建时间 */
	private String creatTime;
	/**付款时间 */
	private String paymentTime;
	/**实付金额 */
	private String actualPayment;
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getTransactionNum() {
		return transactionNum;
	}
	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public String getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}
	public String getActualPayment() {
		return actualPayment;
	}
	public void setActualPayment(String actualPayment) {
		this.actualPayment = actualPayment;
	}
}
