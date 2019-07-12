package org.freelesson.pagingsample;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class GithubLocalCache {
    @Inject RepoDao repoDao;
    @Inject
    Executor ioExecutor;
}
