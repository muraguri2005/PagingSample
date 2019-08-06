package org.freelesson.pagingsample.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import org.freelesson.pagingsample.model.Repo;

import java.util.List;
import java.util.concurrent.Executor;

public class GithubLocalCache {
    RepoDao repoDao;
    Executor ioExecutor;
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
        String query = name.replace(' ','%');
        return repoDao.reposByName(query);
    }
}
