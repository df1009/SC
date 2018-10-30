package com.zhongchou.common.dto;

import java.io.Serializable;

/**
 * 我的项目Dto类
 *
 */
public class MyProjectlDto implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 投资序列号ID */
	private String oidTenderId = "";
	/** 项目名称 */
	private String projectName = "";
	/** 产品代码 */
	private String projectCode = "";
	/** 法人代码 */
	private String ivsName = "";
	/** 发行开始日期 */
	private String issBeginDate = "";
	/** 发行结束日期 */
	private String issEndDate = "";
	/** 提交日期 */
	private String insDate = "";
	/** 项目状态*/
	private String projectState = "";
	/** 是否冻结*/
	private String frzzState = "";





	public String getFrzzState() {
		return frzzState;
	}
	public void setFrzzState(String frzzState) {
		this.frzzState = frzzState;
	}
	public String getProjectState() {
		return projectState;
	}
	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}
	public String getOidTenderId() {
		return oidTenderId;
	}
	public void setOidTenderId(String oidTenderId) {
		this.oidTenderId = oidTenderId;
	}
	public String getIvsName() {
		return ivsName;
	}
	public void setIvsName(String ivsName) {
		this.ivsName = ivsName;
	}
	public String getInsDate() {
		return insDate;
	}
	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getIssBeginDate() {
		return issBeginDate;
	}
	public void setIssBeginDate(String issBeginDate) {
		this.issBeginDate = issBeginDate;
	}
	public String getIssEndDate() {
		return issEndDate;
	}
	public void setIssEndDate(String issEndDate) {
		this.issEndDate = issEndDate;
	}



}
