package org.freelesson.pagingsample;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

public class GithubLocalCache {
    RepoDao repoDao;
    Executor ioExecutor;
    public  GithubLocalCache(RepoDao repoDao, Executor ioExecutor) {
        this.repoDao = repoDao;
        this.ioExecutor = ioExecutor;
    }

    public LiveData<List<Repo>> reposByName(String name) {
        String query = name.replace(' ','%');
        return repoDao.reposByName(query);
    }
}
