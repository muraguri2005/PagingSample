package org.freelesson.pagingsample.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import org.freelesson.pagingsample.data.GithubRepository

class ViewModelFactory(owner: SavedStateRegistryOwner, private val githubRepository: GithubRepository) : AbstractSavedStateViewModelFactory(owner, null) {
     override fun <T : ViewModel> create(key: String,modelClass: Class<T>, handle: SavedStateHandle): T {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel::class.java)) {
            return SearchRepositoriesViewModel(githubRepository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}