package com.bashirli.saksak.di

import android.content.Context
import androidx.room.Room
import com.bashirli.saksak.repo.room.FavRepo
import com.bashirli.saksak.repo.room.FavoritesRepo
import com.bashirli.saksak.service.room.RoomDAO
import com.bashirli.saksak.service.room.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun injectRoomDB(@ApplicationContext context:Context)=
        Room.databaseBuilder(
        context,
            RoomDB::class.java,
            "FavoritesDB"
    ).build()

    @Singleton
    @Provides
    fun injectRoomDAO(roomDB: RoomDB)=roomDB.getDao()

    @Singleton
    @Provides
    fun injectRoomRepo(roomDAO: RoomDAO)=FavoritesRepo(roomDAO) as FavRepo


}