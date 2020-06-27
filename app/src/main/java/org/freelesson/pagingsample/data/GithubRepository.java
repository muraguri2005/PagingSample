package org.freelesson.pagingsample.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.freelesson.pagingsample.api.GithubService;
import org.freelesson.pagingsample.db.GithubLocalCache;
import org.freelesson.pagingsample.model.Repo;
import org.freelesson.pagingsample.model.RepoSearchResult;

import java.util.List;

public class GithubRepository {



    private static final int DATABASE_PAGE_SIZE = 20;

    private GithubService service;
    private GithubLocalCache cache;

    public GithubRepository(GithubService service, GithubLocalCache cache) {
        this.service = service;
        this.cache = cache;
    }




    public RepoSearchResult search(String query) {
        Log.d("GithubRepository","New query: "+query);
        DataSource.Factory dataSourceFactory = cache.reposByName(query);
        RepoBoundaryCallback boundaryCallback = new RepoBoundaryCallback(query,service,cache);
        MutableLiveData<String> networkErrors = boundaryCallback.networkErrors;

        LiveData<PagedList<Repo>> data = new LivePagedListBuilder(dataSourceFactory,DATABASE_PAGE_SIZE).setBoundaryCallback(boundaryCallback).build();
        return new RepoSearchResult(data, networkErrors);
    }

}

interface RepositoryCallback<T> {
    void onSuccess(List<Repo> repos);
    void onError(String errorMessage);
}

