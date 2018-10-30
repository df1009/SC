package com.zhongchou.common.sms;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yanshang.config.Config;
import com.yanshang.config.ConfigKey;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.config.SmsTemplate;
import com.zhongchou.common.dto.SendSmsDto;
import com.zhongchou.common.enums.SmsType;
import com.zhongchou.common.service.ISmsService;

public abstract class AbstractSmsSender {

	protected Logger log = LoggerFactory.getLogger(AbstractSmsSender.class);

	@Autowired
	protected ISmsService smsService;

	/**
	 * 发送手机短信。
	 *
	 * @param SendSmsDto
	 *            发送短信信息对象
	 * @return 是否成功发送
	 */
	public boolean send(SendSmsDto sendSms) {
		if ("Auto".equals(sendSms.getSendMode())) {
			String period = Config.getString(ConfigKey.SMS_NOT_DISTURB_TIME);
			String nowTime = DateUtils.convertDate2String(new Date(), "HH:mm:ss");
			if (!StringUtils.isEmpty(period) && period.split("-").length==2) {
				if (nowTime.compareTo(period.split("-")[0]) > 0 || nowTime.compareTo(period.split("-")[1]) < 0) {
					return smsService.saveNightSms(sendSms);
				}
			} else {
				if (nowTime.compareTo("22:00:00") > 0 || nowTime.compareTo("08:00:00") < 0) {
					return smsService.saveNightSms(sendSms);
				}
			}
		}
		String userId= sendSms.getUserId();
		// 用户名加密 TODO
		sendSms.setUserId(strToConceal(sendSms.getUserId()));
		// 发送状态
		boolean isSendSucc = false;
		// 消息内存判断
		switch (sendSms.getSmsContentType()) {
			// 手机验证时发送验证码
			case IDENTITY:
				isSendSucc = sendIdentifySms(sendSms);
				break;
			// 自定义
			case CUSTOM:
				isSendSucc = sendCustomSms(sendSms);
				break;

			// 手机验证时发送验证码
			default:
				isSendSucc = sendIdentifySms(sendSms);
				break;
		}

		if(Config.getBoolean(ConfigKey.SAVE_SMS_HISTORY)) {
			sendSms.setUserId(userId);
			saveSmsSendLog(sendSms, isSendSucc);
		}
		// 发送状态返回
		return isSendSucc;
	}

	private void saveSmsSendLog(SendSmsDto sendSms, boolean isSendSucc) {
		if(Config.getBoolean(ConfigKey.SAVE_SMS_HISTORY)) {
			if ("0".equals(Config.getString(ConfigKey.SAVE_SMS_HISTORY_LEVEL))) {
				//所有的
				smsService.saveHistory(sendSms, isSendSucc);
			} else if ("1".equals(Config.getString(ConfigKey.SAVE_SMS_HISTORY_LEVEL))) {
				//只失败的
				if (!isSendSucc) {
					smsService.saveHistory(sendSms, isSendSucc);
				}
			}
		}
	}



	/**
	 * 不使用系统提供的短信模板，在业务层自定义短信内容
	 *
	 * @param SendSmsDto
	 *            发送短信信息对象
	 * @return 是否成功发送
	 */
	private boolean sendCustomSms(SendSmsDto sendSms) {
		// 短信内容
		String content = Config.getString(ConfigKey.SMS_SENDER_SIGNATURE);
		content = sendSms.getContents()+content;
		sendSms.setContents(content);

		return sendSms(sendSms);
	}







	/**
	 * 手机验证时发送验证码，发送手机短信。
	 *
	 * @param SendSmsDto
	 *            发送短信信息对象 【设置信息： 1：电话号码，
	 *            2：发送类型（SmsType.TEXT：手机短信，SmsType.VOICE：手机语音）， 3：用户名】
	 * @return 是否成功发送
	 */
	private boolean sendIdentifySms(SendSmsDto sendSms) {
		int sendCount = smsService.selSendSmsTenMinute(sendSms.getPhone());
		//用户10分钟内发送短信大于6条则不再发短信
		if(sendCount>5){
			return false;
		}
		int limit = 10;
		if (ConvUtils.convToInt(Config.getString(ConfigKey.SMS_VERIFY_CODE_LIMIT)) > 0) {
			limit = ConvUtils.convToInt(Config.getString(ConfigKey.SMS_VERIFY_CODE_LIMIT));
		}
		String verifyCode = "";
		Row smsVerifyCodeInfo = smsService.getValidVerifyCode(sendSms);
		if (smsVerifyCodeInfo.isEmpty()) {
			verifyCode =  "";
		} else {
			Date insertDate = smsVerifyCodeInfo.getDate("UPD_DATE");
			long time = DateUtils.getSystemDate().getTime() - insertDate.getTime();
			if (time > limit * 1000){
				verifyCode =  "";
			} else {
				verifyCode = ConvUtils.convToString(smsVerifyCodeInfo.get("VERIFY_CODE"));
				sendSms.setSmsVerifyCodeId(ConvUtils.convToString(smsVerifyCodeInfo.get("ID")));
			}
		}
		if (StringUtils.isEmpty(verifyCode)) {
			// 验证码
			verifyCode = getVerifyCode(sendSms.getSmsType());
			sendSms.setVerifyCode(verifyCode.replaceAll(" ", ""));
		} else {
			sendSms.setVerifyCode(verifyCode);
		}
		// 短信内容
		String content = Config.getString(ConfigKey.SMS_SENDER_SIGNATURE);
		if (ConvUtils.convToInt(Config.getString(ConfigKey.SMS_VERIFY_CODE_LIMIT)) > 0) {
			if("6".equals(sendSms.getOperateType())){//购买模板
				content = getSmsContent("sms_buy_limit_template", verifyCode, Config.getString(ConfigKey.SMS_VERIFY_CODE_LIMIT))+content;
			}else if("7".equals(sendSms.getOperateType())){//撤单模板
				content = getSmsContent("sms_cancleorder_limit_template", verifyCode, Config.getString(ConfigKey.SMS_VERIFY_CODE_LIMIT))+content;
			}else{
				content = getSmsContent("sms_identify_limit_template", verifyCode, Config.getString(ConfigKey.SMS_VERIFY_CODE_LIMIT))+content;
			}
		} else {
			if("6".equals(sendSms.getOperateType())){//购买模板
				content = getSmsContent("sms_buy_template", verifyCode)+content;
			}else if("7".equals(sendSms.getOperateType())){//撤单模板
				content = getSmsContent("sms_cancleorder_template", verifyCode)+content;
			}else{
				content = getSmsContent("sms_identify_template", verifyCode)+content;	
			}
		}
		sendSms.setContents(content);

		return sendSms(sendSms);
	}



	private boolean sendSms(SendSmsDto sendSms) {
		try {
			boolean sendFlg = false;
			//非短信验证码短信保存在数据表中由定时任务统一发送
			if (StringUtils.isEmpty(sendSms.getVerifyCode())&&StringUtils.isEmpty(sendSms.getSendFlag())) {
				String sign = Config.getString(ConfigKey.SMS_SENDER_SIGNATURE);
				sendSms.setContents(sendSms.getContents().replace(sign, ""));
				sendFlg = smsService.saveSystemSms(sendSms);
			}else{//短信验证码和定时任务短信
				if (SmsType.TEXT.equals(sendSms.getSmsType())) {
					sendFlg = sendTextSms(sendSms.getPhone(), sendSms.getContents());
				} else if (SmsType.VOICE.equals(sendSms.getSmsType())) {
					sendFlg = sendVoiceSms(sendSms.getPhone(), sendSms.getContents() + getSmsContent("sms_voice_rehear"));
				}
				if (sendFlg && !StringUtils.isEmpty(sendSms.getVerifyCode())) {
					smsService.saveVerifyCode(sendSms);
				}
			}
			return sendFlg;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return false;
		}
	}

	/**
	 * 发送语音短信。
	 *
	 * @param phone
	 *            电话号码
	 * @param content
	 *            短信内容
	 * @return 是否成功发送
	 */
	protected abstract boolean sendVoiceSms(String phone, String content) throws Exception;

	/**
	 * 发送文本短信。
	 *
	 * @param phone
	 *            电话号码
	 * @param content
	 *            短信内容
	 * @return 是否成功发送
	 */
	protected abstract boolean sendTextSms(String phone, String content) throws Exception;

	private String getSmsContent(String key, Object... values) {
		return MessageFormat.format(SmsTemplate.getString(key), values);
	}

	private String getVerifyCode(SmsType smsType) {
		StringBuffer sb = new StringBuffer();
		Random rand = new Random();
		for (int i = 0;i < 6; i++) {
			sb.append(rand.nextInt(10));
		}
		return sb.toString();
		/*if (SmsType.VOICE == smsType) {
			return org.apache.commons.lang.StringUtils.join(set, " ");
		} else {
			return org.apache.commons.lang.StringUtils.join(set);
		}*/
	}

	/**
	 *
	 * 用户ID转义
	 *
	 * @param 用户ID
	 *
	 * @return 转义后ID
	 */
	public String strToConceal(String userId) {
		String subUserId = "";
		if (userId == null || "".equals(userId)) {
			return subUserId;
		}
		char[] charArray = userId.toCharArray();
		int len = charArray.length;
		if (len < 4) {
			subUserId = String.valueOf(charArray[0]) + "***";
		} else {
			subUserId = String.valueOf(charArray[0]) + String.valueOf(charArray[1]) + "***";
		}
		return subUserId;
	}

}
