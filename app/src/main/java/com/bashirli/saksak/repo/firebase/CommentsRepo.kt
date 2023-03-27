package com.bashirli.saksak.repo.firebase

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.bashirli.saksak.model.CommentModel
import com.bashirli.saksak.model.CommentResultModel
import com.bashirli.saksak.model.FirestoreResultModel
import com.bashirli.saksak.util.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.reflect.Field

class CommentsRepo(
    private val context: Context,
    private val auth:FirebaseAuth,
    private val firestore: FirebaseFirestore
) : CommentsInterface{

    private val commentResultModel=MutableLiveData<CommentResultModel>()
    private val uploadData=MutableLiveData<FirestoreResultModel>()

    override suspend fun getComments(id:Int): CommentResultModel? {
    return try {
        val arrayList=ArrayList<CommentModel>()
        firestore.collection("Comments")
            .document(id.toString())
            .collection("comments")
            .get()
            .addOnSuccessListener {data->
               for(it in data){
                   val header=it.get("header") as String
                   val mainText=it.get("mainText") as String
                   val email=it.get("email") as String
                   val rating=it.get("rating") as String
                   val timestampDate=it.get("date") as Timestamp
                   val date=android.text.format.DateFormat.getDateFormat(context).format(timestampDate.toDate()).toString()
                   val commentModel=CommentModel(header, mainText, email,rating, date)
                   arrayList.add(commentModel)
               }

                commentResultModel.value=CommentResultModel(arrayList,true,"suc")
            }.await()

            commentResultModel.value
    }   catch (e:Exception){
        commentResultModel.value=CommentResultModel(null,false,e.localizedMessage)
        commentResultModel.value

    }
    }

    override suspend fun uploadComment(id: Int, data: CommentModel): FirestoreResultModel? {
        return try {
            val hashMap=HashMap<String,Any>()
            hashMap.put("email",data.email)
            hashMap.put("header",data.header)
            hashMap.put("mainText",data.mainText)
            hashMap.put("date",FieldValue.serverTimestamp())
            hashMap.put("rating",data.rating)
            firestore.collection("Comments")
                .document(id.toString())
                .collection("comments").add(hashMap).addOnSuccessListener{
                uploadData.value=FirestoreResultModel(true,"suc")
                }.await()
            uploadData.value
        }catch (e:Exception){
            uploadData.value= FirestoreResultModel(false,e.localizedMessage)
            uploadData.value
        }
    }

}