package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.flipkartclone.R
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.FragmentCreateNewAddressBinding
import com.example.flipkartclone.domain.models.user.Address
import com.example.flipkartclone.helper.Helpers
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateNewAddress : Fragment() {

    private var _binding: FragmentCreateNewAddressBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userDetailsPref:UserDetailsPref
    private val args by navArgs<CreateNewAddressArgs>()
    var addressType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_create_new_address, container, false)

        if (args.userAddressSaved != null){
            fetchUserSavedAddress(args.userAddressSaved!!)
        }

        binding.ivBackAddNewAddress.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSave.setOnClickListener{
            saveNewAddress()
            Helpers.makeSnackBar(requireView(),"Address saved successfully")
            findNavController().navigateUp()
        }

        binding.rg.setOnCheckedChangeListener{ rb,rbId->
            val btn = rb.findViewById<RadioButton>(rbId)
            addressType = btn.text.toString()
        }

        return binding.root
    }

    private fun saveNewAddress() {
        val userName = binding.etFullName.text.toString()
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val pinCode = binding.etPinCode.text.toString()
        val state = binding.etState.text.toString()
        val city = binding.etCity.text.toString()
        val area = binding.etArea.text.toString()
        val house = binding.etHouse.text.toString()

        var isAllCorrect = true

        if (userName.isEmpty()){
            isAllCorrect = false
            binding.tilFullName.boxStrokeErrorColor = ContextCompat.getColorStateList(requireContext(),
                R.color.red
            )
        }

        if (phoneNumber.isEmpty()){
            isAllCorrect = false
            binding.tilPhoneNumber.boxStrokeErrorColor = ContextCompat.getColorStateList(requireContext(),
                R.color.red
            )
        }

        if (pinCode.isEmpty()){
            isAllCorrect = false
            binding.tilPinCode.boxStrokeErrorColor = ContextCompat.getColorStateList(requireContext(),
                R.color.red
            )
        }

        if (state.isEmpty()){
            isAllCorrect = false
            binding.tilState.boxStrokeErrorColor = ContextCompat.getColorStateList(requireContext(),
                R.color.red
            )
        }

        if (city.isEmpty()){
            isAllCorrect = false
            binding.tilCity.boxStrokeErrorColor = ContextCompat.getColorStateList(requireContext(),
                R.color.red
            )
        }

        if (area.isEmpty()){
            isAllCorrect = false
            binding.tilArea.boxStrokeErrorColor = ContextCompat.getColorStateList(requireContext(),
                R.color.red
            )
        }

        if (house.isEmpty()){
            isAllCorrect = false
            binding.tilHouse.boxStrokeErrorColor = ContextCompat.getColorStateList(requireContext(),
                R.color.red
            )
        }

        if (isAllCorrect){
            val userAddress = Address(
                userName,phoneNumber,pinCode,state,city,house,area,addressType,null)
            userDetailsPref.addAddress(userAddress)
            Log.i("address",userAddress.toString())
        }

    }

    private fun fetchUserSavedAddress(address: Address){
        binding.etFullName.setText(address.name)
        binding.etPhoneNumber.setText(address.phone)
        binding.etPinCode.setText(address.pincode)
        binding.etState.setText(address.state)
        binding.etCity.setText(address.city)
        binding.etArea.setText(address.area)
        binding.etHouse.setText(address.houseNo)
        val addressType = address.type

        if (addressType.contentEquals("Home",true)){
            binding.rbHome.isChecked = true
        }else{
            binding.rbWork.isChecked = true
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}