package com.dimi.themoviedb.persistence

import androidx.room.*
import com.dimi.themoviedb.models.Actor
import com.dimi.themoviedb.api.ApiConstants
import com.dimi.themoviedb.models.CombinedCredit

@Dao
interface ActorsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actor: Actor): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(combinedCredit: CombinedCredit): Long

    @Update
    suspend fun update(actor: Actor)

    @Query(
        """
        SELECT * FROM actor 
        WHERE name LIKE '%' || :query || '%' 
        OR biography LIKE '%' || :query || '%' 
        ORDER BY popularity DESC
        LIMIT (:page * :pageSize)
        """
    )
    suspend fun getAllActors(
        query: String,
        page: Int,
        pageSize: Int = ApiConstants.PAGE_SIZE
    ): List<Actor>

    @Query("SELECT * FROM actor WHERE id =:id")
    suspend fun getActor(
        id: Int
    ): Actor?

    @Query(
        """
        SELECT * FROM combined_credits 
        WHERE actor_id =:actorId 
        ORDER BY vote_count DESC
        LIMIT 10
    """
    )
    suspend fun getActorCredits(
        actorId: Int
    ): List<CombinedCredit>


}