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
import com.utile.myjournal.database.notesDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {


    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycler_notes);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new GridLayoutManager(getContext(), 2);
        if (new notesDB(getContext()).getNotesCount() != 0) {
            v.findViewById(R.id.clickToAddNoteTextView).setVisibility(View.GONE);
            RVadapter rVadapter = new RVadapter(getContext(), MainActivity.getNotes(getContext()), 0);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(rVadapter);
        }
        return v;
    }

}
