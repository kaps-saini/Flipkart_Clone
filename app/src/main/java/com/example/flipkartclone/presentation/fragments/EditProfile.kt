package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.flipkartclone.R
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.FragmentEditProfileBinding
import com.example.flipkartclone.domain.models.user.UserDetails
import com.example.flipkartclone.helper.Helpers
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfile : Fragment() {

    private var _binding:FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userDetailsPref: UserDetailsPref
    private val args by navArgs<EditProfileArgs>()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_edit_profile, container, false)

        fillUserDetails()

        binding.tvSubmit.setOnClickListener{
            addUserDetails()
            Helpers.makeSnackBar(requireView(),"Profile updated successfully")
            findNavController().navigate(R.id.action_editProfile_to_dashboard)
        }

        binding.ivBackEditProfile.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvUpdateNumber.setOnClickListener{
            updateMobileNumber()
            Helpers.makeSnackBar(requireView(),"Mobile number updated successfully")
            findNavController().navigateUp()
        }

        binding.tvDeleteAccount.setOnClickListener{
            Helpers.showDeleteAccountDialog(requireContext()){
                auth.currentUser?.delete()?.addOnSuccessListener {
                    userDetailsPref.deleteUserDetails()
                    Helpers.makeSnackBar(requireView(),"Account Deleted")
                    findNavController().navigate(R.id.action_editProfile_to_dashboard)
                }
            }
        }

        return binding.root
    }

    private fun updateMobileNumber() {
        val number = binding.etMobileNumber.text.toString()

        var isAllCorrect = true

        if (number.isEmpty()){
            Helpers.makeSnackBar(requireView(),"Enter mobile Number")
            isAllCorrect = false
        }else if (number.length < 12){
            Helpers.makeSnackBar(requireView(),"Enter correct mobile number")
            isAllCorrect = false
        }
        if (isAllCorrect){
            userDetailsPref.editPhoneNumber(number)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun addUserDetails(){
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val userPhoneNumber = args.phoneNumber

        var isAllFilled = true

        if (firstName.isEmpty()){
            isAllFilled = false
            Helpers.makeSnackBar(requireView(),"Enter first name")
        }else if (lastName.isEmpty()){
            isAllFilled = false
            Helpers.makeSnackBar(requireView(),"Enter last name")
        }

        if (isAllFilled){
            userDetailsPref.editUserDetails(UserDetails(
                firstName = firstName,lastName = lastName, phoneNumber = userPhoneNumber))
        }
    }

    private fun fillUserDetails(){
        val userData = userDetailsPref.getUserDetails()

        if (args.phoneNumber.isNotEmpty()){
            binding.etMobileNumber.setText(args.phoneNumber)
        }

        if (userData != null){
            binding.etFirstName.setText(userData.firstName.toString())
            binding.etLastName.setText(userData.lastName.toString())
            binding.etMobileNumber.setText(userData.phoneNumber.toString())
        }

    }


}