package com.kf.data.tianyancha.sender;

import java.util.ArrayList;
import java.util.List;

import com.kf.data.fetcher.tools.KfConstant;

/***
 * 
 * @Title: SendMail.java
 * @Package com.kf.data.tianyancha.sender
 * @Description: 发送邮件
 * @author liangyt
 * @date 2017年10月16日 上午10:37:09
 * @version V1.0
 */
public class SendMail {

	/***
	 * 发送邮件
	 * 
	 * @param info
	 */
	public void sendMail(String info) {
		MailSenderInfo mailInfo = fillMailInfo(info);
		MailSender sms = new MailSender();
		sms.sendHtmlMail(mailInfo);
	}

	private MailSenderInfo fillMailInfo(String info) {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(KfConstant.emsHost);
		mailInfo.setMailServerPort(KfConstant.emsPort);
		mailInfo.setValidate(true);
		mailInfo.setUserName(KfConstant.emsUsername);
		mailInfo.setPassword(KfConstant.emsPassword);// 您的邮箱密码
		mailInfo.setFromAddress(KfConstant.emsFromAddress);
		List<String> address = new ArrayList<String>();
		// address.add("unbankspider@163.com");
		// List<String> address2 = new ArrayList<String>();
		String temp[] = KfConstant.emsReciver.split(",");
		for (String string : temp) {
			address.add(string);
		}
		// 添加邮件接收人
		mailInfo.setReceivers(address);
		// 添加邮件抄送人
		// mailInfo.setCcReceivers(address);
		// mailInfo.setToAddress("unbankspider@163.com");//邮箱密码unbank
		String subject = info;
		String content = info;
		mailInfo.setSubject(subject);
		mailInfo.setContent(content);
		String[] files = {};
		mailInfo.setAttachFileNames(files);
		return mailInfo;
	}
}
