package com.tencent.t9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tencent.t9.data.SearchableEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by browserwang on 15/1/31.
 */
public class T9Adapter extends BaseAdapter {

    private Context mContext;

    private List<SearchableEntity> entities;

    public T9Adapter(Context context) {
        mContext = context;
        entities = new ArrayList<>();
    }

    public void loadData(List<SearchableEntity> list) {
        entities.clear();
        entities.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public SearchableEntity getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.t9_list_item, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.t9_item_name_tv);
            holder.phoneTv = (TextView) convertView.findViewById(R.id.t9_item_phone_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchableEntity entity = getItem(position);
        Friend f = FriendManager.getInstance().getFriend(entity.getKeyValue());
        if (entity.getMatchField().getFieldName().equals("mName")) {
            holder.nameTv.setText(f.mName);
            holder.phoneTv.setText(f.mPhone);
        } else if (entity.getMatchField().getFieldName().equals("mPhone")){
            holder.nameTv.setText(f.mName);
            holder.phoneTv.setText(f.mPhone);
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView nameTv;
        TextView phoneTv;
    }
}
