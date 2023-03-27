package com.bashirli.saksak.ui.activity

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bashirli.saksak.R
import com.bashirli.saksak.databinding.ActivityAccountBinding
import com.bashirli.saksak.ui.fragment.account.MainAccFragmentDirections
import com.bashirli.saksak.util.OnBackPressedFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountActivity : AppCompatActivity() {

    @Inject
    lateinit var auth:FirebaseAuth

    private lateinit var binding:ActivityAccountBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val  cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm.activeNetworkInfo==null){
            val alert= AlertDialog.Builder(this@AccountActivity)
            alert.setTitle(R.string.attention).setMessage(R.string.internetProblem)
            alert.setCancelable(false).setNegativeButton(R.string.ok){
                    dialog,which->
                android.os.Process.killProcess(android.os.Process.myPid())
                finishAffinity()
            }.create().show()
            return
        }


    }



}