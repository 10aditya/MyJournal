package com.utile.myjournal.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.utile.myjournal.R;
import com.utile.myjournal.adapters.adapterForMood;


/**
 * Created by Aditya on 16/06/2016.
 */
public class Pop extends Activity {

    public static int width;
    static public int height;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.pop_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;
        height = dm.heightPixels;

        RecyclerView recyclerView = findViewById(R.id.back_of_moodCard);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new GridLayoutManager(getApplicationContext(), 3);
        adapterForMood adapter = new adapterForMood(Pop.this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        getWindow().setLayout((int) (width * (.9)), (int) (height * (.4)));


    }

    public void finishPop(){
        this.finish();
    }
}
