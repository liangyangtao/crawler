package com.kf.data.approved.sender;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/***
 * 
 * @Title: MyAuthenticator.java
 * @Package com.kf.data.tianyancha.sender
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年10月16日 上午10:36:58
 * @version V1.0
 */
public class MyAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
