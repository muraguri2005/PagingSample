package org.freelesson.pagingsample.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.freelesson.pagingsample.data.GithubRepository;
import org.freelesson.pagingsample.ui.SearchRepositoriesViewModel;

public class ViewModelFactory  implements ViewModelProvider.Factory {
    private GithubRepository githubRepository;
    public ViewModelFactory(GithubRepository githubRepository) {
        this.githubRepository = githubRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel.class)) {
            return (T) new SearchRepositoriesViewModel(githubRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
