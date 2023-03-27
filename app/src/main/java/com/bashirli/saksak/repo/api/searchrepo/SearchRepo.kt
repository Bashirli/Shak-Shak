package com.bashirli.saksak.repo.api.searchrepo

import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.util.Resource
import retrofit2.http.Query


interface SearchRepo {


    suspend fun getFilteredSearchProducts(
        searchText:String,
        minPrice:String,
        maxPrice:String
    ) : Resource<List<ProductModel>>


    suspend fun getProductsOnlyForPrice(
        minPrice:String,
        maxPrice:String
    ) : Resource<List<ProductModel>>


    suspend fun getProductsOnlyForMinPrice(
        searchText:String,
        minPrice:String
    ) : Resource<List<ProductModel>>

    suspend fun getProductOnlyMinPrice(
        minPrice:String
    ) : Resource<List<ProductModel>>


    suspend fun getProductCatWithFilter(
        minPrice: String,
        maxPrice: String,
        categoryId: Int
    ) : Resource<List<ProductModel>>

    suspend fun getProductWithOnlyMinPriceAndCat(
        minPrice: String,
        categoryId: Int
    ) : Resource<List<ProductModel>>

}