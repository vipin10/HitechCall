package com.android.hitech.calls.meeting;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hitech.calls.NearByAccounts.NewCodeExpandable;
import com.android.hitech.calls.R;
import com.android.hitech.calls.database.DataBaseClass;
import com.android.hitech.calls.meeting.SelectDateFragment.SelectedDateListener;
import com.android.hitech.calls.meeting.TimePickerFragment.SelectedTimeListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Phonemeeting extends AppCompatActivity implements SelectedDateListener, SelectedTimeListener, View.OnClickListener {
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String phoneno = "phonekey";
    public static final String user = "userKey";
    String aaaaa, accName, contactPerson, destination;
    String aid, aname, cid, cname, faddress, omobile;
    EditText useraccount, usercontact, edtTxtDestination;
    Button buttonnn;
    String pnumber;
    String idofuser;
    String trimmednumber;
    int k;
    String dattt, timmm, dattim;
    LinearLayout li, mi, ms;
    private TextView txtStrtTime, txtStartDate, txtEndDate, txtEndTime;
    Date startDate, enddate;
    boolean isStartDateClicked;
    boolean isStartTimeClicked;
    String strtDate, strtTime, endDate, endTime;
    Map<String, String> hm = new HashMap();
    String userrrrr;
    MultiAutoCompleteTextView multiusers;
    ArrayList<String> myList2 = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;
    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phonemeeting);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Schedule Meeting");
        }
        li = (LinearLayout) findViewById(R.id.nerabyaccountss);
        mi = (LinearLayout) findViewById(R.id.schedulemeeting);
        ms = (LinearLayout) findViewById(R.id.meetingg);
        li.setOnClickListener(this);
        mi.setOnClickListener(this);
        ms.setOnClickListener(this);
        txtStartDate = (TextView) findViewById(R.id.txtStartDate);
        txtStartDate.setOnClickListener(Phonemeeting.this);
        txtEndDate = (TextView) findViewById(R.id.txtEndDate);
        txtEndDate.setOnClickListener(Phonemeeting.this);
        txtStrtTime = (TextView) findViewById(R.id.txtStrtTime);
        txtStrtTime.setOnClickListener(Phonemeeting.this);
        txtEndTime = (TextView) findViewById(R.id.txtEndTime);
        txtEndTime.setOnClickListener(Phonemeeting.this);
        useraccount = (EditText) findViewById(R.id.edtAccNamep);
        usercontact = (EditText) findViewById(R.id.edtcontPersonp);
        edtTxtDestination = (EditText) findViewById(R.id.edtDestinap);
        multiusers = (MultiAutoCompleteTextView) findViewById(R.id.multiUsersAttendingp);
        ArrayAdapter<String> adapterUser = new ArrayAdapter<String>(Phonemeeting.this, android.R.layout.simple_list_item_1, myList2);
        multiusers.setThreshold(1);
        multiusers.setAdapter(adapterUser);
        multiusers.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        buttonnn = (Button) findViewById(R.id.buttonnSubmitp);
        buttonnn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isAllDataEntered()) {
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate.getTime())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, enddate.getTime())
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, edtTxtDestination.getText().toString())
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                    startActivity(intent);
                }
                String m = multiusers.getText().toString();
                String kk = m.replaceAll(" ", "");
                List<String> ll = Arrays.asList(kk.split(","));
                for (k = 0; k < ll.size(); k++) {
                    userrrrr += "#" + hm.get(ll.get(k));
                }
                long result;
                DataBaseClass myDataBase = new DataBaseClass(Phonemeeting.this);
                accName = useraccount.getText().toString();
                contactPerson = usercontact.getText().toString();
                destination = edtTxtDestination.getText().toString();
                ContentValues cv = new ContentValues();
                switch (v.getId()) {
                    case R.id.buttonnSubmitp:
                        if (isAllDataEntered()) {
                            sqLiteDatabase = myDataBase.getWritableDatabase();
                            cv.put(DataBaseClass.COLOUMN_ACC_NAME, accName);
                            cv.put(DataBaseClass.COLOUMN_CONTACT, contactPerson);
                            cv.put(DataBaseClass.COLOUMN_DESTINATION, destination);
                            cv.put(DataBaseClass.COLOUMN_START_DATE, strtDate);
                            cv.put(DataBaseClass.COLOUMN_START_TIME, strtTime);
                            cv.put(DataBaseClass.COLOUMN_END_DATE, endDate);
                            cv.put(DataBaseClass.COLOUMN_END_TIME, endTime);
                            result = sqLiteDatabase.insert("MyDataBase", "", cv);
                            if (result > 0) {
                                useraccount.setText("");
                                usercontact.setText("");
                                edtTxtDestination.setText("");
                                txtStartDate.setText("");
                                txtStrtTime.setText("");
                                txtEndDate.setText("");
                                txtEndTime.setText("");
                                Toast.makeText(getApplicationContext(), "data saved at Position :" + result, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Some problem has occured..", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Add Data", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                String url1 = "https://resume.golbalhunt.in/androidservices/index.php/LocServices/meeetingSave";
                new Savemeeting().execute(url1);
            }
        });


        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
            aaaaa = sharedpreferences.getString(phoneno, "");
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            pnumber = bundle.getString("phoneno");
            trimmednumber = pnumber.trim();
            trimmednumber = trimmednumber.replace("+91", "");
        }
        System.out.println("the fetched phone no. is" + pnumber);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url2 = "https://resume.golbalhunt.in/androidservices/index.php/LocServices/getUsers";
        new GetUser1().execute(url2);
        String serverURL = "https://resume.golbalhunt.in/androidservices/index.php/LocServices/meetingMobile";
        new LongOperation12().execute(serverURL);
    }

    public void onClick(View view) {
        SelectDateFragment newFragment = new SelectDateFragment();
        TimePickerFragment newFragmentt = new TimePickerFragment();
        switch (view.getId()) {
            case R.id.txtStartDate:
                isStartDateClicked = true;
                newFragment.setListener(Phonemeeting.this);
                newFragment.show(getSupportFragmentManager(), "DatePicker");
                break;
            case R.id.txtEndDate:
                isStartDateClicked = false;
                newFragment.setListener(Phonemeeting.this);
                newFragment.show(getSupportFragmentManager(), "DatePicker");
                break;
            case R.id.txtStrtTime:
                isStartTimeClicked = true;
                newFragmentt.setListener(Phonemeeting.this);
                newFragmentt.show(getSupportFragmentManager(), "TimePicker");
                break;
            case R.id.txtEndTime:
                isStartTimeClicked = false;
                newFragmentt.setListener(Phonemeeting.this);
                newFragmentt.show(getSupportFragmentManager(), "TimePicker");
                break;
            case R.id.nerabyaccountss:
                Intent i = new Intent(getApplicationContext(), NewCodeExpandable.class);
                startActivity(i);
                break;
            case R.id.schedulemeeting:
                Toast.makeText(getApplicationContext(), "You can Schedule meetings from here", Toast.LENGTH_LONG).show();
                break;
            case R.id.meetingg:
                Intent in = new Intent(Phonemeeting.this, Meeting_Main_Exp.class);
                startActivity(in);
                break;
        }
    }

    @Override
    public void onDateSelected(int yy, int mm, int dd) {
        NumberFormat formatter = new DecimalFormat("00");
        String string = formatter.format(dd);
        int min = mm + 1;
        if (isStartDateClicked) {
            txtStartDate.setText(getString(R.string.date, yy, min, string));
            dattt = txtStartDate.getText().toString();
        } else {
            txtEndDate.setText(getString(R.string.date, yy, min, string));
        }
    }

    @Override
    public void onTimeSelected(int hourOfDay, int minute) {
        NumberFormat formatter = new DecimalFormat("00");
        String string = formatter.format(hourOfDay);
        String string1 = formatter.format(minute);
        if (isStartTimeClicked) {
            txtStrtTime.setText(getString(R.string.time, string, string1));
            timmm = txtStrtTime.getText().toString();
        } else {
            txtEndTime.setText(getString(R.string.time, string, string1));
        }
    }

    private boolean isAllDataEntered() {
        if (multiusers.length() == 0) {
            showToast("Please Add Attendees");
            return false;
        } else if (useraccount.length() == 0) {
            showToast("Account Name Can't Be Left Blank");
            return false;
        } else if (usercontact.length() == 0) {
            showToast("Contact Name Can't Be left Blank");
            return false;
        }
        if (txtStartDate.length() == 0) {
            showToast("Please select Event start date");
            return false;
        } else if (txtStrtTime.length() == 0) {
            showToast("Please select Event start time");
            return false;
        } else if (txtEndDate.length() == 0) {
            showToast("Please select Event end date");
            return false;
        } else if (txtEndTime.length() == 0) {
            showToast("Please select Event end time");
            return false;
        } else if (isStartDateGreater()) {
            showToast("Start date must be lower than end date");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean isStartDateGreater() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            startDate = dateFormat.parse(txtStartDate.getText().toString() +
                    " " + txtStrtTime.getText().toString());
            enddate = dateFormat.parse(txtEndDate.getText().toString() +
                    " " + txtEndTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate.getTime() > enddate.getTime();
    }

    private class LongOperation12 extends AsyncTask<String, Void, Void> {
        private String Content;
        private String Error = null;
        private ProgressDialog pd = new ProgressDialog(Phonemeeting.this);
        String data = "";

        protected void onPreExecute() {
            pd.setMessage("Please wait..");
            pd.show();
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            try {
                data += "&" + URLEncoder.encode("mob", "UTF-8") + "=" + trimmednumber;
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
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + " ");
                }
                Content = sb.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            pd.dismiss();
            if (Error != null) {
                Toast.makeText(getApplicationContext(), "Unable to find any match", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(Content);
                    JSONArray jsonArray = jsonObject.getJSONArray("meeting");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject resJsonObj = jsonArray.getJSONObject(i);
                        aid = resJsonObj.getString("acc_id");
                        aname = resJsonObj.getString("acc_name");
                        cid = resJsonObj.getString("con_id");
                        cname = resJsonObj.getString("con_name");
                        faddress = resJsonObj.getString("full_address");
                        omobile = resJsonObj.getString("office_mobile");
                        useraccount.setText(aname);
                        usercontact.setText(cname);
                        edtTxtDestination.setText(faddress);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    public class GetUser1 extends AsyncTask<String, Void, Void> {

        String jsonUserData;
        ProgressDialog progressDialog;
        String error2 = null;
        String getUser = "";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Phonemeeting.this);
            progressDialog.setMessage("Loading...... ");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(getUser);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "");
                }
                jsonUserData = builder.toString();

            } catch (Exception e) {
                error2 = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (error2 != null) {
                Toast.makeText(Phonemeeting.this, "Error is" + error2, Toast.LENGTH_SHORT).show();
            } else if (jsonUserData.equals(null)) {
                Toast.makeText(getApplicationContext(), "No Match found", Toast.LENGTH_LONG).show();
            } else {
            }
            try {
                JSONObject jsonObject = new JSONObject(jsonUserData);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                System.out.println("trgdhfhhfhf......" + jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject resJson2 = jsonArray.getJSONObject(i);
                    String id = resJson2.getString("id");
                    String email2 = resJson2.getString("email_address");
                    hm.put(email2, id);
                    myList2.add(email2);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Savemeeting extends AsyncTask<String, Void, Void> {

        String jsonUserData;
        ProgressDialog progressDialog;
        String error2 = null;
        String savemeetdata = "";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Phonemeeting.this);
            progressDialog.setMessage("Loading...... ");
            progressDialog.show();
            dattim = dattt + " " + timmm;
            if (sharedpreferences.contains(user)) {
                idofuser = sharedpreferences.getString(user, "");
            }
            try {
                savemeetdata += "&" + URLEncoder.encode("acc_id", "UTF-8") + "=" + aid;
                savemeetdata += "&" + URLEncoder.encode("con_id", "UTF-8") + "=" + cid;
                savemeetdata += "&" + URLEncoder.encode("uid", "UTF-8") + "=" + idofuser;
                savemeetdata += "&" + URLEncoder.encode("all_uid", "UTF-8") + "=" + userrrrr;
                savemeetdata += "&" + URLEncoder.encode("start_date_time", "UTF-8") + "=" + dattim + ":15";
                savemeetdata += "&" + URLEncoder.encode("location", "UTF-8") + "=" + faddress;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(savemeetdata);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "");
                }
                jsonUserData = builder.toString();
            } catch (Exception e) {
                error2 = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (error2 != null) {
                Toast.makeText(Phonemeeting.this, "Error is" + error2, Toast.LENGTH_SHORT).show();

            } else {
            }

        }
    }
}
