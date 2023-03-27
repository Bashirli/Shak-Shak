package com.bashirli.saksak.repo.api

import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.util.Resource
import retrofit2.Response

interface Repo {
    suspend fun getProducts():Resource<List<ProductModel>>

    suspend fun getSingle(id:Int):Resource<ProductModel>

    suspend fun getCategories(): Resource<List<CategoryModel>>

    suspend fun getSearchProducts(search:String):Resource<List<ProductModel>>

    suspend fun getCategoryProducts(categoryId:Int):Resource<List<ProductModel>>
}