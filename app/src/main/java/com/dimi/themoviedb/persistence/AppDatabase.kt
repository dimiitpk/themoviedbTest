package com.dimi.themoviedb.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dimi.themoviedb.models.*
import com.dimi.themoviedb.utils.ConvertUtils


@Database(
    entities = [
        Movie::class,
        Cast::class,
        Actor::class,
        TVShow::class,
        TVCast::class,
        TVSeason::class,
        TVEpisode::class,
        CombinedCredit::class
    ],
    version = 45
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getActorDao(): ActorsDao

    abstract fun getMoviesDao(): MoviesDao

    abstract fun getTvShowsDao(): TvShowsDao

    companion object {
        const val DATABASE_NAME: String = "app_db"
    }
}






