package com.tencent.t9.data;

/**
 * Created by browserwang on 15/2/10.
 */
public class SortConstants {

    public interface MATCH_DEGREE {
        public int equals   = 3;
        public int starts   = 2;
        public int contains = 1;
    }

    public interface ACTION {
        public int DELETE = 1;
        public int ADD    = 2;
        public int UPDATE = 3;
    }

}
