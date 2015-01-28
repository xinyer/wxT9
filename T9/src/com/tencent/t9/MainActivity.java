package com.tencent.t9;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.t9.view.T9KeyBoard;

public class MainActivity extends Activity {

	TextView tv;
	ListView listView;
	T9KeyBoard keyBoard;
	
	List<Friend> friends = new ArrayList<Friend>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tv = (TextView) findViewById(R.id.textview);
        listView = (ListView) findViewById(R.id.listview);
        keyBoard = (T9KeyBoard) findViewById(R.id.keyboard);
        getPhoneContacts();
    }

    /**
	 * 读取手机通讯录db至本地通讯录缓存
	 */ 
    private static final String[] PHONES_PROJECTION = new String[]{Phone.NUMBER, Phone.DISPLAY_NAME, Phone.CONTACT_ID };
    
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
    private static final int PHONES_NUMBER_INDEX = 1;
    private static final int PHONES_CONTACT_ID_INDEX = 2;  
      
	private void getPhoneContacts() {
		ContentResolver resolver = getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX); 
				Friend friend = new Friend(Long.toString(contactid), contactName, phoneNumber);
				friends.add(friend);
			}

			phoneCursor.close();
		}
	}
}
