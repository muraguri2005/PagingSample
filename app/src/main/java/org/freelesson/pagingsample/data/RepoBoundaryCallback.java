package org.freelesson.pagingsample.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import org.freelesson.pagingsample.api.GithubService;
import org.freelesson.pagingsample.api.RepoSearchResponse;
import org.freelesson.pagingsample.db.GithubLocalCache;
import org.freelesson.pagingsample.model.Repo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoBoundaryCallback extends PagedList.BoundaryCallback<Repo> {
    private final String query;
    private final GithubService service;
    private final GithubLocalCache cache;
    private boolean isRequestInProgress = false;
    private int lastRequestedPage = 1;
    MutableLiveData<String> networkErrors= new MutableLiveData<>();
    private static final int NETWORK_PAGE_SIZE = 50;
    private static final String IN_QUALIFIER = "in:name,description";
    RepoBoundaryCallback(String query, GithubService service, GithubLocalCache cache) {
        this.query = query;
        this.service = service;
        this.cache = cache;
    }

    @Override
    public void onZeroItemsLoaded() {
        requestAndSaveData(query);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull Repo itemAtEnd) {

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
    private void searchRepos(GithubService service, String query, int page, int itemsPerPage,final RepositoryCallback<List<Repo>> repositoryCallback)  {
        String apiQuery = query.concat(IN_QUALIFIER);
        service.searchRepos(apiQuery,page,itemsPerPage).enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(@NotNull Call<RepoSearchResponse> call, @NotNull Response<RepoSearchResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body()!=null && !response.body().items.isEmpty());
                    if (response.body()!=null && !response.body().items.isEmpty())
                        repositoryCallback.onSuccess( response.body().items);
                    else
                        repositoryCallback.onSuccess( Collections.emptyList());
                } else {
                    try {
                        if (response.errorBody() != null && !Objects.requireNonNull(response.errorBody()).string().isEmpty())
                            repositoryCallback.onError(Objects.requireNonNull(response.errorBody()).string());
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                    repositoryCallback.onError("unknown error");
                }
            }

            @Override
            public void onFailure(@NotNull Call<RepoSearchResponse> call, @NotNull Throwable t) {
                repositoryCallback.onError(t.getMessage()!=null && !t.getMessage().isEmpty() ? t.getMessage() : "unknown error");
            }
        });

    }
}
