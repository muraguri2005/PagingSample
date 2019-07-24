package org.freelesson.pagingsample.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubService {
    @GET("search/repositories?sort=stars")
    Call<RepoSearchResponse> searchRepos(@Query("q") String query, @Query("page") Integer page, @Query("per_page") Integer itemsPerPage);

     String BASE_URL = "https://api.github.com/";

     static GithubService create() {
         HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
         logger.setLevel(HttpLoggingInterceptor.Level.BASIC);
         OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logger).build();
         return new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(GithubService.class);
     }
}
