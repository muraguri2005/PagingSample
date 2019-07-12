package org.freelesson.pagingsample;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

public class GithubRepository {

    @Inject GithubService service;
    @Inject GithubLocalCache cache;
    public GithubRepository() {
    }
    public RepoSearchResult search(String query) {
        lastRequestedPage = 1;
        requestAndSaveData(query);
        LiveData<List<Repo>> data = cache.reposByName(query);
        return RepoSearchResult(data, networkErrors);
    }
    boolean isRequestInProgress = false;
    private void requestAndSaveData( String query) {
        if (isRequestInProgress)
            return;
        isRequestInProgress = true;
        searchRepos(service,query,lastRequestedPage,NETWORK_PAGE_SIZE)
    }
    private static final int NETWORK_PAGE_SIZE = 50;
    int lastRequestedPage = 1;

}
