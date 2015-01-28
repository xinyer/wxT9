package com.tencent.wxt9module.data;

import java.util.ArrayList;
import java.util.List;

import annotation.PinyinType;

/**
 * 从普通实体抽象出来的可以搜索的实体，只包括可以搜索的字段
 * @author browserwang
 *
 */
public class SearchableEntity {
	
	int matchDrgee;
	
	String matchFieldName;
	
	PinyinType matchPinyinType;
	
	/**
	 * 键名字
	 */
	String keyName;
	
	/**
	 * 键值
	 */
	String keyValue;
	
	/**
	 * 所有可以搜索的字段
	 */
	List<SearchableField> fields = new ArrayList<SearchableField>();
	
	public void setKey(String keyName, String keyValue) {
		this.keyName = keyName;
		this.keyValue = keyValue;
	}
	
	public void addSearchableField(SearchableField field) {
		this.fields.add(field);
	}
	
	/**
	 * 逐个字段和关键词比较
	 * 此处应该有个顺序
	 * @param keyword
	 * @return
	 */
	public boolean compare(String keyword) {
		matchDrgee = SearchableConstants.MatchDgree.MATCH_NO;
		for (SearchableField field : fields) {
			matchDrgee = field.compare(keyword);
			if (matchDrgee!=SearchableConstants.MatchDgree.MATCH_NO) {
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------------------\n");
		sb.append("KeyName:" + keyName + "\tKeyValue:" + keyValue + "\n");
		for (SearchableField searchableField : fields) {
			sb.append(searchableField.toString());
			sb.append("\n");
		}
		sb.append("\n----------------------------------------");
		return sb.toString();
	}
}
