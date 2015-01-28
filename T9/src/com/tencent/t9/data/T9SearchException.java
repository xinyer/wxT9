package com.tencent.t9.data;

public class T9SearchException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public T9SearchException(String msg) {
		new Exception(msg);
	}
}
