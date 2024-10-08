package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.flipkartclone.R
import com.example.flipkartclone.databinding.FragmentAuthBottomSheetBinding
import com.example.flipkartclone.domain.models.token.ResendTokenData
import com.example.flipkartclone.helper.Helpers
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class AuthBottomSheet : Fragment() {

    private var _binding: FragmentAuthBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var ccp: CountryCodePicker? = null
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_auth_bottom_sheet, container, false)

        hideProgressbar()

        binding.btnContinue.setOnClickListener {
            val num = validateMobileNumber()
            if (num.isNotEmpty()){
                showProgressbar()
                mobileAuth(num)
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun validateMobileNumber():String {
        val mobNum = binding.etMobile.text.toString()
        ccp = binding.ccp

        if (mobNum.isNotEmpty()){
            if (mobNum.length == 10){
                val num = ccp!!.selectedCountryCodeWithPlus + mobNum
                return num.trim()
            }else{
                Helpers.makeSnackBar(requireView(),"Enter a valid phone number")
            }
        }else{
            Helpers.makeSnackBar(requireView(),"Enter phone number")
        }
        return ""
    }

    private fun mobileAuth(phoneNumber:String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.i("TAG", e.message.toString())
                hideProgressbar()
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            hideProgressbar()
            val action = AuthBottomSheetDirections.actionAuthBottomSheetToVerifyOtp(
                phoneNumber = binding.etMobile.text.toString(),
                verificationId = verificationId,
                ResendTokenData(token)
            )
            findNavController().navigate(action)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    findNavController().navigateUp()
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Helpers.makeSnackBar(requireView(),"Something went wrong")
                    }
                    // Update UI
                }
            }
    }

    private fun showProgressbar(){
        binding.btnContinue.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressbar(){
        binding.btnContinue.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

}