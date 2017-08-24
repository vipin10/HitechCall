package com.android.hitech.calls.Alert_popup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hitech.calls.R;
import com.android.hitech.calls.database.DbAutoSave;
import com.android.hitech.calls.homepage.Home_nav;
import com.android.hitech.calls.homepage.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddCall1 extends AppCompatActivity {
    ProgressDialog pd;
    static EditText mobile, anchorinfo, calldesc;
    SessionManager session;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String user = "userKey";
    private String phoneNos;
    private String callTypes;
    String callDurations, callDates;
    TextView jsonParsed;
    String idofuser, getaccid, accid, contactid, Oppurtunitydata, tab_row;
    String getrr, idoppur, callDurationn, dirrr, dateTime, ggg, idjob, c_idd, getcalldesc, oppurjson, mobileno, getaction, getsubject;
    Spinner profileSpinner, subjectspinner, actionspinner;
    ArrayAdapter<String> actionadap, subjectadap, adaptercity, adapterModule;
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<String> oppurlist = new ArrayList<>();
    ArrayList<String> listaccount = new ArrayList<>();
    ArrayList<String> listcontact = new ArrayList<>();
    List<String> strList;
    List<String> strrr;
    ArrayAdapter<String> datAdapter1;
    Context context;
    String[] clientCall, introductoryCall, meetingCall, pOCClientCall, agreementCall, positionCall, offerJoining, candidateCAll, jobCall, feedbackCall, interviewCall, offeJoinCall;
    String[] docuFolUpCal = {"Asked for the list of Document", "Confirmation on list of document recieved"};
    String[] profileStr = {"Accounts", "Candidates", "Contacts", "Opportunities",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_call);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        context = this.getBaseContext();
        getRecentCallDetails();
        session = new SessionManager();
        mobile = (EditText) findViewById(R.id.edtMobile);
        calldesc = (EditText) findViewById(R.id.calldescription);
        anchorinfo = (EditText) findViewById(R.id.anchor);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            anchorinfo.setText(sharedpreferences.getString(Name, ""));
        }
        String phone = getIntent().getStringExtra("phoneno");
        dateTime = getIntent().getStringExtra("DataTime");
        callDurationn = getIntent().getStringExtra("CallDuration");
        dirrr = getIntent().getStringExtra("calldiree");
        profileSpinner = (Spinner) findViewById(R.id.spinProfileItem);
        subjectspinner = (Spinner) findViewById(R.id.spinnerItem3);
        actionspinner = (Spinner) findViewById(R.id.spinProAction);
        adapterModule = new ArrayAdapter<String>(context, R.layout.spinner_item, profileStr);
        profileSpinner.setAdapter(adapterModule);
        if (getCallTypes() == getCallTypes()) {
            if (getCallTypes().contains("INCOMING")) {
                strList = new ArrayList<String>(Arrays.asList("INCOMING"));
            }
            if (getCallTypes().contains("OUTGOING")) {
                strList = new ArrayList<String>(Arrays.asList("OUTGOING"));
            }
            try {

                if (getCallTypes().contains("MISSED")) {
                    strList = new ArrayList<String>(Arrays.asList("MISSED"));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        datAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strrr);
        datAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mobile.setText(phone);
        mobileno = phone;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        candidateCAll = getResources().getStringArray(R.array.candidateCAll);
        clientCall = context.getResources().getStringArray(R.array.clientCall);
        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getrr = accid;
                    getaccid = accid;
                    adaptercity = new ArrayAdapter<String>(context, R.layout.spinner_item, listaccount);
                    subjectadap = new ArrayAdapter<String>(context, R.layout.spinner_item, clientCall);
                } else if (position == 1) {
                    getrr = idjob;
                    adaptercity.notifyDataSetChanged();
                    adaptercity = new ArrayAdapter<String>(context, R.layout.spinner_item, list1);
                    subjectadap = new ArrayAdapter<String>(context, R.layout.spinner_item, candidateCAll);
                } else if (position == 2) {
                    getrr = c_idd;
                    System.out.println("position is 2" + getrr);
                    adaptercity = new ArrayAdapter<String>(context, R.layout.spinner_item, listcontact);
                    subjectadap = new ArrayAdapter<String>(context, R.layout.spinner_item, clientCall);
                } else if (position == 3) {
                    getrr = idoppur;
                    System.out.println("position is 3" + getrr);
                    adaptercity = new ArrayAdapter<String>(context, R.layout.spinner_item, oppurlist);
                    subjectadap = new ArrayAdapter<String>(context, R.layout.spinner_item, candidateCAll);
                }
                subjectspinner.setAdapter(subjectadap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subjectspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offerJoining = getResources().getStringArray(R.array.offerJoining);
                introductoryCall = context.getResources().getStringArray(R.array.introductoryCall);
                meetingCall = context.getResources().getStringArray(R.array.meetingCall);
                pOCClientCall = getResources().getStringArray(R.array.pOCClientCall);
                agreementCall = getResources().getStringArray(R.array.agreementCall);
                positionCall = getResources().getStringArray(R.array.positionCall);
                offerJoining = getResources().getStringArray(R.array.offerJoining);
                jobCall = getResources().getStringArray(R.array.jobCall);
                feedbackCall = getResources().getStringArray(R.array.feedbackCall);
                interviewCall = getResources().getStringArray(R.array.interviewCall);
                offeJoinCall = getResources().getStringArray(R.array.offeJoinCall);
                switch (position) {
                    case 0:
                        if (parent.getItemAtPosition(position).toString() == "Introductory Call") {

                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, introductoryCall);
                        } else {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, jobCall);
                        }
                        break;
                    case 1:
                        if (parent.getItemAtPosition(position).toString() == "Meeting Call") {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, meetingCall);
                        } else {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, feedbackCall);
                        }
                        break;
                    case 2:
                        if (parent.getItemAtPosition(position).toString() == "New POC-Client Call") {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, pOCClientCall);
                        } else {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, interviewCall);
                        }
                        break;
                    case 3:
                        if (parent.getItemAtPosition(position).toString() == "Agreement Call") {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, agreementCall);
                        } else {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, docuFolUpCal);
                        }
                        break;
                    case 4:
                        if (parent.getItemAtPosition(position).toString() == "Position Call") {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, positionCall);
                        } else {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, offeJoinCall);
                        }
                        break;
                    case 5:
                        if (parent.getItemAtPosition(position).toString() == "Offer and Joining") {
                            actionadap = new ArrayAdapter<String>(context, R.layout.spinner_item, offerJoining);
                        }
                        break;
                }
                actionspinner.setAdapter(actionadap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tick, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.homebutton:
                getcalldesc = String.valueOf(calldesc.getText());
                String serverURL = "http://192.168.12.37/androidservices/index.php/api/Service/update";
                new Senddata().execute(serverURL);
                return true;
            default:
                NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        String serverURL = "http://192.168.12.37/androidservices/index.php/api/Service/contact";
        new LongOperation().execute(serverURL);
    }

    private void getRecentCallDetails() {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<StringBuffer> list = new ArrayList<StringBuffer>();
        int perm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        if (perm != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        stringBuffer.append("Call Log :");
        while (managedCursor.moveToLast()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            stringBuffer.append("\nPhone Number:--- ").append(phNumber).append(" \nCall Type:--- ").append(dir).append(" \nCall Date:--- ").append(callDayTime).append(" \nCall duration in sec :--- ").append(callDuration);
            list.add(stringBuffer);

            String[] toStoreData;
            String[] toStoreData1;
            if ((list.get(list.size() - 1).toString()).contains("Phone Number")) {
                toStoreData = (list.get(list.size() - 1).toString()).split("Phone Number:+");
                String[] PhoneNo = toStoreData[1].split("Call Type:");
                phoneNos = PhoneNo[0].replace("---", "");
                callTypes = PhoneNo[1].replace("---", "");
                toStoreData1 = (list.get(list.size() - 1).toString()).split("Call Date:");
                String[] CallDuration = toStoreData1[1].split("Call duration in sec");
                callDates = CallDuration[0].replace("---", "");
                callDurations = CallDuration[1].replace(":---", "");
            }
            break;

        }
        getPhoneNos();
        managedCursor.close();
    }

    public void setPhoneNo(String phoneNos) {
        this.phoneNos = phoneNos;
    }

    public String getPhoneNos() {
        return phoneNos;
    }

    public void setCallTypes(String callTypes) {
        this.callTypes = callTypes;
    }

    public String getCallTypes() {
        return callTypes;
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {
        String Content;
        String Error = null;
        String data;

        protected void onPreExecute() {
            EditText serverText = (EditText) findViewById(R.id.edtMobile);
            pd = new ProgressDialog(AddCall1.this);
            pd.setMessage("Please wait..");
            pd.show();
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            try {
                ggg = String.valueOf(serverText.getText());
                ggg = ggg.trim();
                ggg = ggg.replace("+91", "");
                data += "&" + URLEncoder.encode("data", "UTF-8") + "=" + ggg;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        protected Void doInBackground(String... urls) {
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Content = sb.toString();
            } catch (Exception ex) {
                Error = ex.getMessage();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            pd.dismiss();
            jsonParsed = (TextView) findViewById(R.id.contactperson);
            if (Error != null) {
                Toast.makeText(context, "Unable to find any match", Toast.LENGTH_LONG).show();
            } else {
                String OutputData = null;
                try {
                    JSONObject jsonRootObject = new JSONObject(Content);
                    JSONObject json2 = jsonRootObject.getJSONObject("name");
                    accid = json2.optString("a_id");
                    String name = json2.optString("candidate_name");
                    c_idd = json2.optString("c_id");
                    contactid = c_idd;
                    listcontact.add(name);
                    OutputData = name;
                    Oppurtunitydata += c_idd;
                    if (Content.equals(null) || Content == "" || Content.equals("NULL")) {
                        jsonParsed.setText("Nothing found");
                    } else {
                        jsonParsed.setText(OutputData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Senddata extends AsyncTask<String, Void, String> {
        String oppurerror = null;
        String oppurdata;

        protected void onPreExecute() {
            pd = new ProgressDialog(AddCall1.this);
            pd.setMessage("Please wait..");
            pd.show();
            pd.setCancelable(false);
            try {
                if (sharedpreferences.contains(user)) {
                    idofuser = sharedpreferences.getString(user, "");
                }
                getsubject = subjectspinner.getSelectedItem().toString();
                getaction = actionspinner.getSelectedItem().toString();
                oppurdata += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + idofuser;
                oppurdata += "&" + URLEncoder.encode("call_start", "UTF-8") + "=" + dateTime;
                oppurdata += "&" + URLEncoder.encode("duration", "UTF-8") + "=" + callDurationn;
                oppurdata += "&" + URLEncoder.encode("direction", "UTF-8") + "=" + dirrr;
                oppurdata += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + getsubject;
                oppurdata += "&" + URLEncoder.encode("desc", "UTF-8") + "=" + getcalldesc;
                oppurdata += "&" + URLEncoder.encode("mob", "UTF-8") + "=" + ggg;
                oppurdata += "&" + URLEncoder.encode("action", "UTF-8") + "=" + getaction;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... urls) {
            BufferedReader reader;
            try {
                URL url = new URL(urls[0]);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(oppurdata);
                wr.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                oppurjson = sb.toString();
            } catch (Exception ex) {
                oppurerror = ex.getMessage();
            }
            return oppurjson;
        }

        protected void onPostExecute(String strdb) {
            JSONObject jobj, jobj1;
            if (strdb != null) {
                try {
                    jobj = new JSONObject(strdb);
                    jobj1 = jobj.getJSONObject("jsonobj");
                    tab_row = jobj1.getString("calls_table_id");
                    DbAutoSave dbAutoSave = new DbAutoSave(AddCall1.this);
                    dbAutoSave.insertData(dateTime, tab_row);
                    Toast.makeText(context, "Log Saved successfully", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Unable to Create Log", Toast.LENGTH_LONG).show();
            }
            pd.cancel();
            startActivity(new Intent(AddCall1.this, Home_nav.class));
        }

    }
}