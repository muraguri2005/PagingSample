package org.freelesson.pagingsample.ui;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.freelesson.pagingsample.R;
import org.freelesson.pagingsample.model.Repo;

public class RepoViewHolder extends RecyclerView.ViewHolder {
    private TextView name ;
    private TextView description ;
    private TextView stars ;
    private TextView language ;
    private TextView forks ;

    protected RepoViewHolder(View view) {
        super(view);
       name = view.findViewById(R.id.repo_name);
        description = view.findViewById(R.id.repo_description);
        stars = view.findViewById(R.id.repo_stars);
        language = view.findViewById(R.id.repo_language);
        forks= view.findViewById(R.id.repo_forks);
    }
    public static RepoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_view_item, parent,false);
        return new RepoViewHolder(view);
    }


    public void bind(Repo repo) {
        if (repo==null) {
            Resources resources = itemView.getResources();
            name.setText(resources.getString(R.string.loading));
            description.setVisibility(View.GONE);
            language.setVisibility(View.GONE);
            stars.setText(resources.getText(R.string.unknown));
            forks.setText(resources.getText(R.string.unknown));
        } else {
            showRepoData(repo);
        }
    }
    private void showRepoData(Repo repo) {
        name.setText(repo.fullName);
        int descriptionVisibility = View.GONE;
        if (repo.description!=null && !repo.description.isEmpty()) {
            description.setText(repo.description);
            descriptionVisibility = View.VISIBLE;
        }
        description.setVisibility(descriptionVisibility);
        stars.setText(String.valueOf(repo.stars));
        forks.setText(String.valueOf(repo.forks));

        int languageVisibility = View.GONE;
        if (repo.language != null && !repo.language.isEmpty()) {
            Resources resources = this.itemView.getContext().getResources();
            language.setText(resources.getString(R.string.language,repo.language));
            languageVisibility =  View.VISIBLE;
        }
        language.setVisibility(languageVisibility);

    }
}
