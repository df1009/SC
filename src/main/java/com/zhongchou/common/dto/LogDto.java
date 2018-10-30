package com.zhongchou.common.dto;

public class LogDto {
	// 用户ID
	private String oidUserId;
	// 执行模块
	private String module;
	// 执行方法
	private String method;
	// 相应时间
	private String reponesData;
	// IP地址
	private String ipAddress;
	// 开始时间
	private String startData;
	// 结束时间
	private String enData;
	// 执行结果
	private String commite;
	public String getOidUserId() {
		return oidUserId;
	}
	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getReponesData() {
		return reponesData;
	}
	public void setReponesData(String reponesData) {
		this.reponesData = reponesData;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getStartData() {
		return startData;
	}
	public void setStartData(String startData) {
		this.startData = startData;
	}
	public String getEnData() {
		return enData;
	}
	public void setEnData(String enData) {
		this.enData = enData;
	}
	public String getCommite() {
		return commite;
	}
	public void setCommite(String commite) {
		this.commite = commite;
	}

}
