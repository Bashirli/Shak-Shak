package com.bashirli.saksak.util

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bashirli.saksak.R

const val BASE_URL="xxx"
const val PRODUCTS="xxx"
const val CATEGORY="xxx"


class CustomProgressBar(var context: Context){
    private  val  progressDialog=ProgressDialog(context)
    fun stopBar(){
        progressDialog.cancel()
    }
    fun startBar(){
        progressDialog.setCancelable(false)
        progressDialog.setTitle(R.string.attention)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

    }
}

@BindingAdapter("app:downloadImage")
fun download(view:ImageView,url:String){
    view.setImageURL(view.context,url)
}
