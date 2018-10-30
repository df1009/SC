package com.zhongchou.common.sms;

import com.yanshang.helper.ApplicationContextHelper;


/**
 * 发送短信工厂类
 */
public class SmsSenderFactory {

    /**
     * SmsSender创建实例并返回。
     * @return SmsSender实例
     */
    public static SmsSender create() {
        return (SmsSender) ApplicationContextHelper.getBean("smsSender");
    }
}
