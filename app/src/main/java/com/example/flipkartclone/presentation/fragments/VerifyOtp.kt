package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.flipkartclone.R
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.FragmentVerifyOtpBinding
import com.example.flipkartclone.domain.models.token.ResendTokenData
import com.example.flipkartclone.helper.Helpers
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class VerifyOtp : Fragment() {

    private var _binding: FragmentVerifyOtpBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<VerifyOtpArgs>()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var phoneNumber: String
    private lateinit var resendOtpToken: ResendTokenData
    private lateinit var verifyId: String

    @Inject
    lateinit var userDetailsPref: UserDetailsPref

    private var resendOtpHandler: Handler? = null
    private var resendOtpRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_verify_otp, container, false)

        phoneNumber = args.phoneNumber
        verifyId = args.verificationId
        resendOtpToken = args.resendToken

        hideProgressbar()
        editTextListener()
        resendOtpVisibility()
        binding.otpDigit1.requestFocus()

        binding.confirmOtpButton.setOnClickListener {
            val otp =
                binding.otpDigit1.text.toString() + binding.otpDigit2.text.toString() + binding.otpDigit3.text.toString() +
                        binding.otpDigit4.text.toString() + binding.otpDigit5.text.toString() + binding.otpDigit6.text.toString()

            if (otp.isNotEmpty()) {
                if (otp.length == 6) {
                    showProgressbar()
                    val credential = PhoneAuthProvider.getCredential(verifyId, otp)
                    signInWithPhoneAuthCredential(credential)
                }
            }
        }

        binding.resendOtpTextView.setOnClickListener {
            resendToken()
            resendOtpVisibility()
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        resendOtpHandler?.removeCallbacks(resendOtpRunnable!!)
        resendOtpHandler = null
        resendOtpRunnable = null
    }

    private fun resendOtpVisibility() {
        // Clear OTP fields
        binding.otpDigit1.setText("")
        binding.otpDigit2.setText("")
        binding.otpDigit3.setText("")
        binding.otpDigit4.setText("")
        binding.otpDigit5.setText("")
        binding.otpDigit6.setText("")

        // Disable the resend OTP text view
        binding.resendOtpTextView.visibility = View.INVISIBLE
        binding.resendOtpTextView.isEnabled = false

        // Initialize Handler and Runnable
        resendOtpHandler = Handler(Looper.myLooper()!!)
        resendOtpRunnable = Runnable {
            binding.resendOtpTextView.visibility = View.VISIBLE
            binding.resendOtpTextView.isEnabled = true
        }

        // Post the Runnable with a delay
        resendOtpHandler?.postDelayed(resendOtpRunnable!!, 6000)
    }

    private fun editTextListener() {
        binding.otpDigit1.addTextChangedListener(otpFocusShift(binding.otpDigit1))
        binding.otpDigit2.addTextChangedListener(otpFocusShift(binding.otpDigit2))
        binding.otpDigit3.addTextChangedListener(otpFocusShift(binding.otpDigit3))
        binding.otpDigit4.addTextChangedListener(otpFocusShift(binding.otpDigit4))
        binding.otpDigit5.addTextChangedListener(otpFocusShift(binding.otpDigit5))
        binding.otpDigit6.addTextChangedListener(otpFocusShift(binding.otpDigit6))
    }

    inner class otpFocusShift(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            when (view.id) {
                R.id.otpDigit1 -> if (text.length == 1) {
                    binding.otpDigit2.requestFocus()
                }

                R.id.otpDigit2 -> if (text.length == 1) {
                    binding.otpDigit3.requestFocus()
                } else if (text.isEmpty()) {
                    binding.otpDigit1.requestFocus()
                }

                R.id.otpDigit3 -> if (text.length == 1) {
                    binding.otpDigit4.requestFocus()
                } else if (text.isEmpty()) {
                    binding.otpDigit2.requestFocus()
                }

                R.id.otpDigit4 -> if (text.length == 1) {
                    binding.otpDigit5.requestFocus()
                } else if (text.isEmpty()) {
                    binding.otpDigit3.requestFocus()
                }

                R.id.otpDigit5 -> if (text.length == 1) {
                    binding.otpDigit6.requestFocus()
                } else if (text.isEmpty()) {
                    binding.otpDigit4.requestFocus()
                }

                R.id.otpDigit6 -> if (text.isEmpty()) {
                    binding.otpDigit5.requestFocus()
                }
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        showProgressbar()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    val phoneNumber = user?.phoneNumber

                    if (phoneNumber != null) {
                        if (userDetailsPref.doesUserExist(phoneNumber)){
                            hideProgressbar()
                            findNavController().navigate(R.id.action_verifyOtp_to_dashboard)
                        }else{
                            hideProgressbar()
                            val destination =
                                VerifyOtpDirections.actionVerifyOtpToEditProfile(phoneNumber)
                            findNavController().navigate(destination)
                        }
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        hideProgressbar()
                        Helpers.makeSnackBar(requireView(), "Invalid verification code")
                    }
                    // Update UI
                }
            }
    }

    private fun resendToken() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendOtpToken.token)
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

            verifyId = verificationId
            resendOtpToken = ResendTokenData(token)
        }
    }

    private fun showProgressbar(){
        binding.confirmOtpButton.visibility = View.GONE
        binding.pbOtpVerify.visibility = View.VISIBLE
    }

    private fun hideProgressbar(){
        binding.confirmOtpButton.visibility = View.VISIBLE
        binding.pbOtpVerify.visibility = View.GONE
    }
}