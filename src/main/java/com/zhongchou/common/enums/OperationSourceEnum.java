package com.zhongchou.common.enums;

/**
 * 跟踪操作来源枚举
 */
public enum OperationSourceEnum {

	NONE(""),
    /** 操作类型 */
	OPERATION_REGISTER("1"),
	OPERATION_TYPE_DISP("注册"),
	OPERATION_ACCOUNT_REGISTER("2"),
	OPERATION_ACCOUNT_REGISTER_DISP("实名认证"),
	OPERATION_RECHARGE("3"),
	OPERATION_RECHARGE_DISP("充值"),
	OPERATION_INVESTMENT("4"),
	OPERATION_INVESTMENT_DISP("投资"),
	OPERATION_WITHDRAW("5"),
	OPERATION_WITHDRAW_DISP("提现");

    /**
     * 取得値
     */
    private String value;

    /**
     * Constructor.
     *
     * @param value
     */
    private OperationSourceEnum(String value) {
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
     * 根据参数值取得对应的枚举値
     *
     * @param value 枚举値
     * @return 枚举
     */
    public static OperationSourceEnum getEnum(String value) {
        if ("1".equals(value)) {
            return OPERATION_REGISTER;
        } else if ("2".equals(value)) {
            return OPERATION_ACCOUNT_REGISTER;
        }else if ("3".equals(value)) {
            return OPERATION_RECHARGE;
        }else if ("4".equals(value)) {
            return OPERATION_INVESTMENT;
        }else if ("5".equals(value)) {
            return OPERATION_WITHDRAW;
        }else {
            return NONE;
        }
    }

    /**
     * 根据参数值取得对应的枚举値-汉字表示部分
     *
     * @param value 枚举値
     * @return 枚举
     */
    public static OperationSourceEnum getEnumDisp(String value) {
        if ("1".equals(value)) {
            return OPERATION_TYPE_DISP;
        } else if ("2".equals(value)) {
            return OPERATION_ACCOUNT_REGISTER_DISP;
        } else if ("3".equals(value)) {
            return OPERATION_RECHARGE_DISP;
        } else if ("4".equals(value)) {
            return OPERATION_INVESTMENT_DISP;
        } else if ("5".equals(value)) {
            return OPERATION_WITHDRAW_DISP;
        } else {
            return NONE;
        }
    }
}
