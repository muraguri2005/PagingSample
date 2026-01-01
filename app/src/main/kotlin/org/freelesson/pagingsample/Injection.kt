package org.freelesson.pagingsample

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import org.freelesson.pagingsample.api.GithubService
import org.freelesson.pagingsample.data.GithubRepository
import org.freelesson.pagingsample.db.RepoDatabase
import org.freelesson.pagingsample.ui.ViewModelFactory

object Injection {
    private fun provideGithubRepository(context: Context): GithubRepository {
        return GithubRepository(GithubService.create(), RepoDatabase.getInstance(context))
    }

    fun provideViewModelFactory(
        context: Context,
        owner: SavedStateRegistryOwner
    ): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideGithubRepository(context))
    }
}