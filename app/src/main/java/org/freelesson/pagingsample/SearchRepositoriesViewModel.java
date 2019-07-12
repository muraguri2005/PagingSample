package org.freelesson.pagingsample;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SearchRepositoriesViewModel extends ViewModel {
    GithubRepository repository;
    public SearchRepositoriesViewModel(GithubRepository repository) {

    }
    MutableLiveData<String> queryLiveData = new MutableLiveData<>();
    LiveData<RepoSearchResult> repoResult = Transformations.map(queryLiveData, it -> {
         return repository.search(this);
    });

    LiveData<List<Repo>> repos = Transformations.switchMap(repoResult, it-> {
        return it.data;
    });
}
