package org.freelesson.pagingsample.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import org.freelesson.pagingsample.api.GithubService
import org.freelesson.pagingsample.api.IN_QUALIFIER
import org.freelesson.pagingsample.db.RepoDatabase
import org.freelesson.pagingsample.model.RemoteKeys
import org.freelesson.pagingsample.model.Repo

private const val GITHUB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val query: String,
    private val service: GithubService,
    private val repoDatabase: RepoDatabase
) :
    RemoteMediator<Int, Repo>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosetToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }
        val apiQuery = query + IN_QUALIFIER
        val apiResponse = service.searchRepos(apiQuery, page, state.config.pageSize)
        val repos = apiResponse.items
        val endOfPaginationReached = repos.isEmpty()
        repoDatabase.withTransaction {
            if (loadType == LoadType.REFRESH) {
                repoDatabase.remoteKeysDao().clearRemoteKeys()
                repoDatabase.repoDao().clearRepos()
            }
            val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = repos.map {
                RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
            }
            repoDatabase.remoteKeysDao().insertAll(keys)
            repoDatabase.repoDao().insertAll(repos)
        }

        return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Repo>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { repo ->
            repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Repo>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosetToCurrentPosition(state: PagingState<Int, Repo>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                repoDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}