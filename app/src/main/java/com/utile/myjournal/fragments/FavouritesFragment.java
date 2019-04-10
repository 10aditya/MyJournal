package com.utile.myjournal.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utile.myjournal.R;
import com.utile.myjournal.activities.MainActivity;
import com.utile.myjournal.adapters.RVadapter;
import com.utile.myjournal.database.Entries;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {


    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycler_favourite_entries);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        ArrayList<Entries> e = MainActivity.get_Fav_Entries(getActivity());
        if (e.size() != 0) {
            RVadapter rVadapter = new RVadapter(e, getActivity(), 1);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(rVadapter);
        } else {
            (v.findViewById(R.id.make_favourite)).setVisibility(View.VISIBLE);
        }
        return v;
    }

}
