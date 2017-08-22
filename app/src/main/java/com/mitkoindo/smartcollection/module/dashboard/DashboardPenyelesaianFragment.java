package com.mitkoindo.smartcollection.module.dashboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mitkoindo.smartcollection.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardPenyelesaianFragment extends Fragment
{
    public DashboardPenyelesaianFragment()
    {
        // Required empty public constructor
    }

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //spinner
    private Spinner view_Spinner;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_dashboard_penyelesaian, container, false);
        GetViews(thisView);
        SetupViews();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        //get spinner
        view_Spinner = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Spinner);
    }

    //setup views
    private void SetupViews()
    {
        //set spinner value
        String[] spinnerValue = new String[]
                {
                        getString(R.string.Text_HariIni),
                        getString(R.string.Text_Akumulasi)
                };
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.preset_spinneritem, spinnerValue);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        view_Spinner.setAdapter(spinnerAdapter);
        view_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                //set status ke selected status
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }
}
