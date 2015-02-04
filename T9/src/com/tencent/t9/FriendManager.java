package com.tencent.t9;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by browserwang on 15/2/2.
 */
public class FriendManager {
    private static FriendManager instance = new FriendManager();

    private FriendManager() {
    }

    public static FriendManager getInstance() {
        return instance;
    }

    private List<Friend> friends = new ArrayList<Friend>();

    private Map<String, Friend> friendMap = new HashMap<String, Friend>();
    /**
     * 读取手机通讯录db至本地通讯录缓存
     */
    private static final String[] PHONES_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    private static final int PHONES_DISPLAY_NAME_INDEX = 1;
    private static final int PHONES_NUMBER_INDEX = 0;
    private static final int PHONES_CONTACT_ID_INDEX = 2;

    public void initData(ContentResolver resolver) {
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        friends.clear();
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                Long contactId = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                Friend friend = new Friend(Long.toString(contactId), contactName, phoneNumber);
                friends.add(friend);
                friendMap.put(friend.mUin, friend);
            }

            phoneCursor.close();
        }
    }

    public List<Friend> getFriends() {
        List<Friend> list = new ArrayList<Friend>();
        list.addAll(friends);
        return list;
    }

    public Friend getFriend(String key) {
        return friendMap.get(key);
    }
}
