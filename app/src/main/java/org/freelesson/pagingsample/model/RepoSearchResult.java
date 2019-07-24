package org.freelesson.pagingsample.model;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RepoSearchResult {
    public LiveData<List<Repo>> data;
    public LiveData<String> networkErrors;
    public RepoSearchResult(LiveData<List<Repo>> data,LiveData<String> networkErrors) {
        this.data = data;
        this.networkErrors = networkErrors;
    }
}
