package org.freelesson.pagingsample.db;

import androidx.lifecycle.LiveData;
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
    LiveData<List<Repo>> reposByName( String queryString);

//    @Query("SELECT * from repos  ORDER by stars DESC, name ASC")
//    LiveData<List<Repo>> reposByName( );
}
