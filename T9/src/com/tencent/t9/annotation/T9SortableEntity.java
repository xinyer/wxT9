package com.tencent.t9.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记“字段”这一级排序的字段
 * Created by browserwang on 15/2/5.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface T9SortableEntity {

   /**
    * 数据来源权重
    * @return
    */
    public int DataSrcWeight() default 1;

}
