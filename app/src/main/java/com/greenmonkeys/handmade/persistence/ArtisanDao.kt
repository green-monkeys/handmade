package com.greenmonkeys.handmade.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtisanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtisans(vararg artisans: Artisan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtisan(artisan: Artisan)

    @Query("SELECT 1 FROM Artisan WHERE cga_id = :cgaId AND email = :email")
    fun containsArtisan(email: String, cgaId: Int): Boolean

    @Query("SELECT password as hash, salt FROM Artisan WHERE email = :email AND cga_id = :cgaId")
    fun getArtisanPasswordByEmail(email: String, cgaId: Int): Security.Password

    @Query("SELECT * FROM Artisan WHERE email = :email AND cga_id = :cgaId")
    fun getArtisanByEmail(email: String, cgaId: Int): Artisan
}