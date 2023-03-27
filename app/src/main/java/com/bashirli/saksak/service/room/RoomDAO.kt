package com.bashirli.saksak.service.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bashirli.saksak.model.db.ItemModel

@Dao
interface RoomDAO {
    @Insert
    suspend fun insertData(itemModel: ItemModel)

    @Delete
    suspend fun deleteData(itemModel: ItemModel)

    @Query("delete from Favorites")
    suspend fun deleteAll()

    @Query("delete from Favorites where productId=:id")
    suspend fun deleteIfNotWorks(id:Int)

    @Query("Select * from Favorites")
    suspend fun getAllData():List<ItemModel>


}