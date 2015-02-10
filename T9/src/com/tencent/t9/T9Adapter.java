package com.tencent.t9;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tencent.t9.data.PinyinType;
import com.tencent.t9.data.SearchableEntity;
import com.tencent.t9.data.SearchableField;
import com.tencent.t9.utils.T9Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        Collections.sort(entities, new Comparator<SearchableEntity>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(SearchableEntity lhs, SearchableEntity rhs) {
                return -Long.compare(lhs.getMatchField().getSortWeight(), rhs.getMatchField().getSortWeight());
            }
        });
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
            holder.weightTv = (TextView) convertView.findViewById(R.id.t9_item_weight_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchableEntity entity = getItem(position);
        SearchableField field = entity.getMatchField();
        Friend f = FriendManager.getInstance().getFriend(entity.getKeyValue());
        int color = Color.rgb(18,168,107);
        if (f!=null) {
            if (field.getFieldName().equals("mName")) {
                if (field.getMatchedPinyinType() == PinyinType.ALL_PIN) {
                    T9Utils.setHighLight4AllPin(field, color, holder.nameTv);
                } else if (field.getMatchedPinyinType() == PinyinType.HEAD_PIN) {
                    T9Utils.setHighLight(field, color, holder.nameTv);
                }

                holder.phoneTv.setText(f.mPhone);
            } else if (field.getFieldName().equals("mPhone")){
                holder.nameTv.setText(f.mName);
                T9Utils.setHighLight(field, color, holder.phoneTv);
            }

            holder.weightTv.setText(field.getSortWeight()+"");
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView nameTv;
        TextView phoneTv;
        TextView weightTv;
    }
}
