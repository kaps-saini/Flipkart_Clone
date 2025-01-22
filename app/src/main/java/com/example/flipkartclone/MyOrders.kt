package com.example.flipkartclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flipkartclone.adapter.Orders
import com.example.flipkartclone.data.user.OrderedItemPref
import com.example.flipkartclone.databinding.FragmentMyOrdersBinding
import com.example.flipkartclone.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyOrders : Fragment() {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var orders:Orders

    @Inject
    lateinit var userOrderedItemPref: OrderedItemPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchOrderListData()
    }

    private fun fetchOrderListData() {
        orders = Orders{ position,Item ->
            val direction = MyOrdersDirections.actionMyOrdersToPayment(Item)
            findNavController().navigate(direction)
        }

        binding.rvMyOrders.apply {
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            this.adapter = orders
        }

        orders.differ.submitList(userOrderedItemPref.getOrderedListAsLive())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}