package com.bashirli.saksak.di

import com.bashirli.saksak.repo.api.DownloadRepo
import com.bashirli.saksak.repo.api.Repo
import com.bashirli.saksak.repo.api.searchrepo.SearchRepo
import com.bashirli.saksak.repo.api.searchrepo.SearchRepoClass
import com.bashirli.saksak.service.api.Service
import com.bashirli.saksak.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
   fun injectService()=Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(Service::class.java)


    @Singleton
    @Provides
    fun injectRepo(service: Service)= DownloadRepo(service) as Repo


    @Singleton
    @Provides
    fun injectSearchRepo(service: Service)=SearchRepoClass(service) as SearchRepo


}