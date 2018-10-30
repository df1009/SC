package com.zhongchou.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.codec.binary.Base64;

import com.zhongchou.common.dto.SendEmailDto;

public class SendEmailUtil {
	//private static final String URL = "SCLogin/bindUserEmail.do?t=";
	private static final String SMTPHOST = "smtp.exmail.qq.com";
	private static final String FROMEMAIL = "service@yiyanstart.com";
	private static final String FROMPASSWORD = "bb881652BB";
	private static final String SUBJECT = "《壹盐双创》用户邮箱验证";
	private static final String MESSAGETYPE = "text/html;charset=gb2312";

	public static SendEmailDto sendEmail(String loginId, String userEmail,String path)
			throws MessagingException {
		// 秘钥
		String key = "yilicai";
		key = "7f61ff638daf5bffd12f4ce2";
		String str = loginId + ":" + userEmail+":"+new Date().getTime();
		// 加密
		byte[] encoded = ThreeDesUtil.encryptMode(key.trim().getBytes(),
				str.getBytes());
		str = Base64.encodeBase64String(encoded);
		SendEmailDto emailDto = new SendEmailDto();
		// 封装邮件参数

		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		String time = format.format(new Date());
		StringBuilder sb = new StringBuilder();
		sb.append("<html> <body>")
				.append("<h3>尊敬的壹盐双创用户：</h3><br/>")
				.append("<p>您于" + time + "申请邮箱地址验证，点击以下链接，即可完成验证：</p><br/>")
				.append("<p><a href="+path+ str+">" + path+ str + "</a></p><br/>")
				.append("<p>若点击无法打开验证窗口，您也可以将链接复制到浏览器地址栏访问。</p><br/>")
				.append("<p>为保障您的账号安全，请在24小时内点击该链接。若您没有申请过邮箱地址验证，请您忽略此邮件，由此给您带来的不便请谅解。</p><br/>")
				.append("<p>壹盐双创</p><br/>")
				.append("<p>--------------------------</p><br/>")
				.append("<p>注：此邮件由系统自动发送，请勿回复</p><br/>")
				.append("<p>如果您有任何疑问，请您查看帮助，或联系客服电话4001-021-400</p><br/>")
				.append("</body></html>");
		// 封装调用发送邮件方法的参数
		emailDto.setSmtpHost(SMTPHOST);
		emailDto.setFrom(FROMEMAIL);
		emailDto.setFromUserPassword(FROMPASSWORD);
		emailDto.setMessageText(sb.toString());
		emailDto.setMessageType(MESSAGETYPE);
		emailDto.setTo(userEmail);
		emailDto.setSubject(SUBJECT);
		emailDto.setLoginId(loginId);

		// 调用发送邮件接口
		sendMessage(emailDto);
		return emailDto;
	}
	@SuppressWarnings("static-access")
	public static void sendMessage(SendEmailDto sendEmailDto) throws MessagingException {
		// 配置javax.mail.Session对象
		Properties props = new Properties();
		props.put("mail.smtp.host", sendEmailDto.getSmtpHost());
		props.put("mail.smtp.starttls.enable", "true");// 使用 STARTTLS安全连接
		// props.put("mail.smtp.port", "25"); //google使用465或587端口
		props.put("mail.smtp.auth", "true"); // 使用验证
		Session mailSession = Session.getInstance(props, sendEmailDto);

		// 编写消息
		InternetAddress fromAddress = new InternetAddress(sendEmailDto.getFrom());
		InternetAddress toAddress = new InternetAddress(sendEmailDto.getTo());

		MimeMessage message = new MimeMessage(mailSession);

		message.setFrom(fromAddress);
		message.addRecipient(RecipientType.TO, toAddress);

		message.setSentDate(Calendar.getInstance().getTime());
		message.setSubject(sendEmailDto.getSubject());
		message.setContent(sendEmailDto.getMessageText(), sendEmailDto.getMessageType());

		// 发送消息
		Transport transport = mailSession.getTransport("smtp");
		transport.connect(sendEmailDto.getSmtpHost(), sendEmailDto.getFrom(), sendEmailDto.getFromUserPassword());
		transport.send(message, message.getRecipients(RecipientType.TO));
	}
}
