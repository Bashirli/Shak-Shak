package com.bashirli.saksak.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bashirli.saksak.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private  var job: Job?=null

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val  cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm.activeNetworkInfo==null){
            val alert=AlertDialog.Builder(this@SplashActivity)
            alert.setTitle(R.string.attention).setMessage(R.string.internetProblem)
            alert.setCancelable(false).setNegativeButton(R.string.ok){
                dialog,which->
                android.os.Process.killProcess(android.os.Process.myPid())
               finishAffinity()
            }.create().show()
            return
        }else{
            if(auth.currentUser==null){
                auth.signInAnonymously()
            }
            job = CoroutineScope(Dispatchers.Main).launch{
                delay(1300)
                startActivity(Intent(this@SplashActivity, ScreenActivity::class.java))
                finish()
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }


}