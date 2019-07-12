package org.freelesson.pagingsample;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

public interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Repo> repos);

    @Query("SELECT * from repos where (name LIKE :queryString) or (description LIKE :queryString) ORDER by stars DESC, name ASC")
    LiveData<List<Repo>> reposByName( String queryString);
}
