package com.tencent.t9.data;

import android.content.Context;
import android.os.AsyncTask;

import com.tencent.t9.annotation.T9SearchKey;
import com.tencent.t9.annotation.T9Searchable;
import com.tencent.t9.annotation.T9SortableEntity;
import com.tencent.t9.utils.ChnToSpell;
import com.tencent.t9.utils.SortManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SearchDataCenter {
	
	List<SearchableEntity> searchDataCache = new ArrayList<SearchableEntity>();
	
	List<SearchableEntity> searchResultList = new ArrayList<SearchableEntity>();

    OnSearchCompleteListener completeListener = null;
	
	private static SearchDataCenter instance = new SearchDataCenter();

	private SearchDataCenter() {}

	public static SearchDataCenter getInstance() {
		return instance;
	}

    /**
     * 初始化
     * @param context   上下文引用
     * @param listener  搜索完成监听器
     */
    public void init(Context context, OnSearchCompleteListener listener) {
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

    /**
     *  添加搜索数据
     * @param list
     */
    public void appendSearchData(List<? extends Object> list) {
        new SearchDataAppendTask().execute(list);
    }

    /**
     * 搜索数据
     * @param keyword
     */
    public void doSearch(String keyword) {
        new SearchTask().execute(keyword);
    }

    /**
     * 设置以下5个排序条件的等级
     * @param dataSrcWeight             数据来源
     * @param matchDegreeWeight         匹配度
     * @param matchFieldWeight          匹配字段
     * @param firstCharacterWeight      首字母
     * @param matchIndex                匹配位置
     * @param dataSrcCount              数据来源总数
     * @param matchFieldCount           匹配字段总数
     */
    public void initSortWeight(SortWeight dataSrcWeight, SortWeight matchDegreeWeight,
                              SortWeight matchFieldWeight, SortWeight firstCharacterWeight,
                              SortWeight matchIndex, int dataSrcCount, int matchFieldCount) {
        SortManager.init(dataSrcWeight, matchDegreeWeight, matchFieldWeight, firstCharacterWeight,
                matchIndex, dataSrcCount, matchFieldCount);
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
    public List<SearchableEntity> getAllSearchData() {
        List<SearchableEntity> list = new ArrayList<SearchableEntity>();
        list.addAll(searchDataCache);
        return list;
    }
	
	/**
	 * 获取实体标记T9Searchable字段
	 * @param o
	 */
	private SearchableEntity getSearchData(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		SearchableEntity searchableEntity = new SearchableEntity();
		boolean isSetKey = false;
		for (Field field : fields) {
			if (field.isAnnotationPresent(T9Searchable.class)) {
				T9Searchable t9Searchable= (T9Searchable) field.getAnnotation(T9Searchable.class);
                PinyinType pinyinType = t9Searchable.PinyinType();
                int matchFieldWeight = t9Searchable.MatchFieldSortWeight();
                String value = getFieldStringValue(field, o);
                String name = field.getName();
                SearchableField searchableField = new SearchableField(name, value, pinyinType);
                searchableField.setMatchFieldSortWeight(matchFieldWeight);
                searchableEntity.addSearchableField(searchableField);

			} else if (field.isAnnotationPresent(T9SearchKey.class)) {
                String name = field.getName();
                String value = getFieldStringValue(field, o);
                searchableEntity.setKey(name, value);
                isSetKey = true;

			}
		}

        if(o.getClass().isAnnotationPresent(T9SortableEntity.class)){
            T9SortableEntity t9SortableEntity = (T9SortableEntity)o.getClass().getAnnotation(T9SortableEntity.class);
            int dataSrcWeight = t9SortableEntity.DataSrcWeight();
            searchableEntity.setDataSrcSortWeight(dataSrcWeight);
        }

        //Log.d("wx", searchableEntity.toString());
		
		return searchableEntity;
	}
	
	/**
	 * 获取实体String类型字段的值
	 * @param field
	 * @param obj
	 * @return
	 */
	private String getFieldStringValue(Field field, Object obj) {
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
     * 搜索数据初始化Task
     */
    private class SearchDataInitTask extends AsyncTask<List<? extends Object>, Void, Void> {
        @Override
        protected Void doInBackground(List<? extends Object>... params) {
            if (params[0] == null || params[0].isEmpty()) {
                return null;
            }
            searchDataCache.clear();
            for (Object o:params[0]) {
                searchDataCache.add(getSearchData(o));
            }
            return null;
        }
    }

    /**
     * 搜索数据初始化Task
     */
    private class SearchDataAppendTask extends AsyncTask<List<? extends Object>, Void, Void> {
        @Override
        protected Void doInBackground(List<? extends Object>... params) {
            if (params[0] == null || params[0].isEmpty()) {
                return null;
            }
            for (Object o:params[0]) {
                searchDataCache.add(getSearchData(o));
            }
            return null;
        }
    }

    /**
     * 搜索过程Task
     */
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

    /**
     * 搜索完成监听器
     */
    public interface OnSearchCompleteListener {
        public void onComplete(List<SearchableEntity> list);
    }

    /**
     * 不清空搜索数据缓存，更新某一项搜索数据
     * @param o
     * @param action @see DataAction
     */
    public void updateSearchData(Object o, DataAction action) {
        SearchableEntity entity = getSearchData(o);
        SearchableEntity existEntity = null;
        if (action==DataAction.DELETE || action==DataAction.UPDATE) {
            for (SearchableEntity e:searchDataCache) {
                if (e.equals(entity)) {
                    existEntity = e;
                    break;
                }
            }
        }
        switch (action) {
            case DELETE:
                if (existEntity==null) return;
                searchDataCache.remove(existEntity);
                break;
            case ADD:
                searchDataCache.add(entity);
                break;
            case UPDATE:
                if (existEntity==null) return;
                searchDataCache.remove(existEntity);
                searchDataCache.add(entity);
                break;
        }
    }

    /**
     * 不清空搜索数据缓存，更新list搜索数据
     * @param list
     * @param action
     */
    public void updateSearchData(List<? extends Object> list, DataAction action) {
        if (list==null || list.isEmpty()) return;
        for (Object o : list) {
            updateSearchData(o, action);
        }
    }

    /**
     * 先清空所有缓存数据，再添加list到搜索数据缓存
     * @param list
     */
    public void updateAllSearchData(List<? extends Object> list) {
        new SearchDataInitTask().execute(list);
    }

    /**
     * 清空所有缓存
     */
    public void clearSearchData() {
        if (searchDataCache!=null) {
            searchDataCache.clear();
        }
    }

    public void destroy() {
        searchDataCache.clear();
        searchResultList.clear();
    }
}
