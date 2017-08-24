package com.android.hitech.calls.meeting;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {
   SelectedTimeListener listener;
   public interface SelectedTimeListener{
       void onTimeSelected(int hourOfDay, int minute);
   }

   public void setListener(SelectedTimeListener listener){
       this.listener = listener;
   }
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState){
       Context context = getActivity().getBaseContext();
       Calendar c = Calendar.getInstance();
       int hour = c.get(Calendar.HOUR_OF_DAY);
       int minute = c.get(Calendar.MINUTE);
       TimePickerDialog tpd = new TimePickerDialog(getActivity(),android.support.design.R.style.Base_Theme_AppCompat_Light_Dialog, this, hour, minute, true);
       TextView tvTitle = new TextView(context);
       tvTitle.setText("Select Time");
       tvTitle.setBackgroundColor(Color.parseColor("#EEE8AA"));
       tvTitle.setPadding(5, 3, 5, 3);
       tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
       tpd.setCustomTitle(tvTitle);
       return tpd;
   }

   public void onTimeSet(TimePicker view, int hourOfDay, int minute){
          if (listener != null) {
            listener.onTimeSelected( hourOfDay,  minute);
       }
   }
}
