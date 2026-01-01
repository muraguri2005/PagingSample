package org.freelesson.pagingsample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.freelesson.pagingsample.model.RemoteKeys
import org.freelesson.pagingsample.model.Repo

@Database(entities = [Repo::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: RepoDatabase? = null
        fun getInstance(context: Context): RepoDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context): RepoDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                RepoDatabase::class.java,
                "Github.db"
            ).build()
        }
    }
}