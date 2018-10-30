package com.zhongchou.common.zhongzheng.util;

/**
 *
 * @author liliang
 *
 */
public class TimeoutException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public TimeoutException() {
		super();
	}

	public TimeoutException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message == null ? message : message.trim();
	}

	public void setMessage(String message) {
		this.message = message == null ? message : message.trim();
	}
}
