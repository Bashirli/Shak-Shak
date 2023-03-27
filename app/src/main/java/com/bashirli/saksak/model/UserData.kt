package com.bashirli.saksak.model
import java.io.Serializable
data class UserData(
    val email:String,
    val nickname:String,
    val profilePicture:String
) : Serializable