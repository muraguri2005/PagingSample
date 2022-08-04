package org.freelesson.pagingsample.ui;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagingData;

import org.freelesson.pagingsample.data.GithubRepository;
import org.freelesson.pagingsample.model.Repo;
import org.freelesson.pagingsample.model.RepoSearchResult;


class SearchRepositoriesViewModel extends ViewModel {
    private static final int VISIBLE_THRESHOLD = 5;

    private GithubRepository repository;
    SearchRepositoriesViewModel(GithubRepository repository) {
        this.repository = repository;
    }
    MutableLiveData<String> queryLiveData = new MutableLiveData<>();
    LiveData<RepoSearchResult> repoResult = Transformations.map(queryLiveData, it -> repository.search(it));

    LiveData<PagingData<Repo>> repos = Transformations.switchMap(repoResult, it-> it.data);
    LiveData<String> networkErrors = Transformations.switchMap(repoResult, it -> it.networkErrors);

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

    String lastQueryValue() {
        return queryLiveData.getValue();
    }

}
