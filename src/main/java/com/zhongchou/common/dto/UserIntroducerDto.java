package com.zhongchou.common.dto;

import java.util.Date;

import com.zhongchou.common.model.SearchConditionBase;

/**
 * 用户信息dto类
 *
 */
public class UserIntroducerDto extends SearchConditionBase {

	private static final long serialVersionUID = 1L;

	/** 用户OID */
	private String oidUserId;
	/** 推举用户ID */
	private String introducerOidUserId;
	/** 奖励发送状态  */
	private String inviteAwardFlg = "";
	/** 用户名  */
	private String userName;
	/** 推举用户名  */
	private String introducerUserName;
	/** 发放红包奖励  */
	private String redPacketAmount;
	/** 发放红包奖励  */
	private String point;
	/** 发放奖励时间  */
	private Date inviteAwardDate;

	public String getRedPacketAmount() {
		return redPacketAmount;
	}
	public void setRedPacketAmount(String redPacketAmount) {
		this.redPacketAmount = redPacketAmount;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public Date getInviteAwardDate() {
		return inviteAwardDate;
	}
	public void setInviteAwardDate(Date inviteAwardDate) {
		this.inviteAwardDate = inviteAwardDate;
	}
	public String getOidUserId() {
		return oidUserId;
	}
	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}
	public String getIntroducerOidUserId() {
		return introducerOidUserId;
	}
	public void setIntroducerOidUserId(String introducerOidUserId) {
		this.introducerOidUserId = introducerOidUserId;
	}
	public String getInviteAwardFlg() {
		return inviteAwardFlg;
	}
	public void setInviteAwardFlg(String inviteAwardFlg) {
		this.inviteAwardFlg = inviteAwardFlg;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIntroducerUserName() {
		return introducerUserName;
	}
	public void setIntroducerUserName(String introducerUserName) {
		this.introducerUserName = introducerUserName;
	}

}
