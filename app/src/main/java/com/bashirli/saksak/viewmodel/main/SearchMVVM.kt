package com.bashirli.saksak.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.repo.api.Repo
import com.bashirli.saksak.repo.api.searchrepo.SearchRepo
import com.bashirli.saksak.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class SearchMVVM @Inject constructor(
    private var repo: Repo,
    private var searchRepo: SearchRepo
): ViewModel() {

    private val _productLoading=MutableLiveData<Boolean>()
    val productLoading:LiveData<Boolean>
    get() = _productLoading

    private val _productError=MutableLiveData<Boolean>()
    val productError:LiveData<Boolean>
        get() = _productError

    private val _productSuccess=MutableLiveData<Boolean>()
    val productSuccess:LiveData<Boolean>
        get() = _productSuccess


    private val _searchLoading=MutableLiveData<Boolean>()
    val searchLoading:LiveData<Boolean>
        get() = _searchLoading

    private val _searchError=MutableLiveData<Boolean>()
    val searchError:LiveData<Boolean>
        get() = _searchError

    private val _searchSuccess=MutableLiveData<Boolean>()
    val searchSuccess:LiveData<Boolean>
        get() = _searchSuccess

    private var _searchList=MutableLiveData<List<ProductModel>>()
    val searchList:LiveData<List<ProductModel>>
        get()=_searchList

    private var _productList=MutableLiveData<List<ProductModel>>()
    val productList:LiveData<List<ProductModel>>
        get()=_productList


    private val _errorMessage=MutableLiveData<String>()
    val errorMessage:LiveData<String>
        get() = _errorMessage

    private var job: Job?=null

    private var exceptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        _productLoading.value=false
        _errorMessage.value=throwable.localizedMessage
        _productError.value=true
    }




     fun loadData(){
        _productLoading.value=true
        _productError.value=false
        _productSuccess.value=false
        _errorMessage.value=""
        try {
        job=CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response=repo.getProducts()
            withContext(Dispatchers.Main){
                response.data?.let {
                    _productList.value=it
                    _productSuccess.value=true
                    _productLoading.value
                }
            }
        }
        }catch (e:Exception){
            _productLoading.value=false
            _errorMessage.value=e.localizedMessage
            _productError.value=true
        }
    }


    fun searchProduct(
        searchText:String,
        isFiltered:Boolean,
        minPrice:Int?=null,
        maxPrice:Int?=null
        ){
        if(isFiltered){
            if(minPrice==null&&maxPrice!=null){
                filterSearch(searchText, 0, maxPrice)
            }else if(minPrice!=null&&maxPrice==null){
                filterSearch(searchText, minPrice, maxPrice)
            }else{
                filterSearch(searchText, minPrice!!, maxPrice)
            }
        }else{
            nonFilterSearch(searchText)
        }

    }

    private fun filterSearch(
        searchText:String,
        minPrice:Int,
        maxPrice:Int?=null){
        _productLoading.value=true
        _productError.value=false
        _productSuccess.value=false
        _errorMessage.value=""

        if(searchText.isEmpty()){
           filterSearchForNoTitle(minPrice, maxPrice)
        }else{
            if(maxPrice!=null){
            job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val response=searchRepo.getFilteredSearchProducts(searchText,minPrice.toString(),maxPrice.toString())
                withContext(Dispatchers.Main){
                   when(response.status){
                       Status.SUCCESS  ->{
                        response.data?.let {
                            _searchList.value=it
                            _searchSuccess.value=true
                            _searchLoading.value=false
                        }
                       }
                       Status.ERROR -> {
                        _searchLoading.value=false
                           _errorMessage.value=response.message!!
                           _searchError.value=true
                       }
                       else -> {
                           _searchLoading.value=false
                           _errorMessage.value="Error"
                           _searchError.value=true
                       }
                   }
               }
            }
            }else{
                job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                    val response=searchRepo.getProductsOnlyForMinPrice(searchText,minPrice.toString())
                    withContext(Dispatchers.Main){
                        when(response.status){
                            Status.SUCCESS  ->{
                                response.data?.let {
                                    _searchList.value=it
                                    _searchSuccess.value=true
                                    _searchLoading.value=false
                                }
                            }
                            Status.ERROR -> {
                                _searchLoading.value=false
                                _errorMessage.value=response.message!!
                                _searchError.value=true
                            }
                            else -> {
                                _searchLoading.value=false
                                _errorMessage.value="Error"
                                _searchError.value=true
                            }
                        }
                    }
                }
            }
        }


    }

    private fun filterSearchForNoTitle(minPrice: Int, maxPrice: Int?=null){
    if(maxPrice!=null){
        job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response=searchRepo.getProductsOnlyForPrice(minPrice.toString(),maxPrice.toString())
            withContext(Dispatchers.Main){
                when(response.status){
                    Status.SUCCESS  ->{
                        response.data?.let {
                            _searchList.value=it
                            _searchSuccess.value=true
                            _searchLoading.value=false
                        }
                    }
                    Status.ERROR -> {
                        _searchLoading.value=false
                        _errorMessage.value=response.message!!
                        _searchError.value=true
                    }
                    else -> {
                        _searchLoading.value=false
                        _errorMessage.value="Error"
                        _searchError.value=true
                    }
                }
            }
        }
    }else{
        job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response=searchRepo.getProductOnlyMinPrice(minPrice.toString())
            withContext(Dispatchers.Main){
                when(response.status){
                    Status.SUCCESS  ->{
                        response.data?.let {
                            _searchList.value=it
                            _searchSuccess.value=true
                            _searchLoading.value=false
                        }
                    }
                    Status.ERROR -> {
                        _searchLoading.value=false
                        _errorMessage.value=response.message!!
                        _searchError.value=true
                    }
                    else -> {
                        _searchLoading.value=false
                        _errorMessage.value="Error"
                        _searchError.value=true
                    }
                }
            }
        }
    }
    }


    private fun nonFilterSearch(searchText: String){
        _productLoading.value=true
        _productError.value=false
        _productSuccess.value=false
        _errorMessage.value=""
        try {
            job=CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val response=repo.getSearchProducts(searchText)
                withContext(Dispatchers.Main){
                    response.data?.let {
                        _searchList.value=it
                        _searchLoading.value=false
                        _searchSuccess.value=true

                    }
                }
            }
        }catch (e:Exception){
            _searchLoading.value=false
            _errorMessage.value=e.localizedMessage
            _searchError.value=true
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}