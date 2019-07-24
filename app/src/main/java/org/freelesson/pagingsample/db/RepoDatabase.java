package org.freelesson.pagingsample.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.freelesson.pagingsample.model.Repo;

@Database(entities = {Repo.class}, version = 1, exportSchema = false)
public  abstract class RepoDatabase extends RoomDatabase  {
    public abstract RepoDao repoDaos();

    private static RepoDatabase INSTANCE;
    public static RepoDatabase getInstance(Context context) {
        if (INSTANCE==null){
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }
    private static RepoDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),RepoDatabase.class,"Github.db").build();
    }
}
