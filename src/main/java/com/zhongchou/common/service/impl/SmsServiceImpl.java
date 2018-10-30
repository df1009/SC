package com.zhongchou.common.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.yanshang.config.Config;
import com.yanshang.context.RequestContext;
import com.yanshang.util.DateUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.ISmsDao;
import com.zhongchou.common.dto.SendSmsDto;
import com.zhongchou.common.enums.SmsContentType;
import com.zhongchou.common.service.ISmsService;
import com.zhongchou.common.sms.SmsSender;
import com.zhongchou.common.zhongzhengSao.SendSmsSao;
@Service
public class SmsServiceImpl extends BaseSaoServiceImpl implements ISmsService {
	private final static String SEND_TEXT_SMS_IP_COUNT = "send.text.sms.ip.count";
	private final static String SEND_TEXT_SMS_PHONE_COUNT = "send.text.sms.phone.count";
	@Autowired
	protected RequestContext requestContext;

	@Autowired
	private SmsSender smsSender;

	@Autowired
	private ISmsDao smsDao;

	@Override
	public int checkVerifyCode(SendSmsDto sendSms, int limit) {
	Row r = smsDao.getLastVerifyCode(sendSms);
	int errorTimes = 3;
	if (!StringUtils.isEmpty(Config.getString("sms_code_error_times"))) {
		errorTimes = Integer.parseInt(Config.getString("sms_code_error_times"));
	}
		if (r.isEmpty()) {
			return 1;
		} else {
			Date insertDate = (Date) r.get("UPD_DATE");
			long time = DateUtils.getSystemDate().getTime() - insertDate.getTime();
			if (time > limit * 1000){
				return  2;//超时
			}else if ((int)r.get("ERRORTIMES")>=errorTimes)  {
				//错误次数超限
				smsDao.updateErrorVerifyCodeInvalid(sendSms,r.get("ID").toString());
				return 3;//错误次数超限
			} else if (!sendSms.getVerifyCode().equals(r.get("VERIFY_CODE").toString()))  {
				//验证码错误次数+1
				smsDao.updateErrorSms(sendSms,r.get("ID").toString());
				return 1;//验证码错误
			}
		}
		return 0;
	}




	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean sendSms(SendSmsDto sendSms) {
		try {
			// 短信发送是否成功  (true:成功              false:失败)
			boolean isSendFlg = false;
			// 取得访问者ip
			String sSendIp = requestContext.getRequestIPAddress();
			if ("0:0:0:0:0:0:0:1".equals(sSendIp)) {
				sSendIp = "127.0.0.1";
			}

			sendSms.setSendIp(sSendIp);

			//向手机发送验证码
			StartSmsThread t = new StartSmsThread(sendSms);
			t.start();
			//isSendFlg = smsSender.send(sendSms);

			return true;

		} catch (Exception ex) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return false;
	}

	/**
	 * 防止短信轰炸机等短信群发软件。
	 * @param sendSms    短信发送内容。
	 * @return 是否可以发送短信   (true:可以发送      false:不可以发送)
	 */
	private boolean chkSendSms(SendSmsDto sendSms){
		SendSmsDto sendSms1 = new SendSmsDto();

		// 只对注册短信进行限制。
		if (!SmsContentType.IDENTITY.equals(sendSms.getSmsContentType())) {
			return true;
		}

		// 判断是否是本机ip，如果是，则可以无限发短信。
		if ("127.0.0.1".equals(sendSms.getSendIp())) {
			return true;
		}

		sendSms1.setSendIp(sendSms.getSendIp());
		sendSms1.setSendStatus("1");

		// 取得24小时内，ip地址发送次数。
		int iIp24Countsms = smsDao.getSmsHistoryCntByChk(sendSms1);

		// 24小时内，相同ip发送短信超过10条，不可以发送。
		String sCnt = Config.getString(SEND_TEXT_SMS_IP_COUNT);
		int iCnt = 0;
		if(StringUtils.isDigit(sCnt)){
			iCnt = Integer.parseInt(sCnt);
		}

		if(iCnt > 0 && iIp24Countsms > iCnt) {
			return false;
		}

		sendSms1 = new SendSmsDto();

		sendSms1.setPhone(sendSms.getPhone());
		sendSms1.setSendStatus("1");

		// 取得24小时内，手机号地址发送次数。
		int iPhone24Countsms = smsDao.getSmsHistoryCntByChk(sendSms1);

		// 24小时内，相同手机号发送短信超过10条，不可以发送。
		sCnt = Config.getString(SEND_TEXT_SMS_PHONE_COUNT);
		iCnt = 0;
		if(StringUtils.isDigit(sCnt)){
			iCnt = Integer.parseInt(sCnt);
		}

		if(iCnt > 0 && iPhone24Countsms > 10) {
			return false;
		}

		return true;
	}


	class StartSmsThread extends Thread {
		private SendSmsDto sendSms;

		public StartSmsThread(SendSmsDto sendSms) {
			this.sendSms =sendSms;
		}
		@Override
	    public void run() {
			if (chkSendSms(sendSms)) {
				smsSender.send(sendSms);
			}
	    }
	}

	@Override
	public boolean saveNightSms(SendSmsDto sendSms) {
		int  rtn = smsDao.saveNightSms(sendSms);
		return rtn == 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean saveHistory(SendSmsDto sendSms, boolean isSendSucc) {
		int  rtn = smsDao.saveHistory(sendSms, isSendSucc);
		return rtn == 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean saveVerifyCode(SendSmsDto sendSms) {
		int  rtn = 0;
		if (StringUtils.isEmpty(sendSms.getSmsVerifyCodeId())) {
			rtn = smsDao.saveVerifyCode(sendSms);
		} else {
			rtn = smsDao.updateVerifyCode(sendSms);
		}
		return rtn == 1;
	}

	@Override
	public Row getValidVerifyCode(SendSmsDto sendSms) {
		Row r = smsDao.getLastVerifyCode(sendSms);
		return r;
//		if (r.isEmpty()) {
//			return "";
//		} else {
//			Date insertDate = (Date) r.get("INS_DATE");
//			long time = DateUtils.getSystemDate().getTime() - insertDate.getTime();
//			if (time > limit * 60 * 1000){
//				return "";
//			} else {
//				return ConvUtils.convToString(r.get("VERIFY_CODE"));
//			}
//		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updateVerifyCodeInvalid(SendSmsDto sendSms) {
		int  rtn = smsDao.updateVerifyCodeInvalid(sendSms);
		return rtn == 1;
	}

	//用户前10分钟发送短信
		@Override
	public int selSendSmsTenMinute(String phone) {
		return  smsDao.selSendSmsTenMinute(phone);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean saveSystemSms(SendSmsDto sendSms) {
		 int rtn = smsDao.saveSms(sendSms);
		return rtn == 1;
	}

	@Override
	public Map<String,Object> sendShortMessage(String mobileNum,String loginId,String sendType) {

		// 返回的参数
		Map<String,Object> retMap = new HashMap<String,Object>();
		// 带入的参数
		Map<String,Object> setMap = new HashMap<String,Object>();

		String seq = nextSeq();
		SendSmsSao sendSmsSao = new SendSmsSao(seq);

		setMap.put("mobile",mobileNum );//手机号
		setMap.put("loginId",loginId );//
		setMap.put("smsType",sendType);//
		// 调用接口，返回报文体
		retMap = sendSmsSao.setEncryptData(setMap);
		return retMap;
	}

	public Map getUserVerifyCode(String phone) {
		return smsDao.getUserVerifyCode(phone);
	}
}


