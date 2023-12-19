package com.example.bersihkan.data.local.room
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bersihkan.data.local.model.Recommendation

@Dao
interface RecommendationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recommendation: Recommendation)

    @Query("SELECT * from recommendation WHERE postalCode = :postalCode")
    fun getFacilityByPostalCode(postalCode: Int): Recommendation?

}