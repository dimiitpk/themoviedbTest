package com.dimi.themoviedb.persistence

import androidx.room.*
import com.dimi.themoviedb.models.Cast
import com.dimi.themoviedb.models.Movie
import com.dimi.themoviedb.api.ApiConstants

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert( movie: Movie): Long

    @Update
    suspend fun update( movie: Movie )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert( cast: Cast): Long

    @Query("""
        SELECT * FROM movie 
        WHERE title LIKE '%' || :query || '%' 
        OR overview LIKE '%' || :query || '%' 
        ORDER BY popularity DESC
        LIMIT (:page * :pageSize)
        """)
    suspend fun getAllMovies(
        query: String,
        page: Int,
        pageSize: Int = ApiConstants.PAGE_SIZE
    ): List<Movie>

    @Query("""
        SELECT * FROM movie_cast
        WHERE movieId = :movieId
        ORDER BY `order` ASC
    """)
    suspend fun getMovieCast(
        movieId: Int
    ): List<Cast>

    @Query("""
        SELECT * FROM movie
        WHERE id = :movieId
    """)
    suspend fun getMovie(
        movieId: Int
    ): Movie

    @Query("""
        SELECT * FROM movie 
        WHERE genre_ids LIKE '%' || :genre || '%' 
        ORDER BY popularity DESC
        LIMIT (:page * :pageSize)
        """)
    suspend fun getMoviesByGenre(
        genre: String,
        page: Int,
        pageSize: Int = ApiConstants.PAGE_SIZE
    ): List<Movie>
}