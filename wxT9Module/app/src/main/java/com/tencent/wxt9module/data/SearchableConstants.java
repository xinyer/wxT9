package com.tencent.wxt9module.data;

public class SearchableConstants {

	/**
	 * 匹配度
	 */
	interface MatchDgree{
		
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
