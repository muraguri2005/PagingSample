package org.freelesson.pagingsample.model;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;

public class RepoSearchResult {
    public LiveData<PagingData<Repo>> data;
    public LiveData<String> networkErrors;
    public RepoSearchResult(LiveData<PagingData<Repo>> data, LiveData<String> networkErrors) {
        this.data = data;
        this.networkErrors = networkErrors;
    }
}
