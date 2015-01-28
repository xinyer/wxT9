package com.tencent.t9;

import com.tencent.t9.annotation.PinyinType;
import com.tencent.t9.annotation.T9SearchKey;
import com.tencent.t9.annotation.T9Searchable;

public class Friend {

	@T9SearchKey
	String mUin;
	
	@T9Searchable(PinyinType.QUAN_PIN)
	private String mName;
	
	@T9Searchable(PinyinType.HEAD_PIN)
	private String mPhone;
	
	public Friend(String uin, String name, String phone) {
		mUin = uin;
		mName = name;
		mPhone = phone;
	}
	
}
