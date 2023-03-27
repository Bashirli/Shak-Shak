package com.bashirli.saksak.viewmodel.main

import java.util.ArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.model.db.ItemModel
import com.bashirli.saksak.repo.api.Repo
import com.bashirli.saksak.repo.room.FavRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class FavoritesMVVM @Inject constructor(
    private val favRepo: FavRepo,
    private val repo: Repo
) : ViewModel() {

    private val _loading= MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _errorData= MutableLiveData<Boolean>()
    val errorData: LiveData<Boolean>
        get()=_errorData

    private val _success= MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get()=_success

    private val _errorMessage= MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get()=_errorMessage

    private val _productList= MutableLiveData<List<ProductModel>>()
    val productList: LiveData<List<ProductModel>>
        get()=_productList

    private val _favList=MutableLiveData<ArrayList<ProductModel>>()
    val favList:LiveData<ArrayList<ProductModel>>
    get()=_favList

    private val _catList=MutableLiveData<ArrayList<CategoryModel>>()
    val catList:LiveData<ArrayList<CategoryModel>>
        get()=_catList


    private var job: Job?=null

    private var exceptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        _loading.value=false
        _errorMessage.value=throwable.localizedMessage
        _errorData.value=true
    }




    private fun loadFavs(){
        job=CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response=favRepo.getAllData()
            withContext(Dispatchers.Main+exceptionHandler){
                productList.value?.let {
                    val arrayList=ArrayList<ProductModel>()
                    for(data in response){
                        for(pdata in it){
                            if(pdata.id==data.productId){
                                arrayList.add(pdata)
                            }
                        }
                    }
                    _success.value=true
                    _loading.value=false
                    _favList.value=arrayList
                }
            }
        }
    }

    fun loadData(){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value=""
        try{
            job=CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val response=repo.getProducts()
                withContext(Dispatchers.Main+exceptionHandler){
                    response.data?.let {
                        _productList.value=it
                        loadCat()
                    }
                }
            }
        }catch (e:Exception){
            _loading.value=false
            _errorMessage.value=e.localizedMessage
            _errorData.value=true
        }
    }

    private fun loadCat(){
        try{
            job=CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val response=repo.getCategories()
                withContext(Dispatchers.Main+exceptionHandler){
                    response.data?.let {
                        _catList.value= ArrayList(it)
                        loadFavs()
                    }
                }
            }
        }catch (e:Exception){
            _loading.value=false
            _errorMessage.value=e.localizedMessage
            _errorData.value=true
        }
    }


    fun refreshData(){
        loadData()
    }

    fun deleteItem(id:Int){
        try {
            job=CoroutineScope(Dispatchers.IO).launch {
                    favRepo.deleteIfNotWorks(id)
            }
        }catch (e:Exception){

        }
    }

    fun insertFav(data:ItemModel){
        try {
            job=CoroutineScope(Dispatchers.IO).launch {
                    favRepo.insertData(data)
            }
        }catch (e:Exception){  }
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}