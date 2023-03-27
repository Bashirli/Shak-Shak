package com.bashirli.saksak.viewmodel.acc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPassMVVM @Inject constructor(
    private var auth: FirebaseAuth
) :ViewModel() {

    private val _loading=MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
    get()=_loading

    private val _success=MutableLiveData<Boolean>()
    val success:LiveData<Boolean>
        get()=_success

    private val _errorData=MutableLiveData<Boolean>()
    val errorData:LiveData<Boolean>
        get()=_errorData

    private val _errorMessage=MutableLiveData<String>()
    val errorMessage:LiveData<String>
        get()=_errorMessage


    fun sendEmailLink(email:String){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value=""
        try{

            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                _loading.value=false
                    _success.value=true
            }.addOnFailureListener {
                    _loading.value=false
                    _errorMessage.value=it.localizedMessage
                    _errorData.value=true
            }

        }catch (e:Exception){
            _loading.value=false
            _errorMessage.value=e.localizedMessage
            _errorData.value=true
        }


    }


}