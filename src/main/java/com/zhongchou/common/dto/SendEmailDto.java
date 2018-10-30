package com.zhongchou.common.dto;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SendEmailDto extends Authenticator{
	String smtpHost = "";//发送端邮箱服务器
	String from = "";//发送端邮箱
	String fromUserPassword = "";//发送端邮箱密码
	String to = "";//接受端邮箱
	String subject = "";//邮件标题
	String messageText = "";//发送消息内容
	String messageType = "";//邮件编码格式
	String loginId = "";//用户id
	String sendResultFlg = "";//发送邮件是否成功表示0：失败，1：成功
    public SendEmailDto(){

    }
    public SendEmailDto(String from,String fromUserPassword){
        this.from=from;
        this.fromUserPassword=fromUserPassword;
    }
     protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(from, fromUserPassword);
     }
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getFromUserPassword() {
		return fromUserPassword;
	}
	public void setFromUserPassword(String fromUserPassword) {
		this.fromUserPassword = fromUserPassword;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getSendResultFlg() {
		return sendResultFlg;
	}
	public void setSendResultFlg(String sendResultFlg) {
		this.sendResultFlg = sendResultFlg;
	}


}
