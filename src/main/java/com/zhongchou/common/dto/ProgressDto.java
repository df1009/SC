package com.zhongchou.common.dto;

public class ProgressDto {

	private static final long serialVersionUID = 1L;
	/** 消息更新时间 */
	private String updateTime;
	/** 消息信息 */
	private String message;
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
