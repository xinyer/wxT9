package com.tencent.t9.data;

import com.tencent.t9.utils.ChnToSpell;
import com.tencent.t9.annotation.PinyinType;

/**
 * 可搜索的字段
 * @author browserwang
 *
 */
public class SearchableField {
	/**
	 * 字段名字，原实体类的变量名字
	 */
	String fieldName;
	
	/**
	 * 字段值
	 */
	String fieldValue;
	
	/**
	 * 转拼音类型
	 * @see PinyinType
	 */
	PinyinType pinyinType;
	
	String valueQuanPin, valueHeadPin;
	
	public SearchableField(String fieldName, String fieldValue, PinyinType pinyinType) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.pinyinType = pinyinType;
		chnToSpell();
	}
	
	private void chnToSpell() {
		if (pinyinType == PinyinType.HEAD_PIN) {
			valueHeadPin = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_PINYIN_INITIAL);
		} else if (pinyinType == PinyinType.QUAN_PIN) {
			valueQuanPin = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_QUAN_PIN);
		} else if (pinyinType == PinyinType.QUAN_PIN_AND_HEAD_PIN) {
			valueHeadPin = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_PINYIN_INITIAL);
			valueQuanPin = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_QUAN_PIN);
		}
	}
	
	int compare(String keyword) {
		if (pinyinType == PinyinType.HEAD_PIN) {
			return compare(valueHeadPin, keyword);
		} else if (pinyinType == PinyinType.QUAN_PIN) {
			return compare(valueQuanPin, keyword);
		} else if (pinyinType == PinyinType.QUAN_PIN_AND_HEAD_PIN) {
			int matchDrgee = compare(valueHeadPin, keyword);
			if (matchDrgee==SearchableConstants.MatchDgree.MATCH_NO) {
				return compare(valueQuanPin, keyword);
			} else {
				return matchDrgee;
			}
		}
		return SearchableConstants.MatchDgree.MATCH_NO;
	}
	
	private int compare(String value, String keyword) {
		if (value==null || keyword==null) {
			return SearchableConstants.MatchDgree.MATCH_NO;
		}
		
		int matchDrgee = SearchableConstants.MatchDgree.MATCH_NO;
		if (value.equals(keyword)) {
			matchDrgee = SearchableConstants.MatchDgree.MATCH_FULL;
		} else if (value.startsWith(keyword)) {
			matchDrgee = SearchableConstants.MatchDgree.MATCH_START;
		} else if (value.contains(keyword)) {
			matchDrgee = SearchableConstants.MatchDgree.MATCH_CONTAINS;
		}
		return matchDrgee;
	}
	
	@Override
	public String toString() {
		return "FieldName:" + fieldName + "\t FieldValue:" + fieldValue + "\t PinyinTyep:" + pinyinType;
	}
	
}
