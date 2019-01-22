package com.greenmonkeys.handmade.persistence

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM CGA WHERE id = :id")
    fun getCGAById(id: Int): CGA

    @Query("SELECT password as hash, salt FROM CGA WHERE email = :email")
    fun getCGAPasswordByEmail(email: String): Security.Password

    @Query("SELECT * FROM CGA WHERE email = :email")
    fun getCGAByEmail(email: String): CGA


    @Query("SELECT 1 FROM CGA WHERE id = :cgaId")
    fun cgaIdIsValid(cgaId: Int): Boolean
}