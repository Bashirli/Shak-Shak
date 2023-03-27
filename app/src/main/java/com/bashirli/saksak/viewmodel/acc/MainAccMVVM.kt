package com.bashirli.saksak.viewmodel.acc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainAccMVVM @Inject constructor(
    private var auth: FirebaseAuth
) :ViewModel() {

    private val _loading= MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get()=_loading

    private val _success= MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get()=_success

    private val _errorData= MutableLiveData<Boolean>()
    val errorData: LiveData<Boolean>
        get()=_errorData

    private val _errorMessage= MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get()=_errorMessage

    private var isAnonymous=false

    init{
        checkIsAnonymous()
    }


     fun isLoggedIn():Boolean{
        return auth.currentUser?.let {
            !it.isAnonymous
        }?: false
    }

    private fun checkIsAnonymous(){
       if(auth.currentUser!=null){
           isAnonymous=auth.currentUser!!.isAnonymous
       }else{
           isAnonymous=false
       }

    }

    fun signInAsAGuest(){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value=""
        if(isAnonymous){
            _loading.value=false
            _success.value=true
        }else{
            if(auth.currentUser!=null){
                auth.signOut()
            }
            auth.signInAnonymously().addOnFailureListener {
            _loading.value=false
            _errorMessage.value=it.localizedMessage
            _errorData.value=true
            }.addOnSuccessListener {
                _loading.value=false
                _success.value=true
            }
        }

    }


}