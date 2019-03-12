package com.greenmonkeys.handmade.persistence

import androidx.room.*

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGroupMember(group: Group)

    @Delete
    fun removeGroupMember(group: Group)

    @Query("SELECT a.* FROM Artisan a, `Group` g WHERE g.cga_id = :cgaId AND a.cga_id = g.cga_id AND a.email = g.artisan_id AND g.group_name = :groupName")
    fun getMembersOfGroup(groupName: String, cgaId: String): List<Artisan>

    @Query("SELECT * FROM `Group` WHERE cga_id = :cgaId GROUP BY group_name, cga_id")
    fun getGroupsForCGA(cgaId: String): List<Group>
}