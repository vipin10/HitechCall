package com.android.hitech.calls.call_logs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.hitech.calls.Alert_popup.MyServerSave;
import com.android.hitech.calls.R;
import com.android.hitech.calls.database.DbAutoSave;
import com.android.hitech.calls.homepage.SessionManager;
import com.android.hitech.calls.meeting.Phonemeeting;
import com.android.hitech.calls.splash.MyNetwork;

import java.util.ArrayList;
import java.util.List;

public class CustomList extends ArrayAdapter<String> {
    Context context;
    List<String> phNames, time, calldura, calldirection;
    String numm, idOfUser;
    SessionManager session;
    LinearLayout adddd;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String user = "userKey";
    SharedPreferences sharedpreferences;
    TextView txtPhone, txtDate, aktext;
    ImageView iv2, iv3, iv4;
    View rowView;
    DbAutoSave dbAutoSave;

    public CustomList(Context context, List<String> names, List<String> time, List<String> calldura, List<String> calldirection) {
        super(context, R.layout.custom_mobile_layout, names);
        this.context = context;
        this.phNames = names;
        this.time = time;
        this.calldura = calldura;
        this.calldirection = calldirection;
        sharedpreferences = context.getSharedPreferences(mypreference, context.MODE_PRIVATE);
        idOfUser = sharedpreferences.getString(user, "");
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbAutoSave = new DbAutoSave(context);
        if (sharedpreferences.getInt("integer", 0) == 1) {
            if (!dbAutoSave.getData((String) time.get(position))) {
                rowView = inflater.inflate(R.layout.custom_mobile_layout1, null, false);
                Intent intent = new Intent(context, MyServerSave.class);
                intent.putStringArrayListExtra("ph", (ArrayList<String>) phNames);
                intent.putStringArrayListExtra("time", (ArrayList<String>) time);
                intent.putStringArrayListExtra("calldura", (ArrayList<String>) calldura);
                intent.putStringArrayListExtra("dir", (ArrayList<String>) calldirection);
                intent.putExtra("idofuser", idOfUser);
                context.startService(intent);
            } else {
                rowView = inflater.inflate(R.layout.custom_mobile_layout, null, false);
            }
        } else {
            rowView = inflater.inflate(R.layout.custom_mobile_layout, null, false);
        }
        adddd = (LinearLayout) rowView.findViewById(R.id.addd);
        session = new SessionManager();
        txtPhone = (TextView) rowView.findViewById(R.id.textPhone);
        txtDate = (TextView) rowView.findViewById(R.id.datetext);
        iv2 = (ImageView) rowView.findViewById(R.id.img2);
        iv3 = (ImageView) rowView.findViewById(R.id.img3);
        iv4 = (ImageView) rowView.findViewById(R.id.img4);
        txtPhone.setText(phNames.get(position));
        txtDate.setText(time.get(position));
        iv2.setOnClickListener(listener);
        iv3.setOnClickListener(listener);
        iv4.setOnClickListener(listener);
        if (dbAutoSave.getData(time.get(position))) {
            aktext = (TextView) rowView.findViewById(R.id.aktext);
            aktext.setTextColor(Color.parseColor("#ff669900"));
            aktext.setText("saved");
        }
        return rowView;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View v1 = (View) v.getParent();
            View v2 = (View) v1.getParent();
            ListView v3 = (ListView) v2.getParent();
            int positionn = v3.getPositionForView(v2);
            numm = phNames.get(positionn);
            switch (v.getId()) {
                case R.id.img2:
                    Intent i = new Intent(getContext(), Phonemeeting.class);
                    i.putExtra("phoneno", numm);
                    context.startActivity(i);
                    break;
                case R.id.img3:
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + numm));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                    break;
                case R.id.img4:
                    Intent in = new Intent(Intent.ACTION_SENDTO);
                    in.setData(Uri.parse("smsto:" + numm));
                    in.putExtra("sms_body", "Hi from HiTech Calls");
                    if (in.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(in);
                    }
                    break;
            }
        }
    };


}
