package com.mitkoindo.smartcollection.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import com.mitkoindo.smartcollection.module.assignment.AssignedDebiturFragment;
import com.mitkoindo.smartcollection.module.assignment.UnassignedDebiturFragment;
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

        //cek maximum date
        if (maxDate > -1)
            datePickerDialog.getDatePicker().setMaxDate(maxDate);

        //return created dialog
        return datePickerDialog;
    }

    //max date
    private long maxDate = -1;

    //set maxdate
    public void SetMaxDate(int days)
    {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar calendarClone = (Calendar)currentCalendar.clone();
        calendarClone.add(Calendar.DATE, days);

        //cek apakah setelah ditambah hari, bulan di kalender masih sama dengan bulan sekarang
        if (calendarClone.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH))
        {
            //jika masih sama, set max date sebagai tanggal baru
            maxDate = calendarClone.getTimeInMillis();
        }
        else
        {
            //jika beda, set maxdate sebagai hari terakhir di bulan berjalan
            calendarClone.set(Calendar.DATE, currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendarClone.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            maxDate = calendarClone.getTimeInMillis();
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Property
    //----------------------------------------------------------------------------------------------
    //Activity yang memanggil datepicker
    private Activity caller;

    //Fragment yang memanggil datepicker
    private Fragment callerFragment;

    //date format
    /*private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());*/
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    //set reference ke activity yang memanggil
    public void SetCallerActivity(Activity caller)
    {
        this.caller = caller;
    }

    //set reference ke fragment yang memanggil
    public void SetCallerFragment(Fragment callerFragment)
    {
        this.callerFragment = callerFragment;
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

        if (caller != null)
        {
            PassInfoToCaller(date, formattedDate);
        }
        else if (callerFragment != null)
        {
            PassInfoToFragment(date, formattedDate);
        }
    }

    //pass info ke caller
    private void PassInfoToCaller(Date date, String formattedDate)
    {
        //cek instancenya caller activity
        if (caller instanceof BroadcastBeritaActivity)
        {
            BroadcastBeritaActivity broadcastBeritaActivity = (BroadcastBeritaActivity)caller;
            broadcastBeritaActivity.SetDate(date, formattedDate);
        }
    }

    //pass info ke fragment
    private void PassInfoToFragment(Date date, String formattedDate)
    {
        //cek instance unassigned debitur fragment
        if (callerFragment instanceof UnassignedDebiturFragment)
        {
            UnassignedDebiturFragment unassignedDebiturFragment = (UnassignedDebiturFragment)callerFragment;
            unassignedDebiturFragment.SetDate(date, formattedDate);
        }
        else if (callerFragment instanceof AssignedDebiturFragment)
        {
            AssignedDebiturFragment assignedDebiturFragment = (AssignedDebiturFragment)callerFragment;
            assignedDebiturFragment.SetDate(date, formattedDate);
        }
    }
}
