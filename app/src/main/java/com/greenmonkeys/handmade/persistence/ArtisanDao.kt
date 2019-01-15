package com.greenmonkeys.handmade.persistence

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM Artisan WHERE id = :id AND cga_id = :cgaId")
    fun getArtisanById(id: Int, cgaId: Int): LiveData<Artisan>
}