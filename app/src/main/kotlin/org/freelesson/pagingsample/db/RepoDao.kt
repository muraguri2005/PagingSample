package org.freelesson.pagingsample.db

import androidx.paging.PagingSource
import androidx.room.*
import org.freelesson.pagingsample.model.Repo

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<Repo>)

    @Query("SELECT * from repos where (name LIKE :queryString) or (description LIKE :queryString) ORDER by stars DESC, name ASC")
    fun reposByName(queryString: String?): PagingSource<Int, Repo>
    @Query("DELETE from repos")
    suspend fun clearRepos()
}