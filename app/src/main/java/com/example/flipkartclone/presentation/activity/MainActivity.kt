package com.example.flipkartclone.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flipkartclone.R
import com.example.flipkartclone.data.user.UserCartItemsPref
import com.example.flipkartclone.utils.DestinationName
import com.example.flipkartclone.utils.PaymentStatus
import com.example.flipkartclone.utils.Util
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private  lateinit var btmNav: BottomNavigationView
    private lateinit var navController: NavController
    @Inject
    lateinit var userCartItemsPref:UserCartItemsPref


    private val TAG = "Payment"

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
        
        btmNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> {
                    btmNav.visibility = View.VISIBLE
                    if (navController.currentDestination?.id != R.id.dashboard) {
                        navController.popBackStack(R.id.dashboard, false) // Clears stack up to Dashboard
                    }
                    true
                }
                R.id.explore, R.id.categories, R.id.account, R.id.cart -> {
                    btmNav.visibility = View.VISIBLE
                    navController.navigate(item.itemId)
                    true
                }
                else -> false
            }
        }


        val intent = intent.getStringExtra(Util.DESTINATION_ID)
        when (intent) {
            DestinationName.MY_ORDERS.name -> {
                navController.navigate(R.id.myOrders)
            }
            DestinationName.CART_LAYOUT.name -> {
                navController.navigate(R.id.cart)
            }
            DestinationName.ADDRESS_LAYOUT.name -> {
                navController.navigate(R.id.savedAddress)
            }
            DestinationName.AUTH_LAYOUT.name -> {
                navController.navigate(R.id.authBottomSheet)
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

    override fun onDestroy() {
        super.onDestroy()
        PaymentStatus.isPaymentCompleted = null
    }
}