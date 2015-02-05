package com.tencent.t9;

import com.tencent.t9.annotation.T9SortableEntity;
import com.tencent.t9.annotation.T9SortableField;
import com.tencent.t9.data.PinyinType;
import com.tencent.t9.annotation.T9SearchKey;
import com.tencent.t9.annotation.T9Searchable;

@T9SortableEntity(DataSrc = 1)
public class Friend {

	@T9SearchKey
	String mUin;
	
	@T9Searchable(PinyinType.ALL_PIN_AND_HEAD_PIN)
    @T9SortableField(MatchField = 2)
	public String mName;

	@T9Searchable
    @T9SortableField(MatchField = 1)
	public String mPhone;
	
	public Friend(String uin, String name, String phone) {
		mUin = uin;
		mName = name;
		mPhone = phone;
	}
	
}
