package com.zhongchou.common.dto;

public class APPProductListDto {

	private String productId;//产品id
	private String projectMainTitle;//项目主标题
	private String salesQuota;//总销售额度(募集额度)info表
	private String firstMinBuy;//个人最低起投金额(分)
	//private String surplusQuota;//剩余销售额度(分)
	private String smallImgPath;//列表缩略图
	private String  projectAdds;//项目所在地：上海
	private String  projectState;//项目状态
	private String projectcompanyName;//企业名称
	//private String company_regist_adds;//企业注册地址
	private String remainDays;//剩余天数
	private String buyTotalAmt;//已认购金额
	private String[] projectCharacter;//项目特点（标签）
	//private String deliveryTime;//交割时间
	private String payPersonNum;//认筹人数
	private String summaryText;//文字介绍
	private String percentage;//募集率
	private String showProjectFlag;//是否为展示项目（0否1是）





	public String getShowProjectFlag() {
		return showProjectFlag;
	}
	public void setShowProjectFlag(String showProjectFlag) {
		this.showProjectFlag = showProjectFlag;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getSalesQuota() {
		return salesQuota;
	}
	public void setSalesQuota(String salesQuota) {
		this.salesQuota = salesQuota;
	}
	public String getSummaryText() {
		return summaryText;
	}
	public void setSummaryText(String summaryText) {
		this.summaryText = summaryText;
	}
	public String getRemainDays() {
		return remainDays;
	}
	public void setRemainDays(String remainDays) {
		this.remainDays = remainDays;
	}
	public String getBuyTotalAmt() {
		return buyTotalAmt;
	}
	public void setBuyTotalAmt(String buyTotalAmt) {
		this.buyTotalAmt = buyTotalAmt;
	}
	public String getPayPersonNum() {
		return payPersonNum;
	}
	public void setPayPersonNum(String payPersonNum) {
		this.payPersonNum = payPersonNum;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProjectMainTitle() {
		return projectMainTitle;
	}
	public void setProjectMainTitle(String projectMainTitle) {
		this.projectMainTitle = projectMainTitle;
	}
	public String getFirstMinBuy() {
		return firstMinBuy;
	}
	public void setFirstMinBuy(String firstMinBuy) {
		this.firstMinBuy = firstMinBuy;
	}
	public String getSmallImgPath() {
		return smallImgPath;
	}
	public void setSmallImgPath(String smallImgPath) {
		this.smallImgPath = smallImgPath;
	}
	public String getProjectAdds() {
		return projectAdds;
	}
	public void setProjectAdds(String projectAdds) {
		this.projectAdds = projectAdds;
	}
	public String getProjectState() {
		return projectState;
	}
	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}
	public String getProjectcompanyName() {
		return projectcompanyName;
	}
	public void setProjectcompanyName(String projectcompanyName) {
		this.projectcompanyName = projectcompanyName;
	}
	public String[] getProjectCharacter() {
		return projectCharacter;
	}
	public void setProjectCharacter(String[] projectCharacter) {
		this.projectCharacter = projectCharacter;
	}

}
