package com.utile.myjournal.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.utile.myjournal.R;
import com.utile.myjournal.activities.ImageViewerActivity;
import com.utile.myjournal.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by adity on 05/01/2017.
 */

public class images_adapter extends RecyclerView.Adapter<images_adapter.ViewHolder> {

    private ArrayList<String> imagespath;
    private Context context;


    public images_adapter(Context context, ArrayList<String> imagespath) {
        this.context = context;
        this.imagespath = imagespath;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String currentimage = imagespath.get(position);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(currentimage));
    }

    @Override
    public int getItemCount() {
        return imagespath.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        LinearLayout layout;

        ViewHolder(final View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageviewforrecycler);
            this.layout = itemView.findViewById(R.id.imagesCardView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            String clickedItem = imagespath.get(getAdapterPosition());
            int idofEntry = MainActivity.getEntryIdfromImagePath(context, clickedItem);
            Intent i = new Intent(context, ImageViewerActivity.class);
            i.putExtra("image_uri", clickedItem);
            i.putExtra("entry_Id", idofEntry);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(context,
                    android.R.anim.fade_in, android.R.anim.fade_out);
            context.startActivity(i, options.toBundle());
            Log.d("ListAdapter", getAdapterPosition() + " ");
        }

    }
}