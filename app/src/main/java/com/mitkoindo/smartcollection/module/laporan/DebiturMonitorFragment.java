package com.mitkoindo.smartcollection.module.laporan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mitkoindo.smartcollection.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebiturMonitorFragment extends Fragment {


    public DebiturMonitorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_debitur_monitor, container, false);
    }

}
