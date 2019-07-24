package org.freelesson.pagingsample.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.freelesson.pagingsample.db.GithubLocalCache;
import org.freelesson.pagingsample.api.GithubService;
import org.freelesson.pagingsample.db.InsertCallback;
import org.freelesson.pagingsample.model.Repo;
import org.freelesson.pagingsample.model.RepoSearchResult;
import org.freelesson.pagingsample.api.RepoSearchResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GithubRepository {

    private static final String IN_QUALIFIER = "in:name,description";

    private GithubService service;
    private GithubLocalCache cache;

    public GithubRepository(GithubService service, GithubLocalCache cache) {
        this.service = service;
        this.cache = cache;
    }

    private MutableLiveData<String> networkErrors= new MutableLiveData<>();


    public RepoSearchResult search(String query) {
        lastRequestedPage = 1;
        requestAndSaveData(query);
        LiveData<List<Repo>> data = cache.reposByName(query);
        return new RepoSearchResult(data, networkErrors);
    }
    private boolean isRequestInProgress = false;
    public void requestMore(String query) {
        requestAndSaveData(query);
    }
    private void requestAndSaveData( String query) {
        if (isRequestInProgress)
            return;
        isRequestInProgress = true;
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, new RepositoryCallback() {
            @Override
            public void onSuccess(List list) {
                cache.insert(list, () -> {
                    lastRequestedPage++;
                    isRequestInProgress = false;
                });
            }

            @Override
            public void onError(String errorMessage) {
               networkErrors.postValue(errorMessage);
               isRequestInProgress = false;
            }
        });
    }
    private static final int NETWORK_PAGE_SIZE = 50;
    private int lastRequestedPage = 1;
    private void searchRepos(GithubService service, String query, int page, int itemsPerPage,final RepositoryCallback repositoryCallback)  {
        String apiQuery = query;
//                .concat(IN_QUALIFIER);
        service.searchRepos(apiQuery,page,itemsPerPage).enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(Call<RepoSearchResponse> call, Response<RepoSearchResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body()!=null && !response.body().items.isEmpty());
                    if (response.body()!=null && !response.body().items.isEmpty())
                        repositoryCallback.onSuccess(response.body().items);
                    else
                        repositoryCallback.onSuccess(Collections.emptyList());
                } else {
                    try {
                        if (response.errorBody() != null && !response.errorBody().string().isEmpty())
                            repositoryCallback.onError(response.errorBody().string());
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                    repositoryCallback.onError("unknown error");
                }
            }

            @Override
            public void onFailure(Call<RepoSearchResponse> call, Throwable t) {
               repositoryCallback.onError(t.getMessage()!=null && !t.getMessage().isEmpty() ? t.getMessage() : "unknown error");
            }
        });

    }

}

interface RepositoryCallback<Repo> {
    void onSuccess(List<Repo> repos);
    void onError(String errorMessage);
}
