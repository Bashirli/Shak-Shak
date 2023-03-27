package com.bashirli.saksak.viewmodel.acc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bashirli.saksak.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileMVVM @Inject
constructor(
    private val auth:FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _loading=MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
    get()=_loading

    private val _errorMessage=MutableLiveData<String>()
    val errorMessage:LiveData<String>
        get()=_errorMessage

    private val _errorData=MutableLiveData<Boolean>()
    val errorData:LiveData<Boolean>
        get()=_errorData

    private val _success=MutableLiveData<Boolean>()
    val success:LiveData<Boolean>
        get()=_success

    private val _userData=MutableLiveData<UserData>()
    val userData:LiveData<UserData>
    get()=_userData

    private val _loadSuccess=MutableLiveData<Boolean>()
    val loadSuccess:LiveData<Boolean>
    get()=_loadSuccess

    private val _loadError=MutableLiveData<Boolean>()
    val loadError:LiveData<Boolean>
    get()=_loadError


    fun updateData(nickname:String){
    _loading.value=true
        _success.value=false
        _errorData.value=false
        firestore.collection("UserData")
        .document(auth.currentUser!!.email!!)
        .update("nickname",nickname)
        .addOnFailureListener {
            _errorMessage.value=it.localizedMessage
            _errorData.value=true
            _loading.value=false
        }.addOnSuccessListener {
                _success.value=true
                _loading.value=false
        }
    }

    fun loadData(){
        _loadError.value=false
        _loadSuccess.value=false
        firestore.collection("UserData")
            .document(auth.currentUser!!.email!!).addSnapshotListener { value, error ->
                if(error!=null){
                    _errorMessage.value=error.localizedMessage
                    _loadError.value=true
                return@addSnapshotListener
                }
                if(value!=null){
                    val email = value.get("email") as String
                    val nickname = value.get("nickname") as String
                    val profilePicture = value.get("profilePicture") as String
                    val data=UserData(email, nickname, profilePicture)
                    _userData.value=data
                    _loadSuccess.value=true

                }else{
                    _errorMessage.value="Error"
                    _loadError.value=true
                    return@addSnapshotListener
                }
            }


    }

}