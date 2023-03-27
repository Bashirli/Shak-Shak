package com.bashirli.saksak.repo.api.searchrepo

import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.service.api.Service
import com.bashirli.saksak.util.Resource
import javax.inject.Inject

class SearchRepoClass @Inject constructor(
    private var service: Service
) : SearchRepo {

    override suspend fun getFilteredSearchProducts(
        searchText: String,
        minPrice: String,
        maxPrice: String,
    ): Resource<List<ProductModel>> {
        return try{
            val response=service.getFilteredSearchProducts(searchText, minPrice, maxPrice)
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                } ?: Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }
        }catch (e:Exception){
            Resource.error(e.localizedMessage,null)
        }
    }

    override suspend fun getProductsOnlyForPrice(
        minPrice: String,
        maxPrice: String,
    ): Resource<List<ProductModel>> {
        return try{
            val response=service.getProductsOnlyForPrice(minPrice, maxPrice)
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                } ?: Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }
        }catch (e:Exception){
            Resource.error(e.localizedMessage,null)
        }
    }

    override suspend fun getProductsOnlyForMinPrice(
        searchText: String,
        minPrice: String,
    ): Resource<List<ProductModel>> {
        return try{
            val response=service.getProductsOnlyForMinPrice(searchText, minPrice)
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                } ?: Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }
        }catch (e:Exception){
            Resource.error(e.localizedMessage,null)
        }
    }

    override suspend fun getProductOnlyMinPrice(minPrice: String): Resource<List<ProductModel>> {
        return try{
            val response=service.getProductOnlyMinPrice(minPrice)
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                } ?: Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }
        }catch (e:Exception){
            Resource.error(e.localizedMessage,null)
        }
    }

    override suspend fun getProductCatWithFilter(
        minPrice: String,
        maxPrice: String,
        categoryId: Int,
    ): Resource<List<ProductModel>> {
        return try{
            val response=service.getProductCatWithFilter(minPrice, maxPrice, categoryId)
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                }?:Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }
        }catch (e:Exception){
            Resource.error(e.localizedMessage,null)
        }
    }

    override suspend fun getProductWithOnlyMinPriceAndCat(
        minPrice: String,
        categoryId: Int,
    ): Resource<List<ProductModel>> {
        return try{
            val response=service.getProductWithOnlyMinPriceAndCat(minPrice, categoryId)
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                }?:Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }
        }catch (e:Exception){
            Resource.error(e.localizedMessage,null)
        }
    }
}