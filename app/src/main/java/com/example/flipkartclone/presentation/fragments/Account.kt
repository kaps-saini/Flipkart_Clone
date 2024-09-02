package com.example.flipkartclone.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.flipkartclone.AuthBottomSheet
import com.example.flipkartclone.R
import com.example.flipkartclone.databinding.FragmentAccountBinding
import com.example.flipkartclone.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Account : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_account, container, false)


        binding.btnLogin.setOnClickListener {
            showBottomSheet()
        }


        return binding.root
    }

    private fun showBottomSheet() {
        val bottomSheet = AuthBottomSheet()
        bottomSheet.show(parentFragmentManager,"FullBottomSheet")
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}