package org.freelesson.pagingsample;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executors;

public class Injection {
    private  static GithubLocalCache provideCache(Context context) {
        RepoDatabase database = RepoDatabase.getInstance(context);
        return new GithubLocalCache(database.repoDaos(), Executors.newSingleThreadExecutor());
    }
    private static GithubRepository provideGithubRepository(Context context) {
        return new GithubRepository(GithubService.create(),provideCache(context));
    }
    public static ViewModelProvider.Factory provideViewModelFactory(Context context) {
        return new ViewModelFactory(provideGithubRepository(context));
    }
}
