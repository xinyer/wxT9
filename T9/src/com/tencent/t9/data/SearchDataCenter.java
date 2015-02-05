package com.tencent.t9.data;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.tencent.t9.annotation.PinyinType;
import com.tencent.t9.annotation.T9SearchKey;
import com.tencent.t9.annotation.T9Searchable;
import com.tencent.t9.utils.ChnToSpell;

public class SearchDataCenter {
	
	List<SearchableEntity> searchDataCache = new ArrayList<SearchableEntity>();
	
	List<SearchableEntity> searchResultList = new ArrayList<SearchableEntity>();

    static OnSearchCompleteListener completeListener = null;
	
	private static SearchDataCenter instance = new SearchDataCenter();

	private SearchDataCenter() {}

	public static SearchDataCenter getInstance() {
		return instance;
	}

    public static void init(Context context, OnSearchCompleteListener listener) {
        ChnToSpell.initChnToSpellDB(context);
        completeListener = listener;
    }
	
	/**
	 * 初始化搜索数据
	 * @param list
	 */
	public void initSearchData(List<? extends Object> list) {

        new SearchDataInitTask().execute(list);
	}

    public void doSearch(String keyword) {
        new SearchTask().execute(keyword);
    }

	/**
	 * 搜索数据
	 * @param keyword
	 * @return
	 */
	private void match(String keyword) {
		searchResultList.clear();
		for (SearchableEntity entity:searchDataCache) {
			boolean isMatch = entity.compare(keyword);
			if (isMatch) {
				searchResultList.add(entity);
			}
		}
	}
	
	/**
	 * 获取搜索结果
	 * @return
	 */
	public List<SearchableEntity> getMatchResult() {
		List<SearchableEntity> list = new ArrayList<SearchableEntity>();
		list.addAll(searchResultList);
		return list;
	}

    /**
     * 获取所有数据
     * @return
     */
    public List<SearchableEntity> getAllDatas() {
        List<SearchableEntity> list = new ArrayList<SearchableEntity>();
        list.addAll(searchDataCache);
        return list;
    }
	
	/**
	 * 获取实体标记T9Saearchable字段
	 * @param o
	 * @throws Exception 
	 */
	private static SearchableEntity getSearchData(Object o) throws T9SearchException{
		Field[] fields = o.getClass().getDeclaredFields();
		SearchableEntity searchableEntity = new SearchableEntity();
		boolean isSetKey = false;
		for (Field field : fields) {
			if (field.isAnnotationPresent(T9Searchable.class)) {
				T9Searchable t9Searchable= (T9Searchable) field.getAnnotation(T9Searchable.class);
                PinyinType pinyinType = t9Searchable.value();
                String value = getFieldStringValue(field, o);
                String name = field.getName();
                SearchableField searchableField = new SearchableField(name, value, pinyinType);
                searchableEntity.addSearchableField(searchableField);
			} else if (field.isAnnotationPresent(T9SearchKey.class)) {
				//T9SearchKey t9SearchKey = (T9SearchKey)field.getAnnotation(T9SearchKey.class);
				if (isSetKey) {
                    throw new T9SearchException("You have set more than one key of the SearchEntity.");
                } else {
                    String name = field.getName();
                    String value = getFieldStringValue(field, o);
                    searchableEntity.setKey(name, value);
                    isSetKey = true;
                }
			}
		}
		
		return searchableEntity;
	}
	
	/**
	 * 获取实体String类型字段的值
	 * @param field
	 * @param obj
	 * @return
	 */
	private static String getFieldStringValue(Field field, Object obj) {
		String value = "";
		field.setAccessible(true);
        if (field.getType().getName().equals(String.class.getName())) {
            try {  
                value = (String)field.get(obj);  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            }
        }
        return value;
	}

    /**
     * 搜索数据初始化
     */
    private class SearchDataInitTask extends AsyncTask<List<? extends Object>, Void, Void> {
        @Override
        protected Void doInBackground(List<? extends Object>... params) {
            if (params[0] == null || params[0].isEmpty()) {
                return null;
            }
            searchDataCache.clear();
            for (Object o:params[0]) {
                try {
                    searchDataCache.add(getSearchData(o));
                } catch (T9SearchException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private class SearchTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            match(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (completeListener!=null) {
                completeListener.onComplete(getMatchResult());
            }
        }
    }

    public interface OnSearchCompleteListener {
        public void onComplete(List<SearchableEntity> list);
    }
}
