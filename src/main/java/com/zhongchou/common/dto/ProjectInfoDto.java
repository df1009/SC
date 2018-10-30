package com.zhongchou.common.dto;

import java.util.Date;

public class ProjectInfoDto {

	private static final long serialVersionUID = 1L;
	/** 项目简称 */
	private String projectAbbreviation ;
	/**产品代码  */
	private String projectsId;
	/** 企业法人 */
	private String legalPerson ;
	/** 企业组织机构代码 */
	private String enterpriseId;
	/** 成立时间 */
	private String establishTime;
	/** 行业类别 */
	private String industryCategory;
	/** 风险等级 */
	private String riskLevel;
	/** 风险等级内容 */
	private String riskLevelName;
	/** 股东人数 */
	private String numShareholder;
	/** 出让股份 */
	private String remiseShares;
	/** 目标额度（万元） */
	private String targetLimit ;
	/** 最低认购额度 */
	private String leastLimit;
	/** 最大认购额度 */
	private String maxLimit;
	/** 发行开始日期 */
	private String startIssue;
	/** 发行结束日期 */
	private String endIssue;
	/** 分红方式 */
	private String profitDistribution;
	/** 投资确认书 */
	private String confirmation ;
	/** 产品合同 */
	private String contract;
	/** 产品募集说明书 */
	private String RaiseInstructions ;
	/** 电子签名约定书 */
	private String appointment;


	public String getRiskLevelName() {
		return riskLevelName;
	}
	public void setRiskLevelName(String riskLevelName) {
		this.riskLevelName = riskLevelName;
	}
	/** 项目简称 */
	public String getProjectAbbreviation() {
		return projectAbbreviation;
	}
	/** 项目简称 */
	public void setProjectAbbreviation(String projectAbbreviation) {
		this.projectAbbreviation = projectAbbreviation;
	}
	/**产品代码  */
	public String getProjectsId() {
		return projectsId;
	}
	/**产品代码  */
	public void setProjectsId(String projectsId) {
		this.projectsId = projectsId;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getEstablishTime() {
		return establishTime;
	}
	public void setEstablishTime(String establishTime) {
		this.establishTime = establishTime;
	}
	public String getIndustryCategory() {
		return industryCategory;
	}
	public void setIndustryCategory(String industryCategory) {
		this.industryCategory = industryCategory;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getNumShareholder() {
		return numShareholder;
	}
	public void setNumShareholder(String numShareholder) {
		this.numShareholder = numShareholder;
	}
	public String getRemiseShares() {
		return remiseShares;
	}
	public void setRemiseShares(String remiseShares) {
		this.remiseShares = remiseShares;
	}
	public String getTargetLimit() {
		return targetLimit;
	}
	public void setTargetLimit(String targetLimit) {
		this.targetLimit = targetLimit;
	}
	public String getLeastLimit() {
		return leastLimit;
	}
	public void setLeastLimit(String leastLimit) {
		this.leastLimit = leastLimit;
	}
	public String getMaxLimit() {
		return maxLimit;
	}
	public void setMaxLimit(String maxLimit) {
		this.maxLimit = maxLimit;
	}
	public String getStartIssue() {
		return startIssue;
	}
	public void setStartIssue(String startIssue) {
		this.startIssue = startIssue;
	}
	public String getEndIssue() {
		return endIssue;
	}
	public void setEndIssue(String endIssue) {
		this.endIssue = endIssue;
	}
	public String getProfitDistribution() {
		return profitDistribution;
	}
	public void setProfitDistribution(String profitDistribution) {
		this.profitDistribution = profitDistribution;
	}
	public String getConfirmation() {
		return confirmation;
	}
	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	public String getRaiseInstructions() {
		return RaiseInstructions;
	}
	public void setRaiseInstructions(String raiseInstructions) {
		RaiseInstructions = raiseInstructions;
	}
	public String getAppointment() {
		return appointment;
	}
	public void setAppointment(String appointment) {
		this.appointment = appointment;
	}

}
