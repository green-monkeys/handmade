package com.greenmonkeys.handmade.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CGADao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCGA(cga: CGA)

    @Query("SELECT 1 FROM CGA WHERE email = :email")
    fun containsCGA(email: String): Boolean

    @Query("SELECT * FROM CGA WHERE email = :email")
    fun getCGAByEmail(email: String): CGA

    @Query("SELECT * FROM Artisan WHERE cga_id = :cgaId")
    fun getArtisansForCGA(cgaId: String): List<Artisan>
}