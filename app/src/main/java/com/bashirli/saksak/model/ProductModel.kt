package com.bashirli.saksak.model

import java.io.Serializable

data class ProductModel(
    val id:Int,
    val title:String,
    val price:String,
    val description:String,
    val images:List<String>,
    val creationAt:String,
    val updatedAt:String,
    val category: CategoryModel
) : Serializable

