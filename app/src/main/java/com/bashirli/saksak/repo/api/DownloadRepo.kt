package com.bashirli.saksak.repo.api

import com.bashirli.saksak.service.api.Service
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.util.Resource
import javax.inject.Inject

class DownloadRepo @Inject constructor(
    var service: Service
) : Repo {


    override suspend fun getProducts(): Resource<List<ProductModel>> {
        return try {
            val response=service.getAllProducts()
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                }?: return Resource.error("Error",null)
            }else{
                return Resource.error("Error",null)
            }
        }catch (e:Exception){
            println(e.localizedMessage)
            return Resource.error(e.localizedMessage,null)

        }
    }

    override suspend fun getSingle(id: Int): Resource<ProductModel> {
        return try{
            val response=service.getSingleProduct(id)
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                }?: return Resource.error("Error",null)
            }else{
                return Resource.error("Error",null)
            }
        }catch (e:Exception){
            return Resource.error(e.localizedMessage,null)
        }
    }

    override suspend fun getCategories(): Resource<List<CategoryModel>> {
        return try {
           val response=service.getCategorites()
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                }  ?: return Resource.error("Error",null)
            }else{
                return Resource.error("Error",null)
            }
        }catch (e:Exception){
           return Resource.error(e.localizedMessage,null)
        }
    }

    override suspend fun getSearchProducts(search: String): Resource<List<ProductModel>> {
        return try{
            val response=service.searchProduct(search)
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                }?: return Resource.error("Error",null)
            }else{
                return Resource.error("Error",null)
            }
        }catch (e:Exception){
            return Resource.error(e.localizedMessage,null)
        }
    }

    override suspend fun getCategoryProducts(categoryId: Int): Resource<List<ProductModel>> {
        return try{
            val response=service.getCategoryProducts(categoryId)
            if(response.isSuccessful){
                response.body()?.let {
                    Resource.success(it)
                }?: return Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }
        }catch (e:Exception){
            Resource.error(e.localizedMessage,null)
        }
    }
}