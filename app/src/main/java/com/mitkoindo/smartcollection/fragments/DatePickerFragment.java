package com.mitkoindo.smartcollection.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.mitkoindo.smartcollection.module.berita.BroadcastBeritaActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by W8 on 25/08/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //use current day
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        //set minimum date as today
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

        //return created dialog
        return datePickerDialog;
    }

    //----------------------------------------------------------------------------------------------
    //  Property
    //----------------------------------------------------------------------------------------------
    //Activity yang memanggil datepicker
    private Activity caller;

    //date format
    /*private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());*/
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    //set reference ke activity yang memanggil
    public void SetCallerActivity(Activity caller)
    {
        this.caller = caller;
    }

    //----------------------------------------------------------------------------------------------
    //  Input handler
    //----------------------------------------------------------------------------------------------
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day)
    {
        //parse input ke calendar object
        Calendar calendar = (Calendar)Calendar.getInstance().clone();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        //konversi ke date object
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());

        //dan format jadi string
        String formattedDate = simpleDateFormat.format(date);

        //cek instancenya caller activity
        if (caller instanceof BroadcastBeritaActivity)
        {
            BroadcastBeritaActivity broadcastBeritaActivity = (BroadcastBeritaActivity)caller;
            broadcastBeritaActivity.SetDate(date, formattedDate);
        }
    }
}
