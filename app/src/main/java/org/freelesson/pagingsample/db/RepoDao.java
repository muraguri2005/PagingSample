package org.freelesson.pagingsample.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.freelesson.pagingsample.model.Repo;

import java.util.List;

@Dao
public interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Repo> repos);

    @Query("SELECT * from repos where (name LIKE :queryString) or (description LIKE :queryString) ORDER by stars DESC, name ASC")
    DataSource.Factory<Integer,Repo> reposByName(String queryString);
}
