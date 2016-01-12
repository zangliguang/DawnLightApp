package com.youku.login.sns;

public interface ServiceShellListener<T> {
	
	public void completed(T data);
	
	public boolean failed(String message);	
}
