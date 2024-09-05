package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.SavedAddressAdapter
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.FragmentSavedAddressBinding
import com.example.flipkartclone.domain.models.user.Address
import com.example.flipkartclone.vm.FlipkartCloneViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedAddress : Fragment() {

    private var _binding:FragmentSavedAddressBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FlipkartCloneViewModel>()
    @Inject
    lateinit var userDetailsPref: UserDetailsPref
    private lateinit var savedAddressAdapter: SavedAddressAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_saved_address, container, false)

        savedAddressAdapter = SavedAddressAdapter(
            onClick = {position, address ->
                editAddress(address)
            },
            onRemoveClick = {position, address ->
                removeAddress(position,address)
                viewModel.getAddress()
            }
        )

        setupRv()
        viewModel.getAddress()
        viewModel.addressData.observe(viewLifecycleOwner){ data->
            savedAddressAdapter.differ.submitList(data)
        }

        if (userDetailsPref.getAddressList().isEmpty()){
            binding.tvSavedAddress.text = "No address saved yet!!"
        }else{
            val count = userDetailsPref.getAddressList().size
            binding.tvSavedAddress.text = "$count SAVED ADDRESSES"
        }

        binding.ivBackSavedAddress.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvAddNewAddress.setOnClickListener{
            val action = SavedAddressDirections.actionSavedAddressToCreateNewAddress(null)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun removeAddress(position:Int,address: Address) {
        userDetailsPref.deleteAddress(address)
    }

    private fun editAddress(address: Address) {
        val directions = SavedAddressDirections.actionSavedAddressToCreateNewAddress(address)
        findNavController().navigate(directions)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun setupRv(){
        binding.rvSavedAddress.apply {
            this.adapter = savedAddressAdapter
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }

}