package com.bashirli.saksak.repo.firebase

import com.bashirli.saksak.model.CommentModel
import com.bashirli.saksak.model.CommentResultModel
import com.bashirli.saksak.model.FirestoreResultModel


interface CommentsInterface {

  suspend  fun getComments(id:Int): CommentResultModel?

  suspend fun uploadComment(id:Int,data:CommentModel):FirestoreResultModel?

}