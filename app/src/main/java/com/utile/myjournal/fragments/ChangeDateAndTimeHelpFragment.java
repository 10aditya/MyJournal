package com.utile.myjournal.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utile.myjournal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeDateAndTimeHelpFragment extends Fragment {


    public ChangeDateAndTimeHelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_date_and_time_help, container, false);
    }

}
