package org.freelesson.pagingsample.ui;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import org.freelesson.pagingsample.data.GithubRepository;
import org.freelesson.pagingsample.model.Repo;
import org.freelesson.pagingsample.model.RepoSearchResult;

import java.util.List;

public class SearchRepositoriesViewModel extends ViewModel {
    private static final int VISIBLE_THRESHOLD = 5;

    private GithubRepository repository;
    public SearchRepositoriesViewModel(GithubRepository repository) {
        this.repository = repository;
    }
    MutableLiveData<String> queryLiveData = new MutableLiveData<>();
    LiveData<RepoSearchResult> repoResult = Transformations.map(queryLiveData, it -> {
         return repository.search(it);
    });

    LiveData<PagedList<Repo>> repos = Transformations.switchMap(repoResult, it-> {
        return it.data;
    });
    LiveData<String> networkErrors = Transformations.switchMap(repoResult, it -> {
        return it.networkErrors;
    });

    public void searchRepo(String query) {
        queryLiveData.postValue(query);
    }

    public void listScrolled(int visibleItemCount, int lastVisibleItemPosition, int totalItemCount) {
        if (visibleItemCount+lastVisibleItemPosition+VISIBLE_THRESHOLD >= totalItemCount) {
            String immutableQuery = lastQueryValue();
            if (immutableQuery!=null) {
                repository.search(immutableQuery);
            }
        }
    }

    private String lastQueryValue() {
        return queryLiveData.getValue();
    }

}
