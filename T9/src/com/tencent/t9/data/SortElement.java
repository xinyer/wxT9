package com.tencent.t9.data;

/**
 * 排序条件实体
 * Created by browserwang on 15/2/9.
 */

public class SortElement {

    String name;        //条件名字
    SortWeight weight;  //排序优先级
    int count;          //条件个数

    public SortElement(String name, SortWeight weight, int count) {
        this.name = name;
        this.weight = weight;
        this.count = count;
    }
}
