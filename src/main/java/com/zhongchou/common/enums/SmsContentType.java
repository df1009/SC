package com.zhongchou.common.enums;

import java.util.Arrays;

public enum SmsContentType {
//
//    /** 注册 */
//    REGIST,
//    /** 线上充值成功 */
//    RECHARGE_ONLINE,
//    /** 线下充值成功 */
//    RECHARGE_OFFLINE,
//    /** 提现申请 */
//    CASH_APPLY,
//    /** 提现成功 */
//    CASH_SUCC,
//    /** 提现失败 */
//    CASH_FAIL,
    /** 还款提醒 */
    REPAY_REMIND,
//    /** 还款成功 */
//    REPAY_SUCC,
//    /** 逾期提醒 */
//    OVERDUE_REMIND,
//    /** 回款提醒 */
//    RECOVER_REMIND,
//    /** 回款到账 */
//    RECOVER_SUCC,
//    /** VIP认证通过 */
//    VIP_APPLY,
//    // TODO
//    /** 发标初审通过 */
//    BORROW_VERIFY,
//    /** 发标流标 */
//    BORROW_FAIL,
//    /** 发标复审通过 */
//    BORROW_REVERIFY,
    /** 手机验证时发送验证码 */
    IDENTITY,
//    /** 安全中心更改密码 */
//    MODIFY_PWD,
//    /** 安全中心更改手机号码 */
//    MODIFY_PHONE,
//    /** 绑定银行卡 */
//    BANK_BUNDING,
//    /** 交易密码 */
//    JY_PWD,
//    /** 提现密码 */
//    TX_PWD,
//    /** 修改邮箱地址 */
//    MAIL_ADDR,
//    /** 手机验证 */
//    PHONE_IDENTIFY,
//    /** 重置登录密码 */
//    RESET_USER_PASSWORD,
//    /** 重置支付密码 */
//    RESET_PAYMENT_PASSWORD,
    /** 自定义 */
    CUSTOM;

    public String getValue() {
        return this.name();
    }

    public String getConfigCategory() {
        String category = "UNKNOWN";
            switch (this) {
//            case REGIST:
//                category = "regist";
//                break;
//            case RECHARGE_ONLINE:
//                category = "recharge_online";
//                break;
//            case RECHARGE_OFFLINE:
//                category = "recharge_offline";
//                break;
//            case CASH_APPLY:
//                category = "cash_apply";
//                break;
//            case CASH_SUCC:
//                category = "cash_succ";
//                break;
//            case CASH_FAIL:
//                category = "cash_fail";
//                break;
//            case REPAY_REMIND:
//                category = "repay_remind";
//                break;
//            case REPAY_SUCC:
//                category = "repay_succ";
//                break;
//            case OVERDUE_REMIND:
//                category = "overdue_remind";
//                break;
//            case RECOVER_REMIND:
//                category = "recover_remind";
//                    break;
//            case RECOVER_SUCC:
//                category = "recover_succ";
//                break;
//            case VIP_APPLY:
//                category = "vip_apply";
//                break;
//            case BORROW_VERIFY:
//                category = "borrow_verify";
//                break;
//            case BORROW_FAIL:
//                category = "borrow_fail";
//                break;
//            case BORROW_REVERIFY:
//                category = "borrow_reverify";
//                break;
//            case IDENTITY:
//                category = "identity";
//                break;
//            case MODIFY_PWD:
//                category = "modify_pwd";
//                break;
//            case MODIFY_PHONE:
//                category = "modify_phone";
//                break;
//            case BANK_BUNDING:
//                category = "bank_bunding";
//                break;
//            case JY_PWD:
//                category = "jy_pwd";
//                break;
//            case TX_PWD:
//                category = "tx_pwd";
//                break;
//            case MAIL_ADDR:
//                category = "mail_addr";
//                break;
//            case PHONE_IDENTIFY:
//                category = "phone_identify";
//                break;
//            case RESET_USER_PASSWORD:
//                category = "reset_user_password";
//                break;
//            case RESET_PAYMENT_PASSWORD:
//                category = "reset_payment_password";
//                break;
            default:
                break;
            }
        return category;
    }

    public static SmsContentType getEnum(String value) {
//        if ("REGIST".equalsIgnoreCase(value)) {
//            return REGIST;
//        } else if ("RECHARGE_ONLINE".equalsIgnoreCase(value)) {
//            return RECHARGE_ONLINE;
//        } else if ("RECHARGE_OFFLINE".equalsIgnoreCase(value)) {
//            return RECHARGE_OFFLINE;
//        } else if ("CASH_APPLY".equalsIgnoreCase(value)) {
//            return CASH_APPLY;
//        } else if ("CASH_SUCC".equalsIgnoreCase(value)) {
//            return CASH_SUCC;
//        } else if ("CASH_FAIL".equalsIgnoreCase(value)) {
//            return CASH_FAIL;
//        } else if ("REPAY_REMIND".equalsIgnoreCase(value)) {
//            return REPAY_REMIND;
//        } else if ("REPAY_SUCC".equalsIgnoreCase(value)) {
//            return REPAY_SUCC;
//        } else if ("OVERDUE_REMIND".equalsIgnoreCase(value)) {
//            return OVERDUE_REMIND;
//        } else if ("RECOVER_REMIND".equalsIgnoreCase(value)) {
//            return RECOVER_REMIND;
//        } else if ("RECOVER_SUCC".equalsIgnoreCase(value)) {
//            return RECOVER_SUCC;
//        } else if ("VIP_APPLY".equalsIgnoreCase(value)) {
//            return VIP_APPLY;
//        } else if ("BORROW_VERIFY".equalsIgnoreCase(value)) {
//            return BORROW_VERIFY;
//        } else if ("BORROW_FAIL".equalsIgnoreCase(value)) {
//            return BORROW_FAIL;
//        } else if ("BORROW_REVERIFY".equalsIgnoreCase(value)) {
//            return BORROW_REVERIFY;
//        } else if ("IDENTITY".equalsIgnoreCase(value)) {
//            return IDENTITY;
//        } else if ("MODIFY_PWD".equalsIgnoreCase(value)) {
//            return MODIFY_PWD;
//        } else if ("MODIFY_PHONE".equalsIgnoreCase(value)) {
//            return MODIFY_PHONE;
//        } else if ("BANK_BUNDING".equalsIgnoreCase(value)) {
//            return BANK_BUNDING;
//        } else if ("JY_PWD".equalsIgnoreCase(value)) {
//            return JY_PWD;
//        } else if ("TX_PWD".equalsIgnoreCase(value)) {
//            return TX_PWD;
//        } else if ("MAIL_ADDR".equalsIgnoreCase(value)) {
//            return MAIL_ADDR;
//        } else if ("PHONE_IDENTIFY".equalsIgnoreCase(value)) {
//            return PHONE_IDENTIFY;
//        } else if ("RESET_PAYMENT_PASSWORD".equalsIgnoreCase(value)) {
//            return RESET_PAYMENT_PASSWORD;
//        } else if ("RESET_USER_PASSWORD".equalsIgnoreCase(value)) {
//            return RESET_USER_PASSWORD;
//        }
        throw new RuntimeException("未能转换为枚举类型(class: SmsContentType, value: '" + value + "', availables: " + Arrays.deepToString(values()) + ")");
    }
}
