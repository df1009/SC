package com.zhongchou.common.zhongzhengSao;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zhongchou.common.zhongzheng.cipher.MD5;

public class test {
	public static void main(String[] args) throws NoSuchAlgorithmException, Exception {
		String seq = String.valueOf(new Date().getTime());
		//20625
		/*s
		 * scBody.setValue("login_id", setData.get("loginId").toString());//登录帐号
	scBody.setValue("id_tp", setData.get("idTp").toString());//证件类型1：身份证2：护照3：军官证4：士兵证5：港澳通行证6：户口本7：其他
	scBody.setValue("id_no", setData.get("idNo").toString());//证件号码
	scBody.setValue("card_no", setData.get("cardNo").toString());//登录帐号
	scBody.setValue("mobile_no", setData.get("mobileNo").toString());//预留手机号
	scBody.setValue("pay_pwd", setData.get("payPwd").toString());//支付密码
	scBody.setValue("yzmSsn", setData.get("yzm_ssn").toString());//验证码流水
	scBody.setValue("yzm_content", setData.get("yzmContent").toString());//验证码
		 * */

		//银行验证码
		/*  BankSmsSao r = new BankSmsSao(seq);

		Map ma = new HashMap();
		ma.put("loginId", "20624");
		ma.put("userNmCn", "本人已死");
		ma.put("idTp", "1");
		ma.put("idNo", "150901199005099134");
		ma.put("cardNo", "6214831216327588");
		ma.put("mobileNo", "17091421254");
		System.out.println(r.setEncryptData(ma));*/




		//绑卡并设置密码
	  /*TiedBankCardSao r = new TiedBankCardSao(seq);

			Map ma = new HashMap();
			ma.put("loginId", "20624");
			ma.put("userNmCn", "本人已死");
			ma.put("idTp", "1");
			ma.put("idNo", "150901199005099134");
			ma.put("cardNo", "6214831216327588");
			ma.put("mobileNo", "17091421254");
			ma.put("payPwd", MD5.encode("Df123456789"));
			ma.put("yzmSsn", "201704011491031938624");
			ma.put("yzmContent", "000000");
			ma.put("userIp", "10.10.10.140");
			System.out.println(r.setEncryptData(ma));*/


/*		SendSmsSao t = new SendSmsSao(seq);
		Map m = new HashMap();
		m.put("smsType", "03");
		m.put("loginId", "123456");
		m.put("mobile", "17091424109");

		System.out.println(t.setEncryptData(m));*/


		//查询绑卡
		/*QueryTiedBankCardSao r = new QueryTiedBankCardSao(seq);

		Map ma = new HashMap();
		ma.put("loginId", "20624");
		System.out.println(r.setEncryptData(ma));*/


		//产品额度查询
		/*QueryProductQuotaSao r = new QueryProductQuotaSao(seq);

		Map ma = new HashMap();
		ma.put("loginId", "123456");
		ma.put("productCd", "001410");
		ma.put("userIp", "10.10.10.140");
		System.out.println(r.setEncryptData(ma));*/

		//产品费率查询
		/*QueryProductRateSao r = new QueryProductRateSao(seq);

		Map ma = new HashMap();
		ma.put("productCd", "SP8002");
		System.out.println(r.setEncryptData(ma));*/

		//银行代扣限额
		/*BankCardLimitSao r = new BankCardLimitSao(seq);

		Map ma = new HashMap();
		ma.put("productCd", "SP8002");
		System.out.println(r.setEncryptData(ma));
*/
		/*RegistSao r = new RegistSao(seq);
		Map ma = new HashMap();
		ma.put("loginId", "20624");
		System.out.println(r.setEncryptData(ma));*/

	}
}
