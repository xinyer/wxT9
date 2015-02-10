package com.tencent.t9.utils;

import com.tencent.t9.data.SortElement;
import com.tencent.t9.data.SortWeight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    private static List<SortElement> sortElements = new ArrayList<SortElement>();

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

        sortElements.add(new SortElement("DataSrc", dataSrcWeight, dataSrcCount));
        sortElements.add(new SortElement("MatchDegree", matchDegreeWeight, 3));
        sortElements.add(new SortElement("MatchField", matchFieldWeight, matchFieldCount));
        sortElements.add(new SortElement("FirstCharacter", firstCharacterWeight, 26));
        sortElements.add(new SortElement("MatchIndex", matchIndexWeight, 10));

        Collections.sort(sortElements, comparator);
        initSortWeight(sortElements);
        initSortWeightParam(sortElements);
        for (SortElement e:sortElements) {
            System.out.println(e);
        }
    }

    /**
     * 初始化每个排序条件的初始权重
     */
    private static void initSortWeight(List<SortElement> list) {
        for (SortElement e : list) {
            if (e.rank==SortWeight.RANK0) {
                e.weight = 0;

            } else if (e.rank==SortWeight.RANK1) {
                e.weight = 1;

            } else if (e.rank==SortWeight.RANK2) {
                SortElement tmp = getSortElement(SortWeight.RANK1, sortElements);
                if (tmp!=null) {
                    e.weight = tmp.weight*tmp.count+1;
                } else {
                    e.weight=1;
                }

            } else if (e.rank==SortWeight.RANK3) {
                SortElement tmp = getSortElement(SortWeight.RANK2, sortElements);
                if (tmp!=null) {
                    e.weight = tmp.weight*tmp.count+1;
                } else {
                    e.weight=1;
                }
            } else if (e.rank==SortWeight.RANK4) {
                SortElement tmp = getSortElement(SortWeight.RANK3, sortElements);
                if (tmp!=null) {
                    e.weight = tmp.weight*tmp.count+1;
                } else {
                    e.weight=1;
                }
            } else if (e.rank==SortWeight.RANK5) {
                SortElement tmp = getSortElement(SortWeight.RANK4, sortElements);
                if (tmp!=null) {
                    e.weight = tmp.weight*tmp.count+1;
                } else {
                    e.weight=1;
                }
            }
        }
    }

    private static void initSortWeightParam(List<SortElement> list) {
        for (SortElement e:list) {
            if (e.name.equals("DataSrc")) {
                w_data_src = e.weight;
            } else if (e.name.equals("MatchDegree")) {
                w_match_degree = e.weight;
            } else if (e.name.equals("MatchField")) {
                w_match_field = e.weight;
            } else if (e.name.equals("FirstCharacter")) {
                w_first_char = e.weight;
            } else if (e.name.equals("MatchIndex")) {
                w_match_index = e.weight;
            }
        }
    }

    /**
     * 根据排序条件的优先级先排序List
     */
    static Comparator<SortElement> comparator = new Comparator<SortElement>() {
        @Override
        public int compare(SortElement lhs, SortElement rhs) {
            return lhs.rank.compareTo(rhs.rank);
        }
    };

    /**
     * 根据排序条件优先级获取排序条件
     * @param rank
     * @param list
     * @return
     */
    private static SortElement getSortElement(SortWeight rank, List<SortElement> list) {
        if (list==null || list.isEmpty()) return null;

        for (SortElement e:list) {
            if (e.rank==rank) return e;
        }

        return null;
    }

    /**
     * 计算权重
     * @return
     */
    public static long calculateSortWeight(int dataSrc, int matchDegree, int matchField,
                                          char firstChar, int matchIndex) {
        return w_data_src*dataSrc + w_match_degree*matchDegree + w_match_field*matchField
               + w_first_char*getFirstCharWeight(firstChar) + w_match_index*matchIndex;
    }

    private static int getFirstCharWeight(char c) {
        if (c>='a' && c<='z') {
            return 'z'-c+1;
        } else if (c>='A' && c<='Z') {
            return 'Z'-c+1;
        } else {
            return 0;
        }
    }

}
