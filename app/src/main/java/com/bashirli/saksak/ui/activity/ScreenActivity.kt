package com.bashirli.saksak.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.NavUtils
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bashirli.saksak.R
import com.bashirli.saksak.databinding.ActivityScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenActivity : AppCompatActivity() {
    private lateinit var binding:ActivityScreenBinding
    private lateinit var navController:NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navFragment= supportFragmentManager
                .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController=navFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()||super.onSupportNavigateUp()
    }


}