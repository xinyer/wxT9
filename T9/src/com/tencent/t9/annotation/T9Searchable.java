package com.tencent.t9.annotation;

import com.tencent.t9.data.PinyinType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记字段是否可以搜索
 * Created by browserwang on 15/1/28.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface T9Searchable {

    /**
     * 转拼音类型
     * @return
     */
	PinyinType PinyinType() default PinyinType.NO_PIN;

    /**
     * 排序权重：匹配的字段
     * @return
     */
    public int MatchFieldSortWeight() default 1;

}
