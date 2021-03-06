package com.tencent.t9;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.t9.data.SearchDataCenter;
import com.tencent.t9.data.SearchableEntity;
import com.tencent.t9.data.SortWeight;
import com.tencent.t9.view.T9KeyBoard;

import java.util.List;

public class MainActivity extends Activity implements T9KeyBoard.onDialBtnClickListener,
        T9KeyBoard.onKeyClickListener, ListView.OnScrollListener, SearchDataCenter.OnSearchCompleteListener{

	TextView tv;
	ListView listView;
	T9KeyBoard keyBoard;

    T9Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tv = (TextView) findViewById(R.id.textview);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new T9Adapter(this);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        keyBoard = (T9KeyBoard) findViewById(R.id.keyboard);
        keyBoard.setOnDialBtnClickListener(this);
        keyBoard.setOnKeyClickListener(this);

        FriendManager.getInstance().initData(getContentResolver());
        List<Friend> friends = FriendManager.getInstance().getFriends();

        SearchDataCenter.getInstance().init(this, this);
        SearchDataCenter.getInstance().initSearchData(friends);

        SearchDataCenter.getInstance().initSortWeight(SortWeight.RANK5, SortWeight.RANK4,
                SortWeight.RANK3, SortWeight.RANK2, SortWeight.RANK1, 1, 2);
    }

    @Override
    public void onDialBtnClick(String str) {

    }

    @Override
    public void onResult(String str, boolean isShowResult) {
        tv.setText(str);
        SearchDataCenter.getInstance().doSearch(str);
    }

    @Override
    public void onResultLastChar(char c) {

    }

    @Override
    public void onComplete(List<SearchableEntity> list) {
        adapter.loadData(list);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            if (keyBoard.getVisibility()==View.VISIBLE) {
                keyBoard.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
//        menu.getItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                keyBoard.setVisibility(View.VISIBLE);
                break;
            case R.id.item2:
                keyBoard.setVisibility(View.INVISIBLE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SearchDataCenter.getInstance().destroy();
    }
}
