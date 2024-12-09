package com.example.flipkartclone.presentation.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flipkartclone.R
import com.example.flipkartclone.data.user.OrderedItemPref
import com.example.flipkartclone.data.user.UserCartItemsPref
import com.example.flipkartclone.helper.Helpers
import com.example.flipkartclone.utils.PaymentStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
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

//    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
//
////        Log.i(TAG, p1?.data?.get("amount").toString())
////        Log.i(TAG, p1?.data?.get("name").toString())
////        Log.i(TAG, p1?.data?.get("date").toString())
//        PaymentStatus.isPaymentCompleted?.invoke(true,p1)
//    }

//    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
//        Log.i("pay",p1.toString())
//        //Toast.makeText(this,"Payment failed",Toast.LENGTH_SHORT).show()
//       // navController.navigate(R.id.orderSummary)
//        PaymentStatus.isPaymentCompleted?.invoke(false,p2)
//    }
//
    override fun onDestroy() {
        super.onDestroy()
        PaymentStatus.isPaymentCompleted = null
    }
}