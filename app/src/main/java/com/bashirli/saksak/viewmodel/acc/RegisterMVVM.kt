package com.bashirli.saksak.viewmodel.acc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterMVVM @Inject
constructor(
    private var auth:FirebaseAuth,
    private var firestore:FirebaseFirestore
) : ViewModel() {

    private var _loading=MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
    get()=_loading

    private var _errorData=MutableLiveData<Boolean>()
    val errorData:LiveData<Boolean>
        get()=_errorData

    private var _success=MutableLiveData<Boolean>()
    val success:LiveData<Boolean>
        get()=_success

    private var _errorMessage=MutableLiveData<String>()
    val errorMessage:LiveData<String>
    get()=_errorMessage



     fun register(email:String,password:String,nickname:String){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value=""
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
           createDatabaseForUser(email,nickname)
        }.addOnFailureListener {
            _errorMessage.value=it.localizedMessage
            _errorData.value=true
            _loading.value=false
        }
    }
    private fun createDatabaseForUser(email: String,nickname: String){
        val userData=HashMap<String,Any>()
        userData.put("email",email)
        userData.put("date",FieldValue.serverTimestamp())
        userData.put("profilePicture","null")
        userData.put("nickname",nickname)
        firestore.collection("UserData").document(email).set(userData)
            .addOnFailureListener {
                _loading.value=false
                _errorMessage.value=it.localizedMessage
                _errorData.value=true
            }.addOnSuccessListener {
                _loading.value=false
                _success.value=true
            }

    }

}