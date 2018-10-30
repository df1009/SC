package com.zhongchou.common.enums;
/**
 * 撤销状态
 */
public enum SmsOperateType {

	NONE(""),
	/** 个人注册 */
	REGIST("01"),
	REGIST_DISP("个人注册"),
	/** 企业注册 */
	ORG_REGIST("02"),
	ORG_REGIST_DISP("企业注册"),
	/** 修改电话号码 */
	CHANGE_PHONE("03"),
	CHANGE_PHONE_DISP("修改电话号码"),
	/** 手机认证找回密码 */
	SEARCH_PASSWORD("04"),
	SEARCH_PASSWORD_DISP("手机认证找回密码"),
	/** 贷款申请 */
	FINANCE_APPLICATION("05"),
	FINANCE_APPLICATION_DISP("贷款申请");


	/**
	 * 取得値
	 */
	private String value;

	/**
	 * Constructor.
	 *
	 * @param value
	 */
	private SmsOperateType(String value) {
		this.value = value;
	}

	/**
	 * 返回对应的枚举値。
	 *
	 * @return String
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * 根据参数值取得对应的枚举値-汉子表示部分
	 *
	 * @param value 枚举値
	 * @return 枚举
	 */
	public static SmsOperateType getEnumDisp(String value) {
		if ("01".equals(value)) {
			return REGIST_DISP;
		} else if ("02".equals(value)) {
			return ORG_REGIST_DISP;
		} else if ("03".equals(value)) {
			return CHANGE_PHONE_DISP;
		} else if ("04".equals(value)) {
			return SEARCH_PASSWORD_DISP;
		} else if ("05".equals(value)) {
			return FINANCE_APPLICATION_DISP;
		} else {
			return NONE;
		}
	}
}
