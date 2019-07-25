package org.freelesson.pagingsample.db;

import androidx.lifecycle.LiveData;

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
    public LiveData<List<Repo>> reposByName(String name) {
        System.out.println("Send query: "+name);
        String query = "%".concat(name.replace(' ','%')).concat("%");
        System.out.println("Send query updated: "+query);
        return repoDao.reposByName(query);
    }
}
