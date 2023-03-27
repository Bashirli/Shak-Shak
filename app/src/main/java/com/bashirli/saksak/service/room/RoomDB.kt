package com.bashirli.saksak.service.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bashirli.saksak.model.db.ItemModel

@Database(entities = [ItemModel::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun getDao():RoomDAO
}