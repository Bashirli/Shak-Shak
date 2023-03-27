package com.bashirli.saksak.repo.room

import com.bashirli.saksak.model.db.ItemModel
import com.bashirli.saksak.service.room.RoomDAO
import javax.inject.Inject

class FavoritesRepo @Inject constructor(
   private var roomDAO: RoomDAO
) : FavRepo {

    override suspend fun insertData(itemModel: ItemModel) {
        roomDAO.insertData(itemModel)
    }

    override suspend fun deleteData(itemModel: ItemModel) {
        roomDAO.deleteData(itemModel)
    }

    override suspend fun deleteAll() {
        roomDAO.deleteAll()
    }

    override suspend fun deleteIfNotWorks(id: Int) {
    roomDAO.deleteIfNotWorks(id)
    }

    override suspend fun getAllData(): List<ItemModel> {
    return roomDAO.getAllData()
    }

}