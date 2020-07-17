package com.dimi.themoviedb.persistence

import androidx.room.*
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.models.*

@Dao
interface TvShowsDao {

    /*
    *  OnConflictStrategy.IGNORE is on the entities that are parents of foreign key
    *  so when u replace them they lose foreign keys, but on ignore they don't
    *  but we need to check for return value of insert and if it is -1 then its already inserted and then just update
    * */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert( tvShow: TVShow): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert( cast: TVCast): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert( episode: TVEpisode): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert( season: TVSeason): Long

    @Update
    suspend fun update( tvShow: TVShow )
    @Update
    suspend fun update( season: TVSeason )

    @Query("""
        SELECT * FROM tv_show 
        WHERE title LIKE '%' || :query || '%' 
        OR overview LIKE '%' || :query || '%' 
        ORDER BY popularity DESC
        LIMIT (:page * :pageSize)
        """)
    suspend fun getAllTvShows(
        query: String,
        page: Int,
        pageSize: Int = ApiConstants.PAGE_SIZE
    ): List<TVShow>

    @Query("""
        SELECT * FROM tv_show 
        WHERE genre_ids LIKE '%' || :genre || '%' 
        ORDER BY popularity DESC
        LIMIT (:page * :pageSize)
        """)
    suspend fun getTVShowsByGenre(
        genre: String,
        page: Int,
        pageSize: Int = ApiConstants.PAGE_SIZE
    ): List<TVShow>

    @Transaction
    suspend fun loadInsertOrUpdate( season: TVSeason ) {
        var seasonId = getTvSeasonId( season.tvShowId, season.seasonNumber ) ?: -1

        if( seasonId < 0 )
            seasonId = insert( season )
        else
            update( season )

        season.id = seasonId
    }

    @Query("""
        SELECT * FROM tv_show
        WHERE id = :tvShowId
    """)
    suspend fun getTvShow(
        tvShowId: Int
    ): TVShow

    @Query("""
        SELECT * FROM tv_cast
        WHERE tvShowId = :tvShowId
        ORDER BY `order` ASC
    """)
    suspend fun getTvShowCast(
        tvShowId: Int
    ): List<TVCast>

    @Query("""
        SELECT * FROM tv_season
        WHERE tvShowId = :tvShowId
        ORDER BY season_number ASC
    """)
    suspend fun getTvShowSeasons(
        tvShowId: Int
    ): List<TVSeason>

    @Query("""
        SELECT id FROM tv_season
        WHERE tvShowId = :tvShowId AND season_number = :seasonNumber
    """)
    suspend fun getTvSeasonId(
        tvShowId: Int,
        seasonNumber: Int
    ) : Long?

    @Query("""
        SELECT * FROM tv_episode
        WHERE show_id = :tvShowId AND season_number = :seasonNumber
        ORDER BY episode_number ASC
    """)
    suspend fun getTvShowSeasonEpisodes(
        tvShowId: Int,
        seasonNumber: Int
    ): List<TVEpisode>
}