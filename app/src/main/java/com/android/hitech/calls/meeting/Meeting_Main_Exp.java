package com.android.hitech.calls.meeting;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.hitech.calls.R;
import com.android.hitech.calls.database.DataBaseClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.hitech.calls.database.DataBaseClass.COLOUMN_START_DATE;


public class Meeting_Main_Exp extends AppCompatActivity {
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    String aa;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_main);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Mettings");
        }
        expandableListView = (ExpandableListView) findViewById(R.id.EexpanListView);
        expandableListDetail = getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new Meeting_CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            int previousGroup = 1;

            @Override
            public void onGroupCollapse(int groupPosition) {
                if (groupPosition != previousGroup) {
                    expandableListView.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });

    }

    private HashMap<String, List<String>> getData() {

        DataBaseClass dataBaseClass = new DataBaseClass(Meeting_Main_Exp.this);
        String[] coloumns = new String[]{DataBaseClass.COLOUMN_ID, DataBaseClass.COLOUMN_ACC_NAME, DataBaseClass.COLOUMN_CONTACT,
                DataBaseClass.COLOUMN_DESTINATION, COLOUMN_START_DATE};

        Cursor cursor = dataBaseClass.getWritableDatabase().query(DataBaseClass.TABLE_NAME, coloumns, null, null, null, null, DataBaseClass.COLOUMN_ID + " DESC");
        HashMap<String, List<String>> hashMap = new HashMap<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                aa = cursor.getString(cursor.getColumnIndex(DataBaseClass.COLOUMN_ID));
                List<String> list = new ArrayList<>();
                list.add(cursor.getString(cursor.getColumnIndex(DataBaseClass.COLOUMN_ACC_NAME)));
                list.add(cursor.getString(cursor.getColumnIndex(DataBaseClass.COLOUMN_CONTACT)));
                list.add(cursor.getString(cursor.getColumnIndex(DataBaseClass.COLOUMN_DESTINATION)));
                list.add(cursor.getString(cursor.getColumnIndex(COLOUMN_START_DATE)));
                hashMap.put("Meeting " + aa, list);
            }
            cursor.close();
        }
        return hashMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
}

