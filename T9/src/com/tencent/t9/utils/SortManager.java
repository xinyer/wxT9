package com.tencent.t9.utils;

import com.tencent.t9.data.SortElement;
import com.tencent.t9.data.SortWeight;

/**
 * 排序条件的初始权重
 * Created by browserwang on 15/2/5.
 */
public class SortManager {

    //***************************************************************//
    //***************条件的权重，确定排序条件的优先级先*******************//
    //***************************************************************//

    private static SortWeight DataSrcWeight        = SortWeight.RANK0;

    private static SortWeight MatchDegreeWeight    = SortWeight.RANK0;

    private static SortWeight MatchFieldWeight     = SortWeight.RANK0;

    private static SortWeight FirstCharacterWeight = SortWeight.RANK0;

    private static SortWeight MatchIndex           = SortWeight.RANK0;

    private static int DataSrcCount, MatchFieldCount;

    //***************************************************************//
    //****************条件的实际权重，计算搜索之后的权重******************//
    //***************************************************************//
    private static long w_data_src      = 0;
    private static long w_match_degree  = 0;
    private static long w_match_field   = 0;
    private static long w_first_char    = 0;
    private static long w_match_index   = 0;

    private static SortManager instance = new SortManager();

    private SortManager() {}

    public static SortManager getInstance() {
        return instance;
    }

    /**
     * 初始化条件排序等级
     * @param dataSrcWeight         数据来源
     * @param matchDegreeWeight     匹配度
     * @param matchFieldWeight      匹配字段
     * @param firstCharacterWeight  首字母
     * @param matchIndexWeight      匹配位置
     */
    public static void init(SortWeight dataSrcWeight, SortWeight matchDegreeWeight,
                            SortWeight matchFieldWeight, SortWeight firstCharacterWeight,
                            SortWeight matchIndexWeight, int dataSrcCount, int matchFieldCount) {
        DataSrcWeight = dataSrcWeight;
        MatchDegreeWeight = matchDegreeWeight;
        MatchFieldWeight = matchFieldWeight;
        FirstCharacterWeight = firstCharacterWeight;
        MatchIndex = matchIndexWeight;
        DataSrcCount = dataSrcCount;
        MatchFieldCount = matchFieldCount;

        sortElements[0] = new SortElement("w_data_src", dataSrcWeight, dataSrcCount);
        sortElements[1] = new SortElement("MatchDegree", matchDegreeWeight, 3);
        sortElements[2] = new SortElement("MatchField", matchFieldWeight, matchFieldCount);
        sortElements[3] = new SortElement("FirstCharacter", firstCharacterWeight, 26);
        sortElements[4] = new SortElement("MatchIndex", matchIndexWeight, 10);
    }

    static SortElement[] sortElements = new SortElement[5];

    private void initSortWeight() {
        for (int i=0;i<sortElements.length;i++) {
            SortElement e = sortElements[i];

        }
    }



}
