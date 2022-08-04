package org.freelesson.pagingsample.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.freelesson.pagingsample.api.GithubService
import org.freelesson.pagingsample.db.RepoDatabase
import org.freelesson.pagingsample.model.Repo

class GithubRepository(
    private val service: GithubService,
    private val database: RepoDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { database.repoDao().reposByName(dbQuery) }
        return Pager(
            config = PagingConfig(
                pageSize = DATABASE_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = GithubRemoteMediator(
                query,
                service,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}

private const val DATABASE_PAGE_SIZE = 20

internal interface RepositoryCallback<T> {
    fun onSuccess(repos: List<Repo?>?)
    fun onError(errorMessage: String)
}