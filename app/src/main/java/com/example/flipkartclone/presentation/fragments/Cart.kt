package com.example.flipkartclone.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.CartItem
import com.example.flipkartclone.adapter.RecentlyViewedItems
import com.example.flipkartclone.data.user.UserCartItemsPref
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.FragmentCartBinding
import com.example.flipkartclone.databinding.FragmentCategoriesBinding
import com.example.flipkartclone.domain.models.user.CartItems
import com.example.flipkartclone.helper.Helpers
import com.example.flipkartclone.utils.Resource
import com.example.flipkartclone.utils.Status
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class Cart : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userCartItemsPref: UserCartItemsPref
    @Inject
    lateinit var userDetailsPref: UserDetailsPref
    private val viewModel by viewModels<FlipkartCloneViewModel>()
    private lateinit var cartItem: CartItem
    private val auth = FirebaseAuth.getInstance()
    private lateinit var recentlyViewedItems: RecentlyViewedItems

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
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false)

        if (auth.currentUser != null){
            binding.clAddressCartLayout.visibility = View.VISIBLE
            binding.btnLoginCart.visibility = View.GONE
        }else{
            binding.clAddressCartLayout.visibility = View.GONE
            binding.btnLoginCart.visibility = View.VISIBLE
        }

        binding.btnLoginCart.setOnClickListener {
            findNavController().navigate(R.id.action_cart_to_authBottomSheet)
        }

        cartItem = CartItem(
            onClick = {position, itemData ->
                userCartItemsPref.deleteCartItem(itemData)
                Toast.makeText(requireContext(),"Item removed",Toast.LENGTH_SHORT).show()
                viewModel.getItemsInCart()
            },
            onQuantityChange = {itemData ->
                quantity = itemData.quantity
            }
        )

        recentlyViewedItems = RecentlyViewedItems(
            onClick = { position, itemData ->
                val action = CartDirections.actionCartToItemDetails(itemData)
                findNavController().navigate(action)
            }
        ) { position, itemData ->
            val cartItemData = CartItems(itemData.item, itemData.pricing, itemData.ratings, 1)
            userCartItemsPref.addCartItem(cartItemData)
            viewModel.getItemsInCart()

            Snackbar.make(requireView(), "Added to cart", Snackbar.LENGTH_SHORT)
                .setAction("Undo") { click ->
                    click.setOnClickListener {
                        userCartItemsPref.deleteCartItem(cartItemData)
                        viewModel.getItemsInCart()
                    }
                }.show()
        }

        setupCartRv()
        setupRecentlyViewedRv()
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
                hideEmptyCart()

                data.forEach { item ->
                    val price = item.pricing?.mrp ?: 0
                    val discountPercent = item.pricing?.discount ?: 0
                    val sellingPrice = item.pricing?.sellingPrice ?: 0

                    totalItemPrice += price * quantity
                    totalAmount += sellingPrice * quantity
                    totalDiscount += (price * discountPercent) / 100
                }
            } else {
                showEmptyCart()
            }

            // Update the UI with the calculated values
            binding.textView12.text = "Price ${data?.size ?: 0} item"
            binding.tvItem1Price.text = "₹$totalItemPrice"
            binding.tvDiscountValue.text = "-₹$totalDiscount"
            binding.tvTotalAmountValue.text = "₹$totalAmount"
            binding.tvTotalMrp.text = "₹$totalItemPrice"
            binding.tvTotalPrice.text = "₹$totalAmount"
            binding.tvSaveOnOrder.text = "You will save ₹$totalDiscount on this order"
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.droppedItems.collect { response ->
                    when (response) {
                        is Resource.Error -> {
                            showRecentlyViewPB()
                            binding.appbar.visibility = View.GONE
                            if (response.message?.contains(Status.NoInternet.toString()) == true){
                                Helpers.makeSnackBar(requireView(),"No Internet")
                            }else{
                                Helpers.makeSnackBar(requireView(), response.message.toString())
                                Log.e("dashboard",response.message.toString())
                            }
                        }
                        is Resource.Loading -> {
                            showRecentlyViewPB()
                        }
                        is Resource.Success -> {
                            showRecentlyViewRv()
                            recentlyViewedItems.differ.submitList(response.data)
                        }
                    }
                }
            }
        }

        binding.tvAddressTitle.text = userDetailsPref.getAddressList()[0].name
        binding.tvAddressCategory.text = userDetailsPref.getAddressList()[0].type

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun setupCartRv(){
        binding.rvCartItems.apply {
            this.adapter = cartItem
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun setupRecentlyViewedRv(){
        binding.rvRecentlyViewed.apply {
            this.adapter = recentlyViewedItems
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun showEmptyCart(){
        binding.llNoItemInCart.visibility = View.VISIBLE
        binding.clCartView.visibility = View.GONE
    }

    private fun hideEmptyCart(){
        binding.llNoItemInCart.visibility = View.GONE
        binding.clCartView.visibility = View.VISIBLE
    }

    private fun showRecentlyViewPB(){
        binding.rvRecentlyViewed.visibility = View.GONE
        binding.pbCartRecentlyViewed.visibility = View.VISIBLE
    }

    private fun showRecentlyViewRv(){
        binding.rvRecentlyViewed.visibility = View.VISIBLE
        binding.pbCartRecentlyViewed.visibility = View.GONE
    }

}