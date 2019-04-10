package com.utile.myjournal.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.TextView;

import com.utile.myjournal.R;
import com.utile.myjournal.activities.Pop;
import com.utile.myjournal.activities.enterEntryActivity;

import java.util.ArrayList;

/**
 * Created by Aditya on 05/09/2016.
 */
public class adapterForMood extends RecyclerView.Adapter<adapterForMood.ViewHolder> {

    private final ArrayList<Object> moods;
    private Context context;

    public adapterForMood(Context context) {
        this.context = context;
        moods = new ArrayList<>();
        moods.add(0, "HAPPY");
        moods.add(1, "SAD");
        moods.add(2, "ANGRY");
        moods.add(3, "ROMANTIC");
        moods.add(4, "BLISSFUL");
        moods.add(5, "EXCITED");
        moods.add(6, "SPECIAL");
        moods.add(7, "ASHAMED");
        moods.add(8, "STRESSED");
        moods.add(9, "BORED");
        moods.add(10, "CALM");
        moods.add(11, "FRUSTRATED");
        moods.add(12, "SATISFIED");
        moods.add(13, "CONFUSED");
        moods.add(14, "ANNOYED");
        moods.add(15, "ALONE");
        moods.add(16, "CURIOUS");
        moods.add(17, "HYPER");
        moods.add(18, "SHOCKED");
        moods.add(19, "APOLOGETIC");
        moods.add(20, "GUILTY");
        moods.add(21, "CRAZY");
        moods.add(22, "EXHAUSTED");
        moods.add(23, "PISSED OFF");
        moods.add(24, "PLEASED");
        moods.add(25, "CHILLING");
        moods.add(26, "PARTYING");
        moods.add(27, "SICK");
        moods.add(28, "WORK OUT");
        moods.add(29, "TREKKING");
    }


    @Override
    public adapterForMood.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_getter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(adapterForMood.ViewHolder holder, int position) {
        holder.textView.setText(moods.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return moods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.moodButton);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            enterEntryActivity.xx.setText(moods.get(getAdapterPosition()).toString());
            ((Activity) context).finish();
        }
    }
}
