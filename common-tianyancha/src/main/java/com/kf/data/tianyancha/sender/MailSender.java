package com.kf.data.tianyancha.sender;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/***
 * 
 * @Title: MailSender.java
 * @Package com.kf.data.tianyancha.sender
 * @Description: 以文本格式发送邮件
 * @author liangyt
 * @date 2017年10月16日 上午10:36:19
 * @version V1.0
 */
public class MailSender {

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 * @throws UnsupportedEncodingException
	 */
	public boolean sendTextMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			InternetAddress from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中

			// Address to = new InternetAddress(mailInfo.getToAddress());
			if (mailInfo.getReceivers() != null && mailInfo.getReceivers().size() > 0) {
				String addressList = getMailList(mailInfo.getReceivers());
				InternetAddress[] receiverAddresses = new InternetAddress().parse(addressList);
				mailMessage.setRecipients(Message.RecipientType.TO, receiverAddresses);
			}
			// 邮件抄送人
			if (mailInfo.getCcReceivers() != null && mailInfo.getCcReceivers().size() > 0) {
				String addressList = getMailList(mailInfo.getCcReceivers());
				InternetAddress[] receiverAddresses = new InternetAddress().parse(addressList);
				mailMessage.setRecipients(Message.RecipientType.CC, receiverAddresses);
			}

			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);

			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public boolean sendHtmlMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			InternetAddress from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			// 邮件接收人
			if (mailInfo.getReceivers() != null && mailInfo.getReceivers().size() > 0) {
				String addressList = getMailList(mailInfo.getReceivers());
				InternetAddress[] receiverAddresses = new InternetAddress().parse(addressList);
				mailMessage.setRecipients(Message.RecipientType.TO, receiverAddresses);
			}
			// 邮件抄送人
			if (mailInfo.getCcReceivers() != null && mailInfo.getCcReceivers().size() > 0) {
				String addressList = getMailList(mailInfo.getCcReceivers());
				InternetAddress[] receiverAddresses = new InternetAddress().parse(addressList);
				mailMessage.setRecipients(Message.RecipientType.CC, receiverAddresses);
			}

			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");

			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			if (mailInfo.getAttachFileNames() != null && mailInfo.getAttachFileNames().length > 0) {
				String[] files = mailInfo.getAttachFileNames();
				for (int i = 0; i < files.length; i++) {
					String filepath = mailInfo.getAttachFileNames()[i];
					BodyPart messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(filepath);
					// 添加附件的内容
					messageBodyPart.setDataHandler(new DataHandler(source));
					// 添加附件的标题
					messageBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
					mainPart.addBodyPart(messageBodyPart);
				}
				mailMessage.setContent(mainPart);
			}

			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 获取收件人地址
	private String getMailList(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(s);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
