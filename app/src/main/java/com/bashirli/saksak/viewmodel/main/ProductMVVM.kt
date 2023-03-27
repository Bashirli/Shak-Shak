package com.bashirli.saksak.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bashirli.saksak.model.CommentModel
import com.bashirli.saksak.model.CommentResultModel
import com.bashirli.saksak.model.FirestoreResultModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.model.db.ItemModel
import com.bashirli.saksak.repo.api.Repo
import com.bashirli.saksak.repo.firebase.CommentsInterface
import com.bashirli.saksak.repo.firebase.CommentsRepo
import com.bashirli.saksak.repo.room.FavRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ProductMVVM @Inject
constructor(
    private var commentsRepo: CommentsInterface,
    private var repo: Repo,
    private var auth: FirebaseAuth,
    private var roomRepo:FavRepo
) : ViewModel() {
    private var _loading=MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> get()=_loading

    private var _success=MutableLiveData<Boolean>()
    val success:LiveData<Boolean> get()=_success

    private var _errorData=MutableLiveData<Boolean>()
    val errorData:LiveData<Boolean> get()=_errorData

    private var _productData=MutableLiveData<ProductModel>()
    val productData:LiveData<ProductModel> get()=_productData

    private var _commentData=MutableLiveData<CommentResultModel?>()
    val commentData:LiveData<CommentResultModel?> get()=_commentData

    private var _uploadCommentData=MutableLiveData<FirestoreResultModel?>()
    val uploadCommentData:LiveData<FirestoreResultModel?> get()=_uploadCommentData

    var errorMessage:String?=null

    private val exceptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        _loading.value=false
        errorMessage=throwable.localizedMessage
        _errorData.value=true
    }

    var checkIsOnFav:Boolean?=false
    private set

    private var job: Job?=null

    private fun checkIsOnFav(myData:ProductModel){
       try {
           job=CoroutineScope(Dispatchers.IO).launch {
            val response=roomRepo.getAllData()
               withContext(Dispatchers.Main){
                   for(dbdata in response){

                       if(dbdata.productId==myData.id){
                           checkIsOnFav=true
                           break
                       }else{
                           checkIsOnFav=false
                       }
                   }
                   _success.value=true
                   _loading.value=false
               }

           }
       }catch (e:Exception){
           println(e.localizedMessage)
           checkIsOnFav=null
       }
    }

    fun loadData(id:Int){
        _success.value=false
        _errorData.value=false
        _loading.value=true
        errorMessage=null
        try {
            job=CoroutineScope(Dispatchers.IO+exceptionHandler)
                .launch {
                var response=repo.getSingle(id)
                    withContext(Dispatchers.Main){
                        response.data?.let {
                            data->
                            _productData.value=data
                            checkIsOnFav(data)
                            return@withContext
                        }

                    }
            }


        }catch (e:Exception){
            _loading.value=false
            errorMessage=e.localizedMessage
            _errorData.value=true
        }

    }

    fun deleteFav(){
        try {
            job=CoroutineScope(Dispatchers.IO).launch {
                productData.value?.let {
                    roomRepo.deleteIfNotWorks(it.id)
                }
            }
        }catch (e:Exception){

        }
    }

    fun insertFav(){
        try {
            job=CoroutineScope(Dispatchers.IO).launch {
                productData.value?.let {
                    val data=ItemModel(
                        it.id,
                        it.title
                    )
                    roomRepo.insertData(data)
                }
            }
        }catch (e:Exception){  }
    }

    fun loadComments(id:Int){
        viewModelScope.launch {
           val myData=commentsRepo.getComments(id)
           _commentData.value=myData
       }
    }

    fun isLoged():Boolean{
         auth.currentUser?.let {
         if(it.isAnonymous){
             return false
         }else{
             return true
         }
        }?: return false
    }

    fun uploadComment(id:Int,header:String,mainText:String,rating:String){
        val data=CommentModel(header,mainText,auth.currentUser!!.email!!,rating,null)
        viewModelScope.launch {
            val updateData=commentsRepo.uploadComment(id, data)
            _uploadCommentData.value=updateData
        }
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}