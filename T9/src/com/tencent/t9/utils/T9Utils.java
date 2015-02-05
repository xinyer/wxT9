package com.tencent.t9.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.tencent.t9.data.SearchableField;

/**
 * T9工具类
 * Created by browserwang on 15/2/4.
 */
public class T9Utils {

    private static final String TAG = "T9Utils";

    //**********************************************//
    //*****************字符转T9数字******************//
    //**********************************************//
    private static final int[] numbers = {2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 7,
            8, 8, 8, 9, 9, 9, 9};

    /**
     * 字符串转为T9键盘上的数字
     *
     * @param str
     * @return
     */
    public static String stringToNumber(String str) {
        if (TextUtils.isEmpty(str)) return str;

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (char c : chars) {
            int i = charToNumber(c);
            if (i > 0) {
                sb.append(charToNumber(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 字符转成T9键盘上的数字
     *
     * @param letter
     * @return success:2-9 fail:-1
     */
    private static int charToNumber(char letter) {
        if (letter >= 'A' && letter <= 'Z') {
            return numbers[letter - 'A'];
        } else if (letter >= 'a' && letter <= 'z') {
            return numbers[letter - 'a'];
        } else {
            return -1;
        }
    }

    //**********************************************//
    //***************显示高亮相关方法*****************//
    //**********************************************//

    //private static ForegroundColorSpan highlightColor = new ForegroundColorSpan(Color.rgb(18, 168, 107));

    /**
     * 高亮显示匹配的关键字
     *
     * @param highLightStr 高亮的字符串
     * @param start        匹配位置
     * @param len          匹配长度
     * @param color        高亮颜色
     * @param textView     显示的TextView
     */
    private static void setHighLight(String highLightStr, int start, int len, int color, TextView textView) {
        ForegroundColorSpan highlightColor = new ForegroundColorSpan(color);
        if (start < 0 || start + len > highLightStr.length()) {
            textView.setText(highLightStr);
        } else {
            SpannableStringBuilder style = new SpannableStringBuilder(highLightStr);
            style.setSpan(highlightColor, start, start + len, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            textView.setText(style);
        }
    }

    /**
     * 高亮显示全拼匹配的字段（汉字全拼匹配）
     *
     * @param highLightStr 高亮的字符串
     * @param index        匹配位置
     * @param len          匹配长度
     * @param color        高亮颜色
     * @param textView     显示的TextView
     */
    private static void setHighLight4AllPin(String highLightStr, int index, int len, int color,
                                             TextView textView) {
        if (TextUtils.isEmpty(highLightStr)) {
            textView.setText("");
            Log.d(TAG, "@param highLightStr is null.");
            return;
        }

        if (index < 0) {
            textView.setText(highLightStr);
            Log.d(TAG, "@param index is invalid.");
            return;
        }

        int[] pinyinLen = getPinyinLen(highLightStr);
        int newLength = 0;
        int[] newLen = getPinyinLength(pinyinLen);

        if (index == 0) {
            for (int i = 0; i < newLen.length; i++) {
                if (len <= newLen[i]) {
                    newLength = i + 1;
                    break;
                }
            }
            setHighLight(highLightStr, 0, newLength, color, textView);
        } else {
            int newIndex = 0;
            for (int i = 0; i < newLen.length; i++) {
                if (index == newLen[i]) {
                    newIndex = i + 1;
                    break;
                }
            }

            //去掉newIndex之前的数值
            int[] newLen2 = new int[pinyinLen.length - newIndex];
            for (int i = 0; i < newLen2.length; i++) {
                newLen2[i] = pinyinLen[newIndex + i];
            }

            int[] newLen3 = getPinyinLength(newLen2);
            for (int i = 0; i < newLen3.length; i++) {
                if (len <= newLen3[i]) {
                    newLength = i + 1;
                    break;
                }
            }
            setHighLight(highLightStr, newIndex, newLength, color, textView);
        }
    }

    /**
     * 高亮显示匹配关键字
     *
     * @param field     匹配的字段
     * @param color     高亮显示的颜色
     * @param textView  显示的TextView
     */
    public static void setHighLight(SearchableField field, int color, TextView textView) {
        setHighLight(field.getFieldValue(), field.getIndex(), field.getLen(), color, textView);
    }

    /**
     * 高亮显示全拼匹配的字段（汉字全拼匹配）
     *
     * @param field        匹配的字段
     * @param color        高亮颜色
     * @param textView     显示的TextView
     */
    public static void setHighLight4AllPin(SearchableField field, int color, TextView textView) {
        setHighLight4AllPin(field.getFieldValue(), field.getIndex(), field.getLen(), color, textView);
    }

    /**
     * 单个字的拼音长度->此字到开头长度
     * 张三丰 zhang san deng : [5,3,4] -> [5,8,12]
     *
     * @param pinyinLen
     * @return
     */
    private static int[] getPinyinLength(int[] pinyinLen) {
        int[] array = new int[pinyinLen.length];
        if (array.length == 0) return pinyinLen;

        array[0] = pinyinLen[0];
        for (int i = 1; i < array.length; i++) {
            array[i] = array[i - 1] + pinyinLen[i];
        }
        return array;
    }

    /**
     * 判断全拼是否匹配
     * 只有从全拼的每个首字母开始匹配才算匹配
     * 如zhangsanfeng： “sanfe”或“sanfeng”匹配，而“angsan”、“anfen”不匹配
     *
     * @param str       搜索的字符串
     * @param index     匹配的位置
     * @return
     */
    public static boolean isMatchAllPin(int index, String str) {
        if (index==0) return true;
        int[] pinyinLen = getPinyinLen(str);
        if (pinyinLen==null || pinyinLen.length==0) return false;
        boolean isMatch = false;
        int[] newLen = new int[pinyinLen.length];
        newLen[0] = 0;
        for (int i=1;i<newLen.length;i++) {
            newLen[i] = newLen[i-1] + pinyinLen[i-1];
            if (index==newLen[i]) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }

    /**
     * 获取str转拼音后每个拼音的长度
     *
     * @param str
     * @return
     */
    private static int[] getPinyinLen(String str) {
        if (TextUtils.isEmpty(str)){
            return new int[0];
        }
        int[] pinyinLen = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            String pinyin = ChnToSpell.MakeSpellCode(String.valueOf(str.charAt(i)),
                    ChnToSpell.TRANS_MODE_QUAN_PIN);
            pinyinLen[i] = pinyin.length();
        }
        return pinyinLen;
    }
}
