package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.flipkartclone.R
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Account : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    @Inject
    lateinit var userDetailsPref: UserDetailsPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_account, container, false)

        if (auth.currentUser != null){
            binding.llLogin.visibility = View.GONE
            binding.appBar.visibility = View.VISIBLE
            val userName = userDetailsPref.getUserDetails()?.firstName
            binding.tvUserName.text = "Hey $userName!!"
        }else{
            binding.llLogin.visibility = View.VISIBLE
            binding.appBar.visibility = View.GONE
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            binding.llLogin.visibility = View.VISIBLE
            binding.appBar.visibility = View.GONE
            binding.tvUserName.text = "Log in to get exclusive offers"
            findNavController().navigate(R.id.action_account_to_dashboard)
        }

        binding.tvAccount.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_editProfile)
        }

        binding.tvSavedAddress.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_savedAddress)
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_authBottomSheet)
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}