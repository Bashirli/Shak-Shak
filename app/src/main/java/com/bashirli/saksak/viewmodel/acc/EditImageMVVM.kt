package com.bashirli.saksak.viewmodel.acc

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditImageMVVM @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
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


    private val _errorMessage=MutableLiveData<String>()
    val errorMessage:LiveData<String>
        get()=_errorMessage

    fun updateImage(image: Uri){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        _errorMessage.value=""
        val imageURL="pp/"+UUID.randomUUID()+".jpg"
        storage.reference.child(imageURL).putFile(image)
            .addOnSuccessListener {
            storage.reference.child(imageURL).downloadUrl.addOnSuccessListener {
                val imageURL=it.toString()
                firestore.collection("UserData").document(auth.currentUser!!.email!!)
                    .update("profilePicture",imageURL)
                    .addOnSuccessListener {
                        _loading.value=false
                        _success.value=true
                    }.addOnFailureListener {
                        _loading.value=false
                        _errorMessage.value=it.localizedMessage
                        _errorData.value=true
                    }
            }.addOnFailureListener {
                _loading.value=false
                _errorMessage.value=it.localizedMessage
                _errorData.value=true
            }
        }.addOnFailureListener {
                _loading.value=false
                _errorMessage.value=it.localizedMessage
                _errorData.value=true
        }

    }

    fun deleteImage(){
        _loading.value=true
        _errorData.value=false
        _success.value=false
        firestore.collection("UserData").document(auth.currentUser!!.email!!)
            .update("profilePicture","null")
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