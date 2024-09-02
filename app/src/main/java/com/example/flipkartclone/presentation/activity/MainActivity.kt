package com.example.flipkartclone.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flipkartclone.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private  lateinit var btmNav: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btmNav = findViewById(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        btmNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
                R.id.dashboard, R.id.explore, R.id.categories, R.id.account, R.id.cart -> {
                    btmNav.visibility = View.VISIBLE
                }
                else -> {
                    btmNav.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private val bottomNavigationFragments = setOf(
        R.id.dashboard,
        R.id.explore,
        R.id.categories,
        R.id.account,
        R.id.cart
    )

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (navController.currentDestination?.id in bottomNavigationFragments) {
            finish()
        }else{
            // Handle backstack navigation
            if (!navController.popBackStack()) {
                super.onBackPressed()
            }
        }
    }
}