package com.tencent.t9.data;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.t9.utils.ChnToSpell;
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

        } else if (pinyinType == PinyinType.ALL_PIN) {
            String pinyin = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_QUAN_PIN);
            valueAllPin = T9Utils.stringToNumber(pinyin);
            Log.d("wx", "fieldValue:" + fieldValue + "\t AllPinyin:" + pinyin + "\t num:" + valueAllPin);

        } else if (pinyinType == PinyinType.ALL_PIN_AND_HEAD_PIN) {
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

    protected MatchDegree compare(String keyword) {
        return compare(keyword, pinyinType);
    }

    private MatchDegree compare(String keyword, PinyinType pinyinType) {
        if (TextUtils.isEmpty(keyword)) {
            return MatchDegree.MATCH_NO;
        }

        MatchDegree matchDegree = MatchDegree.MATCH_NO;
        len = keyword.length();
        index = -1;

        switch (pinyinType) {
            case ALL_PIN:
                matchedPinyinType = PinyinType.ALL_PIN;
                if (valueAllPin.equals(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_FULL;

                } else if (valueAllPin.startsWith(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_START;

                } else if (valueAllPin.contains(keyword)) {
                    index = valueAllPin.indexOf(keyword);
                    if (T9Utils.isMatchAllPin(index, fieldValue))
                        matchDegree = MatchDegree.MATCH_PART;
                     else
                        matchDegree = MatchDegree.MATCH_NO;
                } else {
                    matchedPinyinType = PinyinType.NO_PIN;

                }

                break;
            case HEAD_PIN:
                matchedPinyinType = PinyinType.HEAD_PIN;
                if (valueHeadPin.equals(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_FULL;

                } else if (valueHeadPin.startsWith(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_START;

                } else if (valueHeadPin.contains(keyword)) {
                    index = valueHeadPin.indexOf(keyword);
                    matchDegree = MatchDegree.MATCH_PART;

                } else {
                    matchedPinyinType = PinyinType.NO_PIN;

                }

                break;
            case ALL_PIN_AND_HEAD_PIN:

                /*先比较首拼，再比较全拼*/
                matchedPinyinType = PinyinType.HEAD_PIN;
                if (valueHeadPin.equals(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_FULL;

                } else if (valueHeadPin.startsWith(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_START;

                } else if (valueHeadPin.contains(keyword)) {
                    index = valueHeadPin.indexOf(keyword);
                    matchDegree = MatchDegree.MATCH_PART;

                } else {

                    matchedPinyinType = PinyinType.NO_PIN;
                }

                /*首拼匹配返回，否则匹配全拼*/
                if (matchDegree!=MatchDegree.MATCH_NO) break;

                matchedPinyinType = PinyinType.ALL_PIN;
                if (valueAllPin.equals(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_FULL;

                } else if (valueAllPin.startsWith(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_START;

                } else if (valueAllPin.contains(keyword)) {
                    index = valueAllPin.indexOf(keyword);
                    if (T9Utils.isMatchAllPin(index, fieldValue))
                        matchDegree = MatchDegree.MATCH_PART;
                    else
                        matchDegree = MatchDegree.MATCH_NO;
                } else {

                    matchedPinyinType = PinyinType.ALL_PIN;
                }

                break;
            case NO_PIN:
                matchedPinyinType = PinyinType.NO_PIN;
                if (valueNoPin.equals(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_FULL;

                } else if (valueNoPin.startsWith(keyword)) {
                    index=0;
                    matchDegree = MatchDegree.MATCH_START;

                } else if (valueNoPin.contains(keyword)) {
                    index = valueNoPin.indexOf(keyword);
                    matchDegree = MatchDegree.MATCH_PART;

                } else {

                    matchedPinyinType = PinyinType.NO_PIN;
                }
                break;
            default:
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
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取匹配的长度
     *
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
