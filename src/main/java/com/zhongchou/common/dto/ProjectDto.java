package com.zhongchou.common.dto;

import com.zhongchou.common.model.SearchConditionBase;

/**
 * 项目精选
 *
 */
public class ProjectDto extends SearchConditionBase {

	public String getProjectMainTitle() {
		return projectMainTitle;
	}

	public void setProjectMainTitle(String projectMainTitle) {
		this.projectMainTitle = projectMainTitle;
	}

	private static final long serialVersionUID = 1L;
	/** 项目ID */
	private String oidPlatformProjectsId;
	/** 项目主标题 */
	private String projectMainTitle;
	/** 项目名称 */
	private String projectName;

	/** 最低认购金额 */
	private String lowestMoney;

	/** 风险级别 */
	private String riskLevel;

	/** 目标金额 */
	private String aimMoney;

	/** 已募集金额 */
	private String alreadyMoney;

	/** 产品发现状态 */
	private String iss_st;


	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getLowestMoney() {
		return lowestMoney;
	}

	public void setLowestMoney(String lowestMoney) {
		this.lowestMoney = lowestMoney;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getAimMoney() {
		return aimMoney;
	}

	public void setAimMoney(String aimMoney) {
		this.aimMoney = aimMoney;
	}

	public String getAlreadyMoney() {
		return alreadyMoney;
	}

	public void setAlreadyMoney(String alreadyMoney) {
		this.alreadyMoney = alreadyMoney;
	}

	public String getOidPlatformProjectsId() {
		return oidPlatformProjectsId;
	}

	public void setOidPlatformProjectsId(String oidPlatformProjectsId) {
		this.oidPlatformProjectsId = oidPlatformProjectsId;
	}
	public String getIss_st() {
		return iss_st;
	}

	public void setIss_st(String iss_st) {
		this.iss_st = iss_st;
	}
}
