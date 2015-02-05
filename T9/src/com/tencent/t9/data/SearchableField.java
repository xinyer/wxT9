package com.tencent.t9.data;

import android.util.Log;

import com.tencent.t9.utils.ChnToSpell;
import com.tencent.t9.annotation.PinyinType;
import com.tencent.t9.utils.T9Utils;

/**
 * 可搜索的字段
 *
 * @author browserwang
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
     *
     * @see PinyinType
     */
    PinyinType pinyinType;

    /**
     * 字段值不拼，全拼，首字母拼
     */
    String valueNoPin, valueAllPin, valueHeadPin;

    /**
     * 匹配的拼音类型
     */
    PinyinType matchedPinyinType = PinyinType.NO_PIN;

    /**
     * 匹配的位置
     */
    int index = -1;

    /**
     * 匹配的长度
     */
    int len;

    public SearchableField(String fieldName, String fieldValue, PinyinType pinyinType) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.pinyinType = pinyinType;
        chnToSpell();
    }

    private void chnToSpell() {

        if (pinyinType == PinyinType.HEAD_PIN) {
            String pinyin = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_PINYIN_INITIAL);
            valueHeadPin = T9Utils.stringToNumber(pinyin);
            Log.d("wx", "fieldValue:" + fieldValue + "\t headPinyin:" + pinyin + "\t num:" + valueHeadPin);
        } else if (pinyinType == PinyinType.QUAN_PIN) {
            String pinyin = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_QUAN_PIN);
            valueAllPin = T9Utils.stringToNumber(pinyin);
            Log.d("wx", "fieldValue:" + fieldValue + "\t AllPinyin:" + pinyin + "\t num:" + valueAllPin);
        } else if (pinyinType == PinyinType.QUAN_PIN_AND_HEAD_PIN) {
            String pinyin1 = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_PINYIN_INITIAL);
            valueHeadPin = T9Utils.stringToNumber(pinyin1);
            String pinyin2 = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_QUAN_PIN);
            valueAllPin = T9Utils.stringToNumber(pinyin2);
            Log.d("wx", "fieldValue:" + fieldValue + "\t HeadPinyin:" + pinyin1 + "\t AllPinyin:" + pinyin2
                    + "\t headNum:" + valueHeadPin + "\t allNum:" + valueAllPin);
        } else {
            valueNoPin = fieldValue;
            Log.d("wx", "fieldValue:" + fieldValue);
        }
    }

    protected int compare(String keyword) {
        index = -1;
        len = keyword.length();
        if (pinyinType == PinyinType.HEAD_PIN) {
            matchedPinyinType = PinyinType.HEAD_PIN;
            return compare(valueHeadPin, keyword);
        } else if (pinyinType == PinyinType.QUAN_PIN) {
            matchedPinyinType = PinyinType.QUAN_PIN;
            return compare(valueAllPin, keyword);
        } else if (pinyinType == PinyinType.QUAN_PIN_AND_HEAD_PIN) {
            int matchDegree = compare(valueHeadPin, keyword);
            if (matchDegree == SearchableConstants.MatchDegree.MATCH_NO) {
                matchedPinyinType = PinyinType.QUAN_PIN;
                return compare(valueAllPin, keyword);
            } else {
                matchedPinyinType = PinyinType.HEAD_PIN;
                return matchDegree;
            }
        } else if (pinyinType == PinyinType.NO_PIN) {
            matchedPinyinType = PinyinType.NO_PIN;
            return compare(valueNoPin, keyword);
        }
        return SearchableConstants.MatchDegree.MATCH_NO;
    }

    private int compare(String value, String keyword) {
        if (value == null || keyword == null) {
            return SearchableConstants.MatchDegree.MATCH_NO;
        }

        int matchDegree = SearchableConstants.MatchDegree.MATCH_NO;
        len = keyword.length();

        if (value.equals(keyword)) {
            index = 0;
            matchDegree = SearchableConstants.MatchDegree.MATCH_FULL;
        } else if (value.startsWith(keyword)) {
            index = 0;
            matchDegree = SearchableConstants.MatchDegree.MATCH_START;
        } else if (value.contains(keyword)) {
            index = value.indexOf(keyword);
            matchDegree = SearchableConstants.MatchDegree.MATCH_CONTAINS;
        }
        return matchDegree;
    }

    /**
     * 获取字段名字
     *
     * @return
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 获取字段值
     *
     * @return
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * 获取匹配字段的位置
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取匹配的长度
     * @return
     */
    public int getLen() {
        return len;
    }

    public PinyinType getMatchedPinyinType() {
        return matchedPinyinType;
    }

    @Override
    public String toString() {
        return "FieldName:" + fieldName + "\t FieldValue:" + fieldValue + "\t PinyinTyep:" + pinyinType;
    }

}
