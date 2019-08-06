package org.freelesson.pagingsample.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import org.freelesson.pagingsample.model.Repo;

public class ReposAdapter extends PagedListAdapter<Repo, RepoViewHolder> {
    private static DiffUtil.ItemCallback<Repo> REPO_COMPARATOR = new DiffUtil.ItemCallback<Repo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem.fullName.equals(newItem.fullName);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem.equals(newItem);
        }
    };
    public ReposAdapter() {
        super(REPO_COMPARATOR );
    }
    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RepoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repo repoItem = getItem(position);
        if (repoItem!=null)
            holder.bind(repoItem);
    }

}
