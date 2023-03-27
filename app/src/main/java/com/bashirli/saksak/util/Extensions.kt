package com.bashirli.saksak.util

import android.content.Context
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.bundleOf
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bashirli.saksak.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.setImageURL(context: Context, url:String){
    val options= RequestOptions.placeholderOf(placeHolder(context))
        .error(R.drawable.ic_launcher_foreground)
    Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)
}
fun ImageView.setImageProfile(context: Context, url:String){
    val options= RequestOptions.placeholderOf(placeHolder(context))
        .error(R.drawable.profileico)
    Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)
}

    private fun placeHolder(context: Context): CircularProgressDrawable {
    val circularProgressDrawable= CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth=8f
    circularProgressDrawable.centerRadius=40f
    circularProgressDrawable.start()
    return circularProgressDrawable

    }


fun SavingMotionLayout.isAnimEnabled(isEnabled:Boolean){
    if(isEnabled){
        this.definedTransitions.forEach {
            it.isEnabled=true
        }
    }else{
        this.definedTransitions.forEach{
            it.isEnabled=false
        }
    }
}

    fun checkIsEmpty(stringList:ArrayList<String>,context: Context):Boolean{
       for(data in stringList){
           if(data.isEmpty()){
               Toast.makeText(context,R.string.fillTheGaps,Toast.LENGTH_SHORT).show()
               return true
           }
       }
        return true
    }

    fun errorFinderRegister(email:String,password:String,rePass:String,nickname:String,context: Context):Boolean{
        if(email.trim().equals("")||password.trim().equals("")||rePass.trim().equals("")||nickname.trim().equals("")){
            Toast.makeText(context,R.string.fillTheGaps,Toast.LENGTH_SHORT).show()
            return true
        }
        if(nickname.length>25){
            Toast.makeText(context,R.string.longNick,Toast.LENGTH_SHORT).show()
            return true
        }
        if(password.length<6){
            Toast.makeText(context,R.string.passshort,Toast.LENGTH_SHORT).show()
            return true
        }
        if(!password.equals(rePass)){
            Toast.makeText(context,R.string.passequal,Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    fun errorFinderLogin(email:String,password:String,context: Context):Boolean{
        if(email.equals("")||password.equals("")){
            Toast.makeText(context,R.string.fillTheGaps,Toast.LENGTH_SHORT).show()
            return true
        }
        if(password.length<6){
            Toast.makeText(context,R.string.passshort,Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

