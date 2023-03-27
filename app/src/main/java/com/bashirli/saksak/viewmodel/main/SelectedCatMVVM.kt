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

@HiltViewModel
class SelectedCatMVVM @Inject constructor(
    private val repo: Repo,
    private val searchRepo: SearchRepo
) : ViewModel() {

    private val _loading=MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
    get() = _loading

    private val _errorData=MutableLiveData<Boolean>()
    val errorData:LiveData<Boolean>
    get()=_errorData

    private val _success=MutableLiveData<Boolean>()
    val success:LiveData<Boolean>
    get()=_success

    private val _errorMessage=MutableLiveData<String>()
    val errorMessage:LiveData<String>
    get()=_errorMessage

    private val _productList=MutableLiveData<List<ProductModel>>()
    val productList:LiveData<List<ProductModel>>
    get()=_productList

    private var job: Job?=null

    private var exceptionHandler=CoroutineExceptionHandler { coroutineContext, throwable ->
        _loading.value=false
        _errorMessage.value=throwable.localizedMessage
        _errorData.value=true
    }



    fun loadData(categoryId:Int){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value="Error"
        try {
            job=CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val response=repo.getCategoryProducts(categoryId)
                withContext(Dispatchers.Main+exceptionHandler){
                    if(response.status==Status.SUCCESS) {
                        response.data?.let {
                            _productList.value = it
                            _loading.value = false
                            _success.value = true
                        }
                    }else if(response.status==Status.ERROR){
                        _loading.value=false
                        response.message?.let {
                            _errorMessage.value=it
                        }
                        _errorData.value=true
                    }
                }
            }
        }catch (e:Exception){
            _errorMessage.value=e.localizedMessage
            _loading.value=false
            _errorData.value=true
        }

    }


    fun filterCategories(minPrice:Int?,maxPrice:Int?,categoryId: Int){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value="Error"
        if(minPrice==null&&maxPrice!=null){
            filteredSearch("0", maxPrice.toString(),categoryId)
        }else{
            filteredSearch(minPrice.toString(), maxPrice.toString(),categoryId)
        }


    }

    private fun filteredSearch(minPrice:String,maxPrice:String?,categoryId: Int){
        try {
            if(maxPrice!=null){
                job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                    val response=searchRepo.getProductCatWithFilter(minPrice,maxPrice,categoryId)
                    withContext(Dispatchers.Main){
                        when(response.status){
                            Status.ERROR->{
                                _errorMessage.value=response.message!!
                                _errorData.value=true
                                _loading.value=false
                            }
                            Status.SUCCESS->{
                                response.data?.let {
                                    _productList.value=it
                                    _success.value=true
                                    _loading.value=false
                                }
                            }
                            else->{
                                _errorMessage.value="Error"
                                _loading.value=false
                                _errorData.value=true
                            }
                        }
                    }
                }
            }
            else{
                job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                    val response=searchRepo.getProductWithOnlyMinPriceAndCat(minPrice,categoryId)
                    withContext(Dispatchers.Main){
                        when(response.status){
                            Status.ERROR->{
                                _errorMessage.value=response.message!!
                                _errorData.value=true
                                _loading.value=false
                            }
                            Status.SUCCESS->{
                                response.data?.let {
                                    _productList.value=it
                                    _success.value=true
                                    _loading.value=false
                                }
                            }
                            else->{
                                _errorMessage.value="Error"
                                _loading.value=false
                                _errorData.value=true
                            }
                        }
                    }
                }
            }
        }catch (e:Exception){
            _errorMessage.value=e.localizedMessage
            _loading.value=false
            _errorData.value=true
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}