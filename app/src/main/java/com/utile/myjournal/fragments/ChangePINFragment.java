package com.utile.myjournal.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.utile.myjournal.R;
import com.utile.myjournal.database.Constants;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePINFragment extends SlideFragment {

    boolean pinententered = false;

    public ChangePINFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_pin, container, false);
        final EditText newPIN = v.findViewById(R.id.makepin);
        final EditText confirmPIN = v.findViewById(R.id.confirmpin);
        Button button = v.findViewById(R.id.okaybutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPin = newPIN.getText().toString();
                String confirmPin = confirmPIN.getText().toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor spe = sharedPreferences.edit();
                if (Objects.equals(newPin, "")) {
                    pinententered = false;
                    Toast.makeText(getActivity(), "Enter a strong PIN!", Toast.LENGTH_SHORT).show();
                } else if (Objects.equals(newPin, confirmPin)) {
                    spe.putString(Constants.USER_KEY, newPin);
                    spe.apply();
                    Toast.makeText(getActivity(), "PIN updated!", Toast.LENGTH_SHORT).show();
                    pinententered = true;
                } else {
                    Toast.makeText(getActivity(), "PINs don't match!", Toast.LENGTH_SHORT).show();
                    pinententered = false;
                }

            }
        });
        return v;
    }

    @Override
    public boolean canGoForward() {
        return pinententered;
    }

    public static ChangePINFragment newInstance() {
        return new ChangePINFragment();
    }
}
