package com.bashirli.saksak.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.repo.api.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class HomeMVVM @Inject constructor(
    private var repo: Repo
) :ViewModel() {

    private var _loading=MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
    get()=_loading

    private var _errorData=MutableLiveData<Boolean>()
    val errorData:LiveData<Boolean>
        get()=_errorData

    private var _success=MutableLiveData<Boolean>()
    val success:LiveData<Boolean>
        get()=_success

    private var _loadingProduct=MutableLiveData<Boolean>()
    val loadingProduct:LiveData<Boolean>
        get()=_loadingProduct

    private var _errorDataProduct=MutableLiveData<Boolean>()
    val errorDataProduct:LiveData<Boolean>
        get()=_errorDataProduct

    private var _successProduct=MutableLiveData<Boolean>()
    val successProduct:LiveData<Boolean>
        get()=_successProduct

    private var _categoryListProduct=MutableLiveData<List<ProductModel>>()
    val categoryListProduct:LiveData<List<ProductModel>>
        get() = _categoryListProduct


    private var _categoryList=MutableLiveData<List<CategoryModel>>()
    val categoryList:LiveData<List<CategoryModel>>
    get() = _categoryList


    private var job:Job?=null


    var errorMessage:String?=null

    private var exceptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        _loading.value=false
        errorMessage=throwable.localizedMessage
        _errorData.value=true

    }
    private var exceptionHandlerProducts= CoroutineExceptionHandler { coroutineContext, throwable ->
        _loadingProduct.value=false
        errorMessage=throwable.localizedMessage
        _errorDataProduct.value=true

    }

    init {
        loadCategories()
        loadProducts()
    }

    private fun loadProducts(){
        _loadingProduct.value=true
        _successProduct.value=false
        _errorDataProduct.value=false
        errorMessage=null
        try {
            job=CoroutineScope(Dispatchers.IO+exceptionHandlerProducts)
                .launch {
                val response=repo.getProducts()
                withContext(Dispatchers.Main){
                    response.data?.let {myList->
                        _categoryListProduct.value=myList
                        _successProduct.value=true
                        _loadingProduct.value=false
                    }
                }
            }
        }catch (e:Exception){
            errorMessage=e.localizedMessage
            _errorDataProduct.value=true
            _loadingProduct.value=false
        }
    }


    private fun loadCategories(){
        _loading.value=true
        _success.value=false
        _errorData.value=false
        errorMessage=null
        try {
            job=CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val response=repo.getCategories()
                withContext(Dispatchers.Main){
                    response.data?.let {myList->
                        _categoryList.value=myList
                        _success.value=true
                        _loading.value=false
                    }
                }
            }
        }catch (e:Exception){
            errorMessage=e.localizedMessage
            _errorData.value=true
            _loading.value=false
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}