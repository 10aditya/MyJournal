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
import com.utile.myjournal.database.Notes;
import com.utile.myjournal.database.notesDB;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImpNotesFragment extends Fragment {


    public ImpNotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycler_notes);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new GridLayoutManager(getContext(), 2);
        ArrayList<Notes> imp = MainActivity.getImpNotes(getContext());
        if (imp.size() != 0) {
            v.findViewById(R.id.clickToAddNoteTextView).setVisibility(View.GONE);
            RVadapter rVadapter = new RVadapter(getContext(), imp, 0);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(rVadapter);
        }
        return v;
    }

}
