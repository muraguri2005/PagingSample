package org.freelesson.pagingsample.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.freelesson.pagingsample.data.GithubRepository
import org.freelesson.pagingsample.model.Repo

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoriesViewModel(
    private val repository: GithubRepository,
    handle: SavedStateHandle
) : ViewModel() {
    val pagingDataFlow: Flow<PagingData<UiModel>>
    val state: StateFlow<UiState>
    val accept: (UiAction) -> Unit

    init {
        val initialQuery = handle[LAST_SEARCH_QUERY] ?: DEFAULT_QUERY
        val lastQueryScrolled = handle[LAST_QUERY_SCROLLED] ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow.filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }
        val queriesScrolled =
            actionStateFlow.filterIsInstance<UiAction.Scroll>().distinctUntilChanged()
                .shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                    replay = 1
                )
                .onStart { emit(UiAction.Scroll(currentQuery = lastQueryScrolled)) }
        pagingDataFlow = searches.flatMapLatest { searchRepo(query = it.query) }
            .cachedIn(viewModelScope)

        state = combine(searches, queriesScrolled, ::Pair).map { (search,scroll) ->
            UiState(query= search.query, lastQueryScrolled=scroll.currentQuery, hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery)
        }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), initialValue = UiState())
        accept = {action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }

    }

    private fun searchRepo(query: String): Flow<PagingData<UiModel>> =
        repository.getSearchResultStream(query)
            .map { pagingData -> pagingData.map { UiModel.RepoItem(it) } }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null) {
                        return@insertSeparators null
                    }
                    if (before == null) {
                        return@insertSeparators UiModel.SeparatorItem("${after.roundedStarCount}0.000+ stars")
                    }
                    if (before.roundedStarCount > after.roundedStarCount) {
                        if (after.roundedStarCount >= 1) {
                            UiModel.SeparatorItem("${after.roundedStarCount}0.000+stars")
                        } else {
                            UiModel.SeparatorItem(" < 10.000+stars")
                        }
                    } else {
                        null
                    }

                }

            }
}
sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}
data class UiState(val query: String= DEFAULT_QUERY,val lastQueryScrolled: String= DEFAULT_QUERY, val hasNotScrolledForCurrentSearch: Boolean=false)

sealed class UiModel {
    data class RepoItem(val repo: Repo) : UiModel()
    data class SeparatorItem(val descriptionString: String) : UiModel()
}

private val UiModel.RepoItem.roundedStarCount: Int
    get() = this.repo.stars / 10_000

private const val LAST_SEARCH_QUERY = "last_search_query"
private const val LAST_QUERY_SCROLLED = "last_query_scrolled"
private const val DEFAULT_QUERY = "Android"
