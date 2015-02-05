package com.tencent.t9.utils;

import com.tencent.t9.data.SortWeight;

/**
 * 排序条件的初始权重
 * Created by browserwang on 15/2/5.
 */
public class SortManager {

    public static SortWeight DataSrcWeight        = SortWeight.RANK0;

    public static SortWeight MatchDegreeWeight    = SortWeight.RANK0;

    public static SortWeight MatchFieldWeight     = SortWeight.RANK0;

    public static SortWeight FirstCharacterWeight = SortWeight.RANK0;

    public static SortWeight MatchIndex           = SortWeight.RANK0;


    private static SortManager instance = new SortManager();

    private SortManager() {}

    public static SortManager getInstance() {
        return instance;
    }

    /**
     * 初始化排序等级
     * @param dataSrcWeight         数据来源
     * @param matchDegreeWeight     匹配度
     * @param matchFieldWeight      匹配字段
     * @param firstCharacterWeight  首字母
     */
    public static void init(SortWeight dataSrcWeight, SortWeight matchDegreeWeight,
                            SortWeight matchFieldWeight, SortWeight firstCharacterWeight,
                            SortWeight matchIndex) {
        DataSrcWeight = dataSrcWeight;
        MatchDegreeWeight = matchDegreeWeight;
        MatchFieldWeight = matchFieldWeight;
        FirstCharacterWeight = firstCharacterWeight;
        MatchIndex = matchIndex;
    }

}
