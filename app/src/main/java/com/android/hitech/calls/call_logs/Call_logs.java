package com.android.hitech.calls.call_logs;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.hitech.calls.Alert_popup.AddCall1;
import com.android.hitech.calls.R;
import com.android.hitech.calls.homepage.Home_nav;

import java.util.ArrayList;
import java.util.List;

import static android.view.Menu.NONE;

public class Call_logs extends Fragment implements AdapterView.OnItemClickListener {
    TextView showdetail;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String phoneno = "phonekey";
    public static final String user = "userKey";
    public static final String time = "dateTimeKey";
    public static final String calldurat = "callduratKey";
    ListView list;
    List<String> phoneList = new ArrayList<>();
    List<String> calltimeList = new ArrayList<>();
    List<String> setdate = new ArrayList<>();
    List<String> calldura = new ArrayList<>();
    List<String> calldirection = new ArrayList<>();
    String currentphone, dateTime, callDuration1, calldir, setdate1;
    CustomList adapter;
    Cursor managedCursor;
    View view;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup group, Bundle savedInstanceState) {
        view = layoutInflater.inflate(R.layout.mobile_list, null, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        showdetail = (TextView) view.findViewById(R.id.showlog);
        sharedpreferences = getActivity().getSharedPreferences(mypreference, getContext().MODE_PRIVATE);
        sharedpreferences.edit().putInt("integer", 0).apply();
        doOperation();
        adapter = new CustomList(getActivity(), phoneList, setdate, calldura, calldirection);
        list = (ListView) view.findViewById(R.id.myList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    private void doOperation() {
        int perm = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG);
        if (perm != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managedCursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC LIMIT 100");
        if (managedCursor == null) {
            return;
        }
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        managedCursor.moveToFirst();
        while (!(managedCursor.isAfterLast())) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            java.util.Date callDayTime = new java.util.Date(Long.valueOf(callDate));
            String durationn = managedCursor.getString(duration);
            String calldatechange = callDayTime.toString();
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    calldirection.add("OUTGOING");
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    calldirection.add("INCOMING");
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    calldirection.add("MISSED");
                    break;
            }
            phoneList.add(phNumber);
            calldura.add(durationn);
            calltimeList.add(calldatechange);
            setdate.add(calldatechange);
            managedCursor.moveToNext();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentphone = phoneList.get(position);
        dateTime = calltimeList.get(position);
        callDuration1 = calldura.get(position);
        calldir = calldirection.get(position);
        setdate1 = setdate.get(position);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(phoneno, currentphone);
        editor.putString(time, dateTime);
        editor.putString(calldurat, callDuration1);
        editor.apply();
        Intent intent = new Intent(getActivity(), AddCall1.class);
        intent.putExtra("phoneno", currentphone);
        intent.putExtra("DataTime", dateTime);
        intent.putExtra("CallDuration", callDuration1);
        intent.putExtra("calldiree", calldir);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.optionmenu2,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        item.setChecked(true);
        if (item.getItemId() == R.id.a3) {
            item.setCheckable(true);
            sharedpreferences.edit().putInt("integer", 1).apply();
            list.setAdapter(adapter);
            LinearLayout tvs = (LinearLayout) view.findViewById(R.id.tvs);
            Snackbar snackbar = Snackbar.make(tvs, "Log all unsaved...", Snackbar.LENGTH_INDEFINITE);
            TextView txv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
            txv.setTextSize(19);
            snackbar.setActionTextColor(Color.parseColor("#ffff00"));
            snackbar.setAction("Save All", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), Home_nav.class));
                }
            });
            snackbar.show();
        }
        return true;
    }
}
