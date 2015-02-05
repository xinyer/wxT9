package com.tencent.t9.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 从普通实体抽象出来的可以搜索的实体，只包括可以搜索的字段
 * @author browserwang
 *
 */
public class SearchableEntity {
	
	MatchDegree matchDegree;

	/**
	 * 键名字,标记Entity的唯一性
	 */
	String keyName;
	
	/**
	 * 键值,标记Entity的唯一性
	 */
	String keyValue;
	
	/**
	 * 所有可以搜索的字段
	 */
	List<SearchableField> fields = new ArrayList<SearchableField>();

    /**
     * 匹配的字段
     */
    SearchableField matchedField;
	
	public void setKey(String keyName, String keyValue) {
		this.keyName = keyName;
		this.keyValue = keyValue;
	}

    public String getKeyValue() {
        return keyValue;
    }

    public String getKeyName() {
        return keyName;
    }

    public SearchableField getMatchField() {
        return matchedField;
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
		matchDegree = MatchDegree.MATCH_NO;
		for (SearchableField field : fields) {
			matchDegree = field.compare(keyword);
			if (matchDegree!= MatchDegree.MATCH_NO) {
                matchedField = field;
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
