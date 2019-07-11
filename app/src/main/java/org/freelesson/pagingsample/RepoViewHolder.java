package org.freelesson.pagingsample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class RepoViewHolder extends RecyclerView.ViewHolder {
    public RepoViewHolder(View view) {
        super(view);
    }
    public static RepoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_view_item, parent,false);
        return new RepoViewHolder(view);
    }
}
