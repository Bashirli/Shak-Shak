package com.bashirli.saksak.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorites")
data class ItemModel(
    @ColumnInfo(name = "productId") val productId:Int,
    @ColumnInfo(name = "productTitle") val productTitle:String,
    @PrimaryKey(autoGenerate = true) val id:Int?=null
)
