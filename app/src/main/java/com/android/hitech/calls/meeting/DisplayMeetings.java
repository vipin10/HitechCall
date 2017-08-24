package com.android.hitech.calls.meeting;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import com.android.hitech.calls.R;
import com.android.hitech.calls.database.DataBaseClass;
import java.util.ArrayList;

public class DisplayMeetings extends AppCompatActivity {
    ArrayList<String> listDataname = new ArrayList<>();
    ArrayList<String> listData1stname = new ArrayList<>();
    ArrayList<String> listDataMb = new ArrayList<>();
    ArrayList<String> listDataAdd = new ArrayList<>();
    ExpandableListView listView1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exp_activity);
        final DataBaseClass dataBaseClass = new DataBaseClass(DisplayMeetings.this);
        SQLiteDatabase sqLiteDatabase = dataBaseClass.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("DATATABLE", new String[]{"_id", "accountName", "contactPerson", "usersAttending", "Destination", "Meeting"}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String accName = cursor.getString(1);
                String cont = cursor.getString(2);
                String userAttend = cursor.getString(3);
                String desti = cursor.getString(4);
                listDataname.add(accName);
                listData1stname.add(cont);
                listDataMb.add(userAttend);
                listDataAdd.add(desti);
                ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();
                ArrayList<String> groups1 = new ArrayList<String>();
                for (int j = 0; j < listDataname.size(); j++) {
                    listView1 = (ExpandableListView) findViewById(R.id.exListView);
                    listView1.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        int previousGroup = -1;

                        @Override
                        public void onGroupExpand(int groupPosition) {
                            if (groupPosition != previousGroup) {
                                listView1.collapseGroup(previousGroup);
                                previousGroup = groupPosition;
                            }
                        }
                    });
                    ArrayList<String> children1 = new ArrayList<String>();
                    children1.add(listData1stname.get(j));
                    children1.add(listDataMb.get(j));
                    children1.add(listDataAdd.get(j));
                    groups.add(children1);
                    groups1.add(listDataname.get(j));
                }
                meetAdapter adapter = new meetAdapter(getApplicationContext(), groups, groups1);
                listView1.setAdapter(adapter);
            }
        }
    }
}


