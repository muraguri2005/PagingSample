package org.freelesson.pagingsample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubService {
    @GET("search/respositories?sort=stars")
    Call<RepoSearchResponse> searchRepos(@Query("q") String query, @Query("page") Integer page, @Query("per_page") Integer itemsPerPage);
}
