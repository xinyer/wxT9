package com.tencent.t9;

import com.tencent.t9.annotation.T9SortableEntity;
import com.tencent.t9.data.PinyinType;
import com.tencent.t9.annotation.T9SearchKey;
import com.tencent.t9.annotation.T9Searchable;

@T9SortableEntity(DataSrcWeight = 2)
public class Friend {

	@T9SearchKey
	String mUin;
	
	@T9Searchable(PinyinType = PinyinType.ALL_PIN_AND_HEAD_PIN, MatchFieldSortWeight = 2)
	public String mName;

	@T9Searchable(PinyinType = PinyinType.HEAD_PIN, MatchFieldSortWeight = 1)
	public String mPhone;
	
	public Friend(String uin, String name, String phone) {
		mUin = uin;
		mName = name;
		mPhone = phone;
	}
	
}
