package com.utile.myjournal.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utile.myjournal.R;
import com.utile.myjournal.activities.MainActivity;
import com.utile.myjournal.adapters.RVadapter;
import com.utile.myjournal.database.MyDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntryFragment extends Fragment {


    public EntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_entry, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycler_entries);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new GridLayoutManager(getContext(), 2);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        if (new MyDB(getContext()).getEntriesCount() != 0) {
            v.findViewById(R.id.clickToAddEntryTextView).setVisibility(View.GONE);
            RVadapter rVadapter = new RVadapter(MainActivity.get_Entries(getContext()), getContext(), 1);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(rVadapter);

        }
        return v;
    }

}
