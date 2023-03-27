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
class CategoryMVVM @Inject
constructor(
    private var repo: Repo
) : ViewModel() {

    private val _loadingProduct=MutableLiveData<Boolean>()
    val loadingProduct:LiveData<Boolean>
        get()=_loadingProduct

    private val _errorDataProduct=MutableLiveData<Boolean>()
    val errorDataProduct:LiveData<Boolean>
        get()=_errorDataProduct

    private val _successProduct=MutableLiveData<Boolean>()
    val successProduct:LiveData<Boolean>
        get()=_successProduct

    private val _productList=MutableLiveData<List<ProductModel>>()
    val productList:LiveData<List<ProductModel>>
        get()=_productList

    private val _loading=MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
    get()=_loading

    private val _errorData=MutableLiveData<Boolean>()
    val errorData:LiveData<Boolean>
        get()=_errorData

    private val _success=MutableLiveData<Boolean>()
    val success:LiveData<Boolean>
        get()=_success

    private val _categoryList=MutableLiveData<List<CategoryModel>>()
    val categoryList:LiveData<List<CategoryModel>>
        get()=_categoryList


    private var job: Job?=null

    private val _errorMessage=MutableLiveData<String>()
    val errorMessage:LiveData<String>
    get()=_errorMessage

    private val exceptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        _loading.value=false
        _errorMessage.value=throwable.localizedMessage
        _errorData.value=true
    }
    private val exceptionHandlerProduct= CoroutineExceptionHandler { coroutineContext, throwable ->
        _loadingProduct.value=false
        _errorMessage.value=throwable.localizedMessage
        _errorDataProduct.value=true
    }

    init {
        loadAllData()
    }

    private fun loadAllData(){
        loadCategories()
        loadProducts()
    }

    private fun loadProducts(){
        _loadingProduct.value=true
        _errorDataProduct.value=false
        _successProduct.value=false
        _errorMessage.value=""
        try{
            job= CoroutineScope(Dispatchers.IO+exceptionHandlerProduct)
                .launch {
                    var response=repo.getProducts()
                    withContext(Dispatchers.Main){
                        response.data?.let {
                            _productList.value=it
                            _successProduct.value=true
                            _loadingProduct.value=false
                            return@withContext
                        } ?: listIsEmpty()
                    }

                }
        }catch (e:Exception){
            _loadingProduct.value=false
            _errorMessage.value=e.localizedMessage
            _errorDataProduct.value=true
        }
    }

    private fun loadCategories(){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value=""
        try{
            job= CoroutineScope(Dispatchers.IO+exceptionHandler)
                .launch {
                var response=repo.getCategories()
                    withContext(Dispatchers.Main){
                        response.data?.let {
                            _categoryList.value=it
                            _success.value=true
                            _loading.value=false
                            return@withContext
                        } ?: listIsEmpty()
                    }

            }
        }catch (e:Exception){
            _loading.value=false
            _errorMessage.value=e.localizedMessage
            _errorData.value=true
        }
    }

   private fun listIsEmpty(){
       _categoryList.value= listOf<CategoryModel>()
       _success.value=true
       _loading.value=false
   }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}