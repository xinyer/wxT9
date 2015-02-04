package com.tencent.t9.utils;

import android.text.TextUtils;

/**
 * Created by browserwang on 15/2/4.
 */
public class T9Utils {

    private static final int[] numbers = {2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 7,
        8, 8, 8, 9, 9, 9, 9};

    /**
     * 字符串转为T9键盘上的数字
     * @param str
     * @return
     */
    public static String stringToNumber (String str) {
        if (TextUtils.isEmpty(str)) return str;

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (char c : chars) {
            int i = charToNumber(c);
            if (i>0){
                sb.append(charToNumber(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 字符转成T9键盘上的数字
     * @param letter
     * @return success:2-9 fail:-1
     */
    private static int charToNumber (char letter) {
        if (letter>='A' && letter<='Z') {
            return numbers[letter-'A'];
        } else if (letter>='a' && letter<='z') {
            return numbers[letter-'a'];
        } else {
            return -1;
        }
    }
}
