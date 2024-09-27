package com.example.flipkartclone

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.flipkartclone.adapter.CartItem
import com.example.flipkartclone.adapter.RecentlyViewedItems
import com.example.flipkartclone.data.user.OrderedItemPref
import com.example.flipkartclone.data.user.UserCartItemsPref
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.FragmentCartBinding
import com.example.flipkartclone.databinding.FragmentPaymentBinding
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Payment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userOrderedItemPref: OrderedItemPref
    @Inject
    lateinit var userDetailsPref: UserDetailsPref
    private val viewModel by viewModels<FlipkartCloneViewModel>()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var recentlyViewedItems: RecentlyViewedItems

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


}