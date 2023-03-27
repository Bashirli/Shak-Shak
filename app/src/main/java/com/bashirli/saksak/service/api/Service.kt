package com.bashirli.saksak.service.api

import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.util.CATEGORY
import com.bashirli.saksak.util.PRODUCTS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET(PRODUCTS)
    suspend fun getAllProducts():Response<List<ProductModel>>

    @GET(CATEGORY)
    suspend fun getCategorites():Response<List<CategoryModel>>

    @GET(PRODUCTS+"/{id}")
    suspend fun getSingleProduct(@Path("id") id:Int):Response<ProductModel>

    @GET(PRODUCTS)
    suspend fun getCategoryProducts(@Query("categoryId") categoryId:Int) : Response<List<ProductModel>>


    @GET(PRODUCTS)
    suspend fun searchProduct(@Query("title") searchText:String):Response<List<ProductModel>>


    // Filter search list ->


    @GET(PRODUCTS)
    suspend fun getFilteredSearchProducts(
        @Query("title") searchText:String,
        @Query("price_min") minPrice:String,
        @Query("price_max") maxPrice:String
    ) : Response<List<ProductModel>>

    @GET(PRODUCTS)
    suspend fun getProductsOnlyForPrice(
        @Query("price_min") minPrice:String,
        @Query("price_max") maxPrice:String
    ) : Response<List<ProductModel>>

    @GET(PRODUCTS)
    suspend fun getProductsOnlyForMinPrice(
        @Query("title") searchText:String,
        @Query("price_min") minPrice:String
    ) : Response<List<ProductModel>>

    @GET(PRODUCTS)
    suspend fun getProductOnlyMinPrice(@Query("price_min") minPrice:String): Response<List<ProductModel>>

    @GET(PRODUCTS)
    suspend fun getProductCatWithFilter(
        @Query("price_min") minPrice: String,
        @Query("price_max") maxPrice: String,
        @Query("categoryId") categoryId: Int
    ) : Response<List<ProductModel>>

    @GET(PRODUCTS)
    suspend fun getProductWithOnlyMinPriceAndCat(
        @Query("price_min") minPrice: String,
        @Query("categoryId") categoryId: Int
    ) : Response<List<ProductModel>>

}