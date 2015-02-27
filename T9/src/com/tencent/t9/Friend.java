package com.tencent.t9;

import com.tencent.t9.annotation.T9SearchKey;
import com.tencent.t9.annotation.T9Searchable;
import com.tencent.t9.annotation.T9SortableEntity;
import com.tencent.t9.data.PinyinType;

@T9SortableEntity(DataSrcWeight = 1)
public class Friend {

	@T9SearchKey
	String mUin;
	
	@T9Searchable(PinyinType = PinyinType.ALL_PIN_AND_HEAD_PIN, MatchFieldSortWeight = 2)
	public String mName;

	@T9Searchable( MatchFieldSortWeight = 1)
	public String mPhone;
	
	public Friend(String uin, String name, String phone) {
		mUin = uin;
		mName = name;
		mPhone = phone;
	}
	
}
