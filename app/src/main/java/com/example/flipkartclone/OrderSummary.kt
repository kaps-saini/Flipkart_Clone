package com.example.flipkartclone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.system.SystemCleaner
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flipkartclone.adapter.CartItem
import com.example.flipkartclone.data.user.UserCartItemsPref
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.FragmentOrderSummaryBinding
import com.example.flipkartclone.helper.Helpers
import com.example.flipkartclone.utils.Util.PAYMENT_API_KEY
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class OrderSummary : Fragment() {

    private var _binding: FragmentOrderSummaryBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userCartItemsPref: UserCartItemsPref
    @Inject
    lateinit var userDetailsPref: UserDetailsPref
    private val viewModel by viewModels<FlipkartCloneViewModel>()
    private lateinit var cartItem: CartItem
    private val auth = FirebaseAuth.getInstance()

    private var totalItemPrice = 0
    private var totalDeliveryCharge = 0
    private var totalDiscount = 0
    private var totalAmount = 0
    private var quantity = 1

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderSummaryBinding.inflate(inflater, container, false)

        Checkout.preload(this.requireActivity().applicationContext)

        cartItem = CartItem(
            onClick = {position, itemData ->
                userCartItemsPref.deleteCartItem(itemData)
                Toast.makeText(requireContext(),"Item removed", Toast.LENGTH_SHORT).show()
                viewModel.getItemsInCart()
            },
            onQuantityChange = {itemData ->
                quantity = itemData.quantity
            }
        )

        if (userDetailsPref.getAddressList().isNotEmpty()){
            binding.tvAddressTitle.text = userDetailsPref.getAddressList()[0].name
            binding.tvAddressCategory.text = userDetailsPref.getAddressList()[0].type
        }else{
            findNavController().navigate(R.id.action_orderSummary_to_savedAddress)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        userEvents()
    }

    private fun userEvents() {
        binding.btnContinueSummary.setOnClickListener {
            findNavController().navigate(R.id.action_orderSummary_to_payment)
            startPayment()
        }
    }

    private fun setupObservers() {
        setupCartRv()
        viewModel.getItemsInCart()
        viewModel.cartItemsData.observe(viewLifecycleOwner) { data ->
            // Reset totals before recalculating
            totalItemPrice = 0
            totalDiscount = 0
            totalAmount = 0

            cartItem.differ.submitList(data)
            Log.i("cart", data.toString())
            Log.i("cart", data?.size.toString())

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
                showProgressBar()
            }

            // Update the UI with the calculated values
            binding.tvItemsPriceCountSummary.text = "Price ${data?.size ?: 0} item"
            binding.tvItemPriceSummary.text = "₹$totalItemPrice"
            binding.tvDiscountSummary.text = "-₹$totalDiscount"
            binding.tvTotalAmountSummary.text = "₹$totalAmount"
            binding.tvTotalMRPSummary.text = "₹$totalItemPrice"
            binding.tvTotalPriceSummary.text = "₹$totalAmount"
            binding.tvSaveOnOrderSummary.text = "You will save ₹$totalDiscount on this order"
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun setupCartRv(){
        binding.rvOrders.apply {
            this.adapter = cartItem
            this.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun showProgressBar(){
        binding.nesSv.visibility = View.GONE
        binding.pbSummary.visibility = View.VISIBLE
        binding.constraintLayout4.visibility = View.GONE
    }

    private fun hideProgressBar(){
        binding.nesSv.visibility = View.VISIBLE
        binding.pbSummary.visibility = View.GONE
        binding.constraintLayout4.visibility = View.VISIBLE
    }

    private fun startPayment() {
        // Initialize the Checkout object
        val checkout = Checkout()
        checkout.setKeyID(PAYMENT_API_KEY) // Use your Razorpay Key ID here

        // Set the required options
        try {
            val options = JSONObject()
            options.put("name", userDetailsPref.getUserDetails()?.firstName)
            options.put("description", userCartItemsPref.getCartList()[0].item?.name)
            options.put("currency", "INR") // Use the desired currency
            options.put("amount", totalAmount * 100) // Amount in paise (e.g. INR 500)
            options.put("app_name","Flipkart Clone")
            options.put("order_Id","001")
            options.put("date",Helpers.getCurrentDateTime())

            val prefill = JSONObject()
            val userAddress = with(userDetailsPref.getAddressList()[0]) {
                "$houseNo $area $city $state $pincode"
            }
            prefill.put("address", userAddress)
            prefill.put("contact", userDetailsPref.getAddressList()[0].phone)

            options.put("prefill", prefill)
            checkout.open(requireActivity(), options)
        } catch (e: Exception) {
            Log.e("PaymentActivity", "Error in starting Razorpay Checkout", e)
        }
    }

//    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
//        findNavController().navigate(R.id.action_orderSummary_to_payment)
//        Log.i("pay",p1.toString())
//    }
//
//    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
//        Helpers.makeSnackBar(requireView(),"Payment failed")
//        Log.i("pay",p2.toString())
//    }

}