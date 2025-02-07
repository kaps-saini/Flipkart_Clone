package com.example.flipkartclone.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.CartItem
import com.example.flipkartclone.data.user.OrderedItemPref
import com.example.flipkartclone.data.user.UserCartItemsPref
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.ActivityOrderSummaryBinding
import com.example.flipkartclone.domain.models.Item
import com.example.flipkartclone.domain.models.user.OrderedItems
import com.example.flipkartclone.helper.Helpers
import com.example.flipkartclone.utils.DestinationName
import com.example.flipkartclone.utils.PaymentStatus
import com.example.flipkartclone.utils.Util.DESTINATION_ID
import com.example.flipkartclone.utils.Util.PAYMENT_API_KEY
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class OrderSummaryActivity : AppCompatActivity(),PaymentResultWithDataListener {

    private lateinit var btnContinueSummary:Button
    private lateinit var ivBackSummary:ImageView
    private lateinit var tvItemsPriceCountSummary:TextView
    private lateinit var tvItemPriceSummary:TextView
    private lateinit var tvDiscountSummary:TextView
    private lateinit var tvTotalAmountSummary:TextView
    private lateinit var tvTotalMRPSummary:TextView
    private lateinit var tvTotalPriceSummary:TextView
    private lateinit var tvSaveOnOrderSummary:TextView
    private lateinit var tvAddressTitle:TextView
    private lateinit var tvAddressCategory:TextView
    private lateinit var rvOrders:RecyclerView
    private lateinit var nesSv: NestedScrollView
    private lateinit var pbSummary: ProgressBar
    private lateinit var constraintLayout4: ConstraintLayout
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val orderId =  Helpers.generateOrderId()

    @Inject
    lateinit var userCartItemsPref: UserCartItemsPref

    @Inject
    lateinit var userDetailsPref: UserDetailsPref

    @Inject
    lateinit var userOrderedItemPref: OrderedItemPref
    private val viewModel by viewModels<FlipkartCloneViewModel>()
    private lateinit var cartItem: CartItem

    private var totalItemPrice = 0
    private var totalDeliveryCharge = 0
    private var totalDiscount = 0
    private var totalAmount = 0
    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityOrderSummaryBinding>(this, R.layout.activity_order_summary)

        initializeViews()
        Checkout.preload(applicationContext)

        cartItem = CartItem(
            onClick = { position, itemData ->
                userCartItemsPref.deleteCartItem(itemData)
                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show()
                viewModel.getItemsInCart()
            },
            onQuantityChange = { itemData ->
                quantity = itemData.quantity
            }
        )

        if (firebaseAuth.currentUser != null){
            if (userDetailsPref.getAddressList().isNotEmpty()) {
                tvAddressTitle.text = userDetailsPref.getAddressList()[0].name
                tvAddressCategory.text = userDetailsPref.getAddressList()[0].type
            } else {
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra(DESTINATION_ID,DestinationName.ADDRESS_LAYOUT.name)
                startActivity(intent)
            }
        }else{
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra(DESTINATION_ID,DestinationName.AUTH_LAYOUT.name)
            startActivity(intent)
        }

        setupObservers()
        userEvents()
    }


    private fun userEvents() {
        btnContinueSummary.setOnClickListener {
            startPayment()
        }
        ivBackSummary.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            //intent.putExtra(DESTINATION_ID,DestinationName.CART_LAYOUT.name)
//            startActivity(intent)
            //findNavController().navigateUp()
            finish()
        }
    }

    private fun setupObservers() {
        setupCartRv()
        viewModel.getItemsInCart()
        viewModel.cartItemsData.observe(this) { data ->
            // Reset totals before recalculating
            totalItemPrice = 0
            totalDiscount = 0
            totalAmount = 0


            cartItem.differ.submitList(data)

            if (!data.isNullOrEmpty()) {
                hideProgressBar()

                data.forEach { item ->
                    val price = item.pricing?.mrp ?: 0
                    val discountPercent = item.pricing?.discount ?: 0
                    val sellingPrice = item.pricing?.sellingPrice ?: 0

                    totalItemPrice += price * quantity
                    totalAmount += sellingPrice * quantity
                    totalDiscount += (price * discountPercent) / 100
                }
            } else {
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra(DESTINATION_ID,DestinationName.CART_LAYOUT.name)
                startActivity(intent)
                //showProgressBar()
            }

            // Update the UI with the calculated values
           tvItemsPriceCountSummary.text = "Price ${data?.size ?: 0} item"
            tvItemPriceSummary.text = "₹$totalItemPrice"
            tvDiscountSummary.text = "-₹$totalDiscount"
           tvTotalAmountSummary.text = "₹$totalAmount"
            tvTotalMRPSummary.text = "₹$totalItemPrice"
            tvTotalPriceSummary.text = "₹$totalAmount"
            tvSaveOnOrderSummary.text = "You will save ₹$totalDiscount on this order"
        }
    }

    private fun setupCartRv() {
        rvOrders.apply {
            this.adapter = cartItem
            this.layoutManager = LinearLayoutManager(
                this.context,
                LinearLayoutManager.VERTICAL, false
            )
        }
    }

    private fun showProgressBar() {
        nesSv.visibility = View.GONE
        pbSummary.visibility = View.VISIBLE
        constraintLayout4.visibility = View.GONE
    }

    private fun hideProgressBar() {
        nesSv.visibility = View.VISIBLE
        pbSummary.visibility = View.GONE
        constraintLayout4.visibility = View.VISIBLE
    }

    private fun startPayment() {
        // Initialize the Checkout object
        val checkout = Checkout()
        checkout.setKeyID(PAYMENT_API_KEY) // Use your Razorpay Key ID here
        // Set the required options
        try {
            val options = JSONObject()
            userCartItemsPref.getCartList().forEach {
                options.put("name", it.item?.name)
                options.put("description", it.item?.description)
            }
            options.put("currency", "INR") // Use the desired currency
            options.put("amount", totalAmount * 100) // Amount in paise (e.g. INR 500)
            options.put("app_name", "Flipkart Clone")
         //   options.put("order_id", "001")
            options.put("date", Helpers.getCurrentDateTime())

            val prefill = JSONObject()
            val userAddress = with(userDetailsPref.getAddressList()[0]) {
                "$houseNo $area $city $state $pincode"
            }
            prefill.put("address", userAddress)
            prefill.put("contact", userDetailsPref.getAddressList()[0].phone)
            options.put("prefill", prefill)
            checkout.open(this, options)
        } catch (e: Exception) {
            Log.e("PaymentActivity", "Error in starting Razorpay Checkout", e)
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val userOrderList = userCartItemsPref.getCartList()
        val userAddress = with(userDetailsPref.getAddressList()[0]) {
            "$houseNo $area $city $state $pincode"
        }
        val orderItems = OrderedItems(
            orderId,
            p0.toString(),
            userOrderList[0].item,
            totalItemPrice,
            userOrderList[0].ratings,
            quantity,
            totalAmount,
            totalDiscount,
            userAddress,
            p1?.userContact.toString()
        )
        Log.i("pay", orderItems.toString())
        Log.i("pay", p0.toString())
        Log.i("pay", p1.toString())
        Log.i("pay", p1?.data.toString())
        Log.i("pay", p1?.orderId.toString())
        userOrderedItemPref.addOrderedItem(orderItems)
        userCartItemsPref.clearCartItems()
        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra(DESTINATION_ID,DestinationName.MY_ORDERS.name)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK,)
        startActivity(intent)
       // findNavController().navigate(R.id.action_orderSummary_to_payment)

    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Helpers.makeSnackBar(View(this), "Payment failed")
        Log.i("pay", p2.toString())
    }

    private fun initializeViews() {
        tvAddressTitle = findViewById(R.id.tvAddressTitle)
        tvAddressCategory = findViewById(R.id.tvAddressCategory)
        btnContinueSummary = findViewById(R.id.btnContinueSummary)
        ivBackSummary = findViewById(R.id.ivBackSummary)
        tvItemsPriceCountSummary = findViewById(R.id.tvItemsPriceCountSummary)
        tvItemPriceSummary = findViewById(R.id.tvItemPriceSummary)
        tvDiscountSummary = findViewById(R.id.tvDiscountSummary)
        tvTotalAmountSummary = findViewById(R.id.tvTotalAmountSummary)
        tvTotalMRPSummary = findViewById(R.id.tvTotalMRPSummary)
        tvTotalPriceSummary = findViewById(R.id.tvTotalPriceSummary)
        tvSaveOnOrderSummary = findViewById(R.id.tvSaveOnOrderSummary)
        rvOrders = findViewById(R.id.rvOrders)
        nesSv = findViewById(R.id.nesSv)
        pbSummary = findViewById(R.id.pbSummary)
        constraintLayout4 = findViewById(R.id.constraintLayout4)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra(DESTINATION_ID,DestinationName.CART_LAYOUT.name)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK,)
        startActivity(intent)
    }

}