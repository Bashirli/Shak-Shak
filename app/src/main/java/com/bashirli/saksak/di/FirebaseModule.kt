package com.bashirli.saksak.di

import android.content.Context
import com.bashirli.saksak.repo.firebase.CommentsInterface
import com.bashirli.saksak.repo.firebase.CommentsRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun injectAuth()=FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun injectStorage()=FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun injectFirestore()=FirebaseFirestore.getInstance()


    @Singleton
    @Provides
    fun injectComRepo(@ApplicationContext context: Context,auth: FirebaseAuth,firestore: FirebaseFirestore)=CommentsRepo(context,auth,firestore) as CommentsInterface


}