package org.freelesson.pagingsample.model;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import java.util.List;

public class RepoSearchResult {
    public LiveData<PagedList<Repo>> data;
    public LiveData<String> networkErrors;
    public RepoSearchResult(LiveData<PagedList<Repo>> data, LiveData<String> networkErrors) {
        this.data = data;
        this.networkErrors = networkErrors;
    }
}
