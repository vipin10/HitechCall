package com.android.hitech.calls.meeting;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements OnDateSetListener {
    SelectedDateListener listener;

    public interface SelectedDateListener {
        void onDateSelected(int yy, int mm, int dd);
    }

    public void setListener(SelectedDateListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog picker = new DatePickerDialog(getActivity(), android.support.design.R.style.Base_Theme_AppCompat_Light_Dialog, this, yy, mm, dd);
        picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return picker;
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        if (listener != null)
            listener.onDateSelected(yy, mm, dd);

    }

}
