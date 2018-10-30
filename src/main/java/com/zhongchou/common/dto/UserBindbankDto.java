package com.zhongchou.common.dto;

import com.zhongchou.common.model.SearchConditionBase;

/**
 * 用户中心-订单管理
 */
public class UserBindbankDto extends SearchConditionBase {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/** 银行管理ID(UUID自动生成) */
	private String oidAccountBindBankId;

	/** 银行卡号 */
	private String cardNo;

	/** 是否主卡 0副卡 1主卡 */
	private String mainFlg;

	/** 银行代码 */
	private String bank;

	/** 支付渠道FUIOU：富友支付 JT：金通 */
	private String payChannel;

	/** 是否需要银商签约0 否1 是 */
	private String needBankSign;

	public String getOidAccountBindBankId() {
		return oidAccountBindBankId;
	}

	public void setOidAccountBindBankId(String oidAccountBindBankId) {
		this.oidAccountBindBankId = oidAccountBindBankId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getNeedBankSign() {
		return needBankSign;
	}

	public void setNeedBankSign(String needBankSign) {
		this.needBankSign = needBankSign;
	}

	public String getMainFlg() {
		return mainFlg;
	}

	public void setMainFlg(String mainFlg) {
		this.mainFlg = mainFlg;
	}
}
