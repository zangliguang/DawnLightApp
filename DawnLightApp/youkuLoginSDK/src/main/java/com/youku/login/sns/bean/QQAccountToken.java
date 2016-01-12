package com.youku.login.sns.bean;

public class QQAccountToken {

	public String access_token;
	public String expires_in;
	public String openid;
	public String pay_token;
	public String ret;
	public String pf;
	public String pfkey;
	public String sendinstall;
	public String installwording;
	public String auth_time;
	public String page_type;
	@Override
	public String toString() {
		return "[access_token=" + access_token + 
				","+ " expires_in="+ expires_in +
				","+ " pay_token="+ pay_token +
				","+ " ret="+ ret +
				","+ " pf="+ pf +
				","+ " pfkey="+ pfkey +
				","+ " sendinstall="+ sendinstall +
				","+ " installwording="+ installwording +
				","+ " auth_time="+ auth_time +
				","+ " page_type="+ page_type +
				", openid=" + openid + "]";
	}

}
