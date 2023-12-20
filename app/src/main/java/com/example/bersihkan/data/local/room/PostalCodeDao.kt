package com.example.bersihkan.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bersihkan.data.local.model.PostalCode

@Dao
interface PostalCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(postalCode: PostalCode)

    @Query("SELECT * from postalcode WHERE postalCode = :postalCode")
    fun getIdxByPostalCode(postalCode: Int): PostalCode?

}