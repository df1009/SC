package com.zhongchou.common.dto;

import java.util.Date;

import com.zhongchou.common.model.SearchConditionBase;

public class OperationSourceLogDto extends SearchConditionBase {

	private static final long serialVersionUID = 1L;

	/** 操作日志ID */
	private int operationSourceId;

	/** 用户ID */
	private String oidUserId;

	/** 操作类型(1:注册，2:实名认证，3:充值，4:投资，5:体现) */
	private String operationType;

	/** 操作来源(1:APP，2:网站，3:微信) */
	private String source;

	/** APP来源 */
	private String channel;

	/** 操作内容 */
	private String operationContext;

	/** 投标时放入投标ID */
	private String targetId;

	/** 作成日 */
	private Date insDate;


	/**
	 * 获取投标ID的方法。
	 * @return the targetId
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * 设置投标ID的方法。
	 * @param targetId the targetId to set
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * 获取操作日志ID的方法。
	 *
	 * @return 操作日志ID
	 */
	public int getOperationSourceId() {
		return operationSourceId;
	}

	/**
	 * 设置操作日志ID的方法。
	 *
	 * @param operationSourceId 操作日志ID
	 */
	public void setOperationSourceId(int operationSourceId) {
		this.operationSourceId = operationSourceId;
	}

	/**
	 * 获取用户ID的方法。
	 *
	 * @return 用户ID
	 */
	public String getOidUserId() {
		return oidUserId;
	}

	/**
	 * 设置用户ID的方法。
	 *
	 * @param oidUserId 用户ID
	 */
	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}

	/**
	 * 获取操作类型(1:注册，2:实名认证，3:充值，4:投资，5:体现)的方法。
	 *
	 * @return 用户ID
	 */
	public String getOperationType() {
		return operationType;
	}

	/**
	 * 设置操作类型(1:注册，2:实名认证，3:充值，4:投资，5:体现)的方法。
	 *
	 * @param operationType 操作类型
	 */
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	/**
	 * 获取操作来源(1:APP，2:网站，3:微信)的方法。
	 *
	 * @return 操作来源
	 */
	public String getSource() {
		return source;
	}

	/**
	 * 设置操作来源(1:APP，2:网站，3:微信)的方法。
	 *
	 * @param source 操作来源
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 获取操作内容 的方法。
	 *
	 * @return 操作内容
	 */
	public String getOperationContext() {
		return operationContext;
	}

	/**
	 * 设置操作内容的方法。
	 *
	 * @param operationContext 操作内容
	 */
	public void setOperationContext(String operationContext) {
		this.operationContext = operationContext;
	}

	/**
	 * 获取作成日的方法。
	 *
	 * @return 作成日
	 */
	public Date getInsDate() {
		return insDate;
	}

	/**
	 * 设置作成日的方法。
	 *
	 * @param insDate 作成日
	 */
	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}


}
