package com.example.covid_19.AlertDialouge;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private DatePickerDialog.OnDateSetListener m_listener = null;
//    DatePickerDialog.OnDateSetListener ondateSet;
//    private int year, month, day;
//    public DatePickerFragment() {}
//
//    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
//        ondateSet = ondate;
//    }
//    @SuppressLint("NewApi")
//    @Override
//    public void setArguments(Bundle args) {
//        super.setArguments(args);
//        year = args.getInt("year");
//        month = args.getInt("month");
//        day = args.getInt("day");
//    }



    @NonNull
    @Override
    public DatePickerDialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    public void setListeningActivity(DatePickerDialog.OnDateSetListener listener)
    {
        m_listener = listener;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (m_listener != null) {
            m_listener.onDateSet(view, year, month, dayOfMonth);
        }
    }
}