package com.greenmonkeys.handmade.persistence

import androidx.room.*

@Dao
interface ArtisanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtisans(vararg artisans: Artisan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtisan(artisan: Artisan)

    @Query("SELECT 1 FROM Artisan WHERE cga_id = :cgaId AND email = :email")
    fun containsArtisan(email: String, cgaId: String): Boolean

    @Query("SELECT password as hash, salt FROM Artisan WHERE email = :email AND cga_id = :cgaId")
    fun getArtisanPasswordByEmail(email: String, cgaId: String): Security.Password

    @Query("SELECT * FROM Artisan WHERE email = :email AND cga_id = :cgaId")
    fun getArtisanByEmail(email: String, cgaId: String): Artisan

    @Query("UPDATE Artisan SET password = :password, salt = :salt, has_logged_in = 1 WHERE cga_id = :cgaId AND email = :email")
    fun updateArtisanPassword(email: String, cgaId: String, password: String, salt: String)

    @Query("UPDATE Artisan SET has_logged_in = 0 WHERE cga_id = :cgaId AND email = :email")
    fun setArtisanNotLoggedIn(email: String, cgaId: String)
}