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

    @Query("SELECT * FROM CGA WHERE id = :id")
    fun getCGAById(id: Int): LiveData<CGA>
}