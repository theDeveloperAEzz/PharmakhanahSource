package com.pharmakhanah.hp.pharmakhanahsource.model.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.pharmakhanah.hp.pharmakhanahsource.R;

import java.util.ArrayList;


public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ViewHolder> {
    Context context;
    ArrayList objects;

//
    public ProfilePostsAdapter(ArrayList objects) {
        this.objects = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_profile_post_item, parent, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button btnShare;
        TextView textViewAuthorName, textViewStatus, textViewSupName, textViewContent, textViewNumberOfShare;

        ViewHolder(View v) {
            super(v);

        }
    }

}
