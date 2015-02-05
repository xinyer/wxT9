package com.tencent.t9.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记字段是加入结果排序的
 * Created by browserwang on 15/2/5.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface T9Sortable {

    /**
     * 匹配度
     * @return
     */
    public int matchDegree() default 1;

    /**
     * 首字母
     * @return
     */
    public int firstCharacter() default 1;

    /**
     * 匹配的字段
     * @return
     */
    public int matchField() default 1;

}
