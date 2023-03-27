package com.bashirli.saksak.repo.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bashirli.saksak.model.db.ItemModel

interface FavRepo {
    suspend fun insertData(itemModel: ItemModel)

    suspend fun deleteData(itemModel: ItemModel)

    suspend fun deleteAll()

    suspend fun deleteIfNotWorks(id:Int)

    suspend fun getAllData():List<ItemModel>

}