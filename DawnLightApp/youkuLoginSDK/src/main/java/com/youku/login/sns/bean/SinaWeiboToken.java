package com.youku.login.sns.bean;

public class SinaWeiboToken {
	
	public String access_token;
	public long expires_in;
	public String uid;
	public String remind_in;

	public String toString() {
		return "access_token=" + access_token + ",expires_in=" + expires_in
				+ ",uid=" + uid+ ",remind_in=" + remind_in;
	}
	
}
