package com.tencent.t9.data;

public class SearchableConstants {

	/**
	 * 匹配度
	 */
	interface MatchDegree {
		
		//不匹配
		int MATCH_NO 		= 0;
		
		//开头匹配
		int MATCH_START 	= 1;
		
		//中间匹配
		int MATCH_CONTAINS 	= 2;
		
		//完全匹配
		int MATCH_FULL 		= 3;
	}
}
