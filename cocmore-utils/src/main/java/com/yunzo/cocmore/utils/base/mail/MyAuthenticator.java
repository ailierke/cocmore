package com.yunzo.cocmore.utils.base.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Description: <身份验证>. <br>
 * 
 * @date:2014年12月11日 下午3:48:25
 * @author beck
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
