package com.bashirli.saksak.viewmodel.acc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bashirli.saksak.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileMVVM @Inject constructor(
    private val auth:FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _loading=MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
    get()=_loading

    private val _errorData=MutableLiveData<Boolean>()
    val errorData:LiveData<Boolean>
        get()=_errorData

    private val _success=MutableLiveData<Boolean>()
    val success:LiveData<Boolean>
        get()=_success

    private val _loadingSignOut=MutableLiveData<Boolean>()
    val loadingSignOut:LiveData<Boolean>
        get()=_loadingSignOut

    private val _errorDataSignOut=MutableLiveData<Boolean>()
    val errorDataSignOut:LiveData<Boolean>
        get()=_errorDataSignOut

    private val _successSignOut=MutableLiveData<Boolean>()
    val successSignOut:LiveData<Boolean>
        get()=_successSignOut

    private val _errorMessage=MutableLiveData<String>()
    val errorMessage:LiveData<String>
    get()=_errorMessage

    private val _userData=MutableLiveData<UserData>()
    val userData:LiveData<UserData>
        get()=_userData

    fun loadData(){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value=""
        val email=auth.currentUser!!.email!!

        /*
        LiveDataChanges ->

        firestore.collection("UserData").document(email)
            .addSnapshotListener { value, error ->
                if(error!=null){
                    _loading.value=false
                    _errorMessage.value=error.localizedMessage
                    _errorData.value=true
                    return@addSnapshotListener
                }
                if(value!=null){
                    val nickname=value.get("nickname") as String
                    val profilePicture=value.get("profilePicture") as String
                    _userData.value=UserData(email, nickname, profilePicture)
                    _success.value=true
                    _loading.value=false

                }else{
                    _loading.value=false
                    _errorMessage.value="Something went wrong!"
                    _errorData.value=true
                    return@addSnapshotListener
                }
            }

         */

            //  Only Once Data ->
        firestore.collection("UserData")
            .document(email)
            .get()
            .addOnSuccessListener {
                val nickname=it.get("nickname") as String
                val profilePicture=it.get("profilePicture") as String
                _userData.value=UserData(email, nickname, profilePicture)
                _success.value=true
                _loading.value=false
            }.addOnFailureListener {
                _loading.value=false
                _errorMessage.value=it.localizedMessage
                _errorData.value=true
            }

        }

    fun signOut(){
        _loadingSignOut.value=true
        _errorDataSignOut.value=false
        _successSignOut.value=false
        _errorMessage.value=""
        auth.signOut()
            auth.signInAnonymously()
            .addOnSuccessListener {
                _loadingSignOut.value=false
                _successSignOut.value=true
            }.addOnFailureListener {
                    _loadingSignOut.value=false
                    _errorMessage.value=it.localizedMessage
                    _errorDataSignOut.value=true
            }
    }



}