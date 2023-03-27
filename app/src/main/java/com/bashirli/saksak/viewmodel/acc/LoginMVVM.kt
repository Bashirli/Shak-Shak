package com.bashirli.saksak.viewmodel.acc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginMVVM @Inject constructor(
    private var auth: FirebaseAuth
) : ViewModel() {

    private var _loading= MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get()=_loading

    private var _errorData= MutableLiveData<Boolean>()
    val errorData: LiveData<Boolean>
        get()=_errorData

    private var _success= MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get()=_success

    private var _errorMessage= MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get()=_errorMessage



    fun login(email:String,password:String){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value=""
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _success.value=true
            _loading.value=false
        }.addOnFailureListener {
            _errorMessage.value=it.localizedMessage
            _errorData.value=true
            _loading.value=false
        }
    }


}