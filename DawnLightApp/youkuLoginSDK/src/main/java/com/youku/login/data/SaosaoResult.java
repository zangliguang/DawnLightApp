package com.youku.login.data;

import java.io.Serializable;

public class SaosaoResult implements Serializable {
	
	private static final long serialVersionUID = -3134944011488087894L;
	
	public static final int PAY_YES = 1;
	public static final int PAY_NO = 0;
	
	public long firsttime;
	public int limit;
	public String videoid;
	public String img;
	public String title;
	public int paid;
	public int device_limit;//1有设备限制  0没有 
	public int streamtypes;//0表示含有3gphd格式。1表示数据格式不含3gphd
}
