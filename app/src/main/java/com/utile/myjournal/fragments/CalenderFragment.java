package com.utile.myjournal.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.utile.myjournal.R;
import com.utile.myjournal.activities.MainActivity;
import com.utile.myjournal.adapters.RVadapter;
import com.utile.myjournal.database.MyDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalenderFragment extends Fragment {

    String currentDate;
    CalendarView myCalender;
    RecyclerView recyclerView;
    private LinearLayoutManager llm;
    String Date;
    private String dateForIntent;

    public CalenderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);


        myCalender = view.findViewById(R.id.calendarView);
        currentDate = String.valueOf(myCalender.getDate());
        recyclerView = view.findViewById(R.id.recycler_entries_inCV);

        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        final String curDate = sdf.format(date.getTime());
        sdf = new SimpleDateFormat("dd:MM:yyyy", Locale.US);

        recyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        if (new MyDB(getActivity()).getEntriesCount() != 0) {
            RVadapter rVadapter = new RVadapter(MainActivity.get_Entries_of_date(getActivity(), curDate), getActivity(), 1);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(rVadapter);
        }

        myCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                Date = String.format("%04d%02d%02d", i, i1 + 1, i2);
                dateForIntent = String.format("%02d:%02d:%04d", i2, i1 + 1, i);
                if (new MyDB(getActivity()).getEntriesCount() != 0) showEntries(Date);

            }
        });

        return view;
    }


    private void showEntries(String date) {
        RVadapter rVadapter = new RVadapter(MainActivity.get_Entries_of_date(getActivity(), date), getActivity(), 1);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(rVadapter);

    }


}
