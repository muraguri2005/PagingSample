package org.freelesson.pagingsample.db;

import androidx.paging.DataSource;

import org.freelesson.pagingsample.model.Repo;

import java.util.List;
import java.util.concurrent.Executor;

public class GithubLocalCache {
    private final RepoDao repoDao;
    private final Executor ioExecutor;
    public  GithubLocalCache(RepoDao repoDao, Executor ioExecutor) {
        this.repoDao = repoDao;
        this.ioExecutor = ioExecutor;
    }
    public void insert(List<Repo> list, InsertCallback insertCallback) {
        ioExecutor.execute(() -> {
            repoDao.insert(list);
            insertCallback.insertFinished();
        });
    }
    public DataSource.Factory<Integer,Repo> reposByName(String name) {
        String query = "%".concat(name.replace(' ','%')).concat("%");
        return repoDao.reposByName(query);
    }
}
